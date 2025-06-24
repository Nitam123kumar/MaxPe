package com.vuvrecharge.modules.model;

public class BillInfo {
    private String customerName = "";
    private String billNumber = "";
    private String billDate = "";
    private String dueDate = "";
    private String billAmount = "";
    private String fetchType = "";
    private String dealerName = "";
    private String requestId = "";
    private String walletBalance = "";
    public BillInfo() {
    }

    public String getWalletBalance() {
        return (walletBalance == null || walletBalance.equals("")) ? "" : walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getCustomerName() {
        return (customerName == null || customerName.equals("")) ? "" : customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = (customerName == null || customerName.equals("")) ? "" : customerName;
    }

    public String getBillNumber() {
        return (billNumber == null || billNumber.equals("")) ? "" : billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = (billNumber == null || billNumber.equals("")) ? "" : billNumber;
    }

    public String getBillDate() {
        return (billDate == null || billDate.equals("")) ? "" : billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = (billDate == null || billDate.equals("")) ? "" : billDate;
    }

    public String getDueDate() {
        return (dueDate == null || dueDate.equals("")) ? "" : dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = (dueDate == null || dueDate.equals("")) ? "" : dueDate;
    }

    public String getBillAmount() {
        return (billAmount == null || billAmount.equals("")) ? "" : billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = (billAmount == null || billAmount.equals("")) ? "" : billAmount;
    }

    public String getFetchType() {
        return (fetchType == null || fetchType.equals("")) ? "" : fetchType;
    }

    public void setFetchType(String fetchType) {
        this.fetchType = (fetchType == null || fetchType.equals("")) ? "" : fetchType;
    }

    public String getDealerName() {
        return (dealerName == null || dealerName.equals("")) ? "": dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = (dealerName == null || dealerName.equals("")) ? "": dealerName;
    }

    public String getRequestId() {
        return (requestId == null || requestId.equals("")) ? "" : requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = (requestId == null || requestId.equals("")) ? "" : requestId;
    }
}
