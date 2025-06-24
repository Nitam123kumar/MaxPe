package com.vuvrecharge.modules.model;

import android.graphics.Bitmap;

public class VideoData {
    private String title;
    private String duration;
    private String size;
    private String path;
    private Bitmap image;

    public VideoData(String title, String path, String duration, String size, Bitmap image) {
        this.title = title;
        this.path = path;
        this.duration = duration;
        this.size = size;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
