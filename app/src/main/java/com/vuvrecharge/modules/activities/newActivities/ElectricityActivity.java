package com.vuvrecharge.modules.activities.newActivities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.ElectricityOperatorAdapter;
import com.vuvrecharge.modules.model.OperatorData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.OperatorPreferences;
import com.vuvrecharge.utils.LocaleHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ElectricityActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    private DefaultPresenter mDefaultPresenter;
    @BindView(R.id.toolbar_layout)
    LinearLayout toolbar;
    @BindView(R.id.title)
    TextView title;
    //    @BindView(R.id.help)
//    TextView help;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.txtNoData)
    TextView txtNoData;
    @BindView(R.id.txtSearch)
    TextView txtSearch;
    @BindView(R.id.txtCancel)
    ImageView txtCancel;
    @BindView(R.id.electricityListView)
    RecyclerView electricityListView;
    @BindView(R.id.search_electricity)
    EditText search_electricity;

    //    @BindView(R.id.search_electricity_layout)
//    TextInputLayout search_electricity_layout;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String type = null;
    private ArrayList<OperatorData> operator_list = new ArrayList<>();
    private List<OperatorData> operatorDataList = new ArrayList<>();
    private ElectricityOperatorAdapter adapter;
    String title1;

    OperatorPreferences operatorPreferences;
    HashMap<String, String> map;

    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);
        ButterKnife.bind(this);
        toolbar.setOnClickListener(this);
        title1 = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        title.setText(title1);
