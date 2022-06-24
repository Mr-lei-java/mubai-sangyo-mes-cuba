package com.company.sangyo.core;

import com.company.sangyo.entity.storage.Finished;
import com.company.sangyo.entity.storage.QualityTraceability;

import java.util.Set;

public interface QtExportService {
    String NAME = "sangyo_QtExportService";

    /**
     * 成品仓模板导出
     *
     * @param fINISHEDsTableSelected
     * @return
     */
    byte[] export(Set<Finished> fINISHEDsTableSelected);

    /**
     * 质量追溯导出
     *
     * @param qualityTraceabilitySet
     * @return
     */
    byte[] exportQualityTraceability(Set<QualityTraceability> qualityTraceabilitySet);
}
