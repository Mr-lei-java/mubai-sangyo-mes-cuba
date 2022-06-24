package com.company.sangyo.entity.production;

import com.company.sangyo.entity.basic.CalibrationMethodEnum;
import com.company.sangyo.entity.basic.CustomerPartNumber;
import com.company.sangyo.entity.basic.InPlantPartNumber;
import com.company.sangyo.entity.basic.ProcessFlow;
import com.company.sangyo.entity.order.Order;
import com.company.sangyo.entity.order.OrderDataUpdateEnum;
import com.company.sangyo.entity.storage.Warehouse;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SANGYO_WORK_ORDER")
@Entity(name = "sangyo_WorkOrder")
public class WorkOrder extends StandardEntity {
    private static final long serialVersionUID = -7094808514079955172L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID_ID")
    private Order orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_PART_NUMBER_ID")
    private CustomerPartNumber customerPartNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_PLANT_PART_NUMBER_ID_ID")
    private InPlantPartNumber inPlantPartNumberId;

    @JoinColumn(name = "OUTPUT_WAREHOUSE_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private Warehouse outputWarehouse;

    @JoinColumn(name = "PROCESS_FLOW_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private ProcessFlow processFlowId;

    @Column(name = "ORDER_PRIORITY")
    private String orderPriority;

    @Column(name = "IN_PLANT_PART_CONSIGN_DATE")
    private String inPlantPartConsignDate;

    @Column(name = "ORDER_PRODUCTION_QUANTITY", nullable = false)
    @NotNull
    private Integer orderProductionQuantity = 0;

    @Column(name = "DEFAULT_ORDER_PRODUCTION_QUANTITY")
    private Integer defaultOrderProductionQuantity;

    @Column(name = "PLAN_PRODUCTION_QUANTITY", nullable = false)
    @NotNull
    private Integer planProductionQuantity;

    @Column(name = "COMPLETED_QUANTITY")
    private Integer completedQuantity = 0;

    @Column(name = "SUPPLEMENTARY_QUANTITY")
    private Integer supplementaryQuantity = 0;

    @Column(name = "QUANTITY_ISSUED")
    private Integer quantityIssued = 0;

    @Column(name = "SHIP_QUANTITY")
    private Integer shipQuantity = 0;

    @Column(name = "CHANGED_QUANTITY")
    private Integer changedQuantity = 0;

    @Column(name = "SCRAP_RECORD")
    private Integer scrapRecord;

    @Column(name = "ELIGIBILITY")
    private Integer eligibility;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORDER_DATA_UPDATE")
    private String orderDataUpdate;

    @Column(name = "DISTRIBUTE")
    private LocalDateTime distribute;

    @Column(name = "DELIVERY_TIME")
    private LocalDateTime deliveryTime;

    @Column(name = "IS_COPY")
    private Boolean isCopy = false;

    @Column(name = "HAS_CHANGED")
    private Boolean hasChanged = false;

    @Column(name = "IS_SUPPLEMENT")
    private Boolean isSupplement = false;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "SHIP_STATUS")
    private String shipStatus;

    @Column(name = "DISTRIBUTED_STATUS")
    private String distributedStatus;

    @OneToMany(mappedBy = "workOrder")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<WorkOrderAndScreen> workOrderAndScreen;

    public String getInPlantPartConsignDate() {
        return inPlantPartConsignDate;
    }

    public void setInPlantPartConsignDate(String inPlantPartConsignDate) {
        this.inPlantPartConsignDate = inPlantPartConsignDate;
    }

    public String getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(String orderPriority) {
        this.orderPriority = orderPriority;
    }

    public Integer getDefaultOrderProductionQuantity() {
        return defaultOrderProductionQuantity;
    }

    public void setDefaultOrderProductionQuantity(Integer defaultOrderProductionQuantity) {
        this.defaultOrderProductionQuantity = defaultOrderProductionQuantity;
    }

    public OrderDataUpdateEnum getOrderDataUpdate() {
        return orderDataUpdate == null ? null : OrderDataUpdateEnum.fromId(orderDataUpdate);
    }

    public void setOrderDataUpdate(OrderDataUpdateEnum orderDataUpdate) {
        this.orderDataUpdate = orderDataUpdate == null ? null : orderDataUpdate.getId();
    }

    public Integer getEligibility() {
        return eligibility;
    }

    public void setEligibility(Integer eligibility) {
        this.eligibility = eligibility;
    }

    public Boolean getIsCopy() {
        return isCopy;
    }

    public void setIsCopy(Boolean isCopy) {
        this.isCopy = isCopy;
    }

    public Boolean getIsSupplement() {
        return isSupplement;
    }

    public void setIsSupplement(Boolean isSupplement) {
        this.isSupplement = isSupplement;
    }

    public Integer getScrapRecord() {
        return scrapRecord;
    }

    public void setScrapRecord(Integer scrapRecord) {
        this.scrapRecord = scrapRecord;
    }

    public DistributedStatus getDistributedStatus() {
        return distributedStatus == null ? null : DistributedStatus.fromId(distributedStatus);
    }

    public void setDistributedStatus(DistributedStatus distributedStatus) {
        this.distributedStatus = distributedStatus == null ? null : distributedStatus.getId();
    }

    public void setShipQuantity(Integer shipQuantity) {
        this.shipQuantity = shipQuantity;
    }

    public Integer getShipQuantity() {
        return shipQuantity;
    }

    public ShipStatusEnum getShipStatus() {
        return shipStatus == null ? null : ShipStatusEnum.fromId(shipStatus);
    }

    public void setShipStatus(ShipStatusEnum shipStatus) {
        this.shipStatus = shipStatus == null ? null : shipStatus.getId();
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public CustomerPartNumber getCustomerPartNumber() {
        return customerPartNumber;
    }

    public void setCustomerPartNumber(CustomerPartNumber customerPartNumber) {
        this.customerPartNumber = customerPartNumber;
    }

    public Integer getChangedQuantity() {
        return changedQuantity;
    }

    public void setChangedQuantity(Integer changedQuantity) {
        this.changedQuantity = changedQuantity;
    }

    public Boolean getHasChanged() {
        return hasChanged;
    }

    public void setHasChanged(Boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDistribute() {
        return distribute;
    }

    public void setDistribute(LocalDateTime distribute) {
        this.distribute = distribute;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public WorkOrderStatus getStatus() {
        return status == null ? null : WorkOrderStatus.fromId(status);
    }

    public InPlantPartNumber getInPlantPartNumberId() {
        return inPlantPartNumberId;
    }

    public void setInPlantPartNumberId(InPlantPartNumber inPlantPartNumberId) {
        this.inPlantPartNumberId = inPlantPartNumberId;
    }

    public void setProcessFlowId(ProcessFlow processFlowId) {
        this.processFlowId = processFlowId;
    }

    public ProcessFlow getProcessFlowId() {
        return processFlowId;
    }

    public List<WorkOrderAndScreen> getWorkOrderAndScreen() {
        return workOrderAndScreen;
    }

    public void setWorkOrderAndScreen(List<WorkOrderAndScreen> workOrderAndScreen) {
        this.workOrderAndScreen = workOrderAndScreen;
    }

    public Integer getQuantityIssued() {
        return quantityIssued;
    }

    public void setQuantityIssued(Integer quantityIssued) {
        this.quantityIssued = quantityIssued;
    }

    public Integer getSupplementaryQuantity() {
        return supplementaryQuantity;
    }

    public void setSupplementaryQuantity(Integer supplementaryQuantity) {
        this.supplementaryQuantity = supplementaryQuantity;
    }

    public Warehouse getOutputWarehouse() {
        return outputWarehouse;
    }

    public void setOutputWarehouse(Warehouse outputWarehouse) {
        this.outputWarehouse = outputWarehouse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCompletedQuantity() {
        return completedQuantity;
    }

    public void setCompletedQuantity(Integer completedQuantity) {
        this.completedQuantity = completedQuantity;
    }

    public Integer getPlanProductionQuantity() {
        return planProductionQuantity;
    }

    public void setPlanProductionQuantity(Integer planProductionQuantity) {
        this.planProductionQuantity = planProductionQuantity;
    }

    public Integer getOrderProductionQuantity() {
        return orderProductionQuantity;
    }

    public void setOrderProductionQuantity(Integer orderProductionQuantity) {
        this.orderProductionQuantity = orderProductionQuantity;
    }

    public Order getOrderId() {
        return orderId;
    }

    public void setOrderId(Order orderId) {
        this.orderId = orderId;
    }
}
