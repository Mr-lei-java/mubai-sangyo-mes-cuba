package com.company.sangyo;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.Map;

public class SpringDataEntityManager implements EntityManager {

    private com.haulmont.cuba.core.Persistence persistence;

    private DataManager dataManager;

    private Metadata metadata;

    public SpringDataEntityManager(com.haulmont.cuba.core.Persistence persistence, DataManager dataManager,
                                   Metadata metadata) {
        this.persistence = persistence;
        this.dataManager = dataManager;
        this.metadata = metadata;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public void persist(Object entity) {
        persistence.getEntityManager().persist((com.haulmont.cuba.core.entity.Entity) entity);
    }

    @Override
    public Object merge(Object entity) {
        return persistence.getEntityManager().merge((com.haulmont.cuba.core.entity.Entity) entity);
    }

    @Override
    public void remove(Object entity) {
        persistence.getEntityManager().remove((com.haulmont.cuba.core.entity.Entity) entity);
    }

    @Override
    public Object find(Class entityClass, Object primaryKey) {
        return persistence.getEntityManager().find(entityClass, primaryKey);
    }

    @Override
    public Object find(Class entityClass, Object primaryKey, Map properties) {
        return persistence.getEntityManager().find(entityClass, primaryKey);
    }

    @Override
    public Object find(Class entityClass, Object primaryKey, LockModeType lockMode) {
        return persistence.getEntityManager().find(entityClass, primaryKey);
    }

    @Override
    public Object find(Class entityClass, Object primaryKey, LockModeType lockMode, Map properties) {
        return persistence.getEntityManager().find(entityClass, primaryKey);
    }

    @Override
    public Object getReference(Class entityClass, Object primaryKey) {
        return persistence.getEntityManager().getReference(entityClass, primaryKey);
    }

    @Override
    public void flush() {
        persistence.getEntityManager().flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
        persistence.getEntityManager().getDelegate().setFlushMode(flushMode);
    }

    @Override
    public FlushModeType getFlushMode() {
        return persistence.getEntityManager().getDelegate().getFlushMode();
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        persistence.getEntityManager().getDelegate().lock(entity, lockMode);
    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        persistence.getEntityManager().getDelegate().lock(entity, lockMode, properties);
    }

    @Override
    public void refresh(Object entity) {
        persistence.getEntityManager().getDelegate().refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        persistence.getEntityManager().getDelegate().refresh(entity, properties);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        persistence.getEntityManager().getDelegate().refresh(entity, lockMode);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        persistence.getEntityManager().getDelegate().refresh(entity, lockMode, properties);
    }

    @Override
    public void clear() {
        persistence.getEntityManager().getDelegate().clear();
    }

    @Override
    public void detach(Object entity) {
        persistence.getEntityManager().getDelegate().detach(entity);
    }

    @Override
    public boolean contains(Object entity) {
        return persistence.getEntityManager().getDelegate().contains(entity);
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        return persistence.getEntityManager().getDelegate().getLockMode(entity);
    }

    @Override
    public void setProperty(String propertyName, Object value) {
        persistence.getEntityManager().getDelegate().setProperty(propertyName, value);
    }

    @Override
    public Map<String, Object> getProperties() {
        return persistence.getEntityManager().getDelegate().getProperties();
    }

    @Override
    public Query createQuery(String qlString) {
        com.haulmont.cuba.core.Query query = persistence.getEntityManager().createQuery(qlString);
        return query.getDelegate();
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return persistence.getEntityManager().getDelegate().createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        return persistence.getEntityManager().getDelegate().createQuery(updateQuery);
    }

    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        return persistence.getEntityManager().getDelegate().createQuery(deleteQuery);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        com.haulmont.cuba.core.Query query = persistence.getEntityManager().createQuery(qlString, resultClass);
        return (TypedQuery<T>) query.getDelegate();
    }

    @Override
    public Query createNamedQuery(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        com.haulmont.cuba.core.Query query = persistence.getEntityManager().createNativeQuery(sqlString);
        return query.getDelegate();
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        com.haulmont.cuba.core.Query query = persistence.getEntityManager().createNativeQuery(sqlString, resultClass);
        return query.getDelegate();
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return persistence.getEntityManager().getDelegate().createNativeQuery(sqlString, resultSetMapping);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void joinTransaction() {
        persistence.getEntityManager().getDelegate().joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return persistence.getEntityManager().getDelegate().isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return persistence.getEntityManager().getDelegate().unwrap(cls);
    }

    @Override
    public Object getDelegate() {
        return persistence.getEntityManager().getDelegate().getDelegate();
    }

    @Override
    public void close() {
        persistence.getEntityManager().getDelegate().close();
    }

    @Override
    public boolean isOpen() {
        return persistence.getEntityManager().getDelegate().isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return persistence.getEntityManager().getDelegate().getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return persistence.getEntityManager().getDelegate().getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return persistence.getEntityManager().getDelegate().getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return persistence.getEntityManager().getDelegate().getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        return persistence.getEntityManager().getDelegate().createEntityGraph(rootType);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        return persistence.getEntityManager().getDelegate().createEntityGraph(graphName);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        return persistence.getEntityManager().getDelegate().getEntityGraph(graphName);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        return persistence.getEntityManager().getDelegate().getEntityGraphs(entityClass);
    }
}
