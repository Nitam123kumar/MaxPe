package com.vuvrecharge.modules.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseFragment;
import com.vuvrecharge.modules.adapter.PlanAdapter;
import com.vuvrecharge.modules.model.PlanItemData;
import com.vuvrecharge.modules.view.DefaultView;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanSearchFragment extends BaseFragment implements DefaultView {

    DefaultView mDefaultView;
    private Context mContext;
    String title;
    String phone, url, provider, state, selected_operator, selected_circle;
    ViewGroup rootViewMain;
    @BindView(R.id.rvPlanSearch)
    RecyclerView rvPlanSearch;
    @BindView(R.id.no_data)
    TextView no_data;
    EditText editText;
    ImageView imgClose;
    List<PlanItemData> mPlanItemData = new ArrayList<>();
    PlanAdapter adapter;

    public PlanSearchFragment() {
        // Required empty public constructor
    }

    public PlanSearchFragment(DefaultView mDefaultView, EditText editText, ImageView imgClose) {
        this.mDefaultView = mDefaultView;
        this.editText = editText;
        this.imgClose = imgClose;
    }


    @NonNull
    @Contract("_, _, _ -> new")
    public static PlanSearchFragment getInstance(DefaultView mDefaultView, EditText editText, ImageView imgClose){
        return new PlanSearchFragment(mDefaultView,editText,imgClose);
    }

    @Override
    public View provideYourFragmentView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan_search, container, false);
        mContext = this.getActivity();
        rootViewMain = container;
        ButterKnife.bind(this, rootView);
        Log.d("TAG_DATA", "getArguments: "+getArguments().getString("listData"));
        if (getArguments() != null){
            String listData = getArguments().getString("listData");
            title = getArguments().getString("title");
            phone = getArguments().getString("phone");
            url = getArguments().getString("url");
            provider = getArguments().getString("provider");
            state = getArguments().getString("state");
            selected_circle = getArguments().getString("selected_circle");
            selected_operator = getArguments().getString("selected_operator");
            adapter = new PlanAdapter(getLayoutInflater(),title, phone, url, provider, state,selected_operator,selected_circle);
            try {
                JSONArray array = new JSONArray(listData);
                Log.d("TAG_DATA", "provideYourFragmentView1: "+array);
                if (array.length() > 0){
                    for (int i=0; i < array.length(); i++) {

                        PlanItemData itemData = new PlanItemData();
                        JSONObject o = array.getJSONObject(i);
                        itemData.setRs(o.getString("rs"));
                        if (o.has("cost_per_days")){
                            if (o.getString("cost_per_days") != null){
                                itemData.setCost_per_days(o.getString("cost_per_days"));
                            }
                            else {
                                itemData.setCost_per_days("NA");
                            }
                        }

                        itemData.setDesc(o.getString("desc"));
                        itemData.setValidity(o.getString("validity"));
                        itemData.setLast_update(o.getString("last_update"));
                        if(getArguments().getString("type").equals("plan")){
                            itemData.setData(o.getString("data"));
                            itemData.setTalktime(o.getString("talktime"));
                            itemData.setSms_value(o.getString("sms_value"));
                            itemData.setSubscription(o.getString("subscription"));
                        }
                        Log.d("TAG_DATA", "array: "+array);
                        mPlanItemData.add(itemData);
                    }
                    Log.d("TAG_DATA", "mPlanItemData: "+mPlanItemData);

                    setAdapterData(mPlanItemData);
                }
            }catch (JSONException e){
                Log.d("TAG_DATA", "provideYourFragmentView2: "+e.getMessage());
            }

            if (editText != null){

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().isEmpty()){
                            imgClose.setVisibility(View.GONE);
                        }else {
                            imgClose.setVisibility(View.GONE);
                            searchPlan(s.toString());
                        }
                    }
                });
            }
        }

        return rootView;
    }

    private void searchPlan(String text) {
        ArrayList<PlanItemData> data = new ArrayList<>();
        for (PlanItemData operatorData: mPlanItemData){
            if (operatorData.getRs().toUpperCase().toLowerCase().contains(text.toUpperCase().toLowerCase())){
                data.add(operatorData);
            }
        }
        Collections.sort(data, (lhs, rhs) -> {
            int lhsRs = Integer.parseInt(lhs.getRs().trim());
            int rhsRs = Integer.parseInt(rhs.getRs().trim());
            return Integer.compare(lhsRs,rhsRs);
        });
        adapter.searchList(data, mDefaultView);
        if (!data.isEmpty()) {
            no_data.setVisibility(View.GONE);
        } else {
            no_data.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapterData(List<PlanItemData> mPlanItemData) {
        adapter.addData(mPlanItemData, mDefaultView);
        rvPlanSearch.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("TAG_DATA", "setAdapterData: "+mPlanItemData);
    }

    @Override
    public void onError(String error) {
        showError(requireContext(),error);
    }

    @Override
    public void onSuccess(String data) {

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