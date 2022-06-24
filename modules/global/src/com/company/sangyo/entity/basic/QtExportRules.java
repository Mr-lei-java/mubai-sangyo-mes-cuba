package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Table(name = "SANGYO_QT_EXPORT_RULES")
@Entity(name = "sangyo_QtExportRules")
public class QtExportRules extends StandardEntity {
    private static final long serialVersionUID = 6773987462122588300L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "IS_EXPORT_TURNOVER_TIME")
    private Boolean isExportTurnoverTime = false;

    @Column(name = "IS_EXPORT_OPERATING_PERSONNEL")
    private Boolean isExportOperatingPersonnel = false;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "qtExportRules")
    private List<QtExportRuleItem> qtExportRuleItems;

    public Boolean getIsExportOperatingPersonnel() {
        return isExportOperatingPersonnel;
    }

    public void setIsExportOperatingPersonnel(Boolean isExportOperatingPersonnel) {
        this.isExportOperatingPersonnel = isExportOperatingPersonnel;
    }

    public Boolean getIsExportTurnoverTime() {
        return isExportTurnoverTime;
    }

    public void setIsExportTurnoverTime(Boolean isExportTurnoverTime) {
        this.isExportTurnoverTime = isExportTurnoverTime;
    }

    public List<QtExportRuleItem> getQtExportRuleItems() {
        return qtExportRuleItems;
    }

    public void setQtExportRuleItems(List<QtExportRuleItem> qtExportRuleItems) {
        this.qtExportRuleItems = qtExportRuleItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
