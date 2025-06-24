package com.vuvrecharge.modules.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseFragment;
import com.vuvrecharge.modules.adapter.PlanAdapter;
import com.vuvrecharge.modules.model.PlanItemData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.modules.view.PageItemTabPositionChange;
import com.vuvrecharge.utils.OnSwipeTouchListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class SpecialOffers extends BaseFragment implements DefaultView {

    PlanAdapter adapter;
    public Context mContext;
    DefaultPresenter mDefaultPresenter;
    @BindView(R.id.list_data)
    RecyclerView list_data;
    @BindView(R.id.no_data)
    TextView no_data;
    DefaultView mDefaultView;
    List<PlanItemData> mPlanItemData = new ArrayList<>();
    String device_id = "";
    String title = "";
    String phone = "";
    String url = "";
    String provider = "";
    String state = "";
    String selected_operator = "";
    String selected_circle = "";
    EditText editText;
//    PageItemTabPositionChange listener;
    int lastVisiblePos = 0;
    int firstVisiblePos = 0;

    public SpecialOffers(DefaultView mDefaultView, String device_id, List<PlanItemData> mPlanItemData,
                         String title, String phone, String url, String provider, String state,
                         String selected_operator, String selected_circle) {
        this.mDefaultView = mDefaultView;
        this.mPlanItemData = mPlanItemData;
        this.device_id = device_id;
        this.title = title;
        this.phone = phone;
        this.url = url;
        this.provider = provider;
        this.state = state;
        this.selected_operator = selected_operator;
        this.selected_circle = selected_circle;
    }

    public SpecialOffers() {
    }

    @NonNull
    public static SpecialOffers getInstance(){
        return new SpecialOffers();
    }

    ViewGroup rootViewMain;

    @Override
    public View provideYourFragmentView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_special_offers, parent, false);
        mContext = this.getActivity();
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootViewMain = parent;
        ButterKnife.bind(this, rootView);
        initializeEventsList();
        mDefaultPresenter = new DefaultPresenter(this);

        adapter.addData(mPlanItemData, mDefaultView);

        if (!mPlanItemData.isEmpty()) {
            no_data.setVisibility(View.GONE);
        } else {
            no_data.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

//    private void searchPlan(String text) {
//        ArrayList<PlanItemData> data = new ArrayList<>();
//        for (PlanItemData operatorData: mPlanItemData){
//            if (operatorData.getRs().toUpperCase().toLowerCase().contains(text.toUpperCase().toLowerCase())){
//                data.add(operatorData);
//            }
//        }
//        Collections.reverse(data);
//        adapter.searchList(data, mDefaultView);
//        if (!data.isEmpty()) {
//            no_data.setVisibility(View.GONE);
//        } else {
//            no_data.setVisibility(View.VISIBLE);
//        }
//    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeEventsList() {
        adapter = new PlanAdapter(getLayoutInflater(),title,phone,url,provider,state,selected_operator,selected_circle);
        list_data.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list_data.setAdapter(adapter);
    }

    @Override
    public void onError(String error) {
        showError(requireContext(), error);
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
        showToast(rootViewMain, message);
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }

}