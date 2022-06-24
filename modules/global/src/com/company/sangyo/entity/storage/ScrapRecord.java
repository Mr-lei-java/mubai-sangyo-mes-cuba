package com.company.sangyo.entity.storage;

import com.company.sangyo.entity.basic.Screen;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "SANGYO_SCRAP_RECORD")
@Entity(name = "sangyo_ScrapRecord")
public class ScrapRecord extends StandardEntity {
    private static final long serialVersionUID = 5680555270133652420L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SCREEN_ID")
    @NotNull
    private Screen screen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPERATING_PERSONNEL_ID")
    private User operatingPersonnel;

    @Column(name = "WAREHOUSING_TIME")
    private LocalDateTime warehousingTime;

    @Column(name = "SOURCE_SCRAPPED")
    private String sourceScrapped;

    @Column(name = "REMARK")
    private String remark;

    public String getSourceScrapped() {
        return sourceScrapped;
    }

    public void setSourceScrapped(String sourceScrapped) {
        this.sourceScrapped = sourceScrapped;
    }

    public User getOperatingPersonnel() {
        return operatingPersonnel;
    }

    public void setOperatingPersonnel(User operatingPersonnel) {
        this.operatingPersonnel = operatingPersonnel;
    }

    public LocalDateTime getWarehousingTime() {
        return warehousingTime;
    }

    public void setWarehousingTime(LocalDateTime warehousingTime) {
        this.warehousingTime = warehousingTime;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
