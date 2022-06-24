package com.company.sangyo.entity.storage;

import com.company.sangyo.entity.basic.Procedure;
import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.production.WorkOrder;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "SANGYO_UNQUALIFIED")
@Entity(name = "sangyo_Unqualified")
public class Unqualified extends StandardEntity {
    private static final long serialVersionUID = 1068577040445177824L;

    @JoinColumn(name = "SCREEN_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private Screen screenId;

    @JoinColumn(name = "WAREHOUSE_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPERATING_PERSONNEL_ID")
    private User operatingPersonnel;

    @Column(name = "OPERATE_TIME")
    private LocalDateTime operateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_ORDER_ID")
    private WorkOrder workOrder;

    @Column(name = "REMARK2")
    private String remark2;

    @Column(name = "REMARK3")
    private String remark3;

    @Column(name = "INCOMING_CLASSIFICATION")
    private String incomingClassification;

    @Column(name = "ABNORMAL_PARAMETER")
    @Lob
    private String abnormalParameter;

    @Column(name = "IS_PROCESSED")
    private Boolean isProcessed = false;

    @Column(name = "REMARK")
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCESSOR_ID")
    private User processor;

    @Column(name = "PROCESSING_MODE")
    private String processingMode;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "SALE_ORDER")
    private String saleOrder;

    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @Column(name = "PROCESSING_TIME")
    private LocalDateTime processingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_ID")
    private Procedure procedure;

    @Column(name = "REWORK_TYPE")
    private String reworkType;

    @Column(name = "IS_REPEATED")
    private Boolean isRepeated;

    public IncomingClassificationEnum getIncomingClassification() {
        return incomingClassification == null ? null : IncomingClassificationEnum.fromId(incomingClassification);
    }

    public void setIncomingClassification(IncomingClassificationEnum incomingClassification) {
        this.incomingClassification = incomingClassification == null ? null : incomingClassification.getId();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(String saleOrder) {
        this.saleOrder = saleOrder;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getIsRepeated() {
        return isRepeated;
    }

    public void setIsRepeated(Boolean isRepeated) {
        this.isRepeated = isRepeated;
    }

    public String getAbnormalParameter() {
        return abnormalParameter;
    }

    public void setAbnormalParameter(String abnormalParameter) {
        this.abnormalParameter = abnormalParameter;
    }

    public ReworkType getReworkType() {
        return reworkType == null ? null : ReworkType.fromId(reworkType);
    }

    public void setReworkType(ReworkType reworkType) {
        this.reworkType = reworkType == null ? null : reworkType.getId();
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public Boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public LocalDateTime getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(LocalDateTime processingTime) {
        this.processingTime = processingTime;
    }

    public ProcessingMode getProcessingMode() {
        return processingMode == null ? null : ProcessingMode.fromId(processingMode);
    }

    public void setProcessingMode(ProcessingMode processingMode) {
        this.processingMode = processingMode == null ? null : processingMode.getId();
    }

    public User getProcessor() {
        return processor;
    }

    public void setProcessor(User processor) {
        this.processor = processor;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }

    public User getOperatingPersonnel() {
        return operatingPersonnel;
    }

    public void setOperatingPersonnel(User operatingPersonnel) {
        this.operatingPersonnel = operatingPersonnel;
    }

    public void setScreenId(Screen screenId) {
        this.screenId = screenId;
    }

    public Screen getScreenId() {
        return screenId;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Warehouse getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Warehouse warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public String toString() {
        return "Unqualified{" +
                "screenId=" + screenId +
                ", warehouseId=" + warehouseId +
                ", operatingPersonnel=" + operatingPersonnel +
                ", operateTime=" + operateTime +
                ", workOrder=" + workOrder +
                ", remark2='" + remark2 + '\'' +
                ", remark3='" + remark3 + '\'' +
                ", abnormalParameter='" + abnormalParameter + '\'' +
                ", isProcessed=" + isProcessed +
                ", remark='" + remark + '\'' +
                ", processor=" + processor +
                ", processingMode='" + processingMode + '\'' +
                ", source='" + source + '\'' +
                ", saleOrder='" + saleOrder + '\'' +
                ", processingTime=" + processingTime +
                ", procedure=" + procedure +
                ", reworkType='" + reworkType + '\'' +
                ", isRepeated=" + isRepeated +
                '}';
    }
}
