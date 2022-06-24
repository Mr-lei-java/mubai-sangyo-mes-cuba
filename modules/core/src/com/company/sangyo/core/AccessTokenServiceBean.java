package com.company.sangyo.core;

import com.haulmont.addon.restapi.entity.AccessToken;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(AccessTokenService.NAME)
public class AccessTokenServiceBean implements AccessTokenService {

    @Inject
    private DataManager dataManager;

    @Override
    public String getTokenValue(String login) {
        LoadContext<AccessToken> loadContext = LoadContext.create(AccessToken.class).setQuery(
                LoadContext.createQuery("select e from sys$AccessToken e where e.userLogin = :login order by e.createTs desc").
                        setParameter("login", login)
        );

        AccessToken accessToken = dataManager.load(loadContext);
        if (accessToken == null) {
            return null;
        }
        return accessToken.getTokenValue();
    }
}
