package com.company.sangyo.entity.production;

import com.company.sangyo.entity.basic.PartNumberParameters;
import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.storage.QualityTraceability;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_PRODUCTION_RECORDS")
@Entity(name = "sangyo_ProductionRecords")
public class ProductionRecords extends StandardEntity {
    private static final long serialVersionUID = -1213123994524272230L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_ID")
    private Screen screen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARAMETER_ID")
    private PartNumberParameters parameter;

    @Column(name = "PARAMETER_VALUE")
    private String parameterValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUALITY_TRACEABILITY_ID")
    private QualityTraceability qualityTraceability;

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public PartNumberParameters getParameter() {
        return parameter;
    }

    public void setParameter(PartNumberParameters parameter) {
        this.parameter = parameter;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public QualityTraceability getQualityTraceability() {
        return qualityTraceability;
    }

    public void setQualityTraceability(QualityTraceability qualityTraceability) {
        this.qualityTraceability = qualityTraceability;
    }
}