//        setStatusBarGradiant(this);
        mDefaultPresenter = new DefaultPresenter(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (title1 != null) {
            search_electricity.setHint("Search Operater");
            operatorPreferences = new OperatorPreferences(this, type);
            map = operatorPreferences.getData();
            if (map.get("type") != null) {
                operatorDataList.clear();
                Gson gson = new Gson();
                warning_message = map.get("message");
                String operatorImg = map.get("operator_img");
                String operatorDummy = map.get("operator_dunmy_img");
                Log.d("Operator_Image", operatorImg + operatorDummy);
                Type type_ = new TypeToken<List<OperatorData>>() {
                }.getType();
                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
                for (OperatorData data : operatorDataList) {
                    data.setOperator_img(operatorImg);
                    data.setOperator_dunmy_img(operatorDummy);
                }
                this.operatorDataList = operatorDataList;
            } else {
                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
            }
        }

        txtSearch.setOnClickListener(v -> {
            search_electricity.setVisibility(VISIBLE);
            txtSearch.setVisibility(GONE);
            txtCancel.setVisibility(VISIBLE);
            showSoftKeyboard(search_electricity);
            search_electricity.setFocusable(true);
            search_electricity.requestFocus();
        });
        txtCancel.setOnClickListener(v -> {
            search_electricity.setVisibility(GONE);
            txtSearch.setVisibility(VISIBLE);
            txtCancel.setVisibility(GONE);
            search_electricity.setText("");
            search_electricity.clearFocus();
            hideKeyBoard(search_electricity);

        });

//        else if (title1.equals("Postpaid Recharge")) {
//            search_electricity.setHint("Search by postpaid name");
//            type = "Postpaid";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                Gson gson = new Gson();
//                warning_message = map.get("message");
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        }else if (title1.equals("Fastag")) {
//            search_electricity.setHint("Search by Fastag provider name");
//            type = "Fastag";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                Gson gson = new Gson();
//                warning_message = map.get("message");
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        }else if (title1.equals("Insurance")) {
//            search_electricity.setHint("Search by Insurance provider name");
//            type = "Insurance";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                Gson gson = new Gson();
//                warning_message = map.get("message");
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        }else if (title1.equals("Cylinder Bill")) {
//            search_electricity.setHint("Search by Cylinder provider name");
//            type = "Cylinder";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                Gson gson = new Gson();
//                warning_message = map.get("message");
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        }else if (title1.equals("Gas Bill")) {
//            search_electricity.setHint("Search by Gas provider name");
//            type = "Gas";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        }else if (title1.equals("Water Bill")) {
//            search_electricity.setHint("Search by Water board name");
//            type = "Water";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                Gson gson = new Gson();
//                warning_message = map.get("message");
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        }else if (title1.equals("Broadband/Landline")) {
//            search_electricity.setHint("Search by Water board name");
//            type = "Landline";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Credit Card Payment")) {
//            search_electricity.setHint("Search by Credit Card name");
//            type = "CreditCardPayment";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Loan Re payment")) {
//            search_electricity.setHint("Search by Loan Repayment name");
//            type = "LoanRePayment";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Cable TV")) {
//            search_electricity.setHint("Search by Cable TV board name");
//            type = "CableTV";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Municipal Tax")) {
//            search_electricity.setHint("Search by Water board name");
//            type = "MunicipalTax";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Housing Society")) {
//            search_electricity.setHint("Search by Water board name");
//            type = "HousingSociety";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Club Association")) {
//            search_electricity.setHint("Search by Club Association name");
//            type = "ClubAssociation";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//
//        } else if (title1.equals("Hospital Pathology")) {
//            search_electricity.setHint("Search by Hospital Pathology name");
//            type = "HospitalPathology";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Subscription Fees")) {
//            search_electricity.setHint("Search by Subscription Fees name");
//            type = "Subscriptions";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                Gson gson = new Gson();
//                warning_message = map.get("message");
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Donation")) {
//            search_electricity.setHint("Search by Donation name");
//            type = "Donation";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                Gson gson = new Gson();
//                warning_message = map.get("message");
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Recurring Deposit")) {
//            search_electricity.setHint("Search by Recurring Deposit name");
//            type = "RecurringDeposit";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                Gson gson = new Gson();
//                warning_message = map.get("message");
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Prepaid Meter")) {
//            search_electricity.setHint("Search by Prepaid Meter name");
//            type = "PrepaidMeter";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("NCMC Recharge")) {
//            search_electricity.setHint("Search by NCMC Recharge name");
//            type = "NCMCRecharge";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Municipal Services")) {
//            search_electricity.setHint("Search by Municipal Services name");
//            type = "MunicipalServices";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        } else if (title1.equals("Education Fees")) {
//            search_electricity.setHint("Search by Education Fees name");
//            type = "EducationFees";
//            operatorPreferences = new OperatorPreferences(this,type);
//            map = operatorPreferences.getData();
//            if (map.get("type") != null){
//                operatorDataList.clear();
//                warning_message = map.get("message");
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<OperatorData>>() {
//                }.getType();
//                List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
//                this.operatorDataList = operatorDataList;
//            }else {
//                mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
//            }
//        }
        swipeRefreshLayout.setOnRefreshListener(this::loadOperatorData);
        loadOperatorData();

//        help.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), SupportActivity.class);
//            startActivity(intent);
//        });

        search_electricity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    swipeRefreshLayout.setRefreshing(false);
                    searchData(s.toString());
                } else {
                    txtNoData.setVisibility(GONE);
                    loadOperatorData();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "Electricity");
    }

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

    private void searchData(@NonNull String searchableData) {
        ArrayList<OperatorData> data = new ArrayList<>();
        for (OperatorData operatorData : operator_list) {
            if (operatorData.getName().toUpperCase().toLowerCase().contains(searchableData.toUpperCase().toLowerCase())) {
                data.add(operatorData);
                txtNoData.setVisibility(GONE);
            }
        }
        adapter.searchList(data);
        if (data == null || data.isEmpty()) {
            txtNoData.setVisibility(VISIBLE);
        }
    }

    private void loadOperatorData() {
        operator_list = new ArrayList<>();
        for (OperatorData operatorData : operatorDataList) {
            operator_list.add(operatorData);
            swipeRefreshLayout.setRefreshing(false);
        }
        electricityListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ElectricityOperatorAdapter(getActivity(), operator_list, warning_message, title1, type);
        electricityListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {
        showErrorR(error);
    }

    @Override
    public void onSuccess(String message) {

    }

    String warning_message = "";

    @Override
    public void onSuccess(String message, String second_message) {
        try {
            swipeRefreshLayout.setRefreshing(true);
            Log.d("Operator_Image1", message);
            JSONObject jsonObject = new JSONObject(message);
            Log.d("warning_message", message);
            OperatorData operatorData1 = new OperatorData();
            String operator_path = jsonObject.getString("operator_img");
            String operator_dunmy_img = jsonObject.getString("operator_dunmy_img");
            Log.d("Operator_Image", operator_path + "  " + operatorData1.getOperator_img());
            Log.d("Operator_Image", operator_dunmy_img + "  " + operatorData1.getOperator_dunmy_img());
            Gson gson = new Gson();
            Type type_ = new TypeToken<List<OperatorData>>() {
            }.getType();
            List<OperatorData> operatorDataList = gson.fromJson(jsonObject.getString("operators"), type_);
            for (OperatorData op : operatorDataList) {
                op.setOperator_img(operator_path != null ? operator_path : "");
                op.setOperator_dunmy_img(operator_dunmy_img != null ? operator_dunmy_img : "");
            }
            if (operatorDataList.size() > 0) {
                warning_message = jsonObject.getString("message");
                this.operatorDataList = operatorDataList;
                operatorPreferences.setOperator(type, jsonObject.getString("operators"));
                operatorPreferences.setDummyImage(operator_path, operator_dunmy_img);
                operatorPreferences.warringMessage(jsonObject.getString("message"));
                adapter.notifyDataSetChanged();
                txtNoData.setVisibility(GONE);
                electricityListView.setVisibility(VISIBLE);
            } else {
                electricityListView.setVisibility(GONE);
                txtNoData.setVisibility(VISIBLE);
            }
            loadOperatorData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessOther(String message) {

    }

    @Override
    public void onSuccessOther(String data, String data_other) {

    }

    @Override
    public void onShowDialog(String message) {
        if (bottomSheet != null) {
            showLoading(loading_dialog);
        }
        showLoading(loading);
    }

    @Override
    public void onHideDialog() {
        if (bottomSheet != null) {
            hideLoading(loading_dialog);
        }
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
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }
}