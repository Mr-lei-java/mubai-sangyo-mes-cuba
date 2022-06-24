package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.sql.Time;

@Table(name = "SANGYO_STANDING_TIME_CHECK")
@Entity(name = "sangyo_StandingTimeCheck")
public class StandingTimeCheck extends StandardEntity {
    private static final long serialVersionUID = -9053461162029910184L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_PLANT_PART_NUMBER_ID")
    private InPlantPartNumber inPlantPartNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_ID")
    private Procedure procedure;

    @Column(name = "STANDING_TIME")
    private Time standingTime;

    public InPlantPartNumber getInPlantPartNumber() {
        return inPlantPartNumber;
    }

    public void setInPlantPartNumber(InPlantPartNumber inPlantPartNumber) {
        this.inPlantPartNumber = inPlantPartNumber;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public Time getStandingTime() {
        return standingTime;
    }

    public void setStandingTime(Time standingTime) {
        this.standingTime = standingTime;
    }
}
