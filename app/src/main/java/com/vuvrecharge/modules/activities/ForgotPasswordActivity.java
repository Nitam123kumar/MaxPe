package com.vuvrecharge.modules.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.databinding.OtpVerifyPasswordDialogBinding;
import com.vuvrecharge.databinding.RegisterBgBinding;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.forgot_password)
    TextView forgot_password;
    @BindView(R.id.mobile_number)
    TextInputEditText mobile_number;
    @BindView(R.id.goForLogin)
    TextView goForLogin;

    @BindView(R.id.loading)
    LinearLayout loading;

    DefaultPresenter mDefaultPresenter;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mToolbar.setOnClickListener(this);
//        if (getSupportActionBar() != null){
//            mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryB));
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            mToolbar.setNavigationOnClickListener(v -> onBackPressed());
//            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            setTitle("");
//        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        title.setTextColor(Color.WHITE);
        title.setText("Welcome Back");
        mDefaultPresenter = new DefaultPresenter(this);
        forgot_password.setOnClickListener(this);
        goForLogin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "forgot");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.forgot_password:
                validateCredentials();
                break;

            case R.id.goForLogin:
                onBackPressed();
                break;
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }

    private void validateCredentials() {
        if (TextUtils.isEmpty(mobile_number.getText().toString())) {
            onError("Please enter register mobile number");
            return;
        }
        mDefaultPresenter.forgotPasswordOtp(mobile_number.getText().toString() + "");
    }

    @Override
    public void onSuccess(String message) {
        if (dialog != null) {
            dialog.dismiss();
            dialog.cancel();
        }
        try {
            RegisterBgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.register_bg, null, false);
            Dialog dialog = new Dialog(this);
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(false);
            binding.message.setText(message);
            hideKeyBoard(mobile_number);
            binding.goForFeedback.setOnClickListener(view -> {
                dialog.dismiss();
                dialog.cancel();
                finish();
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String message, String second_message) {

    }

    @Override
    public void onSuccessOther(String data) {
        try {
            otpVerifyDialog(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessOther(String data, String data_other) {

    }

    public void otpVerifyDialog(String mobile) {
        try{
            dialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            OtpVerifyPasswordDialogBinding binding_ = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.otp_verify_password_dialog, null, false);
            dialog.setContentView(binding_.getRoot());
            dialog.setCancelable(false);

            binding_.newPassword.requestFocus();

            submit = binding_.goForOtpVerify;
            loading_dialog = binding_.loading;

            changeStatusBarColor(dialog);
            binding_.mobileMessage.setText("Please enter the code sent to +91-" + mobile + "");
            bottomSheet = dialog.findViewById(com.androidadvance.topsnackbar.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }

            binding_.dialogImage.setOnClickListener(v -> {
                if (binding_.goForOtpVerify.getVisibility() == View.VISIBLE) {
                    hideKeyBoard(binding_.hideKeyboard);
                    new Handler().postDelayed(() -> {
                        dialog.dismiss();
//                    dialog = null;
//                    bottomSheet = null;
                    }, 200);
                }
            });

            binding_.goForOtpVerify.setOnClickListener(v -> {
                String otpView = binding_.otpView.getText().toString();
                String newPassword = binding_.newPassword.getText().toString();
                String reNewPassword = binding_.reNewPassword.getText().toString();
                if (TextUtils.isEmpty(newPassword.trim())) {
                    showError1(bottomSheet, "Please enter new password");
                    return;
                }
                if (!newPassword.trim().equals(reNewPassword.trim())) {
                    showError1(bottomSheet, "Confirm password doesn't matched");
                    return;
                }

                if (TextUtils.isEmpty(otpView.trim())) {
                    showError1(bottomSheet, "Please enter otp");
                    return;
                }

                hideKeyBoard(binding_.otpView);
                mDefaultPresenter.forgotPassword(mobile + "",
                        otpView + "",
                        newPassword + "");

            });
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyBoard(mobile_number);
    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError1(bottomSheet, error);
            submit.setVisibility(View.VISIBLE);
        } else {
            showError1(error);
        }
    }

    @Override
    public void onShowDialog(String message) {
        if (bottomSheet != null) {
            showLoading(loading_dialog);
            submit.setVisibility(View.GONE);
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
            showToast(bottomSheet, message);
        } else {
            showToast(message);
        }
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }

}