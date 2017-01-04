package com.huagu.RX.rongxinmedical.Entity;

/**
 * Created by fff on 2016/8/1.
 */
public class CircleScheduleModel {

    public CircleScheduleModel(String name, int range, double position, int color) {
        this.name = name;
        this.Range = range;
        this.position = position;
        this.color = color;
    }

    private String name;
    private int Range;
    private double position;
    private int color;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getRange() {
        return Range;
    }
    public void setRange(int range) {
        Range = range;
    }
    public double getPosition() {
        return position;
    }
    public void setPosition(double position) {
        this.position = position;
    }
    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

}
