package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SANGYO_STATION")
@Entity(name = "sangyo_Station")
public class Station extends StandardEntity {
    private static final long serialVersionUID = 4606929307114363277L;

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "STATION_NUMBER")
    private String stationNumber;

    @JoinColumn(name = "WORKSHOP_ID_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Workshop workshopId;

    @Column(name = "STATION_STATUS")
    private String stationStatus;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "station")
    private List<StationAndUser> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(String stationNumber) {
        this.stationNumber = stationNumber;
    }

    public Workshop getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Workshop workshopId) {
        this.workshopId = workshopId;
    }

    public String getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(String stationStatus) {
        this.stationStatus = stationStatus;
    }

    public List<StationAndUser> getUsers() {
        return users;
    }

    public void setUsers(List<StationAndUser> users) {
        this.users = users;
    }
}
