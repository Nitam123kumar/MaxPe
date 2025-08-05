package com.vuvrecharge.modules.activities.newActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.activities.BillActivity;
import com.vuvrecharge.modules.activities.RechargeActivity;
import com.vuvrecharge.modules.adapter.AllServiceAdapter;
import com.vuvrecharge.modules.model.DashboardMenu;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.utils.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllServicesActivity extends BaseActivity implements DefaultView, AllServiceAdapter.ItemClickListener, View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.recyclerViewAllService)
    RecyclerView recyclerViewAllService;
    ArrayList<DashboardMenu> bbpsList = new ArrayList<>();
    AllServiceAdapter adapter;
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_services);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        title.setText("All Services");
        String data = getIntent().getStringExtra("data");
        try {
            JSONObject object = new JSONObject(data);
            JSONArray bbpsArray = object.getJSONArray("bbps_pay_data");
            if (bbpsArray.length() > 0){
                for (int i=0; i < bbpsArray.length(); i++){
                    JSONObject bbpsObject = bbpsArray.getJSONObject(i);
                    DashboardMenu menu = new DashboardMenu();
                    menu.setLogo(bbpsObject.getString("logo"));
                    menu.setTitle(bbpsObject.getString("title"));
                    menu.setRedirect_link(bbpsObject.getString("redirect_link"));
                    menu.setService_type(bbpsObject.getString("service_type"));
                    menu.setHighlight_text(bbpsObject.getString("highlight_text"));
                    menu.setUp_down_msg(bbpsObject.getString("up_down_msg"));
                    bbpsList.add(menu);
                }
            }
            JSONArray payByApisArray = object.getJSONArray("dashboard_pay_data");
            ArrayList<DashboardMenu> payByApisList = new ArrayList<>();
            if (payByApisArray.length() > 0){
                for (int i=0; i < payByApisArray.length(); i++){
                    JSONObject payByApisObjects = payByApisArray.getJSONObject(i);
                    DashboardMenu menu = new DashboardMenu();
                    menu.setLogo(payByApisObjects.getString("logo"));
                    menu.setTitle(payByApisObjects.getString("title"));
                    menu.setRedirect_link(payByApisObjects.getString("redirect_link"));
                    menu.setService_type(payByApisObjects.getString("service_type"));
                    menu.setHighlight_text(payByApisObjects.getString("highlight_text"));
                    menu.setUp_down_msg(payByApisObjects.getString("up_down_msg"));
                    payByApisList.add(menu);
                }
            }
            bbpsList.addAll(payByApisList);
            adapter = new AllServiceAdapter(this,bbpsList,this);
            recyclerViewAllService.setAdapter(adapter);

        }catch (Exception e){
            Log.d("TAG_ALL", "onSuccess: "+e.getMessage());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "allServices");
    }

    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String data) {

    }
    @Override
    public void onSuccess(String data, String data_other) {
        try {
            JSONObject object = new JSONObject(data);
            JSONArray bbpsArray = object.getJSONArray("bbps_pay_data");
            if (bbpsArray.length() > 0){
                for (int i=0; i < bbpsArray.length(); i++){
                    JSONObject bbpsObject = bbpsArray.getJSONObject(i);
                    DashboardMenu menu = new DashboardMenu();
                    menu.setLogo(bbpsObject.getString("logo"));
                    menu.setTitle(bbpsObject.getString("title"));
                    menu.setRedirect_link(bbpsObject.getString("redirect_link"));
                    menu.setService_type(bbpsObject.getString("service_type"));
                    menu.setHighlight_text(bbpsObject.getString("highlight_text"));
                    menu.setUp_down_msg(bbpsObject.getString("up_down_msg"));
                    bbpsList.add(menu);
                }
            }
            JSONArray payByApisArray = object.getJSONArray("dashboard_pay_data");
            ArrayList<DashboardMenu> payByApisList = new ArrayList<>();
            if (payByApisArray.length() > 0){
                for (int i=0; i < payByApisArray.length(); i++){
                    JSONObject payByApisObjects = payByApisArray.getJSONObject(i);
                    DashboardMenu menu = new DashboardMenu();
                    menu.setLogo(payByApisObjects.getString("logo"));
                    menu.setTitle(payByApisObjects.getString("title"));
                    menu.setRedirect_link(payByApisObjects.getString("redirect_link"));
                    menu.setService_type(payByApisObjects.getString("service_type"));
                    menu.setHighlight_text(payByApisObjects.getString("highlight_text"));
                    menu.setUp_down_msg(payByApisObjects.getString("up_down_msg"));
                    payByApisList.add(menu);
                }
            }
            bbpsList.addAll(payByApisList);
            adapter = new AllServiceAdapter(this,bbpsList,this);
            recyclerViewAllService.setAdapter(adapter);

        }catch (Exception e){
            Log.d("TAG_ALL", "onSuccess: "+e.getMessage());
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
        showDialog(message);
    }

    @Override
    public void onHideDialog() {
        hideDialog();
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
    public void onClickListener(@NonNull String name) {
        Intent intent = null;
        switch (name){
            case "Prepaid":
                intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("title", "Prepaid Recharge");
                startActivity(intent);
                break;
            case "Postpaid":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Postpaid Recharge");
                startActivity(intent);
                break;
            case "DTH":
                intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("title", "DTH Recharge");
                startActivity(intent);
                break;
            case "Electricity":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Electricity Bill");
                startActivity(intent);
                break;
            case "Water":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Water Bill");
                startActivity(intent);
                break;
            case "Cylinder":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Cylinder Bill");
                startActivity(intent);
                break;
            case "Landline":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Broadband/Landline");
                startActivity(intent);
                break;
            case "GiftCards":
                intent = new Intent(getActivity(), BillActivity.class);
                intent.putExtra("title", "Purchase Gift cards");
                startActivity(intent);
                break;
            case "Fastag":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Fastag");
                startActivity(intent);
                break;
            case "Insurance":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Insurance");
                startActivity(intent);
                break;
            case "Gas":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Gas Bill");
                startActivity(intent);
                break;
            case "CreditCardPayment":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Credit Card Payment");
                startActivity(intent);
                break;
            case "LoanRePayment":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Loan Re payment");
                startActivity(intent);
                break;
            case "CableTV":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Cable TV");
                startActivity(intent);
                break;
            case "MunicipalTax":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Municipal Tax");
                startActivity(intent);
                break;
            case "HousingSociety":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Housing Society");
                startActivity(intent);
                break;
            case "ClubAssociation":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Club Association");
                startActivity(intent);
                break;
            case "HospitalPathology":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Hospital Pathology");
                startActivity(intent);
                break;
            case "Subscriptions":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Subscription Fees");
                startActivity(intent);
                break;
            case "Donation":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Donation");
                startActivity(intent);
                break;
            case "Hospital":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Hospital");
                startActivity(intent);
                break;
            case "RecurringDeposit":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Recurring Deposit");
                startActivity(intent);
                break;
            case "PrepaidMeter":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Prepaid Meter");
                startActivity(intent);
                break;
            case "NCMCRecharge":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "NCMC Recharge");
                startActivity(intent);
                break;
            case "MunicipalServices":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Municipal Services");
                startActivity(intent);
                break;
            case "EducationFees":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Education Fees");
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
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