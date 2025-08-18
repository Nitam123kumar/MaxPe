package com.vuvrecharge.modules.activities.newActivities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.vuvrecharge.databinding.RechargeDialogBinding;
import com.vuvrecharge.databinding.ScreenRechargePaymentLayoutBinding;
import com.vuvrecharge.databinding.TransactionDialogBinding;
import com.vuvrecharge.databinding.WalletTransactionBottonDialogBinding;
import com.vuvrecharge.modules.activities.RechargeReportActivity;
import com.vuvrecharge.modules.adapter.BillerAdapter;
import com.vuvrecharge.modules.model.BillFetch;
import com.vuvrecharge.modules.model.PaymentModel;
import com.vuvrecharge.modules.model.ReportsData;
import com.vuvrecharge.modules.model.SpinnerData;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.UserPreferences;
import com.vuvrecharge.preferences.UserPreferencesImpl;
import com.vuvrecharge.utils.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
@SuppressLint({"NonConstantResourceId","SetTextI18n"})
public class ElectricityBillPayActivity extends BaseActivity implements DefaultView, View.OnTouchListener,View.OnClickListener{

    private DefaultPresenter mDefaultPresenter;
    public UserPreferences mDatabase = new UserPreferencesImpl();
    @BindView(R.id.toolbar_layout)
    LinearLayout toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.spinner_layout)
    LinearLayout spinner_layout;
    @BindView(R.id.spinner_layout2)
    LinearLayout spinner_layout2;
    @BindView(R.id.spinner_layout3)
    LinearLayout spinner_layout3;

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.spinner2)
    Spinner spinner2;
    @BindView(R.id.spinner3)
    Spinner spinner3;

    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.provider_name)
    TextView provider_name;
    @BindView(R.id.consumerNumber)
    EditText consumerNumber;

    @BindView(R.id.Consumer_Number)
    TextView Consumer_Number;

//    @BindView(R.id.consumer_number_layout)
//    TextInputLayout consumer_number_layout;
    @BindView(R.id.sub_division_code_layout)
    TextView sub_division_code_layout;
    @BindView(R.id.amount_layout)
    TextView amount_layout;
    @BindView(R.id.sub_division_code)
    EditText sub_division_code;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.recyclerViewBillerInfo)
    RecyclerView recyclerViewBillerInfo;
    @BindView(R.id.constBillInfo)
    ConstraintLayout constBillInfo;
    @BindView(R.id.field4)
    EditText field4;
    @BindView(R.id.viewBill)
    View viewBill;
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.dueToDate)
    TextView dueToDate;
    @BindView(R.id.star_img)
    TextView star_img;
    @BindView(R.id.consumerNumberText)
    TextView consumerNumberText;
    @BindView(R.id.sub_division_codeText)
    TextView sub_division_codeText;
    @BindView(R.id.amountText)
    TextView amountText;
    @BindView(R.id.txtViewMore)
    TextView txtViewMore;
    @BindView(R.id.field4Text)
    TextView field4Text;
    @BindView(R.id.spinnerText)
    TextView spinnerText;
    @BindView(R.id.spinnerText2)
    TextView spinnerText2;
    @BindView(R.id.spinnerText3)
    TextView spinnerText3;
    @BindView(R.id.btnProcess)
    TextView btnProcess;
    @BindView(R.id.txtBillFetch)
    TextView txtBillFetch;
    @BindView(R.id.wallet_amount)
    TextView wallet_amount;
    @BindView(R.id.ProceedToPay)
    TextView proceedToPay;
