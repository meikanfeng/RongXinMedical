package com.huagu.RX.rongxinmedical.Entity;

import java.io.Serializable;

/**
 * Created by fff on 2016/8/17.
 */
public class Message implements Serializable {

    private int STATE;
    private String RECEIVER_ID;
    private String CONTENT;
    private String SENDER_ID;
    private String ID;
    private String ADD_TIME;

    public Message() {
    }

    public int getSTATE() {
        return STATE;
    }

    public void setSTATE(int STATE) {
        this.STATE = STATE;
    }

    public String getRECEIVER_ID() {
        return RECEIVER_ID;
    }

    public void setRECEIVER_ID(String RECEIVER_ID) {
        this.RECEIVER_ID = RECEIVER_ID;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getADD_TIME() {
        return ADD_TIME;
    }

    public void setADD_TIME(String ADD_TIME) {
        this.ADD_TIME = ADD_TIME;
    }
}
