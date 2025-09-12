package com.vuvrecharge.modules.presenter;

import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

import com.vuvrecharge.api.ApiServices;
import com.vuvrecharge.application.MyApplication;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.model.DefaultResponse;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.UserPreferences;
import com.vuvrecharge.preferences.UserPreferencesImpl;
import com.vuvrecharge.utils.Java_AES_Cipher;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DefaultPresenter {

    private DefaultView mDefaultView;
    private SplashView mSplashView;

    UserPreferences mDatabase = new UserPreferencesImpl();

    public DefaultPresenter(DefaultView mDefaultView) {
        this.mDefaultView = mDefaultView;
    }

    public DefaultPresenter(SplashView mSplashView) {
        this.mSplashView = mSplashView;
    }

    public void checkUserIsLogin() {
        if (mDatabase.isUserLogin()) {
            mSplashView.showMainActivity();
        } else {
            mSplashView.showLoginActivity();
        }

    }

    public void checkReferral(String referral_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("sponsorId", referral_id.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.verifySponsor);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Checking referral id...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess("1", body.getMessage() + "");
                            } else {
                                mDefaultView.onSuccess("0", body.getMessage() + "");
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getReferralIncome(String device_id, int page, String Clear) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("page", page);
            post_data.put("token", mDatabase.getToken());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.myReferralIncome);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Please wait for a while");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    try {
                        mDefaultView.onHideDialog();
                        if (response.isSuccessful() && response.code() == 200) {
                            DefaultResponse body = response.body();
                            if (body != null) {
                                if (body.getSuccess() == 1) {
                                    mDefaultView.onSuccess(body.getData());
                                } else {
                                    mDefaultView.onError(body.getMessage());
                                }
                            }
                        } else {
                            mDefaultView.onError("Error bad url");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError(t.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void totalReferrals(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.totalReferralsNew);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            Log.d("encrypted",encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
//                                Log.d("TAG_DATA", "onResponse: "+body.getData());
                                mDefaultView.onSuccess(body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error bad url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError(t.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doRegister(String email, String full_name, String mobile_no, String password, String sponser_id) {
        // Mock  I'm creating a handler to delay the answer a couple of seconds
        new Handler().postDelayed(() -> {
            if (TextUtils.isEmpty(full_name.trim())) {
                mDefaultView.onError("Please enter your full name");
                return;
            }

            if (TextUtils.isEmpty(mobile_no.trim())) {
                mDefaultView.onError("Please enter your mobile number");
                return;
            }
            if (TextUtils.isEmpty(email.trim())) {
                mDefaultView.onError("Please enter your email id");
                return;
            }

            if (TextUtils.isEmpty(password.trim())) {
                mDefaultView.onError("Please enter your password");
                return;
            }

            if (password.trim().length() < 6) {
                mDefaultView.onError("Password length should be between 6 to 25 digits");
                return;
            }

            try {
                JSONObject post_data = new JSONObject();
                post_data.put("referral", "");
                post_data.put("name", full_name.trim());
                post_data.put("mobile", mobile_no.trim());
                post_data.put("email", email.trim());
                post_data.put("password", password.trim());
                post_data.put("sponser_id", sponser_id);
                JSONObject data = new JSONObject();
                data.put("request_url", ApiServices.registerUser);
                data.put("post_data", post_data);

                String data_final = data.toString();
                String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
                mDefaultView.onPrintLog(data_final);
                mDefaultView.onPrintLog(data_final);
                mDefaultView.onShowDialog("Creating your account...");
                Call<DefaultResponse> productsResponseObservable = MyApplication.getInstance()
                        .getApiInterface()
                        .defaultRequest(encrypted);
                productsResponseObservable.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                        mDefaultView.onHideDialog();

                        if (response.isSuccessful() && response.code() == 200) {

                            if (response.body() != null) {

                                if (response.body().getSuccess() == 1) {
                                    mDefaultView.onSuccess(response.body().getMessage());
                                } else {
                                    mDefaultView.onError(response.body().getMessage());
                                }
                            }
                        } else {
                            mDefaultView.onError("Error Bad Url");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError("Error during." + t.getMessage());
                        mDefaultView.onPrintLog(t.getMessage().toString());
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 0);
    }

    public void getCustomerCareNumber(String DTH) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("type", DTH);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getCustomerCareNumber);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();

            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during." + t.getMessage());

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supportContacts(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.supportContacts);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();

            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            Log.d("supportContacts",encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData(),"");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during." + t.getMessage());

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewMember(String device_id, String full_name, String email, String mobile_no,
                             String form_name, String member_des, String selected_state,
                             String selected_city, String land_mark, String address, String pincode,
                             String gst_no, String description, String membertype, String id) {
        // Mock  I'm creating a handler to delay the answer a couple of seconds

        new Handler().postDelayed(() -> {
            if (TextUtils.isEmpty(full_name.trim())) {
                mDefaultView.onError("Please enter full name");
                return;
            }
            if (TextUtils.isEmpty(email.trim())) {
                mDefaultView.onError("Please enter your email id");
                return;
            }

            if (TextUtils.isEmpty(mobile_no.trim())) {
                mDefaultView.onError("Please enter mobile no");
                return;
            }
            if (TextUtils.isEmpty(form_name.trim())) {
                mDefaultView.onError("Please enter firm name");
                return;
            }
           /* if (TextUtils.isEmpty(member_des.trim())) {
                mDefaultView.onError("Please enter member des");
                return;
            }*/
            if (selected_state.equals("Select State")) {
                mDefaultView.onError("Please select state");
                return;
            }
            if (selected_city.equals("Select City")) {
                mDefaultView.onError("Please select city");
                return;
            }

       /*     if (TextUtils.isEmpty(land_mark.trim())) {
                mDefaultView.onError("Please enter landmark");
                return;
            }
            if (TextUtils.isEmpty(address.trim())) {
                mDefaultView.onError("Please enter address");
                return;
            }
            if (TextUtils.isEmpty(pincode.trim())) {
                mDefaultView.onError("Please enter pincode");
                return;
            }
            if (TextUtils.isEmpty(gst_no.trim())) {
                mDefaultView.onError("Please enter gst no");
                return;
            }
            if (TextUtils.isEmpty(description.trim())) {
                mDefaultView.onError("Please enter description");
                return;
            }*/

            try {
                JSONObject post_data = new JSONObject();
                post_data.put("token", mDatabase.getToken().trim());
                post_data.put("device_id", device_id.trim());
                post_data.put("memberName", full_name.trim());
                post_data.put("mobileNumber", mobile_no.trim());
                post_data.put("emailAddress", email.trim());
                post_data.put("sponserId", "");
                post_data.put("stateId", selected_state.trim());
                post_data.put("cityId", selected_city.trim());
                post_data.put("shopName", form_name.trim());
                post_data.put("landmark", land_mark.trim());
                post_data.put("pincode", pincode.trim());
                post_data.put("fullAddress", address.trim());
                post_data.put("gstNumber", gst_no.trim());
                post_data.put("memberDescription", description.trim());
                post_data.put("membertype", membertype.trim());
                post_data.put("packageid", id.trim());

               /* String sponsor_id, String full_name,  String email, String mobile_no,
                        String form_name, String member_des , String selected_state,
                        String selected_city, String land_mark,
                        String address, String pincode,
                        String gst_no, String description*/

                JSONObject data = new JSONObject();
                data.put("request_url", ApiServices.addNewMember);
                data.put("post_data", post_data);

                String data_final = data.toString();
                String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
                mDefaultView.onPrintLog(data_final);
                mDefaultView.onPrintLog(data_final);
                mDefaultView.onShowDialog("Creating your account...");
                Call<DefaultResponse> productsResponseObservable = MyApplication.getInstance()
                        .getApiInterface()
                        .defaultRequest(encrypted);
                productsResponseObservable.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                        mDefaultView.onHideDialog();

                        if (response.isSuccessful() && response.code() == 200) {

                            if (response.body() != null) {
                                if (response.body().getSuccess() == 1) {
                                    mDefaultView.onSuccess(response.body().getMessage(), "");
                                } else {
                                    mDefaultView.onError(response.body().getMessage());
                                }
                            }
                        } else {
                            mDefaultView.onError("Error Bad Url");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError("Error during." + t.getMessage());
                        mDefaultView.onPrintLog(t.getMessage().toString());
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 0);
    }

    public void otpValidate(String email, String full_name, String mobile_no,
                            String password, String otp, String device_id, String sponser_id) {
        try {

            if (TextUtils.isEmpty(otp.trim())) {
                mDefaultView.onShowToast("Please enter otp");
                return;
            }

            JSONObject post_data = new JSONObject();
            String details = "VERSION RELEASE " + Build.VERSION.RELEASE
                    + ", VERSION SDK NUMBER " + Build.VERSION.SDK_INT
                    + ", BOARD " + Build.BOARD
                    + ", MANUFACTURER " + Build.MANUFACTURER
                    + ", MODEL " + Build.MODEL;
            post_data.put("sponser_id", sponser_id);
            post_data.put("name", full_name.trim());
            post_data.put("email", email.trim());
            post_data.put("password", password.trim());
            post_data.put("mobile", mobile_no.trim());
            post_data.put("otp", otp.trim());
            post_data.put("device_id", device_id.trim());
            post_data.put("device_information", details.trim());

            JSONObject data = new JSONObject();
            Log.d("post_data", post_data.toString());
            data.put("request_url", ApiServices.validateRegisterOtp);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Verifying Otp...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commissionReport(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.commissionReport);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchBalance(String token, String device_id, Animation rotation) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("fcm_token", token.trim());
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fetchBalance);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            if (rotation == null) {
                mDefaultView.onShowDialog("Loading...");
            }
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (rotation != null) {
                        rotation.cancel();
                    }
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
                                    JSONObject jsonObject = new JSONObject(body.getData());
                                    mDefaultView.onSuccessOther(jsonObject.toString(), "fetchBalance");
                                } else {
                                    mDefaultView.onSuccessOther(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    if (rotation != null) {
                        rotation.cancel();
                    }
                    mDefaultView.onHideDialog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void referAndEarnFaqs(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.referAndEarnFaqs);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onShowDialog("Loading...");

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
                                    JSONArray jsonObject = new JSONArray(body.getData());
                                    mDefaultView.onSuccess(jsonObject.toString());
                                } else {
                                    mDefaultView.onError(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void myReferrals(String device_id, String page, String clear) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("page", page.trim());
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.myReferrals);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Please wait for a while");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    try {
                        mDefaultView.onHideDialog();
                        if (response.isSuccessful() && response.code() == 200) {
                            DefaultResponse body = response.body();
                            if (body != null) {
                                if (body.getSuccess() == 1) {
                                    mDefaultView.onSuccess(body.getData(), clear);
                                } else {
                                    mDefaultView.onError(body.getMessage());
                                }
                            }
                        } else {
                            mDefaultView.onError("Error Bad Url");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError("Try Again" + t.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void myReferralIncome(String device_id, String page, String clear,String earning_type) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("page", page.trim());
            post_data.put("device_id", device_id.trim());
            post_data.put("earning_type", earning_type.trim());
            post_data.put("token", mDatabase.getToken().trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.myReferralIncome);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Please wait for a while");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    try {
                        mDefaultView.onHideDialog();
                        if (response.isSuccessful() && response.code() == 200) {
                            DefaultResponse body = response.body();
                            if (body != null) {
                                if (body.getSuccess() == 1) {
                                    mDefaultView.onSuccess(body.getData(), clear);
                                } else {
                                    mDefaultView.onError(body.getMessage());
                                }
                            }
                        } else {
                            mDefaultView.onError("Error Bad Url");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError("Try Again" + t.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forgotPassword(String mobile, String otp, String password) {
        try {

            if (TextUtils.isEmpty(mobile.trim())) {
                mDefaultView.onError("Please enter register mobile number");
                return;
            }

            JSONObject post_data = new JSONObject();
            post_data.put("mobile", mobile.trim());
            post_data.put("otp", otp.trim());
            post_data.put("password", password.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.forgotPassword);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Login...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getMessage());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forgotPasswordOtp(String mobile) {
        try {
            if (TextUtils.isEmpty(mobile.trim())) {
                mDefaultView.onError("Please enter register mobile number");
                return;
            }

            JSONObject post_data = new JSONObject();
            post_data.put("mobile", mobile.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.forgotPasswordOtp);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Login...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(mobile);
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(String device_id, String old_password, String new_password) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("old_password", old_password.trim());
            post_data.put("new_password", new_password.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("device_id", device_id.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.updatePassword);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Login...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
//                                mDefaultView.onSuccess("", body.getMessage() + "");
                                mDefaultView.onShowToast(body.getMessage());
                            } else {
                                mDefaultView.onSuccess("", body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transferBalance(String device_id, String amount, String type, String mobile, String position, String mpin) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("amount", amount.trim());
            post_data.put("type", type.trim());
            post_data.put("mobile", mobile.trim());
            post_data.put("position", position.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("device_id", device_id.trim());
            post_data.put("m_pin", mpin.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.transferBalance);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Login...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getMessage() + "", body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPassword() {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("mobile", mDatabase.getUserData().getMobile().trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.resetPassword);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Login...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess("", body.getMessage() + "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPaymentSetting(String device_id, String title, String mpin) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("mpin", mpin);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getPaymentSetting);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
//                                Log.d("TAG_DATA", "onResponse12: "+body.getData());
                                mDefaultView.onSuccessOther(body.getData(), title + "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPaymentSetting2(String device_id, String title) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getPaymentSetting);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
//            mDefaultView.onShowDialog("Loading...");
            Log.d("TAG_DATA", "getPaymentSetting2: " + encrypted);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                Log.d("TAG_DATA", "onResponse12: " + body.getData());

                                mDefaultView.onSuccessOther(body.getData(), title + "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateChecksum(String amount, String device_id, String isCheck) {
        try {
            if (TextUtils.isEmpty(amount.trim())) {
                mDefaultView.onShowToast("Please enter amount");
                return;
            }

            JSONObject post_data = new JSONObject();

            JSONObject data = new JSONObject();

            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("amount", amount);
            post_data.put("bypass_min_balance_check", isCheck);
            data.put("request_url", ApiServices.generateChecksum);
            data.put("post_data", post_data);

            mDefaultView.onPrintLog(post_data.toString() + " " + data.toString());

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Adding...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData(), "paytm");
                            } else {
                                mDefaultView.onError(body.getMessage());
//                                Log.d("TAG_DATA", "onResponse: "+body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateOpenUpi(String amount, String device_id) {
        try {

            if (TextUtils.isEmpty(amount.trim())) {
                mDefaultView.onShowToast("Please enter amount");
                return;
            }

            JSONObject post_data = new JSONObject();

            JSONObject data = new JSONObject();

            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("amount", amount);
            data.put("request_url", ApiServices.generateChecksum);
            data.put("post_data", post_data);

            mDefaultView.onPrintLog(post_data.toString() + " " + data.toString());

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Adding...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), amount);
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void payTMResponse(String order_id, JSONObject response) {
        try {
            JSONObject post_data = new JSONObject();
            JSONObject data = new JSONObject();
            post_data.put("id", mDatabase.getUserId());
            post_data.put("orderid", order_id);
            if (response.toString().equals("{}")) {
                post_data.put("status", "CANCELLED");
            } else {
                post_data.put("status", "SUCCESS");
                post_data.put("response", response);
            }
            data.put("request_url", ApiServices.payTMResponse);
            data.put("post_data", post_data);
            mDefaultView.onPrintLog(post_data.toString() + " " + data.toString());
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Verifying...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                try {
                                    JSONObject jsonObject = new JSONObject(body.getData());
                                    mDefaultView.onPrintLog(jsonObject + "");
                                    mDefaultView.onSuccess(jsonObject.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onShowToast("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateSecurityCode(String old_password, String new_password) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("old_password", old_password.trim());
            post_data.put("new_password", new_password.trim());
            post_data.put("username", mDatabase.getUsername().trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.updateSecurityCode);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Login...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess("", body.getMessage() + "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardData(String token, String device_id, Animation rotation) {
        try {
            mDefaultView.onPrintLog("token :  " + token);
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("fcm_token", token.trim());
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.dashboardData);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            if (rotation == null) {
                mDefaultView.onShowDialog("Loading...");
            }
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (rotation != null) {
                        rotation.cancel();
                    }
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        Log.d("TAG_DATA", "onResponse: " + body.getData());
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
//                                    Log.d("TAG_DATA", "onResponse: "+body.getData());

                                    JSONObject jsonObject = new JSONObject(body.getData() + "\n" + body.getMessage());
                                    mDefaultView.onPrintLog(jsonObject.toString());
                                    mDefaultView.onSuccess(jsonObject.toString(), "");
                                } else {
//                                    Log.d("TAG_DATA", "onResponse: "+body.getData()+"\n"+body.getMessage());
                                    mDefaultView.onSuccessOther(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    if (rotation != null) {
                        rotation.cancel();
                    }
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardDataWithoutRefresh(String token, String device_id, Animation rotation) {
        try {
            mDefaultView.onPrintLog("token :  " + token);

            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("fcm_token", token.trim());
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.dashboardData);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Log.d("fgjksfgj", encrypted );
            if (rotation != null) {
                mDefaultView.onShowDialog("Loading...");
            }
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (rotation != null) {
                        rotation.cancel();
                    }
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
//                        Log.d("TAG_DATA", "onResponse: "+body.getData());
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
//                                    Log.d("TAG_DATA", "onResponse: "+body.getData());
                                    JSONObject jsonObject = new JSONObject(body.getData() + "\n" + body.getMessage());
                                    mDefaultView.onSuccess(jsonObject.toString(), "");
                                } else {
//                                    Log.d("TAG_DATA", "onResponse: "+body.getData()+"\n"+body.getMessage());
                                    mDefaultView.onSuccessOther(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    if (rotation != null) {
                        rotation.cancel();
                    }
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onlineDepositHistory(String device_id,String from, String to, String ref_no_) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("from", from.trim());
            post_data.put("to", to.trim());
            post_data.put("keyword", ref_no_.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.onlineDepositHistory);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");


            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
                                    JSONObject jsonObject = new JSONObject(body.getData());
                                    mDefaultView.onPrintLog(jsonObject.toString());
                                    mDefaultView.onSuccess(jsonObject.toString());
                                } else {
                                    mDefaultView.onSuccessOther(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recentSuccessDepositHistory(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.recentSuccessDepositHistory);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");


            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
                                    JSONObject jsonObject = new JSONObject(body.getData());
                                    mDefaultView.onPrintLog(jsonObject.toString());
                                    mDefaultView.onSuccess(jsonObject.toString());
                                } else {
                                    mDefaultView.onSuccessOther(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fundRequestHistory(String device_id) {
        try {

            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fundRequestHistory);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
                                    JSONObject jsonObject = new JSONObject(body.getData());
                                    mDefaultView.onPrintLog(jsonObject.toString());
                                    mDefaultView.onSuccess(jsonObject.toString());
                                } else {
                                    mDefaultView.onError(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataForNewMember(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.loadDataForNewMember);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
                                    mDefaultView.onSuccess(body.getData());
                                } else {
                                    mDefaultView.onError(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchCities(String device_id, String state_code) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("state", state_code.trim());
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fetchCities);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
                                    mDefaultView.onSuccessOther(body.getData());
                                } else {
                                    mDefaultView.onError(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bankDetails(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.bankDetails);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData());
                            } else {
                                mDefaultView.onSuccessOther(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateCashfreeChecksum(String device_id, String amount) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("amount", amount);

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.generateCashfreeChecksum);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getMessage(), body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fundRequest(String device_id, String mode,
                            String bank, String amount,
                            String ref_no, String comment) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("mode", mode);
            post_data.put("bank", bank);
            post_data.put("amount", amount);
            post_data.put("ref_no", ref_no);
            post_data.put("comment", comment);

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fundRequest);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getMessage());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProfile(String name, String email) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("mobile", mDatabase.getUsername().trim());
            post_data.put("name", name.trim());
            post_data.put("email", email.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.updateProfile);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), body.getMessage() + "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doComplaint(String device_id, String description, String order_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("description", description.trim());
            post_data.put("order_id", order_id.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.doComplaint);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getMessage() + "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void payupayments(String device_id, String amount) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("amount", amount.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.payupayments);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(encrypted);

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "payMethod");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void orderDetailspending(String device_id, String order_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("orderid", order_id.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.orderDetailsNew);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "orderDetailsPending");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void orderDetails(String device_id, String order_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("orderid", order_id.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.orderDetailsNew);
            data.put("post_data", post_data);
            System.out.println("storId==" + post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    Log.d("doMobileRecharge", "orderDetails "+response.body().getData());
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {

                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "orderDetails");

                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void phonepeDynamicQR(String device_id, String amount, String isCheck) {
        try {
            JSONObject post_data = new JSONObject();
            JSONObject data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("amount", amount);
            post_data.put("bypass_min_balance_check", isCheck);
            data.put("request_url", ApiServices.phonepeDynamicQR);

            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onShowDialog("Adding...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData(), "phonePayNew");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hdfcbankDynamicQR(String device_id, String amount, String isCheck) {
        try {
            JSONObject post_data = new JSONObject();
            JSONObject data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("amount", amount);
            post_data.put("bypass_min_balance_check", isCheck);
            data.put("request_url", ApiServices.hdfcbankDynamicQR);

            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Log.d("TAG_DATA", "hdfcbankDynamicQR: "+encrypted);
            mDefaultView.onShowDialog("Adding...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData(), "hdfc");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iciciDynamicQR(String device_id, String amount, String phonePackage) {
        try {
            if (TextUtils.isEmpty(amount.trim())) {
                mDefaultView.onShowToast("Please enter amount");
                return;
            }
            JSONObject post_data = new JSONObject();
            JSONObject data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());
            post_data.put("amount", amount);
            post_data.put("phonepackage", phonePackage);


            data.put("request_url", ApiServices.iciciDynamicQR);
            data.put("post_data", post_data);
            System.out.println("post_data==" + post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onShowDialog("Adding...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "PhonePay");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateRazorPayOrder(String device_id, String amount, String isCheck) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("amount", amount.trim());
            post_data.put("bypass_min_balance_check", isCheck);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.generateRazorPayOrderNew);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(encrypted);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "razorPayMethod");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendOrderId(String device_id, String order_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("order_id", order_id);
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.payuresponse);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), body.getMessage());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void historyCircleOperators(String device_id, String type) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("type", type);
            post_data.put("token", mDatabase.getToken());
            post_data.put("device_id", device_id + "");

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.historyCircleOperators);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findOperatorCircle(String device_id, String mobile) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("mobile", mobile);
            post_data.put("token", mDatabase.getToken());
            post_data.put("device_id", device_id + "");

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.findOperatorCircle);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
//                                Log.d("TAG_DATA", "onResponse: "+body.getData());
                                mDefaultView.onSuccessOther(body.getData(), "operator_circle");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void doMobileRecharge(String number, String operator, String amount, String type,
                                 String std_code, String sub_division, String circle,
                                 String ac_number, String device_id, String mpin, Boolean isUsingCashbackPoints) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("number", number);
            post_data.put("operator", operator);
            post_data.put("amount", amount);
            post_data.put("type", type);
            post_data.put("std_code", std_code);
            post_data.put("sub_division", sub_division);
            post_data.put("circle", circle);
            post_data.put("token", mDatabase.getToken());
            post_data.put("device_id", device_id + "");
            post_data.put("account_no", ac_number);
            post_data.put("m_pin", mpin);
            post_data.put("isUsingCashbackPoints", isUsingCashbackPoints.toString().trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.doMobileRecharge);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData(), body.getMessage());
                            } else if (body.getSuccess() == 2) {
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("discount", body.getDiscount_amount());
                                    object.put("required", body.getRequired_amount());
                                    object.put("used_points",body.getUsable_points());
                                    mDefaultView.onSuccessOther(object.toString(), "INSUFFICIENT");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Request");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getReferrals() {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("mobile", mDatabase.getUserData().getMobile().trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getReferrals);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Please wait for a while");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    try {
                        mDefaultView.onHideDialog();
                        if (response.isSuccessful() && response.code() == 200) {
                            DefaultResponse body = response.body();
                            if (body != null) {
                                if (body.getSuccess() == 1) {
                                    mDefaultView.onSuccess(body.getData());
                                } else {
                                    mDefaultView.onError(body.getMessage());
                                }
                            }
                        } else {
                            mDefaultView.onShowToast("Error Bad Url");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError("Try Again" + t.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userPassbook(String device_id, String page, String from, String to, String ref_no_, String clear) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("page", page.trim());
            post_data.put("from", from.trim());
            post_data.put("to", to.trim());
            post_data.put("keyword", ref_no_.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.walletHistory);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
//            Log.d("TAG_DATA", "userPassbook: "+data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), clear);
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void memberWalletHistory(String device_id, String mobile, String page, String from, String to, String ref_no_, String clear) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("mobile", mobile.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("page", page.trim());
            post_data.put("from", from.trim());
            post_data.put("to", to.trim());
            post_data.put("keyword", ref_no_.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.memberWalletHistory);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), clear);
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void memberProfileDetail(String device_id, String mobile) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("mobile", mobile.trim());
            post_data.put("token", mDatabase.getToken().trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.memberProfileDetail);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void membersList(String device_id, String page, String from, String to, String ref_no_, String clear) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("page", page.trim());
            post_data.put("from", from.trim());
            post_data.put("to", to.trim());
            post_data.put("keyword", ref_no_.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.membersList);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), clear);
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userRechargeHistory(String device_id, String page, String from, String to, String ref_no_, String clear, String refresh) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("page", page.trim());
            post_data.put("from", from.trim());
            post_data.put("to", to.trim());
            post_data.put("keyword", ref_no_.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.userRechargeHistory);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), clear);
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onError("Error during " + t.getMessage());
                    Log.d("TAG_DATA", "onFailure: " + t.getMessage());
                    mDefaultView.onHideDialog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void complainHistory(String device_id, String page, String from, String to, String ref_no_, String clear, String refresh) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("page", page.trim());
            post_data.put("from", from.trim());
            post_data.put("to", to.trim());
            post_data.put("keyword", ref_no_.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.complainHistory);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            if (refresh.equals("No")) {
                mDefaultView.onShowDialog("Loading...");
            }
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), clear);
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void rechargeDetails(String device_id, String order_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("order_id", order_id.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.rechargeDetails);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    //  mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void forgotMpin(String mobile, String otp, String cnfrimMpin, String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("mobile", mobile.trim());
            post_data.put("otp", otp.trim());
            post_data.put("new_mpin", cnfrimMpin.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("device_id", device_id);
            UserData userData = mDatabase.getUserData();
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.forgotMpin);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Please wait for a while");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {

                    try {
                        mDefaultView.onHideDialog();
                        if (response.isSuccessful() && response.code() == 200) {
                            DefaultResponse body = response.body();
                            if (body != null) {
                                if (body.getSuccess() == 1) {
                                    mDefaultView.onShowToast(body.getMessage());
                                } else {
                                    mDefaultView.onError(body.getMessage());

                                }
                            }
                        } else {
                            mDefaultView.onShowToast("Error Bad Url");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onShowToast("Try Again" + t.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSecCode(String oldmPin, String newmPin, String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("old_mpin", oldmPin.trim());
            post_data.put("new_mpin", newmPin.trim());
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken().trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.updateMpin);
            data.put("post_data", post_data);
            String data_final = data.toString();
            mDefaultView.onShowDialog("Loading...");
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    try {
                        mDefaultView.onHideDialog();
                        if (response.isSuccessful() && response.code() == 200) {
                            DefaultResponse body = response.body();
                            if (body != null) {
                                if (body.getSuccess() == 1) {
                                    mDefaultView.onShowToast(body.getMessage());
                                } else {
                                    mDefaultView.onError(body.getMessage());

                                }
                            }
                        } else {
                            mDefaultView.onHideDialog();
                            mDefaultView.onShowToast("Error Bad Url");
                        }
                    } catch (Exception e) {
                        mDefaultView.onHideDialog();
                        Log.d("TAG_DATA", "onResponse: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onShowToast("Try Again" + t.getMessage());
                    } catch (Exception e) {
                        Log.d("TAG_DATA", "onFailure: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchPrepaidPlans(String device_id, String opid, String cirid, String mobile) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("operator", opid);
            post_data.put("circle", cirid);
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            post_data.put("mobile", mobile);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fetchPrepaidPlans);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
//            Log.d("TAG_DATA", "fetchPrepaidPlans: "+data_final);
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
//            Log.d("TAG_DATA", "fetchPrepaidPlans: "+encrypted);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void fetchPrepaidPlanDetailsLocal(String device_id, String operator, String rs) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("operator", operator);
            post_data.put("rs", rs);
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fetchPrepaidPlanDetailsLocal);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
//            Log.d("TAG_DATA", "fetchPrepaidPlans: "+data_final);
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Log.d("fetchPrepaidPlanDetailsLocal", encrypted);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getUserUsableCashbackPoints(String device_id, String operator, String rs) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("operator", operator);
            post_data.put("rs", rs);
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getUserUsableCashbackPoints);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
//            Log.d("TAG_DATA", "fetchPrepaidPlans: "+data_final);
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Log.d("getUserUsableCashbackPoints", encrypted);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void fetchRofferPlans(String device_id, String opid, String cirid, String mobile) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("operator", opid);
            post_data.put("circle", cirid);
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            post_data.put("mobile", mobile);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fetchRofferPlans);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void myCommissionPackage(String device_id, String type, String id) {
        try {
            JSONObject post_data = new JSONObject();
            if (type.equals("My Commission Package")) {
                post_data.put("device_id", device_id.trim());
                post_data.put("token", mDatabase.getToken());
                post_data.put("packageid", "");
            } else {
                post_data.put("device_id", device_id.trim());
                post_data.put("token", mDatabase.getToken());
                post_data.put("packageid", id.trim());
            }

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.myCommissionPackage);
            data.put("post_data", post_data);


            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
                                    mDefaultView.onSuccess(body.getData());
                                } else {
                                    mDefaultView.onError(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMpin(String device_id, String mpin) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("mpin", mpin.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("device_id", device_id.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.setMpin);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onShowDialog("Login...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            mDefaultView.onSuccessOther(body.getMessage(), "MpinSucess");
//                            if (body.getSuccess() == 1) {
//                                mDefaultView.onSuccessOther(body.getMessage(), "MpinSucess");
//                            } else {
//                                mDefaultView.onSuccessOther(body.getMessage(),"MpinSucess");
//                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void myDownlinePackages(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.myDownlinePackages);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            try {
                                if (body.getSuccess() == 1) {
                                    mDefaultView.onSuccess(body.getData());
                                } else {
                                    mDefaultView.onError(body.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchDthPlans(String device_id, String opid) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("operator", opid);
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fetchDthPlans);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dthCustomerInfo(String device_id, String number, String opid) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("operator", opid);
            post_data.put("number", number);
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.dthCustomerInfo);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void whatsappAlerts(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.whatsappAlerts);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData(), "whatsapp");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCommissions(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getCommissions);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBbpsPayBills(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getBbpsPayBills);
            data.put("post_data", post_data);
//            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "bbps");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getAllRechargeServices(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getAllRechargeServices);
            data.put("post_data", post_data);
//            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "allServices");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getRechargeSlider(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getRechargeSlider);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData(), "slider");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cashbackPointsHistory(String device_id, String from, String to, String keyword) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            post_data.put("from", from.trim());
            post_data.put("to", to.trim());
            post_data.put("keyword", keyword.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.cashbackPointsHistory);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData(), "slider");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void operatorsDetails(String device_id, String opid) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("operator", opid);
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.operatorsDetails);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();

//            Log.d("TAG_DATA", "operatorsDetails: "+data_final);
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            Log.d("encrypted", encrypted);

            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
//                                Log.d("TAG_DATA", "operatorsDetails: "+body.getData());
                                mDefaultView.onSuccessOther(body.getData(), "slider");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchBillInfo(String device_id, String opid, String number, String field1, String field2) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("operator", opid);
            post_data.put("device_id", device_id);
            post_data.put("number", number);
            post_data.put("field1", field1);
            post_data.put("field2", field2);
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fetchBillInfo);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchBillPostpaidInfo(String device_id, String opid, String number, String field1) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("operator", opid);
            post_data.put("device_id", device_id);
            post_data.put("number", number);
            post_data.put("field1", field1);
            post_data.put("token", mDatabase.getToken());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fetchBillInfo);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "postpaid");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doElectricityRecharge(String number, String operator, String amount, String type,
                                      String std_code, String sub_division, String circle,
                                      String ac_number, String device_id, String mpin) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("number", number);
            post_data.put("operator", operator);
            post_data.put("amount", amount);
            post_data.put("type", type);
            post_data.put("field2", std_code);
            post_data.put("sub_division", sub_division);
            post_data.put("circle", circle);
            post_data.put("token", mDatabase.getToken());
            post_data.put("device_id", device_id + "");
            post_data.put("account_no", ac_number);
            post_data.put("m_pin", mpin);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.doMobileRecharge);
            data.put("post_data", post_data);

            String data_final = data.toString();
            mDefaultView.onPrintLog(data_final);
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();

                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), body.getMessage());
                            } else if (body.getSuccess() == 2) {
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("discount", body.getDiscount_amount());
                                    object.put("required", body.getRequired_amount());
                                    mDefaultView.onSuccessOther(object.toString(), "INSUFFICIENT");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Request");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFeedback(String device_id, String message, Integer ratings) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            post_data.put("message", message);
            post_data.put("ratings", ratings);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getFeedback);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted)
                    .enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                            if (response.isSuccessful() && response.code() == 200) {
                                DefaultResponse body = response.body();
                                if (body != null) {
                                    if (body.getSuccess() == 1) {
                                        mDefaultView.onSuccessOther(body.getData());
                                    } else {
                                        mDefaultView.onError(body.getMessage());
                                    }
                                }
                            } else {
                                mDefaultView.onError("Error Bad Request");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                            Log.d("TAG_DATA", "onFailure: " + t.getMessage());
                            mDefaultView.onError(t.getMessage());
                        }
                    });

        } catch (Exception e) {
            Log.d("TAG_DATA", "getfeedback1: " + e.getMessage());
        }
    }

    public void topRankers(String device_id, String type) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            post_data.put("type", type);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.topRankers);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted)
                    .enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                            if (response.isSuccessful() && response.code() == 200) {
                                DefaultResponse body = response.body();
                                if (body != null) {
                                    if (body.getSuccess() == 1) {
                                        mDefaultView.onSuccessOther(body.getData());
                                    } else {
                                        mDefaultView.onError(body.getMessage());
                                    }
                                }
                            } else {
                                mDefaultView.onError("Error Bad Request");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                            Log.d("TAG_DATA", "onFailure: " + t.getMessage());
                            mDefaultView.onError(t.getMessage());
                        }
                    });

        } catch (Exception e) {
            Log.d("TAG_DATA", "getfeedback1: " + e.getMessage());
        }
    }

    public void getFollow(String device_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.getFollowersList);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Log.d("encrypted",encrypted);
            MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted)
                    .enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<DefaultResponse> call, @NonNull Response<DefaultResponse> response) {
                            if (response.isSuccessful() && response.code() == 200) {
                                DefaultResponse body = response.body();
                                if (body != null) {
                                    if (body.getSuccess() == 1) {
                                        mDefaultView.onSuccessOther(body.getData(), "follows");
                                    } else {
                                        mDefaultView.onError(body.getMessage());
                                    }
                                }
                            } else {
                                mDefaultView.onError("Error Bad Request");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DefaultResponse> call, @NonNull Throwable t) {
                            Log.d("TAG_DATA", "onFailure: " + t.getMessage());
                            mDefaultView.onError(t.getMessage());
                        }
                    });

        } catch (Exception e) {
            Log.d("TAG_DATA", "getFollow: " + e.getMessage());
        }
    }

    public void doBbpsComplaint(String device_id, String description, String order_id) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("description", description.trim());
            post_data.put("order_id", order_id.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.doBbpsComplaint);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
//                                Log.d("TAG_DATA", "onSuccessOther: "+body.getMessage() + "\n"+body.getData());
                                mDefaultView.onSuccessOther(body.getMessage(), "bbpsComplaint");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchPrepaidPlansLocal(String device_id, String opid, String cirid, String mobile) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("operator", opid);
            post_data.put("circle", cirid);
            post_data.put("device_id", device_id);
            post_data.put("token", mDatabase.getToken());
            post_data.put("mobile", mobile);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.fetchPrepaidPlansLocal);
            data.put("post_data", post_data);
            mDefaultView.onShowDialog("Loading...");
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
//                                Log.d("TAG_DATA", "onResponse: "+body.getData());
                                mDefaultView.onSuccess(body.getData(), "");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void complaintTracking(String device_id, String complaintId) {
        try {
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("complaintId", complaintId.trim());

            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.complaintTracking);
            data.put("post_data", post_data);

            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);
            mDefaultView.onPrintLog(data_final);
            mDefaultView.onShowDialog("Loading...");
            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    mDefaultView.onHideDialog();
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccessOther(body.getData(), "tracking");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        mDefaultView.onError("Error Bad Url");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    mDefaultView.onHideDialog();
                    mDefaultView.onError("Error during " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginUser(String mobile, String password, String device_id) {
        try {
            if (TextUtils.isEmpty(mobile.trim())) {
                mDefaultView.onError("Please enter mobile no");
                return;
            }

            if (TextUtils.isEmpty(password.trim())) {
                mDefaultView.onError("Please enter password");
                return;
            }
            String details = "VERSION RELEASE " + Build.VERSION.RELEASE
                    + ", VERSION SDK NUMBER " + Build.VERSION.SDK_INT
                    + ", BOARD " + Build.BOARD
                    + ", MANUFACTURER " + Build.MANUFACTURER
                    + ", MODEL " + Build.MODEL;

            mDefaultView.onShowDialog("Login...");
            JSONObject post_data = new JSONObject();
            post_data.put("mobile", mobile.trim());
            post_data.put("password", password.trim());
            post_data.put("device_id", device_id.trim());
            post_data.put("device_information", details.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.loginUser);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        try {
                            mDefaultView.onError("Error Bad Url");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mDefaultView.onHideDialog();
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError("Error during " + t.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginVerifyUser(String mobile, String password, String device_id) {
        try {

            String details = "VERSION RELEASE " + Build.VERSION.RELEASE
                    + ", VERSION SDK NUMBER " + Build.VERSION.SDK_INT
                    + ", BOARD " + Build.BOARD
                    + ", MANUFACTURER " + Build.MANUFACTURER
                    + ", MODEL " + Build.MODEL;

            mDefaultView.onShowDialog("Login...");
            JSONObject post_data = new JSONObject();
            post_data.put("mobile", mobile.trim());
            post_data.put("otp", password.trim());
            post_data.put("device_id", device_id.trim());
            post_data.put("device_information", details.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.loginUserWithOtp);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData());
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        try {
                            mDefaultView.onError("Error Bad Url");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mDefaultView.onHideDialog();
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError("Error during " + t.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginOTPUser(String mobile) {
        try {

            if (TextUtils.isEmpty(mobile.trim())) {
                mDefaultView.onError("Please enter mobile no");
                return;
            }

            mDefaultView.onShowDialog("Login...");
            JSONObject post_data = new JSONObject();
            post_data.put("mobile", mobile.trim());
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.sendUserOtp);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "otp");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        try {
                            mDefaultView.onError("Error Bad Url");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mDefaultView.onHideDialog();
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError("Error during " + t.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkLoginWithOtpStatus() {
        try {
            mDefaultView.onShowDialog("Login...");
            JSONObject post_data = new JSONObject();
            post_data.put("token", "login_otp");
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.checkLoginWithOtpStatus);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "status");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        try {
                            mDefaultView.onError("Error Bad Url");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mDefaultView.onHideDialog();
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError("Error during " + t.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addInsufficientBalance(String device_id, String amount) {
        try {
            mDefaultView.onShowDialog("Login...");
            JSONObject post_data = new JSONObject();
            post_data.put("device_id", device_id.trim());
            post_data.put("token", mDatabase.getToken().trim());
            post_data.put("amount", amount);
            JSONObject data = new JSONObject();
            data.put("request_url", ApiServices.addInsufficientBalance);
            data.put("post_data", post_data);
            String data_final = data.toString();
            String encrypted = Java_AES_Cipher.encrypt(BaseMethod.key, BaseMethod.iv, data_final);

            Call<DefaultResponse> responseCall = MyApplication.getInstance()
                    .getApiInterface()
                    .defaultRequest(encrypted);
            responseCall.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(@NotNull Call<DefaultResponse> call, @NotNull Response<DefaultResponse> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        DefaultResponse body = response.body();
                        if (body != null) {
                            if (body.getSuccess() == 1) {
                                mDefaultView.onSuccess(body.getData(), "recharge");
                            } else {
                                mDefaultView.onError(body.getMessage());
                            }
                        }
                    } else {
                        try {
                            mDefaultView.onError("Error Bad Url");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mDefaultView.onHideDialog();
                }

                @Override
                public void onFailure(@NotNull Call<DefaultResponse> call, @NotNull Throwable t) {
                    try {
                        mDefaultView.onHideDialog();
                        mDefaultView.onError("Error during " + t.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