//    @BindView(R.id.txtAmount)
//    TextView txtAmount;
    @BindView(R.id.txtAmountValue)
    TextView txtAmountValue;
    @BindView(R.id.txtRupe)
    TextView txtRupees;
    @BindView(R.id.provider_img)
    ImageView providerImageView;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private String type = null;
    private String name = null;
    private String errorMsg = null;
    String id,titleStr;
    String billAmount = "";
    String amt = "";
    BillerAdapter adapter;
    List<BillFetch> infos = new ArrayList<>();
    List<SpinnerData> spinnerData = new ArrayList<>();
    List<SpinnerData> spinnerData2 = new ArrayList<>();
    List<String> spinnerStrData = new ArrayList<>();
    List<String> spinnerStrData2 = new ArrayList<>();

    String strId = "",strId2 = "",order_id = "";
    ReportsData mReportsData;
    Handler handler;
    Runnable runnable;
    String mPin = "";
    String warning_message = "";
    public BottomSheetDialog dialog = null;
    public FrameLayout bottomSheet = null;
    BottomSheetDialog dialog1 = null;
    String amtPay = "";
    double releaseAmount = 0.000;
    LinearLayoutManager manager;
    Timer timer;
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_bill_pay);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        toolbar.setOnClickListener(this);
        titleStr =  getIntent().getStringExtra("title");
        handler = new Handler();
        proceedToPay.setOnClickListener(v -> {
        showError1("Please Fetch Bill");
        });
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mDefaultPresenter = new DefaultPresenter(this);
        manager = new LinearLayoutManager(this);
        recyclerViewBillerInfo.setLayoutManager(manager);
        setStatusBarGradiant(this);

        if(titleStr != null){
            title.setText(titleStr);
            name = getIntent().getStringExtra("name");
            String logo1 = getIntent().getStringExtra("logo");
            if (logo1 != null){
                Glide.with(this).load(logo1).error(R.drawable.m_svg).into(providerImageView);
                providerImageView.setVisibility(VISIBLE);
            }
            else {
                providerImageView.setVisibility(GONE);
            }
            id = getIntent().getStringExtra("id");
            type = getIntent().getStringExtra("type");
            if (type.equals("Postpaid")){
                consumerNumber.setVisibility(VISIBLE);
                Consumer_Number.setVisibility(VISIBLE);
                Consumer_Number.setText("Mobile Number");
                consumerNumber.setHint("Mobile Number");
            }
            warning_message = getIntent().getStringExtra("warning_message");
            provider_name.setVisibility(VISIBLE);
            proceedToPay.setVisibility(VISIBLE);
            if (name != "" || name != null) {
                provider_name.setText(name);
                mDefaultPresenter.operatorsDetails(device_id, id);
                UserData userData = mDatabase.getUserData();
                wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
            }

//            if (titleStr.contains("Electricity Bill")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                Glide.with(this).load(logo).placeholder(R.drawable.btn_drawable).into(providerImageView);
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                providerImageView.setVisibility(VISIBLE);
//                proceedToPay.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            }else if (titleStr.contains("Postpaid Recharge")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                consumerNumber.setVisibility(VISIBLE);
//                Consumer_Number.setVisibility(VISIBLE);
//                Consumer_Number.setText("Mobile Number");
//                consumerNumber.setHint("Mobile Number");
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            }else if (titleStr.contains("Cylinder Bill")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.contains("Gas Bill")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.contains("Water Bill")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Fastag")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    consumerNumber.setVisibility(VISIBLE);
//                    Consumer_Number.setVisibility(VISIBLE);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Insurance")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Broadband/Landline")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Subscription Fees")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Hospital Pathology")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Club Association")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Housing Society")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (!Objects.equals(name, "") || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Municipal Tax")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Cable TV")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Loan Re payment")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Credit Card Payment")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Education Fees")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Municipal Services")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("NCMC Recharge")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Prepaid Meter")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Recurring Deposit")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else if (titleStr.equals("Donation")) {
//                name = getIntent().getStringExtra("name");
//                String logo = getIntent().getStringExtra("logo");
//                id = getIntent().getStringExtra("id");
//                type = getIntent().getStringExtra("type");
//                warning_message = getIntent().getStringExtra("warning_message");
//                provider_name.setVisibility(VISIBLE);
//                if (name != "" || name != null) {
//                    provider_name.setText(name);
//                    mDefaultPresenter.operatorsDetails(device_id, id);
//                    UserData userData = mDatabase.getUserData();
//                    wallet_amount.setText("Your balance: \u20b9" + userData.getEarnings() + "");
//                }
//            } else
                if (titleStr.contains("Google Play")) {
                provider_name.setVisibility(GONE);
                consumerNumber.setVisibility(VISIBLE);
                Consumer_Number.setVisibility(VISIBLE);
                consumerNumber.setHint("Mobile Number");
                Consumer_Number.setText("Mobile Number");
                amount_layout.setVisibility(VISIBLE);
                amount.setVisibility(VISIBLE);
            }
        }

        scrollView.setOnTouchListener(this);
        constBillInfo.setOnTouchListener(this);
        recyclerViewBillerInfo.setOnTouchListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyBoard(consumerNumber);
