package com.vuvrecharge.modules.activities;

import static com.vuvrecharge.api.ApiServices.IMAGE_FOLLOWS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vuvrecharge.R;
import com.vuvrecharge.api.ApiServices;
import com.vuvrecharge.application.MyApplication;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.databinding.BottomAddWalletBalanceLayoutBinding;
import com.vuvrecharge.databinding.FeedbackBinding;
import com.vuvrecharge.databinding.ResetmpinDialogBinding;
import com.vuvrecharge.databinding.SetPinDialogBinding;
import com.vuvrecharge.databinding.SettingDialogBinding;
import com.vuvrecharge.databinding.WhatsappDialogBinding;
import com.vuvrecharge.modules.activities.newActivities.ComplaintRegistrationActivity;
import com.vuvrecharge.modules.activities.newActivities.MobileBankingActivity;
import com.vuvrecharge.modules.adapter.FollowsAdapter;
import com.vuvrecharge.modules.adapter.PaymentSettingAdapter;
import com.vuvrecharge.modules.model.DefaultResponse;
import com.vuvrecharge.modules.model.Follows;
import com.vuvrecharge.modules.model.PaymentSetting;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.Fingerprint;
import com.vuvrecharge.preferences.PasswordLess;
import com.vuvrecharge.utils.Java_AES_Cipher;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends BaseActivity implements DefaultView, View.OnClickListener, PaymentSettingAdapter.OnClickListener, FollowsAdapter.OnClickListener {

    private DefaultPresenter mDefaultPresenter;
    @BindView(R.id.phoneNumber)
    TextView phoneNumber;
    @BindView(R.id.email_id)
    TextView email_id;
    @BindView(R.id.viewPrivacy)
    View viewPrivacy;
    @BindView(R.id.viewCondition)
    View viewCondition;
    @BindView(R.id.viewRefund)
    View viewRefund;
//    @BindView(R.id.viewChangePassword)
//    View viewChangePassword;
//    @BindView(R.id.viewProfile)
//    View viewProfile;
//    @BindView(R.id.viewAccountDelete)
//    View viewAccountDelete;
    @BindView(R.id.viewLogout)
    View viewLogout;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.email_TV)
    TextView email_TV;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.viewChangeMPin)
    View viewChangeMPin;
    @BindView(R.id.viewInvite)
    View viewInvite;
    @BindView(R.id.viewResetMPin)
    View viewResetMPin;
    @BindView(R.id.viewRateUs)
    View viewRateUs;
    @BindView(R.id.viewFeedBack)
    View viewFeedBack;
    @BindView(R.id.viewFingerPrint)
    View viewFingerPrint;
    @BindView(R.id.viewComplaint)
    View viewComplaint;
    @BindView(R.id.viewFollow)
    View viewFollow;
    @BindView(R.id.logo)
    TextView nameLogo;
//    @BindView(R.id.available_maxPointsTV)
//    TextView available_maxPointsTV;
//    @BindView(R.id.available_earnTV)
//    TextView available_earnTV;
//    @BindView(R.id.available_referralTV)
//    TextView available_referralTV;
    @BindView(R.id.btnSwitch)
    Switch btnSwitch;
    @BindView(R.id.onBack)
    LinearLayout onBack;
    DefaultView defaultView;
    Fingerprint fingerprint;
    PasswordLess passwordLess;
    String dataPayment = null;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    ArrayList<PaymentSetting> list = new ArrayList<>();
    CountDownTimer countDownTimer; // built in android class CountDownTimer
    long totalTimeCountInMilliseconds = 0;
    JSONObject json;
    String data = "";
    ArrayList<Follows> followsList = new ArrayList<>();
    FollowsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        defaultView = this;
        fingerprint = new Fingerprint(this);
        passwordLess = new PasswordLess(this);
        HashMap<String, String> mapData = fingerprint.getDetails();
        HashMap<String, String> mapData2 = passwordLess.getPasswordLess();
        if (Objects.equals(mapData.get(Fingerprint.FINGERPRINT), "true")) {
            btnSwitch.setChecked(true);
        } else {
            btnSwitch.setChecked(false);
        }

        btnSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                fingerprint();
                fingerprint.setFingerprint("true");
            } else {
                fingerprint.setFingerprint("false");
            }
        });

        setValues();
        viewPrivacy.setOnClickListener(this);
        viewCondition.setOnClickListener(this);
        viewRefund.setOnClickListener(this);
