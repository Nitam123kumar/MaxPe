package com.vuvrecharge.modules.activities.newActivities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.romainpiel.shimmer.Shimmer;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.databinding.PlanLayoutBinding;
import com.vuvrecharge.databinding.PlanMpinLayoutBinding;
import com.vuvrecharge.databinding.ScreenRechargePaymentLayoutBinding;
import com.vuvrecharge.databinding.TransactionDialogBinding;
import com.vuvrecharge.databinding.WalletTransactionBottonDialogBinding;
import com.vuvrecharge.modules.activities.AboutActivity;
import com.vuvrecharge.modules.activities.AfterDepositActivity;
import com.vuvrecharge.modules.activities.RechargeActivity;
import com.vuvrecharge.modules.activities.RechargeReportActivity;
import com.vuvrecharge.modules.adapter.SubscriptionAdapter;
import com.vuvrecharge.modules.model.PaymentModel;
import com.vuvrecharge.modules.model.ReportsData;
import com.vuvrecharge.modules.model.Subscriptions;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class PlanDetailsActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    private DefaultPresenter mDefaultPresenter;

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.imgOperator)
    ImageView imgOperator;
    @BindView(R.id.txtPhoneNumber)
    TextView txtPhoneNumber;
    @BindView(R.id.txtPhoneProvider)
    TextView txtPhoneProvider;
    @BindView(R.id.txtPhoneOrigin)
    TextView txtPhoneOrigin;
    @BindView(R.id.txtAmount)
    TextView txtAmount;
    @BindView(R.id.txtPlanDetails)
    TextView txtPlanDetails;
    @BindView(R.id.txtRecharge)
    TextView txtRecharge;
    @BindView(R.id.btnSubmit)
    TextView btnSubmit;
    @BindView(R.id.shimmerTextView)
    TextView shimmerTextView;
    @BindView(R.id.planValidityTV)
    TextView planValidityTV;
    @BindView(R.id.planDataTV)
    TextView planDataTV;
    @BindView(R.id.planDateTV)
    TextView planDateTV;
    @BindView(R.id.planSmsTV)
    TextView planSmsTV;
    @BindView(R.id.planVoiceTV)
    TextView planVoiceTV;
    @BindView(R.id.dataDetailsTV)
    TextView dataDetailsTV;
    @BindView(R.id.planUnlimitedTV)
    TextView planUnlimitedTV;
    @BindView(R.id.smsDetailsTV)
    TextView smsDetailsTV;
    @BindView(R.id.available_pointsTV)
    TextView available_pointsTV;

    DefaultView mDefaultView;

    String warning_message = "";

    String amount = null, number = null, urlProvider = null, provider = null, state = null,
            data = null, des = null, validity = null, circle = null, operator = null, type = null,talktime = null, desc = null,
            pageType = null, subscription = null;
    String m_id = "";
    String order_id = "", mPin = "";
    ReportsData mReportsData;
    Handler handler;
    Runnable runnable;
    BottomSheetDialog dialog2 = null;
    ArrayList<PaymentModel> list = new ArrayList<>();
    double releaseAmount = 0.000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        handler = new Handler();
        setStatusBarGradiant(this);
        amount = getIntent().getStringExtra("amount");
        number = getIntent().getStringExtra("number");
        urlProvider = getIntent().getStringExtra("url");
        provider = getIntent().getStringExtra("provider");
        state = getIntent().getStringExtra("state");
        validity = getIntent().getStringExtra("validity");
        des = getIntent().getStringExtra("des");
        desc = getIntent().getStringExtra("desc");
        talktime = getIntent().getStringExtra("talktime");
        data = getIntent().getStringExtra("data");
        operator = getIntent().getStringExtra("operator");
        circle = getIntent().getStringExtra("circle");
        pageType = getIntent().getStringExtra("pageType");
        txtRecharge.setText("Change");
        title.setText("Plan Summary");
        mDefaultView = this;
        mDefaultPresenter = new DefaultPresenter(this);

        planValidityTV.setVisibility(VISIBLE);
        planDateTV.setText(validity);
        planDataTV.setVisibility(VISIBLE);
        dataDetailsTV.setText(data);
        planVoiceTV.setVisibility(VISIBLE);
        planUnlimitedTV.setText(talktime);
        planSmsTV.setVisibility(VISIBLE);
        smsDetailsTV.setText(desc);

        try {
            UserData userData = mDatabase.getUserData();
            available_pointsTV.setText(userData.getCashbackPoints());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (pageType.equals("Prepaid")) {
            type = "Prepaid";
            subscription = getIntent().getStringExtra("subscription");
            mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
            txtPhoneOrigin.setText(state);
        } else {
            type = "DTH";
            txtPhoneOrigin.setText("");
            txtPhoneOrigin.setVisibility(GONE);
            mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
        }
        if (urlProvider != null) {
            Glide.with(this)
                    .load(urlProvider)
                    .into(imgOperator);
            txtPhoneNumber.setText(number);
            txtPhoneProvider.setText(provider);
            txtAmount.setText(amount);

            txtPlanDetails.setOnClickListener(v -> openPlan(amount, urlProvider, des, validity, data));

            btnSubmit.setOnClickListener(v -> rechargeDialog(number, circle, operator));
        }

        txtRecharge.setOnClickListener(v -> {
            if (type.equals("Prepaid")) {
                Intent intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("title", "Prepaid Recharge");
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("title", "DTH Recharge");
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "plandetails");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.main_wallet_shape);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    private void openPlan(String rs, String url, String desc, String validity, String data) {
        try {
            FrameLayout bottomSheet = null;
            BottomSheetDialog dialog = null;

            PlanLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.plan_layout, null, false);
            dialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(true);
            bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }
            if (subscription == null) {
                binding.recyclerView.setVisibility(GONE);
                binding.txt.setText("");
                binding.txtData.setVisibility(View.INVISIBLE);
                binding.txtDataValue.setVisibility(View.INVISIBLE);
                binding.txtValidityValue.setVisibility(View.INVISIBLE);
                binding.txtValidity.setVisibility(View.INVISIBLE);
            } else if (subscription.isEmpty() || subscription.equals("[]")) {
                binding.recyclerView.setVisibility(GONE);
                binding.txt.setText("");
                if (type.equals("DTH")) {
                    binding.txtData.setVisibility(GONE);
                    binding.txtDataValue.setVisibility(GONE);
                } else {
                    binding.txtDataValue.setVisibility(VISIBLE);
                    binding.txtData.setVisibility(VISIBLE);
                    binding.txtDataValue.setText(data);
                }
            } else {
                binding.txt.setText("ADDITIONAL BENEFITS");
                binding.recyclerView.setVisibility(VISIBLE);
                if (type.equals("DTH")) {
                    binding.txtData.setVisibility(GONE);
                    binding.txtDataValue.setVisibility(GONE);
                } else {
                    binding.txtDataValue.setVisibility(VISIBLE);
                    binding.txtData.setVisibility(VISIBLE);
                    binding.txtDataValue.setText(data);
                }
                try {
                    ArrayList<Subscriptions> list = new ArrayList<>();
                    JSONArray array = new JSONArray(subscription);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Subscriptions subscriptions = new Subscriptions();
                        subscriptions.setOtt_name(object.getString("ott_name"));
                        subscriptions.setOtt_desc(object.getString("ott_desc"));
                        subscriptions.setLogo(object.getString("logo"));
                        list.add(subscriptions);
                    }
                    SubscriptionAdapter adapter = new SubscriptionAdapter(list, this);
                    binding.recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.d("TAG_DATA", "openPlan: " + e.getMessage());
                }
            }


            binding.txtAmount.setText(rs);

            Glide.with(this)
                    .load(url)
                    .into(binding.imgProvider);

            binding.txtDes.setText(desc);
            binding.txtValidityValue.setText(validity);

            binding.btnSubmit.setVisibility(GONE);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void rechargeDialog(String phone, String circle, String operator) {
//        try {
//            PlanMpinLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
//                    R.layout.plan_mpin_layout, null, false);
//
//            Dialog dialog = new Dialog(getActivity(), R.style.CustomAlertDialog);
//            dialog.setContentView(binding.getRoot());
//
//            dialog.setCancelable(true);
//
//            if (warning_message.isEmpty()) {
//                binding.warningMessage.setVisibility(GONE);
//            } else {
//                binding.warningMessage.setVisibility(VISIBLE);
//                binding.warningMessage.setText(warning_message);
//            }
//
//            binding.mpinTxt.requestFocus();
//            binding.mpinTxt.setShowSoftInputOnFocus(true);
//            binding.mpinTxt.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if (!s.toString().isEmpty()) {
//                        mPin = s.toString();
//                    }
//                }
//            });
//            Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//            binding.close.setOnClickListener(v -> {
//                dialog.dismiss();
//                hideKeyBoard(binding.mpinTxt);
//            });
//            binding.confirm.setOnClickListener(v -> {
//                mPin = binding.mpinTxt.getText().toString();
//                if (TextUtils.isEmpty(mPin)) {
//                    Toasty.error(getActivity(), "Please enter your M Pin", Toasty.LENGTH_LONG, false).show();
//                    return;
//                }
//                if (mPin.length() != 4) {
//                    Toasty.error(getActivity(), "MPin length should be 4 digit", Toasty.LENGTH_LONG, false).show();
//                    return;
//                }
//
//                dialog.dismiss();
//                hideKeyBoard(binding.mpinTxt);
//                mDefaultPresenter.doMobileRecharge(phone + "", operator + "",
//                        amount + "", type + "",
//                        0 + "", 0 + "",
//                        circle + "", 0 + "", device_id + "", mPin + "");
//            });
//
//            dialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void rechargeDialog(String phone, String circle, String operator) {
        try {
            PlanMpinLayoutBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(getActivity()),
                    R.layout.plan_mpin_layout,
                    null,
                    false
            );

            Dialog dialog = new Dialog(getActivity(), R.style.CustomAlertDialog);
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(true);

            if (warning_message.isEmpty()) {
                binding.warningMessage.setVisibility(View.GONE);
            } else {
                binding.warningMessage.setVisibility(View.VISIBLE);
                binding.warningMessage.setText(warning_message);
            }

            EditText[] mpinDigits = new EditText[]{
                    binding.pinDigit1,
                    binding.pinDigit2,
                    binding.pinDigit3,
                    binding.pinDigit4
            };

            // Initially disable confirm button
            binding.confirm.setEnabled(false);
            binding.confirm.setBackgroundResource(R.drawable.proceed_to_pay);
            binding.confirm.setTextColor(getResources().getColor(R.color.black));

            TextWatcher pinWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    // Auto-focus jump
                    for (int i = 0; i < mpinDigits.length; i++) {
                        if (mpinDigits[i].getText().hashCode() == s.hashCode()) {
                            if (s.length() == 1 && i < mpinDigits.length - 1) {
                                mpinDigits[i + 1].requestFocus();
                            } else if (s.length() == 0 && i > 0) {
                                mpinDigits[i - 1].requestFocus();
                            }
                            break;
                        }
                    }

                    // Check if all are filled
                    boolean allFilled = true;
                    for (EditText et : mpinDigits) {
                        if (et.getText().toString().trim().isEmpty()) {
                            allFilled = false;
                            break;
                        }
                    }
                    if (allFilled) {
                        binding.confirm.setEnabled(true);
                        binding.confirm.setBackgroundResource(R.drawable.ad_money_button_shape);
                        binding.confirm.setTextColor(getResources().getColor(R.color.white));

                    } else {
                        binding.confirm.setEnabled(false);
                        binding.confirm.setBackgroundResource(R.drawable.proceed_to_pay);
                        binding.confirm.setTextColor(getResources().getColor(R.color.black));

                    }
                }
            };

            for (EditText et : mpinDigits) {
                et.addTextChangedListener(pinWatcher);
            }

            Objects.requireNonNull(dialog.getWindow())
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            binding.close.setOnClickListener(v -> {
                dialog.dismiss();
                hideKeyBoard(mpinDigits[0]);
            });

            binding.confirm.setOnClickListener(v -> {
                StringBuilder pinBuilder = new StringBuilder();
                for (EditText et : mpinDigits) {
                    String digit = et.getText().toString().trim();
                    if (digit.isEmpty()) {
                        Toasty.error(getActivity(), "Please enter your M Pin completely", Toasty.LENGTH_LONG, false).show();
                        return;
                    }
                    pinBuilder.append(digit);
                }

                mPin = pinBuilder.toString();

                if (mPin.length() != 4) {
                    Toasty.error(getActivity(), "MPin length should be 4 digits", Toasty.LENGTH_LONG, false).show();
                    return;
                }

                dialog.dismiss();
                hideKeyBoard(mpinDigits[0]);

                mDefaultPresenter.doMobileRecharge(
                        phone,
                        operator,
                        amount + "",
                        type + "",
                        "0",
                        "0",
                        circle,
                        "0",
                        device_id,
                        mPin
                );
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        BaseMethod.amount = amount.toString();
        BaseMethod.mobile = number.toString();
    }

    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    public void onSuccess(String message, String second_message) {
        try {
            if (second_message.equals("recharge")) {
                JSONObject jsonObject = new JSONObject(message);
                String orderid = jsonObject.getString("orderid");
                this.order_id = orderid;
                String uri = jsonObject.getString("upi_uri");
                Intent upiIntent = new Intent(Intent.ACTION_VIEW);
                upiIntent.setData(Uri.parse(uri.trim()));
                Intent chooser = Intent.createChooser(upiIntent, "Pay with...");
                startActivityForResult(chooser, 104, null);
            } else if (second_message.equals("orderDetails")) {
                Gson gson = new Gson();
                Type type_ = new TypeToken<ReportsData>() {
                }.getType();
                JSONObject object = new JSONObject(message);
                String payment_status = object.getString("payment_status");
                mReportsData = gson.fromJson(message, type_);
//                setData(mReportsData);
                mReportsData.setStatus(payment_status);
//                if (mReportsData.getStatus().equals("pending")) {
//                    handler.postDelayed(runnable, 10000);
//                }
//                runnable = () -> {
//                    if (mReportsData.getStatus().equals("pending")) {
//                        pendingstatus();
//                    }
//                };

                if (payment_status.toLowerCase().equals("pending")) {
                    openPendingDialog();
                } else if (payment_status.toLowerCase().equals("success")) {
                    mDefaultPresenter.doMobileRecharge(
                            number.trim(), operator, amount.trim(), type,
                            "0", "0",
                            circle, "0", device_id, mPin);
                } else if (payment_status.toLowerCase().equals("failed")) {
                    Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    openPendingDialog();
                }
            } else if (second_message.equals("orderDetailsPending")) {
                openPendingDialog();
            } else {
                JSONObject jsonObject = new JSONObject(message);
                warning_message = jsonObject.getString("message");
            }
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
            if (data_other.equals("INSUFFICIENT")) {
                openGatewaysDialog(data);
            } else {
                successDialog(data, data_other);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void successDialog(String data, String message) {
        try {
            TransactionDialogBinding _binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.transaction_dialog, null, false);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(_binding.getRoot());
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);

            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            String status = "";
            try {
                JSONObject jsonObject = new JSONObject(data);
                status = jsonObject.getString("status");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (status.toUpperCase().equals("FAILED")) {
                _binding.txtTitle.setText("Failed");
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("Oops!");
                _binding.txtSlug.setVisibility(GONE);
//                _binding.imgGif.setGifImageResource(R.drawable.animated_wrong);
                Glide.with(this).asGif().load(R.drawable.animated_wrong).into(_binding.imgGif);
                _binding.btnComplete.setBackgroundResource(R.drawable.failed_button);
            } else if (status.toUpperCase().equals("PENDING")) {
                _binding.btnComplete.setBackgroundResource(R.drawable.pending_button);
                _binding.txtTitle.setText("Pending");
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("CHILL!");
//                _binding.imgGif.setGifImageResource(R.drawable.animated_pending);
                Glide.with(this).asGif().load(R.drawable.animated_pending).into(_binding.imgGif);
            } else {
                _binding.btnComplete.setBackgroundResource(R.drawable.green_button);
                _binding.txtTitle.setText("Success");
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("Thank You!");
//                _binding.imgGif.setGifImageResource(R.drawable.animated_right);
                Glide.with(this).asGif().load(R.drawable.animated_right).into(_binding.imgGif);
            }

            String finalStatus = status;
            _binding.btnComplete.setOnClickListener(v -> {
                if (finalStatus.toUpperCase().equals("FAILED")) {
                    dialog.dismiss();
                } else {
                    try {
                        dialog.dismiss();
                        JSONObject jsonObject = new JSONObject(data);
                        Intent intent = new Intent(getActivity(), RechargeReportActivity.class);
                        intent.putExtra("operator_img", jsonObject.getString("operator_img"));
                        intent.putExtra("operator_dunmy_img", jsonObject.getString("operator_dunmy_img"));
                        intent.putExtra("mReportsData", data);
                        if (jsonObject.getString("status").toLowerCase().equals("success")) {
                            intent.putExtra("bps", "1");
                        } else {
                            intent.putExtra("bps", "0");
                        }
                        intent.putExtra("type", type);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShowDialog(String message) {
        showLoading(loading);
    }

    @Override
    public void onHideDialog() {
        hideLoading(loading);
    }

    @Override
    public void onShowToast(String message) {
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

    void pendingstatus() {
        mDefaultPresenter.orderDetailspending(device_id + "", order_id);
    }

    private void setData(ReportsData mReportsData) {
//        try {
//            if (mReportsData.getStatus().equals("pending")) {
//                handler.postDelayed(runnable, 10000);
//            }
//            if (mReportsData.getStatus().equals("SUCCESS")) {
//                handler.postDelayed(runnable, 10000);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void openGatewaysDialog(String data) {
        try {
            list.clear();
            ScreenRechargePaymentLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.screen_recharge_payment_layout, null, false);

            dialog2 = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            dialog2.setContentView(binding.getRoot());
            dialog2.setCancelable(true);
            changeStatusBarColor(dialog2);
            bottomSheet = dialog2.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }
            JSONObject json = new JSONObject(data);
            binding.txtTransactionAmountValue.setText("\u20b9" + txtAmount.getText().toString().trim());
            if (mDatabase != null) {
                if (mDatabase.getEarnings() != null) {
                    binding.txtWalletAmountValue.setText("₹" + mDatabase.getEarnings());
                }
            }
            binding.txtTotalDiscountValue.setText("₹" + json.getString("discount"));
            releaseAmount = (Double.parseDouble(txtAmount.getText().toString().trim()) - json.getDouble("discount"));
            binding.txtPayableAmtValue.setText("₹" + releaseAmount);

            binding.btnPay.setText("Add on ₹" + json.getInt("required"));
            binding.btnPay.setOnClickListener(v -> {
                try {
                    mDefaultPresenter.addInsufficientBalance(device_id, json.getString("required"));
                    dialog2.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            dialog2.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG_DATA", "addBalance: " + e.getMessage());
        }
    }

    Timer timer;

    private void openPendingDialog() {
        try {
            timer = new Timer();
            WalletTransactionBottonDialogBinding _binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.wallet_transaction_botton_dialog, null, false);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            bottomSheetDialog.setContentView(_binding.getRoot());
            bottomSheetDialog.setCancelable(false);
            changeStatusBarColor(bottomSheetDialog);
            bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }

            _binding.btnComplete.setBackgroundResource(R.drawable.pending_button);
            _binding.txtTitle.setText("Waiting for payment");
            _binding.txtMessage.setText("Your transaction is under processing.\nPlease do not press the back button.");
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            mDefaultPresenter.orderDetails(device_id, order_id);
                        }
                    }, 10000
            );

            _binding.btnComplete.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                bottomSheetDialog.cancel();
                timer.cancel();
                showPending("You can recharge after sometime.");
            });

            bottomSheetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 104) {
            if (resultCode == RESULT_OK) {
                mDefaultPresenter.orderDetails(device_id, order_id);
            } else {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }
}