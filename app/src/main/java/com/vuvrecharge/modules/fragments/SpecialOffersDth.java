package com.vuvrecharge.modules.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseFragment;
import com.vuvrecharge.modules.adapter.DthPlanAdapter;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.presenter.OnFragmentListener;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecialOffersDth extends BaseFragment implements DefaultView {

    DthPlanAdapter adapter;
    public Context mContext;
    DefaultPresenter mDefaultPresenter;

    @BindView(R.id.list_data)
    RecyclerView list_data;

    @BindView(R.id.no_data)
    TextView no_data;
    DefaultView mDefaultView;
    JSONArray mPlanItemData;
    String device_id = "";
    String operatorId = "";
    String circleId = "";
    String operatorStr = "";
    String circleStr = "";
    String operatorImg = "";
    String mobileNumber = "";
    OnFragmentListener listener = null;

    public SpecialOffersDth(DefaultView mDefaultView, String device_id, JSONArray mPlanItemData,
                            String operatorId,String circleId,String operatorStr,
                            String circleStr,String operatorImg, String mobileNumber) {
        this.mDefaultView = mDefaultView;
        this.mPlanItemData = mPlanItemData;
        this.operatorId = operatorId;
        this.circleId = circleId;
        this.operatorStr = operatorStr;
        this.circleStr = circleStr;
        this.operatorImg = operatorImg;
        this.mobileNumber = mobileNumber;
        this.device_id = device_id;
    }

    ViewGroup rootViewMain;

    @Override
    public View provideYourFragmentView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_special_offers, parent, false);
        mContext = this.getActivity();
        rootViewMain = parent;
        ButterKnife.bind(this, rootView);
        initializeEventsList();
        mDefaultPresenter = new DefaultPresenter(this);
        if (mPlanItemData.length() > 0) {
            no_data.setVisibility(View.GONE);
        } else {
            no_data.setVisibility(View.VISIBLE);
        }
        adapter.addData(mPlanItemData, mDefaultView);
        return rootView;
    }

    private void initializeEventsList() {
        adapter = new DthPlanAdapter(getLayoutInflater(),operatorId,circleId,operatorStr,circleStr,operatorImg,mobileNumber);
        list_data.setHasFixedSize(true);
        list_data.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list_data.setAdapter(adapter);
    }

    @Override
    public void onError(String error) {
//        showError(requireContext(), error);
        listener.onError(error);
    }

    @Override
    public void onSuccess(String message) {
    }

    @Override
    public void onSuccess(String message, String type) {

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
