package com.vuvrecharge.modules.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommissionNewActivity extends BaseActivity implements DefaultView,View.OnClickListener {

    private DefaultPresenter mDefaultPresenter;

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.today_total)
    TextView today_total;
    @BindView(R.id.today_count)
    TextView today_count;
    @BindView(R.id.yesterday_total)
    TextView yesterday_total;
    @BindView(R.id.yesterday_count)
    TextView yesterday_count;
    @BindView(R.id.this_month_count)
    TextView this_month_count;
    @BindView(R.id.this_month_total)
    TextView this_month_total;
    @BindView(R.id.last_month)
    TextView last_month;
    @BindView(R.id.last_month_count)
    TextView last_month_count;
    @BindView(R.id.total_commission)
    TextView total_commission;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.retry)
    TextView retry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);
        ButterKnife.bind(this);
        title.setText("Recharge & Commission");
        mDefaultPresenter = new DefaultPresenter(this);
        mDefaultPresenter.commissionReport(device_id);
        mToolbar.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "commissionNew");
    }

    @Override
    public void onSuccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);
            printLog(jsonObject.toString());
            today_total.setText("\u20b9" + jsonObject.getString("today_total"));
            today_count.setText("(" + jsonObject.getString("today_count") + ")");
            yesterday_total.setText("\u20b9" + jsonObject.getString("yesterday_total"));
            yesterday_count.setText("(" + jsonObject.getString("yesterday_count") + ")");
            this_month_total.setText("\u20b9" + jsonObject.getString("this_month_total"));
            this_month_count.setText("(" + jsonObject.getString("this_month_count") + ")");
            last_month.setText("\u20b9" + jsonObject.getString("last_month_total"));
            last_month_count.setText("(" + jsonObject.getString("last_month_count") + ")");
            total_commission.setText("\u20b9" + jsonObject.getString("commission_earned"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(String userData, String second_message) {

    }

    @Override
    public void onSuccessOther(String data) {

    }

    @Override
    public void onSuccessOther(String data, String data_other) {

    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(error);
            submit.setVisibility(View.VISIBLE);
        } else {
            showError(error);
        }
    }

    @Override
    public void onShowDialog(String message) {
        if (bottomSheet != null) {
            showLoading(loading_dialog);
        } else {
            showLoading(loading);
        }
    }

    @Override
    public void onHideDialog() {
        if (bottomSheet != null) {
            hideLoading(loading_dialog);
            submit.setVisibility(View.VISIBLE);
        } else {
            hideLoading(loading);
        }
    }

    @Override
    public void onShowToast(String message) {
        if (bottomSheet != null) {
            showToast(message);
        } else {
            showToast(message);
        }
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_layout:
                onBackPressed();
                break;
        }
    }
}
