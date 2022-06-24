package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_ITEM_NUMBER_TEMPLATE")
@Entity(name = "sangyo_ItemNumberTemplate")
public class ItemNumberTemplate extends StandardEntity {
    private static final long serialVersionUID = -4079268539083787869L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_NUMBER_PARAMETER_TEMPLATE_ID")
    private ItemNumberParameterTemplate itemNumberParameterTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PART_NUMBER_PARAMETERS_ID")
    private PartNumberParameters partNumberParameters;

    public ItemNumberParameterTemplate getItemNumberParameterTemplate() {
        return itemNumberParameterTemplate;
    }

    public void setItemNumberParameterTemplate(ItemNumberParameterTemplate itemNumberParameterTemplate) {
        this.itemNumberParameterTemplate = itemNumberParameterTemplate;
    }

    public PartNumberParameters getPartNumberParameters() {
        return partNumberParameters;
    }

    public void setPartNumberParameters(PartNumberParameters partNumberParameters) {
        this.partNumberParameters = partNumberParameters;
    }
}
