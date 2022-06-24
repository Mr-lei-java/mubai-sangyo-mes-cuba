package com.company.sangyo.core;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(NativeQueryService.NAME)
public class NativeQueryServiceBean implements NativeQueryService {
    protected static final Logger log = LoggerFactory.getLogger(NativeQueryServiceBean.class);

    @Inject
    private Persistence persistence;

    @Override
    public List<Object[]> getListData(String sql) {
        log.info(sql);
        List<Object[]> list = null;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager entityManager = persistence.getEntityManager();
            Query query = entityManager.createNativeQuery(sql);
            list = query.getResultList();
        }
        return list;
    }

    @Override
    public Object getSingleResult(String sql) {
        log.info(sql);
        Object result = null;
        try (Transaction transaction = persistence.getTransaction()) {
            EntityManager entityManager = persistence.getEntityManager();
            Query query = entityManager.createNativeQuery(sql);
            result = query.getSingleResult();
        }
        return result;
    }
}
