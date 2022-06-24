package com.company.sangyo.entity.basic;

import com.company.sangyo.entity.production.WorkOrder;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Table(name = "SANGYO_PROCEDURE")
@Entity(name = "sangyo_Procedure")
public class Procedure extends StandardEntity {
    private static final long serialVersionUID = -2601340759226188968L;

    @Column(name = "NAME", nullable = false, unique = true)
    @NotNull
    private String name;

    @Column(name = "PROCEDURE_CODE")
    private String procedureCode;

    @Column(name = "PROCEDURE_STATUS")
    private String procedureStatus;

    @Temporal(TemporalType.TIME)
    @Column(name = "STANDING_TIME")
    private Date standingTime;

    @Column(name = "IS_ALLOWED_PDA")
    private Boolean isAllowedPDA = false;

    @Column(name = "REMARK")
    private String remark;

    @OneToMany(mappedBy = "procedure")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<StationAndProcedure> stationAndProcedure;

    @OneToMany(mappedBy = "procedure")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<OperationInstruction> operationInstruction;

    @OneToMany(mappedBy = "procedure")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<ProcessParameters> processParameters;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "procedure")
    private List<GroupValueVerification> groupValueVerification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_ORDER_ID")
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCESS_FLOW_ID")
    private ProcessFlow processFlow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LABEL_TEMPLATE_ID")
    private LabelTemplate labelTemplate;

    @JoinTable(name = "SANGYO_PROCESS_FLOW_PROCEDURE_LINK",
            joinColumns = @JoinColumn(name = "PROCEDURE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PROCESS_FLOW_ID"))
    @ManyToMany
    private List<ProcessFlow> processFlows;

    public void setProcedureStatus(GeneralState procedureStatus) {
        this.procedureStatus = procedureStatus == null ? null : procedureStatus.getId();
    }

    public GeneralState getProcedureStatus() {
        return procedureStatus == null ? null : GeneralState.fromId(procedureStatus);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public Date getStandingTime() {
        return standingTime;
    }

    public void setStandingTime(Date standingTime) {
        this.standingTime = standingTime;
    }

    public Boolean getAllowedPDA() {
        return isAllowedPDA;
    }

    public void setAllowedPDA(Boolean allowedPDA) {
        isAllowedPDA = allowedPDA;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<StationAndProcedure> getStationAndProcedure() {
        return stationAndProcedure;
    }

    public void setStationAndProcedure(List<StationAndProcedure> stationAndProcedure) {
        this.stationAndProcedure = stationAndProcedure;
    }

    public List<OperationInstruction> getOperationInstruction() {
        return operationInstruction;
    }

    public void setOperationInstruction(List<OperationInstruction> operationInstruction) {
        this.operationInstruction = operationInstruction;
    }

    public List<ProcessParameters> getProcessParameters() {
        return processParameters;
    }

    public void setProcessParameters(List<ProcessParameters> processParameters) {
        this.processParameters = processParameters;
    }

    public List<GroupValueVerification> getGroupValueVerification() {
        return groupValueVerification;
    }

    public void setGroupValueVerification(List<GroupValueVerification> groupValueVerification) {
        this.groupValueVerification = groupValueVerification;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public ProcessFlow getProcessFlow() {
        return processFlow;
    }

    public void setProcessFlow(ProcessFlow processFlow) {
        this.processFlow = processFlow;
    }

    public LabelTemplate getLabelTemplate() {
        return labelTemplate;
    }

    public void setLabelTemplate(LabelTemplate labelTemplate) {
        this.labelTemplate = labelTemplate;
    }

    public List<ProcessFlow> getProcessFlows() {
        return processFlows;
    }

    public void setProcessFlows(List<ProcessFlow> processFlows) {
        this.processFlows = processFlows;
    }
}
