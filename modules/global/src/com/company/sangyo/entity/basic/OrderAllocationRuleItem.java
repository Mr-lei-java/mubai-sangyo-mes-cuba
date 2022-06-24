package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SANGYO_ORDER_ALLOCATION_RULE_ITEM")
@Entity(name = "sangyo_OrderAllocationRuleItem")
public class OrderAllocationRuleItem extends StandardEntity {
    private static final long serialVersionUID = -5297648614207681941L;

    @Column(name = "SERIAL_NUMBER")
    private Integer serialNumber;

    @Column(name = "SCREEN_SOURCE_ENUM", nullable = false)
    @NotNull
    private String screenSourceEnum;

    @Column(name = "VERIFICATION_RULES")
    private String verificationRules;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_PARAMETERS_ID")
    private PartNumberParameters screenParameters;

    @NotNull
    @Column(name = "SOURCE", nullable = false)
    private String source;

    @Column(name = "CALIBRATION_METHOD")
    private String calibrationMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKORDERTANDARD_VALUE_ID")
    private PartNumberParameters workordertandardValue;

    @Column(name = "MUST_MATCH")
    private Boolean mustMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ALLOCATION_RULES_ID")
    private OrderAllocationRules orderAllocationRules;

    @Column(name = "IS_CALCULATED_RSD")
    private Boolean isCalculatedRsd = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_DESCRIPTIVE_PARAMETER_ID")
    private CustomerDescriptiveParameters customerDescriptiveParameter;

    public ScreenSourceEnum getScreenSourceEnum() {
        return screenSourceEnum == null ? null : ScreenSourceEnum.fromId(screenSourceEnum);
    }

    public void setScreenSourceEnum(ScreenSourceEnum screenSourceEnum) {
        this.screenSourceEnum = screenSourceEnum == null ? null : screenSourceEnum.getId();
    }

    public SourceEnum getSource() {
        return source == null ? null : SourceEnum.fromId(source);
    }

    public void setSource(SourceEnum source) {
        this.source = source == null ? null : source.getId();
    }

    public CustomerDescriptiveParameters getCustomerDescriptiveParameter() {
        return customerDescriptiveParameter;
    }

    public void setCustomerDescriptiveParameter(CustomerDescriptiveParameters customerDescriptiveParameter) {
        this.customerDescriptiveParameter = customerDescriptiveParameter;
    }

    public Boolean getIsCalculatedRsd() {
        return isCalculatedRsd;
    }

    public void setIsCalculatedRsd(Boolean isCalculatedRsd) {
        this.isCalculatedRsd = isCalculatedRsd;
    }


    public void setCalibrationMethod(CalibrationMethodEnum calibrationMethod) {
        this.calibrationMethod = calibrationMethod == null ? null : calibrationMethod.getId();
    }

    public CalibrationMethodEnum getCalibrationMethod() {
        return calibrationMethod == null ? null : CalibrationMethodEnum.fromId(calibrationMethod);
    }

    public OrderAllocationRules getOrderAllocationRules() {
        return orderAllocationRules;
    }

    public void setOrderAllocationRules(OrderAllocationRules orderAllocationRules) {
        this.orderAllocationRules = orderAllocationRules;
    }

    public void setMustMatch(Boolean mustMatch) {
        this.mustMatch = mustMatch;
    }

    public Boolean getMustMatch() {
        return mustMatch;
    }

    public PartNumberParameters getScreenParameters() {
        return screenParameters;
    }

    public void setScreenParameters(PartNumberParameters screenParameters) {
        this.screenParameters = screenParameters;
    }

    public PartNumberParameters getWorkordertandardValue() {
        return workordertandardValue;
    }

    public void setWorkordertandardValue(PartNumberParameters workordertandardValue) {
        this.workordertandardValue = workordertandardValue;
    }

    public String getVerificationRules() {
        return verificationRules;
    }

    public void setVerificationRules(String verificationRules) {
        this.verificationRules = verificationRules;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }
}
