package com.company.sangyo;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component(CachedRepositories.NAME)
public class CachedRepositories {

    public static final String NAME = "appbase_CachedRepositories";

    @Inject
    private Persistence persistence;

    @Inject
    private DataManager dataManager;

    @Inject
    private Metadata metadata;

    private Map<Class, Repository> repositoryCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> repositoryClass) {
        Repository repository = repositoryCache.get(repositoryClass);
        if (repository != null) {
            return (T) repository;
        }

        try (Transaction tx = persistence.createTransaction()) {
            SpringDataEntityManager em = new SpringDataEntityManager(persistence, dataManager, metadata);
            JpaRepositoryFactory repositoryFactory = new JpaRepositoryFactory(em);
            repositoryFactory.setRepositoryBaseClass(BaseRepositoryImpl.class);
            repository = (Repository) repositoryFactory.getRepository(repositoryClass);
            repositoryCache.put(repositoryClass, repository);
        }

        return (T) repository;
    }
}
