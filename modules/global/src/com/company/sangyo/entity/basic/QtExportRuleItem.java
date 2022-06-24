package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.List;

@Table(name = "SANGYO_QT_EXPORT_RULE_ITEM")
@Entity(name = "sangyo_QtExportRuleItem")
public class QtExportRuleItem extends StandardEntity {
    private static final long serialVersionUID = 3882418367812170876L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QT_EXPORT_RULES_ID")
    private QtExportRules qtExportRules;

    @OneToMany(mappedBy = "qtExportRuleItem")
    private List<QtExportRuleItemAndPartNumberParameters> qtExportRuleItemAndPartNumberParameters;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_ID")
    private Procedure procedure;

    @ManyToMany
    @JoinTable(name = "SANGYO_QT_EXPORT_RULE_ITEM_PART_NUMBER_PARAMETERS_LINK",
            joinColumns = @JoinColumn(name = "QT_EXPORT_RULE_ITEM_ID"),
            inverseJoinColumns = @JoinColumn(name = "PART_NUMBER_PARAMETERS_ID"))
    private List<PartNumberParameters> parameter;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    public QtExportRules getQtExportRules() {
        return qtExportRules;
    }

    public void setQtExportRules(QtExportRules qtExportRules) {
        this.qtExportRules = qtExportRules;
    }

    public List<QtExportRuleItemAndPartNumberParameters> getQtExportRuleItemAndPartNumberParameters() {
        return qtExportRuleItemAndPartNumberParameters;
    }

    public void setQtExportRuleItemAndPartNumberParameters(List<QtExportRuleItemAndPartNumberParameters> qtExportRuleItemAndPartNumberParameters) {
        this.qtExportRuleItemAndPartNumberParameters = qtExportRuleItemAndPartNumberParameters;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public List<PartNumberParameters> getParameter() {
        return parameter;
    }

    public void setParameter(List<PartNumberParameters> parameter) {
        this.parameter = parameter;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
