package com.company.sangyo.core;

public interface AccessTokenService {
    String NAME = "sangyo_AccessTokenService";

    /**
     * 更具用户登录名查询用户token值
     *
     * @param login 用户登录名
     * @return
     */
    String getTokenValue(String login);
}
