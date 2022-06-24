package com.company.sangyo.entity.production;

import com.company.sangyo.entity.basic.Screen;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_WORK_ORDER_AND_SCREEN")
@Entity(name = "sangyo_WorkOrderAndScreen")
public class WorkOrderAndScreen extends StandardEntity {
    private static final long serialVersionUID = -4130364463683847696L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_ORDER_ID")
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_ID")
    private Screen screen;

    @Column(name = "IS_DISTRIBUTED")
    private Boolean isDistributed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRE_WORK_ORDER_ID")
    private WorkOrder preWorkOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEXT_WORK_ORDER_ID")
    private WorkOrder nextWorkOrder;

    @Column(name = "WORK_ORDER_SCREEN_STATUS")
    private String workOrderScreenStatus;

    public String getWorkOrderScreenStatus() {
        return workOrderScreenStatus;
    }

    public void setWorkOrderScreenStatus(String workOrderScreenStatus) {
        this.workOrderScreenStatus = workOrderScreenStatus;
    }

    public WorkOrder getNextWorkOrder() {
        return nextWorkOrder;
    }

    public void setNextWorkOrder(WorkOrder nextWorkOrder) {
        this.nextWorkOrder = nextWorkOrder;
    }

    public WorkOrder getPreWorkOrder() {
        return preWorkOrder;
    }

    public void setPreWorkOrder(WorkOrder preWorkOrder) {
        this.preWorkOrder = preWorkOrder;
    }

    public Boolean getIsDistributed() {
        return isDistributed;
    }

    public void setIsDistributed(Boolean isDistributed) {
        this.isDistributed = isDistributed;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }
}
