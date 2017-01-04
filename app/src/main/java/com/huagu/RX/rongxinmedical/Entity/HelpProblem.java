package com.huagu.RX.rongxinmedical.Entity;

import java.io.Serializable;

/**
 * Created by fff on 2016/8/16.
 */
public class HelpProblem implements Serializable {

    private String CONTENT;
    private String ID;
    private String ADD_TIME;
    private String TITLE;

    public HelpProblem() {
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
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

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }
}
