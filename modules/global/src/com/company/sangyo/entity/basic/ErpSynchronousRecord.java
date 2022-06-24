package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "SANGYO_ERP_SYNCHRONOUS_RECORD")
@Entity(name = "sangyo_ErpSynchronousRecord")
public class ErpSynchronousRecord extends StandardEntity {
    private static final long serialVersionUID = 6843493599337809251L;

    @Column(name = "TYPE_")
    private String type;

    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;

    @Lob
    @Column(name = "PARAMETER_")
    private String parameter;

    @Lob
    @Column(name = "ORIGIN")
    private String origin;

    @Column(name = "IS_SYNCHRONOUS")
    private Boolean isSynchronous;

    @Column(name = "IS_PROCESSED")
    private Boolean isProcessed;

    @Lob
    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "OPERATE_DATE")
    private LocalDateTime operateDate;

    @Lob
    @Column(name = "REMARK")
    private String remark;

    @Lob
    @Column(name = "PARAMETER_PASSING")
    private String parameterPassing;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Boolean getSynchronous() {
        return isSynchronous;
    }

    public void setSynchronous(Boolean synchronous) {
        isSynchronous = synchronous;
    }

    public Boolean getProcessed() {
        return isProcessed;
    }

    public void setProcessed(Boolean processed) {
        isProcessed = processed;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public LocalDateTime getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(LocalDateTime operateDate) {
        this.operateDate = operateDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParameterPassing() {
        return parameterPassing;
    }

    public void setParameterPassing(String parameterPassing) {
        this.parameterPassing = parameterPassing;
    }
}
