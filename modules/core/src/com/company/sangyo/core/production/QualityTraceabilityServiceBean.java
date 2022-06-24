package com.company.sangyo.core.production;

import com.company.sangyo.entity.basic.PartNumberParameters;
import com.company.sangyo.entity.basic.Screen;
import com.company.sangyo.entity.production.DispatchList;
import com.company.sangyo.entity.production.ProductionRecords;
import com.company.sangyo.entity.production.WorkOrder;
import com.company.sangyo.entity.storage.QualityTraceability;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.User;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Service(QualityTraceabilityService.NAME)
public class QualityTraceabilityServiceBean implements QualityTraceabilityService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(QualityTraceabilityServiceBean.class);
    @Inject
    private DataManager dataManager;

    @Inject
    private Persistence persistence;

    @Override
    public List<ProductionRecords> getProductionRecords(Screen screen) {
        LoadContext<ProductionRecords> loadContext = LoadContext.create(ProductionRecords.class).setQuery(
                LoadContext.createQuery(" select e from sangyo_ProductionRecords e where " +
                        "e.screen =:screen and e.deleteTs is null order by e.createTs")
                        .setParameter("screen", screen)
        ).setView("productionRecords-view");
        return dataManager.loadList(loadContext);
    }


    @Override
    public List<ProductionRecords> getParameterRecord(Screen screen, PartNumberParameters partNumberParameters) {
        LoadContext<ProductionRecords> loadContext = LoadContext.create(ProductionRecords.class).setQuery(
                LoadContext.createQuery(" select e from sangyo_ProductionRecords e where e.screen =:screen and e.parameter = :parameter and e.deleteTs is null order by e.createTs")
                        .setParameter("screen", screen).setParameter("parameter",partNumberParameters)
        ).setView("productionRecords-view");
        return dataManager.loadList(loadContext);
    }

    @Override
    public String createQualityTraceability(User user, WorkOrder workOrder, Screen screen, DispatchList dispatchList) {
        Transaction transaction = persistence.createTransaction();
        try {
            Integer qualityTraceability1 = getQualityTraceability(screen);
            if (qualityTraceability1 == 0) {
                EntityManager entityManager = persistence.getEntityManager();
                QualityTraceability qualityTraceability = new QualityTraceability();
                qualityTraceability.setOperatingPersonnel(user);
                qualityTraceability.setWorkOrder(workOrder);
                qualityTraceability.setScreen(screen);
                qualityTraceability.setState("未进站");
                qualityTraceability.setDispatchList(dispatchList);
                entityManager.merge(qualityTraceability);
                entityManager.merge(dispatchList);
                transaction.commit();
            }
        } catch (Throwable e) {
            log.error("Error", e);
            return "424";
        } finally {
            transaction.end();
        }
        return "200";
    }

    @Override
    public QualityTraceability getQualityTraceability(Screen screen, User user, DispatchList dispatchList, String state) {
        LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                LoadContext.createQuery(" select e from sangyo_QualityTraceability e " +
                        "where e.screen =:screen and e.dispatchList =:dispatchList and e.state in('"+state+"') ")
                        .setParameter("screen", screen)
                        .setParameter("dispatchList", dispatchList)
        ).setView("qualityTraceability-view");
        return dataManager.load(qualityTraceabilityLoadContext);
    }

    @Override
    public QualityTraceability getLatestQualityTraceabilityByScreen(Screen screen) {
        LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                LoadContext.createQuery(" select e from sangyo_QualityTraceability e " +
                        "where e.screen =:screen order by e.createTs desc")
                        .setParameter("screen", screen)
        ).setView("qualityTraceability-view");
        return dataManager.load(qualityTraceabilityLoadContext);
    }


    public Integer getQualityTraceability(Screen screen) {
        LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                LoadContext.createQuery(" select e from sangyo_QualityTraceability e where e.screen =:screen " +
                        "and e.state = '未进站' order by e.createTs desc")
                        .setParameter("screen", screen)
        ).setView("qualityTraceability-view");
        List<QualityTraceability> qualityTraceability = dataManager.loadList(qualityTraceabilityLoadContext);
        return qualityTraceability.size();
    }


    @Override
    public void createQualityTraceability(QualityTraceability qualityTraceability) {
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            entityManager.merge(qualityTraceability);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
        } finally {
            transaction.end();
        }
    }

    @Override
    public String setQualityTraceability(QualityTraceability qualityTraceability) {
        if (getQualityTraceability(qualityTraceability.getScreen().getId()) != 0) {
            return "不能修改，这个网版存在未进站或已进站质量追溯";
        }
        Transaction transaction = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            qualityTraceability.setOutboundTime(null);
            qualityTraceability.setState("已进站");
            entityManager.merge(qualityTraceability);
            transaction.commit();
        } catch (Throwable e) {
            log.error("Error", e);
            return "修改失败";
        } finally {
            transaction.end();
        }
        return "修改成功";
    }

    @Override
    public Integer getQualityTraceability(UUID dispatchListName) {
        LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                LoadContext.createQuery("select e from sangyo_QualityTraceability e " +
                        "where e.screen.id =:id and e.state in('未进站','已进站')")
                        .setParameter("id", dispatchListName)
        ).setView("_minimal");
        List<QualityTraceability> qualityTraceabilityList = dataManager.loadList(qualityTraceabilityLoadContext);
        return qualityTraceabilityList.size();
    }
}
