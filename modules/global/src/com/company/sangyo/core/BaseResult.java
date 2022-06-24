package com.company.sangyo.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseResult<T> implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String code;
    private String msg;
    private List<T> list;
    private Set<T> set;
    private Map<String, Object> map;
    private PageInfo<T> pageInfo;

    public BaseResult(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public BaseResult(String code, String msg, List<T> list) {
        super();
        this.code = code;
        this.msg = msg;
        this.list = list;
    }

    public BaseResult(String code, String msg, PageInfo<T> pageInfo) {
        super();
        this.code = code;
        this.msg = msg;
        this.pageInfo = pageInfo;
    }

    public BaseResult(String code, String msg, Set<T> set) {
        super();
        this.code = code;
        this.msg = msg;
        this.set = set;
    }

    public BaseResult(String code, String msg, List<T> list, Set<T> set) {
        super();
        this.code = code;
        this.msg = msg;
        this.list = list;
        this.set = set;
    }

    public BaseResult(String code, String msg, Map<String, Object> map) {
        super();
        this.code = code;
        this.msg = msg;
        this.map = map;
    }

    public BaseResult(String code, String msg, List<T> list, Map<String, Object> map) {
        this.code = code;
        this.msg = msg;
        this.list = list;
        this.map = map;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Set<T> getSet() {
        return set;
    }

    public void setSet(Set<T> set) {
        this.set = set;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public PageInfo<T> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<T> pageInfo) {
        this.pageInfo = pageInfo;
    }
}

