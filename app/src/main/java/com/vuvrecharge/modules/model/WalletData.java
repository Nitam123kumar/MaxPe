package com.vuvrecharge.modules.model;

public class WalletData {


    /**
     * remaining : 38479.267
     * amount : 189.050
     * txn_amount : 199
     * type : Dr
     * txid : 1591351247275591
     * comment : Payment for Jio [Prepaid] No. 7977591905
     * date_time : 2020-06-05 15:30:47
     */

    private String remaining;
    private String amount;
    private String txn_amount;
    private String type;
    private String txid;
    private String comment;
    private String date_time;

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTxn_amount() {
        return txn_amount;
    }

    public void setTxn_amount(String txn_amount) {
        this.txn_amount = txn_amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
