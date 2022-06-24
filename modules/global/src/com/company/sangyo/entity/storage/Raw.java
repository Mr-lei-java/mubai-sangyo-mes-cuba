package com.company.sangyo.entity.storage;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_RAW")
@Entity(name = "sangyo_Raw")
public class Raw extends StandardEntity {
    private static final long serialVersionUID = -815680274074084529L;

    @JoinColumn(name = "WAREHOUSE_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouseId;

    @Column(name = "MATERIAL")
    private String material;

    @Column(name = "NUMBER_")
    private String number;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "REMARK2")
    private String remark2;

    @Column(name = "REMARK3")
    private String remark3;

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Warehouse getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Warehouse warehouseId) {
        this.warehouseId = warehouseId;
    }
}
