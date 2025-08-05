package com.vuvrecharge.modules.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.activities.newActivities.IntroActivity;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.presenter.SplashView;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.CommissionPreferences;
import com.vuvrecharge.preferences.Fingerprint;
import com.vuvrecharge.preferences.IntroPreferences;
import com.vuvrecharge.preferences.OperatorPreferences;
import com.vuvrecharge.utils.LocaleHelper;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements SplashView, DefaultView {

    DefaultPresenter mDefaultPresenter;
    DefaultView mDefaultView;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    HashMap<String, String> mapData, mapData2;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        fullScreen();
        ButterKnife.bind(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mDefaultView = this;
        mDefaultPresenter  = new DefaultPresenter((SplashView) this);
        mapData = new Fingerprint(this).getDetails();
        mapData2 = new IntroPreferences(this).mapData();
        clearAllOperatorData();
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            if (!Objects.equals(mapData2.get(IntroPreferences.TAG_SLIDE), "true")){
                startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                finish();
            }else {
                mDefaultPresenter.checkUserIsLogin();
            }
        },150);
    }

    public void fullScreen() {
        Window window = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setStatusBarColor(getResources().getColor(R.color.transparent));

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }

    @Override
    public void showLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showMainActivity() {
        if (Objects.equals(mapData.get(Fingerprint.FINGERPRINT), "true")){
            fingerprint();
        }else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    public void fingerprint(){
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL | BiometricManager.Authenticators.BIOMETRIC_STRONG))
        {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(),"This device does not have a fingerprint sensor",Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(),"The biometric sensor is currently unavailable",Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
//                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
//                startActivityForResult(enrollIntent, 0);
                Toast.makeText(getApplicationContext(),"Your device doesn't have fingerprint saved,please check your security settings",Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                Toast.makeText(getApplicationContext(),"Your device doesn't have fingerprint",Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                break;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                break;
            case BiometricManager.BIOMETRIC_SUCCESS:
                break;
        }
        Executor executor= ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(SplashActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                System.exit(0);
                Toast.makeText(SplashActivity.this, "Authenication failed", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        promptInfo=new BiometricPrompt.PromptInfo.Builder().setTitle("MAXPe Payment")
                .setDescription("Use fingerprint to login").setDeviceCredentialAllowed(true).build();
        biometricPrompt.authenticate(promptInfo);
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

    public OperatorPreferences prefElectricity;
    public OperatorPreferences prefFastag;
    public OperatorPreferences prefInsurance;
    public OperatorPreferences prefCylinder;
    public OperatorPreferences prefGas;
    public OperatorPreferences prefWater;
    public OperatorPreferences prefLandline;
    public OperatorPreferences prefCreditCardPayment;
    public OperatorPreferences prefLoanRePayment;
    public OperatorPreferences prefCableTV;
    public OperatorPreferences prefMunicipalTax;
    public OperatorPreferences prefHousingSociety;
    public OperatorPreferences prefClubAssociation;
    public OperatorPreferences prefHospitalPathology;
    public OperatorPreferences prefSubscriptions;
    public OperatorPreferences prefPrepaid;
    public OperatorPreferences prefPrepaid2;
    public OperatorPreferences prefPostpaid;
    public OperatorPreferences prefDTH;
    public OperatorPreferences prefGiftCards;
    public OperatorPreferences donation;
    public OperatorPreferences recurringDeposit;
    public OperatorPreferences municipalServices;
    public OperatorPreferences educationFees;
    public OperatorPreferences nCMCRecharge;
    public OperatorPreferences prepaidMeter;
    public OperatorPreferences profilePreferences;

    public CommissionPreferences prepaidCommissionPreferences;
    public CommissionPreferences dTHCommissionPreferences;
    public CommissionPreferences otherCommissionPreferences;
    private void clearAllOperatorData() {
        prefElectricity = new OperatorPreferences(getApplicationContext(), "Electricity");
        prefElectricity.clear();
        prefFastag = new OperatorPreferences(getApplicationContext(), "Fastag");
        prefFastag.clear();
        donation = new OperatorPreferences(getApplicationContext(), "Donation");
        donation.clear();
        recurringDeposit = new OperatorPreferences(getApplicationContext(), "RecurringDeposit");
        recurringDeposit.clear();
        municipalServices = new OperatorPreferences(getApplicationContext(), "MunicipalServices");
        municipalServices.clear();
        educationFees = new OperatorPreferences(getApplicationContext(), "EducationFees");
        educationFees.clear();
        nCMCRecharge = new OperatorPreferences(getApplicationContext(), "NCMCRecharge");
        nCMCRecharge.clear();
        prepaidMeter = new OperatorPreferences(getApplicationContext(), "PrepaidMeter");
        prepaidMeter.clear();
        prefInsurance = new OperatorPreferences(getApplicationContext(), "Insurance");
        prefInsurance.clear();
        prefCylinder = new OperatorPreferences(getApplicationContext(), "Cylinder");
        prefCylinder.clear();
        prefGas = new OperatorPreferences(getApplicationContext(), "Gas");
        prefGas.clear();
        prefWater = new OperatorPreferences(getApplicationContext(), "Water");
        prefWater.clear();
        prefLandline = new OperatorPreferences(getApplicationContext(), "Landline");
        prefLandline.clear();
        prefCreditCardPayment = new OperatorPreferences(getApplicationContext(), "CreditCardPayment");
        prefCreditCardPayment.clear();
        prefLoanRePayment = new OperatorPreferences(getApplicationContext(), "LoanRePayment");
        prefLoanRePayment.clear();
        prefCableTV = new OperatorPreferences(getApplicationContext(), "CableTV");
        prefCableTV.clear();
        prefMunicipalTax = new OperatorPreferences(getApplicationContext(), "MunicipalTax");
        prefMunicipalTax.clear();
        prefHousingSociety = new OperatorPreferences(getApplicationContext(), "HousingSociety");
        prefHousingSociety.clear();
        prefClubAssociation = new OperatorPreferences(getApplicationContext(), "ClubAssociation");
        prefClubAssociation.clear();
        prefHospitalPathology = new OperatorPreferences(getApplicationContext(), "HospitalPathology");
        prefHospitalPathology.clear();
        prefSubscriptions = new OperatorPreferences(getApplicationContext(), "Subscriptions");
        prefSubscriptions.clear();
        prefPrepaid = new OperatorPreferences(getApplicationContext(), "Prepaid");
        prefPrepaid.clear();
        prefPrepaid2 = new OperatorPreferences(getApplicationContext(), "Prepaid2");
        prefPrepaid2.clear();
        prefPostpaid = new OperatorPreferences(getApplicationContext(), "Postpaid");
        prefPostpaid.clear();
        prefDTH = new OperatorPreferences(getApplicationContext(), "DTH");
        prefDTH.clear();
        prefGiftCards = new OperatorPreferences(getApplicationContext(), "GiftCards");
        prefGiftCards.clear();

        profilePreferences = new OperatorPreferences(getApplicationContext(), "profileDetails");
        profilePreferences.clear();

        prepaidCommissionPreferences = new CommissionPreferences(getApplicationContext(), "PrepaidCommission");
        prepaidCommissionPreferences.clear();
        dTHCommissionPreferences = new CommissionPreferences(getApplicationContext(), "DTHCommission");
        dTHCommissionPreferences.clear();
        otherCommissionPreferences = new CommissionPreferences(getApplicationContext(), "OtherCommission");
        otherCommissionPreferences.clear();

    }
}
