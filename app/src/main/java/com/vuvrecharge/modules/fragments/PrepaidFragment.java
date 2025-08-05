package com.vuvrecharge.modules.fragments;

import static com.vuvrecharge.api.ApiServices.IMAGE_LOGO;

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

public class PrepaidFragment extends BaseFragment implements DefaultView {

    private DefaultPresenter mDefaultPresenter;
    ViewGroup rootViewMain;
    String device_id = "";
    CommissionAdapter prepaidAdapter;
    DTHCommission prepaidCommission;
    CommissionPreferences prepaidCommissionPreferences;
    HashMap<String, String> map;

    OnFragmentListener listener = null;
    ArrayList<DTHCommission> prepaidCommissionList = new ArrayList<>();

    @BindView(R.id.rvPrepaidCommission1)
    RecyclerView rvPrepaidCommission;

    @BindView(R.id.loading)
    LinearLayout loading;


    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prepaid, parent, false);
        this.rootViewMain = parent;
        ButterKnife.bind(this, rootView);
        mDefaultPresenter = new DefaultPresenter(this);
        device_id = Settings.Secure.getString(requireActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        prepaidCommissionPreferences = new CommissionPreferences(requireContext(), "PrepaidCommission");
        map = prepaidCommissionPreferences.getData();


        loading.setVisibility(View.VISIBLE);

        initializePrepaidList();


        if (map.get("type") != null) {

            prepaidCommissionList.clear();
            Gson gson3 = new Gson();
            Type type_3 = new TypeToken<List<DTHCommission>>() {
            }.getType();
            ArrayList<DTHCommission> otherDataList = gson3.fromJson(map.get("list"), type_3);
            this.prepaidCommissionList = otherDataList;
            prepaidAdapter.addEvents(prepaidCommissionList);
        } else {
            mDefaultPresenter.getCommissions(device_id + "");
        }


        return rootView;
    }

    @Override
    public void onError(String error) {
//        listener.onError(error);
    }

    private void initializePrepaidList() {
        prepaidAdapter = new CommissionAdapter(getLayoutInflater(), IMAGE_LOGO);
        rvPrepaidCommission.setHasFixedSize(true);
        rvPrepaidCommission.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvPrepaidCommission.setAdapter(prepaidAdapter);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    public void onSuccess(String data, String data_other) {

        try {
            JSONObject object = new JSONObject(data);
            JSONArray prepaidArray = new JSONArray(object.getString("prepaid_commission"));
            prepaidCommissionPreferences.setCommission("PrepaidCommission", object.getString("prepaid_commission"));

            for (int i = 0; i < prepaidArray.length(); i++) {
                prepaidCommission = new DTHCommission();
                JSONObject jsonObject = prepaidArray.getJSONObject(i);
                prepaidCommission.setCommission(jsonObject.getString("commission"));
                prepaidCommission.setName(jsonObject.getString("name"));
                prepaidCommission.setLogo(jsonObject.getString("logo"));
                prepaidCommissionList.add(prepaidCommission);
            }
            prepaidAdapter.addEvents(prepaidCommissionList);
            loading.setVisibility(View.GONE);

        } catch (JSONException | RuntimeException e) {
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
//        listener.onShowToast(message);
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            listener = (OnFragmentListener) context;
        } else {

        }
    }
}