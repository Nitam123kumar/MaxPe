package com.vuvrecharge.modules.model;

import java.io.Serializable;

public class BillFetch implements Serializable {
    String key = "";
    String value = "";

    public BillFetch() {
    }

    public BillFetch(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
