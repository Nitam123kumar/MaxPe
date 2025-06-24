package com.vuvrecharge.modules.model;

import java.io.Serializable;

public class ReportsData implements Serializable {

    private String username;
    private String name;
    private String user_type;
    private String number;
    private String amount;
    private String final_charge;
    private String status;
    private String operator_name;
    private String order_id;
    private String operator_ref;
    private String request_time;
    private String logo;
    private String display_message="";
    private String complaint_count="";
    private String is_bbps;

    public String getIs_bbps() {
        return is_bbps;
    }

    public void setIs_bbps(String is_bbps) {
        this.is_bbps = is_bbps;
    }

    public String getRecharge_type() {
        return recharge_type;
    }

    public void setRecharge_type(String recharge_type) {
        this.recharge_type = recharge_type;
    }

    private String recharge_type="";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFinal_charge() {
        return final_charge;
    }

    public void setFinal_charge(String final_charge) {
        this.final_charge = final_charge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOperator_ref() {
        return operator_ref;
    }

    public void setOperator_ref(String operator_ref) {
        this.operator_ref = operator_ref;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getComplaint_count() {
        return complaint_count;
    }

    public void setComplaint_count(String complaint_count) {
        this.complaint_count = complaint_count;
    }

    public String getDisplay_message() {
        return display_message;
    }

    public void setDisplay_message(String display_message) {
        this.display_message = display_message;
    }
}
