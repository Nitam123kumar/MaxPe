package com.vuvrecharge.modules.model;

public class CarInsuranceModel {

    private String percent;
    private String redirect_url;
    private String redirect_type;

    public void setPercent(String percent){
        this.percent = percent;
    }

    public String getPercent(){
        return percent;
    }

    public void setRedirect_url(String redirect_url){
        this.redirect_url = redirect_url;
    }

    public String getRedirect_url(){
        return redirect_url;
    }

    public void setRedirect_type(String redirect_type){
        this.redirect_type = redirect_type;
    }

    public String getRedirect_type(){
        return redirect_type;
    }

}
