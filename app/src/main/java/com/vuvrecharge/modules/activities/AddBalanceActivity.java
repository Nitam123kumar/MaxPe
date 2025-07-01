package com.vuvrecharge.modules.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.PaymentResultListener;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.activities.newActivities.MobileBankingActivity;
import com.vuvrecharge.modules.adapter.PaymentMethodSelectAdapter;
import com.vuvrecharge.modules.adapter.PaymentSettingAdapter;
import com.vuvrecharge.modules.model.PaymentSetting;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Objects;

//import androidmads.library.qrgenearator.QRGContents;
//import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AddBalanceActivity extends BaseActivity implements DefaultView, View.OnClickListener,
        PaytmPaymentTransactionCallback, PaymentResultListener,PaymentMethodSelectAdapter.OnClickListener {
    DefaultPresenter mDefaultPresenter;

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.editAmount)
    EditText amount;
//    @BindView(R.id.txtAvailableBalance)
//    TextView txtAvailableBalance;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.buttonAddBalance)
    ConstraintLayout add_balance;
    @BindView(R.id.amount_textView)
    TextView add_amount_show;
    @BindView(R.id.txtThousand)
    TextView txtThousand;
    @BindView(R.id.txtFiveThousandFiveHundred)
    TextView txtFiveThousand;
//    @BindView(R.id.buttonAddBalance)
//    TextView txtTwoThousand;
    @BindView(R.id.txtFiveHundred)
    TextView txtFiveHundred;

    @BindView(R.id.help)
    TextView help;
    @BindView(R.id.txtMsg)
    TextView txtMsg;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.payment_using_imageView)
    ImageView payment_using_imageView;

    @BindView(R.id.upi_tittle_textView)
     TextView upi_tittle_textView;
    @BindView(R.id.pay_using_textView)
    TextView pay_using_textView;
    @BindView(R.id.pay_using_ConstraintLayout)
    ConstraintLayout pay_using_ConstraintLayout;

    ArrayList<PaymentSetting> list = new ArrayList<>();
    JSONObject json = null;
    String data= "";
    String m_id = "";
    String min = "0";
    String max = "0";
    String dataPayment = null;
    String paytm_getway = "";
    DefaultView defaultView;
    BottomSheetDialog bottomSheetDialog =null;

    String hashString, payu_key, payu_username, user_name, user_mobile, user_email, productinfo,
            orderid, razorpay_merchant_key = "", razorpay_min_amount = "0",
            razorpay_max_amount = "0", phonePackage = "", hdfc_dynamic_getway = "",razorpay_getway ="",
            hdfcminamount = "0", hdfcmaxamount = "0", paumin = "", paumax = "", phonepe_getway = "", hdfc_api = "",payu_getway="";
    String titleStr = "";
    private static int B2B_PG_REQUEST_CODE = 777;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_add_balance);
        ButterKnife.bind(this);
        title.setText("Online Deposit");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mDefaultPresenter = new DefaultPresenter(this);
        defaultView = this;
        add_balance.setOnClickListener(this);
        pay_using_ConstraintLayout.setOnClickListener(this);
//        try {
//            UserData userData = mDatabase.getUserData();
//            txtAvailableBalance.setText("Available Balance \u20b9 " + userData.getEarnings());
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        titleStr = intent.getStringExtra("title");
        title.setText(titleStr);
        upi_tittle_textView.setText(titleStr);


        if (Objects.equals(intent.getStringExtra("title"), "Paytm Payment")){
            payment_using_imageView.setImageResource(R.drawable.paytm_logo_2);
        }
         if (Objects.equals(intent.getStringExtra("title"), "UPI Payment")){
            payment_using_imageView.setImageResource(R.drawable.upi_logo_2);
        }
         if (Objects.equals(intent.getStringExtra("title"), "PhonePe Payment")){
            payment_using_imageView.setImageResource(R.drawable.phonepe_logo_2);
        }
         if (Objects.equals(intent.getStringExtra("title"), "Razorpay Payment")){
            payment_using_imageView.setImageResource(R.drawable.razorpay_logo_2);
        }


        txtThousand.setOnClickListener(this);
        txtFiveThousand.setOnClickListener(this);
