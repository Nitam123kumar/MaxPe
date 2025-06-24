package com.vuvrecharge.modules.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseFragment;
import com.vuvrecharge.modules.adapter.DepositAdapter;
import com.vuvrecharge.modules.model.DepositData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.presenter.OnFragmentListener;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DepositFragment extends BaseFragment implements DefaultView {
    public Context mContext;
    private DefaultPresenter mDefaultPresenter;
    ViewGroup rootViewMain;
    @BindView(R.id.list_item)
    RecyclerView list_item;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.txtNoData)
    TextView txtNoData;

    private List<DepositData> depositList = new ArrayList<>();
    DepositAdapter adapter;
    String device_id = null;
    OnFragmentListener listener = null;
    @SuppressLint("HardwareIds")
    @Override
    public View provideYourFragmentView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deposit, parent, false);
        mContext = this.getActivity();
        rootViewMain = parent;
        ButterKnife.bind(this, rootView);
        mDefaultPresenter = new DefaultPresenter(this);
        device_id = Settings.Secure.getString(requireActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        initializeDepositList();
        mDefaultPresenter.onlineDepositHistory(device_id + "");
        return rootView;
    }

    private void initializeDepositList() {
        adapter = new DepositAdapter(getLayoutInflater());
        list_item.setHasFixedSize(true);
        list_item.setLayoutManager(new LinearLayoutManager(requireContext()));
        list_item.setAdapter(adapter);
    }

    @Override
    public void onError(String error) {
//        showError(requireContext(), error);
        try {
            listener.onError(error);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String data) {
        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("history_data");
            if (array.length() > 0){
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    DepositData depositData = new DepositData();
                    depositData.setOrder_id(object1.getString("order_id"));
                    depositData.setTotal_amount(object1.getString("total_amount"));
                    depositData.setCharge(object1.getString("charge"));
                    depositData.setFinal_amount(object1.getString("final_amount"));
                    depositData.setPayment_status(object1.getString("payment_status"));
                    depositData.setOrder_time(object1.getString("order_time"));
                    depositList.add(depositData);
                    adapter.addEvents(depositList);
                    adapter.notifyDataSetChanged();
                    txtNoData.setVisibility(GONE);
                }
                list_item.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            }else {
                list_item.setVisibility(GONE);
                txtNoData.setVisibility(VISIBLE);
            }


        }catch (Exception e){
            Log.d("TAG_DATA", "onSuccessOther: "+e.getMessage());
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
        if (bottomSheet != null) {
            showLoading(loading_dialog);
            submit.setVisibility(GONE);
        } else {
            showLoading(loading);
        }
    }

    @Override
    public void onHideDialog() {
        if (bottomSheet != null) {
            hideLoading(loading_dialog);
            submit.setVisibility(VISIBLE);
        } else {
            hideLoading(loading);
        }
    }

    @Override
    public void onShowToast(String message) {
//        showToast(rootViewMain, message);
        listener.onShowToast(message);
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener){
            listener = (OnFragmentListener) context;
        }else {

        }
    }
}