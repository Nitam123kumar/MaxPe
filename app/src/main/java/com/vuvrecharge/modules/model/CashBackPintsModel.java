package com.vuvrecharge.modules.model;

public class CashBackPintsModel {

    private String cashback_points;
    private String max_balance_slab;
    private String txn_balance_count;

    public String getCashback_points() {
        return cashback_points;
    }

    public void setCashback_points(String cashback_points) {
        this.cashback_points = cashback_points;
    }

    public String getMax_balance_slab() {
        return max_balance_slab;
    }

    public void setMax_balance_slab(String max_balance_slab) {
        this.max_balance_slab = max_balance_slab;
    }

    public String getTxn_balance_count() {
        return txn_balance_count;
    }

    public void setTxn_balance_count(String txn_balance_count) {
        this.txn_balance_count = txn_balance_count;
    }


}
