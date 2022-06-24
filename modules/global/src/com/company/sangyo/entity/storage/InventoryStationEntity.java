package com.company.sangyo.entity.storage;

import com.company.sangyo.entity.basic.Procedure;
import com.company.sangyo.entity.basic.Screen;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "SANGYO_INVENTORY_STATION_ENTITY")
@Entity(name = "sangyo_InventoryStationEntity")
public class InventoryStationEntity extends StandardEntity {
    private static final long serialVersionUID = 3072461089140735835L;

    @Column(name = "INVENTORY_TIME")
    private LocalDateTime inventoryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_CODE_ID")
    private Screen screenCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_NAME_ID")
    private Procedure procedureName;

    @Column(name = "PHYSICAL_NUMBER")
    private String physicalNumber;

    @Column(name = "COMPARE_RESULTS")
    private Boolean compareResults;

    @Lob
    @Column(name = "EXCEPTION_DESCRIPTION")
    private String exceptionDescription;

    @Column(name = "WHETHER_OPERATE")
    private Boolean whetherOperate;

    @Column(name = "OPERATE_RESULT")
    private String operateResult;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "ANNAL_USER")
    private String annalUser;

    @Column(name = "IS_CONFIRMED")
    private Boolean isConfirmed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVENTORY_STATION_RECORDING_ENTITY_ID")
    private InventoryStationRecordingEntity inventoryStationRecordingEntity;

    public InventoryStationRecordingEntity getInventoryStationRecordingEntity() {
        return inventoryStationRecordingEntity;
    }

    public void setInventoryStationRecordingEntity(InventoryStationRecordingEntity inventoryStationRecordingEntity) {
        this.inventoryStationRecordingEntity = inventoryStationRecordingEntity;
    }

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public void setCompareResults(Boolean compareResults) {
        this.compareResults = compareResults;
    }

    public Boolean getCompareResults() {
        return compareResults;
    }

    public String getAnnalUser() {
        return annalUser;
    }

    public void setAnnalUser(String annalUser) {
        this.annalUser = annalUser;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperateResult() {
        return operateResult;
    }

    public void setOperateResult(String operateResult) {
        this.operateResult = operateResult;
    }

    public Boolean getWhetherOperate() {
        return whetherOperate;
    }

    public void setWhetherOperate(Boolean whetherOperate) {
        this.whetherOperate = whetherOperate;
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }

    public void setExceptionDescription(String exceptionDescription) {
        this.exceptionDescription = exceptionDescription;
    }

    public String getPhysicalNumber() {
        return physicalNumber;
    }

    public void setPhysicalNumber(String physicalNumber) {
        this.physicalNumber = physicalNumber;
    }

    public Procedure getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(Procedure procedureName) {
        this.procedureName = procedureName;
    }

    public Screen getScreenCode() {
        return screenCode;
    }

    public void setScreenCode(Screen screenCode) {
        this.screenCode = screenCode;
    }

    public LocalDateTime getInventoryTime() {
        return inventoryTime;
    }

    public void setInventoryTime(LocalDateTime inventoryTime) {
        this.inventoryTime = inventoryTime;
    }
}
