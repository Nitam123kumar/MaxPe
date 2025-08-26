package com.vuvrecharge.modules.activities;

import static com.google.android.material.internal.ViewUtils.showKeyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.utils.LocaleHelper;

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

    @BindView(R.id.call_layout)
    ConstraintLayout call_layout;
    @BindView(R.id.whatsapp_layout)
    ConstraintLayout whatsapp_layout;
    String supportNumber;
    String whatsappNumber;

    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }

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
        changeStatusBarColorLoginPage();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setLayout(no_internet, retry, "register");
        title.setTextColor(Color.WHITE);
        title.setText("New Registration");
        mDefaultView = this;
        mDefaultPresenter = new DefaultPresenter(mDefaultView);
        mDefaultPresenter.supportContacts(device_id);

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

        call_layout.setOnClickListener(v -> {
            if (selectCall()) {
                makeACall(supportNumber);
            }
        });

        whatsapp_layout.setOnClickListener(v -> {

            String url = "https://api.whatsapp.com/send/?phone=91" + whatsappNumber + "&text=" + "Hello MaxPe Support";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            Intent chooser = Intent.createChooser(i, "Chat with...");
            startActivity(chooser);
        });


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
                "151617",
                Objects.requireNonNull(enter_referral_id.getText()).toString() + "");
    }

    @Override
    public void onSuccess(String message) {
        title.setText(R.string.verify_otp);
        otpVerifyDialog();


    }

    @SuppressLint({"SetTextI18n", "RestrictedApi"})
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
                    showErrorLoginPage(bottomSheet, "Please enter otp");
                    return;
                }

                hideKeyBoard(binding_.otpView);
                strSponser = Objects.requireNonNull(enter_referral_id.getText()).toString();
                mDefaultPresenter.otpValidate(Objects.requireNonNull(email.getText()).toString() + "",
                        Objects.requireNonNull(full_name.getText()).toString() + "",
                        enter_mobile_no.getText().toString() + "",
                        "",
                        otpView + "", device_id, strSponser);
            });
            if (dialog != null) {
                dialog.setOnShowListener(d -> {
                    binding_.otpView.requestFocus();

                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                    binding_.otpView.postDelayed(() -> {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(binding_.otpView, InputMethodManager.SHOW_IMPLICIT);
                    }, 200);
                });

                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void otpVerifyDialogCustom() {
        try {
            dialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            OtpVerifyDialogBinding binding_ = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.otp_verify_dialog, null, false);
            dialog.setContentView(binding_.getRoot());
            dialog.setCancelable(false);

            submit = binding_.goForOtpVerify;
            loading_dialog = binding_.loading;

            changeStatusBarColor(dialog);
            binding_.mobileMessage.setText("Please enter the code sent to +91-" + enter_mobile_no.getText().toString());

            bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }


            EditText[] otpFields = new EditText[6];
            otpFields[0] = binding_.etOtp1;
            otpFields[1] = binding_.etOtp2;
            otpFields[2] = binding_.etOtp3;
            otpFields[3] = binding_.etOtp4;
            otpFields[4] = binding_.etOtp5;
            otpFields[5] = binding_.etOtp6;


            for (int i = 0; i < otpFields.length; i++) {
                final int index = i;

                otpFields[i].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 1 && index < otpFields.length - 1) {
                            otpFields[index + 1].requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

                otpFields[i].setOnKeyListener((v, keyCode, event) -> {
                    if (event.getAction() == KeyEvent.ACTION_DOWN
                            && keyCode == KeyEvent.KEYCODE_DEL
                            && otpFields[index].getText().toString().isEmpty()
                            && index > 0) {
                        otpFields[index - 1].requestFocus();
                    }
                    return false;
                });
            }

            dialog.setOnShowListener(d -> {
                otpFields[0].requestFocus();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }
                otpFields[0].postDelayed(() -> {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(otpFields[0], InputMethodManager.SHOW_IMPLICIT);
                }, 200);
            });

            binding_.goForOtpVerify.setOnClickListener(v -> {
                StringBuilder otp = new StringBuilder();
                for (EditText e : otpFields) {
                    otp.append(e.getText().toString());
                }
                String finalOtp = otp.toString();

                if (TextUtils.isEmpty(finalOtp.trim()) || finalOtp.length() < 6) {
                    showErrorLoginPage(bottomSheet, "Please enter full 6-digit OTP");
                    return;
                }

                hideKeyBoard(otpFields[0]);
                strSponser = Objects.requireNonNull(enter_referral_id.getText()).toString();
                mDefaultPresenter.otpValidate(
                        Objects.requireNonNull(email.getText()).toString(),
                        Objects.requireNonNull(full_name.getText()).toString(),
                        enter_mobile_no.getText().toString(),
                        "",
                        finalOtp,
                        device_id,
                        strSponser
                );
            });

            dialog.show();

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
        try {
            JSONObject supportContacts = new JSONObject(data);
            supportNumber = supportContacts.getString("support_number");
            whatsappNumber = supportContacts.getString("whatsapp_number");
            Log.d("support_number", supportNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showErrorLoginPage(bottomSheet, error);
            submit.setVisibility(View.VISIBLE);
        } else {
            showErrorLoginPage(error);
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