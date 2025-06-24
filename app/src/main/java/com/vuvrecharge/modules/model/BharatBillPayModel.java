package com.vuvrecharge.modules.model;

public class BharatBillPayModel {

    String logo;
    String title;
    String redirect_link;
    String up_down_msg;
    private String highlight_text;;

    public BharatBillPayModel() {
    }

    public String getHighlight_text() {
        return highlight_text;
    }

    public void setHighlight_text(String highlight_text) {
        this.highlight_text = highlight_text;
    }

    public String getUp_down_msg() {
        return up_down_msg;
    }

    public void setUp_down_msg(String up_down_msg) {
        this.up_down_msg = up_down_msg;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRedirect_link() {
        return redirect_link;
    }

    public void setRedirect_link(String redirect_link) {
        this.redirect_link = redirect_link;
    }
}
