package com.vuvrecharge.modules.model;

import java.util.List;

public class DownlinePackageData {
    private String type;
    private String memberType;
    private String btnText;

    private List<DownlineineerData> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DownlineineerData> getData() {
        return data;
    }

    public void setData(List<DownlineineerData> data) {
        this.data = data;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }
}
