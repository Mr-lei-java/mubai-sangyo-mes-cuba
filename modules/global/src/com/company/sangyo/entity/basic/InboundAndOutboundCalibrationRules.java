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

@Table(name = "SANGYO_INBOUND_AND_OUTBOUND_CALIBRATION_RULES")
@Entity(name = "sangyo_InboundAndOutboundCalibrationRules")
public class InboundAndOutboundCalibrationRules extends StandardEntity {
    private static final long serialVersionUID = -5911203926144398658L;

    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Column(name = "REMARK")
    private String remark;

    @OneToMany(mappedBy = "inboundAndOutboundCalibrationRules")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<EntryAndExitVerificationRuleItem> entryAndExitVerificationRuleItem;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<EntryAndExitVerificationRuleItem> getEntryAndExitVerificationRuleItem() {
        return entryAndExitVerificationRuleItem;
    }

    public void setEntryAndExitVerificationRuleItem(List<EntryAndExitVerificationRuleItem> entryAndExitVerificationRuleItem) {
        this.entryAndExitVerificationRuleItem = entryAndExitVerificationRuleItem;
    }
}
