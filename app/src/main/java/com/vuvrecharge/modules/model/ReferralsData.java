package com.vuvrecharge.modules.model;

public class ReferralsData {


    /**
     * name : pramod
     * date_time : 2019-08-30 19:22:07
     * refer : 0
     */

    private String name;
    private String date_time;
    private String refer;
    private String date;
    private String income;
    private String order_id;
    private String type;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirst_mobile() {
        return first_mobile;
    }

    public void setFirst_mobile(String first_mobile) {
        this.first_mobile = first_mobile;
    }

    public String getLast_mobile() {
        return last_mobile;
    }

    public void setLast_mobile(String last_mobile) {
        this.last_mobile = last_mobile;
    }

    private String first_mobile;
    private String last_mobile;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }
}
