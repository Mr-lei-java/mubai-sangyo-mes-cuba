package com.company.sangyo.entity.production;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "SANGYO_FACTORY_PRODUCTION_STANDARDS_ENTITY")
@Entity(name = "sangyo_FactoryProductionStandardsEntity")
public class FactoryProductionStandardsEntity extends StandardEntity {
    private static final long serialVersionUID = -9109226645067185206L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STANDARD_DEVIATION")
    private String standardDeviation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(String standardDeviation) {
        this.standardDeviation = standardDeviation;
    }
}
