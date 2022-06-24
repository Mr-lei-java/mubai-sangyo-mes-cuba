package com.company.sangyo.core.order;

import com.alibaba.fastjson.JSON;
import com.company.sangyo.core.BaseResult;
import com.company.sangyo.core.NativeQueryService;
import com.company.sangyo.core.PageInfo;
import com.company.sangyo.core.kanban.QualifiedRateService;
import com.company.sangyo.core.production.WorkOrderService;
import com.company.sangyo.dto.OrderDTO;
import com.company.sangyo.entity.basic.CustomerPartNumber;
import com.company.sangyo.entity.basic.CustomerPartNumberAndinPlantPartNumber;
import com.company.sangyo.entity.basic.GeneralState;
import com.company.sangyo.entity.basic.InPlantPartNumber;
import com.company.sangyo.entity.order.Order;
import com.company.sangyo.entity.order.OrderAndCustomerPartNumber;
import com.company.sangyo.entity.order.OrderNature;
import com.company.sangyo.entity.order.OrderStatusEnumeration;
import com.company.sangyo.entity.production.ShipStatusEnum;
import com.company.sangyo.entity.production.WorkOrder;
import com.company.sangyo.entity.production.WorkOrderStatus;
import com.company.sangyo.event.OrderScreenCloseEvent;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.LoadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service(OrderService.NAME)
public class OrderServiceBean implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceBean.class);

    @Inject
    private Persistence persistence;

    @Inject
    private WorkOrderService workOrderService;

    @Inject
    private DataManager dataManager;

    @Inject
    private NativeQueryService nativeQueryService;

    @Inject
    private Events events;

    @Inject
    private QualifiedRateService qualifiedRateService;

    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String placeOrder(Order order) {
        List<OrderAndCustomerPartNumber> list = order.getOrderAndCustomerPartNumber();
        if (list == null) {
            return "没有添加客户料号";
        }
        for (OrderAndCustomerPartNumber orderAndCustomerPartNumber : list) {
            CustomerPartNumber customerPartNumberId = orderAndCustomerPartNumber.getCustomerPartNumberId();
            List<CustomerPartNumberAndinPlantPartNumber> customerPartNumberAndinPlantPartNumber = customerPartNumberId.getCustomerPartNumberAndinPlantPartNumber();
            if (customerPartNumberAndinPlantPartNumber == null) {
                return "没有添加厂内料号";
            }
            for (CustomerPartNumberAndinPlantPartNumber partNumberAndinPlantPartNumber : customerPartNumberAndinPlantPartNumber) {
                WorkOrder workOrder = new WorkOrder();
                Transaction transaction = persistence.createTransaction();
                try {
                    EntityManager entityManager = persistence.getEntityManager();
                    workOrder.setOrderId(order);
                    workOrder.setInPlantPartNumberId(partNumberAndinPlantPartNumber.getInPlantPartNumber()
                            == null ? new InPlantPartNumber() : partNumberAndinPlantPartNumber.getInPlantPartNumber());
                    workOrder.setOrderProductionQuantity((orderAndCustomerPartNumber.getOrderQuantity()
                            == null) ? 0 : orderAndCustomerPartNumber.getOrderQuantity());
                    workOrder.setDefaultOrderProductionQuantity((orderAndCustomerPartNumber.getOrderQuantity()
                            == null) ? 0 : orderAndCustomerPartNumber.getOrderQuantity());
                    workOrder.setName(workOrderService.getInName() + 1);
                    workOrder.setDistribute(LocalDateTime.now());
                    workOrder.setDeliveryTime(order.getDeliveryTime());
                    entityManager.merge(workOrder);
                    transaction.commit();
                } catch (Throwable e) {
                    log.error("Error", e);
                    return "下发失败";
                } finally {
                    transaction.end();
                }
            }
        }
        return updateOrder(order);
    }

    /**
     * 更新订单状态
     *
     * @param order 订单
     * @return
     */
    private String updateOrder(Order order) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            order.setOrderStatus(OrderStatusEnumeration.ISSUED);
            entityManager.merge(order);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
            return "下发失败";
        } finally {
            transaction.end();
        }
        return "下发成功";
    }

    @Override
    public String genInPlantOrder() {
        try (Transaction tx = persistence.createTransaction()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = sdf.format(new Date());
            String inPlantOrder = getMaxInPlantOrder(dateStr);
            if (inPlantOrder == null) {
                inPlantOrder = dateStr + "0001";
            } else {
                Integer count = Integer.parseInt(inPlantOrder.substring(8));
                DecimalFormat f = new DecimalFormat("0000");
                count++;
                inPlantOrder = dateStr + f.format(count);
            }
            return inPlantOrder;
        } catch (Exception e) {
            log.error("Error", e);
            return null;
        }
    }

    public String getMaxInPlantOrder(String dateStr) {
        EntityManager entityManager;
        try (Transaction tx = persistence.createTransaction()) {
            entityManager = persistence.getEntityManager();
            String sql = "SELECT max(in_plant_order) FROM sangyo_ORDER WHERE delete_ts IS NULL and" +
                    " in_plant_order like '" + dateStr + "%'";
            Query nativeQuery = entityManager.createNativeQuery(sql);
            return (String) nativeQuery.getFirstResult();
        }
    }

    @Override
    public BaseResult<String> getAllCusMnByOrderId(String orderId) {
        ArrayList arrayList = new ArrayList();
        try {
            Order order = getOrderById("select e from sangyo_Order  e where  e.id=:orderId",
                    "orderId", UUID.fromString(orderId));
            if (order == null) {
                return new BaseResult<>("424", "无法获取数据");
            }
            List<OrderAndCustomerPartNumber> orderAndCustomerPartNumber = order.getOrderAndCustomerPartNumber();
            for (OrderAndCustomerPartNumber andCustomerPartNumber : orderAndCustomerPartNumber) {
                Map<String, Object> CustomerPartNumberMap = new HashMap();
                ArrayList arrayListInPlantPartNumberMap = new ArrayList();
                CustomerPartNumber customerPartNumberId = andCustomerPartNumber.getCustomerPartNumberId();
                CustomerPartNumber customerPartNumber = getCustomerPartNumberById(
                        "select e from sangyo_CustomerPartNumber  e where  e.id=:customerPartNumberId",
                        "customerPartNumberId", customerPartNumberId.getId());
                List<CustomerPartNumberAndinPlantPartNumber> customerPartNumberAndinPlantPartNumber
                        = getCustomerPartNumberAndinPlantPartNumbers(customerPartNumber);
                boolean isIssued = true;
                for (CustomerPartNumberAndinPlantPartNumber partNumberAndinPlantPartNumber : customerPartNumberAndinPlantPartNumber) {
                    Map<String, Object> InPlantPartNumberMap = new HashMap();
                    InPlantPartNumber inPlantPartNumber = partNumberAndinPlantPartNumber.getInPlantPartNumber();
                    WorkOrder workOrder = getWorkOrder(orderId, inPlantPartNumber);

                    InPlantPartNumber inPlantPartNumber1 = getInPlantPartNumberById(
                            "select e from sangyo_InPlantPartNumber  e where  e.id=:inPlantPartNumber",
                            "inPlantPartNumber", inPlantPartNumber.getId());
                    log.info(inPlantPartNumber1 + "inPlantPartNumber1inPlantPartNumber1inPlantPartNumber1");
                    InPlantPartNumberMap.put("cusMaterialNumberId", customerPartNumber.getId());//       cusMaterialNumberId: 60000052
                    InPlantPartNumberMap.put("icRelId", "");//             icRelId: 210000583
                    InPlantPartNumberMap.put("imnCount", "");//            imnCount: 0
                    if (workOrder != null && workOrder.getOrderProductionQuantity() != null) {
                        InPlantPartNumberMap.put("imnOrderCount", workOrder.getOrderProductionQuantity());//       imnOrderCount 订单下发数量: 0
                        if (workOrder.getDefaultOrderProductionQuantity()!=null){
                            InPlantPartNumberMap.put("imnOrderCount", workOrder.getDefaultOrderProductionQuantity());//       imnOrderCount 订单下发数量: 0
                        }
                        isIssued = false;
                    }
                    if (workOrder == null) {
                        InPlantPartNumberMap.put("imnOrderCount", "");//       imnOrderCount: 0
                    }
                    if (inPlantPartNumber1 != null) {
                        InPlantPartNumberMap.put("imnStatus", inPlantPartNumber1.getStatus().equals(GeneralState.ENABLE) ? "启用" : "禁用");//           imnStatus: "启用"
                    }
                    if (inPlantPartNumber1 != null) {
                        InPlantPartNumberMap.put("imnType", inPlantPartNumber1.getItemNature());//             imnType: "量产"
                    }
                    if (inPlantPartNumber1 == null) {
                        InPlantPartNumberMap.put("imnType", "");//             imnType: "量产"
                    }
                    InPlantPartNumberMap.put("innerMaterialNumberId", inPlantPartNumber1 != null ? inPlantPartNumber1.getUuid() : "");//      innerMaterialNumberId: 50000480
                    InPlantPartNumberMap.put("innerMaterialNumberStr", inPlantPartNumber1 != null ? inPlantPartNumber1.getInPlantPartNumberName() : "");//     innerMaterialNumberStr: "503-51-无网结-450/400-16-22"
                    InPlantPartNumberMap.put("isDelete", "no");//                   isDelete: "no"

                    InPlantPartNumberMap.put("planProductCountNewAdd", "");//     planProductCountNewAdd: 0
                    InPlantPartNumberMap.put("remark", inPlantPartNumber1 != null ? inPlantPartNumber1.getRemarks() : "");//                     remark: "天合东区976无网结实验123乳剂 方案一"
                    arrayListInPlantPartNumberMap.add(InPlantPartNumberMap);
                }
                if (customerPartNumber != null) {
                    CustomerPartNumberMap.put("cmnType", customerPartNumber.getCustomerPartNumberNature());//              cmnType: "量产"
                }
                if (customerPartNumber == null) {
                    CustomerPartNumberMap.put("cmnType", "");//   cmnType: "量产"
                }
                CustomerPartNumberMap.put("cusMaterialNumberId", customerPartNumber != null ? customerPartNumber.getId() : "");//      cusMaterialNumberId: 60000052
                CustomerPartNumberMap.put("cusMaterialNumberStr", customerPartNumber != null ? customerPartNumber.getName() : "");//     cusMaterialNumberStr: "7A000967"
                CustomerPartNumberMap.put("isDelete", "no");//             isDelete: "no"
                CustomerPartNumberMap.put("listRel", arrayListInPlantPartNumberMap);//              listRel: [{icRelId: 210000583, innerMaterialNumberId: 50000480,…},…]
                CustomerPartNumberMap.put("isIssued", isIssued);//                   isDelete: "no"
                CustomerPartNumberMap.put("orderId", order.getId());//              orderId: 140003906
                CustomerPartNumberMap.put("orderProductCount", andCustomerPartNumber.getOrderQuantity());//         orderProductCount: 10
                if (order.getOrderType().equals(OrderNature.INPLANTEXPERIMENT)) {
                    CustomerPartNumberMap.put("orderType", true);//          orderType:实验订单
                }
                if (!order.getOrderType().equals(OrderNature.INPLANTEXPERIMENT)) {
                    CustomerPartNumberMap.put("orderType", false);//
                }
//                CustomerPartNumberMap.put("orderAndCustomerPartNumberId", andCustomerPartNumber.getId());//订单与客户料号id
                CustomerPartNumberMap.put("ordersCusMnRelId", "");  //ordersCusMnRelId: 230005054
                CustomerPartNumberMap.put("remark", customerPartNumber != null ? customerPartNumber.getRemark() : "");//               remark: null
                arrayList.add(CustomerPartNumberMap);
            }
        } catch (Throwable e) {
            log.error("Error", e);
            return new BaseResult<>("424", "无法获取数据");
        }
        return new BaseResult<>("200", "success", arrayList);
    }

    private List<CustomerPartNumberAndinPlantPartNumber> getCustomerPartNumberAndinPlantPartNumbers(CustomerPartNumber customerPartNumber) {
        LoadContext<CustomerPartNumberAndinPlantPartNumber> customerPartNumberAndinPlantPartNumberLoadContext = LoadContext.create(CustomerPartNumberAndinPlantPartNumber.class).setQuery(
                LoadContext.createQuery("select e from sangyo_CustomerPartNumberAndinPlantPartNumber  e where e.customerPartNumber=:customerPartNumber")
                        .setParameter("customerPartNumber", customerPartNumber != null ? customerPartNumber : null)
        ).setView("customerPartNumberAndinPlantPartNumber-view");

        return dataManager.loadList(customerPartNumberAndinPlantPartNumberLoadContext);
    }

    private WorkOrder getWorkOrder(String orderId, InPlantPartNumber inPlantPartNumber) {
        LoadContext<WorkOrder> workOrderLoadContext = LoadContext.create(WorkOrder.class).setQuery(
                LoadContext.createQuery("select e from sangyo_WorkOrder  e " +
                        "where  e.inPlantPartNumberId.id=:inPlantPartNumber and e.orderId.id=:order")
                        .setParameter("inPlantPartNumber", inPlantPartNumber.getId())
                        .setParameter("order", UUID.fromString(orderId))
        ).setView("workOrder-view");
        return dataManager.load(workOrderLoadContext);
    }


    @Override
    public BaseResult<String> distributeOrder(String orderId, String list, String userName) {
        String updateOrder = null;
        List<Object> list1 = JSON.parseArray(list);
        Transaction transaction = persistence.createTransaction();
        try {
            Order order = getOrderById("select e from sangyo_Order  e where  e.id=:orderId",
                    "orderId", UUID.fromString(orderId));
            if (order != null && !order.getOrderStatus().equals(OrderStatusEnumeration.CREATED)) {
                return new BaseResult<>("424", "已下发,无法再次下发");
            }
            for (Object object : list1) {
                Map<String, Object> ret = (Map<String, Object>) object;//取出list里面的值转为map
                String customerPartNumber = (String) ret.get("customerPartNumberId");
                int orderQuantity = Integer.parseInt((String) ret.get("orderQuantity"));
                String inPlantPartNumber = (String) ret.get("inPlantPartNumberId");
//                String orderAndCustomerPartNumberId = (String) ret.get("inPlantPartNumberId");
                CustomerPartNumber customerPartNumber1 = getCustomerPartNumberById("select e from sangyo_CustomerPartNumber  e where  e.id=:customerPartNumber",
                        "customerPartNumber", UUID.fromString(customerPartNumber));
                OrderAndCustomerPartNumber orderAndCustomerPartNumber = getOrderAndCustomerPartNumber(orderId, customerPartNumber);
//                OrderAndCustomerPartNumber orderAndCustomerPartNumber1 = getOrderAndCustomerPartNumber(orderAndCustomerPartNumberId);
                InPlantPartNumber inPlantPartNumber1 = getInPlantPartNumberById("select e from sangyo_InPlantPartNumber  e where  e.id=:inPlantPartNumber",
                        "inPlantPartNumber", UUID.fromString(inPlantPartNumber));
                if (orderQuantity <= 0 && order != null && order.getOrderType() != null && orderQuantity == 0 && order.getOrderType().equals(OrderNature.INPLANTEXPERIMENT)) {
                    newWorkOrder(order, inPlantPartNumber1, orderQuantity, customerPartNumber1, orderAndCustomerPartNumber.getConsignDate(),orderAndCustomerPartNumber.getInPlantPartConsignDate());
//                    newWorkOrder(order, inPlantPartNumber1, orderQuantity, customerPartNumber1, orderAndCustomerPartNumber1.getConsignDate(),orderAndCustomerPartNumber1.getInPlantPartConsignDate());
                    log.info(orderQuantity + "创建成功");
                }
                if (orderQuantity > 0) {
                    newWorkOrder(order, inPlantPartNumber1, orderQuantity, customerPartNumber1, orderAndCustomerPartNumber.getConsignDate(),orderAndCustomerPartNumber.getInPlantPartConsignDate());
//                    newWorkOrder(order, inPlantPartNumber1, orderQuantity, customerPartNumber1, orderAndCustomerPartNumber1.getConsignDate(),orderAndCustomerPartNumber1.getInPlantPartConsignDate());
                }
            }
            updateOrder = updateOrder(order);
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
            events.publish(new OrderScreenCloseEvent(this, orderId, userName));
        }
        return new BaseResult<>("200", updateOrder);
    }

    private OrderAndCustomerPartNumber getOrderAndCustomerPartNumber(String orderId, String customerPartNumber) {
        LoadContext<OrderAndCustomerPartNumber> orderAndCustomerPartNumberLoadContext = LoadContext.create(OrderAndCustomerPartNumber.class).setQuery(
                LoadContext.createQuery("select e from sangyo_OrderAndCustomerPartNumber  e " +
                        "where  e.customerPartNumberId.id = :customerPartNumber and e.order.id = :orderId")
                        .setParameter("customerPartNumber", UUID.fromString(customerPartNumber)).setParameter("orderId", UUID.fromString(orderId))
        ).setView("orderAndCustomerPartNumber-view");
        return dataManager.load(orderAndCustomerPartNumberLoadContext);
    }

    private OrderAndCustomerPartNumber getOrderAndCustomerPartNumber(String orderAndCustomerPartNumber1) {
        LoadContext<OrderAndCustomerPartNumber> orderAndCustomerPartNumberLoadContext = LoadContext.create(OrderAndCustomerPartNumber.class).setQuery(
                LoadContext.createQuery("select e from sangyo_OrderAndCustomerPartNumber  e " +
                        "where  e.id = :orderAndCustomerPartNumber")
                        .setParameter("orderAndCustomerPartNumber", UUID.fromString(orderAndCustomerPartNumber1))
        ).setView("orderAndCustomerPartNumber-view");
        return dataManager.load(orderAndCustomerPartNumberLoadContext);
    }

    private CustomerPartNumber getCustomerPartNumberById(String s, String customerPartNumber2, UUID uuid) {
        LoadContext<CustomerPartNumber> customerPartNumberLoadContext = LoadContext.create(CustomerPartNumber.class).setQuery(
                LoadContext.createQuery(s)
                        .setParameter(customerPartNumber2, uuid)
        ).setView("customerPartNumber-view");
        return dataManager.load(customerPartNumberLoadContext);
    }

    private Order getOrderById(String s, String orderId2, UUID uuid) {
        LoadContext<Order> loadContext = LoadContext.create(Order.class).setQuery(
                LoadContext.createQuery(s)
                        .setParameter(orderId2, uuid)
        ).setView("order-view");
        return dataManager.load(loadContext);
    }

    private void newWorkOrder(Order order, InPlantPartNumber inPlantPartNumber1,
                              Integer orderQuantity, CustomerPartNumber customerPartNumber1,
                              String consignDate,String inPlantConsignDate) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            WorkOrder workOrder = new WorkOrder();
            workOrder.setOrderId(order);
            workOrder.setOrderPriority(order == null ?null :order.getOrderPriority());
            workOrder.setInPlantPartNumberId(inPlantPartNumber1);
            workOrder.setOrderProductionQuantity(orderQuantity);
            workOrder.setDefaultOrderProductionQuantity(orderQuantity);
            workOrder.setCustomerPartNumber(customerPartNumber1);
            workOrder.setDistribute(LocalDateTime.now());
            if (consignDate != null) {
                consignDate = consignDate + " 00:00:00";
                LocalDateTime ldt = LocalDateTime.parse(consignDate, df);
                workOrder.setDeliveryTime(ldt);
                workOrder.setInPlantPartConsignDate(inPlantConsignDate);
//                if(InPlantConsignDate!=null){
//                InPlantConsignDate = InPlantConsignDate +" 00:00:00";
//                workOrder.setInPlantConsignDate(InPlantConsignDate);
//                }
            }
            workOrder.setName(workOrderService.getInName() != null ? workOrderService.getInName() : "");
            if (order.getOrderType().equals(OrderNature.MARKETORDES)) {
                workOrder.setShipStatus(ShipStatusEnum.NOT_SHIP);
            }
            entityManager.merge(workOrder);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public BaseResult<OrderDTO> getAllOrder(Integer page, Integer limit) {
        LoadContext<Order> loadContext = LoadContext.create(Order.class).setQuery(
                LoadContext.createQuery("select e from sangyo_Order e where e.deleteTs is null")
                        .setFirstResult((page - 1) * limit + 1).setMaxResults(limit)
        ).setView("order-view");
        List<Order> orders = dataManager.loadList(loadContext);
        List<OrderDTO> orderDTOS = new ArrayList<OrderDTO>();
        orders.forEach(item -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setCode(item.getOrderCode());
                    orderDTO.setName(item.getInPlantOrder());
                    orderDTOS.add(orderDTO);
                }
        );
        PageInfo<OrderDTO> pageInfo = new PageInfo<OrderDTO>(page, limit, 10, orderDTOS);
        return new BaseResult<OrderDTO>("200", "success", pageInfo);
    }

    @Override
    public void updateShipStatusOrder(WorkOrder workOrder) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            Order orders = getOrderById("select e from sangyo_Order e where e.id =:id",
                    "id", workOrder.getOrderId().getId());
            if (orders != null) {
                orders.setOrderStatus(OrderStatusEnumeration.OUTWAREHOUSEOFDELIVERY);
                entityManager.merge(orders);
                transaction.commit();
            }
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public void updateDeliverStatusOrder(WorkOrder workOrder) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            boolean updateDeliverStatusOrder = true;
            List<WorkOrder> workOrders = getWorkOrders(workOrder.getOrderId(),
                    "select e from sangyo_WorkOrder e where e.orderId =:orderId",
                    "orderId");
            for (WorkOrder workOrder1 : workOrders) {
                if (!workOrder1.getStatus().equals(WorkOrderStatus.COMPLETED)) {
                    updateDeliverStatusOrder = false;
                }
            }
            if (updateDeliverStatusOrder) {
                Order orders = getOrderById("select e from sangyo_Order e where e.id =:id",
                        "id", workOrder.getOrderId().getId());
                if (orders != null) {
                    orders.setOrderStatus(OrderStatusEnumeration.WAREHOUSEDELIVERED);
                    entityManager.merge(orders);
                    transaction.commit();
                }
            }
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public Collection<KeyValueEntity> getScheduleOrder(UUID order) {
        Order order1 = getOrderById("select e from sangyo_Order e where e.id=:order",
                "order", order);
        List<WorkOrder> workOrders = getWorkOrders(order1,
                "select e from sangyo_WorkOrder e where e.orderId=:order1 ",
                "order1");
        List entityList = new ArrayList();
        for (WorkOrder workOrder : workOrders) {
            KeyValueEntity keyValueEntity = new KeyValueEntity();
            String dispatchName = workOrder.getName();
            String inPlantPartNumber = "";
            if (workOrder.getCustomerPartNumber() != null) {
                inPlantPartNumber = workOrder.getCustomerPartNumber().getName();
            } else {
                inPlantPartNumber = workOrder.getInPlantPartNumberId().getInPlantPartNumberName();
            }
            keyValueEntity.setValue("stationNum", "工单号:" + dispatchName + "--料号名称:" + inPlantPartNumber);
            String dispatchPlannedQuantity = String.valueOf(workOrder.getCompletedQuantity());
            keyValueEntity.setValue("minutest", dispatchPlannedQuantity);
            entityList.add(keyValueEntity);
        }
        return entityList;
    }

    private List<WorkOrder> getWorkOrders(Order order1, String s, String order12) {
        LoadContext<WorkOrder> workOrderLoadContext = LoadContext.create(WorkOrder.class).setQuery(
                LoadContext.createQuery(s)
                        .setParameter(order12, order1)
        ).setView("workOrder-view");
        return dataManager.loadList(workOrderLoadContext);
    }

    @Override
    public BaseResult<InPlantPartNumber> getInPlantPartNumber(String inPlantPartNumber) {
        List<InPlantPartNumber> screenDTOS = new ArrayList<>();
        Transaction transaction = persistence.createTransaction();
        try {
            InPlantPartNumber inPlantPartNumber1 = getInPlantPartNumberById(
                    "select e from sangyo_InPlantPartNumber e where e.id=:id",
                    "id", UUID.fromString(inPlantPartNumber));
            screenDTOS.add(inPlantPartNumber1);
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
        return new BaseResult<>("200", "success", screenDTOS);
    }

    private InPlantPartNumber getInPlantPartNumberById(String s, String id, UUID uuid) {
        LoadContext<InPlantPartNumber> inPlantPartNumberLoadContext = LoadContext.create(InPlantPartNumber.class).setQuery(
                LoadContext.createQuery(s)
                        .setParameter(id, uuid)
        ).setView("inPlantPartNumber-view");
        return dataManager.load(inPlantPartNumberLoadContext);
    }

    @Override
    public BaseResult<CustomerPartNumber> getCustomerPartNumber(String customerPartNumber) {
        List<CustomerPartNumber> screenDTOS = new ArrayList<>();
        Transaction transaction = persistence.createTransaction();
        try {
            CustomerPartNumber customerPartNumber1 = getCustomerPartNumberById(
                    "select e from sangyo_CustomerPartNumber e where e.id=:id",
                    "id", UUID.fromString(customerPartNumber));
            screenDTOS.add(customerPartNumber1);
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }

        return new BaseResult<>("200", "success", screenDTOS);
    }

    @Override
    public Order getOrderById(UUID orderId) {
        LoadContext<Order> loadContext = LoadContext.create(Order.class).setQuery(
                LoadContext.createQuery("select e from sangyo_Order e where e.id =:orderId")
                        .setParameter("orderId", orderId)
        ).setView("order-view");
        return dataManager.load(loadContext);
    }

    @Override
    public Collection<KeyValueEntity> getUnshippedCollection(String tradeDress, String commodityForm) {
        List<KeyValueEntity> unshippedList = new ArrayList();
        for (int i = 0; i < 5; i++) {
            commodityForm = getCommodityForm(tradeDress, commodityForm, unshippedList, i);
        }
        return unshippedList;
    }

    private String getCommodityForm(String tradeDress, String commodityForm, List<KeyValueEntity> unshippedList, int i) {
        KeyValueEntity keyValueEntity = new KeyValueEntity();
        String stationNum = "";
        String sql = "";
        /**
         * 1.已超过交货时间：交货日期小于今天
         * 2.当天交货：交货日期等于今天
         * 3.一天到两天：交货日期大于今天，小于等于今天+2
         * 4.三到四天：交货日期大于今天+2，小于等于今天+4
         * 5.四天以后：交货日期大于今天+4
         */
        switch (i) {
            case 0:
                stationNum = "已超过交货时间数量";
                sql = jointSql("-interval '12 month'", "-interval '1 day'", tradeDress);
                break;
            case 1:
                stationNum = "当天交货数量";
                sql = jointSql("-interval '1 day'", "", tradeDress);
                break;
            case 2:
                stationNum = "一天到两天交货数量";
                sql = jointSql("", "+interval '2 day'", tradeDress);
                break;
            case 3:
                stationNum = "三天到四天交货数量";
                sql = jointSql("+interval '2 day'", "+interval '4 day'", tradeDress);
                break;
            case 4:
                stationNum = "超过四天之后交货数量";
                sql = jointSql("+interval '4 day'", "+interval '12 month'", tradeDress);
                break;
        }
        Object result = nativeQueryService.getSingleResult(sql);
        long quantity = result != null ? (long) result : 0;
        keyValueEntity.setValue("stationNum", stationNum);
        keyValueEntity.setValue("minutest", String.valueOf(quantity));
        if (commodityForm == null) {
            commodityForm = "";
        }
        keyValueEntity.setValue("commodityForm", commodityForm);

        unshippedList.add(keyValueEntity);
        return commodityForm;
    }


    @Override
    public Collection<KeyValueEntity> getUnshippedCollection() {
        List<String> tradeDressList = qualifiedRateService.getAllTradeDress();
        KeyValueEntity keyValueSumEntity = new KeyValueEntity();
        List<KeyValueEntity> unshippedList = new ArrayList();
        String tradeDress = "";
        String sql = "";
        Integer sum = 0;
        for (String s : tradeDressList) {
            KeyValueEntity keyValueEntity = new KeyValueEntity();
            tradeDress = "and yippn.commodity_form = '" + s + "'";
            sql = jointSql("- interval '24 month'", "+interval '24 month'", tradeDress);
            Object result = nativeQueryService.getSingleResult(sql);
            long quantity = result != null ? (long) result : 0;
            if (s == null) {
                s = "";
            }
            if (!String.valueOf(quantity).equals("0")) {
                keyValueEntity.setValue("commodityForm", s);
                keyValueEntity.setValue("minutest", String.valueOf(quantity));
                sum = Math.toIntExact(sum + quantity);
                unshippedList.add(keyValueEntity);
            }
        }
        keyValueSumEntity.setValue("commodityForm", "合计");
        keyValueSumEntity.setValue("minutest", String.valueOf(sum));
        unshippedList.add(keyValueSumEntity);
        return unshippedList;
    }


    /**
     * SELECT SUM
     * ( ywo.order_production_quantity ) - SUM ( ywo.ship_quantity )
     * FROM
     * sangyo_work_order ywo
     * LEFT JOIN sangyo_in_plant_part_number yippn ON yippn.ID = ywo.in_plant_part_number_id_id
     * WHERE
     * ywo.order_id_id IN ( SELECT ID FROM sangyo_order yo WHERE yo.order_type = '销售订单' AND yo.delete_ts IS NULL )
     * AND ywo.delivery_time BETWEEN ( SELECT now( ) - INTERVAL '4 month' )
     * AND ( SELECT now( ) + INTERVAL '12 month' )
     * AND yippn.commodity_form in('镭射-无网结-SP-166','镭射-无网结-SP-166','镭射-主栅-DUP-182','镭射-无网结-SP-166','镭射-主栅-DUP-182')
     * AND ywo.delete_ts IS NULL
     *
     * @return
     */
    private String jointSql(String startTime, String endTime, String tradeDress) {
        String sql = "select sum(ywo.order_production_quantity) - sum(ywo.ship_quantity) from sangyo_work_order ywo LEFT JOIN sangyo_in_plant_part_number yippn ON yippn.ID = ywo.in_plant_part_number_id_id " +
                "where ywo.order_id_id in(select id from sangyo_order yo where yo.order_type = '销售订单' and yo.delete_ts is null) " +
                tradeDress +
                "AND ywo.delivery_time BETWEEN ( SELECT now( ) " + startTime + ")" +
                "AND ( SELECT now( ) " + endTime + " ) and ywo.delete_ts is null and ywo.ship_status !='已出仓'";
        return sql;
    }
}
