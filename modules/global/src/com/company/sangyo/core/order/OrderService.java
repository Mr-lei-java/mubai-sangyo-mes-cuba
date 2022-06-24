package com.company.sangyo.core.order;

import com.company.sangyo.core.BaseResult;
import com.company.sangyo.dto.OrderDTO;
import com.company.sangyo.entity.basic.CustomerPartNumber;
import com.company.sangyo.entity.basic.InPlantPartNumber;
import com.company.sangyo.entity.order.Order;
import com.company.sangyo.entity.production.WorkOrder;
import com.haulmont.cuba.core.entity.KeyValueEntity;

import java.util.Collection;
import java.util.UUID;

public interface OrderService {
    String NAME = "sangyo_OrderService";

    /**
     * 订单下发
     *
     * @param order
     */
    String placeOrder(Order order);

    /**
     * 通过厂内订单号查询
     * 料号信息
     *
     * @param orderId
     * @return
     */
    BaseResult<String> getAllCusMnByOrderId(String orderId);

    /**
     * 订单下发
     *
     * @param orderId
     * @param list
     * @return
     */
    BaseResult<String> distributeOrder(String orderId, String list, String userName);

    /**
     * 分页查询
     *
     * @param page  第几页
     * @param limit 多少行
     * @return
     */
    BaseResult<OrderDTO> getAllOrder(Integer page, Integer limit);

    /**
     * 获取厂内订单号
     *
     * @return
     */
    String genInPlantOrder();

    /**
     * 更新订单状态出仓中
     *
     * @param workOrder
     */
    void updateShipStatusOrder(WorkOrder workOrder);

    /**
     * 更新订单状态已出仓
     *
     * @param workOrder
     */
    void updateDeliverStatusOrder(WorkOrder workOrder);


    /**
     * 从订单查询工单数量
     *
     * @param order
     * @return
     */
    Collection<KeyValueEntity> getScheduleOrder(UUID order);


    /**
     * 厂内料号详情
     *
     * @param inPlantPartNumber
     * @return
     */
    BaseResult<InPlantPartNumber> getInPlantPartNumber(String inPlantPartNumber);

    /**
     * 客户料号详情
     *
     * @param customerPartNumber
     * @return
     */
    BaseResult<CustomerPartNumber> getCustomerPartNumber(String customerPartNumber);

    Order getOrderById(UUID orderId);

    Collection<KeyValueEntity> getUnshippedCollection(String tradeDress, String commodityForm);

    Collection<KeyValueEntity> getUnshippedCollection();
}
