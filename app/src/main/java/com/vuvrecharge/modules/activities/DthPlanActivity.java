package com.vuvrecharge.modules.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.custom.ClassesTabLayout;
import com.vuvrecharge.custom.DynamicViewPager;
import com.vuvrecharge.modules.fragments.SpecialOffersDth;
import com.vuvrecharge.modules.model.DthPlanData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.presenter.OnFragmentListener;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.utils.LocaleHelper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DthPlanActivity extends BaseActivity implements DefaultView, OnFragmentListener,View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.tab_layout)
    ClassesTabLayout mClassesTabLayout;
    @BindView(R.id.pager)
    ViewPager mViewPager;

    DefaultPresenter mDefaultPresenter;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.txtSearch)
    TextView txtSearch;
    @BindView(R.id.infoLayout)
    LinearLayout infoLayout;

    DefaultView mDefaultView;

    String selected_operator = "";
    String selected_circle = "";
    String selected_operator_str = "";
    String selected_circle_str = "";
    String operator_img = "";
    String mobile_number_str = "";
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.bind(getActivity());
        mToolbar.setOnClickListener(this);
        mDefaultView = this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        title.setText("Plans Details");
        Intent intent = getIntent();
        infoLayout.setVisibility(View.GONE);
        txtSearch.setVisibility(View.GONE);
        selected_operator = intent.getStringExtra("selected_operator");
        selected_circle = "NA";
        selected_operator_str = intent.getStringExtra("selected_operator_str");
        selected_circle_str = "NA";
        operator_img = intent.getStringExtra("operator_img");
        mobile_number_str = intent.getStringExtra("mobile_number_str");
        mDefaultPresenter = new DefaultPresenter(this);
        mDefaultPresenter.fetchDthPlans(device_id + "", selected_operator + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "DTHplans");
    }

    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String message) {
        onBackPressed();
    }

    @Override
    public void onSuccess(String message, String second_message) {
        List<DthPlanData> planDataList = new ArrayList<>();
        List<DthPlanData> addPlanDataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(message);
            JSONArray addonpack = new JSONArray();
            JSONArray plans = jsonObject.getJSONArray("plans");
            try {
                addonpack = jsonObject.getJSONArray("addonpack");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Gson gson = new Gson();
            Type type_ = new TypeToken<List<DthPlanData>>() {
            }.getType();
            planDataList = gson.fromJson(plans.toString(), type_);
            addPlanDataList = gson.fromJson(addonpack.toString(), type_);
            printLog(planDataList.size() + " planDataList");
            printLog(addPlanDataList.size() + " addPlanDataList");

            CustomTabAdapter mTabAdapter = new CustomTabAdapter(getSupportFragmentManager(), plans, addonpack);
            mViewPager.setOffscreenPageLimit(0);
            mViewPager.setAdapter(mTabAdapter);
            mClassesTabLayout.setTabMode(TabLayout.MODE_FIXED);
            mClassesTabLayout.setupWithViewPager(mViewPager);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mClassesTabLayout));
        } catch (Exception e) {
            e.printStackTrace();
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
    public void onClick(@NonNull View v) {
        switch(v.getId()){
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }

    public class CustomTabAdapter extends FragmentStatePagerAdapter {
        int count = 2;
        JSONArray planDataList;
        JSONArray addPlanDataList;

        public CustomTabAdapter(FragmentManager fm, JSONArray planDataList, JSONArray addPlanDataList) {
            super(fm);
            this.planDataList = planDataList;
            this.addPlanDataList = addPlanDataList;
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return new SpecialOffersDth(mDefaultView, device_id, planDataList,selected_operator,
                        "",selected_operator_str,selected_circle_str,operator_img,mobile_number_str);
            } else {
                return new SpecialOffersDth(mDefaultView, device_id, addPlanDataList,selected_operator,
                        "",selected_operator_str,selected_circle_str,operator_img,mobile_number_str);
            }

        }

        @Override
        public int getCount() {

            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "PLANS";
            } else {
                return "ADD ON PACK";
            }

        }
    }

}
