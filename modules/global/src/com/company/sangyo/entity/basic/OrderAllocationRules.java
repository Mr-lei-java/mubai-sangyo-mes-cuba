package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SANGYO_ORDER_ALLOCATION_RULES")
@Entity(name = "sangyo_OrderAllocationRules")
public class OrderAllocationRules extends StandardEntity {
    private static final long serialVersionUID = 1449681075319545460L;

    @Column(name = "RULE_CODING")
    private String ruleCoding;

    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Column(name = "STATE", nullable = false)
    @NotNull
    private String state;

    @Column(name = "REMARK")
    private String remark;

    @OneToMany(mappedBy = "orderAllocationRules")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<OrderAllocationRuleItem> orderAllocationRuleItem;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "orderAllocationRules")
    private List<GroupValueVerification> groupValueVerification;

    public void setState(GeneralState state) {
        this.state = state == null ? null : state.getId();
    }

    public GeneralState getState() {
        return state == null ? null : GeneralState.fromId(state);
    }

    public String getRuleCoding() {
        return ruleCoding;
    }

    public void setRuleCoding(String ruleCoding) {
        this.ruleCoding = ruleCoding;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<OrderAllocationRuleItem> getOrderAllocationRuleItem() {
        return orderAllocationRuleItem;
    }

    public void setOrderAllocationRuleItem(List<OrderAllocationRuleItem> orderAllocationRuleItem) {
        this.orderAllocationRuleItem = orderAllocationRuleItem;
    }

    public List<GroupValueVerification> getGroupValueVerification() {
        return groupValueVerification;
    }

    public void setGroupValueVerification(List<GroupValueVerification> groupValueVerification) {
        this.groupValueVerification = groupValueVerification;
    }
}
