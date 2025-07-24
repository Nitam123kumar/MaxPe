package com.vuvrecharge.modules.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.fragments.DepositFragment;
import com.vuvrecharge.modules.fragments.MaxPointFragment;
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
    @BindView(R.id.viewMaxPoints)
    View viewMaxPoints;
    @BindView(R.id.maxPointsTxt)
    TextView maxPointsTxt;

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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mToolbar.setOnClickListener(this);
        title.setText("History");
        mDefaultPresenter = new DefaultPresenter(this);
        setStatusBarGradiant(this);
        initializeEventsList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "statement");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.main_wallet_shape);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }


    private void initializeEventsList() {
        addMore.setVisibility(View.GONE);

        String showFragment = getIntent().getStringExtra("statementsActivity");
        String maxPointsData = getIntent().getStringExtra("maxPointsActivity");
        if (showFragment != null && !showFragment.isEmpty()) {

            setFragment(new RechargeHistoryFragment());
            viewStatements.setBackgroundResource(R.drawable.statements_select_bg_shape);
            statementsView.setTextColor(Color.WHITE);
            viewMaxPoints.setBackgroundResource(R.drawable.null_shape);
            maxPointsTxt.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewReports.setBackgroundResource(R.drawable.null_shape);
            reportsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewDeposit.setBackgroundResource(R.drawable.null_shape);
            depositView.setTextColor(getResources().getColor(R.color.colorBlackU));
            statementsView.setText(Html.fromHtml("<b>Recharge</b>"));
            reportsView.setText(Html.fromHtml("Wallet"));
            depositView.setText(Html.fromHtml("Deposit"));
            maxPointsTxt.setText(Html.fromHtml("Max Point"));

        } else if (maxPointsData !=null && !maxPointsData.isEmpty()) {
            setFragment(new MaxPointFragment());
            viewMaxPoints.setBackgroundResource(R.drawable.statements_select_bg_shape);
            maxPointsTxt.setTextColor(Color.WHITE);
            viewStatements.setBackgroundResource(R.drawable.null_shape);
            statementsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewReports.setBackgroundResource(R.drawable.null_shape);
            reportsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewDeposit.setBackgroundResource(R.drawable.null_shape);
            depositView.setTextColor(getResources().getColor(R.color.colorBlackU));
            statementsView.setText(Html.fromHtml("Recharge"));
            reportsView.setText(Html.fromHtml("Wallet"));
            depositView.setText(Html.fromHtml("Deposit"));
            maxPointsTxt.setText(Html.fromHtml("<b>Max Point</b>"));

        } else {

            setFragment(new DepositFragment());
            viewDeposit.setBackgroundResource(R.drawable.statements_select_bg_shape);
            statementsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewReports.setBackgroundResource(R.drawable.null_shape);
            reportsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewMaxPoints.setBackgroundResource(R.drawable.null_shape);
            viewStatements.setBackgroundResource(R.drawable.null_shape);
            maxPointsTxt.setTextColor(getResources().getColor(R.color.colorBlackU));
            depositView.setTextColor(Color.WHITE);
            statementsView.setText(Html.fromHtml("Recharge"));
            reportsView.setText(Html.fromHtml("Wallet"));
            depositView.setText(Html.fromHtml("<b>Deposit</b>"));
            maxPointsTxt.setText(Html.fromHtml("Max Point"));
        }


        viewStatements.setOnClickListener(v -> {
            setFragment(new RechargeHistoryFragment());
            viewStatements.setBackgroundResource(R.drawable.statements_select_bg_shape);
            statementsView.setTextColor(Color.WHITE);
            viewReports.setBackgroundResource(R.drawable.null_shape);
            reportsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewDeposit.setBackgroundResource(R.drawable.null_shape);
            depositView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewMaxPoints.setBackgroundResource(R.drawable.null_shape);
            maxPointsTxt.setTextColor(getResources().getColor(R.color.colorBlackU));
            statementsView.setText(Html.fromHtml("<b>Recharge</b>"));
            reportsView.setText(Html.fromHtml("Wallet"));
            depositView.setText(Html.fromHtml("Deposit"));
            maxPointsTxt.setText(Html.fromHtml("Max Point"));
        });

        viewReports.setOnClickListener(v -> {
            setFragment(new WalletFragment());
            viewReports.setBackgroundResource(R.drawable.statements_select_bg_shape);
            reportsView.setTextColor(Color.WHITE);
            viewStatements.setBackgroundResource(R.drawable.null_shape);
            statementsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewDeposit.setBackgroundResource(R.drawable.null_shape);
            depositView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewMaxPoints.setBackgroundResource(R.drawable.null_shape);
            maxPointsTxt.setTextColor(getResources().getColor(R.color.colorBlackU));
            statementsView.setText(Html.fromHtml("Recharge"));
            depositView.setText(Html.fromHtml("Deposit"));
            reportsView.setText(Html.fromHtml("<b>Wallet</b>"));
            maxPointsTxt.setText(Html.fromHtml("Max Point"));
        });

        viewDeposit.setOnClickListener(v -> {
            setFragment(new DepositFragment());
            viewDeposit.setBackgroundResource(R.drawable.statements_select_bg_shape);
            depositView.setTextColor(Color.WHITE);
            viewStatements.setBackgroundResource(R.drawable.null_shape);
            statementsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewMaxPoints.setBackgroundResource(R.drawable.null_shape);
            maxPointsTxt.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewReports.setBackgroundResource(R.drawable.null_shape);
            reportsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            statementsView.setText(Html.fromHtml("Recharge"));
            reportsView.setText(Html.fromHtml("Wallet"));
            depositView.setText(Html.fromHtml("<b>Deposit</b>"));
            maxPointsTxt.setText(Html.fromHtml("Max Point"));
        });
        viewMaxPoints.setOnClickListener(v -> {
            setFragment(new MaxPointFragment());
            viewMaxPoints.setBackgroundResource(R.drawable.statements_select_bg_shape);
            maxPointsTxt.setTextColor(Color.WHITE);
            viewDeposit.setBackgroundResource(R.drawable.null_shape);
            depositView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewStatements.setBackgroundResource(R.drawable.null_shape);
            statementsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewReports.setBackgroundResource(R.drawable.null_shape);
            reportsView.setTextColor(getResources().getColor(R.color.colorBlackU));
            statementsView.setText(Html.fromHtml("Recharge"));
            reportsView.setText(Html.fromHtml("Wallet"));
            depositView.setText(Html.fromHtml("Deposit"));
            maxPointsTxt.setText(Html.fromHtml("<b>Max Point</b>"));
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
