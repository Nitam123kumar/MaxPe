package com.vuvrecharge.modules.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.activities.newActivities.ElectricityActivity;
import com.vuvrecharge.modules.adapter.BharatBillPayAdapter;
import com.vuvrecharge.modules.adapter.FinancialServicesAdapter;
import com.vuvrecharge.modules.adapter.OTTSubscriptionsAdapter;
import com.vuvrecharge.modules.adapter.RecyclerViewSliderAdapter;
import com.vuvrecharge.modules.model.BharatBillPayModel;
import com.vuvrecharge.modules.model.FinancialServicesData;
import com.vuvrecharge.modules.model.OTTSubscriptionsData;
import com.vuvrecharge.modules.model.RechargeAndBillPaymentData;
import com.vuvrecharge.modules.model.YoutubeSlides;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.APIStorePreferences;
import com.vuvrecharge.utils.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NotifyDataSetChanged,NonConstantResourceId")
public class BharatBillPayActivity extends BaseActivity implements DefaultView, BharatBillPayAdapter.ItemClickListener, FinancialServicesAdapter.ItemClickListener, View.OnClickListener, OTTSubscriptionsAdapter.ItemClickListener {

    private DefaultPresenter mDefaultPresenter;


    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.recycler_view_provider)
    RecyclerView recyclerView;
    @BindView(R.id.financial_services_recyclerView)
    RecyclerView financial_services_recyclerView;
    @BindView(R.id.utility_bills_recyclerView)
    RecyclerView utility_bills_recyclerView;
    @BindView(R.id.ott_recharge_recyclerView)
    RecyclerView ott_recharge_recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;
    String stringTitle = null;
    BharatBillPayAdapter adapter;
    FinancialServicesAdapter adapter1;
    FinancialServicesAdapter utility_bills_adapter;
    OTTSubscriptionsAdapter ottAdapter;
    List<String> ott_List;
    List<BharatBillPayModel> bbpsList = new ArrayList<>();
    List<OTTSubscriptionsData> oTTList = new ArrayList<>();
    List<BharatBillPayModel> rechargeAndBillPaymentDataList = new ArrayList<>();
    List<FinancialServicesData> financialServicesList = new ArrayList<>();
    List<FinancialServicesData> utility_billsList = new ArrayList<>();

    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bharat_bill_pay);
        ButterKnife.bind(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mDefaultPresenter = new DefaultPresenter(this);
        stringTitle = getIntent().getStringExtra("title");
        title.setText(stringTitle);
        String data = getIntent().getStringExtra("data");
        adapter = new BharatBillPayAdapter(this, rechargeAndBillPaymentDataList, this);
        recyclerView.setAdapter(adapter);
        mToolbar.setOnClickListener(this);
//        setStatusBarGradiant(this);
//        swipeRefreshLayout.setRefreshing(false);
        financial_services_recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter1 = new FinancialServicesAdapter(this, financialServicesList, this);
        financial_services_recyclerView.setAdapter(adapter1);

        utility_bills_recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        utility_bills_adapter = new FinancialServicesAdapter(this, utility_billsList, this);
        utility_bills_recyclerView.setAdapter(utility_bills_adapter);
        ;
        try {
            ott_List = new ArrayList<>();
            List<OTTSubscriptionsData> ottItem = (mDatabase.getUserData().getLogoSliders());
            for (OTTSubscriptionsData ottData : ottItem) {
                ott_List.add(mDatabase.getUserData().getOtt_slides_path() + "/" + ottData.getLogo());

                Log.d("OTTData", ottData.getLogo() + " " + ottData.getTitle());
            }
            oTTList.addAll(ottItem);
            ottAdapter = new OTTSubscriptionsAdapter(this, oTTList, ott_List, this);
            ott_recharge_recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            ott_recharge_recyclerView.setAdapter(ottAdapter);


        } catch (Exception e) {
            Log.d("TAG_BBPS", "onSuccess: " + e.getMessage());
        }

//        }

        var lastFetch = mDatabase.getTime();
        var now = System.currentTimeMillis();


        if (now - lastFetch > 24 * 60 * 60) {
            mDefaultPresenter.getAllRechargeServices(device_id);
        } else {

            String operatorData = mDatabase.getOperatorString();
            if (!operatorData.isEmpty()) {
                try {
                    JSONObject object = new JSONObject(operatorData);
                    apiStoreData(object);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                mDefaultPresenter.getAllRechargeServices(device_id);
            }
        }
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void apiStoreData(JSONObject object) {
        try {

            if (object.has("services_by_categ")) {
                JSONObject servicesObject = object.getJSONObject("services_by_categ");

                rechargeAndBillPaymentDataList.clear();
                if (servicesObject.has("Recharge_&_Bill_Payments")) {
                    JSONArray rechargeArray = servicesObject.getJSONArray("Recharge_&_Bill_Payments");
                    Log.d("Recharge_&_Bill_Payments", String.valueOf(rechargeArray));
                    for (int i = 0; i < rechargeArray.length(); i++) {
                        JSONObject obj = rechargeArray.getJSONObject(i);
                        BharatBillPayModel model = new BharatBillPayModel();
                        model.setLogo(obj.getString("logo"));
                        model.setTitle(obj.getString("title"));
                        model.setRedirect_link(obj.getString("redirect_link"));
                        model.setUp_down_msg(obj.getString("up_down_msg"));
                        model.setHighlight_text(obj.getString("highlight_text"));
                        model.setData(obj.getString("activity_data"));
                        rechargeAndBillPaymentDataList.add(model);
                        adapter.searchData(rechargeAndBillPaymentDataList);
                        adapter.notifyDataSetChanged();
                    }
                }

                if (servicesObject.has("Utilities_Bill")) {
                    JSONArray utilitiesArray = servicesObject.getJSONArray("Utilities_Bill");
                    utility_billsList.clear();
                    for (int i = 0; i < utilitiesArray.length(); i++) {
                        JSONObject obj = utilitiesArray.getJSONObject(i);
                        FinancialServicesData model = new FinancialServicesData();
                        model.setLogo(obj.getString("logo"));
                        model.setTitle(obj.getString("title"));
                        model.setRedirect_link(obj.getString("redirect_link"));
                        model.setUp_down_msg(obj.getString("up_down_msg"));
                        model.setHighlight_text(obj.getString("highlight_text"));
                        model.setData(obj.getString("activity_data"));
                        utility_billsList.add(model);
                        utility_bills_adapter.searchData(utility_billsList);
                    }
                }

                if (servicesObject.has("Financial_Services")) {
                    JSONArray financialArray = servicesObject.getJSONArray("Financial_Services");
                    financialServicesList.clear();
                    for (int i = 0; i < financialArray.length(); i++) {
                        JSONObject obj = financialArray.getJSONObject(i);
                        FinancialServicesData model = new FinancialServicesData();
                        model.setLogo(obj.getString("logo"));
                        model.setTitle(obj.getString("title"));
                        model.setRedirect_link(obj.getString("redirect_link"));
                        model.setUp_down_msg(obj.getString("up_down_msg"));
                        model.setHighlight_text(obj.getString("highlight_text"));
                        model.setData(obj.getString("activity_data"));
                        financialServicesList.add(model);
                        Log.d("financialServicesList", String.valueOf(obj));
                        adapter1.notifyDataSetChanged();
                        adapter1.searchData(financialServicesList);
                    }
                }

                if (servicesObject.has("Insurance")) {
                    JSONArray insuranceArray = servicesObject.getJSONArray("Insurance");
                    for (int i = 0; i < insuranceArray.length(); i++) {
                        JSONObject obj = insuranceArray.getJSONObject(i);
                        BharatBillPayModel model = new BharatBillPayModel();
                        model.setLogo(obj.getString("logo"));
                        model.setTitle(obj.getString("title"));
                        model.setRedirect_link(obj.getString("redirect_link"));
                        model.setUp_down_msg(obj.getString("up_down_msg"));
                        model.setHighlight_text(obj.getString("highlight_text"));
                        model.setData(obj.getString("activity_data"));
//                        insuranceDataList.add(model);
                    }
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "Bill Pay");
    }


    private void refreshData() {
        mDefaultPresenter.getAllRechargeServices(device_id);
    }

    @Override
    public void onClickListener(@NonNull String redirection_type, String intent_name, String activityExtraData, String link, String logo) {
        Intent intent;

        Class<?> clazz = null;
        try {
            clazz = Class.forName(intent_name);
            intent = new Intent(getActivity(), clazz);
            JSONObject object = new JSONObject(activityExtraData);

            if (object.length() > 0) {

                Iterator<String> keys = object.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = object.optString(key, "");

                    intent.putExtra(key, value);
                }

            }
            intent.putExtra("logo", logo);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


//        switch (name){
//            case "Electricity":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Electricity Bill");
//                startActivity(intent);
//                break;
//            case "Water":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Water Bill");
//                startActivity(intent);
//                break;
//            case "Insurance":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Insurance");
//                startActivity(intent);
//                break;
//            case "Cylinder":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Cylinder Bill");
//                startActivity(intent);
//                break;
//            case "Fastag":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Fastag");
//                startActivity(intent);
//                break;
//            case "Postpaid":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Postpaid Recharge");
//                startActivity(intent);
//                break;
//            case "GiftCards":
//                intent = new Intent(getActivity(), BillActivity.class);
//                intent.putExtra("title", "Purchase Gift cards");
//                startActivity(intent);
//                break;
//            case "Landline":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Broadband/Landline");
//                startActivity(intent);
//                break;
//            case "DTH":
//                intent = new Intent(getActivity(), RechargeActivity.class);
//                intent.putExtra("title", "DTH Recharge");
//                startActivity(intent);
//                break;
//            case "Gas":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Gas Bill");
//                startActivity(intent);
//                break;
//            case "Prepaid":
//                intent = new Intent(getActivity(), RechargeActivity.class);
//                intent.putExtra("title", "Prepaid Recharge");
//                startActivity(intent);
//                break;
//            case "CreditCardPayment":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Credit Card Payment");
//                startActivity(intent);
//                break;
//            case "LoanRePayment":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Loan Re payment");
//                startActivity(intent);
//                break;
//            case "CableTV":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Cable TV");
//                startActivity(intent);
//                break;
//            case "MunicipalTax":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Municipal Tax");
//                startActivity(intent);
//                break;
//            case "HousingSociety":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Housing Society");
//                startActivity(intent);
//                break;
//            case "ClubAssociation":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Club Association");
//                startActivity(intent);
//                break;
//            case "HospitalPathology":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Hospital Pathology");
//                startActivity(intent);
//                break;
//            case "Subscriptions":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Subscription Fees");
//                startActivity(intent);
//                break;
//            case "Donation":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Donation");
//                startActivity(intent);
//                break;
//            case "Hospital":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Hospital");
//                startActivity(intent);
//                break;
//            case "RecurringDeposit":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Recurring Deposit");
//                startActivity(intent);
//                break;
//            case "PrepaidMeter":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Prepaid Meter");
//                startActivity(intent);
//                break;
//            case "NCMCRecharge":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "NCMC Recharge");
//                startActivity(intent);
//                break;
//            case "MunicipalServices":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Municipal Services");
//                startActivity(intent);
//                break;
//            case "EducationFees":
//                intent = new Intent(getActivity(), ElectricityActivity.class);
//                intent.putExtra("title", "Education Fees");
//                startActivity(intent);
//                break;
//            default:
//                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
//        }
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

    @Override
    public void onError(String error) {

    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    public void onSuccess(String data, String data_other) {
        try {
            JSONObject object = new JSONObject(data);
            refresh_layout.setRefreshing(false);

            mDatabase.putOperatorString(data);
            mDatabase.putTime(System.currentTimeMillis());

            if (object.has("services_by_categ")) {
                JSONObject servicesObject = object.getJSONObject("services_by_categ");

                rechargeAndBillPaymentDataList.clear();
                if (servicesObject.has("Recharge_&_Bill_Payments")) {
                    JSONArray rechargeArray = servicesObject.getJSONArray("Recharge_&_Bill_Payments");
                    Log.d("Recharge_&_Bill_Payments", String.valueOf(rechargeArray));
                    for (int i = 0; i < rechargeArray.length(); i++) {
                        JSONObject obj = rechargeArray.getJSONObject(i);
                        BharatBillPayModel model = new BharatBillPayModel();
                        model.setLogo(obj.getString("logo"));
                        model.setTitle(obj.getString("title"));
                        model.setRedirect_link(obj.getString("redirect_link"));
                        model.setUp_down_msg(obj.getString("up_down_msg"));
                        model.setHighlight_text(obj.getString("highlight_text"));
                        model.setData(obj.getString("activity_data"));
                        rechargeAndBillPaymentDataList.add(model);
                        adapter.searchData(rechargeAndBillPaymentDataList);
                        adapter.notifyDataSetChanged();
                    }
                }

                if (servicesObject.has("Utilities_Bill")) {
                    JSONArray utilitiesArray = servicesObject.getJSONArray("Utilities_Bill");
                    utility_billsList.clear();
                    for (int i = 0; i < utilitiesArray.length(); i++) {
                        JSONObject obj = utilitiesArray.getJSONObject(i);
                        FinancialServicesData model = new FinancialServicesData();
                        model.setLogo(obj.getString("logo"));
                        model.setTitle(obj.getString("title"));
                        model.setRedirect_link(obj.getString("redirect_link"));
                        model.setUp_down_msg(obj.getString("up_down_msg"));
                        model.setHighlight_text(obj.getString("highlight_text"));
                        model.setData(obj.getString("activity_data"));
                        utility_billsList.add(model);
                        utility_bills_adapter.searchData(utility_billsList);
                    }
                }

                if (servicesObject.has("Financial_Services")) {
                    JSONArray financialArray = servicesObject.getJSONArray("Financial_Services");
                    financialServicesList.clear();
                    for (int i = 0; i < financialArray.length(); i++) {
                        JSONObject obj = financialArray.getJSONObject(i);
                        FinancialServicesData model = new FinancialServicesData();
                        model.setLogo(obj.getString("logo"));
                        model.setTitle(obj.getString("title"));
                        model.setRedirect_link(obj.getString("redirect_link"));
                        model.setUp_down_msg(obj.getString("up_down_msg"));
                        model.setHighlight_text(obj.getString("highlight_text"));
                        model.setData(obj.getString("activity_data"));
                        financialServicesList.add(model);
                        Log.d("financialServicesList", String.valueOf(obj));
                        adapter1.notifyDataSetChanged();
                        adapter1.searchData(financialServicesList);
                    }
                }

                if (servicesObject.has("Insurance")) {
                    JSONArray insuranceArray = servicesObject.getJSONArray("Insurance");
                    for (int i = 0; i < insuranceArray.length(); i++) {
                        JSONObject obj = insuranceArray.getJSONObject(i);
                        BharatBillPayModel model = new BharatBillPayModel();
                        model.setLogo(obj.getString("logo"));
                        model.setTitle(obj.getString("title"));
                        model.setRedirect_link(obj.getString("redirect_link"));
                        model.setUp_down_msg(obj.getString("up_down_msg"));
                        model.setHighlight_text(obj.getString("highlight_text"));
                        model.setData(obj.getString("activity_data"));
//                        insuranceDataList.add(model);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClickListener(String redirection_type, String intent_name, String extra_data) {
        Intent intent;

        Class<?> clazz = null;
        try {
            clazz = Class.forName(intent_name);
            intent = new Intent(getActivity(), clazz);
            JSONObject object = new JSONObject(extra_data);
            Log.e("extra_data",extra_data);

            if (object.length() > 0) {
                Iterator<String> keys = object.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = object.optString(key, "");

                    intent.putExtra(key, value);
                }

            }
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}