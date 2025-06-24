package com.vuvrecharge.modules.model;

public class PayData {

    /**
     * orderid : 1000007
     * name : Rakesh Kumar
     * amount : 1
     * time : 1574055453
     * mobile : 9664160650
     */

    private String orderid;
    private String name;
    private String amount;
    private String time;
    private String mobile;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}