//        hideKeyBoard(provider_name);
        hideKeyBoard(sub_division_code);
        hideKeyBoard(field4);
        hideKeyBoard(amount);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "ElectricityBill");
        wallet_amount.setText("Your balance: \u20b9" +mDatabase.getEarnings()+"");
    }

    public void doBillPay(View view) {
        String consumer_number = consumerNumber.getText().toString().trim();
        String subDivision = sub_division_code.getText().toString().trim();

        if (titleStr.equals("Fastag")){
            amt = amount.getText().toString().trim();
        }else {
            if (billAmount.isEmpty() || billAmount == null){
                amt = amount.getText().toString().trim();
            }else {
                amt = billAmount.trim();
            }
        }

//        if (consumer_number.trim().isEmpty()) {
//            showError(errorMsg);
//            consumerNumber.setText("");
//            return;
//        }

        if (amt.trim().isEmpty()) {
            showError("Please enter amount");
            amount.setText("");
            return;
        }

        hideKeyBoard(consumerNumber);
        rechargeDialog(
                consumerNumber.getText().toString()
                ,id,amt,type,amount.getText().toString().trim(),
                subDivision,"","",provider_name.getText().toString());
    }

    @Override
    public void onError(String error) {
            showErrorR(error);
    }

    @SuppressLint({"UseRequiresApi", "ObsoleteSdkInt"})
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.main_wallet_shape);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccess(String data) {
        infos.clear();
        try {
            JSONObject object = new JSONObject(data);
            if ((object.getJSONObject("additionalInfo") != null)){
                JSONObject object1 = new JSONObject(object.getString("additionalInfo"));
                Iterator<String> keys = object1.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = object1.getString(key);
                    if (!value.isEmpty()){
                        infos.add(new BillFetch(key, value));
                    }
                }

                if (infos.size() < 3){
                    txtViewMore.setVisibility(GONE);
                    adapter = new BillerAdapter(infos, titleStr, type,2);
                }else if (infos.size() < 4){
                    txtViewMore.setVisibility(GONE);
                    adapter = new BillerAdapter(infos, titleStr, type,3);
                }else {
                    txtViewMore.setVisibility(VISIBLE);
                }


//                txtViewMore.setOnClickListener(v -> {
//                    if (adapter.getItemCount() == 2) {
//                        txtViewMore.setText("Less More");
//                        adapter = new BillerAdapter(infos, titleStr, type, infos.size());
//                    }else if (adapter.getItemCount() == 3) {
//                        txtViewMore.setText("Less More");
//                        adapter = new BillerAdapter(infos, titleStr, type, infos.size());
//                    }else {
//                        adapter = new BillerAdapter(infos, titleStr, type,3);
//                        txtViewMore.setText("View More");
//                    }
//                    recyclerViewBillerInfo.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                });
                // Changes On 26/06/2025 By Nitan Singh


                adapter = new BillerAdapter(infos, titleStr, type, 3);
                recyclerViewBillerInfo.setAdapter(adapter);
                adapter.notifyDataSetChanged();

// Show Due Date if available
                for (BillFetch model : infos) {
                    if (model.getKey().equals("Due Date")) {
                        String dueDateValue = model.getValue();
                        dueToDate.setText("Due Date: " + dueDateValue);
                        break;
                    }
                }

                txtViewMore.setOnClickListener(v -> {
                    if (adapter.getItemCount() == 3) {
                        adapter = new BillerAdapter(infos, titleStr, type, infos.size());
                        txtViewMore.setText("Less More");
                    } else {
                        adapter = new BillerAdapter(infos, titleStr, type, 3);
                        txtViewMore.setText("View More");
                    }

                    recyclerViewBillerInfo.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                });


            }

            billAmount = object.getString("billAmount");
//            txtAmount.setText("Bill Amount");
            txtAmountValue.setText(billAmount);
            txtRupees.setVisibility(VISIBLE);

            if (titleStr.equals("Fastag")){
                amount_layout.setVisibility(VISIBLE);
                amount.setVisibility(VISIBLE);
//                txtAmount.setVisibility(GONE);
                txtAmountValue.setVisibility(GONE);
                txtRupees.setVisibility(GONE);
                viewLine.setVisibility(GONE);
                viewBill.setVisibility(GONE);
            }

            recyclerViewBillerInfo.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
            btnProcess.setVisibility(VISIBLE);
            proceedToPay.setVisibility(GONE);
            txtBillFetch.setVisibility(GONE);
            constBillInfo.setVisibility(VISIBLE);
        }catch (JSONException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccess(String data, String data_other) {
        try {
            if (data_other.equals("recharge")){
                JSONObject jsonObject = new JSONObject(data);
                String orderid = jsonObject.getString("orderid");
                this.order_id = orderid;
                String uri = jsonObject.getString("upi_uri");
                Intent upiIntent = new Intent(Intent.ACTION_VIEW);
                upiIntent.setData(Uri.parse(uri.trim()));
                Intent chooser = Intent.createChooser(upiIntent, "Pay with...");
                startActivityForResult(chooser, 104, null);
            }else if (data_other.equals("orderDetails")){
                Gson gson = new Gson();
                Type type_ = new TypeToken<ReportsData>() {
                }.getType();
                JSONObject object = new JSONObject(data);
                String payment_status = object.getString("payment_status");
                mReportsData = gson.fromJson(data, type_);
                mReportsData.setStatus(payment_status);
                if (payment_status.toLowerCase().equals("pending")){
                    openPendingDialog();
                }else if (payment_status.toLowerCase().equals("success")){
                    mDefaultPresenter.doElectricityRecharge(
                            consumerNumber.getText().toString().trim(),id,amt,type,
                            amount.getText().toString().trim(), sub_division_code.getText().toString().trim(),
                            "","",device_id,mPin);
                }else if (payment_status.toLowerCase().equals("failed")){
                    Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
                }else {
                    openPendingDialog();
                }
            }else if (data_other.equals("orderDetailsPending")){
                openPendingDialog();
            }
            else{
                successDialog(data, data_other);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    private void pendingstatus() {
        mDefaultPresenter.orderDetailspending(device_id + "", order_id);
    }

    private void setData(ReportsData mReportsData) {
        try {
            if (mReportsData.getStatus().equals("pending")) {
                handler.postDelayed(runnable, 10000);
            }
            if (mReportsData.getStatus().equals("SUCCESS")) {
                handler.postDelayed(runnable, 10000);
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
            Log.d("DATA_DATA",data);
            if (data_other.equals("INSUFFICIENT")){
                openGatewaysDialog(data);
            } else{
                createLayout(data);
            }
        }catch (Exception e){
            Log.d("TAG_DATA", "onSuccessOther: "+e.getMessage());
        }
    }

    private void createLayout(String data) {
        try {
            JSONObject object = new JSONObject(data);
            int a = Integer.parseInt(object.getString("total_fields"));
            String jsonNumberData = object.getString("number_data");
            String is_billFetch = object.getString("is_fetch");

            if (is_billFetch.equals("1")) {
                txtBillFetch.setVisibility(VISIBLE);
                btnProcess.setVisibility(GONE);
                proceedToPay.setVisibility(VISIBLE);
            } else {
                txtBillFetch.setVisibility(GONE);
                amount_layout.setVisibility(VISIBLE);
                amount.setVisibility(VISIBLE);
                btnProcess.setVisibility(VISIBLE);
                proceedToPay.setVisibility(GONE);
            }

            JSONObject numberField = new JSONObject(jsonNumberData);
            txtBillFetch.setOnClickListener(v -> {
                hideKeyBoard(consumerNumber);
                hideKeyBoard(sub_division_code);
                hideKeyBoard(amount);
                if (consumerNumber.getText().toString().isEmpty()) {
                    try {
                        if (!numberField.getString("eg").isEmpty() || numberField.getString("eg").isBlank()){
                            Toast.makeText(this, numberField.getString("eg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.d("TAG_DATA", "onSuccessOther: "+e.getMessage());
                    }
                } else {
                    billFetch(id,
                            consumerNumber.getText().toString() + strId,
                            sub_division_code.getText().toString() + strId2,
                            amount.getText().toString().trim());
                }

            });
            if (numberField.getBoolean("is_select")) {
                spinnerText.setHint(numberField.getString("eg"));
                spinnerData.add(new SpinnerData("0", "---Select---"));
                JSONArray arr = numberField.getJSONArray("select_values");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    spinnerData.add(new SpinnerData(
                            obj.getString("id"),
                            obj.getString("value")
                    ));
                }
                for (SpinnerData data1 : spinnerData) {
                    spinnerStrData.add(data1.getValue());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_data, R.id.title, spinnerStrData);
                spinner.setAdapter(adapter);
                spinner_layout.setVisibility(VISIBLE);
                spinnerText.setVisibility(VISIBLE);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            strId = spinnerData.get(position).getValue();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else {

                if (is_billFetch.equals("1")) {
                    consumerNumber.setVisibility(VISIBLE);
                    Consumer_Number.setVisibility(VISIBLE);
                    consumerNumberText.setVisibility(VISIBLE);
                    star_img.setVisibility(VISIBLE);
                }
                else {
                    consumerNumber.setVisibility(GONE);
                    Consumer_Number.setVisibility(GONE);
                    consumerNumberText.setVisibility(GONE);
                    star_img.setVisibility(GONE);
                }
                consumerNumber.setHint(numberField.getString("field_name"));
                Consumer_Number.setText(numberField.getString("field_name"));
                consumerNumberText.setText(numberField.getString("eg"));
//                amount_layout.setText(numberField.getString("field_name"));
//                amount.setHint(numberField.getString("eg"));

                int maxLength = Integer.parseInt(numberField.getString("max_length"));
                InputFilter[] filters = new InputFilter[1];
                filters[0] = new InputFilter.LengthFilter(maxLength);
                consumerNumber.setFilters(filters);
                consumerNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().length() < maxLength){
                            showSoftKeyboard(consumerNumber);
                            if (is_billFetch.equals("1")) {
                                txtBillFetch.setVisibility(VISIBLE);
                                btnProcess.setVisibility(GONE);
                                proceedToPay.setVisibility(VISIBLE);
                            } else {
                                txtBillFetch.setVisibility(GONE);
                                amount_layout.setVisibility(VISIBLE);
                                amount.setVisibility(VISIBLE);
                                btnProcess.setVisibility(VISIBLE);
                                proceedToPay.setVisibility(GONE);
                            }
                            if (type.equals("Fastag")){
                                amount_layout.setVisibility(GONE);
                                amount.setVisibility(GONE);
                                amount.setText("");
                            }
                            constBillInfo.setVisibility(GONE);
                        }
                    }
                });

                if (numberField.getString("type").toUpperCase().equals("numeric".toUpperCase())) {
                    consumerNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (numberField.getString("type").equals("alphanumeric")) {
                    consumerNumber.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    Log.d("TAG_ELECTRICITY", "onSuccessOther: " + numberField.getString("type"));
                }
            }

            String jsonField1Data = object.getString("field1_data");
            JSONObject field1Data = new JSONObject(jsonField1Data);
            if (a > 1) {
                if (jsonField1Data == null) {
                    Log.d("TAG_ELECTRICITY", "onSuccessOther2: " + jsonField1Data);
                }
                if (field1Data.getBoolean("is_select")) {
                    spinnerText2.setText(field1Data.getString("eg"));
                    spinnerData2.add(new SpinnerData("0", "---Select---"));
                    JSONArray arr = field1Data.getJSONArray("select_values");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        spinnerData2.add(new SpinnerData(
                                obj.getString("id"),
                                obj.getString("value")
                        ));
                    }
                    for (SpinnerData data1 : spinnerData2) {
                        spinnerStrData2.add(data1.getValue());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_data, R.id.title, spinnerStrData2);
                    spinner2.setAdapter(adapter);
                    spinner_layout2.setVisibility(VISIBLE);
                    spinnerText2.setVisibility(VISIBLE);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                strId2 = spinnerData2.get(position).getId();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    sub_division_code_layout.setText(field1Data.getString("field_name"));
                    sub_division_code.setHint(field1Data.getString("eg"));
                    sub_division_code_layout.setVisibility(VISIBLE);
                    sub_division_code.setVisibility(VISIBLE);

                    int field1DataMaxLength = Integer.parseInt(field1Data.getString("max_length"));
                    InputFilter[] filters2 = new InputFilter[1];
                    filters2[0] = new InputFilter.LengthFilter(field1DataMaxLength);
                    sub_division_code.setFilters(filters2);

                    sub_division_code.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.toString().length() < field1DataMaxLength){
                                showSoftKeyboard(sub_division_code);
                                if (is_billFetch.equals("1")) {
                                    txtBillFetch.setVisibility(VISIBLE);
                                    btnProcess.setVisibility(GONE);
                                    proceedToPay.setVisibility(VISIBLE);
                                } else {
                                    txtBillFetch.setVisibility(GONE);
                                    amount_layout.setVisibility(VISIBLE);
                                    amount.setVisibility(VISIBLE);
                                    btnProcess.setVisibility(VISIBLE);
                                    proceedToPay.setVisibility(GONE);
                                }
                                constBillInfo.setVisibility(GONE);
                            }
                        }
                    });

                    if (field1Data.getString("type").toUpperCase().equals("numeric".toUpperCase())) {
                        sub_division_code.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else if (field1Data.getString("type").toUpperCase().equals("alphanumeric".toUpperCase())) {
                        sub_division_code.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else {
                        Log.d("TAG_ELECTRICITY", "onSuccessOther: " + numberField.getString("type"));
                    }
                }
            }

            String jsonField2Data = object.getString("field2_data");
            JSONObject field2Data = new JSONObject(jsonField2Data);
            if (a > 2) {
                if (jsonField2Data == null) {
                    Log.d("TAG_ELECTRICITY", "onSuccessOther2: " + jsonField2Data);
                }

                if (field2Data.getBoolean("is_select")) {
                    spinnerText3.setText(field2Data.getString("eg"));
                    spinnerData.add(new SpinnerData("0", "---Select---"));
                    JSONArray arr = field2Data.getJSONArray("select_values");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        spinnerData.add(new SpinnerData(
                                obj.getString("id"),
                                obj.getString("value")
                        ));
                    }
                    setSpinner(spinnerData, spinner3, spinnerText3, spinner_layout3);
                } else {
                    amount_layout.setText(field2Data.getString("field_name"));
                    amountText.setText(field2Data.getString("eg"));
                    amount_layout.setVisibility(VISIBLE);
                    amountText.setVisibility(VISIBLE);
                    amount.setVisibility(VISIBLE);
                    amount.setHint(field2Data.getString("eg"));

                    int field2DataMaxLength = Integer.parseInt(field2Data.getString("max_length"));
                    InputFilter[] filters3 = new InputFilter[1];
                    filters3[0] = new InputFilter.LengthFilter(field2DataMaxLength);
                    amount.setFilters(filters3);

                    amount.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.toString().length() < field2DataMaxLength){
                                showSoftKeyboard(amount);
                                if (is_billFetch.equals("1")) {
                                    txtBillFetch.setVisibility(VISIBLE);
                                    btnProcess.setVisibility(GONE);
                                    proceedToPay.setVisibility(VISIBLE);
                                } else {
                                    txtBillFetch.setVisibility(GONE);
                                    amount_layout.setVisibility(VISIBLE);
                                    amount.setVisibility(VISIBLE);
                                    btnProcess.setVisibility(VISIBLE);
                                    proceedToPay.setVisibility(GONE);
                                }
                                constBillInfo.setVisibility(GONE);
                            }
                        }
                    });

                    if (field2Data.getString("type").toUpperCase().equals("numeric".toUpperCase())) {
                        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    } else if (field2Data.getString("type").toUpperCase().equals("alphanumeric".toUpperCase())) {
                        amount.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else {
                        Log.d("TAG_ELECTRICITY", "onSuccessOther: " + numberField.getString("type"));
                    }
                }
            }

            String jsonField3Data = object.getString("field3_data");
            JSONObject field3Data = new JSONObject(jsonField3Data);
            if (a > 3) {
                if (jsonField2Data == null) {
                    Log.d("TAG_ELECTRICITY", "onSuccessOther3: " + jsonField3Data);
                }
                if (field3Data.getBoolean("is_select")) {
                    spinnerText3.setText(field3Data.getString("eg"));
                    spinnerData.add(new SpinnerData("0", "---Select---"));
                    JSONArray arr = field3Data.getJSONArray("select_values");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        spinnerData.add(new SpinnerData(
                                obj.getString("id"),
                                obj.getString("value")
                        ));
                    }
                } else {
                }
            }
        }catch (Exception e){
            Log.d("TAG_DATA", "createLayout: "+e.getMessage());
        }
    }

    private void setSpinner(@NonNull List<SpinnerData> data, Spinner sp, TextView spTxt, LinearLayout layout) {
        for (SpinnerData data1: data){
            spinnerStrData.add(data1.getValue());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_data, R.id.title, spinnerStrData);
        sp.setAdapter(adapter);
        layout.setVisibility(VISIBLE);
        spTxt.setVisibility(VISIBLE);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    strId = spinnerData.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void billFetch(String operatorId, String numberFieldData, String field1Data, String field2Data) {
        mDefaultPresenter.fetchBillInfo(device_id, operatorId, numberFieldData, field1Data, field2Data);
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
            amtPay = amount;
            if (warning_message.isEmpty()) {
                binding.warningMessage.setVisibility(GONE);
            } else {
                binding.warningMessage.setVisibility(VISIBLE);
                binding.warningMessage.setText(warning_message);
            }
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
                    if (!s.toString().isEmpty()){
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
                mDefaultPresenter.doElectricityRecharge(number + "", operator + "",
                        amount + "", type + "",
                        std_code + "", sub_division + "",
                        circle + "", ac_number + "", device_id + "",mPin);
            });
            dialog.show();
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
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("Oops!");
                _binding.txtSlug.setVisibility(GONE);
                Glide.with(this).asGif().load(R.drawable.animated_wrong).into(_binding.imgGif);
                _binding.btnComplete.setBackgroundResource(R.drawable.failed_button);
            } else if (status.toUpperCase().equals("PENDING")){
                _binding.btnComplete.setBackgroundResource(R.drawable.pending_button);
                _binding.txtTitle.setText("Pending");
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("CHILL!");
                Glide.with(this).asGif().load(R.drawable.animated_pending).into(_binding.imgGif);
            }else {
                _binding.btnComplete.setBackgroundResource(R.drawable.green_button);
                _binding.txtTitle.setText("Success");
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("Thank You!");
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
                        if (jsonObject.getString("status").toLowerCase().equals("success")){
                            intent.putExtra("bps", "1");
                        }else {
                            intent.putExtra("bps", "0");
                        }
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

    private void openGatewaysDialog(String data) {
        try {
            ScreenRechargePaymentLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.screen_recharge_payment_layout, null, false);

            dialog1 = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            dialog1.setContentView(binding.getRoot());
            dialog1.setCancelable(true);
            changeStatusBarColor(dialog1);
            bottomSheet = dialog1.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }

            JSONObject json = new JSONObject(data);
            binding.txtTransactionAmountValue.setText("\u20b9" + amtPay);
            if (mDatabase != null) {
                if (mDatabase.getEarnings() != null) {
                    binding.txtWalletAmountValue.setText("₹" + mDatabase.getEarnings());
                }
            }
            binding.txtTotalDiscountValue.setText("₹" + json.getString("discount"));
            releaseAmount = (Double.parseDouble(amtPay) - Double.parseDouble(json.getString("discount")));
            binding.txtPayableAmtValue.setText("₹" + releaseAmount);

            binding.btnPay.setText("Add on ₹" + json.getInt("required"));
            binding.btnPay.setOnClickListener(v -> {
                try {
                    mDefaultPresenter.addInsufficientBalance(device_id, json.getString("required"));
                    dialog1.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            dialog1.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

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
    public boolean onTouch(View v, MotionEvent event) {
        hideKeyBoard(consumerNumber);
//        hideKeyBoard(provider_name);
        hideKeyBoard(sub_division_code);
        hideKeyBoard(field4);
        hideKeyBoard(amount);
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_layout) {
            onBackPressed();
        }
    }
}