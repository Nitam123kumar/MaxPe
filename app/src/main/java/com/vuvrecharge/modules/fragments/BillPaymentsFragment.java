package com.vuvrecharge.modules.fragments;

import static com.vuvrecharge.api.ApiServices.IMAGE_LOGO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseFragment;
import com.vuvrecharge.modules.adapter.CommissionAdapter;
import com.vuvrecharge.modules.model.DTHCommission;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.presenter.OnFragmentListener;
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

public class BillPaymentsFragment extends BaseFragment implements DefaultView {

    private DefaultPresenter mDefaultPresenter;
    ViewGroup rootViewMain;
    String device_id = "";
    CommissionAdapter othersAdapter;
    DTHCommission otherCommission;
    CommissionPreferences otherCommissionPreferences;
    HashMap<String, String> map3;

    OnFragmentListener listener = null;
    ArrayList<DTHCommission> otherCommissionList = new ArrayList<>();

    @BindView(R.id.rvBillPaymentCommission)
    RecyclerView rvBillPaymentCommission;
    @BindView(R.id.loading)
    LinearLayout loading;

    @SuppressLint("HardwareIds")
    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bill_payments, parent, false);
        this.rootViewMain = parent;
        ButterKnife.bind(this, rootView);
        mDefaultPresenter = new DefaultPresenter(this);
        device_id = Settings.Secure.getString(requireActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        otherCommissionPreferences = new CommissionPreferences(requireContext(), "OtherCommission");
        map3 = otherCommissionPreferences.getData();


        loading.setVisibility(View.VISIBLE);
        initializeOtherList();

        if (map3.get("type") != null) {

            otherCommissionList.clear();
            Gson gson3 = new Gson();
            Type type_3 = new TypeToken<List<DTHCommission>>() {
            }.getType();
            ArrayList<DTHCommission> otherDataList = gson3.fromJson(map3.get("list"), type_3);
            this.otherCommissionList = otherDataList;
            othersAdapter.addEvents(otherCommissionList);
        } else {
            mDefaultPresenter.getCommissions(device_id + "");
        }

        return rootView;
    }

    @Override
    public void onError(String error) {
        listener.onError(error);
    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    public void onSuccess(String data, String data_other) {

        try {
            JSONObject object = new JSONObject(data);
            JSONArray othersArray = new JSONArray(object.getString("others"));

            otherCommissionPreferences.setCommission("OtherCommission", object.getString("others"));


            for (int i = 0; i < othersArray.length(); i++) {
                otherCommission = new DTHCommission();
                JSONObject jsonObject = othersArray.getJSONObject(i);
                otherCommission.setCommission(jsonObject.getString("commission"));
                otherCommission.setName(jsonObject.getString("name"));
                otherCommission.setLogo(jsonObject.getString("logo"));
                otherCommissionList.add(otherCommission);
            }
            othersAdapter.addEvents(otherCommissionList);
            loading.setVisibility(View.GONE);
        } catch (JSONException | RuntimeException e) {
            e.printStackTrace();
        }

    }
    private void initializeOtherList() {
        othersAdapter = new CommissionAdapter(getLayoutInflater(), IMAGE_LOGO);
        rvBillPaymentCommission.setHasFixedSize(true);
        rvBillPaymentCommission.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvBillPaymentCommission.setAdapter(othersAdapter);
        loading.setVisibility(View.GONE);
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
        listener.onShowToast(message);
    }

    @Override
    public void onPrintLog(String message) {

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