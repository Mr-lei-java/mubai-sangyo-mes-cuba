package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SANGYO_CUSTOMER_ANDCUSTOMER_DESCRIPTIVE_PARAMETERS")
@Entity(name = "sangyo_CustomerAndcustomerDescriptiveParameters")
public class CustomerAndcustomerDescriptiveParameters extends StandardEntity {
    private static final long serialVersionUID = 2875818580274928199L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CUSTOMER_ID_ID")
    @NotNull
    private Customer customerId;

    @JoinColumn(name = "CUSTOMER_DESCRIPTIVE_PARAMETERS_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private CustomerDescriptiveParameters customerDescriptiveParameters;

    @Column(name = "TYPE_")
    private String type;

    @Column(name = "PARAMETERS_VALUE")
    private String parametersValue;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPLOAD_FILES_ID")
    private FileDescriptor uploadFiles;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "STANDARD_DEVIATION")
    private Boolean standardDeviation;

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public CustomerDescriptiveParameters getCustomerDescriptiveParameters() {
        return customerDescriptiveParameters;
    }

    public void setCustomerDescriptiveParameters(CustomerDescriptiveParameters customerDescriptiveParameters) {
        this.customerDescriptiveParameters = customerDescriptiveParameters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParametersValue() {
        return parametersValue;
    }

    public void setParametersValue(String parametersValue) {
        this.parametersValue = parametersValue;
    }

    public FileDescriptor getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(FileDescriptor uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Boolean standardDeviation) {
        this.standardDeviation = standardDeviation;
    }
}
