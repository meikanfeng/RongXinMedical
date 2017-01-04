package com.huagu.RX.rongxinmedical.Entity;

import java.io.Serializable;

/**
 * Created by fff on 2016/8/18.
 */
public class PatientMessage implements Serializable {

    private String HEAD_PHOTO;
    private String NO_READ_COUNT;
    private String CONTENT;
    private String SENDER_ID;
    private String USERNAME;
    private String ADD_TIME;

    public PatientMessage() {
    }

    public String getHEAD_PHOTO() {
        return HEAD_PHOTO;
    }

    public void setHEAD_PHOTO(String HEAD_PHOTO) {
        this.HEAD_PHOTO = HEAD_PHOTO;
    }

    public String getNO_READ_COUNT() {
        return NO_READ_COUNT;
    }

    public void setNO_READ_COUNT(String NO_READ_COUNT) {
        this.NO_READ_COUNT = NO_READ_COUNT;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getSENDER_ID() {
        return SENDER_ID;
    }

    public void setSENDER_ID(String SENDER_ID) {
        this.SENDER_ID = SENDER_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getADD_TIME() {
        return ADD_TIME;
    }

    public void setADD_TIME(String ADD_TIME) {
        this.ADD_TIME = ADD_TIME;
    }
}
