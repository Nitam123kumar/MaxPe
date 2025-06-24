package com.vuvrecharge.modules.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.model.ReportsData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

public class AfterDepositActivity extends BaseActivity implements DefaultView,View.OnClickListener {

    DefaultPresenter defaultPresenter;
    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    ReportsData mReportsData;
    @BindView(R.id.imgCopy)
    ImageView rlCopy;
    @BindView(R.id.txtRupees)
    TextView tvAmount;
    @BindView(R.id.txtOrderId)
    TextView order_id;
    @BindView(R.id.btnComplete)
    TextView btnLogin;
    @BindView(R.id.txtWalletBalanceValue)
    TextView walletBalance;
    @BindView(R.id.imgGif)
    GifImageView operator_img;
    @BindView(R.id.loading)
    LinearLayout loading;
    String orderid;
    Handler handler;
    Runnable runnable;
    String payment_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_deposit);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        Intent intent=getIntent();
        orderid = intent.getStringExtra("order_id");

        defaultPresenter = new DefaultPresenter(this);

        defaultPresenter.orderDetails(device_id, orderid);

        handler = new Handler();
        mReportsData = new ReportsData();
        pendingstatus();
        btnLogin.setOnClickListener(v -> {
            Intent intent1 = new Intent(AfterDepositActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        });
        rlCopy.setOnClickListener(v -> copyTextMain(ordr_id));
    }


    void pendingstatus() {
        defaultPresenter.orderDetailspending(device_id + "", orderid);
    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(bottomSheet, error);
            submit.setVisibility(View.VISIBLE);
        } else {
            showError(error);
        }
    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void setData(ReportsData mReportsData) {
        try {
            if (mReportsData.getStatus().equals("pending")) {
                handler.postDelayed(runnable, 10000);
            }
            if (mReportsData.getStatus().equals("SUCCESS")) {
                handler.postDelayed(runnable, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    String ordr_id = "";
    @Override
    public void onSuccess(String data, String data_other) {
        Gson gson = new Gson();
        printLog(data + "");
        Type type_ = new TypeToken<ReportsData>() {
        }.getType();
        mReportsData = gson.fromJson(data, type_);
        setData(mReportsData);

        try {
            JSONObject object = new JSONObject(data);
            ordr_id = object.getString("order_id");
            payment_status = object.getString("payment_status");
            String wallet = object.getString("wallet");
            String amount = object.getString("amount");
            tvAmount.setText("\u20b9" + amount + "/-");
            order_id.setText("Order Id : " + ordr_id);
            walletBalance.setText("\u20b9" + wallet);

            mReportsData.setStatus(payment_status);
            if (mReportsData.getStatus().equals("pending")) {
                handler.postDelayed(runnable, 10000);
            }
            runnable = () -> {
                if (mReportsData.getStatus().equals("pending")) {
                    pendingstatus();
                }
            };

            if (payment_status.toLowerCase().equals("pending")) {
                Glide.with(getBaseContext()).load(R.drawable.animated_pending).into(operator_img);
                title.setText("Transaction Processing...");
                changeStatusBarColorProcess();
                mReportsData.setStatus(payment_status);
                // pendingstatus();
                btnLogin.setBackgroundResource(R.drawable.pending_button);

            } else if (payment_status.toLowerCase().equals("success")) {
                title.setText("Transaction Success");
                changeStatusBarColorGreen();
                btnLogin.setBackgroundResource(R.drawable.green_button);
                Glide.with(getBaseContext()).load(R.drawable.animated_right).into(operator_img);
            } else if (payment_status.toLowerCase().equals("failed")) {
                title.setText("Transaction Failed");
                Glide.with(getBaseContext()).load(R.drawable.animated_wrong).into(operator_img);
                btnLogin.setBackgroundResource(R.drawable.failed_button);
                changeStatusBarColorError();
            } else {
                title.setText("Transaction Refunded");
                btnLogin.setBackgroundResource(R.drawable.failed_button);
                Glide.with(getBaseContext()).load(R.drawable.animated_wrong).into(operator_img);
                changeStatusBarColorError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onSuccessOther(String data) {

    }

    @Override
    public void onSuccessOther(String data, String data_other) {

    }

    @Override
    public void onShowDialog(String message) {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideDialog() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onShowToast(String message) {

    }

    @Override
    public void onPrintLog(String message) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "afterdeposit");
        setData(mReportsData);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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