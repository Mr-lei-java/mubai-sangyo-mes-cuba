package com.company.sangyo.entity.storage;

import com.company.sangyo.entity.basic.Procedure;
import com.company.sangyo.entity.basic.Screen;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "SANGYO_REWORK_RECORD")
@Entity(name = "sangyo_ReworkRecord")
public class ReworkRecord extends StandardEntity {
    private static final long serialVersionUID = 4982688528695881972L;

    @JoinColumn(name = "REWORK_PROCEDURE_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Procedure reworkProcedureId;

    @JoinColumn(name = "OPERATING_PERSONNEL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User operatingPersonnel;

    @Column(name = "OPERATE_TIME")
    private LocalDateTime operateTime;

    @Column(name = "REMARK")
    private String remark;

    @JoinColumn(name = "SOURCE_WAREHOUSE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse sourceWarehouse;

    @Column(name = "REWORK_TYPE")
    private String reworkType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_ID")
    private Screen screen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOURCE_PROCEDURE_ID")
    private Procedure sourceProcedure;

    @Column(name = "IS_REPEATED")
    private Boolean isRepeated;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsRepeated() {
        return isRepeated;
    }

    public void setIsRepeated(Boolean isRepeated) {
        this.isRepeated = isRepeated;
    }

    public Procedure getSourceProcedure() {
        return sourceProcedure;
    }

    public void setSourceProcedure(Procedure sourceProcedure) {
        this.sourceProcedure = sourceProcedure;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public ReworkType getReworkType() {
        return reworkType == null ? null : ReworkType.fromId(reworkType);
    }

    public void setReworkType(ReworkType reworkType) {
        this.reworkType = reworkType == null ? null : reworkType.getId();
    }

    public void setOperatingPersonnel(User operatingPersonnel) {
        this.operatingPersonnel = operatingPersonnel;
    }

    public User getOperatingPersonnel() {
        return operatingPersonnel;
    }

    public Warehouse getSourceWarehouse() {
        return sourceWarehouse;
    }

    public void setSourceWarehouse(Warehouse sourceWarehouse) {
        this.sourceWarehouse = sourceWarehouse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }

    public Procedure getReworkProcedureId() {
        return reworkProcedureId;
    }

    public void setReworkProcedureId(Procedure reworkProcedureId) {
        this.reworkProcedureId = reworkProcedureId;
    }
}