//        txtTwoThousand.setOnClickListener(this);
        txtFiveHundred.setOnClickListener(this);
        mToolbar.setOnClickListener(this);
        help.setOnClickListener(this);
        try {
//            add_amount_show.setText("Continue");
//            add_amount_show.setTextColor(getResources().getColor(R.color.black_4));
//            add_amount_show.setTypeface(add_amount_show.getTypeface(), Typeface.BOLD);
//            add_balance.setBackgroundResource(R.drawable.btn_drawable_disable);
            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()){
//                        add_amount_show.setTextColor(getResources().getColor(R.color.black_4));
//                        add_amount_show.setTypeface(add_amount_show.getTypeface(), Typeface.BOLD);
//                        add_balance.setBackgroundResource(R.drawable.btn_drawable_disable);
//                        add_amount_show.setText("Continue");
                    }else {
                        add_amount_show.setText("\u20b9"+s.toString());
                        add_amount_show.setTextColor(Color.WHITE);
                        add_amount_show.setTypeface(add_amount_show.getTypeface(), Typeface.BOLD);
//                        add_balance.setBackgroundResource(R.drawable.btn_drawable);
                    }
                }
            });
            JSONObject jsonObject = new JSONObject(data);
            m_id = jsonObject.getString("merchant_id");
            min = jsonObject.getString("gateway_min_amount");
            max = jsonObject.getString("gateway_max_amount");
            paytm_getway = jsonObject.getString("paytm_getway");
            payu_getway = jsonObject.getString("payu_getway");
            paumax = jsonObject.getString("payu_max_amount");
            paumin = jsonObject.getString("payu_min_amount");
            razorpay_getway = jsonObject.getString("razorpay_getway");
            razorpay_min_amount = jsonObject.getString("razorpay_min_amount");
            razorpay_max_amount = jsonObject.getString("razorpay_max_amount");
            razorpay_merchant_key = jsonObject.getString("razorpay_merchant_key");
            hdfc_dynamic_getway = jsonObject.getString("hdfc_dynamic_getway");
            phonepe_getway = jsonObject.getString("phonepe_getway");
            hdfcminamount = jsonObject.getString("hdfc_min_amount");
            hdfcmaxamount = jsonObject.getString("hdfc_max_amount");
        } catch (Exception e) {
            e.printStackTrace();
        }

        showSoftKeyboard(amount);
        amount.setFocusable(true);
        amount.requestFocus();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyBoard(amount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "addbalance");
    }

    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String message) {
        hideKeyBoard(amount);
        try {
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra("from", "passbook");
            intent.putExtra("paymentdata", message);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String data, @NonNull String data_other) {
        try {
            if (data_other.equals("payMethod")) {
                JSONObject jsonObject = new JSONObject(data);
                payu_key = jsonObject.getString("payu_key");
                payu_username = jsonObject.getString("payu_username");
                user_name = jsonObject.getString("user_name");
                user_mobile = jsonObject.getString("user_mobile");
                user_email = jsonObject.getString("user_email");
                productinfo = jsonObject.getString("productinfo");
                orderid = jsonObject.getString("orderid");
            } else if (data_other.equals("razorPayMethod")) {
                JSONObject jsonObject = new JSONObject(data);
                String orderid = jsonObject.getString("orderid");
                this.order_id = orderid;
                String uri = jsonObject.getString("upi_uri");
                Intent upiIntent = new Intent(Intent.ACTION_VIEW);
                String uriString = uri;
                upiIntent.setData(Uri.parse(uriString.trim()));
                Intent chooser = Intent.createChooser(upiIntent, "Pay with...");
                startActivityForResult(chooser, 104, null);
            } else if (data_other.equals("PhoneList")) {
                phonePackage = data;
                mDefaultPresenter.iciciDynamicQR(device_id, "10", phonePackage);
            } else {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessOther(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.getString("status").equals("success")) {
                Toasty.success(getActivity(), "Payment Success", Toast.LENGTH_LONG, false).show();
            } else if (jsonObject.getString("status").equals("pending")) {
                Toasty.success(getActivity(), "Payment pending", Toast.LENGTH_LONG, false).show();
            } else if (jsonObject.getString("status").equals("failed")) {
                Toasty.success(getActivity(), "Payment failed", Toast.LENGTH_LONG, false).show();
            } else {
                Toasty.success(getActivity(), "Payment cancelled", Toast.LENGTH_LONG, false).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessOther(String data, String data_other) {
        try {
            if (data_other.equals("hdfc")) {
                JSONObject jsonObject = new JSONObject(data);
                String orderid = jsonObject.getString("orderid");
                this.order_id = orderid;
                String uri = jsonObject.getString("upi_uri");
                Intent upiIntent = new Intent(Intent.ACTION_VIEW);
                String uriString = uri;
                upiIntent.setData(Uri.parse(uriString.trim()));
                Intent chooser = Intent.createChooser(upiIntent, "Pay with...");
                startActivityForResult(chooser, 102, null);
            } else if (data_other.equals("phonePayNew")) {
                JSONObject jsonObject = new JSONObject(data);
                String orderid = jsonObject.getString("orderid");
                this.order_id = orderid;
                String uri = jsonObject.getString("upi_uri");
                Intent upiIntent = new Intent(Intent.ACTION_VIEW);
                String uriString = uri;
                upiIntent.setData(Uri.parse(uriString.trim()));
                Intent chooser = Intent.createChooser(upiIntent, "Pay with...");
                startActivityForResult(chooser, 103, null);
                System.out.println("orderIdPhonePay===" + orderid);
            } else if (data_other.equals("paytm")){
                JSONObject jsonObject = new JSONObject(data);
                String orderid = jsonObject.getString("orderid");
                String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");
                this.order_id = orderid;
                initializePaytmPayment(CHECKSUMHASH, orderid, amount.getText().toString());
            } else if (data_other.equals("timepass")){
                try {
                    dataPayment = data;
                    json = new JSONObject(data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                Log.d("TAG_DATA", "onSuccessOther:  no payment gateway");
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void addAmount(int valueToAdd) {
        String currentText = amount.getText().toString();
        int currentAmount = 0;


        if (!currentText.isEmpty()) {
            try {
                currentAmount = Integer.parseInt(currentText);
            } catch (NumberFormatException e) {
                currentAmount = 0;
            }
        }

        int newAmount = currentAmount + valueToAdd;
        amount.setText(String.valueOf(newAmount));
    }


    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
            case R.id.txtThousand:
                addAmount(1000);
                break;
            case R.id.txtFiveThousandFiveHundred:
               addAmount(1500);
                break;
//            case R.id.txtTwoThousand:
//                amount.setText("2000");
//                break;
            case R.id.txtFiveHundred:
                addAmount(500);
                break;
//
//            case R.id.pay_using_ConstraintLayout:
//                usingPaymentBottomSheet();
//                break;

            case R.id.help:
                Intent intent = new Intent(getActivity(), SupportActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonAddBalance:
                String hdfcstrAmount = amount.getText().toString();
                if (titleStr.equals("UPI Payment")) {
                    if (TextUtils.isEmpty(hdfcstrAmount)) {
                        showError("Please enter amount");
                        return;
                    }
                    mDefaultPresenter.hdfcbankDynamicQR(device_id, hdfcstrAmount,"false");
                } else if (titleStr.equals("Razorpay Payment")) {
                    String strrazorAmount = amount.getText().toString();
                    if (TextUtils.isEmpty(strrazorAmount)) {
                        showError("Please enter amount");
                        return;
                    }
                    mDefaultPresenter.generateRazorPayOrder(device_id, strrazorAmount,"false");
//                } else if (titleStr.equals("CashFree Payment")) {
//                    String strrazorAmount = amount.getText().toString();
//                    if (TextUtils.isEmpty(strrazorAmount)) {
//                        showError("Please enter amount");
//                        return;
//                    }
//                    mDefaultPresenter.generateRazorPayOrder(device_id, strrazorAmount, "false");
//                }else if (titleStr.equals("PhonePe Payment")) {
                    String phonePeAmount = amount.getText().toString();
                    if (TextUtils.isEmpty(phonePeAmount)) {
                        showError("Please enter amount");
                        return;
                    }
                    mDefaultPresenter.phonepeDynamicQR(device_id, phonePeAmount,"false");
                } else if (titleStr.equals("Paytm Payment")) {
                    String amount_online_txt = amount.getText().toString();
                    if (amount_online_txt.replaceAll("\\s", "").isEmpty()) {
                        onError("Please enter amount");
                        amount.setText("");
                        return;
                    }
                    hideKeyBoard(amount);
                    mDefaultPresenter.generateChecksum(amount_online_txt, device_id + "", "false");
                    break;
                }
                break;
        }
    }

    private void usingPaymentBottomSheet(){
        try {


        FrameLayout bottomSheet = null;
        list.clear();

        bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        View layout = getLayoutInflater().inflate(R.layout.using_payment_layout, null,false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView recyclerView = layout.findViewById(R.id.using_payment_recyclerView);

        changeStatusBarColor(bottomSheetDialog);
        bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(false);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setPeekHeight(600);
        }

        if (json.getString("phonepe_getway").equals("Yes")){
            list.add(new PaymentSetting(R.drawable.phonepe_logo_2,"PhonePe","Payment Gateway"));
        }
        if (json.getString("hdfc_dynamic_getway").equals("Yes")){
            list.add(new PaymentSetting(R.drawable.upi_logo_2,"UPI","Payment Gateway"));
        }
        if (json.getString("razorpay_getway").equals("Yes")){
            list.add(new PaymentSetting(R.drawable.razorpay_logo_2,"Razorpay","Payment Gateway"));
        }
        if (json.getString("paytm_getway").equals("Yes")){
            list.add(new PaymentSetting(R.drawable.paytm_logo_2,"Paytm","Payment Gateway"));
        }
        if (json.getString("manual_getway").equals("Yes")){
            list.add(new PaymentSetting(R.drawable.mobile_banking_logo_2,"Mobile","Banking (IMPS)"));
        }
//            if (json.getString("manual_getway").equals("Yes")){
//                list.add(new PaymentSetting(R.drawable.mobile_banking_logo_2,"Mobile","Banking (IMPS)"));
//            }

        if (!list.isEmpty()){
            LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
        }

        PaymentMethodSelectAdapter adapter1 = new PaymentMethodSelectAdapter((Context) this, (PaymentSettingAdapter.OnClickListener) this);
        adapter1.addData(list);
        recyclerView.setAdapter(adapter1);

        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initializePaytmPayment(String checksumHash, String order_id, String amount) {
        try {
            PaytmOrder paytmOrder = new PaytmOrder(order_id + "",
                    m_id + "", checksumHash + "",
                    amount + "", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + order_id);
            TransactionManager transactionManager = new TransactionManager(paytmOrder, this);
            transactionManager.startTransaction(this, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String order_id = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 600 && data != null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(AddBalanceActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(AddBalanceActivity.this, AfterDepositActivity.class);
                intent.putExtra("order_id", order_id);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == 103) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(AddBalanceActivity.this, AfterDepositActivity.class);
                intent.putExtra("order_id", order_id);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == 104) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(AddBalanceActivity.this, AfterDepositActivity.class);
                intent.putExtra("order_id", order_id);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onTransactionResponse(@NonNull Bundle bundle) {
        try {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void networkNotAvailable() {
        try {
            onError("Network error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorProceed(String s) {
        try {
            onError("Transaction cancelled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        try {
            onError("Transaction cancelled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void someUIErrorOccurred(String s) {
        try {
            onError("Transaction cancelled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        try {
            onError("Transaction cancelled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressedCancelTransaction() {
        try {
            onError("Transaction cancelled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        try {
            onError("Transaction cancelled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        try {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            Toasty.success(getActivity(), "Payment Success", Toast.LENGTH_LONG, false).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toasty.error(getActivity(), "Payment failed", Toast.LENGTH_LONG, false).show();
    }

    @Override
    public void onClick(String name) {
        switch (name){
            case "Paytm":
                data=dataPayment;
                pay_using_textView.setText("Payment Getaway");
                upi_tittle_textView.setText("Paytm Payment");
                payment_using_imageView.setImageResource(R.drawable.paytm_logo_2);
                bottomSheetDialog.cancel();
                break;
            case "Razorpay":
                data=dataPayment;
                pay_using_textView.setText("Payment Getaway");
                upi_tittle_textView.setText("Razorpay Payment");
                payment_using_imageView.setImageResource(R.drawable.paytm_logo_2);
                bottomSheetDialog.cancel();
                break;
            case "UPI":
                data=dataPayment;
                pay_using_textView.setText("Payment Getaway");
                upi_tittle_textView.setText("UPI Payment");
                payment_using_imageView.setImageResource(R.drawable.paytm_logo_2);
                bottomSheetDialog.cancel();
                break;
            case "PhonePe":
                data=dataPayment;
                pay_using_textView.setText("Payment Getaway");
                upi_tittle_textView.setText("PhonePe Payment");
                payment_using_imageView.setImageResource(R.drawable.paytm_logo_2);
                bottomSheetDialog.cancel();
                break;
            case "Mobile":
              Intent  intent = new Intent(getActivity(), MobileBankingActivity.class);
                startActivity(intent);
                break;
        }
    }
}