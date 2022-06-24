package com.company.sangyo.entity.order;

import com.company.sangyo.entity.basic.CustomerPartNumber;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SANGYO_ORDER_AND_CUSTOMER_PART_NUMBER")
@Entity(name = "sangyo_OrderAndCustomerPartNumber")
public class OrderAndCustomerPartNumber extends StandardEntity {
    private static final long serialVersionUID = -5464297912749579937L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "DCONSIGNDATE")
    private String consignDate;

    @Column(name = "IN_PLANT_PARTDCONSIGNDATE")
    private String inPlantPartConsignDate;

    @JoinColumn(name = "CUSTOMER_PART_NUMBER_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerPartNumber customerPartNumberId;

    @Column(name = "ORDER_QUANTITY", nullable = false)
    @NotNull
    private Integer orderQuantity;

    @Column(name = "ORDER_STATUS")
    private Integer orderStatus;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getConsignDate() {
        return consignDate;
    }

    public void setConsignDate(String consignDate) {
        this.consignDate = consignDate;
    }

    public String getInPlantPartConsignDate() {
        return inPlantPartConsignDate;
    }

    public void setInPlantPartConsignDate(String inPlantPartConsignDate) {
        this.inPlantPartConsignDate = inPlantPartConsignDate;
    }

    public CustomerPartNumber getCustomerPartNumberId() {
        return customerPartNumberId;
    }

    public void setCustomerPartNumberId(CustomerPartNumber customerPartNumberId) {
        this.customerPartNumberId = customerPartNumberId;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
}
