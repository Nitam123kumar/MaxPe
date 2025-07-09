package com.vuvrecharge.modules.activities.newActivities;

import static com.vuvrecharge.api.ApiServices.IMAGE_LOGO;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.activities.MainActivity;
import com.vuvrecharge.modules.adapter.CommissionAdapter;
import com.vuvrecharge.modules.fragments.BillPaymentsFragment;
import com.vuvrecharge.modules.fragments.DTHFragment;
import com.vuvrecharge.modules.fragments.DepositFragment;
import com.vuvrecharge.modules.fragments.PrepaidFragment;
import com.vuvrecharge.modules.fragments.RechargeHistoryFragment;
import com.vuvrecharge.modules.fragments.WalletFragment;
import com.vuvrecharge.modules.model.DTHCommission;
import com.vuvrecharge.modules.model.OperatorData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.CommissionPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommissionChartActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    DefaultPresenter mDefaultPresenter;
    DefaultView defaultView;
    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;

    @BindView(R.id.commissionFrameLayout)
    FrameLayout commissionFrameLayout;
    @BindView(R.id.prepaidTextView)
    TextView prepaidTextView;
    @BindView(R.id.DTHTextView)
    TextView DTHTextView;
    @BindView(R.id.billPaymentsTextView)
    TextView billPaymentsTextView;

    @BindView(R.id.viewBillPayments)
    View viewBillPayments;
    @BindView(R.id.viewDTH)
    View viewDTH;

    @BindView(R.id.bill_ImgV)
    ImageView bill_ImgV;
    @BindView(R.id.dth_ImgV)
    ImageView dth_ImgV;
    @BindView(R.id.mobile_ImgV)
    ImageView mobile_ImgV;

    @BindView(R.id.viewPrepaid)
    View viewPrepaid;
    @BindView(R.id.getStartedTV)
    TextView getStartedTV;
    @BindView(R.id.Operators)
    TextView Operators;
    @BindView(R.id.operatorTV)
    TextView operatorTV;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
//    @BindView(R.id.rvPrepaidCommission)
//    RecyclerView rvPrepaidCommission;
//    @BindView(R.id.rvDTHCommission)
//    RecyclerView rvDTHCommission;
//    @BindView(R.id.rvOthersCommission)
//    RecyclerView rvOthersCommission;

    @BindView(R.id.loading)
    LinearLayout loading;

    CommissionAdapter prepaidAdapter;
    CommissionAdapter dthAdapter;
    CommissionAdapter othersAdapter;
    CommissionPreferences prepaidCommissionPreferences;
    CommissionPreferences dTHCommissionPreferences;
    CommissionPreferences otherCommissionPreferences;
    HashMap<String, String> map, map2, map3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_chart);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        title.setText("Commission Chart");
        mDefaultPresenter = new DefaultPresenter(this);
        defaultView = this;
        getStartedTV.setOnClickListener(this);
        setStatusBarGradiant(this);

//        prepaidCommissionPreferences = new CommissionPreferences(this, "PrepaidCommission");
//        otherCommissionPreferences = new CommissionPreferences(this, "OtherCommission");
//        dTHCommissionPreferences = new CommissionPreferences(this, "DTHCommission");
//        map = prepaidCommissionPreferences.getData();
//        map2 = dTHCommissionPreferences.getData();
//        map3 = otherCommissionPreferences.getData();

//        initializePrepaidList();
//        initializeDTHList();
//        initializeOtherList();
        initializeEventsList();
