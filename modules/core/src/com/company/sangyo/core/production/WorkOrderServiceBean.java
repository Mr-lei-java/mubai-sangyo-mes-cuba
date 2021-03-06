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
     * ????????????????????????
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
        //???????????????
        String outWorkOrderName = "";
        //????????????
        String inPlantParNumber = "";
        //?????????????????????
        String Warehousing = "";
        //????????????????????????
        String inWorkOrderName = "";
        WorkOrder workOrder = getWorkOrderById(workOrder1.getId(), "select e from sangyo_WorkOrder e where e.id =:id", "workOrder-view");
        if (workOrder != null) {
            Warehousing = workOrder.getInPlantPartNumberId().getInventoryCoding();
            inWorkOrderName = workOrder.getName();
            if (workOrder.getProcessFlowId() == null) {
                //????????????????????????
                return "?????????????????????";
            }
        }
        if (workOrder == null) {
            return "";
        }
        List<ProcessFlowDetailsAndProcedure> procedures = getProcessFlowDetailsAndProcedures(workOrder);

        if (procedures.size() == 0) {
            //?????????????????????????????????
            return "??????????????????????????????";
        }
        if (workOrder.getPlanProductionQuantity() == null) {
            return "???????????????????????????";
        }
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            if (workOrder.getProcessFlowId().getWhethertartWithFirstProcessProcess()) {
                //???????????????
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
                //??????????????????
                workOrder.setPlanProductionQuantity(planQuantity);
                workOrder.setQuantityIssued(planQuantity);
                workOrder.setDistributedStatus(DistributedStatus.DISTRIBUTED);
                workOrder.setShipStatus(ShipStatusEnum.NOT_SHIP);
                entityManager.merge(workOrder);
            } else {
                //???????????? ???????????????????????????ERP??????
                for (Finished finished : finisheds) {
                    //??????
                    inPlantParNumber = getString(inPlantParNumber, finished);
                    //????????????
                    outWorkOrderName = finished.getWorkOrder().getName();
                    //????????????
                    getERPMaterialOut(outWorkOrderName, inPlantParNumber, finished);
                    //????????????
                    getERPMaterialStorage(inWorkOrderName, Warehousing, finished);
                }
                //??????????????????????????????????????????????????????????????????
                finisheds.stream().forEach(item -> {
                    WorkOrderAndScreen preWorkOrderAndScreen = getWorkOrderAndScreen(item.getScreenId());
                    if (preWorkOrderAndScreen != null) {
                        //????????????????????????????????????????????????
                        preWorkOrderAndScreen.setNextWorkOrder(workOrder);
                        preWorkOrderAndScreen.setWorkOrderScreenStatus("?????????");
                        entityManager.merge(preWorkOrderAndScreen);
                    }

                    //?????????????????????
                    item.setRelWorkOrder(item.getWorkOrder());
                    item.setStatus(FinishedScreenStatus.NOT_OUT_WAREHOUSE);
                    item.setOperatingPersonnel(user);
                    item.setWorkOrder(workOrder);
                    item.setWarehousingTime(LocalDateTime.now());
                    item.setInPlantPartNumber(workOrder.getInPlantPartNumberId());
                    //????????????
                    item.getScreenId().setScreenStatus(ScreenStatusEnum.NOTOUT);
                    item.getScreenId().setWorkOrder(workOrder);
                    //?????????????????????????????????
                    WorkOrderAndScreen workOrderAndScreen = new WorkOrderAndScreen();
                    workOrderAndScreen.setWorkOrder(workOrder);
                    //?????????????????????????????????????????????????????????
                    workOrderAndScreen.setPreWorkOrder(item.getScreenId().getWorkOrder());
                    //TODO ??????????????? ?????????????????????
                    workOrderAndScreen.setWorkOrderScreenStatus("????????????");

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

                //??????????????????????????????????????? ?????????????????????????????????
                for (List<Semi> semiList : getGroupByWarehouse(semis)) {
                    semiList.stream().forEach(semi -> {
                        WorkOrderAndScreen preWorkOrderAndScreen = getWorkOrderAndScreen(semi.getScreenId());
                        if (preWorkOrderAndScreen != null) {
                            //????????????????????????????????????????????????
                            preWorkOrderAndScreen.setNextWorkOrder(workOrder);
                            preWorkOrderAndScreen.setWorkOrderScreenStatus("?????????");
                            entityManager.merge(preWorkOrderAndScreen);
                        }

                        WorkOrderAndScreen workOrderAndScreen = new WorkOrderAndScreen();
                        workOrderAndScreen.setWorkOrder(workOrder);
                        //?????????????????????????????????????????????????????????
                        workOrderAndScreen.setPreWorkOrder(semi.getScreenId().getWorkOrder());
                        //TODO ???????????????
                        workOrderAndScreen.setWorkOrderScreenStatus("?????????");
                        workOrderAndScreen.setScreen(semi.getScreenId());
                        workOrderAndScreen.setIsDistributed(true);
                        semi.getScreenId().setScreenStatus(ScreenStatusEnum.ALREADY_MATCH_ORDER);
                        semi.setStatus("?????????");
                        semi.setOperatingPersonnel(user);
                        entityManager.merge(workOrderAndScreen);
                        entityManager.merge(semi.getScreenId());
                        entityManager.merge(semi);
                    });
                    //???????????? ???????????????????????????ERP??????
                    for (Semi semi : semiList) {
                        if (semi.getWorkOrder().getInPlantPartNumberId() != null) {
                            inPlantParNumber = semi.getWorkOrder().getInPlantPartNumberId().getInventoryCoding();
                        }
                        outWorkOrderName = semi.getWorkOrder().getName();
                        //????????????
                        getERPMaterialOut(outWorkOrderName, inPlantParNumber, semi);
                    }
                    //????????????
                    Procedure procedure = semiList.get(0).getWarehouseId().getProcedure();
                    //??????????????????????????????????????????????????????-1
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
                            //????????????????????????????????????
                            dispatchList.setPlannedQuantity(semis.size());
                            dispatchList.setReceiveCount(0);
                            dispatchList.setAccomplishQuantity(0);
                        } else {
                            uptoDispatchList(semis, dispatchList, planQuantity);
                        }
                        entityManager.merge(dispatchList);
                        for (Semi semi : semiList) {
                            //????????????????????????
                            if (i == flag) {
                                QualityTraceability qualityTraceability = new QualityTraceability();
                                qualityTraceability.setWorkOrder(workOrder);
                                qualityTraceability.setDispatchList(dispatchList);
                                qualityTraceability.setScreen(semi.getScreenId());
                                qualityTraceability.setState("?????????");
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
                //???????????????
                if (workOrder.getCompletedQuantity() != null) {
                    workOrder.setCompletedQuantity(workOrder.getCompletedQuantity() + finisheds.size());
                } else {
                    workOrder.setCompletedQuantity(finisheds.size());
                }
                if (workOrder.getPlanProductionQuantity() <= workOrder.getCompletedQuantity()) {
                    workOrder.setStatus(WorkOrderStatus.COMPLETED);
                }
                //??????????????????
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
            return "????????????";
        } finally {
            transaction.end();
        }
        return "????????????";
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
        //??????????????????????????????????????????????????????
        if ((semis.size() + dispatchList.getPlannedQuantity()) >= planQuantity) {
            dispatchList.setPlannedQuantity(planQuantity);
        }
        if ((semis.size() + dispatchList.getPlannedQuantity()) < planQuantity) {
            //????????????????????????????????????????????? = ?????????????????? + ????????????????????????
            dispatchList.setPlannedQuantity(semis.size() + dispatchList.getPlannedQuantity());
        }
        if ((semis.size() + dispatchList.getPlannedQuantity()) <= dispatchList.getAccomplishQuantity()) {
            dispatchList.setStatus("?????????");
        }
        if ((semis.size() + dispatchList.getPlannedQuantity()) > dispatchList.getAccomplishQuantity()) {
            dispatchList.setStatus("?????????");
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
//            //???????????????erp??????
//            if (semi.getWarehouseId().getName().equals("???????????????001")) {
//                isWarehouse = false;
//            }
            //??????????????????
//            if (isWarehouse && semi.getWarehouseId().getIsSynchronizeERP() && newConfig.getERPIsEnableMaterialOut().equals("true")) {
//                ErpApiAccessResult erpData = erpService.getERPData("4", workOrderName, inPlantParNumber, semi.getScreenId().getScreenCode());
//                log.info("??????????????????Semi semi : semiListerpService.getERPData" + workOrderName + inPlantParNumber);
//                return erpData.getStatus().equals("success");
//            }
//        }
        return true;
    }


    @Override
    public boolean getERPMaterialOut(String workOrderName, String inPlantParNumber, Finished finished) throws Throwable {
        //????????????
//        if (!inPlantParNumber.equals("") && finished.getWarehouseId().getIsSynchronizeERP() && newConfig.getERPIsEnableMaterialOut().equals("true")) {
//            ErpApiAccessResult erpData = erpService.getERPData("4", workOrderName, inPlantParNumber, finished.getScreenId().getScreenCode());
//            log.info("????????????Finished finished : finisheds+erpService.getERPData" + workOrderName + inPlantParNumber);
//            return erpData.getStatus().equals("success");
//        }
        return true;

    }

    @Override
    public boolean getERPMaterialStorage(String workOrderName, String Warehousing, Finished finished) throws Throwable {
        //????????????
//        if (!Warehousing.equals("") && newConfig.getERPIsEnableMaterialStorage().equals("true")) {
//            ErpApiAccessResult erpData = erpService.getERPData("3", workOrderName, Warehousing, finished.getScreenId().getScreenCode());
//            log.info("????????????Finished finished : finisheds+erpService.getERPData" + workOrderName + Warehousing);
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
        //????????????
        List<Finished> finishedList = new ArrayList<>();
        //??????????????????????????????
        OrderAllocationRules orderAllocationRules = workOrder != null ?
                workOrder.getProcessFlowId().getFinishedOrderAllocationRules() : null;
        List<Finished> finishedList1 = getFinished(inPlantPartNumber,
                "where e.status ='?????????' and e.inPlantPartNumber <>:inPlantPartNumber ",
                "finished-view_1");
        List<Finished> finishes1 = new ArrayList<>(finishedList1);
        if (orderAllocationRules != null) {
            WorkOrder finalWorkOrder = workOrder;
            // ???500???????????????????????????
            int threadSize = 300;
            // ???????????????
            int dataSize = finishes1.size();
            // ?????????
            int threadNum = dataSize / threadSize + 1;
            if (dataSize % threadSize == 0) {
                threadNum = threadNum - 1;
            }
            List<Finished> cutList = null;
            ExecutorService executorService = ThreadPoolExecutors.getScheduledExecutor();
            List<Future<List<Finished>>> futureList = new ArrayList<Future<List<Finished>>>();
            for (int i = 0; i < threadNum; i++) {
                // ???????????????????????????
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
        //????????????
        List<Finished> finishedList = new ArrayList<>();
        //???????????????????????????
        InPlantPartNumber inPlantPartNumber = workOrder.getInPlantPartNumberId();
        List<Finished> finishes = getFinished(inPlantPartNumber,
                "where e.inPlantPartNumber =:inPlantPartNumber and e.status ='?????????'",
                "fINISHED-view");
        for (Finished finished : finishes) {
            log.info(finished.getScreenId().getScreenCode() + "????????????");
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
        //????????????
        OrderAllocationRules orderAllocationRules = workOrder.getProcessFlowId().getFinishedOrderAllocationRules();
        //??????????????????
        List<OrderAllocationRuleItem> orderAllocationRuleItems = orderAllocationRules.getOrderAllocationRuleItem();

        //??????????????????
        List<GroupValueVerification> groupValueVerifications = orderAllocationRules.getGroupValueVerification();

        //?????????????????????????????????????????????????????????map????????????????????????
        HashMap<Object, String> standardParamMap = new HashMap<Object, String>();

        //??????????????????
        HashMap<Object, String> rsdMap = new HashMap<Object, String>();

        //?????????????????????
        HashMap<Object, String> toleranceMap = new HashMap<Object, String>();

        //??????????????????
        if (!workOrder.getOrderId().getOrderType().equals(OrderNature.INPLANTCHOICE)) {
            List<CustomerPartNumberAndPartNumberParameters> customerPartNumberAndPartNumberParametersList =
                    workOrder.getCustomerPartNumber().getCustomerPartNumberAndPartNumberParameters();
            customerPartNumberAndPartNumberParametersList.forEach(item -> {
                standardParamMap.put(item.getPartNumberParameterId(), item.getParameterValue());
            });
        }

        //????????????????????????????????????????????????????????????
        List<CustomerAndcustomerDescriptiveParameters> customerAndcustomerDescriptiveParametersList = getCustomerAndcustomerDescriptiveParameters(workOrder);
        customerAndcustomerDescriptiveParametersList.forEach(item -> {
            rsdMap.put(item.getCustomerDescriptiveParameters(), item.getParametersValue());
        });

        //??????????????????
        List<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParameters = workOrder.getInPlantPartNumberId().getInPlantPartNumberAndPartNumberParameters();

        //??????????????????????????????????????????????????????????????????????????????????????????map???
        inPlantPartNumberAndPartNumberParameters.forEach(item -> {
            standardParamMap.put(item.getPartNumberParameterId(), item.getParameterValue());
            toleranceMap.put(item.getPartNumberParameterId(), item.getTolerance());
        });

        //??????????????????
        List<ProductionRecords> productionRecordsList = qualityTraceabilityService.getProductionRecords(finished.getScreenId());
        HashMap<Object, String> uploadMap = new HashMap<Object, String>();
        HashMap<Object, String> screenPartNumberParamMap = new HashMap<Object, String>();
        for (ProductionRecords productionRecords : productionRecordsList) {
            //????????????+?????????
            if (productionRecords.getParameterValue() != null && !productionRecords.getParameterValue().equals("null")) {
                uploadMap.put(productionRecords.getParameter(), productionRecords.getParameterValue());
            }
        }

        //???????????????????????????????????????
        List<InPlantPartNumberAndPartNumberParameters> screenPartNumberParamList = getInPlantPartNumberAndPartNumberParameters(finished.getWorkOrder());
        for (InPlantPartNumberAndPartNumberParameters inPlantPartNumberAndPartNumberParameters1 : screenPartNumberParamList) {
            //????????????+?????????
            if (inPlantPartNumberAndPartNumberParameters1.getParameterValue() != null && !inPlantPartNumberAndPartNumberParameters1.getParameterValue().equals("null")) {
                screenPartNumberParamMap.put(inPlantPartNumberAndPartNumberParameters1.getPartNumberParameterId(), inPlantPartNumberAndPartNumberParameters1.getParameterValue());
            }
        }

        //????????????????????????????????????????????????????????????????????????
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
            //????????????????????????
            float rsd = 0f;
            if (isCalculatedRsd) {
                CustomerDescriptiveParameters cdp = o.getCustomerDescriptiveParameter();
                rsd = rsdMap.get(cdp) != null && !rsdMap.get(cdp).equals("") ? Float.parseFloat(rsdMap.get(cdp)) : 0f;
            }
            //?????????????????? ???????????????????????????????????????????????????????????????????????????
            if (o.getMustMatch() != null ? o.getMustMatch() : false) {
                log.info(o.getScreenParameters().getName() + "????????????");
                if (standardValue.equals("0") && observedValue.equals("0")) {
                    rsd = 0f;
                }
                singleMatch = checkService.singleValueCheck(standardValue, calibrationMethod, tolerance, observedValue, rsd);
                //????????????????????????????????????????????????????????????
                if (!singleMatch) {
                    break;
                }
            }
            if (o.getMustMatch() == null || !o.getMustMatch()) {
                if (observedValue.equals("") || observedValue.equals("0")) {
                    log.info("?????????????????????????????????????????????????????????????????????");
                    singleMatch = true;
                }
                if (!(observedValue.equals("") || observedValue.equals("0"))) {
                    log.info("????????????????????????????????????????????????????????????????????????");
                    log.info(o.getScreenParameters().getName() + "????????????");
                    singleMatch = checkService.singleValueCheck(standardValue, calibrationMethod, tolerance, observedValue, rsd);
                    //????????????????????????????????????????????????????????????
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
                //?????????????????????
                HashMap<Object, String> toleranceMap1 = new HashMap<Object, String>();
                //??????
                HashMap<Object, String> rangeMap = new HashMap<>();

                //??????????????????
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
                    //??????????????????
                    List<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParameters1 = workOrder.getInPlantPartNumberId().getInPlantPartNumberAndPartNumberParameters();
                    //??????????????????????????????????????????????????????????????????????????????????????????map???
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
                    log.info(groupValueVerification.getStandardValue().getName() + "???????????????");
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
                        log.info(groupValueVerification.getStandardValue().getName() + "???????????????");
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
        //????????????
        ProcessFlow processFlow = workOrder.getProcessFlowId();
        //????????????????????????
        List<Warehouse> warehouseList = new ArrayList<>();
        List<Procedure> kube = processFlow.getKube();
        kube.forEach(item -> {
            List<Warehouse> list = getWarehouseByProcedure(item);
            warehouseList.addAll(list);
        });

        //????????????
        List<Semi> semiList = new ArrayList<>();

        //???????????????????????????
        List<Semi> semiList1 = new ArrayList<>();
        for (int i = 0; i < warehouseList.size(); i++) {
            //??????????????????????????????????????????
            List<Semi> semiList2 = getSemis(warehouseList, i);
            semiList1.addAll(semiList2);
        }

        // ???500???????????????????????????
        int threadSize = 300;
        // ???????????????
        int dataSize = semiList1.size();
        // ?????????
        int threadNum = dataSize / threadSize + 1;
        if (dataSize % threadSize == 0) {
            threadNum = threadNum - 1;
        }
        List<Semi> cutList = null;
        ExecutorService executorService = ThreadPoolExecutors.getScheduledExecutor();
        List<Future<List<Semi>>> futureList = new ArrayList<Future<List<Semi>>>();
        for (int i = 0; i < threadNum; i++) {
            // ???????????????????????????
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
                            if (item.getStatus().equals("?????????")) {
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

        //????????????
        OrderAllocationRules orderAllocationRules = workOrder.getProcessFlowId().getOrderAllocationRules();
        //??????????????????
        List<OrderAllocationRuleItem> orderAllocationRuleItems = orderAllocationRules.getOrderAllocationRuleItem();

        //??????????????????
        List<GroupValueVerification> groupValueVerifications = orderAllocationRules.getGroupValueVerification();

        //?????????????????????????????????????????????????????????map????????????????????????
        HashMap<Object, String> standardParamMap = new HashMap<Object, String>();

        //??????????????????
        HashMap<Object, String> rsdMap = new HashMap<Object, String>();

        //?????????????????????
        HashMap<Object, String> toleranceMap = new HashMap<Object, String>();

        //??????????????????
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

        //??????????????????
        List<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParameters = workOrder.getInPlantPartNumberId().getInPlantPartNumberAndPartNumberParameters();

        //??????????????????????????????????????????????????????????????????????????????????????????map???
        inPlantPartNumberAndPartNumberParameters.forEach(item -> {
            standardParamMap.put(item.getPartNumberParameterId(), item.getParameterValue());
            toleranceMap.put(item.getPartNumberParameterId(), item.getTolerance());
        });

        //??????????????????
        List<ProductionRecords> productionRecordsList = qualityTraceabilityService.getProductionRecords(semi.getScreenId());
        HashMap<Object, String> uploadMap = new HashMap<Object, String>();
        HashMap<Object, String> screenPartNumberParamMap = new HashMap<Object, String>();
        for (ProductionRecords productionRecords : productionRecordsList) {
            //????????????+?????????
            if (productionRecords.getParameterValue() != null && !productionRecords.getParameterValue().equals("null")) {
                uploadMap.put(productionRecords.getParameter(), productionRecords.getParameterValue());
            }
        }

        //???????????????????????????????????????
        List<InPlantPartNumberAndPartNumberParameters> screenPartNumberParamList = getInPlantPartNumberAndPartNumberParameters(semi.getScreenId().getWorkOrder());
        for (InPlantPartNumberAndPartNumberParameters inPlantPartNumberAndPartNumberParameters1 : screenPartNumberParamList) {
            //????????????+?????????
            if (inPlantPartNumberAndPartNumberParameters1.getParameterValue() != null && !inPlantPartNumberAndPartNumberParameters1.getParameterValue().equals("null")) {
                screenPartNumberParamMap.put(inPlantPartNumberAndPartNumberParameters1.getPartNumberParameterId(), inPlantPartNumberAndPartNumberParameters1.getParameterValue());
            }
        }

        //????????????????????????????????????????????????????????????????????????
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
            //????????????????????????
            float rsd = 0f;
            if (isCalculatedRsd) {
                CustomerDescriptiveParameters cdp = o.getCustomerDescriptiveParameter();
                rsd = rsdMap.get(cdp) != null && !rsdMap.get(cdp).equals("") ? Float.parseFloat(rsdMap.get(cdp)) : 0f;
            }
//            Float f = Float.parseFloat(observedValue) + rsd;
            //?????????????????? ???????????????????????????????????????????????????????????????????????????
            if (o.getMustMatch() != null ? o.getMustMatch() : false) {
                log.info(o.getScreenParameters().getName() + "????????????");
                if (standardValue.equals("0") && observedValue.equals("0")) {
                    rsd = 0f;
                }
                singleMatch = checkService.singleValueCheck(standardValue, calibrationMethod, tolerance, observedValue, rsd);
                //????????????????????????????????????????????????????????????
                if (!singleMatch) {
                    break;
                }
            }
            if (o.getMustMatch() == null || !o.getMustMatch()) {
                if (observedValue.equals("") || observedValue.equals("0")) {
                    log.info("?????????????????????????????????????????????????????????????????????");
                    singleMatch = true;
                }
                if (!(observedValue.equals("") || observedValue.equals("0"))) {
                    log.info("????????????????????????????????????????????????????????????????????????");
                    log.info(o.getScreenParameters().getName() + "????????????");
                    singleMatch = checkService.singleValueCheck(standardValue, calibrationMethod, tolerance, observedValue, rsd);
                    //????????????????????????????????????????????????????????????
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
                //?????????????????????
                HashMap<Object, String> toleranceMap1 = new HashMap<Object, String>();
                //??????
                HashMap<Object, String> rangeMap = new HashMap<>();
                if (groupValueVerification.getSource().equals(SourceEnum.CUSTOMER)) {
                    //??????????????????
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
                    //??????????????????
                    List<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParameters1 = workOrder.getInPlantPartNumberId().getInPlantPartNumberAndPartNumberParameters();
                    //??????????????????????????????????????????????????????????????????????????????????????????map???
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
                    log.info(groupValueVerification.getStandardValue().getName() + "???????????????");
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
                        log.info(groupValueVerification.getStandardValue().getName() + "???????????????");
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
        //????????????????????????????????????????????????????????????
        LoadContext<CustomerAndcustomerDescriptiveParameters> customerAndcustomerDescriptiveParametersLoadContext = LoadContext.create(CustomerAndcustomerDescriptiveParameters.class).setQuery(
                LoadContext.createQuery("select e from sangyo_CustomerAndcustomerDescriptiveParameters e where e.customerId =:customerId")
                        .setParameter("customerId", workOrder.getOrderId().getCustomer())
        ).setView("customerAndcustomerDescriptiveParameters-view_1");
        return dataManager.loadList(customerAndcustomerDescriptiveParametersLoadContext);
    }

    @Override
    public void changeQuantity(UUID workOrderId, Integer completedQuantity, Integer changedQuantity, Integer
            quantityIssued, String remark, User user) {
        //?????????????????????
        List<WorkOrderAndScreen> unfinishedScreens = getUnfinishedScreens(workOrderId);
        log.info("????????????????????????{}", unfinishedScreens);

        //??????
        WorkOrder workOrder = persistence.createTransaction().execute((Transaction.Callable<WorkOrder>) em ->
                em.find(WorkOrder.class, workOrderId, "_local"));

        //????????????????????????????????????????????????????????????????????????????????????
        if (quantityIssued > changedQuantity) {
            //?????????????????????????????????????????????????????????
            if (changedQuantity > unfinishedScreens.size()) {
                //??????????????????
                unfinishedScreens.stream().sorted(Comparator.comparing(WorkOrderAndScreen::getCreateTs).reversed()).collect(Collectors.toList());
                //??????????????????
                List<WorkOrderAndScreen> retainingScreens = unfinishedScreens.subList(0, changedQuantity - completedQuantity);
                //??????????????????????????????
                unfinishedScreens = ListUtils.getReduceList(unfinishedScreens, retainingScreens);
            }
            //??????????????????????????????????????????????????????????????????????????? ???????????????????????????
            unfinishedScreens.forEach(item -> {
                QualityTraceability qualityTraceability = qualityTraceabilityService.getLatestQualityTraceabilityByScreen(item.getScreen());
                if (qualityTraceability.getState().equals("?????????") || qualityTraceability.getState().equals("?????????")) {
                    qualityTraceability.setState("????????????");
                    qualityTraceabilityService.createQualityTraceability(qualityTraceability);
                }
                //???????????????????????????
                unqualifiedService.newUnqualified(workOrder, item.getScreen(), user, null, "??????????????????", null, false);
            });
        }

        //?????????????????????????????????
        List<DispatchList> dispatchListList = dispatchListService.getDispatchListByWorkOrder(workOrderId, DispatchListType.NORMAL);
        dispatchListList.forEach(item -> {
            item.setPlannedQuantity(changedQuantity);
            dispatchListService.createDispatchList(item);
        });

        //????????????????????????????????????????????????????????????
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
        //?????????????????????????????????????????????
        List<WorkOrderAndScreen> finishedScreens = getFinishedScreens(workOrderId);
        //????????????????????????????????????
        List<WorkOrderAndScreen> allScreens = getAllScreens(workOrderId);
        //?????????????????????
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
                        " e.workOrder.id =:workOrderId and e.workOrderScreenStatus in ('????????????','?????????','??????')")
                        .setParameter("workOrderId", workOrderId)
        ).setView("workOrderAndScreen-view");
        return dataManager.loadList(workOrderAndScreenLoadContext);
    }

    @Override
    public List getScheduleWorkOrder(UUID workOrder) {
        List<Screen> screens = getScreensByWorkOrderId(workOrder, "select e from sangyo_Screen e ", "where e.workOrder.id=:workOrder and e.screenStatus='??????'", "workOrder", "screen-view_1");

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
        keyValueEntity.setValue("stationNum", "??????");
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
                        " where e.workOrder.id =:id and e.screenStatus ='?????????'",
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
                            List<Finished> finisheds = getFinishedByWorkOrderId(workOrder1.getId(), "where e.workOrder.id=:id and e.status='?????????'", "finished-view");
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
                //????????????????????????
                orderService.updateDeliverStatusOrder(workOrder1);
            }
            if (workOrder1.getCompletedQuantity() < workOrder1.getPlanProductionQuantity()) {
                workOrder1.setStatus(WorkOrderStatus.IN_PROGRESS);
                //????????????????????????
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
                    parameterValue = parameterValue + "??" + customerAndcustomerDescriptiveParameters.getParametersValue();
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
                                .setParameter("name", name + "?????????")
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
            return "????????????";
        } finally {
            transaction.end();
        }
        return "????????????";
    }

    @Override
    public String setWorkOrderScreen(UUID workOrderId) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            List<Screen> screens = getScreensByWorkOrderId(workOrderId, "select e from sangyo_Screen e ", "where e.workOrder.id =:id and e.screenStatus='?????????'", "id", "screen-view");
            if (screens.size() > 0) {
                for (Screen screen : screens) {
                    setScreen(screen);
                }
            }
            List<Finished> finishedList = getFinishedByWorkOrderId(workOrderId, "where e.workOrder.id =:id and e.status='?????????'", "fINISHED-view");
            if (finishedList.size() > 0) {
                for (Finished finished : finishedList) {
                    setFinished(finished);
                }
            }
        } catch (Throwable e) {
            log.error("Error", e);
            return "????????????";
        } finally {
            transaction.end();
        }
        return "????????????";
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
