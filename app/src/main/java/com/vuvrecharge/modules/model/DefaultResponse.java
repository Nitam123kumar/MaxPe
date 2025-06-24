package com.vuvrecharge.modules.model;

public class DefaultResponse {

    private int success;
    private String message;
    private String data;
    private int required_amount = 0;
    private double discount_amount = 0.00;

    public int getRequired_amount() {
        return required_amount;
    }

    public void setRequired_amount(int required_amount) {
        this.required_amount = required_amount;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

}
