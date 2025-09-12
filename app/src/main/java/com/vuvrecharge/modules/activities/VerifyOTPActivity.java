package com.vuvrecharge.modules.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.vuvrecharge.utils.LocaleHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

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
    @BindView(R.id.resendOtpTV)
    TextView resendOtpTV;
    @BindView(R.id.timer1)
    TextView timer1;
    @BindView(R.id.back_home)
    ImageView back_home;
    @BindView(R.id.call_layout)
    ConstraintLayout call_layout;
    @BindView(R.id.whatsapp_layout)
    ConstraintLayout whatsapp_layout;

    int tryCount = 0;
    CountDownTimer timer;
    String supportNumber1;
    String whatsappNumber1;
    String number;

    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otpactivity);
        ButterKnife.bind(this);
        presenter = new DefaultPresenter(this);
        number = getIntent().getStringExtra("number");
        optOfNumber.setText("OTP Sent to Mobile +91 " + number);
        changeStatusBarColorLoginPage();
        presenter.supportContacts(device_id);

        call_layout.setOnClickListener(v -> {
            if (selectCall()) {
                makeACall(supportNumber1);
            }
        });

        whatsapp_layout.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send/?phone=91" + whatsappNumber1 + "&text=" + "Hello MaxPe Support, My registered mobile number is " + number;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            Intent chooser = Intent.createChooser(i, "Chat with...");
            startActivity(chooser);
        });
        resendOtpTV.setOnClickListener(v -> {
            presenter.loginOTPUser(number);
            Toasty.success(this, "Resend OTP", Toasty.LENGTH_SHORT).show();
        });

        back_home.setOnClickListener(v -> {
            onBackPressed();
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
                timer1.setVisibility(View.VISIBLE);
                timer1.setText("Didn't receive OTP? Resend in " + millisUntilFinished / 1000 + " seconds");
                resendOtpTV.setVisibility(View.GONE);
                timerTV.setVisibility(View.GONE);

            }

            @Override
            public void onFinish() {
                btnVerifyOtp.setEnabled(true);
                btnVerifyOtp.setClickable(true);
                resendOtpTV.setVisibility(View.VISIBLE);
                timerTV.setVisibility(View.VISIBLE);
                timer1.setVisibility(View.GONE);
            }
        }.start();


        btnVerifyOtp.setOnClickListener(v -> {
            String btnText = btnVerifyOtp.getText().toString();
                if (Objects.requireNonNull(btnVerifyOtp.getText()).toString().isEmpty()) {
                    showErrorLoginPage("Enter OTP");
                } else {
                    presenter.loginVerifyUser(
                            number,
                            enterOtp.getText().toString().trim(),
                            device_id
                    );
                    hideKeyBoard(enterOtp);
                    timer.cancel();

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
            timer.onFinish();
            timer.cancel();
            btnVerifyOtp.setText("Verified");
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String data, String data_other) {
        try {
            JSONObject object = new JSONObject(data);
            if (data_other.equals("otp")) {
                String value = object.getString("status");
                if (value.equals("Success")) {
                    timer.start();
                    btnVerifyOtp.setText("Verify OTP");
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSuccessOther(String data) {

    }

    @Override
    public void onSuccessOther(String data, String data_other) {
        try {
            JSONObject supportContacts = new JSONObject(data);
            supportNumber1 = supportContacts.getString("support_number");
            whatsappNumber1 = supportContacts.getString("whatsapp_number");
            Log.d("support_number", supportNumber1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showErrorLoginPage(bottomSheet, error);
            submit.setVisibility(View.VISIBLE);
        }
//        showErrorLoginPage(error);
        Toasty.error(this, error, Toasty.LENGTH_LONG).show();
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