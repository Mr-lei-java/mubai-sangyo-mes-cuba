package com.company.sangyo.entity.basic;

import com.company.sangyo.entity.storage.Warehouse;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SANGYO_PROCESS_FLOW")
@Entity(name = "sangyo_ProcessFlow")
public class ProcessFlow extends StandardEntity {
    private static final long serialVersionUID = -5305546323746312158L;

    @Column(name = "NAME", nullable = false, unique = true)
    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ALLOCATION_RULES_ID")
    private OrderAllocationRules orderAllocationRules;

    @Column(name = "IS_ALL_MATCHED")
    private Boolean isAllMatched = false;

    @ManyToMany
    @JoinTable(name = "SANGYO_PROCESS_FLOW_PROCEDURE_LINK",
            joinColumns = @JoinColumn(name = "PROCESS_FLOW_ID"),
            inverseJoinColumns = @JoinColumn(name = "PROCEDURE_ID"))
    private List<Procedure> kube;

    @Column(name = "PROCESS_NUMBER")
    private String processNumber;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "WHETHERTART_WITH_FIRST_PROCESS_PROCESS")
    private Boolean whethertartWithFirstProcessProcess = false;

    @Column(name = "WHETHER_ALL_MATCH")
    private Boolean whetherAllMatch;

    @Column(name = "WHETHER_END_AT_END_PROCESS")
    private Boolean whetherEndAtEndProcess;

    @JoinColumn(name = "OUTPUT_SCREEN_OUTPUT_TRANSFER_LIBRARY_CATEGORY_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Procedure outputScreenOutputTransferLibraryCategory;

    @ManyToMany
    @JoinTable(name = "SANGYO_PROCESS_FLOW_WAREHOUSE_LINK",
            joinColumns = @JoinColumn(name = "PROCESS_FLOW_ID"),
            inverseJoinColumns = @JoinColumn(name = "WAREHOUSE_ID"))
    private List<Warehouse> distributionSourceTransitWarehouse;

    @Column(name = "TYPE_LIBRARY")
    private String typeLibrary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FINISHED_ORDER_ALLOCATION_RULES_ID")
    private OrderAllocationRules finishedOrderAllocationRules;

    @OneToMany(mappedBy = "processFlow")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<ProcessFlowDetailsAndProcedure> processFlowDetailsAndProcedure;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "processFlowId")
    private List<ProcessFlowId> processFlowId;

    public void setTypeLibrary(TypeLibrary typeLibrary) {
        this.typeLibrary = typeLibrary == null ? null : typeLibrary.getId();
    }

    public TypeLibrary getTypeLibrary() {
        return typeLibrary == null ? null : TypeLibrary.fromId(typeLibrary);
    }

    public void setStatus(GeneralState status) {
        this.status = status == null ? null : status.getId();
    }

    public GeneralState getStatus() {
        return status == null ? null : GeneralState.fromId(status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderAllocationRules getOrderAllocationRules() {
        return orderAllocationRules;
    }

    public void setOrderAllocationRules(OrderAllocationRules orderAllocationRules) {
        this.orderAllocationRules = orderAllocationRules;
    }

    public Boolean getAllMatched() {
        return isAllMatched;
    }

    public void setAllMatched(Boolean allMatched) {
        isAllMatched = allMatched;
    }

    public List<Procedure> getKube() {
        return kube;
    }

    public void setKube(List<Procedure> kube) {
        this.kube = kube;
    }

    public String getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(String processNumber) {
        this.processNumber = processNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getWhethertartWithFirstProcessProcess() {
        return whethertartWithFirstProcessProcess;
    }

    public void setWhethertartWithFirstProcessProcess(Boolean whethertartWithFirstProcessProcess) {
        this.whethertartWithFirstProcessProcess = whethertartWithFirstProcessProcess;
    }

    public Boolean getWhetherAllMatch() {
        return whetherAllMatch;
    }

    public void setWhetherAllMatch(Boolean whetherAllMatch) {
        this.whetherAllMatch = whetherAllMatch;
    }

    public Boolean getWhetherEndAtEndProcess() {
        return whetherEndAtEndProcess;
    }

    public void setWhetherEndAtEndProcess(Boolean whetherEndAtEndProcess) {
        this.whetherEndAtEndProcess = whetherEndAtEndProcess;
    }

    public Procedure getOutputScreenOutputTransferLibraryCategory() {
        return outputScreenOutputTransferLibraryCategory;
    }

    public void setOutputScreenOutputTransferLibraryCategory(Procedure outputScreenOutputTransferLibraryCategory) {
        this.outputScreenOutputTransferLibraryCategory = outputScreenOutputTransferLibraryCategory;
    }

    public List<Warehouse> getDistributionSourceTransitWarehouse() {
        return distributionSourceTransitWarehouse;
    }

    public void setDistributionSourceTransitWarehouse(List<Warehouse> distributionSourceTransitWarehouse) {
        this.distributionSourceTransitWarehouse = distributionSourceTransitWarehouse;
    }

    public OrderAllocationRules getFinishedOrderAllocationRules() {
        return finishedOrderAllocationRules;
    }

    public void setFinishedOrderAllocationRules(OrderAllocationRules finishedOrderAllocationRules) {
        this.finishedOrderAllocationRules = finishedOrderAllocationRules;
    }

    public List<ProcessFlowDetailsAndProcedure> getProcessFlowDetailsAndProcedure() {
        return processFlowDetailsAndProcedure;
    }

    public void setProcessFlowDetailsAndProcedure(List<ProcessFlowDetailsAndProcedure> processFlowDetailsAndProcedure) {
        this.processFlowDetailsAndProcedure = processFlowDetailsAndProcedure;
    }

    public List<ProcessFlowId> getProcessFlowId() {
        return processFlowId;
    }

    public void setProcessFlowId(List<ProcessFlowId> processFlowId) {
        this.processFlowId = processFlowId;
    }
}
