package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author leitengfei
 */
@Table(name = "SANGYO_CUSTOMER")
@Entity(name = "sangyo_Customer")
public class Customer extends StandardEntity {
    private static final long serialVersionUID = -8907468467096085021L;

    @Column(name = "CUSTOMER_CODING", nullable = false, unique = true)
    @NotNull
    private String customerCoding;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "DELIVERY_ADDRESS")
    private String deliveryAddress;

    @Column(name = "CONTACTS")
    private String contacts;

    @Column(name = "CONTACT_NUMBER")
    private String contactNumber;

    @Column(name = "LABEL_TEMPLATE")
    private String labelTemplate;

    @Column(name = "PERFECT_STATE")
    private String perfectState;

    @OnDelete(DeletePolicy.CASCADE)
    @JoinColumn(name = "SHIPPING_REPORT_PRINT_TEMPLATE_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private FileDescriptor shippingReportPrintTemplate;

    @Column(name = "CUSTOMER_SERVICE_TECHNICIAN")
    private String customerServiceTechnician;

    @Column(name = "CLIENTS_CATEGORIES", nullable = false)
    @NotNull
    private String clientsCategories;

    @OneToMany(mappedBy = "customerId")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<CustomerAndcustomerDescriptiveParameters> customerAndcustomerDescriptiveParameters;

    @Column(name = "REMARK")
    private String remark;

    public String getCustomerCoding() {
        return customerCoding;
    }

    public void setCustomerCoding(String customerCoding) {
        this.customerCoding = customerCoding;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLabelTemplate() {
        return labelTemplate;
    }

    public void setLabelTemplate(String labelTemplate) {
        this.labelTemplate = labelTemplate;
    }

    public String getPerfectState() {
        return perfectState;
    }

    public void setPerfectState(String perfectState) {
        this.perfectState = perfectState;
    }

    public FileDescriptor getShippingReportPrintTemplate() {
        return shippingReportPrintTemplate;
    }

    public void setShippingReportPrintTemplate(FileDescriptor shippingReportPrintTemplate) {
        this.shippingReportPrintTemplate = shippingReportPrintTemplate;
    }

    public String getCustomerServiceTechnician() {
        return customerServiceTechnician;
    }

    public void setCustomerServiceTechnician(String customerServiceTechnician) {
        this.customerServiceTechnician = customerServiceTechnician;
    }

    public String getClientsCategories() {
        return clientsCategories;
    }

    public void setClientsCategories(String clientsCategories) {
        this.clientsCategories = clientsCategories;
    }

    public List<CustomerAndcustomerDescriptiveParameters> getCustomerAndcustomerDescriptiveParameters() {
        return customerAndcustomerDescriptiveParameters;
    }

    public void setCustomerAndcustomerDescriptiveParameters(List<CustomerAndcustomerDescriptiveParameters> customerAndcustomerDescriptiveParameters) {
        this.customerAndcustomerDescriptiveParameters = customerAndcustomerDescriptiveParameters;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
