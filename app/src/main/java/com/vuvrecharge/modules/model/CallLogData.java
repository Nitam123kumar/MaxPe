package com.vuvrecharge.modules.model;

public class CallLogData {

    private String name;
    private String number;
    private String date_time;
    private String type;
    private String duration;



    public CallLogData(String name, String number, String date_time, String type, String duration) {
        this.name = name;
        this.number = number;
        this.date_time = date_time;
        this.type = type;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
