package com.vuvrecharge.modules.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.OperatorPreferences;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    private DefaultPresenter mDefaultPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.earnings)
    TextView earnings;

    @BindView(R.id.full_name)
    TextView full_name;
    @BindView(R.id.email_id)
    TextView email_id;
    @BindView(R.id.mobile_number)
    TextView mobile_number;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.sponsor_id)
    TextView sponser_id;
    @BindView(R.id.reg_time)
    TextView reg_time;
    @BindView(R.id.user_type)
    TextView user_type;

    @BindView(R.id.country)
    TextView country;
    @BindView(R.id.state_name)
    TextView state_name;
    @BindView(R.id.city_name)
    TextView city_name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.landmark)
    TextView landmark;
    @BindView(R.id.pincode)
    TextView pincode;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.shop_name)
    TextView shop_name;
    @BindView(R.id.gst_no)
    TextView gst_no;
    @BindView(R.id.master_distributor)
    TextView master_distributor;
    @BindView(R.id.distributor)
    TextView distributor;
    @BindView(R.id.distributor_bg_main)
    CardView distributor_bg_main;
    @BindView(R.id.distributor_bg)
    LinearLayout distributor_bg;
    @BindView(R.id.master_distributor_bg)
    LinearLayout master_distributor_bg;

    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.instagram)
    ImageView instagram;
    @BindView(R.id.youtube)
    ImageView youtube;
    @BindView(R.id.facebook)
    ImageView facebook;
    @BindView(R.id.telegram)
    ImageView telegram;
    OperatorPreferences preferences;
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        ButterKnife.bind(this);
        setToolbarWhite(mToolbar);
        title.setText("Profile");
        mDefaultPresenter = new DefaultPresenter(this);
        preferences = new OperatorPreferences(this,"profileDetails");
        map = preferences.getData();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (map != null){
            if (Objects.equals(map.get("type"), "profile")){
                try {
                    JSONObject jsonObject = new JSONObject(map.get("list"));
                    full_name.setText(jsonObject.getString("name"));
                    email_id.setText(jsonObject.getString("email"));
                    username.setText(jsonObject.getString("username"));
                    mobile_number.setText(jsonObject.getString("mobile"));
                    user_type.setText(jsonObject.getString("user_type"));
                    country.setText(jsonObject.getString("country"));
                    state_name.setText("N/A");
                    if (jsonObject.getString("state_name") != null) {
                        if (!jsonObject.getString("state_name").isEmpty()) {
                            state_name.setText(jsonObject.getString("state_name"));
                        }
                    }
                    state_name.setText("N/A");
                    if (jsonObject.getString("city_name") != null) {
                        if (!jsonObject.getString("city_name").isEmpty()) {
                            state_name.setText(jsonObject.getString("city_name"));
                        }
                    }
                    landmark.setText("N/A");
                    if (jsonObject.getString("landmark") != null) {
                        if (!jsonObject.getString("landmark").isEmpty()) {
                            landmark.setText(jsonObject.getString("landmark"));
                        }
                    }
                    address.setText("N/A");
                    if (jsonObject.getString("address") != null) {
                        if (!jsonObject.getString("address").isEmpty()) {
                            address.setText(jsonObject.getString("address"));
                        }
                    }
                    pincode.setText("N/A");
                    if (jsonObject.getString("pincode") != null) {
                        if (!jsonObject.getString("pincode").isEmpty()) {
                            pincode.setText(jsonObject.getString("pincode"));
                        }
                    }
                    description.setText("N/A");
                    if (jsonObject.getString("description") != null) {
                        if (!jsonObject.getString("description").isEmpty()) {
                            description.setText(jsonObject.getString("description"));
                        }
                    }
                    shop_name.setText("N/A");
                    if (jsonObject.getString("shop_name") != null) {
                        if (!jsonObject.getString("shop_name").isEmpty()) {
                            shop_name.setText(jsonObject.getString("shop_name"));
                        }
                    }
                    gst_no.setText("N/A");
                    if (jsonObject.getString("gst_no") != null) {
                        if (jsonObject.getString("gst_no").isEmpty() || jsonObject.getString("gst_no").equals("null")) {
                            gst_no.setText("N/A");
                        }else{
                            gst_no.setText(jsonObject.getString("gst_no"));
                        }
                    }
                    sponser_id.setText("N/A");
                    if (jsonObject.getString("sponser_id") != null) {
                        if (!jsonObject.getString("sponser_id").isEmpty()) {
                            sponser_id.setText(jsonObject.getString("sponser_id"));
                        }
                    }

                    if (jsonObject.getString("master_distributor").equals("NA") &&
                            jsonObject.getString("distributor").equals("NA")) {
                        distributor_bg_main.setVisibility(View.GONE);
                    } else {
                        distributor_bg_main.setVisibility(View.VISIBLE);
                        if (jsonObject.getString("master_distributor").equals("NA")) {
                            master_distributor_bg.setVisibility(View.GONE);
                        } else {
                            master_distributor_bg.setVisibility(View.VISIBLE);
                            master_distributor.setText(jsonObject.getString("master_distributor"));
                        }
                        if (jsonObject.getString("distributor").equals("NA")) {
                            distributor_bg.setVisibility(View.GONE);
                        } else {
                            distributor_bg.setVisibility(View.VISIBLE);
                            distributor.setText(jsonObject.getString("distributor"));
                        }
                    }


                    long date_t = Long.parseLong(jsonObject.getString("reg_time").trim());
                    date_t = date_t * 1000;
                    Date date = new Date(date_t);
                    try {
                        String dateFormatNew = "N/A";
                        dateFormatNew = BaseMethod.dateFormatNew.format(date);
                        reg_time.setText(dateFormatNew);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                if (mDatabase != null){
                    if (mDatabase.getUserData() != null){
                        if (mDatabase.getUserData().getMobile() != null) {
                            mDefaultPresenter.memberProfileDetail(device_id, mDatabase.getUserData().getMobile());
                        }
                    }
                }
            }
        }

        instagram.setOnClickListener(this);
        youtube.setOnClickListener(this);
        facebook.setOnClickListener(this);
        telegram.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "profile");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
    }

    @Override
    protected void onPause() {
        hideAllDialog();
        super.onPause();
    }

    @Override
    public void onSuccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);
            full_name.setText(jsonObject.getString("name"));
            email_id.setText(jsonObject.getString("email"));
            username.setText(jsonObject.getString("username"));
            mobile_number.setText(jsonObject.getString("mobile"));
            user_type.setText(jsonObject.getString("user_type"));
            country.setText(jsonObject.getString("country"));

            preferences.setOperator("profile",userData);

            state_name.setText("N/A");
            if (jsonObject.getString("state_name") != null) {
                if (!jsonObject.getString("state_name").isEmpty()) {
                    state_name.setText(jsonObject.getString("state_name"));
                }
            }
            state_name.setText("N/A");
            if (jsonObject.getString("city_name") != null) {
                if (!jsonObject.getString("city_name").isEmpty()) {
                    state_name.setText(jsonObject.getString("city_name"));
                }
            }
            landmark.setText("N/A");
            if (jsonObject.getString("landmark") != null) {
                if (!jsonObject.getString("landmark").isEmpty()) {
                    landmark.setText(jsonObject.getString("landmark"));
                }
            }
            address.setText("N/A");
            if (jsonObject.getString("address") != null) {
                if (!jsonObject.getString("address").isEmpty()) {
                    address.setText(jsonObject.getString("address"));
                }
            }
            pincode.setText("N/A");
            if (jsonObject.getString("pincode") != null) {
                if (!jsonObject.getString("pincode").isEmpty()) {
                    pincode.setText(jsonObject.getString("pincode"));
                }
            }
            description.setText("N/A");
            if (jsonObject.getString("description") != null) {
                if (!jsonObject.getString("description").isEmpty()) {
                    description.setText(jsonObject.getString("description"));
                }
            }
            shop_name.setText("N/A");
            if (jsonObject.getString("shop_name") != null) {
                if (!jsonObject.getString("shop_name").isEmpty()) {
                    shop_name.setText(jsonObject.getString("shop_name"));
                }
            }
            gst_no.setText("N/A");
            if (jsonObject.getString("gst_no") != null) {
                if (jsonObject.getString("gst_no").isEmpty() || jsonObject.getString("gst_no").equals("null")) {
                    gst_no.setText("N/A");
                }else{
                    gst_no.setText(jsonObject.getString("gst_no"));
                }
            }
            sponser_id.setText("N/A");
            if (jsonObject.getString("sponser_id") != null) {
                if (!jsonObject.getString("sponser_id").isEmpty()) {
                    sponser_id.setText(jsonObject.getString("sponser_id"));
                }
            }

            if (jsonObject.getString("master_distributor").equals("NA") &&
                    jsonObject.getString("distributor").equals("NA")) {
                distributor_bg_main.setVisibility(View.GONE);
            } else {
                distributor_bg_main.setVisibility(View.VISIBLE);
                if (jsonObject.getString("master_distributor").equals("NA")) {
                    master_distributor_bg.setVisibility(View.GONE);
                } else {
                    master_distributor_bg.setVisibility(View.VISIBLE);
                    master_distributor.setText(jsonObject.getString("master_distributor"));
                }
                if (jsonObject.getString("distributor").equals("NA")) {
                    distributor_bg.setVisibility(View.GONE);
                } else {
                    distributor_bg.setVisibility(View.VISIBLE);
                    distributor.setText(jsonObject.getString("distributor"));
                }
            }


            long date_t = Long.parseLong(jsonObject.getString("reg_time").trim());
            date_t = date_t * 1000;
            Date date = new Date(date_t);
            try {
                String dateFormatNew = "N/A";
                dateFormatNew = BaseMethod.dateFormatNew.format(date);
                reg_time.setText(dateFormatNew);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(String userData, String second_message) {

    }

    @Override
    public void onSuccessOther(String data) {

    }

    @Override
    public void onSuccessOther(String data, String data_other) {

    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(bottomSheet, error);
            submit.setVisibility(View.VISIBLE);
        } else {
            showError(error);
        }
    }

    @Override
    public void onShowDialog(String message) {
        if (loading != null) {
            showLoading(loading);
            submit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHideDialog() {
        if (loading != null) {
            hideLoading(loading);
            submit.setVisibility(View.VISIBLE);
        }
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
    public void onClick(@NonNull View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.instagram:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/maxpe_payments/"));
                startActivity(intent);
                break;
            case R.id.youtube:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCkAdOopGYK6B6zkSdKhRJnA"));
                startActivity(intent);
                break;
            case R.id.facebook:
                Toast.makeText(this, "pending", Toast.LENGTH_SHORT).show();
                break;
            case R.id.telegram:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/MaxPePayments"));
                startActivity(intent);
                break;
        }
    }

    TextView submit;
    LinearLayout loading;

}
