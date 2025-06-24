package com.vuvrecharge.modules.model;

import java.util.Date;

public class DateData {

    private Date date;
    private String id;

    public DateData() {
    }

    public DateData(String id, Date date) {
        this.date = date;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
