package com.vuvrecharge.modules.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.activities.newActivities.ElectricityActivity;
import com.vuvrecharge.modules.adapter.BharatBillPayAdapter;
import com.vuvrecharge.modules.model.BharatBillPayModel;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NotifyDataSetChanged,NonConstantResourceId")
public class BharatBillPayActivity extends BaseActivity implements BharatBillPayAdapter.ItemClickListener, View.OnClickListener {

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
//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;
    String stringTitle = null;
    BharatBillPayAdapter adapter;
    List<BharatBillPayModel> bbpsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bharat_bill_pay);
        ButterKnife.bind(this);
//        mDefaultPresenter = new DefaultPresenter(this);
        stringTitle = getIntent().getStringExtra("title");
        title.setText(stringTitle);
        String data = getIntent().getStringExtra("data");
        adapter = new BharatBillPayAdapter(this, bbpsList, this);
        recyclerView.setAdapter(adapter);
        mToolbar.setOnClickListener(this);
//        swipeRefreshLayout.setRefreshing(false);
        if (data != null){
            try {
                JSONObject object = new JSONObject(data);
                JSONArray array = object.getJSONArray("bbps_pay_data");
                for (int i = 0; i < array.length(); i++){
                    BharatBillPayModel payModel = new BharatBillPayModel();
                    payModel.setLogo(array.getJSONObject(i).getString("logo"));
                    payModel.setTitle(array.getJSONObject(i).getString("title"));
                    payModel.setRedirect_link(array.getJSONObject(i).getString("redirect_link"));
                    payModel.setUp_down_msg(array.getJSONObject(i).getString("up_down_msg"));
                    payModel.setHighlight_text(array.getJSONObject(i).getString("highlight_text"));
                    bbpsList.add(payModel);
                    adapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                Log.d("TAG_BBPS", "onSuccess: "+e.getMessage());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "Bill Pay");
    }

    @Override
    public void onClickListener(@NonNull String name) {
        Intent intent;
        switch (name){
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
            case "Insurance":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Insurance");
                startActivity(intent);
                break;
            case "Cylinder":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Cylinder Bill");
                startActivity(intent);
                break;
            case "Fastag":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Fastag");
                startActivity(intent);
                break;
            case "Postpaid":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Postpaid Recharge");
                startActivity(intent);
                break;
            case "GiftCards":
                intent = new Intent(getActivity(), BillActivity.class);
                intent.putExtra("title", "Purchase Gift cards");
                startActivity(intent);
                break;
            case "Landline":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Broadband/Landline");
                startActivity(intent);
                break;
            case "DTH":
                intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("title", "DTH Recharge");
                startActivity(intent);
                break;
            case "Gas":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Gas Bill");
                startActivity(intent);
                break;
            case "Prepaid":
                intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("title", "Prepaid Recharge");
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