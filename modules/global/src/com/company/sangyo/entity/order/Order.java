package com.company.sangyo.entity.order;

import com.company.sangyo.entity.basic.Customer;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SANGYO_ORDER")
@Entity(name = "sangyo_Order")
public class Order extends StandardEntity {
    private static final long serialVersionUID = -636681259343080300L;

    @Column(name = "ORDER_CODE")
    private String orderCode;

    @Column(name = "ORDER_QUANTITY")
    private Integer orderQuantity;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "order")
    private List<PartNumberOrder> partNumberOrders;

    @Column(name = "ORDER_PROPERTIES")
    private String orderProperties;

    @Column(name = "ORDER_TIME")
    private LocalDateTime orderTime;

    @Column(name = "DELIVERY_TIME")
    private LocalDateTime deliveryTime;

    @Column(name = "ORDER_TYPE", nullable = false)
    @NotNull
    private String orderType;

    @Column(name = "IN_PLANT_ORDER", nullable = false, unique = true)
    @NotNull
    private String inPlantOrder;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "ORDER_DATA_UPDATE")
    private String orderDataUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(name = "ORDER_STATUS")
    private String orderStatus;

    @Column(name = "ORDER_PRIORITY")
    private String orderPriority;

    @Column(name = "ORDER_PROJECT")
    private String orderProject;

    @Column(name = "COLUMN_REMARK")
    private String columnRemark;

    @Column(name = "REMARK2")
    private String remark2;

    @Column(name = "REMARK3")
    private String remark3;

    @OneToMany(mappedBy = "order")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    private List<OrderAndCustomerPartNumber> orderAndCustomerPartNumber;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "order")
    private List<StockOrder> stockOrder;

    public void setOrderProject(OrderProjectEnum orderProject) {
        this.orderProject = orderProject == null ? null : orderProject.getId();
    }

    public OrderProjectEnum getOrderProject() {
        return orderProject == null ? null : OrderProjectEnum.fromId(orderProject);
    }

    public void setOrderStatus(OrderStatusEnumeration orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.getId();
    }

    public OrderStatusEnumeration getOrderStatus() {
        return orderStatus == null ? null : OrderStatusEnumeration.fromId(orderStatus);
    }

    public void setOrderDataUpdate(OrderDataUpdateEnum orderDataUpdate) {
        this.orderDataUpdate = orderDataUpdate == null ? null : orderDataUpdate.getId();
    }

    public OrderDataUpdateEnum getOrderDataUpdate() {
        return orderDataUpdate == null ? null : OrderDataUpdateEnum.fromId(orderDataUpdate);
    }

    public void setOrderType(OrderNature orderType) {
        this.orderType = orderType == null ? null : orderType.getId();
    }

    public OrderNature getOrderType() {
        return orderType == null ? null : OrderNature.fromId(orderType);
    }

    public void setOrderProperties(OrderProjectEnum orderProperties) {
        this.orderProperties = orderProperties == null ? null : orderProperties.getId();
    }

    public OrderProjectEnum getOrderProperties() {
        return orderProperties == null ? null : OrderProjectEnum.fromId(orderProperties);
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public List<PartNumberOrder> getPartNumberOrders() {
        return partNumberOrders;
    }

    public void setPartNumberOrders(List<PartNumberOrder> partNumberOrders) {
        this.partNumberOrders = partNumberOrders;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getInPlantOrder() {
        return inPlantOrder;
    }

    public void setInPlantOrder(String inPlantOrder) {
        this.inPlantOrder = inPlantOrder;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(String orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getColumnRemark() {
        return columnRemark;
    }

    public void setColumnRemark(String columnRemark) {
        this.columnRemark = columnRemark;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public List<OrderAndCustomerPartNumber> getOrderAndCustomerPartNumber() {
        return orderAndCustomerPartNumber;
    }

    public void setOrderAndCustomerPartNumber(List<OrderAndCustomerPartNumber> orderAndCustomerPartNumber) {
        this.orderAndCustomerPartNumber = orderAndCustomerPartNumber;
    }

    public List<StockOrder> getStockOrder() {
        return stockOrder;
    }

    public void setStockOrder(List<StockOrder> stockOrder) {
        this.stockOrder = stockOrder;
    }
}
