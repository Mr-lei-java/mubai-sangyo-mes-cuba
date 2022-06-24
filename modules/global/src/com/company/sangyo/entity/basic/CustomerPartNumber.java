package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SANGYO_CUSTOMER_PART_NUMBER")
@Entity(name = "sangyo_CustomerPartNumber")
public class CustomerPartNumber extends StandardEntity {
    private static final long serialVersionUID = 2007781727007384899L;

    @Column(name = "INVENTORY_CODING", nullable = false, unique = true)
    @NotNull
    private String inventoryCoding;

    @Column(name = "PERFECT_STATE")
    private String perfectState;

    @Column(name = "NAME")
    private String name;

    @JoinColumn(name = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(name = "SCREEN_FRAME_SIZE_MODEL")
    private String screenFrameSizeModel;

    @Column(name = "CUSTOMER_PART_NUMBER_STATUS")
    private String customerPartNumberStatus;

    @Column(name = "ANGLE")
    private String angle;

    @Column(name = "LOT_NUMBER_MANAGEMENT")
    private Boolean isNumberManagement;

    @Column(name = "QUALITY_PERIOD_UNIT")
    private String qualityPeriodUnit;

    @Column(name = "QUALITY_DAYNUM")
    private String qualityDayNum;

    @Column(name = "INVENTORY_CLASSIFICATION")
    private String inventoryClassification;

    @Column(name = "P_T_VALUE_LINE_NUMBER")
    private String pTValueLineNumber;

    @Column(name = "TEN_ERR")
    private Double tenErr;

    @Column(name = "MEMB_TH_SPC")
    private Double membThSpc;

    @Column(name = "MEMB_TH_ERR")
    private Double membThErr;

    @Column(name = "TTL_TH_SPC")
    private Double ttlThSpc;

    @Column(name = "QUALITY_MAN_FLAG")
    private Boolean qualityManFlag = false;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "TTL_TH_ERR")
    private Double ttlThErr;

    @Column(name = "LINE_WID_SPC")
    private Double lineWidSpc;

    @Column(name = "LINE_WIT_ERR")
    private Double lineWitErr;

    @Column(name = "PT_VAL")
    private Double ptVal;

    @Column(name = "PT_ERR")
    private Double ptErr;

    @Column(name = "X_DIS_SPC")
    private Double screenXDisSpc;

    @Column(name = "X_DIS_ERR")
    private Double screenXDisErr;

    @Column(name = "Y_DIS_SPC")
    private Double screenYDisSpc;

    @Column(name = "Y_DIS_ERR")
    private Double screenYDisErr;

    @Column(name = "NOTE")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DW_ATTCH1_ID")
    private FileDescriptor dwAttch1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DW_ATTCH2_ID")
    private FileDescriptor dwAttch2;

    @Column(name = "NET_YARN_SPECIFICATION")
    private String netYarnSpecification;

    @Column(name = "TENSION")
    private String tension;

    @Column(name = "STEEL_MESH_FORMAT_SIZE")
    private String steelMeshFormatSize;

    @NotNull
    @Column(name = "ITEM_DESCRIPTION")
    private String itemDescription;

    @NotNull
    @Column(name = "MESH_SPECIFICATION")
    private String meshSpecification;

    @NotNull
    @Column(name = "CUSTOMER_PART_NUMBER_NATURE")
    private String customerPartNumberNature;

    @Column(name = "COMMODITY_FORM")
    private String commodityForm;

    @Column(name = "COMMODITY_FORM_ID")
    private String commodityFormId;

    @Column(name = "MESH_WIDTH")
    private String meshWidth;

    @Column(name = "COPYRIGHT_VERSION_NUMBER")
    private String copyrightVersionNumber;

    @Column(name = "REMARK")
    private String remark;

    @OneToMany(mappedBy = "customerPartNumber")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<CustomerPartNumberAndinPlantPartNumber> customerPartNumberAndinPlantPartNumber;

    @OneToMany(mappedBy = "customerPartNumber")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<CustomerPartNumberAndPartNumberParameters> customerPartNumberAndPartNumberParameters;

    public String getInventoryCoding() {
        return inventoryCoding;
    }

    public void setInventoryCoding(String inventoryCoding) {
        this.inventoryCoding = inventoryCoding;
    }

    public String getPerfectState() {
        return perfectState;
    }

