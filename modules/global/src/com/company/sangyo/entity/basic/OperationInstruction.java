package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SANGYO_OPERATION_INSTRUCTION")
@Entity(name = "sangyo_OperationInstruction")
public class OperationInstruction extends StandardEntity {
    private static final long serialVersionUID = 1591096661710013053L;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROCEDURE_ID")
    @NotNull
    private Procedure procedure;

    @JoinColumn(name = "ISSUE_PARAMETERS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartNumberParameters issueParameters;

    @Column(name = "IS_CALCULATED_RSD")
    private Boolean isCalculatedRsd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_DESCRIPTIVE_PARAMETER_ID")
    private CustomerDescriptiveParameters customerDescriptiveParameter;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public PartNumberParameters getIssueParameters() {
        return issueParameters;
    }

    public void setIssueParameters(PartNumberParameters issueParameters) {
        this.issueParameters = issueParameters;
    }

    public Boolean getCalculatedRsd() {
        return isCalculatedRsd;
    }

    public void setCalculatedRsd(Boolean calculatedRsd) {
        isCalculatedRsd = calculatedRsd;
    }

    public CustomerDescriptiveParameters getCustomerDescriptiveParameter() {
        return customerDescriptiveParameter;
    }

    public void setCustomerDescriptiveParameter(CustomerDescriptiveParameters customerDescriptiveParameter) {
        this.customerDescriptiveParameter = customerDescriptiveParameter;
    }
}
