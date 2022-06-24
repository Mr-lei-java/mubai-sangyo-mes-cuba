package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SANGYO_PROCESS_PARAMETERS")
@Entity(name = "sangyo_ProcessParameters")
public class ProcessParameters extends StandardEntity {
    private static final long serialVersionUID = -7676852719132112528L;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_ID")
    private Procedure procedure;

    @Column(name = "REMARK")
    private String remark;

    @JoinColumn(name = "PARAMETER_NAME_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartNumberParameters parameterName;

    @Column(name = "IS_VERIFIED")
    private Boolean isVerified = false;

    @JoinColumn(name = "STANDARD_VALUE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartNumberParameters standardValue;

    @Column(name = "TYPE_")
    private String type;

    @Column(name = "IS_REQUIRED")
    private Boolean isRequired = false;

    @Column(name = "CHECK_WAY")
    private String checkWay;

    @Column(name = "SOURCE", nullable = false)
    @NotNull
    private String source;

    @Column(name = "IS_CALCULATED_RSD")
    private Boolean isCalculatedRsd = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_DESCRIPTIVE_PARAMETER_ID")
    private CustomerDescriptiveParameters customerDescriptiveParameter;

    public void setSource(SourceEnum source) {
        this.source = source == null ? null : source.getId();
    }

    public SourceEnum getSource() {
        return source == null ? null : SourceEnum.fromId(source);
    }

    public void setCheckWay(CalibrationMethodEnum checkWay) {
        this.checkWay = checkWay == null ? null : checkWay.getId();
    }

    public CalibrationMethodEnum getCheckWay() {
        return checkWay == null ? null : CalibrationMethodEnum.fromId(checkWay);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public PartNumberParameters getParameterName() {
        return parameterName;
    }

    public void setParameterName(PartNumberParameters parameterName) {
        this.parameterName = parameterName;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public PartNumberParameters getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(PartNumberParameters standardValue) {
        this.standardValue = standardValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Boolean getCalculatedRsd() {
        return isCalculatedRsd;
    }

    public void setCalculatedRsd(Boolean calculatedRsd) {
        isCalculatedRsd = calculatedRsd;
    }

    public CustomerDescriptiveParameters getCustomerDescriptiveParameter() {
        return customerDescriptiveParameter;
    }

    public void setCustomerDescriptiveParameter(CustomerDescriptiveParameters customerDescriptiveParameter) {
        this.customerDescriptiveParameter = customerDescriptiveParameter;
    }
}
