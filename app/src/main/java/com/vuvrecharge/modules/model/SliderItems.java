package com.vuvrecharge.modules.model;

public class SliderItems {
    //set to String, if you want to add image url from internet

    String discount_text;
    String recharge_text;
    String recharge_btn_text;
    String recharge_logo;
    String redirect;
    String color_code;
    String other_redirect_url;

    public SliderItems() {
    }

    public String getOther_redirect_url() {
        return other_redirect_url;
    }

    public void setOther_redirect_url(String other_redirect_url) {
        this.other_redirect_url = other_redirect_url;
    }

    public String getDiscount_text() {
        return discount_text;
    }

    public void setDiscount_text(String discount_text) {
        this.discount_text = discount_text;
    }

    public String getRecharge_text() {
        return recharge_text;
    }

    public void setRecharge_text(String recharge_text) {
        this.recharge_text = recharge_text;
    }

    public String getRecharge_btn_text() {
        return recharge_btn_text;
    }

    public void setRecharge_btn_text(String recharge_btn_text) {
        this.recharge_btn_text = recharge_btn_text;
    }

    public String getRecharge_logo() {
        return recharge_logo;
    }

    public void setRecharge_logo(String recharge_logo) {
        this.recharge_logo = recharge_logo;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }
}
