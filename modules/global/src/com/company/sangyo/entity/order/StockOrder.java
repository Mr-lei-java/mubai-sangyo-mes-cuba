package com.company.sangyo.entity.order;

import com.company.sangyo.entity.basic.InPlantPartNumber;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_STOCK_ORDER")
@Entity(name = "sangyo_StockOrder")
public class StockOrder extends StandardEntity {
    private static final long serialVersionUID = -5547891304769171150L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_PLANT_PART_NUMBER_ID")
    private InPlantPartNumber inPlantPartNumber;

    @Column(name = "QUANTITY_STOCK")
    private Integer quantityStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    public InPlantPartNumber getInPlantPartNumber() {
        return inPlantPartNumber;
    }

    public void setInPlantPartNumber(InPlantPartNumber inPlantPartNumber) {
        this.inPlantPartNumber = inPlantPartNumber;
    }

    public Integer getQuantityStock() {
        return quantityStock;
    }

    public void setQuantityStock(Integer quantityStock) {
        this.quantityStock = quantityStock;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
