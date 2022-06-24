package com.company.sangyo.entity.storage;

import com.company.sangyo.entity.basic.Procedure;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SANGYO_INVENTORY_STATION_RECORDING_ENTITY")
@Entity(name = "sangyo_InventoryStationRecordingEntity")
public class InventoryStationRecordingEntity extends StandardEntity {
    private static final long serialVersionUID = 8696359097572125325L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_NAME_ID")
    private Procedure procedureName;

    @Column(name = "INVENTORY_USER")
    private String inventoryUser;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "TOTAL")
    private String total;

    @Column(name = "COIL_DEFICIT")
    private String coilDeficit;

    @Column(name = "SURPLUS_COIL")
    private String surplusCoil;

    @Column(name = "REMARK")
    private String remark;

    @OneToMany(mappedBy = "inventoryStationRecordingEntity")
    private List<InventoryStationEntity> inventoryStation;

    public List<InventoryStationEntity> getInventoryStation() {
        return inventoryStation;
    }

    public void setInventoryStation(List<InventoryStationEntity> inventoryStation) {
        this.inventoryStation = inventoryStation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSurplusCoil() {
        return surplusCoil;
    }

    public void setSurplusCoil(String surplusCoil) {
        this.surplusCoil = surplusCoil;
    }

    public String getCoilDeficit() {
        return coilDeficit;
    }

    public void setCoilDeficit(String coilDeficit) {
        this.coilDeficit = coilDeficit;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getInventoryUser() {
        return inventoryUser;
    }

    public void setInventoryUser(String inventoryUser) {
        this.inventoryUser = inventoryUser;
    }

    public Procedure getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(Procedure procedureName) {
        this.procedureName = procedureName;
    }
}
