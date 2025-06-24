package com.vuvrecharge.modules.model;

/**
 * Created by Pawan on 4/8/2016.
 */
public class DirectoryItem {
    String TITLE;
    String DATA;

    public DirectoryItem(String TITLE,
                         String DATA) {
        this.TITLE = TITLE;
        this.DATA = DATA;
    }

    public DirectoryItem() {

    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }
}
