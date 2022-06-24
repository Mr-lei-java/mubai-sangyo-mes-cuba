package com.company.sangyo.entity.storage;

import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.production.DispatchList;
import com.company.sangyo.entity.production.ProductionRecords;
import com.company.sangyo.entity.production.WorkOrder;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SANGYO_QUALITY_TRACEABILITY")
@Entity(name = "sangyo_QualityTraceability")
public class QualityTraceability extends StandardEntity {
    private static final long serialVersionUID = -3242395007803885650L;

    @Column(name = "GET_IN_TIME")
    private LocalDateTime getInTime;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "qualityTraceability")
    private List<ProductionRecords> productionRecords;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_ORDER_ID")
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SCREEN_ID")
    @NotNull
    private Screen screen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISPATCH_LIST_ID")
    private DispatchList dispatchList;

    @Column(name = "OUTBOUND_TIME")
    private LocalDateTime outboundTime;

    @Column(name = "OUT_WAREHOUSE_TIME")
    private LocalDateTime outWarehouseTime;

    @JoinColumn(name = "OPERATING_PERSONNEL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User operatingPersonnel;

    @JoinColumn(name = "OUTBOUND_OPERATING_PERSONNEL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User outboundOperatingPersonnel;

    @Column(name = "STATE")
    private String state;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public LocalDateTime getGetInTime() {
        return getInTime;
    }

    public void setGetInTime(LocalDateTime getInTime) {
        this.getInTime = getInTime;
    }

    public List<ProductionRecords> getProductionRecords() {
        return productionRecords;
    }

    public void setProductionRecords(List<ProductionRecords> productionRecords) {
        this.productionRecords = productionRecords;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public DispatchList getDispatchList() {
        return dispatchList;
    }

    public void setDispatchList(DispatchList dispatchList) {
        this.dispatchList = dispatchList;
    }

    public LocalDateTime getOutboundTime() {
        return outboundTime;
    }

    public void setOutboundTime(LocalDateTime outboundTime) {
        this.outboundTime = outboundTime;
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

    public User getOutboundOperatingPersonnel() {
        return outboundOperatingPersonnel;
    }

    public void setOutboundOperatingPersonnel(User outboundOperatingPersonnel) {
        this.outboundOperatingPersonnel = outboundOperatingPersonnel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
