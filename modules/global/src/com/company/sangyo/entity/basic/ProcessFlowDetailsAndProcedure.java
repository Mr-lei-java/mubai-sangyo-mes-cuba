package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_PROCESS_FLOW_DETAILS_AND_PROCEDURE")
@Entity(name = "sangyo_ProcessFlowDetailsAndProcedure")
public class ProcessFlowDetailsAndProcedure extends StandardEntity {
    private static final long serialVersionUID = 2696311074390012120L;

    @Column(name = "IS_FIRST_PROCEDURE")
    private Boolean isFirstProcedure = false;

    @Column(name = "IS_LAST_PROCEDURE")
    private Boolean isLastProcedure = false;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "SERIAL_NUMBER")
    private Integer serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCESS_FLOW_ID")
    private ProcessFlow processFlow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_ID")
    private Procedure procedure;

    @JoinColumn(name = "PRE_PROCEDURE_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Procedure preProcedureId;

    public Boolean getFirstProcedure() {
        return isFirstProcedure;
    }

    public void setFirstProcedure(Boolean firstProcedure) {
        isFirstProcedure = firstProcedure;
    }

    public Boolean getLastProcedure() {
        return isLastProcedure;
    }

    public void setLastProcedure(Boolean lastProcedure) {
        isLastProcedure = lastProcedure;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ProcessFlow getProcessFlow() {
        return processFlow;
    }

    public void setProcessFlow(ProcessFlow processFlow) {
        this.processFlow = processFlow;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public Procedure getPreProcedureId() {
        return preProcedureId;
    }

    public void setPreProcedureId(Procedure preProcedureId) {
        this.preProcedureId = preProcedureId;
    }
}
