package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

/**
 * @author leitengfei
 */
@Table(name = "SANGYO_PROCESS_FLOW_ID")
@Entity(name = "sangyo_ProcessFlowId")
public class ProcessFlowId extends StandardEntity {
    private static final long serialVersionUID = 8104855740033526754L;

    @Column(name = "SERIAL_NUMBER")
    private Integer serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCESS_FLOW_ID_ID")
    private ProcessFlow processFlowId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_ID")
    private Procedure procedure;

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ProcessFlow getProcessFlowId() {
        return processFlowId;
    }

    public void setProcessFlowId(ProcessFlow processFlowId) {
        this.processFlowId = processFlowId;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }
}
