package com.company.sangyo.entity.storage;

import com.company.sangyo.entity.basic.InPlantPartNumber;
import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.production.WorkOrder;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "SANGYO_FINISHED")
@Entity(name = "sangyo_Finished")
public class Finished extends StandardEntity {
    private static final long serialVersionUID = -8626558075448200831L;

    @JoinColumn(name = "SCREEN_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private Screen screenId;

    @Column(name = "REWORK_TYPE")
    private String reworkType;

    @JoinColumn(name = "WAREHOUSE_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_PLANT_PART_NUMBER_ID")
    private InPlantPartNumber inPlantPartNumber;

    @Column(name = "OUT_WAREHOUSE_TIME")
    private LocalDateTime outWarehouseTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPERATING_PERSONNEL_ID")
    private User operatingPersonnel;

    @Column(name = "WAREHOUSING_TIME")
    private LocalDateTime warehousingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_ORDER_ID")
    private WorkOrder workOrder;

    @Column(name = "PROCESSING_MODE")
    private String processingMode;

    @Column(name = "PROCESSING_TIME")
    private LocalDateTime processingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REL_WORK_ORDER_ID")
    private WorkOrder relWorkOrder;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "REMARK1")
    private String remark1;

    public Finished() {
        this(new Builder());
    }

    public Finished(Builder builder) {
        this.screenId = builder.screenId;
        this.workOrder = builder.workOrder;
        this.warehousingTime = builder.warehousingTime;
        this.remark1 = builder.remark1;
        this.inPlantPartNumber = builder.inPlantPartNumber;
        this.warehouseId = builder.warehouseId;
        this.status = builder.status;
        this.remark = builder.remark;
    }

    public ReworkType getReworkType() {
        return reworkType == null ? null : ReworkType.fromId(reworkType);
    }

    public void setReworkType(ReworkType reworkType) {
        this.reworkType = reworkType == null ? null : reworkType.getId();
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public LocalDateTime getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(LocalDateTime processingTime) {
        this.processingTime = processingTime;
    }

    public ProcessingMode getProcessingMode() {
        return processingMode == null ? null : ProcessingMode.fromId(processingMode);
    }

    public void setProcessingMode(ProcessingMode processingMode) {
        this.processingMode = processingMode == null ? null : processingMode.getId();
    }

    public LocalDateTime getOutWarehouseTime() {
        return outWarehouseTime;
    }

    public void setOutWarehouseTime(LocalDateTime outWarehouseTime) {
        this.outWarehouseTime = outWarehouseTime;
    }

    public LocalDateTime getWarehousingTime() {
        return warehousingTime;
    }

    public void setWarehousingTime(LocalDateTime warehousingTime) {
        this.warehousingTime = warehousingTime;
    }

    public User getOperatingPersonnel() {
        return operatingPersonnel;
    }

    public void setOperatingPersonnel(User operatingPersonnel) {
        this.operatingPersonnel = operatingPersonnel;
    }

    public FinishedScreenStatus getStatus() {
        return status == null ? null : FinishedScreenStatus.fromId(status);
    }

    public void setStatus(FinishedScreenStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public WorkOrder getRelWorkOrder() {
        return relWorkOrder;
    }

    public void setRelWorkOrder(WorkOrder relWorkOrder) {
        this.relWorkOrder = relWorkOrder;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public InPlantPartNumber getInPlantPartNumber() {
        return inPlantPartNumber;
    }

    public void setInPlantPartNumber(InPlantPartNumber inPlantPartNumber) {
        this.inPlantPartNumber = inPlantPartNumber;
    }

    public void setScreenId(Screen screenId) {
        this.screenId = screenId;
    }

    public Screen getScreenId() {
        return screenId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Warehouse getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Warehouse warehouseId) {
        this.warehouseId = warehouseId;
    }
    public static class Builder {
        Screen screenId;
        WorkOrder workOrder;
        LocalDateTime warehousingTime;
        String remark1;
        InPlantPartNumber inPlantPartNumber;
        Warehouse warehouseId;
        String status;
        String remark;

        public Builder setScreenId(Screen screenId) {
            this.screenId = screenId;
            return this;
        }

        public Builder setWorkOrder(WorkOrder workOrder) {
            this.workOrder = workOrder;
            return this;
        }

        public Builder setWarehousingTime(LocalDateTime warehousingTime) {
            this.warehousingTime = warehousingTime;
            return this;
        }

        public Builder setRemark1(String remark1) {
            this.remark1 = remark1;
            return this;
        }

        public Builder setInPlantPartNumber(InPlantPartNumber inPlantPartNumber) {
            this.inPlantPartNumber = inPlantPartNumber;
            return this;
        }

        public Builder setWarehouseId(Warehouse warehouseId) {
            this.warehouseId = warehouseId;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setRemark(String remark) {
            this.remark = remark;
            return this;
        }

        public Builder() {
            screenId = screenId;
            workOrder = workOrder;
            warehousingTime = warehousingTime;
            remark1 = remark1;
            inPlantPartNumber = inPlantPartNumber;
            warehouseId = warehouseId;
            status = status;
            remark = remark;
        }

        Builder(Finished finished) {
            this.screenId = finished.screenId;
            this.workOrder = finished.workOrder;
            this.warehousingTime = finished.warehousingTime;
            this.remark1 = finished.remark1;
            this.inPlantPartNumber = finished.inPlantPartNumber;
            this.warehouseId = finished.warehouseId;
            this.status = finished.status;
            this.remark = finished.remark;
        }

        public Finished build() {
            return new Finished(this);
        }
    }
}
