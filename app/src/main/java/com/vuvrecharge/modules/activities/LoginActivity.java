/*
 *
 *  * Copyright (C) 2018 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.vuvrecharge.modules.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.databinding.ActivityLoginBinding;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.UserPreferences;
import com.vuvrecharge.preferences.UserPreferencesImpl;

import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    private DefaultPresenter mDefaultPresenter;
    ActivityLoginBinding binding;

    UserPreferences mDatabase = new UserPreferencesImpl();

    @BindView(R.id.main_bg)
    LinearLayout main_bg;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;

    @BindView(R.id.retry)
    TextView retry;
    int tryCount = 0;
//    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ButterKnife.bind(this);
        mDefaultPresenter = new DefaultPresenter(this);
        binding.btnLogin.setOnClickListener(this);
        binding.register.setOnClickListener(this);
        binding.buttonForgot.setOnClickListener(this);
        binding.buttonLogin.setOnClickListener(this);
        binding.passwordLayout.setHint("Password");
        binding.password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(binding.password, InputMethodManager.SHOW_FORCED);
                }
            }
        });
        mDefaultPresenter.checkLoginWithOtpStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "login");
//        preferences = getSharedPreferences("debug",Context.MODE_PRIVATE);
//        if (!Objects.equals(preferences.getString("state", ""), "cancel")){
//            if (isDeveloperModeEnabled()) {
//                openDeveloperDialog();
//            }
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideAllDialog();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        backAlert();
    }

    @Override
    public void onSuccess(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            if (jsonObject.getString("token") != null) {
                mDatabase.setToken(jsonObject.getString("token"));
                mDatabase.setUserLogin(true);
            }
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            timer.onFinish();
            timer.cancel();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    CountDownTimer timer;

    @Override
    public void onSuccess(String message, @NonNull String second_message) {
        try {
            if (second_message.equals("status")) {
                JSONObject object = new JSONObject(message);
                String value = object.getString("login_with_otp");
                if (value.equals("On")) {
                    otpView();
                }
            } else if (second_message.equals("otp")) {
                JSONObject object = new JSONObject(message);
                String value = object.getString("status");
                String otp = object.getString("otp");
                if (value.equals("Success")) {
                    binding.btnLogin.setText("Verify OTP");
                    binding.passwordLayout.setVisibility(View.VISIBLE);
                    binding.password.setVisibility(View.VISIBLE);
                    binding.passwordLayout.setHint("Enter OTP");
                    timer = new CountDownTimer(120000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (Objects.requireNonNull(binding.password.getText().toString()).isEmpty()) {
                                binding.btnLogin.setEnabled(false);
                                binding.btnLogin.setClickable(false);
                            } else {
                                binding.btnLogin.setEnabled(true);
                                binding.btnLogin.setClickable(true);
                            }
                            if (otp != null) {
                                binding.password.setText(otp);
                                mDefaultPresenter.loginVerifyUser(
                                        binding.username.getText().toString().trim(),
                                        binding.password.getText().toString().trim(),
                                        device_id
                                );
                                hideKeyBoard(binding.password);
                            }
                            binding.buttonForgot.setVisibility(View.VISIBLE);
                            binding.buttonForgot.setText("Didn't receive OTP? Resend in " + millisUntilFinished / 1000 + " seconds");
                        }

                        @Override
                        public void onFinish() {
                            binding.buttonForgot.setVisibility(View.GONE);
                            binding.btnLogin.setEnabled(true);
                            binding.btnLogin.setClickable(true);
                            binding.btnLogin.setText("Resend OTP");
                        }
                    }.start();
                }
            }
        } catch (Exception e) {
            Log.d("TAG_DATA", "onSuccess: " + e.getMessage());
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
            showError(bottomSheet, error);
            submit.setVisibility(View.VISIBLE);
        }
        showError(error);
    }

    @Override
    public void onShowDialog(String message) {
        if (bottomSheet != null) {
            showLoading(loading_dialog);
            submit.setVisibility(View.GONE);
        }
        showLoading(loading);
    }

    @Override
    public void onHideDialog() {
        if (bottomSheet != null) {
            hideLoading(loading_dialog);
            submit.setVisibility(View.VISIBLE);
        }
        hideLoading(loading);
    }

    @Override
    public void onShowToast(String message) {
        if (bottomSheet != null) {
            showToast(bottomSheet, message);
            submit.setVisibility(View.VISIBLE);
        } else {
            showToast(message);
        }
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }

    private void validateCredentials() {
        if (binding.btnLogin.getText().equals("Login")) {
            if (tryCount < 5) {
                mDefaultPresenter.loginUser(Objects.requireNonNull(binding.username.getText()).toString(),
                        Objects.requireNonNull(binding.password.getText()).toString(), device_id);
                tryCount++;
                hideKeyBoard(binding.username);
                hideKeyBoard(binding.password);
            } else {
                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
                hideKeyBoard(binding.username);
                hideKeyBoard(binding.password);
            }
        } else if (binding.btnLogin.getText().equals("Send OTP")) {
            if (Objects.requireNonNull(binding.username.getText()).toString().isEmpty()) {
                showError("Enter mobile number");
            } else {
                mDefaultPresenter.loginOTPUser(binding.username.getText().toString().trim());
                hideKeyBoard(binding.username);
            }
        } else if (binding.btnLogin.getText().equals("Resend OTP")) {
            if (Objects.requireNonNull(binding.username.getText()).toString().isEmpty()) {
                showError("Enter mobile number");
            } else {
                mDefaultPresenter.loginOTPUser(binding.username.getText().toString().trim());
                hideKeyBoard(binding.username);
            }
        } else if (binding.btnLogin.getText().equals("Verify OTP")) {
            if (Objects.requireNonNull(binding.username.getText()).toString().isEmpty()) {
                showError("Enter mobile number");
            } else if (Objects.requireNonNull(binding.password.getText()).toString().isEmpty()) {
                showError("Enter OTP");
            } else {
                mDefaultPresenter.loginVerifyUser(
                        binding.username.getText().toString().trim(),
                        binding.password.getText().toString().trim(),
                        device_id
                );
                hideKeyBoard(binding.password);
            }
        }
    }

    @Override
    public void onClick(@NonNull View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnLogin:
                validateCredentials();
                break;
            case R.id.register:
                intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.button_forgot:
                if (binding.buttonForgot.getText().equals("Reset Password?")) {
                    intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void otpView() {
        binding.buttonForgot.setVisibility(View.GONE);
        binding.passwordLayout.setVisibility(View.GONE);
        binding.password.setVisibility(View.GONE);
        binding.btnLogin.setText("Send OTP");
        binding.buttonLogin.setVisibility(View.GONE);
    }

}
