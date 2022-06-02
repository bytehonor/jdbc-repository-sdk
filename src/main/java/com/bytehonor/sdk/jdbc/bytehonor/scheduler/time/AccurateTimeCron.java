package com.bytehonor.sdk.jdbc.bytehonor.scheduler.time;

import java.time.LocalDateTime;

public class AccurateTimeCron implements TimeCron {

    private int minute;

    private int hour;

    private int day;

    public AccurateTimeCron() {
        this(ANY, ANY, ANY);
    }

    public AccurateTimeCron(int minute, int hour, int day) {
        this.minute = minute;
        this.hour = hour;
        this.day = day;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public boolean match(LocalDateTime ldt) {
        if (ldt == null) {
            return false;
        }
        if (ANY != this.day && ldt.getDayOfMonth() != this.day) {
            return false;
        }
        if (ANY != this.hour && ldt.getHour() != this.hour) {
            return false;
        }
        if (ANY != this.minute && ldt.getMinute() != this.minute) {
            return false;
        }
        return true;
    }

}
