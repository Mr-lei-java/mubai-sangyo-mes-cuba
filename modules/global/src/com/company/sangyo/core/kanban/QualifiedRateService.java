package com.company.sangyo.core.kanban;

import com.haulmont.cuba.core.entity.KeyValueEntity;

import java.util.Collection;
import java.util.List;

public interface QualifiedRateService {
    String NAME = "sangyo_QualifiedRateService";

    Collection<KeyValueEntity> getQualifiedRateCollection(String startDate, String endDate, String tradeDress);

    List<String> getAllTradeDress(String tradeDress);

    List<String> getAllTradeDress();

    List<String> getAllProcedureName();
}
