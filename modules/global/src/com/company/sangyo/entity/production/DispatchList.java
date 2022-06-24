package com.company.sangyo.entity.production;

import com.company.sangyo.entity.basic.Procedure;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Table(name = "SANGYO_DISPATCH_LIST")
@Entity(name = "sangyo_DispatchList")
public class DispatchList extends StandardEntity {
    private static final long serialVersionUID = -6438573401461867065L;

    @Column(name = "NAME", nullable = false, unique = true)
    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_ID")
    private Procedure procedure;

    @Column(name = "ISSUE_TIME")
    private LocalDateTime issueTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_ORDER_ID")
    private WorkOrder workOrder;

    @Column(name = "PLANNED_QUANTITY")
    private Integer plannedQuantity;

    @Column(name = "RECEIVE_COUNT")
    private Integer receiveCount;

    @Column(name = "ACCOMPLISH_QUANTITY")
    private Integer accomplishQuantity;

    @Column(name = "STATUS")
    private String status;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "dispatchList")
    private List<DispatchListAndScreen> screens;

    @Column(name = "TYPE_")
    private String type;

    @Column(name = "WHETHER_CHECK")
    private Boolean whetherCheck;

    @Column(name = "ISSUE_QUANTITY")
    private Integer issueQuantity;

    @Column(name = "DISPATCH_LIST_ACCOMPLISH_QUANTITY")
    private Integer dispatchListAccomplishQuantity;

    @Column(name = "WHETHER_MARK_RED")
    private Boolean whetherMarkRed;

    @Column(name = "DUE_TIME")
    @Temporal(TemporalType.DATE)
    private Date dueTime;

    public void setType(DispatchListType type) {
        this.type = type == null ? null : type.getId();
    }

    public DispatchListType getType() {
        return type == null ? null : DispatchListType.fromId(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Integer getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(Integer plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    public Integer getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Integer getAccomplishQuantity() {
        return accomplishQuantity;
    }

    public void setAccomplishQuantity(Integer accomplishQuantity) {
        this.accomplishQuantity = accomplishQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DispatchListAndScreen> getScreens() {
        return screens;
    }

    public void setScreens(List<DispatchListAndScreen> screens) {
        this.screens = screens;
    }

    public Boolean getWhetherCheck() {
        return whetherCheck;
    }

    public void setWhetherCheck(Boolean whetherCheck) {
        this.whetherCheck = whetherCheck;
    }

    public Integer getIssueQuantity() {
        return issueQuantity;
    }

    public void setIssueQuantity(Integer issueQuantity) {
        this.issueQuantity = issueQuantity;
    }

    public Integer getDispatchListAccomplishQuantity() {
        return dispatchListAccomplishQuantity;
    }

    public void setDispatchListAccomplishQuantity(Integer dispatchListAccomplishQuantity) {
        this.dispatchListAccomplishQuantity = dispatchListAccomplishQuantity;
    }

    public Boolean getWhetherMarkRed() {
        return whetherMarkRed;
    }

    public void setWhetherMarkRed(Boolean whetherMarkRed) {
        this.whetherMarkRed = whetherMarkRed;
    }

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }
}
