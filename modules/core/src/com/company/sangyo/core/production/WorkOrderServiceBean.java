package com.company.sangyo.core.production;

import com.company.sangyo.config.NewConfig;
import com.company.sangyo.core.CheckService;
import com.company.sangyo.core.order.OrderService;
import com.company.sangyo.entity.basic.*;
import com.company.sangyo.entity.order.Order;
import com.company.sangyo.entity.order.OrderNature;
import com.company.sangyo.entity.order.OrderStatusEnumeration;
import com.company.sangyo.entity.production.*;
import com.company.sangyo.entity.storage.*;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.company.sangyo.common.ThreadPoolExecutors;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang3.StringUtils;
import com.company.sangyo.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service(WorkOrderService.NAME)
public class WorkOrderServiceBean implements WorkOrderService {
    private static final Logger log = LoggerFactory.getLogger(WorkOrderServiceBean.class);

    @Inject
    private DataManager dataManager;

    @Inject
    private Persistence persistence;

    @Inject
    private QualityTraceabilityService qualityTraceabilityService;

    @Inject
    private CheckService checkService;

    @Inject
    private DispatchListService dispatchListService;

    @Inject
    private OrderService orderService;

//    @Inject
//    private ERPService erpService;

    @Inject
    private UnqualifiedService unqualifiedService;

    @Inject
    private NewConfig newConfig;


    @Override
    public String getInName() {
        List<WorkOrder> producedOrderList = getWorkOrderList();
        if (producedOrderList.size() == 0) {
            return 0 + "";
        }
        List<Long> longList = new ArrayList();
        for (WorkOrder workOrder : producedOrderList) {
            longList.add(Long.parseLong(workOrder.getName()));
        }
        return String.valueOf(Collections.max(longList) + 1);
    }

    private List<WorkOrder> getWorkOrderList() {
        LoadContext<WorkOrder> loadContext = LoadContext.create(WorkOrder.class).setQuery(
                LoadContext.createQuery("select e from sangyo_WorkOrder e " +
                        "where e.isSupplement = false order by e.name desc")
        );
        return dataManager.loadList(loadContext);
    }

    /**
     * 按库别给网版分组
     *
     * @param screenList
     * @return
     */
    private Collection<List<Semi>> getGroupByWarehouse(Set<Semi> screenList) {
        Map<Warehouse, List<Semi>> map = new HashMap<>();
        screenList.forEach(tmp -> {
            Warehouse warehouse = tmp.getWarehouseId();
            if (map.containsKey(warehouse)) {
                map.get(warehouse).add(tmp);
            }
            if (!map.containsKey(warehouse)) {
                List<Semi> screenList1 = new ArrayList<>();
                screenList1.add(tmp);
                map.put(warehouse, screenList1);
            }
        });
        return map.values();
    }

