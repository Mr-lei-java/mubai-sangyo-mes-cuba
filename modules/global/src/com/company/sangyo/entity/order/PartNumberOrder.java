package com.company.sangyo.entity.order;

import com.company.sangyo.entity.basic.CustomerPartNumber;
import com.company.sangyo.entity.basic.InPlantPartNumber;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_PART_NUMBER_ORDER")
@Entity(name = "sangyo_PartNumberOrder")
public class PartNumberOrder extends StandardEntity {
    private static final long serialVersionUID = -7819286228540608472L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_PART_NUMBER_ID")
    private CustomerPartNumber customerPartNumber;

    @Column(name = "ORDER_PRODUCTION_QUANTITY")
    private Integer orderProductionQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_PLANT_PART_NUMBER_ID")
    private InPlantPartNumber inPlantPartNumber;

    @Column(name = "IN_PLANTORDER_PRODUCTION_QUANTITY")
    private Integer inPlantorderProductionQuantity;

    @Column(name = "PLAN_PRODUCTION_QUANTITY")
    private Integer planProductionQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    public CustomerPartNumber getCustomerPartNumber() {
        return customerPartNumber;
    }

    public void setCustomerPartNumber(CustomerPartNumber customerPartNumber) {
        this.customerPartNumber = customerPartNumber;
    }

    public Integer getOrderProductionQuantity() {
        return orderProductionQuantity;
    }

    public void setOrderProductionQuantity(Integer orderProductionQuantity) {
        this.orderProductionQuantity = orderProductionQuantity;
    }

    public InPlantPartNumber getInPlantPartNumber() {
        return inPlantPartNumber;
    }

    public void setInPlantPartNumber(InPlantPartNumber inPlantPartNumber) {
        this.inPlantPartNumber = inPlantPartNumber;
    }

    public Integer getInPlantorderProductionQuantity() {
        return inPlantorderProductionQuantity;
    }

    public void setInPlantorderProductionQuantity(Integer inPlantorderProductionQuantity) {
        this.inPlantorderProductionQuantity = inPlantorderProductionQuantity;
    }

    public Integer getPlanProductionQuantity() {
        return planProductionQuantity;
    }

    public void setPlanProductionQuantity(Integer planProductionQuantity) {
        this.planProductionQuantity = planProductionQuantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
