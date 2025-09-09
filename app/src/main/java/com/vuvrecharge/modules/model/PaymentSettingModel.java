package com.vuvrecharge.modules.model;

public class PaymentSettingModel {

    private Object image;
    private String str;
    private String str2;
    private String packageName;

    public PaymentSettingModel(Object image, String str, String str2, String packageName) {
        this.image = image;
        this.str = str;
        this.str2 = str2;
        this.packageName = packageName;
    }

    public Object getImage() { return image; }
    public String getStr() { return str; }
    public String getStr2() { return str2; }
    public String getPackageName() { return packageName; }

}
