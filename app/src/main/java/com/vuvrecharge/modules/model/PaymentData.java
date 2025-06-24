package com.vuvrecharge.modules.model;

import java.io.Serializable;

public class PaymentData implements Serializable {

    private String bank_name;
    private String ac_no;
    private String ifsc_code;
    private String ac_type;
    private String ac_holder;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAc_no() {
        return ac_no;
    }

    public void setAc_no(String ac_no) {
        this.ac_no = ac_no;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getAc_type() {
        return ac_type;
    }

    public void setAc_type(String ac_type) {
        this.ac_type = ac_type;
    }

    public String getAc_holder() {
        return ac_holder;
    }

    public void setAc_holder(String ac_holder) {
        this.ac_holder = ac_holder;
    }


}