    @Override
    public String distribute(WorkOrder workOrder1, Set<Semi> semis, Set<Finished> finisheds, Integer planQuantity, User user) {
        //出库批次号
        String outWorkOrderName = "";
        //出库料号
        String inPlantParNumber = "";
        //成品仓入库料号
        String Warehousing = "";
        //成品仓入库批次号
        String inWorkOrderName = "";
        WorkOrder workOrder = getWorkOrderById(workOrder1.getId(), "select e from sangyo_WorkOrder e where e.id =:id", "workOrder-view");
        if (workOrder != null) {
            Warehousing = workOrder.getInPlantPartNumberId().getInventoryCoding();
            inWorkOrderName = workOrder.getName();
            if (workOrder.getProcessFlowId() == null) {
                //没有设置工艺流程
                return "未设置工艺流程";
            }
        }
        if (workOrder == null) {
            return "";
        }
        List<ProcessFlowDetailsAndProcedure> procedures = getProcessFlowDetailsAndProcedures(workOrder);

        if (procedures.size() == 0) {
            //工艺流程下没有配置工序
            return "工艺流程下未配置工序";
        }
        if (workOrder.getPlanProductionQuantity() == null) {
            return "未填写计划生产数量";
        }
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            if (workOrder.getProcessFlowId().getWhethertartWithFirstProcessProcess()) {
                //创建派工单
                procedures.forEach(procedure -> {
                            DispatchList dispatchList = new DispatchList();
                            dispatchList.setWorkOrder(workOrder);
                            dispatchList.setProcedure(procedure.getProcedure());
                            dispatchList.setPlannedQuantity(planQuantity);
                            dispatchList.setName(workOrder.getName() + procedure.getProcedure().getName());
                            dispatchList.setType(DispatchListType.NORMAL);
                            dispatchList.setIssueTime(LocalDateTime.now());
                            dispatchList.setAccomplishQuantity(0);
                            dispatchList.setReceiveCount(0);
                            entityManager.merge(dispatchList);
                        }
                );
                //更新工单状态
                workOrder.setPlanProductionQuantity(planQuantity);
                workOrder.setQuantityIssued(planQuantity);
                workOrder.setDistributedStatus(DistributedStatus.DISTRIBUTED);
                workOrder.setShipStatus(ShipStatusEnum.NOT_SHIP);
                entityManager.merge(workOrder);
            } else {
                //循环判读 成品仓网版是否同步ERP数据
                for (Finished finished : finisheds) {
                    //料号
                    inPlantParNumber = getString(inPlantParNumber, finished);
                    //工单编号
                    outWorkOrderName = finished.getWorkOrder().getName();
                    //材料出库
                    getERPMaterialOut(outWorkOrderName, inPlantParNumber, finished);
                    //材料入库
                    getERPMaterialStorage(inWorkOrderName, Warehousing, finished);
                }
                //设置成品仓中的配单网版状态，配属工单号和状态
                finisheds.stream().forEach(item -> {
                    WorkOrderAndScreen preWorkOrderAndScreen = getWorkOrderAndScreen(item.getScreenId());
                    if (preWorkOrderAndScreen != null) {
                        //更改上一工单的配属工单为当前工单
                        preWorkOrderAndScreen.setNextWorkOrder(workOrder);
                        preWorkOrderAndScreen.setWorkOrderScreenStatus("被配走");
                        entityManager.merge(preWorkOrderAndScreen);
                    }

                    //成品仓状态改变
                    item.setRelWorkOrder(item.getWorkOrder());
                    item.setStatus(FinishedScreenStatus.NOT_OUT_WAREHOUSE);
                    item.setOperatingPersonnel(user);
                    item.setWorkOrder(workOrder);
                    item.setWarehousingTime(LocalDateTime.now());
                    item.setInPlantPartNumber(workOrder.getInPlantPartNumberId());
                    //网版改变
                    item.getScreenId().setScreenStatus(ScreenStatusEnum.NOTOUT);
                    item.getScreenId().setWorkOrder(workOrder);
                    //创建工单与网版的关联表
                    WorkOrderAndScreen workOrderAndScreen = new WorkOrderAndScreen();
                    workOrderAndScreen.setWorkOrder(workOrder);
                    //设置当前工单的前置工单为网版的当前工单
                    workOrderAndScreen.setPreWorkOrder(item.getScreenId().getWorkOrder());
                    //TODO 成品仓配单 网版与工单状态
                    workOrderAndScreen.setWorkOrderScreenStatus("生产完成");

                    workOrderAndScreen.setScreen(item.getScreenId());
                    workOrderAndScreen.setIsDistributed(true);

                    entityManager.merge(workOrderAndScreen);
                    entityManager.merge(item);
                    entityManager.merge(item.getScreenId());
                });

                procedures.stream().forEach(processFlowDetailsAndProcedure -> {
                    DispatchList dispatchList = null;
                    dispatchList = getDispatchList(workOrder, processFlowDetailsAndProcedure, "select e from sangyo_DispatchList e", " where e.workOrder =:workOrder and e.procedure =:procedure and e.type =:type");
                    if (dispatchList == null) {
                        dispatchList = getDispatchList(planQuantity, workOrder, processFlowDetailsAndProcedure);
                        dispatchListService.createDispatchList(dispatchList);
                    }
                });

                //先要对网版按来源库进行分组 成品库、不同库别中转库
                for (List<Semi> semiList : getGroupByWarehouse(semis)) {
                    semiList.stream().forEach(semi -> {
                        WorkOrderAndScreen preWorkOrderAndScreen = getWorkOrderAndScreen(semi.getScreenId());
                        if (preWorkOrderAndScreen != null) {
                            //更改上一工单的配属工单为当前工单
                            preWorkOrderAndScreen.setNextWorkOrder(workOrder);
                            preWorkOrderAndScreen.setWorkOrderScreenStatus("被配走");
                            entityManager.merge(preWorkOrderAndScreen);
                        }

                        WorkOrderAndScreen workOrderAndScreen = new WorkOrderAndScreen();
                        workOrderAndScreen.setWorkOrder(workOrder);
                        //设置当前工单的前置工单为网版的当前工单
                        workOrderAndScreen.setPreWorkOrder(semi.getScreenId().getWorkOrder());
                        //TODO 中转库配单
                        workOrderAndScreen.setWorkOrderScreenStatus("生产中");
                        workOrderAndScreen.setScreen(semi.getScreenId());
                        workOrderAndScreen.setIsDistributed(true);
                        semi.getScreenId().setScreenStatus(ScreenStatusEnum.ALREADY_MATCH_ORDER);
                        semi.setStatus("已配单");
                        semi.setOperatingPersonnel(user);
                        entityManager.merge(workOrderAndScreen);
                        entityManager.merge(semi.getScreenId());
                        entityManager.merge(semi);
                    });
                    //循环判读 中转库网版是否同步ERP数据
                    for (Semi semi : semiList) {
                        if (semi.getWorkOrder().getInPlantPartNumberId() != null) {
                            inPlantParNumber = semi.getWorkOrder().getInPlantPartNumberId().getInventoryCoding();
                        }
                        outWorkOrderName = semi.getWorkOrder().getName();
                        //材料出库
                        getERPMaterialOut(outWorkOrderName, inPlantParNumber, semi);
                    }
                    //来源库别
                    Procedure procedure = semiList.get(0).getWarehouseId().getProcedure();
                    //从之后的工序开始创建派工单，没有返回-1
                    int flag = procedures.indexOf(procedure) + 1;
                    for (int i = flag; i < procedures.size(); i++) {
                        DispatchList dispatchList = getDispatchList(workOrder, procedures.get(i), "select e from sangyo_DispatchList e ", "where e.workOrder =:workOrder and e.procedure =:procedure and e.type =:type");
                        if (dispatchList == null) {
                            dispatchList = new DispatchList();
                            dispatchList.setIssueTime(LocalDateTime.now());
                            dispatchList.setName(workOrder.getName() + procedures.get(i).getProcedure().getName());
                            dispatchList.setType(DispatchListType.NORMAL);
                            dispatchList.setWorkOrder(workOrder);
                            dispatchList.setProcedure(procedures.get(i).getProcedure());
                            //派工单则进行计划数量变更
                            dispatchList.setPlannedQuantity(semis.size());
                            dispatchList.setReceiveCount(0);
                            dispatchList.setAccomplishQuantity(0);
                        } else {
                            uptoDispatchList(semis, dispatchList, planQuantity);
                        }
                        entityManager.merge(dispatchList);
                        for (Semi semi : semiList) {
                            //创建质量追溯记录
                            if (i == flag) {
                                QualityTraceability qualityTraceability = new QualityTraceability();
                                qualityTraceability.setWorkOrder(workOrder);
                                qualityTraceability.setDispatchList(dispatchList);
                                qualityTraceability.setScreen(semi.getScreenId());
                                qualityTraceability.setState("未进站");
                                entityManager.merge(qualityTraceability);
                            }
                        }
                    }
                }
                workOrder.setPlanProductionQuantity(planQuantity);
                if (finisheds.size() > 0) {
                    if (workOrder.getOrderProductionQuantity() <= finisheds.size()) {
                        workOrder.setStatus(WorkOrderStatus.COMPLETED);
                    } else if (workOrder.getOrderProductionQuantity() > finisheds.size()) {
                        workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
                    }
                }
                //已完成数量
                if (workOrder.getCompletedQuantity() != null) {
                    workOrder.setCompletedQuantity(workOrder.getCompletedQuantity() + finisheds.size());
                } else {
                    workOrder.setCompletedQuantity(finisheds.size());
                }
                if (workOrder.getPlanProductionQuantity() <= workOrder.getCompletedQuantity()) {
                    workOrder.setStatus(WorkOrderStatus.COMPLETED);
                }
                //本次下发数量
                workOrder.setQuantityIssued(workOrder.getQuantityIssued() + semis.size() + finisheds.size());

                if (workOrder.getQuantityIssued() == 0) {
                    workOrder.setDistributedStatus(DistributedStatus.NOT_DISTRIBUTED);
                } else if (workOrder.getQuantityIssued() < workOrder.getPlanProductionQuantity()) {
                    workOrder.setDistributedStatus(DistributedStatus.DISTRIBUTING);
                } else {
                    workOrder.setDistributedStatus(DistributedStatus.DISTRIBUTED);
                }
                entityManager.merge(workOrder);
            }
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
            return "处理失败";
        } finally {
            transaction.end();
        }
        return "下发成功";
    }

    private DispatchList getDispatchList(WorkOrder workOrder, ProcessFlowDetailsAndProcedure processFlowDetailsAndProcedure, String s, String s2) {
        DispatchList dispatchList;
        LoadContext<DispatchList> dispatchListLoadContext = LoadContext.create(DispatchList.class).setQuery(
                LoadContext.createQuery(s +
                        s2)
                        .setParameter("workOrder", workOrder)
                        .setParameter("procedure", processFlowDetailsAndProcedure.getProcedure())
                        .setParameter("type", DispatchListType.NORMAL)
        );
        dispatchList = dataManager.load(dispatchListLoadContext);
        return dispatchList;
    }

    private WorkOrderAndScreen getWorkOrderAndScreen(Screen screenId) {
        LoadContext<WorkOrderAndScreen> workOrderAndScreenLoadContext = LoadContext.create(WorkOrderAndScreen.class).setQuery(
                LoadContext.createQuery("select e from sangyo_WorkOrderAndScreen e where e.screen =:screen and e.workOrder =:workOrder")
                        .setParameter("screen", screenId).setParameter("workOrder", screenId.getWorkOrder())
        ).setView("workOrderAndScreen-view");
        return dataManager.load(workOrderAndScreenLoadContext);
    }

    private List<ProcessFlowDetailsAndProcedure> getProcessFlowDetailsAndProcedures(WorkOrder workOrder) {
        LoadContext<ProcessFlowDetailsAndProcedure> processFlowDetailsAndProcedureLoadContext = LoadContext.create(ProcessFlowDetailsAndProcedure.class).setQuery(
                LoadContext.createQuery("select e from sangyo_ProcessFlowDetailsAndProcedure e" +
                        " where e.processFlow =:processFlow order by e.serialNumber")
                        .setParameter("processFlow", workOrder.getProcessFlowId())
        ).setView("processFlowDetailsAndProcedure-view");
        return dataManager.loadList(processFlowDetailsAndProcedureLoadContext);
    }

    private void uptoDispatchList(Set<Semi> semis, DispatchList dispatchList, Integer planQuantity) {
        //修改派工单计划数量小于或等于计划数量
        if ((semis.size() + dispatchList.getPlannedQuantity()) >= planQuantity) {
            dispatchList.setPlannedQuantity(planQuantity);
        }
        if ((semis.size() + dispatchList.getPlannedQuantity()) < planQuantity) {
            //已存在派工单则进行计划数量变更 = 选择网版数量 + 派工单已有的数量
            dispatchList.setPlannedQuantity(semis.size() + dispatchList.getPlannedQuantity());
        }
        if ((semis.size() + dispatchList.getPlannedQuantity()) <= dispatchList.getAccomplishQuantity()) {
            dispatchList.setStatus("已完成");
        }
        if ((semis.size() + dispatchList.getPlannedQuantity()) > dispatchList.getAccomplishQuantity()) {
            dispatchList.setStatus("进行中");
        }
    }

    private String getString(String inPlantParNumber, Finished finished) {
        if (finished.getWorkOrder().getInPlantPartNumberId() != null) {
            inPlantParNumber = finished.getWorkOrder().getInPlantPartNumberId().getInventoryCoding();
        }
        return inPlantParNumber;
    }

    private boolean getERPMaterialOut(String workOrderName, String inPlantParNumber, Semi semi) throws Throwable {
//        if (!inPlantParNumber.equals("") && semi.getWarehouseId().getIsSynchronizeERP() && newConfig.getERPIsEnableMaterialOut().equals("true")) {
//            boolean isWarehouse = true;
//            //假框不调用erp接口
//            if (semi.getWarehouseId().getName().equals("假框中转库001")) {
//                isWarehouse = false;
//            }
            //调用出库方法
//            if (isWarehouse && semi.getWarehouseId().getIsSynchronizeERP() && newConfig.getERPIsEnableMaterialOut().equals("true")) {
//                ErpApiAccessResult erpData = erpService.getERPData("4", workOrderName, inPlantParNumber, semi.getScreenId().getScreenCode());
//                log.info("调用出库方法Semi semi : semiListerpService.getERPData" + workOrderName + inPlantParNumber);
//                return erpData.getStatus().equals("success");
//            }
//        }
        return true;
    }


    @Override
    public boolean getERPMaterialOut(String workOrderName, String inPlantParNumber, Finished finished) throws Throwable {
        //材料出库
//        if (!inPlantParNumber.equals("") && finished.getWarehouseId().getIsSynchronizeERP() && newConfig.getERPIsEnableMaterialOut().equals("true")) {
//            ErpApiAccessResult erpData = erpService.getERPData("4", workOrderName, inPlantParNumber, finished.getScreenId().getScreenCode());
//            log.info("材料出库Finished finished : finisheds+erpService.getERPData" + workOrderName + inPlantParNumber);
//            return erpData.getStatus().equals("success");
//        }
        return true;

    }

    @Override
    public boolean getERPMaterialStorage(String workOrderName, String Warehousing, Finished finished) throws Throwable {
        //材料入库
//        if (!Warehousing.equals("") && newConfig.getERPIsEnableMaterialStorage().equals("true")) {
//            ErpApiAccessResult erpData = erpService.getERPData("3", workOrderName, Warehousing, finished.getScreenId().getScreenCode());
//            log.info("材料入库Finished finished : finisheds+erpService.getERPData" + workOrderName + Warehousing);
//            return erpData.getStatus().equals("success");
//        }
        return true;

    }


    private DispatchList getDispatchList(Integer planQuantity, WorkOrder workOrder, ProcessFlowDetailsAndProcedure
            processFlowDetailsAndProcedure) {
        DispatchList dispatchList;
        dispatchList = new DispatchList();
        dispatchList.setIssueTime(LocalDateTime.now());
        dispatchList.setName(workOrder.getName() + processFlowDetailsAndProcedure.getProcedure().getName());
        dispatchList.setType(DispatchListType.NORMAL);
        dispatchList.setWorkOrder(workOrder);
        dispatchList.setProcedure(processFlowDetailsAndProcedure.getProcedure());
        dispatchList.setPlannedQuantity(planQuantity);
        dispatchList.setReceiveCount(0);
        dispatchList.setAccomplishQuantity(0);
        return dispatchList;
    }

    @Override
    public List<Finished> getAllFinished(WorkOrder workOrder) throws ExecutionException, InterruptedException {
        InPlantPartNumber inPlantPartNumber = workOrder.getInPlantPartNumberId();
        workOrder = getWorkOrderById(workOrder);
        //返回结果
        List<Finished> finishedList = new ArrayList<>();
        //根据配单规则进行匹配
        OrderAllocationRules orderAllocationRules = workOrder != null ?
                workOrder.getProcessFlowId().getFinishedOrderAllocationRules() : null;
        List<Finished> finishedList1 = getFinished(inPlantPartNumber,
                "where e.status ='可配单' and e.inPlantPartNumber <>:inPlantPartNumber ",
                "finished-view_1");
        List<Finished> finishes1 = new ArrayList<>(finishedList1);
        if (orderAllocationRules != null) {
            WorkOrder finalWorkOrder = workOrder;
            // 每500条数据开启一条线程
            int threadSize = 300;
            // 总数据条数
            int dataSize = finishes1.size();
            // 线程数
            int threadNum = dataSize / threadSize + 1;
            if (dataSize % threadSize == 0) {
                threadNum = threadNum - 1;
            }
            List<Finished> cutList = null;
            ExecutorService executorService = ThreadPoolExecutors.getScheduledExecutor();
            List<Future<List<Finished>>> futureList = new ArrayList<Future<List<Finished>>>();
            for (int i = 0; i < threadNum; i++) {
                // 确定每条线程的数据
                if (i == threadNum - 1) {
                    cutList = finishes1.subList(threadSize * i, dataSize);
                } else {
                    cutList = finishes1.subList(threadSize * i, threadSize * (i + 1));
                }
                List<Finished> finalCutList = cutList;
                Future<List<Finished>> future = executorService.submit(new Callable<List<Finished>>() {
                    @Override
                    public List<Finished> call() throws Exception {
                        authentication.begin();
                        try {
                            // authenticated code
                            List<Finished> semiList2 = new ArrayList<>();
                            finalCutList.forEach(item -> {
                                if (finishedMatch(finalWorkOrder, item)) {
                                    semiList2.add(item);

                                }
                            });
                            return semiList2;
                        } finally {
                            authentication.end();
                        }

                    }
                });
                futureList.add(future);
            }

            futureList.forEach(semiFuture -> {
                try {
                    semiFuture.get().forEach(semi -> {
                        if (semi != null) {
                            finishedList.add(semi);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            log.info(finishedList.toString());

        }
        return finishedList;
    }

    private WorkOrder getWorkOrderById(WorkOrder workOrder) {
        LoadContext<WorkOrder> workOrderLoadContext = LoadContext.create(WorkOrder.class).setQuery(
                LoadContext.createQuery("select e from sangyo_WorkOrder e where e.id =:id")
                        .setParameter("id", workOrder.getId())
        ).setView("workOrder-view");
        workOrder = dataManager.load(workOrderLoadContext);
        return workOrder;
    }

    @Override
    public List<Finished> getAllFinishedInPlantPartNumber(WorkOrder workOrder) {
        //返回结果
        List<Finished> finishedList = new ArrayList<>();
        //厂内料号一致即匹配
        InPlantPartNumber inPlantPartNumber = workOrder.getInPlantPartNumberId();
        List<Finished> finishes = getFinished(inPlantPartNumber,
                "where e.inPlantPartNumber =:inPlantPartNumber and e.status ='可配单'",
                "fINISHED-view");
        for (Finished finished : finishes) {
            log.info(finished.getScreenId().getScreenCode() + "料号配单");
            finishedList.add(finished);
        }
        return finishedList;
    }

    private List<Finished> getFinished(InPlantPartNumber inPlantPartNumber, String s, String s2) {
        LoadContext<Finished> loadContext = LoadContext.create(Finished.class).setQuery(
                LoadContext.createQuery("select e from sangyo_FINISHED e " +
                        s)
                        .setParameter("inPlantPartNumber", inPlantPartNumber)
        ).setView(s2);
        return dataManager.loadList(loadContext);
    }


    public Boolean finishedMatch(WorkOrder workOrder, Finished finished) {
//        log.info(finished.getWarehouseId().getName() + ":" + finished.getScreenId().getScreenCode());
        //配单规则
        OrderAllocationRules orderAllocationRules = workOrder.getProcessFlowId().getFinishedOrderAllocationRules();
        //配单规则条目
        List<OrderAllocationRuleItem> orderAllocationRuleItems = orderAllocationRules.getOrderAllocationRuleItem();

        //组值校验条目
        List<GroupValueVerification> groupValueVerifications = orderAllocationRules.getGroupValueVerification();

        //把厂内料号和客户料号所有的参数放到一个map里，作为工单标准
        HashMap<Object, String> standardParamMap = new HashMap<Object, String>();

        //料号参数公差
        HashMap<Object, String> rsdMap = new HashMap<Object, String>();

        //料号参数的公差
        HashMap<Object, String> toleranceMap = new HashMap<Object, String>();

        //客户料号参数
        if (!workOrder.getOrderId().getOrderType().equals(OrderNature.INPLANTCHOICE)) {
            List<CustomerPartNumberAndPartNumberParameters> customerPartNumberAndPartNumberParametersList =
                    workOrder.getCustomerPartNumber().getCustomerPartNumberAndPartNumberParameters();
            customerPartNumberAndPartNumberParametersList.forEach(item -> {
                standardParamMap.put(item.getPartNumberParameterId(), item.getParameterValue());
            });
        }

        //客户描述性参数（对标差维护在客户料号下）
        List<CustomerAndcustomerDescriptiveParameters> customerAndcustomerDescriptiveParametersList = getCustomerAndcustomerDescriptiveParameters(workOrder);
        customerAndcustomerDescriptiveParametersList.forEach(item -> {
            rsdMap.put(item.getCustomerDescriptiveParameters(), item.getParametersValue());
        });

        //工单料号参数
        List<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParameters = workOrder.getInPlantPartNumberId().getInPlantPartNumberAndPartNumberParameters();

        //对应的需要把网版对应的厂内料号参数以及部分参数实测值放在一个map里
        inPlantPartNumberAndPartNumberParameters.forEach(item -> {
            standardParamMap.put(item.getPartNumberParameterId(), item.getParameterValue());
            toleranceMap.put(item.getPartNumberParameterId(), item.getTolerance());
        });

        //上传参数记录
        List<ProductionRecords> productionRecordsList = qualityTraceabilityService.getProductionRecords(finished.getScreenId());
        HashMap<Object, String> uploadMap = new HashMap<Object, String>();
        HashMap<Object, String> screenPartNumberParamMap = new HashMap<Object, String>();
        for (ProductionRecords productionRecords : productionRecordsList) {
            //料号参数+实测值
            if (productionRecords.getParameterValue() != null && !productionRecords.getParameterValue().equals("null")) {
                uploadMap.put(productionRecords.getParameter(), productionRecords.getParameterValue());
            }
        }

        //网版最新的上游厂内料号参数
        List<InPlantPartNumberAndPartNumberParameters> screenPartNumberParamList = getInPlantPartNumberAndPartNumberParameters(finished.getWorkOrder());
        for (InPlantPartNumberAndPartNumberParameters inPlantPartNumberAndPartNumberParameters1 : screenPartNumberParamList) {
            //料号参数+实测值
            if (inPlantPartNumberAndPartNumberParameters1.getParameterValue() != null && !inPlantPartNumberAndPartNumberParameters1.getParameterValue().equals("null")) {
                screenPartNumberParamMap.put(inPlantPartNumberAndPartNumberParameters1.getPartNumberParameterId(), inPlantPartNumberAndPartNumberParameters1.getParameterValue());
            }
        }

        //根据配单规则一条条的去筛选网版，降低代码循环次数
        Boolean singleMatch = true;
        Boolean multiMatch = true;

        for (OrderAllocationRuleItem o : orderAllocationRuleItems) {

            String standardValue = standardParamMap.get(o.getWorkordertandardValue()) != null && !standardParamMap.get(o.getWorkordertandardValue()).equals("") ? standardParamMap.get(o.getWorkordertandardValue()) : "0";
            String observedValue = "0";
            if (o.getScreenParameters().getUse().equals(UseEnum.UPLOADPARAMETERS)) {
                observedValue = uploadMap.get(o.getScreenParameters()) != null ? uploadMap.get(o.getScreenParameters()) : "0";
            }
            if (!o.getScreenParameters().getUse().equals(UseEnum.UPLOADPARAMETERS)) {
                observedValue = screenPartNumberParamMap.get(o.getScreenParameters()) != null ? screenPartNumberParamMap.get(o.getScreenParameters()) : "0";
            }

            String calibrationMethod = String.valueOf(o.getCalibrationMethod());
            boolean isCalculatedRsd = o.getIsCalculatedRsd() != null ? o.getIsCalculatedRsd() : false;

            double tolerance = toleranceMap.get(o.getWorkordertandardValue()) != null && !toleranceMap.get(o.getWorkordertandardValue()).equals("") ? Double.parseDouble(toleranceMap.get(o.getWorkordertandardValue())) : 0;
            //默认不计算对标差
            float rsd = 0f;
            if (isCalculatedRsd) {
                CustomerDescriptiveParameters cdp = o.getCustomerDescriptiveParameter();
                rsd = rsdMap.get(cdp) != null && !rsdMap.get(cdp).equals("") ? Float.parseFloat(rsdMap.get(cdp)) : 0f;
            }
            //是否必须匹配 等同于参数是否可以为空，必须匹配代表参数不可以为空
            if (o.getMustMatch() != null ? o.getMustMatch() : false) {
                log.info(o.getScreenParameters().getName() + "上传参数");
                if (standardValue.equals("0") && observedValue.equals("0")) {
                    rsd = 0f;
                }
                singleMatch = checkService.singleValueCheck(standardValue, calibrationMethod, tolerance, observedValue, rsd);
                //其中一条配单规则明显不匹配，直接跳出循环
                if (!singleMatch) {
                    break;
                }
            }
            if (o.getMustMatch() == null || !o.getMustMatch()) {
                if (observedValue.equals("") || observedValue.equals("0")) {
                    log.info("配单规则不是必须匹配且网版参数值为空，直接匹配");
                    singleMatch = true;
                }
                if (!(observedValue.equals("") || observedValue.equals("0"))) {
                    log.info("配单规则不是必须匹配但网版参数值不为空，需要校验");
                    log.info(o.getScreenParameters().getName() + "上传参数");
                    singleMatch = checkService.singleValueCheck(standardValue, calibrationMethod, tolerance, observedValue, rsd);
                    //其中一条配单规则明显不匹配，直接跳出循环
                    if (!singleMatch) {
                        break;
                    }
                }
            }
        }
        if (singleMatch) {
            for (GroupValueVerification groupValueVerification : groupValueVerifications) {
                String calibrationMethod = String.valueOf(groupValueVerification.getCheckType());
                HashMap<Object, String> map2 = new HashMap<Object, String>();
                //料号参数的公差
                HashMap<Object, String> toleranceMap1 = new HashMap<Object, String>();
                //极差
                HashMap<Object, String> rangeMap = new HashMap<>();

                //客户料号参数
                if (groupValueVerification.getSource().equals(SourceEnum.CUSTOMER) && !workOrder.getOrderId().getOrderType().equals(OrderNature.INPLANTCHOICE)) {
                    List<CustomerPartNumberAndPartNumberParameters> customerPartNumberAndPartNumberParametersList =
                            workOrder.getCustomerPartNumber().getCustomerPartNumberAndPartNumberParameters();
                    customerPartNumberAndPartNumberParametersList.forEach(item -> {
                        map2.put(item.getPartNumberParameterId().getId(), item.getParameterValue());
                        toleranceMap1.put(item.getPartNumberParameterId().getId(), item.getTolerance());
                        rangeMap.put(item.getPartNumberParameterId().getId(), item.getRange());
                    });
                }

                if (!groupValueVerification.getSource().equals(SourceEnum.CUSTOMER)) {
                    //工单料号参数
                    List<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParameters1 = workOrder.getInPlantPartNumberId().getInPlantPartNumberAndPartNumberParameters();
                    //对应的需要把网版对应的厂内料号参数以及部分参数实测值放在一个map里
                    inPlantPartNumberAndPartNumberParameters1.forEach(item -> {
                        map2.put(item.getPartNumberParameterId().getId(), item.getParameterValue());
                        toleranceMap1.put(item.getPartNumberParameterId(), item.getTolerance());
                        rangeMap.put(item.getPartNumberParameterId(), item.getRange());
                    });
                }
                String standardValue = map2.get(groupValueVerification.getStandardValue().getId()) != null ? map2.get(groupValueVerification.getStandardValue().getId()) : "0";

                String observedValue = null;
                List<String> list = new ArrayList<>();
                for (PartNumberParameters partNumberParameters : groupValueVerification.getParametersNameList()) {
                    if (partNumberParameters.getUse().equals(UseEnum.UPLOADPARAMETERS) && uploadMap.get(partNumberParameters) != null && !uploadMap.get(partNumberParameters).equals("null")) {
                        list.add(uploadMap.get(partNumberParameters));
                    }

                    if (!partNumberParameters.getUse().equals(UseEnum.UPLOADPARAMETERS) && screenPartNumberParamMap.get(partNumberParameters) != null) {
                        list.add(screenPartNumberParamMap.get(partNumberParameters));
                    }

                }
                observedValue = StringUtils.join(list, "-");
                if (observedValue == null || observedValue.equals("")) {
                    observedValue = "0";
                }
                float rsd = 0f;
                boolean isCalculatedRsd = groupValueVerification.getIsCalculatedRsd() != null ? groupValueVerification.getIsCalculatedRsd() : false;
                if (isCalculatedRsd) {
                    CustomerDescriptiveParameters cdp = groupValueVerification.getCustomerDescriptiveParameter();
                    rsd = rsdMap.get(cdp) != null && !rsdMap.get(cdp).equals("") ? Float.parseFloat(rsdMap.get(cdp)) : 0f;
                }
                double tolerance = toleranceMap1.get(groupValueVerification.getStandardValue()) != null && !toleranceMap1.get(groupValueVerification.getStandardValue()).equals("") ? Double.valueOf(toleranceMap1.get(groupValueVerification.getStandardValue())) : 0;
                double range = rangeMap.get(groupValueVerification.getStandardValue()) != null && !rangeMap.get(groupValueVerification.getStandardValue()).equals("") ? Double.valueOf(rangeMap.get(groupValueVerification.getStandardValue())) : 0;
                if (groupValueVerification.getMustMatch() != null ? groupValueVerification.getMustMatch() : false) {
                    log.info(groupValueVerification.getStandardValue().getName() + "料号校验名");
                    if (standardValue.equals("0") && observedValue.equals("0")) {
                        rsd = 0f;
                    }
                    multiMatch = checkService.multiValueCheck(standardValue, calibrationMethod, tolerance, rsd, observedValue, range);
                    if (!multiMatch) {
                        break;
                    }
                }
                if (groupValueVerification.getMustMatch() == null || !groupValueVerification.getMustMatch()) {
                    if (observedValue.equals("0")) {
                        multiMatch = true;
                    }
                    if (!observedValue.equals("0")) {
                        log.info(groupValueVerification.getStandardValue().getName() + "料号校验名");
                        multiMatch = checkService.multiValueCheck(standardValue, calibrationMethod, tolerance, rsd, observedValue, range);
                        if (!multiMatch) {
                            break;
                        }
                    }
                }
            }
        }
        return singleMatch && multiMatch;
    }

    @Inject
    private Authentication authentication;

    @Override
    public List<Semi> getAllSemi(UUID workOrderId) throws InterruptedException, ExecutionException {
        WorkOrder workOrder = getWorkOrderById(workOrderId,
                "select e from sangyo_WorkOrder e where e.id =:id", "workOrder-view");
        //工艺流程
        ProcessFlow processFlow = workOrder.getProcessFlowId();
        //配单来源中转库别
        List<Warehouse> warehouseList = new ArrayList<>();
        List<Procedure> kube = processFlow.getKube();
        kube.forEach(item -> {
            List<Warehouse> list = getWarehouseByProcedure(item);
            warehouseList.addAll(list);
        });

        //返回结果
        List<Semi> semiList = new ArrayList<>();

        //查询中转库所有网版
        List<Semi> semiList1 = new ArrayList<>();
        for (int i = 0; i < warehouseList.size(); i++) {
            //当前库别的所有中转库网版记录
            List<Semi> semiList2 = getSemis(warehouseList, i);
            semiList1.addAll(semiList2);
        }

        // 每500条数据开启一条线程
        int threadSize = 300;
        // 总数据条数
        int dataSize = semiList1.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        if (dataSize % threadSize == 0) {
            threadNum = threadNum - 1;
        }
        List<Semi> cutList = null;
        ExecutorService executorService = ThreadPoolExecutors.getScheduledExecutor();
        List<Future<List<Semi>>> futureList = new ArrayList<Future<List<Semi>>>();
        for (int i = 0; i < threadNum; i++) {
            // 确定每条线程的数据
            if (i == threadNum - 1) {
                cutList = semiList1.subList(threadSize * i, dataSize);
            } else {
                cutList = semiList1.subList(threadSize * i, threadSize * (i + 1));
            }
            List<Semi> finalCutList = cutList;
            Future<List<Semi>> future = executorService.submit(new Callable<List<Semi>>() {
                @Override
                public List<Semi> call() throws Exception {
                    authentication.begin();
                    try {
                        // authenticated code
                        List<Semi> semiList2 = new ArrayList<>();
                        finalCutList.forEach(item -> {
                            if (item.getStatus().equals("可配单")) {
                                if (match(workOrder, item)) {
                                    semiList2.add(item);
                                }
                            }
                        });
                        return semiList2;
                    } finally {
                        authentication.end();
                    }

                }
            });
            futureList.add(future);
        }

        futureList.forEach(semiFuture -> {
            try {
                semiFuture.get().forEach(semi -> {
                    if (semi != null) {
                        semiList.add(semi);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        log.info(semiList.toString());
        return semiList;
    }

    private List<Warehouse> getWarehouseByProcedure(Procedure item) {
        LoadContext<Warehouse> loadContext = LoadContext.create(Warehouse.class).setQuery(
                LoadContext.createQuery("select e from sangyo_Warehouse e where e.procedure=:procedure")
                        .setParameter("procedure", item)
        ).setView("warehouse-view");
        return dataManager.loadList(loadContext);
    }

    private List<Semi> getSemis(List<Warehouse> warehouseList, int i) {
        LoadContext<Semi> loadContext = LoadContext.create(Semi.class).setQuery(
                LoadContext.createQuery("select e from sangyo_Semi e where e.warehouseId =:warehouseId")
                        .setParameter("warehouseId", warehouseList.get(i))
        ).setView("sEMI-view");
        return dataManager.loadList(loadContext);
    }

    @Override
    public Boolean match(WorkOrder workOrder, Semi semi) {
        log.info(semi.getWarehouseId().getName() + ":" + semi.getScreenId().getScreenCode());

        //配单规则
        OrderAllocationRules orderAllocationRules = workOrder.getProcessFlowId().getOrderAllocationRules();
        //配单规则条目
        List<OrderAllocationRuleItem> orderAllocationRuleItems = orderAllocationRules.getOrderAllocationRuleItem();

        //组值校验条目
        List<GroupValueVerification> groupValueVerifications = orderAllocationRules.getGroupValueVerification();

        //把厂内料号和客户料号所有的参数放到一个map里，作为工单标准
        HashMap<Object, String> standardParamMap = new HashMap<Object, String>();

        //料号参数公差
        HashMap<Object, String> rsdMap = new HashMap<Object, String>();

        //料号参数的公差
        HashMap<Object, String> toleranceMap = new HashMap<Object, String>();

        //客户料号参数
        if (!workOrder.getOrderId().getOrderType().equals(OrderNature.INPLANTCHOICE)) {
            List<CustomerPartNumberAndPartNumberParameters> customerPartNumberAndPartNumberParametersList =
                    workOrder.getCustomerPartNumber().getCustomerPartNumberAndPartNumberParameters();
            customerPartNumberAndPartNumberParametersList.forEach(item -> {
                standardParamMap.put(item.getPartNumberParameterId(), item.getParameterValue());
            });
        }
        List<CustomerAndcustomerDescriptiveParameters> customerAndcustomerDescriptiveParametersList = getCustomerAndcustomerDescriptiveParameters(workOrder);

        customerAndcustomerDescriptiveParametersList.forEach(item -> {
            rsdMap.put(item.getCustomerDescriptiveParameters(), item.getParametersValue());
        });

        //工单料号参数
        List<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParameters = workOrder.getInPlantPartNumberId().getInPlantPartNumberAndPartNumberParameters();

        //对应的需要把网版对应的厂内料号参数以及部分参数实测值放在一个map里
        inPlantPartNumberAndPartNumberParameters.forEach(item -> {
            standardParamMap.put(item.getPartNumberParameterId(), item.getParameterValue());
            toleranceMap.put(item.getPartNumberParameterId(), item.getTolerance());
        });

        //上传参数记录
        List<ProductionRecords> productionRecordsList = qualityTraceabilityService.getProductionRecords(semi.getScreenId());
        HashMap<Object, String> uploadMap = new HashMap<Object, String>();
        HashMap<Object, String> screenPartNumberParamMap = new HashMap<Object, String>();
        for (ProductionRecords productionRecords : productionRecordsList) {
            //料号参数+实测值
            if (productionRecords.getParameterValue() != null && !productionRecords.getParameterValue().equals("null")) {
                uploadMap.put(productionRecords.getParameter(), productionRecords.getParameterValue());
            }
        }

        //网版最新的上游厂内料号参数
        List<InPlantPartNumberAndPartNumberParameters> screenPartNumberParamList = getInPlantPartNumberAndPartNumberParameters(semi.getScreenId().getWorkOrder());
        for (InPlantPartNumberAndPartNumberParameters inPlantPartNumberAndPartNumberParameters1 : screenPartNumberParamList) {
            //料号参数+实测值
            if (inPlantPartNumberAndPartNumberParameters1.getParameterValue() != null && !inPlantPartNumberAndPartNumberParameters1.getParameterValue().equals("null")) {
                screenPartNumberParamMap.put(inPlantPartNumberAndPartNumberParameters1.getPartNumberParameterId(), inPlantPartNumberAndPartNumberParameters1.getParameterValue());
            }
        }

        //根据配单规则一条条的去筛选网版，降低代码循环次数
        Boolean singleMatch = true;
        Boolean multiMatch = true;

        for (OrderAllocationRuleItem o : orderAllocationRuleItems) {

            String standardValue = standardParamMap.get(o.getWorkordertandardValue()) != null && !standardParamMap.get(o.getWorkordertandardValue()).equals("") ? standardParamMap.get(o.getWorkordertandardValue()) : "0";
            String observedValue = "0";
            if (o.getScreenParameters().getUse().equals(UseEnum.UPLOADPARAMETERS)) {
                observedValue = uploadMap.get(o.getScreenParameters()) != null ? uploadMap.get(o.getScreenParameters()) : "0";
            } else {
                observedValue = screenPartNumberParamMap.get(o.getScreenParameters()) != null ? screenPartNumberParamMap.get(o.getScreenParameters()) : "0";
            }

            String calibrationMethod = String.valueOf(o.getCalibrationMethod());
            boolean isCalculatedRsd = o.getIsCalculatedRsd() != null ? o.getIsCalculatedRsd() : false;

            double tolerance = toleranceMap.get(o.getWorkordertandardValue()) != null && !toleranceMap.get(o.getWorkordertandardValue()).equals("") ? Double.valueOf(toleranceMap.get(o.getWorkordertandardValue())) : 0;
            //默认不计算对标差
            float rsd = 0f;
            if (isCalculatedRsd) {
                CustomerDescriptiveParameters cdp = o.getCustomerDescriptiveParameter();
                rsd = rsdMap.get(cdp) != null && !rsdMap.get(cdp).equals("") ? Float.parseFloat(rsdMap.get(cdp)) : 0f;
            }
//            Float f = Float.parseFloat(observedValue) + rsd;
            //是否必须匹配 等同于参数是否可以为空，必须匹配代表参数不可以为空
            if (o.getMustMatch() != null ? o.getMustMatch() : false) {
                log.info(o.getScreenParameters().getName() + "上传参数");
                if (standardValue.equals("0") && observedValue.equals("0")) {
                    rsd = 0f;
                }
                singleMatch = checkService.singleValueCheck(standardValue, calibrationMethod, tolerance, observedValue, rsd);
                //其中一条配单规则明显不匹配，直接跳出循环
                if (!singleMatch) {
                    break;
                }
            }
            if (o.getMustMatch() == null || !o.getMustMatch()) {
                if (observedValue.equals("") || observedValue.equals("0")) {
                    log.info("配单规则不是必须匹配且网版参数值为空，直接匹配");
                    singleMatch = true;
                }
                if (!(observedValue.equals("") || observedValue.equals("0"))) {
                    log.info("配单规则不是必须匹配但网版参数值不为空，需要校验");
                    log.info(o.getScreenParameters().getName() + "上传参数");
                    singleMatch = checkService.singleValueCheck(standardValue, calibrationMethod, tolerance, observedValue, rsd);
                    //其中一条配单规则明显不匹配，直接跳出循环
                    if (!singleMatch) {
                        break;
                    }
                }
            }
        }
        if (singleMatch) {
            for (GroupValueVerification groupValueVerification : groupValueVerifications) {
                String calibrationMethod = String.valueOf(groupValueVerification.getCheckType());
                HashMap<Object, String> map2 = new HashMap<Object, String>();
                //料号参数的公差
                HashMap<Object, String> toleranceMap1 = new HashMap<Object, String>();
                //极差
                HashMap<Object, String> rangeMap = new HashMap<>();
                if (groupValueVerification.getSource().equals(SourceEnum.CUSTOMER)) {
                    //客户料号参数
                    if (!workOrder.getOrderId().getOrderType().equals(OrderNature.INPLANTCHOICE)) {
                        List<CustomerPartNumberAndPartNumberParameters> customerPartNumberAndPartNumberParametersList =
                                workOrder.getCustomerPartNumber().getCustomerPartNumberAndPartNumberParameters();
                        customerPartNumberAndPartNumberParametersList.forEach(item -> {
                            map2.put(item.getPartNumberParameterId().getId(), item.getParameterValue());
                            toleranceMap1.put(item.getPartNumberParameterId().getId(), item.getTolerance());
                            rangeMap.put(item.getPartNumberParameterId().getId(), item.getRange());
                        });
                    }
                }
                if (!groupValueVerification.getSource().equals(SourceEnum.CUSTOMER)) {
                    //工单料号参数
                    List<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParameters1 = workOrder.getInPlantPartNumberId().getInPlantPartNumberAndPartNumberParameters();
                    //对应的需要把网版对应的厂内料号参数以及部分参数实测值放在一个map里
                    inPlantPartNumberAndPartNumberParameters1.forEach(item -> {
                        map2.put(item.getPartNumberParameterId().getId(), item.getParameterValue());
                        toleranceMap1.put(item.getPartNumberParameterId(), item.getTolerance());
                        rangeMap.put(item.getPartNumberParameterId(), item.getRange());
                    });
                }
                String standardValue = map2.get(groupValueVerification.getStandardValue().getId()) != null ? map2.get(groupValueVerification.getStandardValue().getId()) : "0";

                String observedValue = null;
                List<String> list = new ArrayList<>();
                for (PartNumberParameters partNumberParameters : groupValueVerification.getParametersNameList()) {
                    if (partNumberParameters.getUse().equals(UseEnum.UPLOADPARAMETERS) &&
                            uploadMap.get(partNumberParameters) != null &&
                            !uploadMap.get(partNumberParameters).equals("null")) {
                        list.add(uploadMap.get(partNumberParameters));
                    }
                    if (!partNumberParameters.getUse().equals(UseEnum.UPLOADPARAMETERS) &&
                            screenPartNumberParamMap.get(partNumberParameters) != null) {
                        list.add(screenPartNumberParamMap.get(partNumberParameters));
                    }
                }
                observedValue = StringUtils.join(list, "-");
                if (observedValue == null || observedValue.equals("")) {
                    observedValue = "0";
                }
                float rsd = 0f;
                boolean isCalculatedRsd = groupValueVerification.getIsCalculatedRsd() != null ? groupValueVerification.getIsCalculatedRsd() : false;
                if (isCalculatedRsd) {
                    CustomerDescriptiveParameters cdp = groupValueVerification.getCustomerDescriptiveParameter();
                    rsd = rsdMap.get(cdp) != null && !rsdMap.get(cdp).equals("") ? Float.parseFloat(rsdMap.get(cdp)) : 0f;
                }
                double tolerance = toleranceMap1.get(groupValueVerification.getStandardValue()) != null && !toleranceMap1.get(groupValueVerification.getStandardValue()).equals("") ? Double.valueOf(toleranceMap1.get(groupValueVerification.getStandardValue())) : 0;
                double range = rangeMap.get(groupValueVerification.getStandardValue()) != null && !rangeMap.get(groupValueVerification.getStandardValue()).equals("") ? Double.valueOf(rangeMap.get(groupValueVerification.getStandardValue())) : 0;
                if (groupValueVerification.getMustMatch() != null ? groupValueVerification.getMustMatch() : false) {
                    if (standardValue.equals("0") && observedValue.equals("0")) {
                        rsd = 0f;
                    }
                    log.info(groupValueVerification.getStandardValue().getName() + "料号校验名");
                    multiMatch = checkService.multiValueCheck(standardValue, calibrationMethod, tolerance, rsd, observedValue, range);
                    if (!multiMatch) {
                        break;
                    }
                }
                if (groupValueVerification.getMustMatch() == null || !groupValueVerification.getMustMatch()) {
                    if (observedValue.equals("0")) {
                        multiMatch = true;
                    }
                    if (!observedValue.equals("0")) {
                        log.info(groupValueVerification.getStandardValue().getName() + "料号校验名");
                        multiMatch = checkService.multiValueCheck(standardValue, calibrationMethod, tolerance, rsd, observedValue, range);
                        if (!multiMatch) {
                            break;
                        }
                    }
                }
            }
        }
        return singleMatch && multiMatch;
    }

    private List<InPlantPartNumberAndPartNumberParameters> getInPlantPartNumberAndPartNumberParameters(WorkOrder workOrder2) {
        LoadContext<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParametersLoadContext = LoadContext.create(InPlantPartNumberAndPartNumberParameters.class).setQuery(
                LoadContext.createQuery("select e from sangyo_InPlantPartNumberAndPartNumberParameters e where e.inPlantPartNumber=:inPlantPartNumber")
                        .setParameter("inPlantPartNumber", workOrder2.getInPlantPartNumberId())
        ).setView("inPlantPartNumberAndPartNumberParameters-view");
        return dataManager.loadList(inPlantPartNumberAndPartNumberParametersLoadContext);
    }

    private List<CustomerAndcustomerDescriptiveParameters> getCustomerAndcustomerDescriptiveParameters(WorkOrder workOrder) {
        //客户描述性参数（对标差维护在客户料号下）
        LoadContext<CustomerAndcustomerDescriptiveParameters> customerAndcustomerDescriptiveParametersLoadContext = LoadContext.create(CustomerAndcustomerDescriptiveParameters.class).setQuery(
                LoadContext.createQuery("select e from sangyo_CustomerAndcustomerDescriptiveParameters e where e.customerId =:customerId")
                        .setParameter("customerId", workOrder.getOrderId().getCustomer())
        ).setView("customerAndcustomerDescriptiveParameters-view_1");
        return dataManager.loadList(customerAndcustomerDescriptiveParametersLoadContext);
    }

    @Override
    public void changeQuantity(UUID workOrderId, Integer completedQuantity, Integer changedQuantity, Integer
            quantityIssued, String remark, User user) {
        //未完成生产网版
        List<WorkOrderAndScreen> unfinishedScreens = getUnfinishedScreens(workOrderId);
        log.info("未完成生产网版：{}", unfinishedScreens);

        //工单
        WorkOrder workOrder = persistence.createTransaction().execute((Transaction.Callable<WorkOrder>) em ->
                em.find(WorkOrder.class, workOrderId, "_local"));

        //已下发数量小于变更后数量，直接修改数量即可，不用特别处理
        if (quantityIssued > changedQuantity) {
            //筛选未完成的网版，保留生产快接近完成的
            if (changedQuantity > unfinishedScreens.size()) {
                //生产的还不够
                unfinishedScreens.stream().sorted(Comparator.comparing(WorkOrderAndScreen::getCreateTs).reversed()).collect(Collectors.toList());
                //这些网版保留
                List<WorkOrderAndScreen> retainingScreens = unfinishedScreens.subList(0, changedQuantity - completedQuantity);
                //这些网版进不合格品库
                unfinishedScreens = ListUtils.getReduceList(unfinishedScreens, retainingScreens);
            }
            //更新未完成生产的网版的质量追溯记录，类型：异常出站 原因：工单数量减少
            unfinishedScreens.forEach(item -> {
                QualityTraceability qualityTraceability = qualityTraceabilityService.getLatestQualityTraceabilityByScreen(item.getScreen());
                if (qualityTraceability.getState().equals("已进站") || qualityTraceability.getState().equals("未进站")) {
                    qualityTraceability.setState("异常出站");
                    qualityTraceabilityService.createQualityTraceability(qualityTraceability);
                }
                //创建不合格品库记录
                unqualifiedService.newUnqualified(workOrder, item.getScreen(), user, null, "工单数量减少", null, false);
            });
        }

        //更新所有正常派工单数量
        List<DispatchList> dispatchListList = dispatchListService.getDispatchListByWorkOrder(workOrderId, DispatchListType.NORMAL);
        dispatchListList.forEach(item -> {
            item.setPlannedQuantity(changedQuantity);
            dispatchListService.createDispatchList(item);
        });

        //更新工单计划生产数量、变更数量、变更标识
        updateWorkOrderQuantity(workOrderId, changedQuantity);
    }

    private void updateWorkOrderQuantity(UUID workOrderID, Integer quantity) {
        WorkOrder workOrder = persistence.createTransaction().execute((Transaction.Callable<WorkOrder>) em ->
                em.find(WorkOrder.class, workOrderID, "_local"));
        workOrder.setPlanProductionQuantity(quantity);
        workOrder.setChangedQuantity(quantity);
        workOrder.setHasChanged(true);
        updateWorkOrder(workOrder);
    }

    private List<WorkOrderAndScreen> getUnfinishedScreens(UUID workOrderId) {
        //获取当前工单下已完成生产的网版
        List<WorkOrderAndScreen> finishedScreens = getFinishedScreens(workOrderId);
        //获取工单下所有已配单网版
        List<WorkOrderAndScreen> allScreens = getAllScreens(workOrderId);
        //获取上面的差集
        return (List<WorkOrderAndScreen>) ListUtils.getReduceList(allScreens, finishedScreens);
    }

    private List<WorkOrderAndScreen> getAllScreens(UUID workOrderId) {
        LoadContext<WorkOrderAndScreen> workOrderAndScreenLoadContext = LoadContext.create(WorkOrderAndScreen.class).setQuery(
                LoadContext.createQuery("select e from sangyo_WorkOrderAndScreen e where" +
                        " e.workOrder.id =:workOrderId")
                        .setParameter("workOrderId", workOrderId)
        ).setView("workOrderAndScreen-view");
        return dataManager.loadList(workOrderAndScreenLoadContext);
    }

    @Override
    public List<WorkOrderAndScreen> getFinishedScreens(UUID workOrderId) {
        LoadContext<WorkOrderAndScreen> workOrderAndScreenLoadContext = LoadContext.create(WorkOrderAndScreen.class).setQuery(
                LoadContext.createQuery("select e from sangyo_WorkOrderAndScreen e where" +
                        " e.workOrder.id =:workOrderId and e.workOrderScreenStatus in ('生产完成','被配走','报废')")
                        .setParameter("workOrderId", workOrderId)
        ).setView("workOrderAndScreen-view");
        return dataManager.loadList(workOrderAndScreenLoadContext);
    }

    @Override
    public List getScheduleWorkOrder(UUID workOrder) {
        List<Screen> screens = getScreensByWorkOrderId(workOrder, "select e from sangyo_Screen e ", "where e.workOrder.id=:workOrder and e.screenStatus='报废'", "workOrder", "screen-view_1");

        List<Screen> screenList = getScreensByWorkOrderId(workOrder, "select e from sangyo_Screen e ", "where e.workOrder.id=:workOrder", "workOrder", "screen-view_1");

        WorkOrder workOrder2 = getWorkOrderById(workOrder, "select e from sangyo_WorkOrder e where e.id =:id", "workOrder-view");
        List<ProcessFlowDetailsAndProcedure> procedures = getProcessFlowDetailsAndProcedures(workOrder2);
        List entityList = new ArrayList();
        for (ProcessFlowDetailsAndProcedure procedure : procedures) {
            KeyValueEntity keyValueEntity = new KeyValueEntity();
            String dispatchName = procedure.getProcedure().getName();
            keyValueEntity.setValue("stationNum", dispatchName);
            Integer screenQuantity = 0;
            for (Screen screen : screenList) {
                if (procedure.getProcedure().getName().equals(screen.getCurrentProcedure().getName())) {
                    screenQuantity = screenQuantity + 1;
                }
            }
            String dispatchPlannedQuantity = String.valueOf(screenQuantity);
            keyValueEntity.setValue("minutest", dispatchPlannedQuantity);
            entityList.add(keyValueEntity);
        }

        KeyValueEntity keyValueEntity = new KeyValueEntity();
        keyValueEntity.setValue("stationNum", "报废");
        keyValueEntity.setValue("minutest", screens.size());
        entityList.add(keyValueEntity);

        return entityList;
    }

    @Override
    public void updateWorkOrder(UUID workOrderId) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            Order order = null;
            WorkOrder workOrder1 = getWorkOrderById(workOrderId, "select e from sangyo_WorkOrder e where e.id=:id ", "workOrder-view_1");
            if (workOrder1 != null) {
                List<Screen> screens = getScreensByWorkOrderId(workOrder1.getId(),
                        "select e from sangyo_Screen e",
                        " where e.workOrder.id =:id and e.screenStatus ='已出仓'",
                        "id", "screen-view");
                workOrder1.setShipQuantity(screens.size());
                log.info(workOrder1.getOrderProductionQuantity() + "workOrder1.getOrderProductionQuantity()");
                log.info(screens.size() + "screens.size()");
                if (screens.size() >= workOrder1.getOrderProductionQuantity()) {
                    workOrder1.setShipStatus(ShipStatusEnum.ALREADYSHIP);
                } else {
                    workOrder1.setShipStatus(ShipStatusEnum.IN_SHIP);
                }
                order = getOrder(workOrder1);
                if (order != null) {
                    List<WorkOrder> workOrders = getWorkOrderByOrder(order);
                    boolean shipStatus = true;
                    for (WorkOrder workOrder : workOrders) {
                        if (workOrder.getShipStatus() != null && !workOrder.getShipStatus().equals(ShipStatusEnum.ALREADYSHIP)) {
                            shipStatus = false;
                        }
                    }
                    if (shipStatus) {
                        order.setOrderStatus(OrderStatusEnumeration.WAREHOUSEDELIVERED);
                    }
                    if (!shipStatus) {
                        order.setOrderStatus(OrderStatusEnumeration.OUTWAREHOUSEOFDELIVERY);
                    }
                }
            }

            List<Screen> screens = getScreensByWorkOrderId(workOrder1.getId(), "select e from sangyo_Screen e", " where e.workOrder.id=:id ", "id", "screen-view");
            if (workOrder1.getShipStatus() != null) {
                if (workOrder1.getShipStatus().equals(ShipStatusEnum.ALREADYSHIP)) {
                    for (Screen screen : screens) {
                        if (screen.getScreenStatus().equals(ScreenStatusEnum.NOTOUT)) {
                            screen.setScreenStatus(ScreenStatusEnum.CANMATCHWORKORDER);
                            entityManager.merge(screen);
                            List<Finished> finisheds = getFinishedByWorkOrderId(workOrder1.getId(), "where e.workOrder.id=:id and e.status='未出仓'", "finished-view");
                            for (Finished finished : finisheds) {
                                finished.setStatus(FinishedScreenStatus.CAN_MATE_ORDER);
                                entityManager.merge(finished);
                            }
                        }
                    }
                }
            }
            entityManager.merge(workOrder1);
            if (order != null) {
                entityManager.merge(order);
            }
            transaction.commit();
            if (workOrder1.getCompletedQuantity() >= workOrder1.getPlanProductionQuantity()) {
                workOrder1.setStatus(WorkOrderStatus.COMPLETED);
                //更新订单状态更新
                orderService.updateDeliverStatusOrder(workOrder1);
            }
            if (workOrder1.getCompletedQuantity() < workOrder1.getPlanProductionQuantity()) {
                workOrder1.setStatus(WorkOrderStatus.IN_PROGRESS);
                //更新订单状态更新
                orderService.updateShipStatusOrder(workOrder1);
            }
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
    }

    private Order getOrder(WorkOrder workOrder1) {
        Order order;
        LoadContext<Order> orderLoadContext = LoadContext.create(Order.class).setQuery(
                LoadContext.createQuery("select e from sangyo_Order e " +
                        "where e.inPlantOrder=:InPlantOrder")
                        .setParameter("InPlantOrder", workOrder1.getOrderId().getInPlantOrder())
        ).setView("order-view");
        order = dataManager.load(orderLoadContext);
        return order;
    }

    private WorkOrder getWorkOrderById(UUID workOrderId, String s, String s2) {
        LoadContext<WorkOrder> workOrderLoadContext = LoadContext.create(WorkOrder.class).setQuery(
                LoadContext.createQuery(s)
                        .setParameter("id", workOrderId)
        ).setView(s2);
        return dataManager.load(workOrderLoadContext);
    }

    private List<WorkOrder> getWorkOrderByOrder(Order orders) {
        LoadContext<WorkOrder> workOrderLoadContext1 = LoadContext.create(WorkOrder.class).setQuery(
                LoadContext.createQuery("select e from sangyo_WorkOrder e" +
                        " where e.orderId.id=:id")
                        .setParameter("id", orders.getId())
        ).setView("workOrder-view");
        return dataManager.loadList(workOrderLoadContext1);
    }

    @Override
    public void updateWorkOrder(WorkOrder workOrder) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            entityManager.merge(workOrder);
            transaction.commit();
        } catch (Exception e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public WorkOrder getWorkOrderById(UUID workOrderId) {
        LoadContext<WorkOrder> workOrderLoadContext = LoadContext.create(WorkOrder.class).setQuery(
                LoadContext.createQuery("select e from sangyo_WorkOrder e where e.id =:id")
                        .setParameter("id", workOrderId)
        ).setView("workOrder-view");
        return dataManager.load(workOrderLoadContext);
    }

    @Override
    public String getInPlantPartNumberAndPartNumberParameters(UUID workOrderId, String name) {
        String parameterValue = "";
        Transaction transaction = persistence.createTransaction();
        try {
            WorkOrder workOrder = getWorkOrderById(workOrderId, "select e from sangyo_WorkOrder e where e.id =:id", "workOrder-view_4");

            InPlantPartNumberAndPartNumberParameters inPlantPartNumberAndPartNumberParameters = getInPlantPartNumberAndPartNumberParameters(name, workOrder);
            if (inPlantPartNumberAndPartNumberParameters != null) {
                parameterValue = inPlantPartNumberAndPartNumberParameters.getParameterValue();
            }
            if (workOrder.getOrderId().getCustomer() != null) {
                CustomerAndcustomerDescriptiveParameters customerAndcustomerDescriptiveParameters = getCustomerAndcustomerDescriptiveParameters(name, workOrder);
                if (customerAndcustomerDescriptiveParameters != null) {
                    parameterValue = parameterValue + "±" + customerAndcustomerDescriptiveParameters.getParametersValue();
                }
            }
        } catch (Throwable e) {
            log.error("Error", e);
            return parameterValue;
        } finally {
            transaction.end();
        }
        return parameterValue;
    }

    private CustomerAndcustomerDescriptiveParameters getCustomerAndcustomerDescriptiveParameters(String name, WorkOrder workOrder) {
        LoadContext<CustomerAndcustomerDescriptiveParameters> customerAndcustomerDescriptiveParametersLoadContext =
                LoadContext.create(CustomerAndcustomerDescriptiveParameters.class).setQuery(
                        LoadContext.createQuery("select e from sangyo_CustomerAndcustomerDescriptiveParameters e " +
                                "where e.customerId.id =:id and e.customerDescriptiveParameters.name=:name")
                                .setParameter("id", workOrder.getOrderId().getCustomer().getId())
                                .setParameter("name", name + "对标差")
                ).setView("customerAndcustomerDescriptiveParameters-view");
        return dataManager.load(customerAndcustomerDescriptiveParametersLoadContext);
    }

    private InPlantPartNumberAndPartNumberParameters getInPlantPartNumberAndPartNumberParameters(String name, WorkOrder workOrder) {
        LoadContext<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParametersLoadContext =
                LoadContext.create(InPlantPartNumberAndPartNumberParameters.class).setQuery(
                        LoadContext.createQuery("select e from sangyo_InPlantPartNumberAndPartNumberParameters e " +
                                "where e.inPlantPartNumber.id =:id and e.partNumberParameterId.name=:name")
                                .setParameter("id", workOrder.getInPlantPartNumberId().getId())
                                .setParameter("name", name)
                ).setView("inPlantPartNumberAndPartNumberParameters-view");
        return dataManager.load(inPlantPartNumberAndPartNumberParametersLoadContext);
    }

    @Override
    public String setWorkOrderIsCopy(UUID workOrderId) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            WorkOrder workOrder = getWorkOrderById(workOrderId,
                    "select e from sangyo_WorkOrder e where e.id =:id", "_local");
            if (workOrder != null) {
                workOrder.setIsCopy(true);
                entityManager.merge(workOrder);
                transaction.commit();
            }
        } catch (Throwable e) {
            log.error("Error", e);
            return "复制失败";
        } finally {
            transaction.end();
        }
        return "复制成功";
    }

    @Override
    public String setWorkOrderScreen(UUID workOrderId) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            List<Screen> screens = getScreensByWorkOrderId(workOrderId, "select e from sangyo_Screen e ", "where e.workOrder.id =:id and e.screenStatus='未出仓'", "id", "screen-view");
            if (screens.size() > 0) {
                for (Screen screen : screens) {
                    setScreen(screen);
                }
            }
            List<Finished> finishedList = getFinishedByWorkOrderId(workOrderId, "where e.workOrder.id =:id and e.status='未出仓'", "fINISHED-view");
            if (finishedList.size() > 0) {
                for (Finished finished : finishedList) {
                    setFinished(finished);
                }
            }
        } catch (Throwable e) {
            log.error("Error", e);
            return "更新失败";
        } finally {
            transaction.end();
        }
        return "更新成功";
    }

    private List<Finished> getFinishedByWorkOrderId(UUID workOrderId, String s, String s2) {
        LoadContext<Finished> finishedLoadContext = LoadContext.create(Finished.class).setQuery(
                LoadContext.createQuery("select e from sangyo_FINISHED e " +
                        s)
                        .setParameter("id", workOrderId)
        ).setView(s2);
        return dataManager.loadList(finishedLoadContext);
    }

    private List<Screen> getScreensByWorkOrderId(UUID workOrderId, String s, String s2, String id, String s3) {
        LoadContext<Screen> screenLoadContext = LoadContext.create(Screen.class).setQuery(
                LoadContext.createQuery(s +
                        s2)
                        .setParameter(id, workOrderId)
        ).setView(s3);
        return dataManager.loadList(screenLoadContext);
    }

    public void setScreen(Screen screen) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            screen.setScreenStatus(ScreenStatusEnum.CANMATCHWORKORDER);
            entityManager.merge(screen);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
    }

    public void setFinished(Finished finished) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            finished.setStatus(FinishedScreenStatus.CAN_MATE_ORDER);
            log.info(finished.getScreenId().getScreenCode() + "finished.getScreenId().getScreenCode()");
            entityManager.merge(finished);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public int getWorkOrderByNameAndQuantity(String name, int defaultOrderProduction, UUID workOrderId) {
        List<WorkOrder> workOrderList = getWorkOrderListByName(name);
        WorkOrder workOrder1 = getWorkOrderByName(name);
        int defaultOrderProductionQuantity = workOrder1.getDefaultOrderProductionQuantity() != null ?
                workOrder1.getDefaultOrderProductionQuantity() : 0;
        int orderProductionQuantity = 0;
        log.info("workOrderId{}", workOrderId.toString());
        WorkOrder workOrderId1 = getWorkOrderById(workOrderId, "select e from sangyo_WorkOrder e where e.id =:id", "workOrder-view");
        for (WorkOrder workOrder : workOrderList) {
            if (!workOrderId.equals(workOrder.getId())) {
                log.info("workOrderId{},workOrder{}", workOrderId.toString(), workOrder.getId());
                orderProductionQuantity = orderProductionQuantity + workOrder.getOrderProductionQuantity();
            }
        }
        log.info("{orderProductionQuantity{},defaultOrderProduction{}", orderProductionQuantity, defaultOrderProduction);
        orderProductionQuantity = defaultOrderProduction + orderProductionQuantity;
        log.info("{defaultOrderProductionQuantity{},orderProductionQuantity{}", defaultOrderProductionQuantity, orderProductionQuantity);
        return defaultOrderProductionQuantity - orderProductionQuantity;
    }

    public List<WorkOrder> getWorkOrderListByName(String name) {

        LoadContext<WorkOrder> workOrderLoadContext =
                LoadContext.create(WorkOrder.class).setQuery(
                        LoadContext.createQuery(
                                "select e from sangyo_WorkOrder e " +
                                        "where e.name Like '" + name + "%' ")
                ).setView("workOrder-view_5");
        return dataManager.loadList(workOrderLoadContext);
    }

    @Override
    public WorkOrder getWorkOrderByName(String name) {
        LoadContext<WorkOrder> workOrderLoadContext =
                LoadContext.create(WorkOrder.class).setQuery(
                        LoadContext.createQuery(
                                "select e from sangyo_WorkOrder e " +
                                        "where e.name =:name ")
                                .setParameter("name", name)
                ).setView("workOrder-view_5");
        return dataManager.load(workOrderLoadContext);
    }
}
