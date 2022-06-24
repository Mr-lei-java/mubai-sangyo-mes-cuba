package com.company.sangyo;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.*;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseRepositoryImpl<T extends Entity<ID>, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private DataManager dataManager;

    private Metadata metadata;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.dataManager = ((com.mubai.yalong.SpringDataEntityManager) entityManager).getDataManager();
        this.metadata = ((com.mubai.yalong.SpringDataEntityManager) entityManager).getMetadata();
    }

    @Override
    public T load(ID id, String view) {
        LoadContext<T> loadContext = LoadContext.create(getDomainClass())
                .setId(id)
                .setView(view);
        return dataManager.load(loadContext);
    }

    @Override
    public T load(String where, Map<String, Object> parameters, String view) {
        MetaClass metaClass = metadata.getClass(getDomainClass());
        Assert.notNull(metaClass, "MetaClass must not be null!");

        String name = metaClass.getName();
        LoadContext<T> loadContext = LoadContext.create(getDomainClass())
                .setQuery(LoadContext.createQuery("select e from " + name + " e where " + where)
                        .setParameters(parameters))
                .setView(view);
        return dataManager.load(loadContext);
    }

    @Override
    public T load(LoadContext<T> loadContext) {
        return dataManager.load(loadContext);
    }

    @Override
    public List<T> loadList(String view) {
        MetaClass metaClass = metadata.getClass(getDomainClass());
        Assert.notNull(metaClass, "MetaClass must not be null!");

        String name = metaClass.getName();
        LoadContext<T> loadContext = LoadContext.create(getDomainClass())
                .setQuery(LoadContext.createQuery("select e from " + name + " e"))
                .setView(view);
        return dataManager.loadList(loadContext);
    }

    @Override
    public List<T> loadList(String where, Map<String, Object> parameters, String view) {
        MetaClass metaClass = metadata.getClass(getDomainClass());
        Assert.notNull(metaClass, "MetaClass must not be null!");

        String name = metaClass.getName();
        LoadContext<T> loadContext = LoadContext.create(getDomainClass())
                .setQuery(LoadContext.createQuery("select e from " + name + " e where " + where)
                        .setParameters(parameters))
                .setView(view);
        return dataManager.loadList(loadContext);
    }

    @Override
    public List<T> loadList(LoadContext<T> loadContext) {
        return dataManager.loadList(loadContext);
    }

    @Override
    public List<KeyValueEntity> loadValues(List<String> properties) {
        MetaClass metaClass = metadata.getClass(getDomainClass());
        Assert.notNull(metaClass, "MetaClass must not be null!");

        String name = metaClass.getName();
        ValueLoadContext context = ValueLoadContext.create()
                .setQuery(ValueLoadContext.createQuery("select e from " + name + " e"))
                .setProperties(properties);
        return dataManager.loadValues(context);
    }

    @Override
    public List<KeyValueEntity> loadValues(String where, Map<String, Object> parameters, List<String> properties) {
        MetaClass metaClass = metadata.getClass(getDomainClass());
        Assert.notNull(metaClass, "MetaClass must not be null!");

        String name = metaClass.getName();
        ValueLoadContext context = ValueLoadContext.create()
                .setQuery(ValueLoadContext.createQuery("select e from " + name + " e where " + where)
                        .setParameters(parameters))
                .setProperties(properties);
        return dataManager.loadValues(context);
    }

    @Override
    public List<KeyValueEntity> loadValues(ValueLoadContext valueLoadContext) {
        return dataManager.loadValues(valueLoadContext);
    }

    @Override
    public long getCount() {
        LoadContext<T> loadContext = LoadContext.create(getDomainClass());
        return dataManager.getCount(loadContext);
    }

    @Override
    public long getCount(String where, Map<String, Object> parameters) {
        MetaClass metaClass = metadata.getClass(getDomainClass());
        Assert.notNull(metaClass, "MetaClass must not be null!");

        String name = metaClass.getName();
        LoadContext<T> loadContext = LoadContext.create(getDomainClass())
                .setQuery(LoadContext.createQuery("select e from " + name + " e where " + where)
                        .setParameters(parameters));
        return dataManager.getCount(loadContext);
    }

    @Override
    public long getCount(LoadContext<T> loadContext) {
        return dataManager.getCount(loadContext);
    }

    @Override
    public T commit(T entity) {
        return dataManager.commit(entity);
    }

    @Override
    public T commit(T entity, String view) {
        return dataManager.commit(entity, view);
    }

    @Override
    public Set<Entity> commit(CommitContext commitContext) {
        return dataManager.commit(commitContext);
    }

    @Override
    public T reload(T entity, String view) {
        return dataManager.reload(entity, view);
    }

    @Override
    public T reload(T entity, String view, boolean loadDynamicAttributes) {
        return dataManager.reload(entity, metadata.getViewRepository().getView(entity.getClass(), view),
                null, loadDynamicAttributes);
    }

    @Override
    public void remove(T entity) {
        dataManager.remove(entity);
    }
}
