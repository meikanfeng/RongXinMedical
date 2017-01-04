package com.huagu.RX.rongxinmedical.Entity;

import java.io.Serializable;

/**
 * Created by fff on 2016/8/17.
 */
public class SleepRank implements Serializable {


    private String name;
    private String photo;
    private String score;
    private String user;

    public SleepRank() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