//        if (map.get("type") != null) {
//            prepaidCommissionList.clear();
//            dthCommissionList.clear();
//            otherCommissionList.clear();
//            Gson gson = new Gson();
//            Type type_ = new TypeToken<List<DTHCommission>>() {
//            }.getType();
//            ArrayList<DTHCommission> prepaidDataList = gson.fromJson(map.get("list"), type_);
//            this.prepaidCommissionList = prepaidDataList;
//            prepaidAdapter.addEvents(prepaidCommissionList);
//            Gson gson2 = new Gson();
//            Type type_2 = new TypeToken<List<DTHCommission>>() {
//            }.getType();
//            ArrayList<DTHCommission> dthDataList = gson2.fromJson(map2.get("list"), type_2);
//            this.dthCommissionList = dthDataList;
//            dthAdapter.addEvents(dthCommissionList);
//            Gson gson3 = new Gson();
//            Type type_3 = new TypeToken<List<DTHCommission>>() {
//            }.getType();
//            ArrayList<DTHCommission> otherDataList = gson3.fromJson(map3.get("list"), type_3);
//            this.otherCommissionList = otherDataList;
//            othersAdapter.addEvents(otherCommissionList);
//        } else {
//            mDefaultPresenter.getCommissions(device_id + "");
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "commissionChart");
    }

    private void initializeEventsList() {

        setFragment(new PrepaidFragment());
        viewPrepaid.setBackgroundResource(R.drawable.commission_tab_shape);
        prepaidTextView.setTextColor(Color.WHITE);
        viewDTH.setBackgroundResource(R.drawable.null_shape);
        DTHTextView.setTextColor(getResources().getColor(R.color.colorBlackU));
        viewBillPayments.setBackgroundResource(R.drawable.null_shape);
        billPaymentsTextView.setTextColor(getResources().getColor(R.color.colorBlackU));
        prepaidTextView.setText(Html.fromHtml("<b>Prepaid</b>"));
        DTHTextView.setText(Html.fromHtml("DTH"));
        billPaymentsTextView.setText(Html.fromHtml("Bill Payments"));
        Operators.setText("5");
        operatorTV.setText("Operators");
        bill_ImgV.setImageResource(R.drawable.biller_svg);
        mobile_ImgV.setImageResource(R.drawable.mobile_white_svg);
        dth_ImgV.setImageResource(R.drawable.dth_svg);

        viewPrepaid.setOnClickListener(v -> {
            setFragment(new PrepaidFragment());
            viewPrepaid.setBackgroundResource(R.drawable.commission_tab_shape);
            prepaidTextView.setTextColor(Color.WHITE);
            viewDTH.setBackgroundResource(R.drawable.null_shape);
            DTHTextView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewBillPayments.setBackgroundResource(R.drawable.null_shape);
            billPaymentsTextView.setTextColor(getResources().getColor(R.color.colorBlackU));
            prepaidTextView.setText(Html.fromHtml("<b>Prepaid</b>"));
            DTHTextView.setText(Html.fromHtml("DTH"));
            billPaymentsTextView.setText(Html.fromHtml("Bill Payments"));
            Operators.setText("5");
            operatorTV.setText("Operators");
            bill_ImgV.setImageResource(R.drawable.biller_svg);
            dth_ImgV.setImageResource(R.drawable.dth_svg);
            mobile_ImgV.setImageResource(R.drawable.mobile_white_svg);
        });

        viewDTH.setOnClickListener(v -> {
            setFragment(new DTHFragment());
            viewDTH.setBackgroundResource(R.drawable.commission_tab_shape);
            DTHTextView.setTextColor(Color.WHITE);
            viewPrepaid.setBackgroundResource(R.drawable.null_shape);
            prepaidTextView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewBillPayments.setBackgroundResource(R.drawable.null_shape);
            billPaymentsTextView.setTextColor(getResources().getColor(R.color.colorBlackU));
            prepaidTextView.setText(Html.fromHtml("<b>Prepaid</b>"));
            DTHTextView.setText(Html.fromHtml("DTH"));
            billPaymentsTextView.setText(Html.fromHtml("Bill Payments"));
            Operators.setText("5");
            operatorTV.setText("Operators");
            bill_ImgV.setImageResource(R.drawable.biller_svg);
            dth_ImgV.setImageResource(R.drawable.dth_white_svg);
            mobile_ImgV.setImageResource(R.drawable.mobile_black_svg);
        });

        viewBillPayments.setOnClickListener(v -> {
            setFragment(new BillPaymentsFragment());
            viewBillPayments.setBackgroundResource(R.drawable.commission_tab_shape);
            billPaymentsTextView.setTextColor(Color.WHITE);
            viewPrepaid.setBackgroundResource(R.drawable.null_shape);
            prepaidTextView.setTextColor(getResources().getColor(R.color.colorBlackU));
            viewDTH.setBackgroundResource(R.drawable.null_shape);
            DTHTextView.setTextColor(getResources().getColor(R.color.colorBlackU));
            prepaidTextView.setText(Html.fromHtml("<b>Prepaid</b>"));
            DTHTextView.setText(Html.fromHtml("DTH"));
            billPaymentsTextView.setText(Html.fromHtml("Bill Payments"));
            Operators.setText("5");
            operatorTV.setText("Services");
            bill_ImgV.setImageResource(R.drawable.biller_white_svg);
            dth_ImgV.setImageResource(R.drawable.dth_svg);
            mobile_ImgV.setImageResource(R.drawable.mobile_black_svg);
        });

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

    private void initializePrepaidList() {
//        prepaidAdapter = new CommissionAdapter(getLayoutInflater(), IMAGE_LOGO);
//        rvPrepaidCommission.setHasFixedSize(true);
//        rvPrepaidCommission.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
//        rvPrepaidCommission.setAdapter(prepaidAdapter);
    }

    private void initializeDTHList() {
//        dthAdapter = new CommissionAdapter(getLayoutInflater(), IMAGE_LOGO);
//        rvDTHCommission.setHasFixedSize(true);
//        rvDTHCommission.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
//        rvDTHCommission.setAdapter(dthAdapter);
    }

    private void initializeOtherList() {
//        othersAdapter = new CommissionAdapter(getLayoutInflater(), IMAGE_LOGO);
//        rvOthersCommission.setHasFixedSize(true);
//        rvOthersCommission.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
//        rvOthersCommission.setAdapter(othersAdapter);
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(commissionFrameLayout.getId(), fragment)
                .commit();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onSuccess(String data) {
    }

    DTHCommission prepaidCommission;
    DTHCommission dthCommission;
    DTHCommission otherCommission;
    ArrayList<DTHCommission> prepaidCommissionList = new ArrayList<>();
    ArrayList<DTHCommission> dthCommissionList = new ArrayList<>();
    ArrayList<DTHCommission> otherCommissionList = new ArrayList<>();

    @Override
    public void onSuccess(String data, String data_other) {
//        try {
//            JSONObject object = new JSONObject(data);
//            JSONArray prepaidArray = new JSONArray(object.getString("prepaid_commission"));
//            JSONArray dthArray = new JSONArray(object.getString("dth_commission"));
//            JSONArray othersArray = new JSONArray(object.getString("others"));
//
//            prepaidCommissionPreferences.setCommission("PrepaidCommission", object.getString("prepaid_commission"));
//            dTHCommissionPreferences.setCommission("DTHCommission", object.getString("dth_commission"));
//            otherCommissionPreferences.setCommission("OtherCommission", object.getString("others"));
//
//            for (int i = 0; i < prepaidArray.length(); i++) {
//                prepaidCommission = new DTHCommission();
//                JSONObject jsonObject = prepaidArray.getJSONObject(i);
//                prepaidCommission.setCommission(jsonObject.getString("commission"));
//                prepaidCommission.setName(jsonObject.getString("name"));
//                prepaidCommission.setLogo(jsonObject.getString("logo"));
//                prepaidCommissionList.add(prepaidCommission);
//            }
//            prepaidAdapter.addEvents(prepaidCommissionList);
//
//            for (int i = 0; i < dthArray.length(); i++) {
//                dthCommission = new DTHCommission();
//                JSONObject jsonObject = dthArray.getJSONObject(i);
//                dthCommission.setCommission(jsonObject.getString("commission"));
//                dthCommission.setName(jsonObject.getString("name"));
//                dthCommission.setLogo(jsonObject.getString("logo"));
//                dthCommissionList.add(dthCommission);
//            }
//            dthAdapter.addEvents(dthCommissionList);
//
//            for (int i = 0; i < othersArray.length(); i++) {
//                otherCommission = new DTHCommission();
//                JSONObject jsonObject = othersArray.getJSONObject(i);
//                otherCommission.setCommission(jsonObject.getString("commission"));
//                otherCommission.setName(jsonObject.getString("name"));
//                otherCommission.setLogo(jsonObject.getString("logo"));
//                otherCommissionList.add(otherCommission);
//            }
//            othersAdapter.addEvents(otherCommissionList);
//        } catch (JSONException | RuntimeException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onSuccessOther(String data) {

    }

    @Override
    public void onSuccessOther(String data, String data_other) {

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

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
            case R.id.getStartedTV:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                finish();
        }
    }
}