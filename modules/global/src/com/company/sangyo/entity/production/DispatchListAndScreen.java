package com.company.sangyo.entity.production;

import com.company.sangyo.entity.basic.Screen;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SANGYO_DISPATCH_LIST_AND_SCREEN")
@Entity(name = "sangyo_DispatchListAndScreen")
public class DispatchListAndScreen extends StandardEntity {
    private static final long serialVersionUID = 2895916816678491254L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISPATCH_LIST_ID")
    private DispatchList dispatchList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_ID")
    private Screen screen;

    @Column(name = "STATUS")
    private Boolean status;

    public DispatchList getDispatchList() {
        return dispatchList;
    }

    public void setDispatchList(DispatchList dispatchList) {
        this.dispatchList = dispatchList;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
