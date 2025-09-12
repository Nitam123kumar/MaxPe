package com.vuvrecharge.modules.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.material.textfield.TextInputEditText;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterUserActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.full_name)
    TextInputEditText full_name;
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
    @BindView(R.id.refer_code)
    CheckBox refer_code;
    @BindView(R.id.refer_Title)
    TextView refer_Title;

    String supportNumber;
    String whatsappNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ButterKnife.bind(this);
        changeStatusBarColorLoginPage();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setLayout(no_internet, retry, "register");
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

        if (refer_code.isChecked()){
            refer_Title.setVisibility(View.VISIBLE);
            refer_Title.setVisibility(View.GONE);
        }else {
            refer_Title.setVisibility(View.GONE);
            refer_Title.setVisibility(View.VISIBLE);
        }

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

        refer_code.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                refer_Title.setVisibility(View.VISIBLE);
                refer_Title.setVisibility(View.GONE);
            }else {
                refer_Title.setVisibility(View.GONE);
                refer_Title.setVisibility(View.VISIBLE);
            }
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
        hideKeyBoard(email);
    }

    @Override
    public void onClick(@NonNull View view) {

        switch (view.getId()) {
            case R.id.register:
//                validateCredentials();
                break;
            case R.id.goForLogin:
                onBackPressed();
                break;
        }

    }

    private void validateCredentials() {
        mDefaultPresenter.doRegister(
                Objects.requireNonNull(email.getText()).toString() + "",
                Objects.requireNonNull(full_name.getText()).toString() + "",
                "",
                "151617",
                Objects.requireNonNull(enter_referral_id.getText()).toString() + "");
    }


    @Override
    public void onError(String error) {

    }

    @Override
    public void onSuccess(String data) {

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
    public void onShowDialog(String message) {

    }

    @Override
    public void onHideDialog() {

    }

    @Override
    public void onShowToast(String message) {

    }

    @Override
    public void onPrintLog(String message) {

    }
}