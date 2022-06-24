package com.company.sangyo.core;

import java.util.List;

public interface NativeQueryService {
    String NAME = "sangyo_NativeQueryService";

    List<Object[]> getListData(String sql);

    Object getSingleResult(String sql);
}
