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

@Table(name = "SANGYO_LABEL_TEMPLATE")
@Entity(name = "sangyo_LabelTemplate")
public class LabelTemplate extends StandardEntity {
    private static final long serialVersionUID = 144092179866698960L;

    @Column(name = "EMPLATE_NAME", nullable = false)
    @NotNull
    private String templateName;

    @Column(name = "IS_PRINT_WORK_ORDER")
    private Boolean isPrintWorkOrder;

    @Column(name = "TEMPLATE_CODING", nullable = false)
    @NotNull
    private String templateCoding;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REMARK")
    private String remark;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "labelTemplate")
    private List<MaterialParameters> materialParameters;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "labelTemplate")
    private List<UploadParameters> uploadParameters;
}
