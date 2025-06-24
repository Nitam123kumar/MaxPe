package com.vuvrecharge.modules.model;

import java.io.Serializable;

public class PaymentModeData implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
