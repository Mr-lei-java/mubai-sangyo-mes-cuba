package com.company.sangyo.core.production;

import com.company.sangyo.entity.production.DispatchList;
import com.company.sangyo.entity.production.DispatchListType;

import java.util.List;
import java.util.UUID;

public interface DispatchListService {
    String NAME = "sangyo_DispatchListService";

    /**
     * 更新派工单数量以及工单数量
     *
     * @param dispatchListId   派工单
     * @param workOrderId      工单
     * @param isLastProcedure
     * @param outStationStatus
     * @return
     */
    String updateDispatchList(UUID dispatchListId, UUID workOrderId, Boolean isLastProcedure, String outStationStatus, Boolean isFirstProcedure);

    /**
     * 创建派工单
     *
     * @param dispatchList
     */
    void createDispatchList(DispatchList dispatchList);

    /**
     * 获取派工单完成数量
     *
     * @param dispatchListName
     * @return
     */
    Integer getAccomplishQuantityByDispatchListName(String dispatchListName);

    /**
     * 根据工单ID和派工单类型查找派工单
     *
     * @param workOrderId
     * @param dispatchListType
     * @return
     */
    List<DispatchList> getDispatchListByWorkOrder(UUID workOrderId, DispatchListType dispatchListType);


    void closeDispatchListRepositoryList();
}
