package com.vuvrecharge.modules.model;

import java.util.List;

public class MycommisonPackageData {



    private String type;

    private List<CommissionTypeData> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CommissionTypeData> getData() {
        return data;
    }

    public void setData(List<CommissionTypeData> data) {
        this.data = data;
    }
}
