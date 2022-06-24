package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_QT_EXPORT_RULE_ITEM_AND_PART_NUMBER_PARAMETERS")
@Entity(name = "sangyo_QtExportRuleItemAndPartNumberParameters")
public class QtExportRuleItemAndPartNumberParameters extends StandardEntity {
    private static final long serialVersionUID = 8738225529910399999L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QT_EXPORT_RULE_ITEM_ID")
    private QtExportRuleItem qtExportRuleItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PART_NUMBER_PARAMETERS_ID")
    private PartNumberParameters partNumberParameters;

    @Column(name = "SERIAL_NUMBER")
    private Integer serialNumber;

    public QtExportRuleItem getQtExportRuleItem() {
        return qtExportRuleItem;
    }

    public void setQtExportRuleItem(QtExportRuleItem qtExportRuleItem) {
        this.qtExportRuleItem = qtExportRuleItem;
    }

    public PartNumberParameters getPartNumberParameters() {
        return partNumberParameters;
    }

    public void setPartNumberParameters(PartNumberParameters partNumberParameters) {
        this.partNumberParameters = partNumberParameters;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }
}
