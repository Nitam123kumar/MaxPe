package com.vuvrecharge.modules.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyOTPActivity extends BaseActivity implements DefaultView {

    DefaultPresenter presenter;
    @BindView(R.id.enter_otp)
    TextInputEditText enterOtp;

    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;

    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.opt_of_number)
    TextView optOfNumber;
    @BindView(R.id.btnVerifyOtp)
    TextView btnVerifyOtp;
    @BindView(R.id.timer)
    TextView timerTV;
    @BindView(R.id.help_support)
    TextView help_support;
    @BindView(R.id.call_layout)
    ConstraintLayout call_layout;
    @BindView(R.id.whatsapp_layout)
    ConstraintLayout whatsapp_layout;

    int tryCount = 0;
    CountDownTimer timer;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otpactivity);
        ButterKnife.bind(this);
        presenter = new DefaultPresenter(this);
        number = getIntent().getStringExtra("number");
        optOfNumber.setText("OTP Sent to Mobile +" + number);

        call_layout.setOnClickListener(v -> {
            if (selectCall()) {
                makeACall(mDatabase.getUserData().getSupport_number());
            }
        });

        whatsapp_layout.setOnClickListener(v -> {
            UserData userData = mDatabase.getUserData();
            String url = "https://api.whatsapp.com/send/?phone=91" + userData.getWhatsapp_number() + "&text=" + "Hello MaxPe Support, My registered mobile number is " + userData.getMobile();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            Intent chooser = Intent.createChooser(i, "Chat with...");
            startActivity(chooser);
        });


        timer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (Objects.requireNonNull(btnVerifyOtp.getText().toString()).isEmpty()) {
                    btnVerifyOtp.setEnabled(false);
                    btnVerifyOtp.setClickable(false);
                } else {
                    btnVerifyOtp.setEnabled(true);
                    btnVerifyOtp.setClickable(true);
                }
//                            if (otp != null) {
//                                binding.password.setText(otp);
//                                mDefaultPresenter.loginVerifyUser(
//                                        binding.username.getText().toString().trim(),
//                                        binding.password.getText().toString().trim(),
//                                        device_id
//                                );
//                                hideKeyBoard(binding.password);
//                            }
                timerTV.setText("Didn't receive OTP? Resend in " + millisUntilFinished / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                btnVerifyOtp.setEnabled(true);
                btnVerifyOtp.setClickable(true);
//                btnVerifyOtp.setText("Resend OTP");
            }
        }.start();

        btnVerifyOtp.setOnClickListener(v -> {
            if (Objects.requireNonNull(btnVerifyOtp.getText()).toString().isEmpty()) {
                showError1("Enter mobile number");
            } else if (Objects.requireNonNull(btnVerifyOtp.getText()).toString().isEmpty()) {
                showError1("Enter OTP");
            } else {
                presenter.loginVerifyUser(
                        number,
                        enterOtp.getText().toString().trim(),
                        device_id
                );
                hideKeyBoard(enterOtp);
            }
        });

    }


    @Override
    public void onSuccess(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
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

    @Override
    public void onSuccess(String data, String data_other) {

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
        }
        showError1(error);
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

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "login");
    }
}