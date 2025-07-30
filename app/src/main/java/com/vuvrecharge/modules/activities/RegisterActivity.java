package com.vuvrecharge.modules.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.databinding.OtpVerifyDialogBinding;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.full_name)
    TextInputEditText full_name;
    @BindView(R.id.enter_mobile_no)
    TextInputEditText enter_mobile_no;
    @BindView(R.id.enter_password)
    TextInputEditText enter_password;
    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.enter_referral_id)
    TextInputEditText enter_referral_id;
    @BindView(R.id.goForLogin)
    TextView goForLogin;
    DefaultPresenter mDefaultPresenter;
    DefaultView mDefaultView;
    String strSponser;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryB));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("");
        }
        setLayout(no_internet, retry, "register");
        title.setTextColor(Color.WHITE);
        title.setText("New Registration");
        mDefaultView = this;
        mDefaultPresenter = new DefaultPresenter(mDefaultView);

        InstallReferrerClient mReferrerClient = InstallReferrerClient.newBuilder(this).build();

        mReferrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        try {

                            ReferrerDetails response = mReferrerClient.getInstallReferrer();
                            String referrer = response.getInstallReferrer();

                            if (referrer.length() > 12) {
                                referrer = "";
                            }
                            saveReferrerCode(referrer);
                            if (!getReferrerCode().equals("")) {
                                enter_referral_id.setText(getReferrerCode());

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        full_name.requestFocus();
                                    }
                                }, 200);
                            }
                            mReferrerClient.endConnection();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app
                        printLog("FEATURE_NOT_SUPPORTED " + responseCode);
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection could not be established
                        printLog("SERVICE_UNAVAILABLE " + responseCode);

                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.

            }
        });

        register.setOnClickListener(this);
        goForLogin.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyBoard(enter_mobile_no);
    }

    @Override
    public void onClick(@NonNull View view) {

        switch (view.getId()) {
            case R.id.register:
                validateCredentials();
                break;
            case R.id.goForLogin:
                onBackPressed();
                break;
            case R.id.toolbar_layout:
                onBackPressed();
                break;
        }

    }

    private void validateCredentials() {
        mDefaultPresenter.doRegister(
                Objects.requireNonNull(email.getText()).toString() + "",
                Objects.requireNonNull(full_name.getText()).toString() + "",
                Objects.requireNonNull(enter_mobile_no.getText()).toString() + "",
                Objects.requireNonNull(enter_password.getText()).toString() + "",
                Objects.requireNonNull(enter_referral_id.getText()).toString() + "");
    }

    @Override
    public void onSuccess(String message) {
        title.setText("Verify OTP");
        otpVerifyDialog();
    }

    @SuppressLint("SetTextI18n")
    public void otpVerifyDialog() {
        try {
            dialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            OtpVerifyDialogBinding binding_ = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.otp_verify_dialog, null, false);
            dialog.setContentView(binding_.getRoot());
            dialog.setCancelable(false);

            binding_.otpView.requestFocus();

            submit = binding_.goForOtpVerify;
            loading_dialog = binding_.loading;

            changeStatusBarColor(dialog);
            binding_.mobileMessage.setText("Please enter the code sent to +91-" + enter_mobile_no.getText().toString() + "");
            bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
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
                        if (dialog != null) {
                            dialog.dismiss();
                            dialog = null;
                            bottomSheet = null;
                        }
                    }, 200);
                }
            });

            binding_.goForOtpVerify.setOnClickListener(v -> {

                String otpView = Objects.requireNonNull(binding_.otpView.getText()).toString();
                if (TextUtils.isEmpty(otpView.trim())) {
                    showError1(bottomSheet, "Please enter otp");
                    return;
                }

                hideKeyBoard(binding_.otpView);
                strSponser = Objects.requireNonNull(enter_referral_id.getText()).toString();
                mDefaultPresenter.otpValidate(Objects.requireNonNull(email.getText()).toString() + "",
                        Objects.requireNonNull(full_name.getText()).toString() + "",
                        enter_mobile_no.getText().toString() + "",
                        Objects.requireNonNull(enter_password.getText()).toString() + "",
                        otpView + "", device_id, strSponser);

            });
            if (dialog != null) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSuccess(String message, String second_message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            mDatabase.setToken(jsonObject.getString("token"));
            mDatabase.setUserLogin(true);
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        } catch (Exception e) {
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