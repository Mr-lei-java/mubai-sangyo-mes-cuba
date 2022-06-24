package com.company.sangyo.core.storage;

import com.company.sangyo.core.production.QualityTraceabilityService;
import com.company.sangyo.core.production.ReworkService;
import com.company.sangyo.core.production.WorkOrderService;
import com.company.sangyo.entity.basic.Procedure;
import com.company.sangyo.entity.basic.ProcessFlowDetailsAndProcedure;
import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.basic.ScreenStatusEnum;
import com.company.sangyo.entity.production.DispatchList;
import com.company.sangyo.entity.production.DispatchListType;
import com.company.sangyo.entity.production.WorkOrder;
import com.company.sangyo.entity.production.WorkOrderStatus;
import com.company.sangyo.entity.storage.*;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;

@Service(UnqualifiedService.NAME)
public class UnqualifiedServiceBean implements UnqualifiedService {
    private static final Logger log = LoggerFactory.getLogger(UnqualifiedService.class);

    @Inject
    private Persistence persistence;

    @Inject
    private DataManager dataManager;

    @Inject
    private QualityTraceabilityService qualityTraceabilityService;

    @Inject
    private SemiService semiService;

    @Inject
    private FinishedService finishedService;

    @Inject
    private ReworkService reworkService;

    @Inject
    private WorkOrderService workOrderService;

