package com.vuvrecharge.modules.model;

public class DashboardMenu {

    private String logo;
    private String title;
    private String redirect_link;
    private String service_type;
    private String highlight_text;
    private String up_down_msg;

    public DashboardMenu() {
    }

    public DashboardMenu(String logo, String title, String redirect_link, String service_type, String highlight_text, String up_down_msg) {
        this.logo = logo;
        this.title = title;
        this.redirect_link = redirect_link;
        this.service_type = service_type;
        this.highlight_text = highlight_text;
        this.up_down_msg = up_down_msg;
    }

    public String getUp_down_msg() {
        return up_down_msg;
    }

    public void setUp_down_msg(String up_down_msg) {
        this.up_down_msg = up_down_msg;
    }

    public String getHighlight_text() {
        return highlight_text;
    }

    public void setHighlight_text(String highlight_text) {
        this.highlight_text = highlight_text;
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

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }
}
