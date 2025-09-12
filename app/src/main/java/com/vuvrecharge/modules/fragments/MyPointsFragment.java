package com.vuvrecharge.modules.fragments;

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
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseFragment;
import com.vuvrecharge.modules.adapter.ReferralsIncomeAdapter;
import com.vuvrecharge.modules.model.ReferralsIncomeData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPointsFragment extends BaseFragment implements DefaultView {

    public Context mContext;
    private DefaultPresenter mDefaultPresenter;
    ViewGroup rootViewMain;
    String device_id = null;
    private ReferralsIncomeAdapter adapter;

    @BindView(R.id.txtNoData)
    ImageView txtNoData;
    @BindView(R.id.myPoints)
    RecyclerView myPoints;

    int page = 1;
    int remaining_pages = 3;
    int pastVisibleItems;
    int visibleItemCount;
    int totalItemCount;
    int previousTotal = 0;
    boolean isLoading = true;

    @Override
    public View provideYourFragmentView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_points, parent, false);
        mContext = this.getActivity();
        rootViewMain = parent;
        ButterKnife.bind(this, rootView);

        mDefaultPresenter = new DefaultPresenter(this);
        device_id = Settings.Secure.getString(requireActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mDefaultPresenter.myReferralIncome(device_id, page + "", "Yes", "points");

        initializeEventsList();

        return rootView;
    }

    private void initializeEventsList() {
        adapter = new ReferralsIncomeAdapter(getLayoutInflater());
        myPoints.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        myPoints.setLayoutManager(linearLayoutManager);
        myPoints.setAdapter(adapter);
        myPoints.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }

                    if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems)) {
                        if (remaining_pages > 0) {
                            page++;
                            mDefaultPresenter.myReferrals(device_id + "", page + "", "No");
                            isLoading = true;
                        }
                    }
                }
            }
        });

    }


    @Override
    public void onError(String error) {

    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    public void onSuccess(String data, String data_other) {
        try {

            isLoading = false;
            JSONObject jsonObject = new JSONObject(data);
            printLog(jsonObject.toString());
            remaining_pages = Integer.parseInt(jsonObject.getString("remaining_pages"));
            JSONArray jsonArray = jsonObject.getJSONArray("history_data");
            Log.d("TAG", "onSuccess points: " + jsonArray.toString());
            if (jsonArray.length() > 0) {

                Gson gson = new Gson();
                Type type_ = new TypeToken<List<ReferralsIncomeData>>() {
                }.getType();
                List<ReferralsIncomeData> passbookData = gson.fromJson(jsonArray.toString(), type_);
                if (data_other.equals("Yes")) {
                    initializeEventsList();
                }
                adapter.addEvents(passbookData, data_other);
                txtNoData.setVisibility(View.GONE);
                myPoints.setVisibility(View.VISIBLE);
            } else {
                txtNoData.setVisibility(View.VISIBLE);
                myPoints.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        showDialog(message);
    }

    @Override
    public void onHideDialog() {
        hideDialog();
    }

    @Override
    public void onShowToast(String message) {
        showDialog(message);
    }

    @Override
    public void onPrintLog(String message) {

    }
}