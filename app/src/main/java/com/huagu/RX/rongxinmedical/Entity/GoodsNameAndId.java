package com.huagu.RX.rongxinmedical.Entity;

/**
 * Created by Administrator on 2017/1/14.
 */
public class GoodsNameAndId {
    private String gc_name;
    private String id;

    public String getGc_name() {
        return gc_name;
    }

    public void setGc_name(String gc_name) {
        this.gc_name = gc_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GoodsNameAndId(String gc_name, String id) {
        this.gc_name = gc_name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "GoodsNameAndId [gc_name=" + gc_name + ", id=" + id + "]";
    }
}
