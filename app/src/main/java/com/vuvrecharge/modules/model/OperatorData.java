package com.vuvrecharge.modules.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OperatorData implements Serializable {


    /**
     * id : 1
     * name : AIRTEL
     */
    @SerializedName("operator_img")
    private String operator_img;

    @SerializedName("operator_dunmy_img")
    private String operator_dunmy_img;

    private String id;
    private String name;
    private String logo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }



    public void setOperator_img(String operator_img) {
        this.operator_img = operator_img;
    }
    public String getOperator_img() {
        return operator_img;
    }
    public void setOperator_dunmy_img(String operator_dunmy_img) {
        this.operator_dunmy_img = operator_dunmy_img;
    }
    public String getOperator_dunmy_img() {
        return operator_dunmy_img;
    }


}
