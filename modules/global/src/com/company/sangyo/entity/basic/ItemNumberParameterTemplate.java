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

@Table(name = "SANGYO_ITEM_NUMBER_PARAMETER_TEMPLATE")
@Entity(name = "sangyo_ItemNumberParameterTemplate")
public class ItemNumberParameterTemplate extends StandardEntity {
    private static final long serialVersionUID = 1136337063522933968L;

    @Column(name = "NAME")
    private String name;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "itemNumberParameterTemplate")
    private List<ItemNumberTemplate> templateName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemNumberTemplate> getTemplateName() {
        return templateName;
    }

    public void setTemplateName(List<ItemNumberTemplate> templateName) {
        this.templateName = templateName;
    }
}
