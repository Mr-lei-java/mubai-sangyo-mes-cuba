package com.company.sangyo.core.production;

import com.company.sangyo.entity.basic.PartNumberParameters;
import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.production.DispatchList;
import com.company.sangyo.entity.production.ProductionRecords;
import com.company.sangyo.entity.production.WorkOrder;
import com.company.sangyo.entity.storage.QualityTraceability;
import com.haulmont.cuba.security.entity.User;

import java.util.List;
import java.util.UUID;

public interface QualityTraceabilityService {
    String NAME = "sangyo_QualityTraceabilityService";

    List<ProductionRecords> getProductionRecords(Screen screen);

    /**
     *
     * @param screen
     * @param partNumberParameters
     * @return
     */
    List<ProductionRecords> getParameterRecord(Screen screen, PartNumberParameters partNumberParameters);

    /**
     * 创建质量追溯记录
     *
     * @param user         用户
     * @param workOrder    工单
     * @param screen       网版
     * @param dispatchList 派工单
     * @return
     */
    String createQualityTraceability(User user, WorkOrder workOrder, Screen screen, DispatchList dispatchList);

    /**
     * 获取质量追溯记录
     *
     * @param screen
     * @param user
     * @param dispatchList
     */
    QualityTraceability getQualityTraceability(Screen screen, User user, DispatchList dispatchList, String status);

    QualityTraceability getLatestQualityTraceabilityByScreen(Screen screen);

    /**
     * 更改质量追溯出站时间为当前时间
     *
     * @param qualityTraceability
     */
    void createQualityTraceability(QualityTraceability qualityTraceability);

    /**
     * 修改质量追溯进站时间和状态
     * @param qualityTraceability
     * @return
     */
    String setQualityTraceability(QualityTraceability qualityTraceability);

    /**
     * 查询网版质量追溯，未完成和已进站数量
     * @param dispatchListName
     * @return
     */
    Integer getQualityTraceability(UUID dispatchListName);
}
