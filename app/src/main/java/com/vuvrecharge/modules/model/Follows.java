package com.vuvrecharge.modules.model;

public class Follows {
    String logo;
    String title;
    String link;

    public Follows() {
    }

    public Follows(String logo, String title, String link) {
        this.logo = logo;
        this.title = title;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
}
