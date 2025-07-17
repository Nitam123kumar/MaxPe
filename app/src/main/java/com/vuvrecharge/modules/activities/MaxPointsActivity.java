package com.vuvrecharge.modules.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.MaxPointsAdapter;
import com.vuvrecharge.modules.model.DepositData;
import com.vuvrecharge.modules.model.MaxPePointsData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaxPointsActivity extends BaseActivity implements DefaultView {

    DefaultPresenter defaultPresenter;
    @BindView(R.id.back_home)
    ImageView back_to_home;

    @BindView(R.id.txtNoData)
    TextView txtNoData;
    @BindView(R.id.maxPointRecyclerView)
    RecyclerView maxPointRecyclerView;

    private List<MaxPePointsData> mMaxPePointsData = new ArrayList<>();
    MaxPointsAdapter maxPointsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_points);
        ButterKnife.bind(this);
        defaultPresenter = new DefaultPresenter(this);
        defaultPresenter.cashbackPointsHistory(device_id+"");
        maxPointsAdapter=new MaxPointsAdapter(this,mMaxPePointsData);

        maxPointRecyclerView.setLayoutManager(new LinearLayoutManager(this , RecyclerView.VERTICAL,false));
        maxPointRecyclerView.setAdapter(maxPointsAdapter);

        back_to_home.setOnClickListener(v -> {
            onBackPressed();
            finish();
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

    }

    @Override
    public void onSuccessOther(String data) {

    }

    @Override
    public void onSuccessOther(String data, String data_other) {
        Log.d("cashbackLogs", String.valueOf(data));
        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("cashbackLogs");

            if (array.length() > 0){
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    MaxPePointsData maxPePointData = new MaxPePointsData();
                    maxPePointData.setOrder_id(object1.getString("order_id"));
                    maxPePointData.setAmount(object1.getString("amount"));
                    maxPePointData.setRemark(object1.getString("remark"));
                    maxPePointData.setCredit(object1.getString("credit"));
                    maxPePointData.setDate_time(object1.getString("date_time"));
                    maxPePointData.setClosing_points(object1.getString("closing_points"));
                    maxPePointData.setDebit(object1.getString("debit"));
                    maxPePointData.setType(object1.getString("type"));
                    maxPePointData.setOpening_points(object1.getString("opening_points"));
                    mMaxPePointsData.add(maxPePointData);
                    maxPointsAdapter.addEvents(mMaxPePointsData);
                    maxPointsAdapter.notifyDataSetChanged();
                    txtNoData.setVisibility(GONE);

                }

                maxPointRecyclerView.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            }else {
                maxPointRecyclerView.setVisibility(GONE);
                txtNoData.setVisibility(VISIBLE);
            }


        }catch (Exception e){
            Log.d("TAG_DATA", "onSuccessOther: "+e.getMessage());
        }
    }

    @Override
    public void onShowDialog(String message) {

    }

    @Override
    public void onHideDialog() {

    }

    @Override
    public void onShowToast(String message) {

    }

    @Override
    public void onPrintLog(String message) {

    }
}