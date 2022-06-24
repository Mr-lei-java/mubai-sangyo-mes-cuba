package com.company.sangyo.entity.production;

import com.company.sangyo.entity.basic.Customer;
import com.company.sangyo.entity.basic.InPlantPartNumber;
import com.company.sangyo.entity.basic.Procedure;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "SANGYO_ISSUE_KANBAN_AND_DISPATCH_LIST")
@Entity(name = "sangyo_IssueKanbanAndDispatchList")
public class IssueKanbanAndDispatchList extends StandardEntity {
    private static final long serialVersionUID = 3991415565590203374L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ISSUE_KANBAN_ID")
    private IssueKanban issueKanban;

    @Column(name = "ISSUE_QUANTITY")
    private Integer issueQuantity;

    @Column(name = "DUE_TIME")
    private LocalDateTime dueTime;

    @Column(name = "INITIAL_QUANTITY")
    private Integer initialQuantity;

    @Column(name = "PROCEDURE_ACCOMPLISH_QUANTITY")
    private Integer procedureAccomplishQuantity;

    @Column(name = "PLANNED_QUANTITY")
    private Integer plannedQuantity;

    @Column(name = "ISSUE_TIME")
    private LocalDateTime issueTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ISSUE_USER_ID")
    private User issueUser;

    @JoinColumn(name = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(name = "DELIVERY_TIME")
    private LocalDateTime deliveryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISPATCH_LIST_ID")
    private DispatchList dispatchList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_PLANT_PART_NUMBER_ID")
    private InPlantPartNumber inPlantPartNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_ID")
    private Procedure procedure;

    @Column(name = "DISPATCH_LIST_ACCOMPLISH_QUANTITY")
    private Integer dispatchListAccomplishQuantity;

    @Column(name = "PERFECTION_RATE")
    private String perfectionRate;

    @Column(name = "WORK_ORDER_NUMBER")
    private String workOrderNumber;

    @Column(name = "FACTORY_ORDER_NUMBER")
    private String factoryOrderNumber;

    @Column(name = "WHETHER_MARK_RED")
    private Boolean whetherMarkRed = false;

    @Column(name = "WHETHER_END")
    private Boolean whetherEnd = false;

    public IssueKanban getIssueKanban() {
        return issueKanban;
    }

    public void setIssueKanban(IssueKanban issueKanban) {
        this.issueKanban = issueKanban;
    }

    public Integer getIssueQuantity() {
        return issueQuantity;
    }

    public void setIssueQuantity(Integer issueQuantity) {
        this.issueQuantity = issueQuantity;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public Integer getProcedureAccomplishQuantity() {
        return procedureAccomplishQuantity;
    }

    public void setProcedureAccomplishQuantity(Integer procedureAccomplishQuantity) {
        this.procedureAccomplishQuantity = procedureAccomplishQuantity;
    }

    public Integer getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(Integer plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }

    public User getIssueUser() {
        return issueUser;
    }

    public void setIssueUser(User issueUser) {
        this.issueUser = issueUser;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public DispatchList getDispatchList() {
        return dispatchList;
    }

    public void setDispatchList(DispatchList dispatchList) {
        this.dispatchList = dispatchList;
    }

    public InPlantPartNumber getInPlantPartNumber() {
        return inPlantPartNumber;
    }

    public void setInPlantPartNumber(InPlantPartNumber inPlantPartNumber) {
        this.inPlantPartNumber = inPlantPartNumber;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public Integer getDispatchListAccomplishQuantity() {
        return dispatchListAccomplishQuantity;
    }

    public void setDispatchListAccomplishQuantity(Integer dispatchListAccomplishQuantity) {
        this.dispatchListAccomplishQuantity = dispatchListAccomplishQuantity;
    }

    public String getPerfectionRate() {
        return perfectionRate;
    }

    public void setPerfectionRate(String perfectionRate) {
        this.perfectionRate = perfectionRate;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public String getFactoryOrderNumber() {
        return factoryOrderNumber;
    }

    public void setFactoryOrderNumber(String factoryOrderNumber) {
        this.factoryOrderNumber = factoryOrderNumber;
    }

    public Boolean getWhetherMarkRed() {
        return whetherMarkRed;
    }

    public void setWhetherMarkRed(Boolean whetherMarkRed) {
        this.whetherMarkRed = whetherMarkRed;
    }

    public Boolean getWhetherEnd() {
        return whetherEnd;
    }

    public void setWhetherEnd(Boolean whetherEnd) {
        this.whetherEnd = whetherEnd;
    }
}
