package com.vuvrecharge.modules.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.CustomberDTHListAdapter;
import com.vuvrecharge.modules.adapter.CustomberListAdapter;
import com.vuvrecharge.modules.model.CustomberListData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpLineActivity extends BaseActivity implements DefaultView,View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    public TextView title;
    DefaultPresenter mDefaultPresenter;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    DefaultView mDefaultView;
    CustomberListAdapter adapter;
    CustomberDTHListAdapter adapterDTH;
    @BindView(R.id.customber_list)
    RecyclerView customber_list;
    @BindView(R.id.no_data)
    TextView no_data;
    String DTH = "";
    String string = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_line);
        ButterKnife.bind(getActivity());
        mToolbar.setOnClickListener(this);
        mDefaultView = this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mDefaultPresenter = new DefaultPresenter(this);
        try {
            Intent intent = getIntent();
            string = intent.getStringExtra("title");
            DTH = intent.getStringExtra("type");
            if (string.equals("DTH Toll Free Number")){
                title.setText(string);
            }else {
                title.setText(string);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mDefaultPresenter.getCustomerCareNumber(DTH);
        initializeEventsList();
        setStatusBarGradiant(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "helpLine");
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

    private void initializeEventsList() {
        adapter = new CustomberListAdapter(getLayoutInflater());
        customber_list.setHasFixedSize(true);
        customber_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        customber_list.setAdapter(adapter);
    }

    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String message) {


        try {
            JSONObject jsonObject = new JSONObject(message);
            String operator_img = jsonObject.getString("operator_img");

            Gson gson = new Gson();
            Type type_ = new TypeToken<List<CustomberListData>>() {
            }.getType();
            List<CustomberListData> dataList = gson.fromJson(jsonObject.getJSONArray("operators").toString(), type_);
            adapter.addData(dataList,operator_img);
            no_data.setText("No record found");
            if (dataList.size() > 0) {
                customber_list.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
            } else {
                customber_list.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onSuccess(String message, String second_message) {

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