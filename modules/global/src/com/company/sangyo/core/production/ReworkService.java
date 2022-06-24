package com.company.sangyo.core.production;

import com.company.sangyo.entity.basic.Procedure;
import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.storage.ReworkRecord;
import com.company.sangyo.entity.storage.ReworkType;
import com.company.sangyo.entity.storage.Warehouse;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.security.entity.User;

import java.util.Collection;

public interface ReworkService {
    String NAME = "sangyo_ReworkService";

    /**
     * 不合格品库返工
     *
     * @param screen
     * @param sourceWarehouse
     * @param sourceProcedure
     * @param procedure
     * @param reworkType
     * @param user
     * @param remark
     */
    void rework(Screen screen, Warehouse sourceWarehouse, Procedure sourceProcedure, Procedure procedure,
                ReworkType reworkType, User user, String remark, Boolean isRepeated);

    String updateReworkRecord(ReworkRecord reworkRecord);

    Collection<KeyValueEntity> getReworkRateCollection(String startDate, String endDate, String tradeDress, String procedureName);

    Collection<KeyValueEntity> getScrappageCollection(String startDate, String endDate, String tradeDress);
}
