package com.company.sangyo.core.production;

import com.company.sangyo.entity.production.WorkOrder;
import com.company.sangyo.entity.production.WorkOrderAndScreen;
import com.company.sangyo.entity.storage.Finished;
import com.company.sangyo.entity.storage.Semi;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.security.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface WorkOrderService {
    String NAME = "sangyo_WorkOrderService";

    /**
     * 获取工单的厂内编号最大值
     *
     * @return
     */
    String getInName();

    /**
     * 工单下发，
     *
     * @param workOrder 工单状态
     * @param semis     中转仓
     * @param finisheds 成品仓
     * @param user      用户
     * @return
     */
    String distribute(WorkOrder workOrder, Set<Semi> semis, Set<Finished> finisheds, Integer planQuantity, User user);

    /**
     * 成品仓配单规则
     *
     * @param workOrder
     * @return
     */
    List<Finished> getAllFinished(WorkOrder workOrder) throws ExecutionException, InterruptedException;

    /**
     * 成品仓料号一致
     *
     * @param workOrder
     * @return
     */
    List<Finished> getAllFinishedInPlantPartNumber(WorkOrder workOrder);

    /**
     * 中转库配单规则
     *
     * @param workOrderId
     * @return
     */
    List<Semi> getAllSemi(UUID workOrderId) throws InterruptedException, ExecutionException;

    /**
     * @param workOrder
     * @param semi
     * @return
     */
    Boolean match(WorkOrder workOrder, Semi semi);

    /**
     * 工单数量减少
     *
     * @param workOrderId
     * @param completedQuantity
     * @param changedQuantity
     * @param quantityIssued
     * @param remark
     * @param user
     */
    void changeQuantity(UUID workOrderId, Integer completedQuantity, Integer changedQuantity, Integer quantityIssued, String remark, User user);

    /**
     * 查询网版里面的派工单以及完成数量
     *
     * @param workOrder
     * @return
     */
    Collection<KeyValueEntity> getScheduleWorkOrder(UUID workOrder);

    /**
     * 更新工单状态
     *
     * @param workOrderId
     */
    void updateWorkOrder(UUID workOrderId);

    void updateWorkOrder(WorkOrder workOrder);

    /**
     * 根据工单id获取工单实体
     *
     * @param workOrderId
     * @return
     */
    WorkOrder getWorkOrderById(UUID workOrderId);

    /**
     * 获取计算对标差数值
     *
     * @param workOrderId
     * @param name
     * @return
     */
    String getInPlantPartNumberAndPartNumberParameters(UUID workOrderId, String name);

    /**
     * 修改工单已复制
     *
     * @param workOrderId
     * @return
     */
    String setWorkOrderIsCopy(UUID workOrderId);

    /**
     * 通过工单id编辑网版和成品仓状态
     *
     * @param workOrderId
     * @return
     */
    String setWorkOrderScreen(UUID workOrderId);

    List<WorkOrderAndScreen> getFinishedScreens(UUID workOrderId);

    //erp材料出库接口
    boolean getERPMaterialOut(String workOrderName, String inPlantParNumber, Finished finished) throws Throwable;

    //erp材料入库接口
    boolean getERPMaterialStorage(String workOrderName, String Warehousing, Finished finished) throws Throwable;

    /**
     *
     *
     * @param name
     * @param defaultOrderProduction
     * @return
     */
    int getWorkOrderByNameAndQuantity(String name, int defaultOrderProduction,UUID workOrderId);

    WorkOrder getWorkOrderByName(String name);
}
