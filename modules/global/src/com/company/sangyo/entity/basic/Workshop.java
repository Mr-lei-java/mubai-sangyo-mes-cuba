package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SANGYO_WORKSHOP")
@Entity(name = "sangyo_Workshop")
public class Workshop extends StandardEntity {
    private static final long serialVersionUID = -8045535922618393673L;

    @Column(name = "NAME", nullable = false, unique = true)
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
