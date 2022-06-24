package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SANGYO_PART_NUMBER_PARAMETERS")
@Entity(name = "sangyo_PartNumberParameters")
public class PartNumberParameters extends StandardEntity {
    private static final long serialVersionUID = -5368091977793641045L;

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPLOAD_FILES_ID")
    private FileDescriptor uploadFiles;

    @Column(name = "USE_")
    private String use;

    @Column(name = "PARAMETERSTYPE")
    private String parameterstype;

    @JoinTable(name = "SANGYO_QT_EXPORT_RULE_ITEM_PART_NUMBER_PARAMETERS_LINK",
            joinColumns = @JoinColumn(name = "PART_NUMBER_PARAMETERS_ID"),
            inverseJoinColumns = @JoinColumn(name = "QT_EXPORT_RULE_ITEM_ID"))
    @ManyToMany
    private List<QtExportRuleItem> qtExportRuleItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_VALUE_VERIFICATION_ID")
    private GroupValueVerification groupValueVerification;

    @JoinTable(name = "SANGYO_GROUP_VALUE_VERIFICATION_PART_NUMBER_PARAMETERS_LINK",
            joinColumns = @JoinColumn(name = "PART_NUMBER_PARAMETERS_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_VALUE_VERIFICATION_ID"))
    @ManyToMany
    private List<GroupValueVerification> groupValueVerifications;

    public void setUse(UseEnum use) {
        this.use = use == null ? null : use.getId();
    }

    public UseEnum getUse() {
        return use == null ? null : UseEnum.fromId(use);
    }

    public void setParameterstype(ParameterstypeEnum parameterstype) {
        this.parameterstype = parameterstype == null ? null : parameterstype.getId();
    }

    public ParameterstypeEnum getParameterstype() {
        return parameterstype == null ? null : ParameterstypeEnum.fromId(parameterstype);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileDescriptor getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(FileDescriptor uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public List<QtExportRuleItem> getQtExportRuleItems() {
        return qtExportRuleItems;
    }

    public void setQtExportRuleItems(List<QtExportRuleItem> qtExportRuleItems) {
        this.qtExportRuleItems = qtExportRuleItems;
    }

    public GroupValueVerification getGroupValueVerification() {
        return groupValueVerification;
    }

    public void setGroupValueVerification(GroupValueVerification groupValueVerification) {
        this.groupValueVerification = groupValueVerification;
    }

    public List<GroupValueVerification> getGroupValueVerifications() {
        return groupValueVerifications;
    }

    public void setGroupValueVerifications(List<GroupValueVerification> groupValueVerifications) {
        this.groupValueVerifications = groupValueVerifications;
    }
}
