package com.jarvis.be.dailychores.com.jarvis.be.dailychores.model;

/**
 * Created by parimal on 07-04-2015.
 */
public class ChoresModel {
    String title, address, lastUpdated, calId, calName;



    public ChoresModel(String title, String address, String lastUpdated, String calId, String calName) {
        this.title = title;
        this.address = address;
        this.lastUpdated = lastUpdated;
        this.calId = calId;
        this.calName = calName;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCalId() {
        return calId;
    }

    public void setCalId(String calId) {
        this.calId = calId;
    }

    public String getCalName() {
        return calName;
    }

    public void setCalName(String calName) {
        this.calName = calName;
    }
}
