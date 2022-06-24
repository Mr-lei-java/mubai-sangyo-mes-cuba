package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SANGYO_GROUP_VALUE_VERIFICATION")
@Entity(name = "sangyo_GroupValueVerification")
public class GroupValueVerification extends StandardEntity {
    private static final long serialVersionUID = -730587719283677287L;

    @Column(name = "SERIAL_NUMBER")
    private Integer serialNumber;

    @JoinColumn(name = "PARAMETERS_NAME_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartNumberParameters parametersName;

    @ManyToMany
    @JoinTable(name = "YALONG_GROUP_VALUE_VERIFICATION_PART_NUMBER_PARAMETERS_LINK",
            joinColumns = @JoinColumn(name = "GROUP_VALUE_VERIFICATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "PART_NUMBER_PARAMETERS_ID"))
    private List<PartNumberParameters> parametersNameList;

    @Column(name = "CHECK_TYPE")
    private String checkType;

    @NotNull
    @Column(name = "SOURCE", nullable = false)
    private String source;

    @JoinColumn(name = "STANDARD_VALUE_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private PartNumberParameters standardValue;

    @Column(name = "TIMING_CALIBRATION", nullable = false)
    @NotNull
    private String timingCalibration;

    @Column(name = "IS_CALCULATED_RSD")
    private Boolean isCalculatedRsd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_DESCRIPTIVE_PARAMETER_ID")
    private CustomerDescriptiveParameters customerDescriptiveParameter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_ID")
    private Procedure procedure;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ORDER_ALLOCATION_RULES_ID")
    private OrderAllocationRules orderAllocationRules;

    @Column(name = "MUST_MATCH")
    private Boolean mustMatch;

    public List<PartNumberParameters> getParametersNameList() {
        return parametersNameList;
    }

    public void setParametersNameList(List<PartNumberParameters> parametersNameList) {
        this.parametersNameList = parametersNameList;
    }

    public Boolean getMustMatch() {
        return mustMatch;
    }

    public void setMustMatch(Boolean mustMatch) {
        this.mustMatch = mustMatch;
    }

    public OrderAllocationRules getOrderAllocationRules() {
        return orderAllocationRules;
    }

    public void setOrderAllocationRules(OrderAllocationRules orderAllocationRules) {
        this.orderAllocationRules = orderAllocationRules;
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

    public MultiCheckType getCheckType() {
        return checkType == null ? null : MultiCheckType.fromId(checkType);
    }

    public void setCheckType(MultiCheckType checkType) {
        this.checkType = checkType == null ? null : checkType.getId();
    }

    public SourceEnum getSource() {
        return source == null ? null : SourceEnum.fromId(source);
    }

    public void setSource(SourceEnum source) {
        this.source = source == null ? null : source.getId();
    }

    public void setTimingCalibration(TimingCalibrationEnum timingCalibration) {
        this.timingCalibration = timingCalibration == null ? null : timingCalibration.getId();
    }

    public TimingCalibrationEnum getTimingCalibration() {
        return timingCalibration == null ? null : TimingCalibrationEnum.fromId(timingCalibration);
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public PartNumberParameters getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(PartNumberParameters standardValue) {
        this.standardValue = standardValue;
    }

    public PartNumberParameters getParametersName() {
        return parametersName;
    }

    public void setParametersName(PartNumberParameters parametersName) {
        this.parametersName = parametersName;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }
}
