package com.company.sangyo.entity.storage;

import com.company.sangyo.entity.basic.Procedure;
import com.company.sangyo.entity.basic.ProcessFlow;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author leitengfei
 */
@Table(name = "SANGYO_WAREHOUSE")
@Entity(name = "sangyo_Warehouse")
public class Warehouse extends StandardEntity {
    private static final long serialVersionUID = -4985213675470759001L;

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "TYPE_")
    private String type;

    @Column(name = "QUANTITY_SCREEN_VERSION")
    private Integer quantityScreenVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCEDURE_ID")
    private Procedure procedure;

    @Column(name = "LOW_WATER_LEVEL")
    private Integer lowWaterLevel;

    @Column(name = "HIGH_WATER_LEVEL")
    private Integer highWaterLevel;

    @JoinTable(name = "SANGYO_PROCESS_FLOW_WAREHOUSE_LINK",
            joinColumns = @JoinColumn(name = "WAREHOUSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PROCESS_FLOW_ID"))
    @ManyToMany
    private List<ProcessFlow> processFlows;

    @Column(name = "IS_SYNCHRONIZE_ERP")
    private Boolean isSynchronizeERP = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantityScreenVersion() {
        return quantityScreenVersion;
    }

    public void setQuantityScreenVersion(Integer quantityScreenVersion) {
        this.quantityScreenVersion = quantityScreenVersion;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public Integer getLowWaterLevel() {
        return lowWaterLevel;
    }

    public void setLowWaterLevel(Integer lowWaterLevel) {
        this.lowWaterLevel = lowWaterLevel;
    }

    public Integer getHighWaterLevel() {
        return highWaterLevel;
    }

    public void setHighWaterLevel(Integer highWaterLevel) {
        this.highWaterLevel = highWaterLevel;
    }

    public List<ProcessFlow> getProcessFlows() {
        return processFlows;
    }

    public void setProcessFlows(List<ProcessFlow> processFlows) {
        this.processFlows = processFlows;
    }

    public Boolean getSynchronizeERP() {
        return isSynchronizeERP;
    }

    public void setSynchronizeERP(Boolean synchronizeERP) {
        isSynchronizeERP = synchronizeERP;
    }
}
