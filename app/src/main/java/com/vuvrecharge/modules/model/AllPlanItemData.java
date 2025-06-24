package com.vuvrecharge.modules.model;

import java.util.List;

public class AllPlanItemData {

    private String name;

    List<PlanItemData> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlanItemData> getList() {
        return list;
    }

    public void setList(List<PlanItemData> list) {
        this.list = list;
    }

}
