package com.company.sangyo.core.production;

import com.company.sangyo.core.NativeQueryService;
import com.company.sangyo.entity.basic.Procedure;
import com.company.sangyo.entity.basic.ProcessFlowDetailsAndProcedure;
import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.production.*;
import com.company.sangyo.entity.storage.QualityTraceability;
import com.company.sangyo.entity.storage.ReworkRecord;
import com.company.sangyo.entity.storage.ReworkType;
import com.company.sangyo.entity.storage.Warehouse;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service(ReworkService.NAME)
public class ReworkServiceBean implements ReworkService {
    private static final Logger log = LoggerFactory.getLogger(ReworkServiceBean.class);

    @Inject
    private Persistence persistence;

    @Inject
    private DataManager dataManager;

    @Inject
    private NativeQueryService nativeQueryService;

    @Override
    public void rework(Screen screen, Warehouse sourceWarehouse, Procedure sourceProcedure, Procedure procedure,
                       ReworkType reworkType, User user, String remark, Boolean isRepeated) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            ReworkRecord reworkRecord = new ReworkRecord();
            reworkRecord.setReworkProcedureId(procedure);
            reworkRecord.setOperateTime(LocalDateTime.now());
            reworkRecord.setOperatingPersonnel(user);
            reworkRecord.setRemark(remark);
            reworkRecord.setSourceWarehouse(sourceWarehouse);
            reworkRecord.setReworkType(reworkType);
            reworkRecord.setSourceProcedure(sourceProcedure);
            reworkRecord.setScreen(screen);
            reworkRecord.setIsRepeated(isRepeated);
            reworkRecord.setIsActive(true);
            entityManager.merge(reworkRecord);
            String a = null;
            log.info(reworkType + "reworkType" + ReworkType.B + "ReworkType.B");
            if (reworkType.equals(ReworkType.B)) {
                LoadContext<Screen> screenLoadContext = LoadContext.create(Screen.class).setQuery(
                        LoadContext.createQuery("select e from sangyo_Screen e where e.id =:screen")
                                .setParameter("screen", screen.getId())
                ).setView("screen-view_1");
                Screen screen1 = dataManager.load(screenLoadContext);
                if (screen1 == null) {
                    log.info("返回就近中转库没有查询到网版号");
                }
                if (screen1.getWorkOrder().getId() != null) {
                    LoadContext<WorkOrder> workOrderLoadContext = LoadContext.create(WorkOrder.class).setQuery(
                            LoadContext.createQuery("select e from sangyo_WorkOrder e where e.id =:id")
                                    .setParameter("id", screen1.getWorkOrder().getId())
                    ).setView("workOrder-view_4");
                    WorkOrder perWorkOrder = dataManager.load(workOrderLoadContext);
                    if (perWorkOrder != null) {
                        log.info(perWorkOrder.getName());
                        perWorkOrder.setCompletedQuantity(perWorkOrder.getCompletedQuantity() + 1);
                        if (perWorkOrder.getCompletedQuantity().equals(perWorkOrder.getPlanProductionQuantity())) {
                            perWorkOrder.setStatus(WorkOrderStatus.COMPLETED);
                        }

                        log.info(perWorkOrder.getCompletedQuantity() + "perWorkOrder.getCompletedQuantity()");
                        a = isReworkType(perWorkOrder);
                    }
                    LoadContext<WorkOrderAndScreen> workOrderAndScreenLoadContext = LoadContext.create(WorkOrderAndScreen.class).setQuery(
                            LoadContext.createQuery("select e from sangyo_WorkOrderAndScreen e " +
                                    "where e.screen.id =:screen and e.workOrder.id =:workOrder")
                                    .setParameter("screen", screen1.getId()).setParameter("workOrder", screen1.getWorkOrder().getId())
                    ).setView("workOrderAndScreen-view");
                    WorkOrderAndScreen preWorkOrderAndScreen = dataManager.load(workOrderAndScreenLoadContext);
                    log.info(preWorkOrderAndScreen != null ? "2:" : "null--" + ":preWorkOrderAndScreen");
                    if (preWorkOrderAndScreen != null) {
                        //返回就近，更改当前工单的网版与工单状态为返工
                        preWorkOrderAndScreen.setWorkOrderScreenStatus("就近中转库返工");
                        entityManager.merge(preWorkOrderAndScreen);
                    }
                }
                LoadContext<ProcessFlowDetailsAndProcedure> processFlowDetailsAndProcedureLoadContext = LoadContext.create(ProcessFlowDetailsAndProcedure.class).setQuery(
                        LoadContext.createQuery("select e from sangyo_ProcessFlowDetailsAndProcedure e" +
                                " where e.processFlow =:processFlow order by e.serialNumber")
                                .setParameter("processFlow", screen1.getWorkOrder().getProcessFlowId())
                ).setView("processFlowDetailsAndProcedure-view");
                List<ProcessFlowDetailsAndProcedure> procedures = dataManager.loadList(processFlowDetailsAndProcedureLoadContext);
                LoadContext<Procedure> procedureLoadContext = LoadContext.create(Procedure.class).setQuery(
                        LoadContext.createQuery("select e from sangyo_Procedure e where e.id =:id ")
                                .setParameter("id", screen1.getCurrentProcedure().getId())
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
                                    .setParameter("workOrder", screen1.getWorkOrder().getId())
                                    .setParameter("procedure", procedures.get(i).getProcedure())
                                    .setParameter("type", DispatchListType.NORMAL)
                    ).setView("dispatchList-view");
                    dispatchList = dataManager.load(dispatchListLoadContext);
                    if (dispatchList != null) {
                        dispatchList.setAccomplishQuantity(dispatchList.getAccomplishQuantity() + 1);
                        dispatchList.setReceiveCount(dispatchList.getReceiveCount() + 1);
                        if (dispatchList.getAccomplishQuantity() >= dispatchList.getPlannedQuantity()) {
                            dispatchList.setStatus("已完成");
                        }
                        entityManager.merge(dispatchList);
                    }
                }
            }
            if (reworkType.equals(ReworkType.A)) {
                LoadContext<Screen> screenLoadContext = LoadContext.create(Screen.class).setQuery(
                        LoadContext.createQuery("select e from sangyo_Screen e where e.id =:screen")
                                .setParameter("screen", screen.getId())
                ).setView("screen-view_1");
                Screen screen1 = dataManager.load(screenLoadContext);
                if (screen1.getWorkOrder().getId() != null) {
                    LoadContext<WorkOrderAndScreen> workOrderAndScreenLoadContext = LoadContext.create(WorkOrderAndScreen.class).setQuery(
                            LoadContext.createQuery("select e from sangyo_WorkOrderAndScreen e " +
                                    "where e.screen.id =:screen and e.workOrder.id =:workOrder")
                                    .setParameter("screen", screen1.getId()).setParameter("workOrder", screen1.getWorkOrder().getId())
                    ).setView("workOrderAndScreen-view");
                    WorkOrderAndScreen preWorkOrderAndScreen = dataManager.load(workOrderAndScreenLoadContext);
                    if (preWorkOrderAndScreen != null) {
                        //返回就近，更改当前工单的网版与工单状态为返工
                        preWorkOrderAndScreen.setWorkOrderScreenStatus("原生产队列返工");
                        entityManager.merge(preWorkOrderAndScreen);
                    }
                }
            }
            //2、创建返工派工单

            //找返工工序最新的质量追溯记录，取对应的工单
            LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                    LoadContext.createQuery(" select e from sangyo_QualityTraceability e " +
                            "where e.screen =:screen and e.dispatchList.procedure =:procedure and e.state in('正常出站','异常出站','校验错误异常出站') order by e.outboundTime desc")
                            .setParameter("screen", screen)
                            .setParameter("procedure", procedure)
            ).setView("qualityTraceability-view");
            QualityTraceability qualityTraceability = dataManager.load(qualityTraceabilityLoadContext);
            //TODO 如果没有找到质量追溯记录应该是不可以返工的，暂时不处理
            if (qualityTraceability.getWorkOrder() != null) {

            }
            LoadContext<DispatchList> dispatchListLoadContext = LoadContext.create(DispatchList.class).setQuery(
                    LoadContext.createQuery("select e from sangyo_DispatchList e" +
                            " where e.workOrder =:workOrder and e.procedure =:procedure and e.type='REWORK'")
                            .setParameter("workOrder", qualityTraceability.getWorkOrder())
                            .setParameter("procedure", procedure)
            ).setView("dispatchList-view");
            DispatchList dispatchList = dataManager.load(dispatchListLoadContext);
            if (dispatchList == null) {
                dispatchList = new DispatchList();
                dispatchList.setName(qualityTraceability.getWorkOrder().getName() + procedure.getName() + "-返工");
                dispatchList.setProcedure(procedure);
                dispatchList.setIssueTime(LocalDateTime.now());
                dispatchList.setAccomplishQuantity(0);
                dispatchList.setWorkOrder(qualityTraceability.getWorkOrder());
                dispatchList.setDispatchListAccomplishQuantity(0);
                dispatchList.setReceiveCount(0);
                dispatchList.setPlannedQuantity(1);
                dispatchList.setType(DispatchListType.REWORK);
            } else {
                dispatchList.setPlannedQuantity(dispatchList.getPlannedQuantity() + 1);
                if (dispatchList.getStatus().equals("已完成")) {
                    dispatchList.setStatus("进行中");
                }
            }
            //创建质量追溯记录
            QualityTraceability qualityTraceability1 = new QualityTraceability();
            qualityTraceability1.setWorkOrder(dispatchList.getWorkOrder());
            qualityTraceability1.setDispatchList(dispatchList);
            qualityTraceability1.setScreen(screen);
            qualityTraceability1.setState("未进站");
            entityManager.merge(qualityTraceability1);
            entityManager.merge(dispatchList);
            transaction.commit();
            log.info(a);
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
    }

    public String isReworkType(WorkOrder workOrder) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            entityManager.merge(workOrder);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
            return "424";
        } finally {
            transaction.end();
            return "200";
        }
    }

    @Override
    public String updateReworkRecord(ReworkRecord reworkRecord) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            entityManager.merge(reworkRecord);
            transaction.commit();
        } catch (Throwable e) {
            ExceptionUtils.getStackTrace(e);
            return "424";
        } finally {
            transaction.end();
            return "200";
        }
    }

    @Override
    public Collection<KeyValueEntity> getReworkRateCollection(String startDate, String endDate, String tradeDress, String procedureName) {
        List<KeyValueEntity> keyValueEntities = new ArrayList();

        String sql = "select\n" +
                "\tdate_trunc('day', yqt.outbound_time) as mydate,\n" +
                "\tcount(distinct yqt.*)as mycount,\n" +
                "\tcount(distinct yrr.*)as mycount1\n" +
                "from\n" +
                "\tsangyo_quality_traceability yqt\n" +
                "left join sangyo_work_order ywo on\n" +
                "\tyqt.work_order_id = ywo.id\n" +
                "left join sangyo_rework_record yrr on\n" +
                "\t(yrr.screen_id = yqt.screen_id\n" +
                "\t\tand to_date(yrr.create_ts::text , 'yyyy-MM-dd') = to_date(yqt.outbound_time::text , 'yyyy-MM-dd'))\n" +
                "and yrr.source_procedure_id in (\n" +
                "\tselect\n" +
                "\t\typfdap.procedure_id\n" +
                "\tfrom\n" +
                "\t\tsangyo_process_flow_details_and_procedure ypfdap\n" +
                "\twhere\n" +
                "\t\typfdap.process_flow_id = ywo.process_flow_id_id)" +
                "left join sangyo_order yo on\n" +
                "\tywo.order_id_id = yo.id\n" +
                "left join sangyo_dispatch_list ydl on\n" +
                "\tyqt .dispatch_list_id = ydl.id\n" +
                "left join sangyo_procedure yp on\n" +
                "\tydl.procedure_id = yp.id\n" +
                "left join sangyo_in_plant_part_number yippn on\n" +
                "\tyippn.id = ywo.in_plant_part_number_id_id\n" +
                "where\n" +
                "\tyo.order_type = '销售订单'\n" + "AND yrr.rework_type <> 'B'" +
                tradeDress +
//                "\tand yp.\"name\" = '涂布目测'\n" +
                "\tand yqt.outbound_time between (" + startDate + ") and (" + endDate + ")\n" +
                procedureName +
                "group by\n" +
                "\tmydate\n" +
                "order by\n" +
                "\tmydate";
        List<Object[]> listData = nativeQueryService.getListData(sql);

        for (int i = 0; i < listData.size(); i++) {
            KeyValueEntity keyValueEntity = new KeyValueEntity();
            keyValueEntity.setValue("stationNum", listData.get(i)[0].toString().substring(5, 10));
            Long warehouseInQuantity = (Long) listData.get(i)[1];
            Long reworkQuantity = (Long) listData.get(i)[2];
            double qualifiedRate;
            if (warehouseInQuantity == 0L) {
                qualifiedRate = 0;
            } else {
                qualifiedRate = new BigDecimal((float) reworkQuantity / warehouseInQuantity).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            log.info("日期{},进站{},返工{},返工率{}", listData.get(i)[0].toString().substring(5, 10), warehouseInQuantity.toString(), reworkQuantity, qualifiedRate);
            keyValueEntity.setValue("minutest", String.valueOf(qualifiedRate));
            keyValueEntity.setValue("Number", String.valueOf(reworkQuantity));
            keyValueEntity.setValue("total", String.valueOf(warehouseInQuantity));
            keyValueEntities.add(keyValueEntity);
        }
        return keyValueEntities;
    }

    @Override
    public Collection<KeyValueEntity> getScrappageCollection(String startDate, String endDate, String tradeDress) {
        List<KeyValueEntity> keyValueEntities = new ArrayList();

        String sql = "select\n" +
                "\tdate_trunc('day', yqt.outbound_time) as mydate,\n" +
                "\tcount(distinct yqt.screen_id)as mycount,\n" +
                "\tcount(case when yu.processing_mode = '报废' then 1 else null end) as mycount1\n" +
                "from\n" +
                "\tsangyo_quality_traceability yqt\n" +
                "left join sangyo_work_order ywo on\n" +
                "\tyqt.work_order_id = ywo.id\n" +
                "left join sangyo_unqualified yu on\n" +
                "\tyu.screen_id_id = yqt.screen_id\n" +
                "left join sangyo_dispatch_list ydl on\n" +
                "\tyqt.dispatch_list_id = ydl.id\n" +
                "left join sangyo_procedure yp on\n" +
                "\tydl.procedure_id = yp.id\n" +
                "left join sangyo_in_plant_part_number yippn on\n" +
                "\tyippn.id = ywo.in_plant_part_number_id_id\n" +
                "where\n" +
                "\typ.\"name\" = '覆膜'\n" +
                "\tor yp.\"name\" = '涂布'\n" +
                tradeDress +
                "\tand yqt.outbound_time between (" + startDate + ") and (" + endDate + ")\n" +
                "group by\n" +
                "\tmydate\n" +
                "order by\n" +
                "\tmydate";
        List<Object[]> listData = nativeQueryService.getListData(sql);

        for (int i = 0; i < listData.size(); i++) {
            KeyValueEntity keyValueEntity = new KeyValueEntity();
            keyValueEntity.setValue("stationNum", listData.get(i)[0].toString().substring(5, 10));
            Long warehouseInQuantity = (Long) listData.get(i)[1];
            Long reworkQuantity = (Long) listData.get(i)[2];
            double qualifiedRate;
            if (warehouseInQuantity == 0L) {
                qualifiedRate = 0;
            } else {
                qualifiedRate = new BigDecimal((float) reworkQuantity / warehouseInQuantity).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                qualifiedRate = qualifiedRate * 100;
            }
            log.info("日期{},报废{},总数{},报废率{}", listData.get(i)[0].toString().substring(5, 10), warehouseInQuantity.toString(), reworkQuantity, qualifiedRate);
            keyValueEntity.setValue("minutest", String.valueOf(qualifiedRate));
            keyValueEntity.setValue("total", String.valueOf(warehouseInQuantity));
            keyValueEntity.setValue("Number", String.valueOf(reworkQuantity));
            keyValueEntities.add(keyValueEntity);
        }
        return keyValueEntities;
    }
}