    public void setPerfectState(String perfectState) {
        this.perfectState = perfectState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getScreenFrameSizeModel() {
        return screenFrameSizeModel;
    }

    public void setScreenFrameSizeModel(String screenFrameSizeModel) {
        this.screenFrameSizeModel = screenFrameSizeModel;
    }

    public String getCustomerPartNumberStatus() {
        return customerPartNumberStatus;
    }

    public void setCustomerPartNumberStatus(String customerPartNumberStatus) {
        this.customerPartNumberStatus = customerPartNumberStatus;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public Boolean getNumberManagement() {
        return isNumberManagement;
    }

    public void setNumberManagement(Boolean numberManagement) {
        isNumberManagement = numberManagement;
    }

    public String getQualityPeriodUnit() {
        return qualityPeriodUnit;
    }

    public void setQualityPeriodUnit(String qualityPeriodUnit) {
        this.qualityPeriodUnit = qualityPeriodUnit;
    }

    public String getQualityDayNum() {
        return qualityDayNum;
    }

    public void setQualityDayNum(String qualityDayNum) {
        this.qualityDayNum = qualityDayNum;
    }

    public String getInventoryClassification() {
        return inventoryClassification;
    }

    public void setInventoryClassification(String inventoryClassification) {
        this.inventoryClassification = inventoryClassification;
    }

    public String getpTValueLineNumber() {
        return pTValueLineNumber;
    }

    public void setpTValueLineNumber(String pTValueLineNumber) {
        this.pTValueLineNumber = pTValueLineNumber;
    }

    public Double getTenErr() {
        return tenErr;
    }

    public void setTenErr(Double tenErr) {
        this.tenErr = tenErr;
    }

    public Double getMembThSpc() {
        return membThSpc;
    }

    public void setMembThSpc(Double membThSpc) {
        this.membThSpc = membThSpc;
    }

    public Double getMembThErr() {
        return membThErr;
    }

    public void setMembThErr(Double membThErr) {
        this.membThErr = membThErr;
    }

    public Double getTtlThSpc() {
        return ttlThSpc;
    }

    public void setTtlThSpc(Double ttlThSpc) {
        this.ttlThSpc = ttlThSpc;
    }

    public Boolean getQualityManFlag() {
        return qualityManFlag;
    }

    public void setQualityManFlag(Boolean qualityManFlag) {
        this.qualityManFlag = qualityManFlag;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getTtlThErr() {
        return ttlThErr;
    }

    public void setTtlThErr(Double ttlThErr) {
        this.ttlThErr = ttlThErr;
    }

    public Double getLineWidSpc() {
        return lineWidSpc;
    }

    public void setLineWidSpc(Double lineWidSpc) {
        this.lineWidSpc = lineWidSpc;
    }

    public Double getLineWitErr() {
        return lineWitErr;
    }

    public void setLineWitErr(Double lineWitErr) {
        this.lineWitErr = lineWitErr;
    }

    public Double getPtVal() {
        return ptVal;
    }

    public void setPtVal(Double ptVal) {
        this.ptVal = ptVal;
    }

    public Double getPtErr() {
        return ptErr;
    }

    public void setPtErr(Double ptErr) {
        this.ptErr = ptErr;
    }

    public Double getScreenXDisSpc() {
        return screenXDisSpc;
    }

    public void setScreenXDisSpc(Double screenXDisSpc) {
        this.screenXDisSpc = screenXDisSpc;
    }

    public Double getScreenXDisErr() {
        return screenXDisErr;
    }

    public void setScreenXDisErr(Double screenXDisErr) {
        this.screenXDisErr = screenXDisErr;
    }

    public Double getScreenYDisSpc() {
        return screenYDisSpc;
    }

    public void setScreenYDisSpc(Double screenYDisSpc) {
        this.screenYDisSpc = screenYDisSpc;
    }

    public Double getScreenYDisErr() {
        return screenYDisErr;
    }

    public void setScreenYDisErr(Double screenYDisErr) {
        this.screenYDisErr = screenYDisErr;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public FileDescriptor getDwAttch1() {
        return dwAttch1;
    }

    public void setDwAttch1(FileDescriptor dwAttch1) {
        this.dwAttch1 = dwAttch1;
    }

    public FileDescriptor getDwAttch2() {
        return dwAttch2;
    }

    public void setDwAttch2(FileDescriptor dwAttch2) {
        this.dwAttch2 = dwAttch2;
    }

    public String getNetYarnSpecification() {
        return netYarnSpecification;
    }

    public void setNetYarnSpecification(String netYarnSpecification) {
        this.netYarnSpecification = netYarnSpecification;
    }

    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public String getSteelMeshFormatSize() {
        return steelMeshFormatSize;
    }

    public void setSteelMeshFormatSize(String steelMeshFormatSize) {
        this.steelMeshFormatSize = steelMeshFormatSize;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getMeshSpecification() {
        return meshSpecification;
    }

    public void setMeshSpecification(String meshSpecification) {
        this.meshSpecification = meshSpecification;
    }

    public String getCustomerPartNumberNature() {
        return customerPartNumberNature;
    }

    public void setCustomerPartNumberNature(String customerPartNumberNature) {
        this.customerPartNumberNature = customerPartNumberNature;
    }

    public String getCommodityForm() {
        return commodityForm;
    }

    public void setCommodityForm(String commodityForm) {
        this.commodityForm = commodityForm;
    }

    public String getCommodityFormId() {
        return commodityFormId;
    }

    public void setCommodityFormId(String commodityFormId) {
        this.commodityFormId = commodityFormId;
    }

    public String getMeshWidth() {
        return meshWidth;
    }

    public void setMeshWidth(String meshWidth) {
        this.meshWidth = meshWidth;
    }

    public String getCopyrightVersionNumber() {
        return copyrightVersionNumber;
    }

    public void setCopyrightVersionNumber(String copyrightVersionNumber) {
        this.copyrightVersionNumber = copyrightVersionNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<CustomerPartNumberAndinPlantPartNumber> getCustomerPartNumberAndinPlantPartNumber() {
        return customerPartNumberAndinPlantPartNumber;
    }

    public void setCustomerPartNumberAndinPlantPartNumber(List<CustomerPartNumberAndinPlantPartNumber> customerPartNumberAndinPlantPartNumber) {
        this.customerPartNumberAndinPlantPartNumber = customerPartNumberAndinPlantPartNumber;
    }

    public List<CustomerPartNumberAndPartNumberParameters> getCustomerPartNumberAndPartNumberParameters() {
        return customerPartNumberAndPartNumberParameters;
    }

    public void setCustomerPartNumberAndPartNumberParameters(List<CustomerPartNumberAndPartNumberParameters> customerPartNumberAndPartNumberParameters) {
        this.customerPartNumberAndPartNumberParameters = customerPartNumberAndPartNumberParameters;
    }
}