    @Override
    public String newUnqualified(WorkOrder workOrder, Screen screen, User user, String procedure, String remark, String abnormalParameter, Boolean isRepeated) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            LoadContext<WorkOrder> workOrderLoadContext = LoadContext.create(WorkOrder.class).setQuery(
                    LoadContext.createQuery("select e from sangyo_WorkOrder  e where  e.id=:id")
                            .setParameter("id", workOrder.getId())
            ).setView("workOrder-view_4");
            WorkOrder workOrder1 = dataManager.load(workOrderLoadContext);
            LoadContext<Screen> screenLoadContext = LoadContext.create(Screen.class).setQuery(
                    LoadContext.createQuery("select e from sangyo_Screen  e where  e.id=:id")
                            .setParameter("id", screen.getId())
            ).setView("screen-view_2");
            Screen screen1 = dataManager.load(screenLoadContext);
            Unqualified unqualified = dataManager.create(Unqualified.class);
            if (screen1 != null) {
                screen1.setScreenCurrentPosition("不合格品库");
                unqualified.setScreenId(screen1);
                entityManager.merge(screen1);
            }
            unqualified.setOperatingPersonnel(user);
            unqualified.setOperateTime(LocalDateTime.now());
            unqualified.setWorkOrder(workOrder1);
            unqualified.setRemark2(procedure);
            unqualified.setRemark(remark);
            unqualified.setIncomingClassification(IncomingClassificationEnum.PROCESS_ABNORMALITIES);
            unqualified.setAbnormalParameter(abnormalParameter);
            unqualified.setIsRepeated(isRepeated);
            entityManager.merge(unqualified);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
            return "ng";
        } finally {
            transaction.end();
            return "ok";
        }
    }

    @Override
    public String processing(Unqualified unqualified, Procedure procedure, ReworkType reworkType, ProcessingMode processingMode, User user, String remark) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            unqualified.setProcessingMode(processingMode);
            unqualified.setProcessor(user);
            unqualified.setProcessingTime(LocalDateTime.now());
            unqualified.setRemark3(remark);
            unqualified.setIsProcessed(true);
            if (processingMode == ProcessingMode.SCRAP) {
                //创建报废记录
                ScrapRecord scrapRecord = new ScrapRecord();
                scrapRecord.setScreen(unqualified.getScreenId());
                scrapRecord.setWarehousingTime(LocalDateTime.now());
                scrapRecord.setOperatingPersonnel(user);
                scrapRecord.setSourceScrapped("不合格品库");
                scrapRecord.setRemark(remark);

                unqualified.getScreenId().setScreenStatus(ScreenStatusEnum.SCRAPPED);
                unqualified.getScreenId().setFrame(null);
                entityManager.merge(unqualified.getScreenId());
                entityManager.merge(scrapRecord);
                LoadContext<ProcessFlowDetailsAndProcedure> processFlowDetailsAndProcedureLoadContext = LoadContext.create(ProcessFlowDetailsAndProcedure.class).setQuery(
                        LoadContext.createQuery("select e from sangyo_ProcessFlowDetailsAndProcedure e" +
                                " where e.processFlow =:processFlow order by e.serialNumber")
                                .setParameter("processFlow", unqualified.getWorkOrder().getProcessFlowId())
                ).setView("processFlowDetailsAndProcedure-view");
                List<ProcessFlowDetailsAndProcedure> procedures = dataManager.loadList(processFlowDetailsAndProcedureLoadContext);
                LoadContext<Procedure> procedureLoadContext = LoadContext.create(Procedure.class).setQuery(
                        LoadContext.createQuery("select e from sangyo_Procedure e where e.id =:id ")
                                .setParameter("id", unqualified.getScreenId().getCurrentProcedure().getId())
                ).setView("procedure-view");
                Procedure procedure1 = dataManager.load(procedureLoadContext);
                int flag1 = 0;
                for (int i = 0; i < procedures.size(); i++) {
                    if (procedures.get(i).getProcedure().equals(procedure1)) {
                        flag1 = procedures.get(i).getSerialNumber();
                    }
                }
                for (int i = flag1; i < procedures.size(); i++) {
                    DispatchList dispatchList = null;
                    LoadContext<DispatchList> dispatchListLoadContext = LoadContext.create(DispatchList.class).setQuery(
                            LoadContext.createQuery("select e from sangyo_DispatchList e " +
                                    "where e.workOrder.id =:workOrder and e.procedure =:procedure and e.type =:type")
                                    .setParameter("workOrder", unqualified.getWorkOrder().getId())
                                    .setParameter("procedure", procedures.get(i).getProcedure())
                                    .setParameter("type", DispatchListType.NORMAL)
                    );
                    dispatchList = dataManager.load(dispatchListLoadContext);
                    if (dispatchList != null) {
                        dispatchList.setAccomplishQuantity(dispatchList.getAccomplishQuantity() + 1);
                        dispatchList.setReceiveCount(dispatchList.getReceiveCount() + 1);
                        if (dispatchList.getAccomplishQuantity() >= dispatchList.getPlannedQuantity()) {
                            dispatchList.setStatus("已完成");
                        }
                    }
                    if(dispatchList !=null){
                        entityManager.merge(dispatchList);
                    }
                }
                unqualified.getWorkOrder().setCompletedQuantity(unqualified.getWorkOrder().getCompletedQuantity() + 1);
                if (unqualified.getWorkOrder().getCompletedQuantity().equals(unqualified.getWorkOrder().getPlanProductionQuantity())) {
                    unqualified.getWorkOrder().setStatus(WorkOrderStatus.COMPLETED);
                }
                entityManager.merge(unqualified.getWorkOrder());
            } else if (processingMode == ProcessingMode.REWORK) {
                //1、创建返工记录
                reworkService.rework(unqualified.getScreenId(), unqualified.getWarehouseId(), unqualified.getScreenId().getCurrentProcedure(), procedure, reworkType, user, remark, unqualified.getIsRepeated());
            } else if (processingMode == ProcessingMode.NORMAL) {
                //下一工序的质量追溯记录
                LoadContext<ProcessFlowDetailsAndProcedure> next = LoadContext.create(ProcessFlowDetailsAndProcedure.class).setQuery(
                        LoadContext.createQuery("select e from sangyo_ProcessFlowDetailsAndProcedure e" +
                                " where e.processFlow =:processFlow and e.preProcedureId.name =:preProcedureId")
                                .setParameter("processFlow", unqualified.getWorkOrder().getProcessFlowId()).setParameter("preProcedureId", unqualified.getRemark2())
                ).setView("processFlowDetailsAndProcedure-view");
                ProcessFlowDetailsAndProcedure nextProcedure = dataManager.load(next);
                if (nextProcedure != null) {
                    DispatchList nextDispatchList = null;
                    Boolean isRepeated = unqualified.getIsRepeated() ? true : false;
                    nextDispatchList = getNextDispatchList(unqualified.getWorkOrder(), nextProcedure.getProcedure(), isRepeated ? DispatchListType.REWORK : DispatchListType.NORMAL);
                    if (nextDispatchList == null) {
                        nextDispatchList = new DispatchList();
                        nextDispatchList.setProcedure(nextProcedure.getProcedure());
                        nextDispatchList.setWorkOrder(unqualified.getWorkOrder());
                        nextDispatchList.setIssueTime(LocalDateTime.now());
                        nextDispatchList.setType(isRepeated ? DispatchListType.REWORK : DispatchListType.NORMAL);
                        nextDispatchList.setName(unqualified.getWorkOrder().getName() + nextProcedure.getProcedure().getName() + (isRepeated ? "-返工" : ""));
                        nextDispatchList.setReceiveCount(0);
                        nextDispatchList.setAccomplishQuantity(0);
                        nextDispatchList.setPlannedQuantity(1);
                    } else {
                        nextDispatchList.setPlannedQuantity(isRepeated ? (nextDispatchList.getPlannedQuantity() + 1) : nextDispatchList.getPlannedQuantity());
                        nextDispatchList.setStatus("进行中");
                    }
                    qualityTraceabilityService.createQualityTraceability(user, unqualified.getWorkOrder(), unqualified.getScreenId(), nextDispatchList);
                } else {
                    WorkOrder workOrder = unqualified.getWorkOrder();
                    //进入对应的仓库
                    if (workOrder.getOutputWarehouse().getType().equals(TypeLibrary.SEMI)) {
                        String semi = semiService.createSemi(unqualified.getScreenId(), remark, workOrder, "");
                        if (semi.equals("中转库，产成品入库erp接口处理失败")) {
                            return semi;
                        }
                    } else if (workOrder.getOutputWarehouse().getType().equals(TypeLibrary.FINISHED)) {
                        boolean finished = finishedService.createFinished(unqualified.getScreenId(), remark, workOrder, "");
                        if (!finished) {
                            return "成品仓，产成品入库erp接口处理失败";
                        }
                    }
                    //工单完成数量+1
                    workOrder.setCompletedQuantity(workOrder.getCompletedQuantity() + 1);
                    workOrderService.updateWorkOrder(workOrder);
                }
            }
            entityManager.merge(unqualified);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
            return "处理失败";
        } finally {
            transaction.end();
        }
        return "处理成功";
    }

    private DispatchList getNextDispatchList(WorkOrder workOrder, Procedure nextProcedure, DispatchListType dispatchListType) {
        DispatchList nextDispatchList;
        LoadContext<DispatchList> nextDispatchListLoadContext = LoadContext.create(DispatchList.class).setQuery(
                LoadContext.createQuery("select e from sangyo_DispatchList e" +
                        " where e.workOrder =:workOrder and e.procedure =:procedure and e.type =:type")
                        .setParameter("workOrder", workOrder)
                        .setParameter("procedure", nextProcedure)
                        .setParameter("type", dispatchListType)
        ).setView("dispatchList-view");
        nextDispatchList = dataManager.load(nextDispatchListLoadContext);
        return nextDispatchList;
    }

    @Override
    public Set<Procedure> getAvailableProcedures(UUID screenId, Procedure procedure, String procedureName) {
        List<Procedure> procedureList = new ArrayList<>();
        Set<Procedure> procedureSet1 = new HashSet<>();
        LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                LoadContext.createQuery("select e from sangyo_QualityTraceability e " +
                        "where e.screen.id=:screenId order by e.createTs")
                        .setParameter("screenId", screenId)
        ).setView("qualityTraceability-view_2");
        List<QualityTraceability> qualityTraceabilities = dataManager.loadList(qualityTraceabilityLoadContext);

        for (QualityTraceability qualityTraceabilitie : qualityTraceabilities) {
            procedureList.add(qualityTraceabilitie.getDispatchList().getProcedure());
        }
        for (Procedure procedure1 : procedureList) {
            procedureSet1.add(procedure1);
            if (procedureName != null) {
                if (procedure1.getName().equals(procedureName)) {
                    break;
                }
            }
        }
        return procedureSet1;
    }

}
