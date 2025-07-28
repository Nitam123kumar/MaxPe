package com.vuvrecharge.modules.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.databinding.RechargeDialogBinding;
import com.vuvrecharge.databinding.ScreenRechargePaymentLayoutBinding;
import com.vuvrecharge.databinding.TransactionDialogBinding;
import com.vuvrecharge.databinding.WalletTransactionBottonDialogBinding;
import com.vuvrecharge.modules.adapter.SearchableSpinnerOperatorAdapter;
import com.vuvrecharge.modules.model.CircleData;
import com.vuvrecharge.modules.model.OperatorData;
import com.vuvrecharge.modules.model.PaymentModel;
import com.vuvrecharge.modules.model.ReportsData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.OperatorPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class BillActivity extends BaseActivity implements DefaultView,
        SearchableSpinnerOperatorAdapter.OnItemClickListeners,View.OnClickListener{

    /*
    * live id=186
    * local id = 186
    * */
    String id = "186";
    private DefaultPresenter mDefaultPresenter;
    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.wallet_amount)
    TextView wallet_amount;
//    @BindView(R.id.std_code_bg_layout)
//    TextInputLayout std_code_bg;
//    @BindView(R.id.account_no_bg_layout)
//    TextInputLayout account_no_bg;
//    @BindView(R.id.mobile_number_layout)
//    TextInputLayout mobile_number_layout;
    @BindView(R.id.std_code)
    EditText std_code;
    @BindView(R.id.mobile_number)
    EditText mobile_number;
    @BindView(R.id.account_no)
    EditText account_no;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.selectCircle)
    ConstraintLayout selectCircle;
    @BindView(R.id.txtOperator)
    TextView txtOperator;
    String string = "";
    ArrayList<String> operator_list = new ArrayList<>();
    ArrayList<String> circle_list = new ArrayList<>();
    String type = "";
    DefaultView mDefaultView;

    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.tvservice)
    TextView tvservice;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.help)
    TextView help;
    @BindView(R.id.imgBBPS)
    ImageView imgBBPS;
    @BindView(R.id.btnBillFetch)
    TextView btnBillFetch;
    AlertDialog dialog1;
    String m_id = "",order_id = "",mPin = "";
    ReportsData mReportsData;
    Handler handler;
    Runnable runnable;
    BottomSheetDialog dialog2 = null;
    ArrayList<PaymentModel> list = new ArrayList<>();
    String amt = "";
    double releaseAmount = 0.000;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mToolbar.setOnClickListener(this);
        string = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        mDefaultPresenter = new DefaultPresenter(this);
        title.setText(string);
        handler = new Handler();
        setStatusBarGradiant(this);
        txtOperator.setText("Google Play Gift Card");
        BaseMethod.amount = "";
        BaseMethod.mobile = "";
        help.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SupportActivity.class);
            startActivity(intent);
        });
        wallet_amount.setText("Your balance : \u20b9" + mDatabase.getEarnings());


        switch (string) {
            case "Electricity Bill":
                type = "Electricity";
                selected_operator = "Select Service Provider";
                std_code.setVisibility(GONE);
                account_no.setVisibility(VISIBLE);
                mobile_number.setHint("Consumer/Account/K Number");
//                account_no.setHint("Sub Division/Code (If any)");
                amount.setVisibility(VISIBLE);
                imgBBPS.setVisibility(VISIBLE);
                btnBillFetch.setVisibility(GONE);
                break;
            case "Landline Postpaid":
                selected_operator = "Select Operator";
                type = "Landline";
                std_code.setVisibility(GONE);
                account_no.setVisibility(GONE);
                amount.setVisibility(VISIBLE);
                mobile_number.setHint("Landline Number/User Id with STD code");
//                amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                imgBBPS.setVisibility(VISIBLE);
                btnBillFetch.setVisibility(GONE);
                break;
            case "Gas Bill":
                selected_operator = "Select Service Provider";
                type = "Gas";
                std_code.setVisibility(GONE);
                account_no.setVisibility(GONE);
                amount.setVisibility(VISIBLE);
                mobile_number.setHint("Customer Number");
//                amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                imgBBPS.setVisibility(VISIBLE);
                btnBillFetch.setVisibility(VISIBLE);
                break;
            case "Water Bill":
                selected_operator = "Select Service Provider";
                type = "Water";
                imgBBPS.setVisibility(VISIBLE);
                amount.setVisibility(VISIBLE);
                std_code.setVisibility(GONE);
                account_no.setVisibility(GONE);
                mobile_number.setHint("Consumer/Account/K Number");
//                amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                btnBillFetch.setVisibility(GONE);
                break;
            case "Purchase Gift cards":
                type = "GiftCards";
//                account_no_bg.setVisibility(GONE);
                account_no.setVisibility(GONE);
                InputFilter[] filters3 = new InputFilter[1];
                filters3[0] = new InputFilter.LengthFilter(10);
                mobile_number.setFilters(filters3);
//                mobile_number_layout.setHint("Mobile Number");
                mobile_number.setHint("Mobile Number");
//                std_code.setVisibility(GONE);
                std_code.setVisibility(GONE);
                tvservice.setVisibility(GONE);
                selected_operator = "Select Operator";
                imgBBPS.setVisibility(GONE);
                btnBillFetch.setVisibility(GONE);
                break;
        }
        switch (type) {
            case "Electricity":
            case "Gas":
            case "Water":
                operator_list.add("Select Service Provider");
                break;
            default:
                operator_list.add("Select Operator");
                break;
        }
        circle_list.add("Select Circle");

        submit.setBackgroundResource(R.drawable.proceed_to_pay);
        submit.setTextColor(getResources().getColor(R.color.black_4));
        submit.setTypeface(submit.getTypeface(), Typeface.BOLD);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    submit.setTextColor(Color.WHITE);
                    submit.setBackgroundResource(R.drawable.ad_money_button_shape);
                    submit.setTypeface(submit.getTypeface(), Typeface.BOLD);
                }else {
                    submit.setTextColor(getResources().getColor(R.color.black_4));
                    submit.setBackgroundResource(R.drawable.proceed_to_pay);
                    submit.setTypeface(submit.getTypeface(), Typeface.BOLD);
                }
            }
        });
        mDefaultView = this;
    }

    String selected_operator = "Select Operator";
    String selected_circle = "Select Circle";
    String selected_operator_str = "";
    String selected_circle_str = "";

    private void loadOperatorSpinner() {
        selectCircle.setOnClickListener(this::openDialog);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.main_wallet_shape);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }
    private void openDialog(View vi) {
        View view = getLayoutInflater().inflate(R.layout.searchable_spinner_layout, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog1 = builder.create();
        RecyclerView rv = view.findViewById(R.id.recyclerViewList);
        EditText editText = view.findViewById(R.id.editSearch);

        Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        view.findViewById(R.id.btnClose).setOnClickListener(v -> {
            dialog1.dismiss();
            dialog1.cancel();
        });

        SearchableSpinnerOperatorAdapter circleAdapter = new SearchableSpinnerOperatorAdapter(this, operatorDataList, this);
        rv.setAdapter(circleAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    ArrayList<OperatorData> data = new ArrayList<>();
                    for (OperatorData circleData: operatorDataList){
                        if (circleData.getName().toUpperCase().toLowerCase().contains(s.toString().toUpperCase().toLowerCase())){
                            data.add(circleData);
                        }
                    }
                    if (data == null || data.isEmpty()){
                        showToast("Not data found");
                        circleAdapter.searchData(operatorDataList);

                    }else {
                        circleAdapter.searchData(data);
                    }
                }
            }
        });
        dialog1.show();
    }

    @Override
    public void onItemClick(@NonNull OperatorData model, int position) {
        txtOperator.setText(model.getName());
        dialog1.cancel();
        dialog1.dismiss();
        if (!txtOperator.getText().toString().equals("Select Service Provider")){
            if (position == 0){
                selected_operator_str = model.getName();
//                selected_operator = model.getName();
                selected_operator = model.getId();
            }else {
                selected_operator_str = model.getName();
//                selected_operator = model.getName();
                selected_operator = model.getId();
            }
        }else {
            showError("Please Select service provider.");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyBoard(mobile_number);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "bill");
        try {
            wallet_amount.setText("Your balance : \u20b9" + mDatabase.getEarnings());
            System.out.println("Earning==" + mDatabase.getEarnings());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String message) {

    }

    String warning_message = "";

    @Override
    public void onSuccess(String message, String second_message) {
        try {
            if (second_message.equals("recharge")){
                JSONObject jsonObject = new JSONObject(message);
                String orderid = jsonObject.getString("orderid");
                this.order_id = orderid;
                String uri = jsonObject.getString("upi_uri");
                Intent upiIntent = new Intent(Intent.ACTION_VIEW);
                upiIntent.setData(Uri.parse(uri.trim()));
                Intent chooser = Intent.createChooser(upiIntent, "Pay with...");
                startActivityForResult(chooser, 104, null);
            }else if (second_message.equals("orderDetails")){
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

                if (payment_status.toLowerCase().equals("pending")){
                    openPendingDialog();
                }else if (payment_status.toLowerCase().equals("success")){
                    mDefaultPresenter.doMobileRecharge(
                            mobile_number.getText().toString().trim(),selected_operator,amount.getText().toString().trim(),type,
                            "0", "0",
                            selected_circle,"0",device_id, mPin,false);
                }else if (payment_status.toLowerCase().equals("failed")){
                    Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
                }else {
                    openPendingDialog();
                }
            }else if (second_message.equals("orderDetailsPending")){
                openPendingDialog();
            }else {
                JSONObject jsonObject = new JSONObject(message);
                Gson gson = new Gson();
                Type type_ = new TypeToken<List<OperatorData>>() {
                }.getType();
                List<OperatorData> operatorDataList = gson.fromJson(jsonObject.getString("operators"), type_);
                Type type_1 = new TypeToken<List<CircleData>>() {
                }.getType();
                List<CircleData> circleDataList = gson.fromJson(jsonObject.getString("circles"), type_1);
                warning_message = jsonObject.getString("message");
                this.operatorDataList = operatorDataList;
                this.circleDataList = circleDataList;

                operator_list = new ArrayList<>();
                circle_list = new ArrayList<>();

                switch (type) {
                    case "Electricity":
                    case "Gas":
                    case "Water":
                        operator_list.add("Select Service Provider");
                        break;
                    default:
                        operator_list.add("Select Operator");
                        break;
                }

                circle_list.add("Select Circle");

                for (OperatorData operatorData : operatorDataList) {
                    operator_list.add(operatorData.getName());
                }
                for (CircleData circleData : circleDataList) {
                    circle_list.add(circleData.getState_name());
                }
                loadOperatorSpinner();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void successDialog(String data, String message) {
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
                _binding.txtTitle.setText("Oops!");
//                _binding.txtMessage.setText("Your transaction has failed.\nPlease go back and try again.");
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("Oops!");
                _binding.txtSlug.setVisibility(GONE);
//                _binding.imgGif.setGifImageResource(R.drawable.animated_wrong);
                Glide.with(this).asGif().load(R.drawable.animated_wrong).into(_binding.imgGif);
                _binding.btnComplete.setBackgroundResource(R.drawable.ad_money_button_shape);
            } else if (status.toUpperCase().equals("PENDING")){
                _binding.btnComplete.setBackgroundResource(R.drawable.ad_money_button_shape);
                _binding.txtTitle.setText("Pending");
//                _binding.txtMessage.setText("Your transaction is under process.\nPlease check your history after a few min.");
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("CHILL!");
//                _binding.imgGif.setGifImageResource(R.drawable.animated_pending);
                Glide.with(this).asGif().load(R.drawable.animated_pending).into(_binding.imgGif);
            }else {
                _binding.btnComplete.setBackgroundResource(R.drawable.ad_money_button_shape);
                _binding.txtTitle.setText("Success");
//                _binding.txtMessage.setText("Your transaction has been\ncompleted successfully.");
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
                        if (jsonObject.getString("status").toLowerCase().equals("success")){
                            intent.putExtra("bps", "1");
                        }else {
                            intent.putExtra("bps", "0");
                        }
                        intent.putExtra("mReportsData", data);
                        intent.putExtra("type",type);
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
    public void onSuccessOther(String data) {

    }

    @Override
    public void onSuccessOther(String data, String data_other) {
        try {
            if (data_other.equals("INSUFFICIENT")) {
                openGatewaysDialog(data);
            } else {
                if (dialog != null) {
                    dialog.dismiss();
                }
                successDialog(data, data_other);
            }
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

    List<OperatorData> operatorDataList = new ArrayList<>();
    List<CircleData> circleDataList = new ArrayList<>();

    public void doRecharge(View view) {
        String mobile_number_str = Objects.requireNonNull(mobile_number.getText()).toString();
        String std_code_str = Objects.requireNonNull(std_code.getText()).toString();
        String account_no_str = Objects.requireNonNull(account_no.getText()).toString();
        String amount_str = Objects.requireNonNull(amount.getText()).toString();

        switch (type) {
            case "Electricity":
            case "Gas":
            case "Water":
                if (selected_operator.equals("Select Service Provider")) {
                    showError("Please select service provider");
                    return;
                }

                if (mobile_number_str.trim().isEmpty()) {
                    showError("Please enter Consumer/Account/K Number");
                    mobile_number.setText("");
                    return;
                }
                if (amount_str.trim().isEmpty()) {
                    showError("Please enter amount");
                    amount.setText("");
                    return;
                }

                if (account_no_str.trim().isEmpty()) {
                    account_no_str = "";
                }

                hideKeyBoard(mobile_number);
                rechargeDialog(mobile_number_str + "", selected_operator + "",
                        amount_str + "", type + "",
                        "0", account_no_str + "", "1", "",
                        txtOperator.getText().toString().trim() + "");

                break;
            case "GiftCards":
                if (mobile_number_str.trim().length() == 0) {
                    showError("Please enter mobile number");
                    mobile_number.setText("");
                    return;
                }

                if (mobile_number_str.trim().length() < 10) {
                    showError("Please enter valid mobile number");
                    mobile_number.setText("");
                    return;
                }

                if (amount_str.trim().length() == 0) {
                    showError("Please enter amount");
                    amount.setText("");
                    return;
                }


                if (account_no_str.trim().length() == 0) {
                    account_no_str = "";
                }
                rechargeDialog(mobile_number_str + "", selected_operator + "",
                        amount_str + "", type + "",
                        "0", account_no_str + "", "1", "",
                        txtOperator.getText().toString() + "");
                break;
            default:
                if (selected_operator.equals("Select Operator")) {
                    showError("Please select operator");
                    return;
                }

                if (std_code_str.trim().length() == 0) {
                    showError("Please enter STD code");
                    std_code.setText("");
                    return;
                }
                if (mobile_number_str.trim().length() == 0) {
                    showError("Please enter landline no./User id");
                    mobile_number.setText("");
                    return;
                }

                if (amount_str.trim().length() == 0) {
                    showError("Please enter amount");
                    amount.setText("");
                    return;
                }
                if (account_no_str.trim().length() == 0) {
                    account_no_str = "";
                }
                hideKeyBoard(mobile_number);

                rechargeDialog(mobile_number_str + "", selected_operator + "",
                        amount_str + "", type + "",
                        std_code_str + "", "0", "1",
                        account_no_str + "", txtOperator.getText().toString() + "");

                break;
        }

    }

    public BottomSheetDialog dialog = null;
    public FrameLayout bottomSheet = null;

    public void rechargeDialog(String number, String operator, String amount, String type,
                               String std_code, String sub_division, String circle, String ac_number,
                               String selected_operator_str) {
        try {
            RechargeDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                    R.layout.recharge_dialog, null, false);
            Dialog dialog = new Dialog(getActivity(), R.style.CustomAlertDialog);
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(true);
            binding.operator.setText(selected_operator_str.trim());
            binding.number.setText(number.trim());
            binding.amount.setText("\u20b9" + amount);
//            if (warning_message.equals("")) {
//                binding.warningMessage.setVisibility(GONE);
//            } else {
//                binding.warningMessage.setVisibility(VISIBLE);
//            }
            binding.warningMessage.setText("Read Carefully! Wrong number successful recharge will not be refunded. By confirming you are agree with our Terms & Conditions!");
            binding.close.setOnClickListener(v -> dialog.dismiss());

            binding.mpinTxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()){
                        mPin = s.toString();
                    }
                }
            });

            binding.confirm.setOnClickListener(v -> {
                mPin = binding.mpinTxt.getText().toString();
                if (TextUtils.isEmpty(mPin)) {
                    Toasty.error(getActivity(),"Please enter your M Pin", Toasty.LENGTH_LONG, false).show();
                    return;
                }
                if (mPin.length() != 4) {
                    Toasty.error(getActivity(),"MPin length should be 4 digit", Toasty.LENGTH_LONG, false).show();
                    return;
                }
                dialog.dismiss();
                mDefaultPresenter.doMobileRecharge(number + "", id + "",
                        amount + "", type + "",
                        std_code + "", sub_division + "",
                        circle + "", ac_number + "", device_id + "",mPin,false);
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            binding.txtTransactionAmountValue.setText("\u20b9"+amount.getText().toString().trim());
            if (mDatabase != null){
                if (mDatabase.getEarnings() != null){
                    binding.txtWalletAmountValue.setText("₹"+mDatabase.getEarnings());
                }
            }
            binding.txtTotalDiscountValue.setText("₹"+json.getString("discount"));
            releaseAmount = (Double.parseDouble(amount.getText().toString().trim()) - json.getDouble("discount"));
            binding.txtPayableAmtValue.setText("₹"+releaseAmount);

            binding.btnPay.setText("Add on ₹"+json.getInt("required"));

            binding.btnPay.setOnClickListener(v -> {
                try {
                    mDefaultPresenter.addInsufficientBalance(device_id, json.getString("required"));
                    dialog2.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            dialog2.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG_DATA", "addBalance: "+e.getMessage());
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
                    },10000
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
        if (requestCode == 104){
            if (resultCode == RESULT_OK){
                mDefaultPresenter.orderDetails(device_id, order_id);
            } else {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onClick(@NonNull View v) {
        switch(v.getId()){
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }
}