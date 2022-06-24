package com.company.sangyo.entity.basic;

import com.company.sangyo.entity.production.WorkOrder;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "SANGYO_SCREEN")
@Entity(name = "sangyo_Screen")
public class Screen extends StandardEntity {
    private static final long serialVersionUID = 9081855195733189430L;

    @Column(name = "SCREEN_CODE", nullable = false, unique = true)
    @NotNull
    private String screenCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_ORDER_ID")
    private WorkOrder workOrder;

    @Column(name = "SCREEN_STATUS")
    private String screenStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENT_PROCEDURE_ID")
    private Procedure currentProcedure;

    @Column(name = "SCREEN_CURRENT_POSITION")
    private String screenCurrentPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PART_NUMBER_ID")
    private InPlantPartNumber partNumber;

    @Column(name = "OUT_PW_TIME")
    private LocalDateTime outPwTime;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "FRAME")
    private String frame;

    public void setScreenStatus(ScreenStatusEnum screenStatus) {
        this.screenStatus = screenStatus == null ? null : screenStatus.getId();
    }

    public ScreenStatusEnum getScreenStatus() {
        return screenStatus == null ? null : ScreenStatusEnum.fromId(screenStatus);
    }

    public String getScreenCode() {
        return screenCode;
    }

    public void setScreenCode(String screenCode) {
        this.screenCode = screenCode;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Procedure getCurrentProcedure() {
        return currentProcedure;
    }

    public void setCurrentProcedure(Procedure currentProcedure) {
        this.currentProcedure = currentProcedure;
    }

    public String getScreenCurrentPosition() {
        return screenCurrentPosition;
    }

    public void setScreenCurrentPosition(String screenCurrentPosition) {
        this.screenCurrentPosition = screenCurrentPosition;
    }

    public InPlantPartNumber getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(InPlantPartNumber partNumber) {
        this.partNumber = partNumber;
    }

    public LocalDateTime getOutPwTime() {
        return outPwTime;
    }

    public void setOutPwTime(LocalDateTime outPwTime) {
        this.outPwTime = outPwTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }
}
