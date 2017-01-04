package com.huagu.RX.rongxinmedical.Entity;

import java.io.Serializable;

/**
 * Created by fff on 2016/8/8.
 */
public class Press implements Serializable{

    private double max;

    private double middle;

    private double p95;

    public Press(double max, double middle, double p95) {
        this.max = max;
        this.middle = middle;
        this.p95 = p95;
    }

    public double getMax() {
        return max;
    }

    public double getMiddle() {
        return middle;
    }

    public double getP95() {
        return p95;
    }

}