//        viewChangePassword.setOnClickListener(this);
        viewComplaint.setOnClickListener(this);
        viewFollow.setOnClickListener(this);
        viewFeedBack.setOnClickListener(this);
//        viewProfile.setOnClickListener(this);
//        viewAccountDelete.setOnClickListener(this);
        viewLogout.setOnClickListener(this);
        viewRateUs.setOnClickListener(this);
        viewInvite.setOnClickListener(this);
        onBack.setOnClickListener(this);
        viewFingerPrint.setOnClickListener(this);
        viewChangeMPin.setOnClickListener(this);
        viewResetMPin.setOnClickListener(this);
        mDefaultPresenter = new DefaultPresenter(this);
        mDefaultPresenter.getPaymentSetting2(device_id + "", "timepass");
        mDefaultPresenter.totalReferrals(device_id);

//        statusBarColor();
    }

    private void setValues() {
        try {
            UserData userData = mDatabase.getUserData();
            String shortName = mDatabase.getUserData().getName();
            String initials = getInitials(shortName, 2);
            nameLogo.setText(initials);
            name.setText(userData.getName());
            email_TV.setText(userData.getEmail());
//            available_maxPointsTV.setText(userData.getCashbackPoints());
//            available_earnTV.setText("\u20b9"+userData.getEarnings());
            phoneNumber.setText("+91-"+userData.getMobile());
            email_id.setText("Member Since " + userData.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getInitials(String name, int count) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }

        String[] words = name.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();

        for (int i = 0; i < words.length && i < count; i++) {
            if (!words[i].isEmpty()) {
                initials.append(words[i].charAt(0));
            }
        }

        return initials.toString().toUpperCase(); // optional uppercase
    }

    public void fingerprint() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "This device does not have a fingerprint sensor", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "The biometric sensor is currently unavailable", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "Your device doesn't have fingerprint saved,please check your security settings", Toast.LENGTH_SHORT).show();
                break;
        }
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(AccountActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                System.exit(0);
                Toast.makeText(AccountActivity.this, "Authenication failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("MAXPe Payment")
                .setDescription("Use fingerprint to login").setDeviceCredentialAllowed(true).build();
        biometricPrompt.authenticate(promptInfo);
    }

    @Override
    public void onClick(@NonNull View view) {
        Intent intent;
        UserData userData = mDatabase.getUserData();
        switch (view.getId()) {
            case R.id.viewPrivacy:
                intent = new Intent(getActivity(), AboutActivity.class);
                intent.putExtra("from", "Privacy Policy");
                startActivity(intent);
                break;
            case R.id.viewCondition:
                intent = new Intent(getActivity(), AboutActivity.class);
                intent.putExtra("from", "Terms & Conditions");
                startActivity(intent);
                break;
            case R.id.viewRefund:
                intent = new Intent(getActivity(), AboutActivity.class);
                intent.putExtra("from", "Return, Refund and Cancellation policy");
                startActivity(intent);
                break;
//            case R.id.viewProfile:
//                intent = new Intent(getActivity(), ProfileActivity.class);
//                startActivity(intent);
//                break;
            case R.id.viewComplaint:
                intent = new Intent(getActivity(), ComplaintRegistrationActivity.class);
                startActivity(intent);
                break;
//            case R.id.viewChangePassword:
//                openSetting();
//                break;
            case R.id.viewLogout:
                makeLogout();
                break;
            case R.id.viewChangeMPin:
                setPin();
                break;
//            case R.id.btnWallet:
//                addBalance();
//                break;
            case R.id.viewResetMPin:
                ResetMpinPin(userData.getMobile());
                break;
            case R.id.viewFollow:
                followUs();
                break;
//            case R.id.support:
//                intent = new Intent(getActivity(), SupportActivity.class);
//                startActivity(intent);
//                break;
            case R.id.viewInvite:
                intent = new Intent(getActivity(), ShareEarnActivity.class);
                startActivity(intent);
                break;
            case R.id.viewFingerPrint:
                if (btnSwitch.isChecked()) {
                    btnSwitch.setChecked(false);
                    fingerprint.setFingerprint("false");
                } else {
                    btnSwitch.setChecked(true);
                    fingerprint.setFingerprint("true");
                }
                break;
//            case R.id.instagram:
//                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/maxpe_payments/"));
//                startActivity(intent);
//                break;
            case R.id.viewRateUs:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vuvrecharge"));
                startActivity(intent);
                break;
            case R.id.viewFeedBack:
                intent = new Intent(getActivity(),FeedbackActivity.class);
                startActivity(intent);
                break;
//            case R.id.viewAccountDelete:
//                makeDeleteAccount();
//                break;
            case R.id.onBack:
                onBackPressed();
                finish();
                break;
            default:
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();

        }
    }

    private void addBalance() {
        try {
            FrameLayout bottomSheet = null;
            BottomSheetDialog dialogBottomSheetDialog = null;
            list.clear();

            BottomAddWalletBalanceLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.bottom_add_wallet_balance_layout, null, false);

            dialogBottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            dialogBottomSheetDialog.setContentView(binding.getRoot());
            dialogBottomSheetDialog.setCancelable(true);
            changeStatusBarColor(dialogBottomSheetDialog);
            bottomSheet = dialogBottomSheetDialog.findViewById(com.denzcoskun.imageslider.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }

            if (json.getString("phonepe_getway").equals("Yes")) {
                list.add(new PaymentSetting(R.drawable.phonepe_logo_2, "PhonePe", "Payment Gateway"));
            }
            if (json.getString("hdfc_dynamic_getway").equals("Yes")) {
                list.add(new PaymentSetting(R.drawable.upi_logo_2, "UPI", "Payment Gateway"));
            }
            if (json.getString("razorpay_getway").equals("Yes")) {
                list.add(new PaymentSetting(R.drawable.razorpay_logo_2, "Razorpay", "Payment Gateway"));
            }
            if (json.getString("paytm_getway").equals("Yes")) {
                list.add(new PaymentSetting(R.drawable.paytm_logo_2, "Paytm", "Payment Gateway"));
            }
            if (json.getString("manual_getway").equals("Yes")) {
                list.add(new PaymentSetting(R.drawable.mobile_banking_logo_2, "Mobile", "Banking (IMPS)"));
            }
//            if (json.getString("manual_getway").equals("Yes")){
//                list.add(new PaymentSetting(R.drawable.mobile_banking_logo_2,"Mobile","Banking (IMPS)"));
//            }

            if (!list.isEmpty()) {
                LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                binding.recyclerViewPaymentSetting.setLayoutManager(manager);
            }
            if (list.size() > 1) {
                binding.recyclerViewPaymentSetting.setLayoutManager(new GridLayoutManager(this, 2));
            }

            if (list.size() > 2) {
                binding.recyclerViewPaymentSetting.setLayoutManager(new GridLayoutManager(this, 3));
            }

            if (list.size() > 3) {
                binding.recyclerViewPaymentSetting.setLayoutManager(new GridLayoutManager(this, 4));
            }

            PaymentSettingAdapter adapter1 = new PaymentSettingAdapter(this, this);
            adapter1.addData(list);
            binding.recyclerViewPaymentSetting.setAdapter(adapter1);

            dialogBottomSheetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ResetMpinPin(String mobile) {
        try {
            ResetmpinDialogBinding binding_ = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                    R.layout.resetmpin_dialog, null, false);
            dialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
            dialog.setContentView(binding_.getRoot());
            changeStatusBarColor(dialog);

            bottomSheet = dialog.findViewById(com.denzcoskun.imageslider.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }

            submit = binding_.submit;
            loading_dialog = binding_.loading;

            TextView textView = binding_.resend;
            binding_.dialogImage.setOnClickListener(v -> {

                if (submit.getVisibility() == View.VISIBLE) {
                    hideKeyBoard(binding_.oldPassword);
                    new Handler().postDelayed(() -> {
                        if (dialog != null) {
                            dialog.dismiss();
                            bottomSheet = null;
                            dialog = null;
                        }
                    }, 200);
                }

            });
            binding_.resendOtp.setOnClickListener(v -> {
                if (TextUtils.isEmpty(mobile.trim())) {
                    Toasty.error(getApplication(), "Please enter mobile", Toast.LENGTH_LONG, false).show();
                    return;
                }

                try {
                    JSONObject post_data = new JSONObject();
                    post_data.put("mobile", mDatabase.getUserData().getMobile().trim());
                    JSONObject data = new JSONObject();
                    data.put("request_url", ApiServices.forgotMpinOtp);
                    data.put("post_data", post_data);

                    String data_final = data.toString();
                    String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
                    defaultView.onShowDialog("Loading...");

                    binding_.resendOtp.setVisibility(View.GONE);
                    Call<DefaultResponse> responseCall = MyApplication.getInstance()
                            .getApiInterface()
                            .defaultRequest(encrypted);
                    responseCall.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                            defaultView.onHideDialog();
                            binding_.resendOtp.setVisibility(View.VISIBLE);
                            if (response.isSuccessful() && response.code() == 200) {
                                DefaultResponse body = response.body();
                                if (body != null) {
                                    if (body.getSuccess() == 1) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(body.getData());
                                            totalTimeCountInMilliseconds = 1 * 180 * 1000;
                                            setTimer(textView);
                                            Toasty.success(getActivity(), body.getMessage(), Toast.LENGTH_LONG, false).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toasty.error(getActivity(), body.getMessage(), Toast.LENGTH_LONG, false).show();

                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                            defaultView.onHideDialog();
                            binding_.resend.setVisibility(View.VISIBLE);

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            binding_.submit.setOnClickListener(v -> {
                String oldPassword = binding_.oldPassword.getText().toString();
                String newPassword = binding_.newPassword.getText().toString();
                String reNewPassword = binding_.reNewPassword.getText().toString();

                if (TextUtils.isEmpty(oldPassword.trim())) {
                    showError(bottomSheet, "Please enter otp");
                    return;
                }
                if (TextUtils.isEmpty(newPassword.trim())) {
                    showError(bottomSheet, "Please enter new Mpin");
                    return;
                }
                if (!newPassword.trim().equals(reNewPassword.trim())) {
                    showError(bottomSheet, "Confirm Mpin doesn't matched");
                    return;
                }
                binding_.submit.setVisibility(View.GONE);
                hideKeyBoard(binding_.reNewPassword);
                if (dialog != null) {
                    dialog.dismiss();
                }
                hideLoading(loading_dialog);
                mDefaultPresenter.forgotMpin(mobile + "", oldPassword + "", newPassword + "", device_id);
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void feedback(String mobile) {
        try {
            FeedbackBinding binding_ = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                    R.layout.feedback, null, false);
            dialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
            dialog.setContentView(binding_.getRoot());
            dialog.setCancelable(false);
            changeStatusBarColor(dialog);

            bottomSheet = dialog.findViewById(com.denzcoskun.imageslider.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }

            loading_dialog = binding_.loading;

            binding_.dialogImage.setOnClickListener(v -> {
                if (binding_.btnSubmit.getVisibility() == View.VISIBLE) {
                    hideKeyBoard(binding_.feedback);
                    new Handler().postDelayed(() -> {
                        dialog.dismiss();
                        bottomSheet = null;
                        dialog = null;
                    }, 200);
                }

            });
            binding_.btnSubmit.setOnClickListener(v -> {
                if (TextUtils.isEmpty(binding_.feedback.getText().toString().trim())) {
                    showError(bottomSheet, "Please provide feedback");
                    return;
                }

                hideKeyBoard(binding_.feedback);
                mDefaultPresenter.getFeedback(device_id, binding_.feedback.getText().toString().trim(), 5);
                dialog.dismiss();
                hideLoading(loading_dialog);

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setPin() {
        SetPinDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                R.layout.set_pin_dialog, null, false);

        dialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(binding.getRoot());
        changeStatusBarColor(dialog);

        bottomSheet = dialog.findViewById(com.denzcoskun.imageslider.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(false);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setPeekHeight(600);
        }
        binding.proceed.setOnClickListener(v -> {
            String oldmPin = binding.pin.getText().toString();
            String newmPin = binding.pinCon.getText().toString();

            if (TextUtils.isEmpty(oldmPin.trim())) {
                showError(bottomSheet, "Please enter old Mpin");
                binding.pin.setText("");
                return;
            }
            if (oldmPin.length() < 4) {
                showError(bottomSheet, "Please enter 4 digits  Mpin");
                return;
            }
            if (TextUtils.isEmpty(newmPin.trim())) {
                showError(bottomSheet, "Please enter New Mpin");
                binding.pinCon.setText("");
                return;
            }
            if (newmPin.length() < 4) {
                showError(bottomSheet, "Please enter 4 digits  Mpin");
                return;
            }
            hideKeyBoard(binding.pin);
            dialog.dismiss();
            mDefaultPresenter.getSecCode(oldmPin + "", newmPin + "", device_id + "");
            hideLoading(loading_dialog);
        });

        binding.dialogImage.setOnClickListener(v -> {
            hideKeyBoard(binding.pin);
            dialog.dismiss();
        });

        dialog.show();
    }

    public void whatsAppDialog(String data) {
        try {
            WhatsappDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.whatsapp_dialog, null, false);
            Dialog dialog = new Dialog(this);
            dialog.setContentView(binding.getRoot());
//            dialog.setCancelable(false);

            JSONObject jsonObject = new JSONObject(data);
            printLog(jsonObject.toString());

            binding.message.setText(jsonObject.getString("message"));
            binding.action.setText(jsonObject.getString("buttonText"));

            String whatsappMsg = jsonObject.getString("whatsappMsg");
            String number = jsonObject.getString("number");
            binding.close.setOnClickListener(v -> dialog.dismiss());
            binding.action.setOnClickListener(v -> {

                boolean installed = appInstalledOrNot("com.whatsapp");
                if (installed) {
                    Intent waIntent = new Intent(Intent.ACTION_VIEW);
                    waIntent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+91" + number + "&text=" + whatsappMsg));
                    startActivity(waIntent);

                } else {
                    onShowToast("WhatsApp not install in your phone. Please install WhatsApp first.");
                }

                dialog.dismiss();

            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean appInstalledOrNot(String url) {
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void openSetting() {
        try {
            SettingDialogBinding binding_ = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                    R.layout.setting_dialog, null, false);
            dialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
            dialog.setContentView(binding_.getRoot());
            dialog.setCancelable(true);
            changeStatusBarColor(dialog);

            bottomSheet = dialog.findViewById(com.denzcoskun.imageslider.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }

            TextView submit = binding_.submit;
            loading_dialog = binding_.loading;

            binding_.dialogImage.setOnClickListener(v -> {
                if (submit.getVisibility() == View.VISIBLE) {
                    hideKeyBoard(binding_.oldPassword);
                    new Handler().postDelayed(() -> {
                        dialog.dismiss();
                    }, 200);
                }
            });

            binding_.submit.setOnClickListener(v -> {
                String oldPassword = binding_.oldPassword.getText().toString();
                String newPassword = binding_.newPassword.getText().toString();
                String reNewPassword = binding_.reNewPassword.getText().toString();

                if (TextUtils.isEmpty(oldPassword.trim())) {
                    showError(bottomSheet, "Please enter old password");
                    return;
                }
                if (TextUtils.isEmpty(newPassword.trim())) {
                    showError(bottomSheet, "Please enter new password");
                    return;
                }
                if (!newPassword.trim().equals(reNewPassword.trim())) {
                    showError(bottomSheet, "Confirm password doesn't matched");
                    return;
                }
                hideKeyBoard(binding_.oldPassword);
                hideKeyBoard(binding_.newPassword);
                hideKeyBoard(binding_.reNewPassword);
                mDefaultPresenter.updatePassword(device_id, oldPassword, newPassword);
                dialog.dismiss();
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "account");
        new Handler(Looper.getMainLooper())
                .postDelayed(() -> mDefaultPresenter.getFollow(device_id), 1000);
    }

    @Override
    public void onSuccess(String message) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        try {
            JSONObject jsonObject = new JSONObject(message);

//            available_referralTV.setText("\u20b9" + jsonObject.getString("totalReferralPaid"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(String message, String second_message) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        if (!message.equals("")) {
            mDatabase.setUserData(message);
            setValues();
        }
//        Toasty.success(getActivity(), second_message, Toast.LENGTH_LONG, false).show();
        showMessage(second_message);
    }

    public void showMessage(String message) {
        try {
            View view = getLayoutInflater().inflate(R.layout.message_success_dialog_layout, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            AlertDialog dialog1 = builder.create();
            if (dialog1.getWindow() != null) {
                dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            ImageView img = view.findViewById(R.id.imgRight);
            ConstraintLayout layout = view.findViewById(R.id.layout);
            TextView title = view.findViewById(R.id.txtTitle);
            if (message.equals("Password updated successfully.")) {
                img.setImageResource(R.drawable.right_image);
                layout.setBackgroundResource(R.drawable.light_white_background_drawable);
                title.setText(message);
            } else if (message.equals("Mpin updated successfully.")) {
                img.setImageResource(R.drawable.right_image);
                layout.setBackgroundResource(R.drawable.light_white_background_drawable);
                title.setText(message);
            } else {
                img.setImageResource(R.drawable.wrong_image);
                layout.setBackgroundResource(R.drawable.light_white_background_drawable);
                title.setText(message);
            }

            view.findViewById(R.id.btnOk)
                    .setOnClickListener(v -> {
                        dialog1.dismiss();
                        dialog1.cancel();
                    });
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                dialog1.dismiss();
                dialog1.cancel();
            }, 4000);
            dialog1.show();
        } catch (Exception e) {
            Log.d("TAG_DATA", "showMessage: " + e.getMessage());
        }

    }

    @Override
    public void onSuccessOther(String data) {
        try {
            JSONObject object = new JSONObject(data);
            String message = object.getString("message");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccessOther(String data, @NonNull String data_other) {
        if (data_other.equals("")) {
            whatsAppDialog(data);
        } else if (data_other.equals("UPI Payment")) {
            Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
            intent.putExtra("data", data);
            intent.putExtra("title", "UPI Payment");
            startActivity(intent);
        } else if (data_other.equals("PhonePe Payment")) {
            Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
            intent.putExtra("data", data);
            intent.putExtra("title", "UPI Payment");
            startActivity(intent);
        } else if (data_other.equals("Razorpay Payment")) {
            Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
            intent.putExtra("data", data);
            intent.putExtra("title", "Razorpay Payment");
            startActivity(intent);
        } else if (data_other.equals("Paytm Payment")) {
            Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
            intent.putExtra("data", data);
            intent.putExtra("title", "Paytm Payment");
            startActivity(intent);
        }
//        else if (data_other.equals("CashFree Payment")) {
//            Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
//            intent.putExtra("data", data);
//            intent.putExtra("title", "CashFree Payment");
//            startActivity(intent);
//        }
        else if (data_other.equals("timepass")) {
            try {
                dataPayment = data;
                json = new JSONObject(data);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else if (data_other.equals("follows")) {
            this.data = data;
        }
    }

    private void setTimer(TextView resend) {
        try {
            resend.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
                // 500 means, onTick function will be called at every 500 milliseconds
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long leftTimeInMilliseconds) {
                    totalTimeCountInMilliseconds = leftTimeInMilliseconds;
                    //  long seconds = leftTimeInMilliseconds / 1000;
                    resend.setText("" + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(leftTimeInMilliseconds),
                            TimeUnit.MILLISECONDS.toSeconds(leftTimeInMilliseconds) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(leftTimeInMilliseconds))));

                    //  resend.setText("Re-Generate After " + String.format("%02d", (seconds % 3600) / 60) + ":" + String.format("%02d", (seconds % 3600) % 60));
                }

                @Override
                public void onFinish() {
                    totalTimeCountInMilliseconds = 0;
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    if (resend != null) {
                        resend.setVisibility(View.GONE);
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(bottomSheet, error);
        }
        showError(error);
    }

    @Override
    public void onShowDialog(String message) {
        if (bottomSheet != null) {
            showLoading(loading);
        }
        showLoading(loading);
    }

    @Override
    public void onHideDialog() {
        if (bottomSheet != null) {
            hideLoading(loading);
        }
        hideLoading(loading);
    }

    @Override
    public void onShowToast(String message) {
        if (bottomSheet != null) {
            showToast(bottomSheet, message);
        }
        showToast(message);
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

    //    TODO Payment Clicks
    @Override
    public void onClick(@NonNull String name) {
        Intent intent = null;
        switch (name) {
            case "Paytm":
                intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", dataPayment);
                intent.putExtra("title", "Paytm Payment");
                startActivity(intent);
//                mDefaultPresenter.getPaymentSetting(device_id + "","Paytm Payment");
                break;
            case "Razorpay":
                intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", dataPayment);
                intent.putExtra("title", "Razorpay Payment");
                startActivity(intent);
//                mDefaultPresenter.getPaymentSetting(device_id + "","Razorpay Payment");
                break;
            case "UPI":
                intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", dataPayment);
                intent.putExtra("title", "UPI Payment");
                startActivity(intent);
//                mDefaultPresenter.getPaymentSetting(device_id + "","UPI Payment");
                break;
            case "PhonePe":
                intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", dataPayment);
                intent.putExtra("title", "PhonePe Payment");
                startActivity(intent);
//                mDefaultPresenter.getPaymentSetting(device_id + "","PhonePe Payment");
                break;
            case "Mobile":
                intent = new Intent(getActivity(), MobileBankingActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void followUs() {
        FrameLayout bottomSheet = null;
        TextView submit;
        LinearLayout loading_dialog;
        BottomSheetDialog dialog = null;
        followsList.clear();

        dialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.follows_layout, null,false);
        RecyclerView recyclerView=layout.findViewById(R.id.followUsRecyclerView);
        ImageView cancel=layout.findViewById(R.id.img2);
        dialog.setContentView(layout);
        dialog.setCancelable(true);
        changeStatusBarColor(dialog);
        bottomSheet = dialog.findViewById(com.denzcoskun.imageslider.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(false);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setPeekHeight(600);
        }

        BottomSheetDialog finalDialog = dialog;
        cancel.setOnClickListener(v -> {
            finalDialog.cancel();
        });

        try {
            adapter = new FollowsAdapter(this, this);
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("followers_data");
            for (int i = 0; i < array.length(); i++) {
                String logo = array.getJSONObject(i).getString("logo");
                String title = array.getJSONObject(i).getString("title");
                String redirect_url = array.getJSONObject(i).getString("redirect_url");

                followsList.add(new Follows(IMAGE_FOLLOWS + "/" + logo, title, redirect_url));
                adapter.addData(followsList);
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            Log.d("TAG_DATA", "followUs: " + e.getMessage());
        }

        dialog.show();
    }

    @Override
    public void onFollowLinkClick(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
}