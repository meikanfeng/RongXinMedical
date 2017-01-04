package com.huagu.RX.rongxinmedical.Entity;

/**
 * Created by fengm on 2016/8/2.
 */
public class Month {


    public Month(int year, int month, int[] day, int day_num, int dayofweeks) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.day_num = day_num;
        this.dayofweeks = dayofweeks;
    }

    private int year;

    private int month;

    private int[] day;

    private int day_num;

    private int dayofweeks;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int[] getDay() {
        return day;
    }

    public void setDay(int[] day) {
        this.day = day;
    }

    public int getDay_num() {
        return day_num;
    }

    public void setDay_num(int day_num) {
        this.day_num = day_num;
    }

    public int getDayofweeks() {
        return dayofweeks;
    }

    public void setDayofweeks(int dayofweeks) {
        this.dayofweeks = dayofweeks;
    }
}
