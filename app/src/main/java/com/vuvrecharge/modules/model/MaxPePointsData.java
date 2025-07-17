package com.vuvrecharge.modules.model;

public class MaxPePointsData {
    private String order_id;
    private String amount;
    private String credit;
    private String debit;
    private String opening_points;
    private String closing_points;
    private String date_time;
    private String type;
    private String remark;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getOpening_points() {
        return opening_points;
    }

    public void setOpening_points(String opening_points) {
        this.opening_points = opening_points;
    }

    public String getClosing_points() {
        return closing_points;
    }

    public void setClosing_points(String closing_points) {
        this.closing_points = closing_points;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
