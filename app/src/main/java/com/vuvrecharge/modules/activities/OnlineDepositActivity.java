package com.vuvrecharge.modules.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.DepositAdapter;
import com.vuvrecharge.modules.model.DepositData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineDepositActivity extends BaseActivity implements DefaultView,View.OnClickListener {

    private DepositAdapter adapter;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        title.setText("Deposit History");
        initializeEventsList();
        mDefaultPresenter = new DefaultPresenter(this);
        mDefaultPresenter.onlineDepositHistory(device_id + "","","","");

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "onlineDeposit");
    }

    private void initializeEventsList() {
        adapter = new DepositAdapter(getLayoutInflater());
        list_item.setHasFixedSize(true);
        list_item.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list_item.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);

            Gson gson = new Gson();
            Type type_ = new TypeToken<List<DepositData>>() {
            }.getType();

            List<DepositData> dataList = gson.fromJson(jsonObject.getString("history_data"), type_);
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