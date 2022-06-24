package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_IN_PLANT_PART_NUMBER_AND_PART_NUMBER_PARAMETERS")
@Entity(name = "sangyo_InPlantPartNumberAndPartNumberParameters")
public class InPlantPartNumberAndPartNumberParameters extends StandardEntity {
    private static final long serialVersionUID = 3346832320086905871L;

    @Column(name = "SERIAL_NUMBER")
    private Integer serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_PLANT_PART_NUMBER_ID")
    private InPlantPartNumber inPlantPartNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PART_NUMBER_PARAMETER_ID_ID")
    private PartNumberParameters partNumberParameterId;

    @Column(name = "TYPE_")
    private String type;

    @JoinColumn(name = "UPLOAD_FILES_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private SOPManagement uploadFiles;

    @Column(name = "PARAMETER_VALUE")
    private String parameterValue;

    @Column(name = "GROUP_")
    private String group;

    @Column(name = "TOLERANCE")
    private String tolerance;

    @Column(name = "RANGE_")
    private String range;

    @Column(name = "REMARK")
    private String remark;

    public void setType(ParameterstypeEnum type) {
        this.type = type == null ? null : type.getId();
    }

    public ParameterstypeEnum getType() {
        return type == null ? null : ParameterstypeEnum.fromId(type);
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public InPlantPartNumber getInPlantPartNumber() {
        return inPlantPartNumber;
    }

    public void setInPlantPartNumber(InPlantPartNumber inPlantPartNumber) {
        this.inPlantPartNumber = inPlantPartNumber;
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

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTolerance() {
        return tolerance;
    }

    public void setTolerance(String tolerance) {
        this.tolerance = tolerance;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
