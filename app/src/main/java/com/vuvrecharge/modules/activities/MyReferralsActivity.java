package com.vuvrecharge.modules.activities;

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
import com.vuvrecharge.modules.adapter.ReferralsAdapter;
import com.vuvrecharge.modules.model.ReferralsData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyReferralsActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    private ReferralsAdapter adapter;

    private DefaultPresenter mDefaultPresenter;

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.coins_list)
    RecyclerView coins_list;

    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    int page = 1;
    int remaining_pages = 3;
    int pastVisibleItems;
    int visibleItemCount;
    int totalItemCount;
    int previousTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrals);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        title.setText("My Referrals");
        initializeEventsList();
        mDefaultPresenter = new DefaultPresenter(this);
        mDefaultPresenter.myReferrals(device_id, page + "", "Yes");

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "myreferral");
    }

    boolean isLoading = true;

    private void initializeEventsList() {
        adapter = new ReferralsAdapter(getLayoutInflater());
        coins_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        coins_list.setLayoutManager(linearLayoutManager);
        coins_list.setAdapter(adapter);
        coins_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        showError(error);
    }

    @Override
    public void onSuccess(String message) {


    }

    @Override
    public void onSuccess(String data, String data_other) {
        try {
            isLoading = false;
            JSONObject jsonObject = new JSONObject(data);
            printLog(jsonObject.toString());
            remaining_pages = Integer.parseInt(jsonObject.getString("remaining_pages"));
            String fixedMobile = jsonObject.getString("fixedMobile");
            JSONArray jsonArray = jsonObject.getJSONArray("history_data");
            Gson gson = new Gson();
            Type type_ = new TypeToken<List<ReferralsData>>() {
            }.getType();
            List<ReferralsData> passbookData = gson.fromJson(jsonArray.toString(), type_);
            if (data_other.equals("Yes")) {
                initializeEventsList();
            }
            adapter.addEvents(passbookData, data_other, fixedMobile);
        } catch (Exception e) {
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
        showDialog(message);
    }

    @Override
    public void onHideDialog() {
        hideDialog();
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