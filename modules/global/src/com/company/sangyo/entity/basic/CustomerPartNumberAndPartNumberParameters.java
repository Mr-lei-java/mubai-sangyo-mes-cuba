package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_CUSTOMER_PART_NUMBER_AND_PART_NUMBER_PARAMETERS")
@Entity(name = "sangyo_CustomerPartNumberAndPartNumberParameters")
public class CustomerPartNumberAndPartNumberParameters extends StandardEntity {
    private static final long serialVersionUID = -3360486293183386921L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_PART_NUMBER_ID")
    private CustomerPartNumber customerPartNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PART_NUMBER_PARAMETER_ID_ID")
    private PartNumberParameters partNumberParameterId;

    @Column(name = "TYPE_")
    private String type;

    @JoinColumn(name = "UPLOAD_FILES_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private SOPManagement uploadFiles;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    @Column(name = "PARAMETER_VALUE")
    private String parameterValue;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "RANGE_")
    private String range;

    @Column(name = "TOLERANCE")
    private String tolerance;

    @Column(name = "GROUP_")
    private String group;

    public void setType(ParameterstypeEnum type) {
        this.type = type == null ? null : type.getId();
    }

    public ParameterstypeEnum getType() {
        return type == null ? null : ParameterstypeEnum.fromId(type);
    }

    public CustomerPartNumber getCustomerPartNumber() {
        return customerPartNumber;
    }

    public void setCustomerPartNumber(CustomerPartNumber customerPartNumber) {
        this.customerPartNumber = customerPartNumber;
    }

    public PartNumberParameters getPartNumberParameterId() {
        return partNumberParameterId;
    }

    public void setPartNumberParameterId(PartNumberParameters partNumberParameterId) {
        this.partNumberParameterId = partNumberParameterId;
    }

    public SOPManagement getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(SOPManagement uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getTolerance() {
        return tolerance;
    }

    public void setTolerance(String tolerance) {
        this.tolerance = tolerance;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
