package com.company.sangyo.entity.storage;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SANGYO_INVENTORY_RECORDING")
@Entity(name = "sangyo_InventoryRecording")
public class InventoryRecording extends StandardEntity {
    private static final long serialVersionUID = -8749844328306836809L;

    @Column(name = "WAREHOUSE_NAME")
    private String warehouseName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVENTORY_USER_ID")
    private User inventoryUser;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "TOTAL")
    private Integer total;

    @Column(name = "WAREHOUSE_QUANTITY")
    private Integer warehouseQuantity;

    @Column(name = "NORMAL_NUMBER")
    private Integer normalNumber;

    @Column(name = "COIL_DEFICIT")
    private Integer coilDeficit;

    @Column(name = "SURPLUS_COIL")
    private Integer surplusCoil;

    @Column(name = "REMARKS")
    private String remarks;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "inventoryRecording")
    private List<InventoryCheck> inventoryCheck;

    public Integer getNormalNumber() {
        return normalNumber;
    }

    public void setNormalNumber(Integer normalNumber) {
        this.normalNumber = normalNumber;
    }

    public Integer getWarehouseQuantity() {
        return warehouseQuantity;
    }

    public void setWarehouseQuantity(Integer warehouseQuantity) {
        this.warehouseQuantity = warehouseQuantity;
    }

    public List<InventoryCheck> getInventoryCheck() {
        return inventoryCheck;
    }

    public void setInventoryCheck(List<InventoryCheck> inventoryCheck) {
        this.inventoryCheck = inventoryCheck;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSurplusCoil() {
        return surplusCoil;
    }

    public void setSurplusCoil(Integer surplusCoil) {
        this.surplusCoil = surplusCoil;
    }

    public Integer getCoilDeficit() {
        return coilDeficit;
    }

    public void setCoilDeficit(Integer coilDeficit) {
        this.coilDeficit = coilDeficit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
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

    public User getInventoryUser() {
        return inventoryUser;
    }

    public void setInventoryUser(User inventoryUser) {
        this.inventoryUser = inventoryUser;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}
