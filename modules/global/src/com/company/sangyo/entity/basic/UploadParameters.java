package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_UPLOAD_PARAMETERS")
@Entity(name = "sangyo_UploadParameters")
public class UploadParameters extends StandardEntity {
    private static final long serialVersionUID = -3357353427559709860L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LABEL_TEMPLATE_ID")
    private LabelTemplate labelTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARAMETERS_ID")
    private PartNumberParameters parameters;

    public LabelTemplate getLabelTemplate() {
        return labelTemplate;
    }

    public void setLabelTemplate(LabelTemplate labelTemplate) {
        this.labelTemplate = labelTemplate;
    }

    public PartNumberParameters getParameters() {
        return parameters;
    }

    public void setParameters(PartNumberParameters parameters) {
        this.parameters = parameters;
    }
}
