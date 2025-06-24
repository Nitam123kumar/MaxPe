package com.vuvrecharge.modules.model;

import java.util.Date;

public class WeekData {

    private Date start_date;
    private Date end_date;
    private String id;

    public WeekData() {
    }

    public WeekData(String id, Date start_date, Date end_date) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.id = id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
