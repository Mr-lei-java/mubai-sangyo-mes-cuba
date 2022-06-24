package com.company.sangyo.event;


import com.haulmont.addon.globalevents.GlobalApplicationEvent;
import com.haulmont.addon.globalevents.GlobalUiEvent;

/**
 * @author leitengfei
 */
public class OrderScreenCloseEvent extends GlobalApplicationEvent implements GlobalUiEvent {

    private String orderId;

    private String login;

    public OrderScreenCloseEvent(Object source, String orderId, String login) {
        super(source);
        this.orderId = orderId;
        this.login = login;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}

