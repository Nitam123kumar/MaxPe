package com.vuvrecharge.modules.model;

import java.util.List;

public class OfferSlider {

    private String title;
    private String url;
    private String logo;
    private String data;
    private String redirection_type;

    public String getOfferTitle() {
        return title;
    }

    public void setOfferTitle(String title) {
        this.title = title;
    }

    public String getRedirection_type() {
        return redirection_type;
    }

    public void setRedirection_type(String redirection_type) {
        this.redirection_type = redirection_type;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
