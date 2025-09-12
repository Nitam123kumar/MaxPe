package com.vuvrecharge.modules.activities;

import static com.vuvrecharge.api.ApiServices.IMAGE_FOLLOWS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.FollowsAdapter;
import com.vuvrecharge.modules.model.Follows;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.utils.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupportActivity extends BaseActivity implements DefaultView, View.OnClickListener, FollowsAdapter.OnClickListener {

    DefaultPresenter mDefaultPresenter;
    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.app_name_tv)
    TextView app_name_tv;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.mobile_tv)
    TextView mobile_tv;
    @BindView(R.id.email)
    TextView email;
//    @BindView(R.id.tvAddress)
//    TextView address;
    @BindView(R.id.tv_whatsapp)
    TextView tv_whatsapp;
    @BindView(R.id.youtube)
    ImageView youtube;
    @BindView(R.id.xcode)
    ImageView xcode;
    @BindView(R.id.facebook)
    ImageView facebook;
    @BindView(R.id.linkedln)
    ImageView linkedln;
    @BindView(R.id.imgContactSupport)
    ImageView imgContactSupport;
    @BindView(R.id.imgWhatsAppSupport)
    ImageView imgWhatsAppSupport;
    @BindView(R.id.imgEmailSupport)
    ImageView imgEmailSupport;
    @BindView(R.id.imgAddressSupport)
    ImageView imgAddressSupport;

    @BindView(R.id.contactSupportLayout)
    ConstraintLayout contactSupportLayout;
    @BindView(R.id.emailSupportLayout)
    ConstraintLayout emailSupportLayout;
    @BindView(R.id.llDTHNumber)
    TextView llDTHNumber;
    @BindView(R.id.llPrepaidNumber)
    TextView llPrepaidNumber;
    @BindView(R.id.whatsAppSupportLayout)
    ConstraintLayout whatsAppSupportLayout;
    @BindView(R.id.updateLayout2)
    ConstraintLayout updateLayout2;
    @BindView(R.id.updatedLayout)
    ConstraintLayout updatedLayout;
    @BindView(R.id.follow_us_List)
    RecyclerView follow_us_List;
    @BindView(R.id.feedbackLayout)
    ConstraintLayout feedbackLayout;

    ArrayList<Follows> followsList = new ArrayList<>();
    FollowsAdapter adapter;
    String device_id = "";

    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        ButterKnife.bind(this);
        title.setText(R.string.help_and_support);
        mToolbar.setOnClickListener(this);
        contactSupportLayout.setOnClickListener(this);
        whatsAppSupportLayout.setOnClickListener(this);
        updateLayout2.setOnClickListener(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        xcode.setOnClickListener(this);
        linkedln.setOnClickListener(this);
        facebook.setOnClickListener(this);
        emailSupportLayout.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);
        updatedLayout.setOnClickListener(this);
        youtube.setOnClickListener(this);
        mDefaultPresenter=new  DefaultPresenter(this);
        device_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mDefaultPresenter.getFollow(device_id);
        follow_us_List.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
//        setStatusBarGradiant(this);
        llPrepaidNumber.setOnClickListener(v -> {
            Intent intent1 = new Intent(getActivity(), HelpLineActivity.class);
            intent1.putExtra("title", "Prepaid Toll Free Number");
            intent1.putExtra("type", "Prepaid");
            startActivity(intent1);
        });
        llDTHNumber.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HelpLineActivity.class);
            intent.putExtra("title", "DTH Toll Free Number");
            intent.putExtra("type", "DTH");
            startActivity(intent);
        });
        Log.d("Email",mDatabase.getUserData().getSupport_email());
    }

    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "support");
        if (mDatabase != null){
            if (mDatabase.getUserData() != null){
                setData(mDatabase.getUserData());
            }
        }
    }

    private void setData(@NonNull UserData userData) {
        if (userData != null){
            if (userData.getCompany_name() != null) {
                app_name_tv.setText(userData.getCompany_name());
            }
            if (userData.getSupport_message() != null) {
                time_tv.setText(userData.getSupport_message());
            }
            if (userData.getSupport_number() != null) {
                mobile_tv.setText(userData.getSupport_number());
            }
            if (userData.getSupport_email() != null) {
//                email.setText(userData.getSupport_email());
            }
            if (userData.getWhatsapp_number() != null) {
                tv_whatsapp.setText(userData.getWhatsapp_number());
            }
//            if (userData.getAddress() != null) {
//                address.setText(userData.getAddress());
//            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
    }

    @Override
    public void onClick(@NonNull View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.contactSupportLayout:
                if (selectCall()) {
                    makeACall(mDatabase.getUserData().getSupport_number());
                }
                break;
            case R.id.whatsAppSupportLayout:
                UserData userData = mDatabase.getUserData();
                String url = "https://api.whatsapp.com/send/?phone=91" + userData.getWhatsapp_number() + "&text=" + "Hello MaxPe Support, My registered mobile number is " + userData.getMobile();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                Intent chooser = Intent.createChooser(i, "Chat with...");
                startActivity(chooser);
                break;
            case R.id.updatedLayout:
                UserData userData1 = mDatabase.getUserData();
                String url1 = "https://api.whatsapp.com/send/?phone=91" + userData1.getWhatsapp_number() + "&text=" + "Hello MaxPe Support, My registered mobile number is " + userData1.getMobile();
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse(url1));
                Intent chooser1 = Intent.createChooser(in, "Chat with...");
                startActivity(chooser1);
                break;
            case R.id.updateLayout2:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/maxpe_payments/"));
                startActivity(intent);
                break;
            case R.id.emailSupportLayout:
                UserData emailUserData = mDatabase.getUserData();
                if (selectEmail()){
                    Intent email_intent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+emailUserData.getSupport_email()));
                    startActivity(Intent.createChooser(email_intent, "Send Email"));
                }
                break;
            case R.id.xcode:
                Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linkedln:
                Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.youtube:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCkAdOopGYK6B6zkSdKhRJnA"));
                startActivity(intent);
                break;
            case R.id.facebook:
                Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.telegram:
//                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/MaxPePayments"));
//                startActivity(intent);
//                break;
            case R.id.toolbar_layout:
                onBackPressed();
                break;
            case R.id.feedbackLayout:
                Intent feedbackIntent = new Intent(this, FeedbackActivity.class);
                startActivity(feedbackIntent);
                break;
        }
    }
    public void makeACall(String phoneNo) {
        String callInfo = "tel:" + phoneNo;
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(callInfo));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        startActivity(callIntent);
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

    @Override
    public void onSuccessOther(String data) {

    }

    @Override
    public void onSuccessOther(String data, String data_other) {

        try {

            Log.d("TAG_DATA", "followUs: " + data + "");

         if (data_other.equals("follows")) {
             try {
                 adapter = new FollowsAdapter(this, this);
                 JSONObject object = new JSONObject(data);
                 JSONArray array = object.getJSONArray("followers_data");
                 Log.d("TAG_DATA", "followUsList: " + array);
                 for (int i = 0; i < array.length(); i++) {
                     String logo = array.getJSONObject(i).getString("logo");
                     String title = array.getJSONObject(i).getString("title");
                     String redirect_url = array.getJSONObject(i).getString("redirect_url");

                     followsList.add(new Follows(IMAGE_FOLLOWS + "/" + logo, title, redirect_url));
                     adapter.addData(followsList);
                 }

                 follow_us_List.setAdapter(adapter);

             } catch (Exception e) {
                 Log.d("TAG_DATA", "followUs: " + e.getMessage());
             }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
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

    @Override
    public void onFollowLinkClick(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
}