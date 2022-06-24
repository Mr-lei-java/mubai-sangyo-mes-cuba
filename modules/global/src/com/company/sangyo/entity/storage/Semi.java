package com.company.sangyo.entity.storage;

import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.production.WorkOrder;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "SANGYO_SEMI")
@Entity(name = "sangyo_Semi")
public class Semi extends StandardEntity {
    private static final long serialVersionUID = -5230157617241385389L;

    @JoinColumn(name = "WAREHOUSE_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouseId;

    @JoinColumn(name = "SCREEN_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private Screen screenId;

    @Column(name = "WAREHOUSING_TIME")
    private LocalDateTime warehousingTime;

    @Column(name = "OUT_WAREHOUSE_TIME")
    private LocalDateTime outWarehouseTime;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REWORK_TYPE")
    private String reworkType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPERATING_PERSONNEL_ID")
    private User operatingPersonnel;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "REMARK4")
    private String remark4;

    @Column(name = "PROCESSING_MODE")
    private String processingMode;

    @Column(name = "PROCESSING_TIME")
    private LocalDateTime processingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_ORDER_ID")
    private WorkOrder workOrder;

    @Column(name = "REMARK2")
    private String remark2;

    @Column(name = "REMARK3")
    private String remark3;

    public ReworkType getReworkType() {
        return reworkType == null ? null : ReworkType.fromId(reworkType);
    }

    public void setReworkType(ReworkType reworkType) {
        this.reworkType = reworkType == null ? null : reworkType.getId();
    }

    public Semi() {
        this(new Builder());
    }

    public Semi(Builder builder) {
        this.status = builder.status;
        this.screenId = builder.screenId;
        this.warehousingTime = builder.warehousingTime;
        this.warehouseId = builder.warehouseId;
        this.workOrder = builder.workOrder;
        this.remark4 = builder.remark4;
        this.remark = builder.remark;
    }

    public String getRemark4() {
        return remark4;
    }

    public void setRemark4(String remark4) {
        this.remark4 = remark4;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOutWarehouseTime() {
        return outWarehouseTime;
    }

    public void setOutWarehouseTime(LocalDateTime outWarehouseTime) {
        this.outWarehouseTime = outWarehouseTime;
    }

    public User getOperatingPersonnel() {
        return operatingPersonnel;
    }

    public void setOperatingPersonnel(User operatingPersonnel) {
        this.operatingPersonnel = operatingPersonnel;
    }

    public LocalDateTime getWarehousingTime() {
        return warehousingTime;
    }

    public void setWarehousingTime(LocalDateTime warehousingTime) {
        this.warehousingTime = warehousingTime;
    }

    public void setScreenId(Screen screenId) {
        this.screenId = screenId;
    }

    public Screen getScreenId() {
        return screenId;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
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

    @Override
    public String toString() {
        return "Semi{" +
                "warehouseId=" + warehouseId +
                ", screenId=" + screenId +
                ", warehousingTime=" + warehousingTime +
                ", outWarehouseTime=" + outWarehouseTime +
                ", status='" + status + '\'' +
                ", operatingPersonnel=" + operatingPersonnel +
                ", remark='" + remark + '\'' +
                ", remark4='" + remark4 + '\'' +
                ", processingMode='" + processingMode + '\'' +
                ", processingTime=" + processingTime +
                ", workOrder=" + workOrder +
                ", remark2='" + remark2 + '\'' +
                ", remark3='" + remark3 + '\'' +
                '}';
    }

    /**
     * 静态内部类 Builder
     */
    public static class Builder {
        public String status;
        public Screen screenId;
        public LocalDateTime warehousingTime;
        public Warehouse warehouseId;
        public WorkOrder workOrder;
        public String remark4;
        public String remark;

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setScreenId(Screen screenId) {
            this.screenId = screenId;
            return this;
        }

        public Builder setWarehousingTime(LocalDateTime warehousingTime) {
            this.warehousingTime = warehousingTime;
            return this;
        }

        public Builder setWarehouseId(Warehouse warehouseId) {
            this.warehouseId = warehouseId;
            return this;
        }

        public Builder setWorkOrder(WorkOrder workOrder) {
            this.workOrder = workOrder;
            return this;
        }

        public Builder setRemark4(String remark4) {
            this.remark4 = remark4;
            return this;
        }

        public Builder setRemark(String remark) {
            this.remark = remark;
            return this;
        }

        //构造方法
        public Builder() {
            status = status;
            screenId = screenId;
            warehousingTime = warehousingTime;
            warehouseId = warehouseId;
            workOrder = workOrder;
            remark4 = remark4;
            remark = remark;
        }

        //构造方法
        Builder(Semi semi) {
            this.status = semi.status;
            this.screenId = semi.screenId;
            this.warehousingTime = semi.warehousingTime;
            this.warehouseId = semi.warehouseId;
            this.workOrder = semi.workOrder;
            this.remark4 = semi.remark4;
            this.remark = semi.remark;
        }

        //构建一个实体
        public Semi build() {
            return new Semi(this);
        }
    }
}
