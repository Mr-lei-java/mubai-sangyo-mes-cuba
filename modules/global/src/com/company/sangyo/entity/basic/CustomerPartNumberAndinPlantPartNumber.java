package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SANGYO_CUSTOMER_PART_NUMBER_ANDIN_PLANT_PART_NUMBER")
@Entity(name = "sangyo_CustomerPartNumberAndinPlantPartNumber")
public class CustomerPartNumberAndinPlantPartNumber extends StandardEntity {
    private static final long serialVersionUID = -2559168470025523261L;

    @Column(name = "NUMERICAL_ORDER")
    private Integer numericalOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CUSTOMER_PART_NUMBER_ID")
    @NotNull
    private CustomerPartNumber customerPartNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IN_PLANT_PART_NUMBER_ID")
    @NotNull
    private InPlantPartNumber inPlantPartNumber;

    @Column(name = "REMARK")
    private String remark;

    public Integer getNumericalOrder() {
        return numericalOrder;
    }

    public void setNumericalOrder(Integer numericalOrder) {
        this.numericalOrder = numericalOrder;
    }

    public CustomerPartNumber getCustomerPartNumber() {
        return customerPartNumber;
    }

    public void setCustomerPartNumber(CustomerPartNumber customerPartNumber) {
        this.customerPartNumber = customerPartNumber;
    }

    public InPlantPartNumber getInPlantPartNumber() {
        return inPlantPartNumber;
    }

    public void setInPlantPartNumber(InPlantPartNumber inPlantPartNumber) {
        this.inPlantPartNumber = inPlantPartNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
