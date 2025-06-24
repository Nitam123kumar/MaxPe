package com.vuvrecharge.modules.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.model.AddData;
import com.vuvrecharge.modules.model.BankData;
import com.vuvrecharge.modules.model.PayData;

import java.lang.reflect.Type;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.receiverid)
    TextView receiverid;
    @BindView(R.id.transaction_id)
    TextView transaction_id;
    @BindView(R.id.date_time)
    TextView date_time;

    PayData payData;
    AddData addData;
    BankData bankData;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        title.setText("Payment Details");

        Intent intent = getIntent();
        String from = intent.getStringExtra("from");

        Gson gson = new Gson();
        Type type = new TypeToken<AddData>() {}.getType();
        Type type_ = new TypeToken<PayData>() {}.getType();
        Type type_bankData = new TypeToken<BankData>() {}.getType();

        if (from.equals("pay")) {
            payData = gson.fromJson(getIntent().getStringExtra("paymentdata"), type_);
            if (payData != null) {
                amount.setText("\u20b9" + payData.getAmount().trim());
                name.setText(payData.getName().trim());
                receiverid.setText("Natty India Wallet linked to " + payData.getMobile().trim());
                transaction_id.setText("Transaction ID: " + payData.getOrderid());

                long date_t = Long.parseLong(payData.getTime().trim());
                date_t = date_t * 1000;
                Date date = new Date(date_t);
                try {
                    String dateFormatNew = "N/A";
                    dateFormatNew = BaseMethod.dateFormatNew.format(date);
                    date_time.setText(dateFormatNew);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            message.setText("Payment Successfully");
        }
        else if (from.equals("bank")) {
            message.setText("Requested Successfully");
            bankData = gson.fromJson(getIntent().getStringExtra("paymentdata"), type_bankData);
            if (bankData != null) {
                amount.setText("\u20b9" + bankData.getAmount().trim());
                name.setText("Available Merchant Balance : \u20b9" + mDatabase.getUserData().getEarnings());
                receiverid.setText(bankData.getRemarks());
                transaction_id.setText("Transaction ID: " + bankData.getOrderid());
                long date_t = Long.parseLong(bankData.getDate_time().trim());
                date_t = date_t * 1000;
                Date date = new Date(date_t);
                try {
                    String dateFormatNew = "N/A";
                    dateFormatNew = BaseMethod.dateFormatNew.format(date);
                    date_time.setText(dateFormatNew);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (from.equals("wallet")) {
            message.setText("Transfer Successfully");
            bankData = gson.fromJson(getIntent().getStringExtra("paymentdata"), type_bankData);
            if (bankData != null) {
                amount.setText("\u20b9" + bankData.getAmount().trim());
                name.setText("Available Merchant Balance : \u20b9" + mDatabase.getUserData().getEarnings());
                receiverid.setText(bankData.getRemarks());
                transaction_id.setText("Transaction ID: " + bankData.getOrderid());
                long date_t = Long.parseLong(bankData.getDate_time().trim());
                date_t = date_t * 1000;
                Date date = new Date(date_t);
                try {
                    String dateFormatNew = "N/A";
                    dateFormatNew = BaseMethod.dateFormatNew.format(date);
                    date_time.setText(dateFormatNew);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            message.setText("Added Successfully");
            addData = gson.fromJson(getIntent().getStringExtra("paymentdata"), type);
            if (addData != null) {
                amount.setText("\u20b9" + addData.getAmount().trim());
                name.setText("Available Wallet : \u20b9" + mDatabase.getUserData().getEarnings());
                receiverid.setText("Bank transaction id " + addData.getBanktxnid().trim());
                transaction_id.setText("Transaction ID: " + addData.getOrderid());
                long date_t = Long.parseLong(addData.getTime().trim());
                date_t = date_t * 1000;
                Date date = new Date(date_t);
                try {
                    String dateFormatNew = "N/A";
                    dateFormatNew = BaseMethod.dateFormatNew.format(date);
                    date_time.setText(dateFormatNew);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "payment");
    }


    @Override
    public void onClick(@NonNull View v) {
        switch(v.getId()){
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }
}
