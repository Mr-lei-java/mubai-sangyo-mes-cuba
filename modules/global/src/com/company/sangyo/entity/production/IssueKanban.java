package com.company.sangyo.entity.production;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SANGYO_ISSUE_KANBAN")
@Entity(name = "sangyo_IssueKanban")
public class IssueKanban extends StandardEntity {
    private static final long serialVersionUID = 7772769937829874617L;

    @Column(name = "ISSUE_TIME")
    private LocalDateTime issueTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ISSUE_USER_ID")
    private User issueUser;

    @Column(name = "ISSUE_TOTAL")
    private Integer issueTotal;

    @Column(name = "ACCOMPLISH_QUANTITY")
    private Integer accomplishQuantity;

    @Column(name = "PERFECTION_TIME")
    private LocalDateTime perfectionTime;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "issueKanban")
    private List<IssueKanbanAndDispatchList> issueKanbanAndDispatchList;

    @Column(name = "PERFECTION_RATE")
    private String perfectionRate;

    @Column(name = "REMARK")
    private String remark;

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }

    public User getIssueUser() {
        return issueUser;
    }

    public void setIssueUser(User issueUser) {
        this.issueUser = issueUser;
    }

    public Integer getIssueTotal() {
        return issueTotal;
    }

    public void setIssueTotal(Integer issueTotal) {
        this.issueTotal = issueTotal;
    }

    public Integer getAccomplishQuantity() {
        return accomplishQuantity;
    }

    public void setAccomplishQuantity(Integer accomplishQuantity) {
        this.accomplishQuantity = accomplishQuantity;
    }

    public LocalDateTime getPerfectionTime() {
        return perfectionTime;
    }

    public void setPerfectionTime(LocalDateTime perfectionTime) {
        this.perfectionTime = perfectionTime;
    }

    public List<IssueKanbanAndDispatchList> getIssueKanbanAndDispatchList() {
        return issueKanbanAndDispatchList;
    }

    public void setIssueKanbanAndDispatchList(List<IssueKanbanAndDispatchList> issueKanbanAndDispatchList) {
        this.issueKanbanAndDispatchList = issueKanbanAndDispatchList;
    }

    public String getPerfectionRate() {
        return perfectionRate;
    }

    public void setPerfectionRate(String perfectionRate) {
        this.perfectionRate = perfectionRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
