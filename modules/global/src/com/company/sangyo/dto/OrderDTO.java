package com.company.sangyo.dto;

import java.io.Serializable;

/**
 * @author leitengfei
 */
public class OrderDTO implements Serializable {

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
