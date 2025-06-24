package com.vuvrecharge.modules.model;

public class PaymentModel {
    private int image;
    private String str;
    private String str2;
    private boolean isSelected;

    public PaymentModel() {
    }

    public PaymentModel(int image, String str, String str2) {
        this.image = image;
        this.str = str;
        this.str2 = str2;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }
}
