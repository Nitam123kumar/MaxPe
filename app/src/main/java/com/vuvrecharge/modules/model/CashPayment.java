package com.vuvrecharge.modules.model;

public class CashPayment {
    private String CHECKSUMHASH;
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
