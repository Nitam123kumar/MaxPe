package com.vuvrecharge.modules.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Res {
    @SerializedName("message")
    @Expose
    private String message;



    @SerializedName("CHECKSUMHASH")
    @Expose
    private String CHECKSUMHASH;
    @SerializedName("orderid")
    @Expose
    private String orderid;




    public String getCHECKSUMHASH() {
        return CHECKSUMHASH;
    }

    public void setCHECKSUMHASH(String CHECKSUMHASH) {
        this.CHECKSUMHASH = CHECKSUMHASH;
    }



    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
