package com.company.sangyo.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultString;


@Source(type = SourceType.DATABASE)
public interface NewConfig extends Config {

    //正式服嵌入网站地址
//    @Property("frontend.url")
//    @DefaultString("http://114.217.18.137:8082")
//    String getFrontendURL();

    //    @Property("frontend.url")
//    @DefaultString("http://192.168.0.147:8021")
//    String getFrontendURL();
    //测试服网站地址
    @Property("frontend.url")
    @DefaultString("http://47.102.153.162:8010")
    String getFrontendURL();

//    @Property("frontend.url")
//    @DefaultString("http://localhost:4200")
//    String getFrontendURL();

    //erp网址接口地址
//    @Property("erp.url")
//    @DefaultString("http://114.217.18.137:8089")
//    String getERPURL();
//
//    @Property("erp.isEnableCustomer")
//    @DefaultString("false")
//    String getERPIsEnableCustomer();
//
//    @Property("erp.isEnableInvBas")
//    @DefaultString("false")
//    String getERPIsEnableInvBas();
//
//    @Property("erp.isEnableSaleOrder")
//    @DefaultString("false")
//    String getERPIsEnableSaleOrder();
//
//    @Property("erp.isEnableSaleOut")
//    @DefaultString("false")
//    String getERPIsEnableSaleOut();
//
//    @Property("erp.isEnableMaterialOut")
//    @DefaultString("false")
//    String getERPIsEnableMaterialOut();
//
//    @Property("erp.isEnableMaterialStorage")
//    @DefaultString("false")
//    String getERPIsEnableMaterialStorage();
//
//    @Property("erp.isEnableMorphologicalTransformation")
//    @DefaultString("false")
//    String getERPIsEnableMorphologicalTransformation();
//
//    @Property("erp.isEnable")
//    @DefaultString("false")
//    String getERPIsEnable();
}
