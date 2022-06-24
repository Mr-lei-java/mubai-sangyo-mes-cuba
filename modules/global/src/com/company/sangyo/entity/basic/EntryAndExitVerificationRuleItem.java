package com.company.sangyo.entity.basic;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SANGYO_ENTRY_AND_EXIT_VERIFICATION_RULE_ITEM")
@Entity(name = "sangyo_EntryAndExitVerificationRuleItem")
public class EntryAndExitVerificationRuleItem extends StandardEntity {
    private static final long serialVersionUID = 16854630424117043L;

    @JoinColumn(name = "PARAMETERS_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private PartNumberParameters parametersId;

    @Column(name = "ALIGNMENT_METHOD", nullable = false)
    @NotNull
    private String alignmentMethod;

    @JoinColumn(name = "SCREEN_PARAMETERS_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private PartNumberParameters screenParameters;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INBOUND_AND_OUTBOUND_CALIBRATION_RULES_ID")
    @NotNull
    private InboundAndOutboundCalibrationRules inboundAndOutboundCalibrationRules;

    public void setAlignmentMethod(CalibrationMethodEnum alignmentMethod) {
        this.alignmentMethod = alignmentMethod == null ? null : alignmentMethod.getId();
    }

    public CalibrationMethodEnum getAlignmentMethod() {
        return alignmentMethod == null ? null : CalibrationMethodEnum.fromId(alignmentMethod);
    }

    public PartNumberParameters getParametersId() {
        return parametersId;
    }

    public void setParametersId(PartNumberParameters parametersId) {
        this.parametersId = parametersId;
    }

    public PartNumberParameters getScreenParameters() {
        return screenParameters;
    }

    public void setScreenParameters(PartNumberParameters screenParameters) {
        this.screenParameters = screenParameters;
    }

    public InboundAndOutboundCalibrationRules getInboundAndOutboundCalibrationRules() {
        return inboundAndOutboundCalibrationRules;
    }

    public void setInboundAndOutboundCalibrationRules(InboundAndOutboundCalibrationRules inboundAndOutboundCalibrationRules) {
        this.inboundAndOutboundCalibrationRules = inboundAndOutboundCalibrationRules;
    }
}
