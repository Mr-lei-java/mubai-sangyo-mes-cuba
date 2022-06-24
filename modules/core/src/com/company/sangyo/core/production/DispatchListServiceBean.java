package com.company.sangyo.core.production;

import com.company.sangyo.CachedRepositories;
import com.company.sangyo.entity.basic.Procedure;
import com.company.sangyo.entity.basic.ProcessFlowDetailsAndProcedure;
import com.company.sangyo.entity.production.*;
import com.company.sangyo.entity.storage.*;
import com.company.sangyo.repositories.DispatchListRepository;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service(DispatchListService.NAME)
public class DispatchListServiceBean implements DispatchListService {
    private static final Logger log = LoggerFactory.getLogger(DispatchListServiceBean.class);

    @Inject
    private DataManager dataManager;

    @Inject
    private Persistence persistence;

    @Inject
    private CachedRepositories cachedRepositories;

    /**
     * 1、网版出站派工单数量进行变更
     * 2、查询当前派工单是否为首工序
     * 3、不是首工序进行，当前派工单之前所有的派工单进行异常出站网版，是首工序直接进行第五步不加第四步的数量
     * 4、把异常出站的多个网版进行查询是否为报废，或者返工到就近中转库的数量累加
     * 5、把当前派工单的质量追溯所有符合正常出站以及异常出站数量加上第四步的数量赋给派工单的完成数量
     *
     * @param dispatchListId   派工单
     * @param workOrderId      工单
     * @param isLastProcedure  是否为末工序
     * @param outStationStatus 出站情况
     * @param isFirstProcedure 是否为首工序
     * @return 是否成功
     */
    @Override
    public String updateDispatchList(UUID dispatchListId, UUID workOrderId, Boolean isLastProcedure, String outStationStatus, Boolean isFirstProcedure) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            DispatchList dispatchList = getDispatchList(dispatchListId);
            WorkOrder workOrder = getWorkOrder(workOrderId);
            //派工单生产数量
            Integer accomplishQuantity = getAccomplishQuantityByDispatchListName(dispatchList.getName());
            //正常网版完成数量
            Integer eligibility = 0;
            //返工派工单完成数量
            Integer numberRework = 0;
            if (dispatchList.getType() != null && dispatchList.getType().equals(DispatchListType.REWORK)) {
                String dispatchListName = dispatchList.getName();
                log.info("dispatchListName{}", dispatchListName);
                dispatchListName = dispatchListName.substring(0, dispatchListName.indexOf("-"));
                numberRework = getEligibility(dispatchListName);
            }
            eligibility = getEligibility(dispatchList.getName());
            if (accomplishQuantity > dispatchList.getPlannedQuantity()) {
                return "完成网版数量超过计划数量";
            }
            //TODO 派工单数量统计
            if (!isFirstProcedure) {
                Integer abnormalScreens = getAbnormalScreens(workOrder, dispatchList);
                dispatchList.setAccomplishQuantity(accomplishQuantity + abnormalScreens);
            } else {
                dispatchList.setAccomplishQuantity(accomplishQuantity);
            }
            //TODO 正常派工单出站数量+报废数量
            updateWorkOrder(workOrderId, isLastProcedure, outStationStatus, entityManager, workOrder, eligibility, numberRework);
            if (dispatchList.getReceiveCount() == 0) {
                dispatchList.setStatus("未开始");
            } else if (dispatchList.getReceiveCount() > 0) {
                dispatchList.setStatus("进行中");
            }
            if (dispatchList.getAccomplishQuantity() >= dispatchList.getPlannedQuantity()) {
                dispatchList.setStatus("已完成");
                log.info("派工单关闭" + dispatchList.getName());
            }
            entityManager.merge(dispatchList);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
            return "424";
        } finally {
            transaction.end();
        }
        return "200";
    }

    private WorkOrder getWorkOrder(UUID workOrderId) {
        LoadContext<WorkOrder> workOrderLoadContext = LoadContext.create(WorkOrder.class).setQuery(
                LoadContext.createQuery("select e from sangyo_WorkOrder e where e.id =:id")
                        .setParameter("id", workOrderId)
        ).setView("workOrder-view_3");
        return dataManager.load(workOrderLoadContext);
    }

    private DispatchList getDispatchList(UUID dispatchListId) {
        LoadContext<DispatchList> dispatchListLoadContext = LoadContext.create(DispatchList.class).setQuery(
                LoadContext.createQuery("select e from sangyo_DispatchList e where e.id =:id")
                        .setParameter("id", dispatchListId)
        ).setView("dispatchList-view");
        return dataManager.load(dispatchListLoadContext);
    }

    private void updateWorkOrder(UUID workOrderId, Boolean isLastProcedure, String outStationStatus, EntityManager entityManager, WorkOrder workOrder, Integer eligibility, Integer numberRework) {
        WorkOrder workOrder1 = getWorkOrder(workOrderId);
        if (isLastProcedure && "正常出站".equals(outStationStatus) && workOrder1 != null && !workOrder1.getStatus().equals(WorkOrderStatus.COMPLETED)) {
            log.info("更新工单完成数量");
            //获取报废数量
            List<WorkOrderAndScreen> workOrderAndScreen = getWorkOrderAndScreens(workOrderId);
            //成品仓完成数量
            List<Finished> finisheds = getFinisheds(workOrderId);
            //就近中转库返工
            LoadContext<WorkOrderAndScreen> workOrderAndScreenLoadContext1 = LoadContext.create(WorkOrderAndScreen.class).setQuery(
                    LoadContext.createQuery("select e from sangyo_WorkOrderAndScreen e " +
                            "where e.workOrder.id =:id and e.workOrderScreenStatus='就近中转库返工'")
                            .setParameter("id", workOrderId)
            ).setView("workOrderAndScreen-view_2");
            List<WorkOrderAndScreen> workOrderAndScreens = dataManager.loadList(workOrderAndScreenLoadContext1);
            //中转库完成数量
            List<Semi> semis = getSemis(workOrderId);
            if (semis.size() > 0) {
                Integer accomplishQuantity = semis.size();
            }
            int workOrderScreenStatus = 0;
            if (workOrderAndScreens.size() > 0) {
                workOrderScreenStatus = workOrderAndScreens.size();
            }
            //成品仓数量加报废数量
            if (workOrder1.getOutputWarehouse().getType().equals(TypeLibrary.FINISHED)) {
                log.info("eligibility{},numberRework{},workOrder:成品仓", eligibility, numberRework);
                //正常派工单正常出站数量  ，去除正常派工单正常出站数量，加返工派工单正常出站数量
                workOrder1.setEligibility(finisheds.size());
                //加报废数量，加成品仓完成数量，加返回就近中转库完成数量    去除正常派工单正常出站数量，加返工派工单正常出站数量，
                workOrder1.setCompletedQuantity(finisheds.size() + workOrderAndScreen.size() + workOrderScreenStatus);
            } else {
                log.info("eligibility:{},numberRework:{}", eligibility, numberRework);
                //正常派工单正常出站数量，加返工派工单正常出站数量
                workOrder1.setEligibility(eligibility + numberRework);
                //正常派工单正常出站数量，加返工派工单正常出站数量，加报废数量，加返回就近中转库完成数量
//                workOrder1.setCompletedQuantity(eligibility + workOrderAndScreen.size() + workOrderScreenStatus + numberRework);
                //正常派工单正常出站数量，加返工派工单正常出站数量，加报废数量
                workOrder1.setCompletedQuantity(eligibility + workOrderAndScreen.size() + workOrderScreenStatus);
            }
            //完成数量大于等于计划数量工单关闭
            if (workOrder1.getCompletedQuantity() >= workOrder1.getPlanProductionQuantity()) {
                workOrder1.setStatus(WorkOrderStatus.COMPLETED);
            }
            entityManager.merge(workOrder1);
        }
    }

    private List<Semi> getSemis(UUID workOrderId) {
        LoadContext<Semi> semiLoadContext = LoadContext.create(Semi.class).setQuery(
                LoadContext.createQuery("select e from sangyo_SEMI e " +
                        "where e.workOrder.id =:id and e.status in('未出仓','已出仓','已配单','可配单')")
                        .setParameter("id", workOrderId)
        ).setView("sEMI-view");
        return dataManager.loadList(semiLoadContext);
    }

    private List<Finished> getFinisheds(UUID workOrderId) {
        LoadContext<Finished> finishedLoadContext = LoadContext.create(Finished.class).setQuery(
                LoadContext.createQuery("select e from sangyo_FINISHED e " +
                        "where e.workOrder.id =:id and e.status in('未出仓','已出仓','已配单','可配单')")
                        .setParameter("id", workOrderId)
        ).setView("fINISHED-view");
        return dataManager.loadList(finishedLoadContext);
    }

    private List<WorkOrderAndScreen> getWorkOrderAndScreens(UUID workOrderId) {
        LoadContext<WorkOrderAndScreen> workOrderAndScreenLoadContext = LoadContext.create(WorkOrderAndScreen.class).setQuery(
                LoadContext.createQuery("select e from sangyo_WorkOrderAndScreen e " +
                        "where e.workOrder.id =:id and e.screen.screenStatus='报废'")
                        .setParameter("id", workOrderId)
        );
        return dataManager.loadList(workOrderAndScreenLoadContext);
    }

    @Override
    public void createDispatchList(DispatchList dispatchList) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            entityManager.merge(dispatchList);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public Integer getAccomplishQuantityByDispatchListName(String dispatchListName) {
        LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                LoadContext.createQuery("select e from sangyo_QualityTraceability e " +
                        "where e.dispatchList.name =:name and e.state in('正常出站','异常出站','校验错误异常出站')")
                        .setParameter("name", dispatchListName)
        ).setView("_minimal");
        List<QualityTraceability> qualityTraceabilityList = dataManager.loadList(qualityTraceabilityLoadContext);
        return qualityTraceabilityList.size();
    }

    @Override
    public List<DispatchList> getDispatchListByWorkOrder(UUID workOrderId, DispatchListType dispatchListType) {
        return dataManager.load(DispatchList.class)
                .query("select e from sangyo_DispatchList e " +
                        "where e.workOrder.id = :workOrderId and e.type =:type")
                .parameter("workOrderId", workOrderId)
                .parameter("type", dispatchListType)
                .view("_local")
                .list();
    }

    public Integer getEligibility(String dispatchList) {
        LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                LoadContext.createQuery("select e from sangyo_QualityTraceability e " +
                        "where e.dispatchList.name =:name and e.state in('正常出站')")
                        .setParameter("name", dispatchList)
        ).setView("_minimal");
        List<QualityTraceability> qualityTraceabilityList = dataManager.loadList(qualityTraceabilityLoadContext);
        return qualityTraceabilityList.size();
    }

    public Integer getAbnormalScreens(WorkOrder workOrder, DispatchList dispatchList) {
        List<ProcessFlowDetailsAndProcedure> procedures = getProcessFlowDetailsAndProcedures(workOrder);
        Procedure procedure1 = getProcedure(dispatchList);
        int flag1 = 0;
        for (ProcessFlowDetailsAndProcedure procedure : procedures) {
            if (procedure.getProcedure().equals(procedure1)) {
                flag1 = procedure.getSerialNumber();
            }
        }
        flag1 = flag1 - 2;
        //TODO 获取不合格品库网版处理方式为报废+返工且返工就近中转库的所有网版
        List<Unqualified> unqualifiedList = new ArrayList<Unqualified>();
        //获取当前派工单之前的派工单
        for (int i = flag1; i >= 0; i--) {
            DispatchList dispatchList1 = getDispatchList(workOrder, procedures, i);
            List<QualityTraceability> qualityTraceabilityList = getQualityTraceabilityList(dispatchList1.getName());
            //查询派工单内所有异常出站网版，在不合格库处理为报废或者返回到就近中转库网版进行统计
            for (QualityTraceability qualityTraceability : qualityTraceabilityList) {
                Unqualified unqualifiedScreenList = getUnqualifiedScreenList(qualityTraceability.getScreen().getUuid());
                Unqualified unqualifiedScreenLists = getUnqualifiedScreenLists(qualityTraceability.getScreen().getUuid());
                if (unqualifiedScreenList != null) {
                    unqualifiedList.add(unqualifiedScreenList);
                }
                if (unqualifiedScreenLists != null) {
                    unqualifiedList.add(unqualifiedScreenLists);
                }
            }
        }
        return unqualifiedList.size();
    }

    private Unqualified getUnqualifiedScreenList(UUID screenId) {
        LoadContext<Unqualified> unqualifiedLoadContext = LoadContext.create(Unqualified.class).setQuery(
                LoadContext.createQuery("select e from sangyo_UNQUALIFIED e " +
                        "where e.screenId.id =:screen and e.reworkType ='B'")
                        .setParameter("screen", screenId)
        ).setView("_minimal");
        return dataManager.load(unqualifiedLoadContext);
    }

    private Unqualified getUnqualifiedScreenLists(UUID screenId) {
        LoadContext<Unqualified> unqualifiedLoadContext = LoadContext.create(Unqualified.class).setQuery(
                LoadContext.createQuery("select e from sangyo_UNQUALIFIED e " +
                        "where e.screenId.id =:screen and e.processingMode='报废'")
                        .setParameter("screen", screenId)
        ).setView("_minimal");
        return dataManager.load(unqualifiedLoadContext);
    }


    private List<QualityTraceability> getQualityTraceabilityList(String dispatchListName) {
        List<QualityTraceability> qualityTraceabilityList;
        LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                LoadContext.createQuery("select e from sangyo_QualityTraceability e " +
                        "where e.dispatchList.name =:name and e.state in('异常出站')")
                        .setParameter("name", dispatchListName)
        ).setView("qualityTraceability-view_2");
        qualityTraceabilityList = dataManager.loadList(qualityTraceabilityLoadContext);
        return qualityTraceabilityList;
    }

    private DispatchList getDispatchList(WorkOrder workOrder, List<ProcessFlowDetailsAndProcedure> procedures, int i) {
        LoadContext<DispatchList> dispatchListLoadContext = LoadContext.create(DispatchList.class).setQuery(
                LoadContext.createQuery("select e from sangyo_DispatchList e " +
                        "where e.workOrder.id =:workOrder and e.procedure =:procedure and e.type =:type")
                        .setParameter("workOrder", workOrder.getId())
                        .setParameter("procedure", procedures.get(i).getProcedure())
                        .setParameter("type", DispatchListType.NORMAL)
        ).setView("dispatchList-view");
        return dataManager.load(dispatchListLoadContext);
    }

    private Procedure getProcedure(DispatchList dispatchList) {
        LoadContext<Procedure> procedureLoadContext = LoadContext.create(Procedure.class).setQuery(
                LoadContext.createQuery("select e from sangyo_Procedure e where e.id =:id ")
                        .setParameter("id", dispatchList.getProcedure().getId())
        ).setView("procedure-view");
        return dataManager.load(procedureLoadContext);
    }

    private List<ProcessFlowDetailsAndProcedure> getProcessFlowDetailsAndProcedures(WorkOrder workOrder) {
        LoadContext<ProcessFlowDetailsAndProcedure> processFlowDetailsAndProcedureLoadContext = LoadContext.create(ProcessFlowDetailsAndProcedure.class).setQuery(
                LoadContext.createQuery("select e from sangyo_ProcessFlowDetailsAndProcedure e" +
                        " where e.processFlow =:processFlow order by e.serialNumber")
                        .setParameter("processFlow", workOrder.getProcessFlowId())
        ).setView("processFlowDetailsAndProcedure-view");
        return dataManager.loadList(processFlowDetailsAndProcedureLoadContext);
    }

    @Override
    public void closeDispatchListRepositoryList() {
        DispatchListRepository dispatchListRepository = cachedRepositories.get(DispatchListRepository.class);
        try {
            CommitContext commitContext = new CommitContext();
            List<DispatchList> dispatchListList = getDispatchListRepositoryList();
            for (DispatchList dispatchList : dispatchListList) {
                dispatchList.setStatus("已完成");
                commitContext.addInstanceToCommit(dispatchList);
            }
            dispatchListRepository.commit(commitContext);
        } catch (Throwable e) {
            log.error("Error", e);
        }
    }

    private List<DispatchList> getDispatchListRepositoryList() {
        LoadContext<DispatchList> loadContext = LoadContext.create(DispatchList.class).setQuery(
                LoadContext.createQuery("select e from sangyo_DispatchList e " +
                        "where e.workOrder.status = '已完成' and e.status <> '已完成'")
        ).setView("dispatchList-view_1");
        return dataManager.loadList(loadContext);
    }
}
