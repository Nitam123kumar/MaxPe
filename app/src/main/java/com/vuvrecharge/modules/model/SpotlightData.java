package com.vuvrecharge.modules.model;

public class SpotlightData {

    private String title;
    private String redirect_link;
    private String logo;
    private String type;
    private String data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getRedirectLink() {
        return redirect_link;
    }

    public void setRedirectLink(String redirect_link) {
        this.redirect_link = redirect_link;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
