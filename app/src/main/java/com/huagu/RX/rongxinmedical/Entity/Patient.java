package com.huagu.RX.rongxinmedical.Entity;

import java.io.Serializable;

/**
 * Created by fff on 2016/8/15.
 */
public class Patient implements Serializable {


    private String AGE;
    private String USER_ID;
    private String updated;
    private String SEX;
    private String name;
    private String PHOTO;
    private String ADD_TIME;

    public Patient() {
    }

    public Patient(String AGE, String USER_ID, String updated, String SEX, String name, String PHOTO, String ADD_TIME) {
        this.AGE = AGE;
        this.USER_ID = USER_ID;
        this.updated = updated;
        this.SEX = SEX;
        this.name = name;
        this.PHOTO = PHOTO;
        this.ADD_TIME = ADD_TIME;
    }

    public String getADD_TIME() {
        return ADD_TIME;
    }

    public void setADD_TIME(String ADD_TIME) {
        this.ADD_TIME = ADD_TIME;
    }

    public void setAGE(String AGE) {
        this.AGE = AGE;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPHOTO(String PHOTO) {
        this.PHOTO = PHOTO;
    }

    public String getAGE() {
        return AGE;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public String getUpdated() {
        return updated;
    }

    public String getSEX() {
        return SEX;
    }

    public String getName() {
        return name;
    }

    public String getPHOTO() {
        return PHOTO;
    }
}
