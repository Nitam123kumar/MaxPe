package com.vuvrecharge.modules.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.TopEarnerAdapter;
import com.vuvrecharge.modules.model.TopEarnerData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopEarnerActivity extends BaseActivity implements DefaultView , View.OnClickListener {

    DefaultPresenter defaultPresenter;
    @BindView(R.id.top_earner_RecyclerView)
    RecyclerView top_earner_RecyclerView;
    @BindView(R.id.back_home)
    ImageView back_home;
    @BindView(R.id.rank_3)
    TextView rank_3;
    @BindView(R.id.rank_2)
    TextView rank_2;
    @BindView(R.id.rank_1)
    TextView rank_1;
    @BindView(R.id.no_3_nameTV)
    TextView no_3_nameTV;
    @BindView(R.id.no_2_nameTV)
    TextView no_2_nameTV;
    @BindView(R.id.no_1_nameTV)
    TextView no_1_nameTV;
    @BindView(R.id.top_referral_no_3)
    TextView top_referral_no_3;
    @BindView(R.id.top_referral_no_2)
    TextView top_referral_no_2;
    @BindView(R.id.top_referral_no_1)
    TextView top_referral_no_1;
    @BindView(R.id.top_transaction)
    TextView top_transaction;
    @BindView(R.id.top_referral)
    TextView top_referral;
    String firstName;
    String secondName;
    String thirdName;
    TopEarnerAdapter topEarnerAdapter;
    List<TopEarnerData> topEarnerDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_top_earner);
        defaultPresenter = new DefaultPresenter(this);
        defaultPresenter.topRankers(device_id, "referrals");
        ButterKnife.bind(this);
        setStatusBarGradiant(this);
        back_home.setOnClickListener(this);
        top_earner_RecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        topEarnerAdapter = new TopEarnerAdapter(this, topEarnerDataList);
        top_earner_RecyclerView.setAdapter(topEarnerAdapter);
        onClickData();

    }
    private String getInitials(String name, int count) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }

        String[] words = name.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();

        for (int i = 0; i < words.length && i < count; i++) {
            if (!words[i].isEmpty()) {
                initials.append(words[i].charAt(0));
            }
        }

        return initials.toString().toUpperCase(); // optional uppercase
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    void onClickData() {
        top_referral.setTextColor(getResources().getColor(R.color.black));
        top_transaction.setTextColor(getResources().getColor(R.color.white));
        top_referral.setBackground(getResources().getDrawable(R.drawable.top_earner_shape));
        top_transaction.setBackground(getResources().getDrawable(R.drawable.top_transaction));
        top_referral.setOnClickListener(v -> {
            defaultPresenter.topRankers(device_id, "referrals");
            top_referral.setTextColor(getResources().getColor(R.color.black));
            top_transaction.setTextColor(getResources().getColor(R.color.white));
            top_referral.setBackground(getResources().getDrawable(R.drawable.top_earner_shape));
            top_transaction.setBackground(getResources().getDrawable(R.drawable.top_transaction));
        });
        top_transaction.setOnClickListener(v -> {
            defaultPresenter.topRankers(device_id, "transactions");
            top_referral.setTextColor(getResources().getColor(R.color.white));
            top_transaction.setTextColor(getResources().getColor(R.color.black));
            top_referral.setBackground(getResources().getDrawable(R.drawable.top_transaction));
            top_transaction.setBackground(getResources().getDrawable(R.drawable.top_earner_shape));
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.main_wallet_shape);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccessOther(String data) {

        Log.d("TopEarnerActivity", data);
        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = new JSONArray(object.getString("rankers"));
            int arrayLength = array.length();
            if (arrayLength > 0) {
                topEarnerDataList.clear();
                firstName= (array.length() > 0) ? array.optJSONObject(0).optString("name", "") : "";
                secondName= (array.length() > 1) ? array.optJSONObject(1).optString("name", "") : "";
                thirdName= (array.length() > 2) ? array.optJSONObject(2).optString("name", "") : "";

                rank_1.setText("1");
                rank_2.setText("2");
                rank_3.setText("3");
                no_1_nameTV.setText(firstName);
                no_2_nameTV.setText(secondName);
                no_3_nameTV.setText(thirdName);
                String initialsFirstName = getInitials(firstName, 2);
                String initialsSecondName = getInitials(secondName, 2);
                String initialsThirdName = getInitials(thirdName, 2);
                top_referral_no_1.setText(initialsFirstName);
                top_referral_no_2.setText(initialsSecondName);
                top_referral_no_3.setText(initialsThirdName);

                for (int i = 0; i < arrayLength; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    TopEarnerData topEarnerData = new TopEarnerData();
                    topEarnerData.setName(obj.getString("name"));
                    topEarnerData.setAmount(obj.getString("amount"));

                    topEarnerDataList.add(topEarnerData);
                    topEarnerAdapter.notifyDataSetChanged();

                }

            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSuccessOther(String data, String data_other) {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_home:
                finish();
                break;
        }
    }
}