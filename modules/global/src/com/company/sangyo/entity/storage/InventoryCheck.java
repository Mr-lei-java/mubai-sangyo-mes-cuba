package com.company.sangyo.entity.storage;

import com.company.sangyo.entity.basic.Screen;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "SANGYO_INVENTORY_CHECK")
@Entity(name = "sangyo_InventoryCheck")
public class InventoryCheck extends StandardEntity {
    private static final long serialVersionUID = 258701451336859706L;

    @Column(name = "INVENTORY_TIME")
    private LocalDateTime inventoryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_CODE_ID")
    private Screen screenCode;

    @Column(name = "WAREHOUSE_NAME")
    private String warehouseName;

    @Column(name = "PHYSICAL_NUMBER")
    private String physicalNumber;

    @Column(name = "COMPARE_RESULTS")
    private Boolean compareResults;

    @Column(name = "EXCEPTION_DESCRIPTION")
    private String exceptionDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANNAL_USER_ID")
    private User annalUser;

    @Column(name = "WHETHER_OPERATE")
    private String whetherOperate;

    @Column(name = "OPERATE_RESULT")
    private String operateResult;

    @Column(name = "IS_CONFIRMED")
    private String isConfirmed = String.valueOf(ConfirmedEnum.UNCONFIRMED);

    @Column(name = "REMARK")
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVENTORY_RECORDING_ID")
    private InventoryRecording inventoryRecording;

    public ConfirmedEnum getIsConfirmed() {
        return isConfirmed == null ? null : ConfirmedEnum.fromId(isConfirmed);
    }

    public void setIsConfirmed(ConfirmedEnum isConfirmed) {
        this.isConfirmed = isConfirmed == null ? null : isConfirmed.getId();
    }

    public InventoryRecording getInventoryRecording() {
        return inventoryRecording;
    }

    public void setInventoryRecording(InventoryRecording inventoryRecording) {
        this.inventoryRecording = inventoryRecording;
    }

    public User getAnnalUser() {
        return annalUser;
    }

    public void setAnnalUser(User annalUser) {
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

    public String getWhetherOperate() {
        return whetherOperate;
    }

    public void setWhetherOperate(String whetherOperate) {
        this.whetherOperate = whetherOperate;
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }

    public void setExceptionDescription(String exceptionDescription) {
        this.exceptionDescription = exceptionDescription;
    }

    public Boolean getCompareResults() {
        return compareResults;
    }

    public void setCompareResults(Boolean compareResults) {
        this.compareResults = compareResults;
    }

    public String getPhysicalNumber() {
        return physicalNumber;
    }

    public void setPhysicalNumber(String physicalNumber) {
        this.physicalNumber = physicalNumber;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
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

    @Override
    public String toString() {
        return "InventoryCheck{" +
                "inventoryTime=" + inventoryTime +
                ", screenCode=" + screenCode +
                ", warehouseName='" + warehouseName + '\'' +
                ", physicalNumber='" + physicalNumber + '\'' +
                ", compareResults=" + compareResults +
                ", exceptionDescription='" + exceptionDescription + '\'' +
                ", annalUser=" + annalUser +
                ", whetherOperate='" + whetherOperate + '\'' +
                ", operateResult='" + operateResult + '\'' +
                ", isConfirmed='" + isConfirmed + '\'' +
                ", remark='" + remark + '\'' +
                ", inventoryRecording=" + inventoryRecording +
                '}';
    }
}
