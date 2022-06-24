package com.company.sangyo.core.storage;

import com.company.sangyo.entity.basic.Procedure;
import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.production.WorkOrder;
import com.company.sangyo.entity.storage.ProcessingMode;
import com.company.sangyo.entity.storage.ReworkType;
import com.company.sangyo.entity.storage.Unqualified;
import com.haulmont.cuba.security.entity.User;

import java.util.Set;
import java.util.UUID;

public interface UnqualifiedService {
    String NAME = "sangyo_UnqualifiedService";

    /**
     * 创建不合格品记录
     *
     * @param workOrder 工单
     * @param screen    网版
     * @param procedure 工序
     * @param user      用户
     * @param remark    备注
     * @return
     */
    String newUnqualified(WorkOrder workOrder, Screen screen, User user, String procedure, String remark, String abnormalParameter, Boolean isRepeated);

    /**
     * 不合格品库处理
     *
     * @param unqualified    不合格品库
     * @param reworkType     返工方式 A、B
     * @param processingMode 处理模式
     * @param procedure      工序
     * @param user           用户
     * @param remark         备注
     * @return
     */
    String processing(Unqualified unqualified, Procedure procedure, ReworkType reworkType, ProcessingMode processingMode, User user, String remark);

    /**
     * 可返工工序
     *
     * @param screenId
     * @param procedure
     * @param procedureName
     * @return
     */
    Set<Procedure> getAvailableProcedures(UUID screenId, Procedure procedure, String procedureName);
}
