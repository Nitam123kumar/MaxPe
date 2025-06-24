package com.vuvrecharge.modules.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.MyCommisonPackageAdapter;
import com.vuvrecharge.modules.model.MycommisonPackageData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCommisionPackageActivity extends BaseActivity implements DefaultView,View.OnClickListener {

    private MyCommisonPackageAdapter adapter;

    private DefaultPresenter mDefaultPresenter;

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_data)
    TextView no_data;
    @BindView(R.id.list_item)
    RecyclerView list_item;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;

    String id = "",data="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycommison_package);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        initializeEventsList();
        mDefaultPresenter = new DefaultPresenter(this);
        try {
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            data = intent.getStringExtra("data");
            System.out.println("id" + id);
            if (data.equals("My Commission Package")){
                title.setText(data);
            }else {
                title.setText(data);
            }
            mDefaultPresenter.myCommissionPackage(device_id + "",data+"",id+"");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "myCommissionPackage");
    }

    private void initializeEventsList() {
        adapter = new MyCommisonPackageAdapter(getLayoutInflater());
        list_item.setHasFixedSize(true);
        list_item.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list_item.setAdapter(adapter);
    }

    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String message) {
        printLog(message);
        try {
            JSONArray jsonObject = new JSONArray(message);
            Gson gson = new Gson();
            Type type_ = new TypeToken<List<MycommisonPackageData>>() {
            }.getType();
            List<MycommisonPackageData> dataList = gson.fromJson(jsonObject.toString(), type_);
            adapter.addEvents(dataList);
            if (dataList.size() == 0) {
                no_data.setVisibility(View.VISIBLE);
            } else {
                no_data.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(String data, String data_other) {

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
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
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