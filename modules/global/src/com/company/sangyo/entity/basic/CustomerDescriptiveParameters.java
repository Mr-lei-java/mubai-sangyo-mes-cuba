package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SANGYO_CUSTOMER_DESCRIPTIVE_PARAMETERS")
@Entity(name = "sangyo_CustomerDescriptiveParameters")
public class CustomerDescriptiveParameters extends StandardEntity {
    private static final long serialVersionUID = 417900183874192500L;

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "PARAMETERSTYPE")
    private String parameterstype;

    @Column(name = "IS_RSD")
    private Boolean isRsd = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameterstype() {
        return parameterstype;
    }

    public void setParameterstype(String parameterstype) {
        this.parameterstype = parameterstype;
    }

    public Boolean getRsd() {
        return isRsd;
    }

    public void setRsd(Boolean rsd) {
        isRsd = rsd;
    }
}
