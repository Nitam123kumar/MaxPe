package com.vuvrecharge.modules.model;

import org.json.JSONObject;

public class DthPlanData {

    private JSONObject rs;
    private String desc;
    private String plan_name;
    private String last_update;

    public JSONObject getRs() {
        return rs;
    }

    public void setRs(JSONObject rs) {
        this.rs = rs;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

}
