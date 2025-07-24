package com.vuvrecharge.modules.model;

public class OTTSubscriptionsData {
    private String title;
    private String logo;
    private String redirection_type;
    private String url;
    private String data;
    public String getData() {
        return data;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRedirection_type() {
        return redirection_type;
    }

    public void setRedirection_type(String redirection_type) {
        this.redirection_type = redirection_type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

}
