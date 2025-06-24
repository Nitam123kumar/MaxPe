package com.vuvrecharge.modules.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.fragments.DepositFragment;
import com.vuvrecharge.modules.fragments.RechargeHistoryFragment;
import com.vuvrecharge.modules.fragments.WalletFragment;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.presenter.OnFragmentListener;
import com.vuvrecharge.modules.view.DefaultView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatementsActivity extends BaseActivity implements DefaultView, OnFragmentListener,View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.statementsView)
    TextView statementsView;
    @BindView(R.id.reportsView)
    TextView reportsView;
    @BindView(R.id.depositView)
    TextView depositView;

    @BindView(R.id.viewStatements)
    View viewStatements;
    @BindView(R.id.viewDeposit)
    View viewDeposit;

    @BindView(R.id.viewReports)
    View viewReports;

    @BindView(R.id.addMore)
    ImageView addMore;

    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    private DefaultPresenter mDefaultPresenter;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_statements);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        title.setText("History");
        mDefaultPresenter = new DefaultPresenter(this);
        initializeEventsList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "statement");
    }

    private void initializeEventsList() {
        addMore.setVisibility(View.GONE);

        setFragment(new RechargeHistoryFragment());
        viewStatements.setBackgroundResource(R.drawable.tab_background_drawable);
        statementsView.setTextColor(Color.WHITE);
        viewReports.setBackgroundResource(R.drawable.tab_background_drawable_selected);
        reportsView.setTextColor(getResources().getColor(R.color.colorPrimaryB));
        viewDeposit.setBackgroundResource(R.drawable.tab_background_drawable_selected);
        depositView.setTextColor(getResources().getColor(R.color.colorPrimaryB));
        statementsView.setText(Html.fromHtml("<b>Recharge History</b>"));
        reportsView.setText(Html.fromHtml("Wallet History"));
        depositView.setText(Html.fromHtml("Deposit History"));

        viewStatements.setOnClickListener(v -> {
            setFragment(new RechargeHistoryFragment());
            viewStatements.setBackgroundResource(R.drawable.tab_background_drawable);
            statementsView.setTextColor(Color.WHITE);
            viewReports.setBackgroundResource(R.drawable.tab_background_drawable_selected);
            reportsView.setTextColor(getResources().getColor(R.color.colorPrimaryB));
            viewDeposit.setBackgroundResource(R.drawable.tab_background_drawable_selected);
            depositView.setTextColor(getResources().getColor(R.color.colorPrimaryB));
            statementsView.setText(Html.fromHtml("<b>Recharge History</b>"));
            reportsView.setText(Html.fromHtml("Wallet History"));
            depositView.setText(Html.fromHtml("Deposit History"));
        });

        viewReports.setOnClickListener(v -> {
            setFragment(new WalletFragment());
            viewReports.setBackgroundResource(R.drawable.tab_background_drawable);
            reportsView.setTextColor(Color.WHITE);
            viewStatements.setBackgroundResource(R.drawable.tab_background_drawable_selected);
            statementsView.setTextColor(getResources().getColor(R.color.colorPrimaryB));
            viewDeposit.setBackgroundResource(R.drawable.tab_background_drawable_selected);
            depositView.setTextColor(getResources().getColor(R.color.colorPrimaryB));
            statementsView.setText(Html.fromHtml("Recharge History"));
            depositView.setText(Html.fromHtml("Deposit History"));
            reportsView.setText(Html.fromHtml("<b>Wallet History</b>"));
        });

        viewDeposit.setOnClickListener(v -> {
            setFragment(new DepositFragment());
            viewDeposit.setBackgroundResource(R.drawable.tab_background_drawable);
            depositView.setTextColor(Color.WHITE);
            viewStatements.setBackgroundResource(R.drawable.tab_background_drawable_selected);
            statementsView.setTextColor(getResources().getColor(R.color.colorPrimaryB));
            viewReports.setBackgroundResource(R.drawable.tab_background_drawable_selected);
            reportsView.setTextColor(getResources().getColor(R.color.colorPrimaryB));
            statementsView.setText(Html.fromHtml("Recharge History"));
            reportsView.setText(Html.fromHtml("Wallet History"));
            depositView.setText(Html.fromHtml("<b>Deposit History</b>"));
        });

    }
    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(bottomSheet, error);
        }
        showError(error);
    }


    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void onSuccess(String message, String second_message) {
    }

    @Override
    public void onSuccessOther(String data) {

    }

    @Override
    public void onSuccessOther(String data, String data_other) {
        Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    @Override
    public void onShowDialog(String message) {
    }

    @Override
    public void onHideDialog() {
    }

    @Override
    public void onShowToast(String message) {
        if (bottomSheet != null) {
            showToast(bottomSheet, message);
        } else {
            showToast(message);
        }
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
    }

    private void setFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(),fragment)
                .commit();
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
