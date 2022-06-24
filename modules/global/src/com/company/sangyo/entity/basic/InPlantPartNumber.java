package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SANGYO_IN_PLANT_PART_NUMBER")
@Entity(name = "sangyo_InPlantPartNumber")
public class InPlantPartNumber extends StandardEntity {
    private static final long serialVersionUID = -6833455090610873682L;

    @Column(name = "INVENTORY_CODING", nullable = false, unique = true)
    @NotNull
    private String inventoryCoding;

    @Column(name = "QUALITY_MAN_FLAG")
    private Boolean qualityManFlag = false;

    @Column(name = "PERFECT_STATE")
    private String perfectState;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PART_NUMBER_LABEL_TEMPLATE")
    private String partNumberLabelTemplate;

    @Column(name = "FILM_CODE", nullable = false)
    @NotNull
    private String inPlantPartNumberName;

    @Column(name = "INVENTORY_CLASSIFICATION")
    private String inventoryClassification;

    @Column(name = "LOT_NUMBER_MANAGEMENT")
    private Boolean lotNumberManagement;

    @Column(name = "QUALITY_PERIOD_UNIT")
    private String qualityPeriodUnit;

    @Column(name = "QUALITY_DAYNUM")
    private String qualityDaynum;

    @Column(name = "SCREEN_FRAME_SIZE_MODEL")
    private String screenFrameSizeModel;

    @Column(name = "NET_YARN_SPECIFICATION")
    private String netYarnSpecification;

    @Column(name = "COMMODITY_FORM_ID")
    private String commodityFormID;

    @Column(name = "WIRE_MESH_ANGLE")
    private String wireMeshAngle;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "TENSION")
    private String tension;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ITEM_NATURE")
    private String itemNature;

    @Column(name = "MESH_WIDTH")
    private String meshWidth;

    @Column(name = "COMMODITY_FORM")
    private String commodityForm;

    @OneToMany(mappedBy = "inPlantPartNumber")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<CustomerPartNumberAndinPlantPartNumber> customerPartNumber;

    @OneToMany(mappedBy = "inPlantPartNumber")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParameters;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "inPlantPartNumber")
    private List<StandingTimeCheck> standingTimeCheck;

    public String getInventoryCoding() {
        return inventoryCoding;
    }

    public void setInventoryCoding(String inventoryCoding) {
        this.inventoryCoding = inventoryCoding;
    }

    public Boolean getQualityManFlag() {
        return qualityManFlag;
    }

    public void setQualityManFlag(Boolean qualityManFlag) {
        this.qualityManFlag = qualityManFlag;
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

    public String getPartNumberLabelTemplate() {
        return partNumberLabelTemplate;
    }

    public void setPartNumberLabelTemplate(String partNumberLabelTemplate) {
        this.partNumberLabelTemplate = partNumberLabelTemplate;
    }

    public String getInPlantPartNumberName() {
        return inPlantPartNumberName;
    }

    public void setInPlantPartNumberName(String inPlantPartNumberName) {
        this.inPlantPartNumberName = inPlantPartNumberName;
    }

    public String getInventoryClassification() {
        return inventoryClassification;
    }

    public void setInventoryClassification(String inventoryClassification) {
        this.inventoryClassification = inventoryClassification;
    }

    public Boolean getLotNumberManagement() {
        return lotNumberManagement;
    }

    public void setLotNumberManagement(Boolean lotNumberManagement) {
        this.lotNumberManagement = lotNumberManagement;
    }

    public String getQualityPeriodUnit() {
        return qualityPeriodUnit;
    }

    public void setQualityPeriodUnit(String qualityPeriodUnit) {
        this.qualityPeriodUnit = qualityPeriodUnit;
    }

    public String getQualityDaynum() {
        return qualityDaynum;
    }

    public void setQualityDaynum(String qualityDaynum) {
        this.qualityDaynum = qualityDaynum;
    }

    public String getScreenFrameSizeModel() {
        return screenFrameSizeModel;
    }

    public void setScreenFrameSizeModel(String screenFrameSizeModel) {
        this.screenFrameSizeModel = screenFrameSizeModel;
    }

    public String getNetYarnSpecification() {
        return netYarnSpecification;
    }

    public void setNetYarnSpecification(String netYarnSpecification) {
        this.netYarnSpecification = netYarnSpecification;
    }

    public String getCommodityFormID() {
        return commodityFormID;
    }

    public void setCommodityFormID(String commodityFormID) {
        this.commodityFormID = commodityFormID;
    }

    public String getWireMeshAngle() {
        return wireMeshAngle;
    }

    public void setWireMeshAngle(String wireMeshAngle) {
        this.wireMeshAngle = wireMeshAngle;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemNature() {
        return itemNature;
    }

    public void setItemNature(String itemNature) {
        this.itemNature = itemNature;
    }

    public String getMeshWidth() {
        return meshWidth;
    }

    public void setMeshWidth(String meshWidth) {
        this.meshWidth = meshWidth;
    }

    public String getCommodityForm() {
        return commodityForm;
    }

    public void setCommodityForm(String commodityForm) {
        this.commodityForm = commodityForm;
    }

    public List<CustomerPartNumberAndinPlantPartNumber> getCustomerPartNumber() {
        return customerPartNumber;
    }

    public void setCustomerPartNumber(List<CustomerPartNumberAndinPlantPartNumber> customerPartNumber) {
        this.customerPartNumber = customerPartNumber;
    }

    public List<InPlantPartNumberAndPartNumberParameters> getInPlantPartNumberAndPartNumberParameters() {
        return inPlantPartNumberAndPartNumberParameters;
    }

    public void setInPlantPartNumberAndPartNumberParameters(List<InPlantPartNumberAndPartNumberParameters> inPlantPartNumberAndPartNumberParameters) {
        this.inPlantPartNumberAndPartNumberParameters = inPlantPartNumberAndPartNumberParameters;
    }

    public List<StandingTimeCheck> getStandingTimeCheck() {
        return standingTimeCheck;
    }

    public void setStandingTimeCheck(List<StandingTimeCheck> standingTimeCheck) {
        this.standingTimeCheck = standingTimeCheck;
    }
}
