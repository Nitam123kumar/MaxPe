package com.vuvrecharge.modules.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.model.UserData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupportActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.instagram)
    ImageView instagram;
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
    @BindView(R.id.llDTHNumber)
    TextView llDTHNumber;
    @BindView(R.id.llPrepaidNumber)
    TextView llPrepaidNumber;
    @BindView(R.id.whatsAppSupportLayout)
    ConstraintLayout whatsAppSupportLayout;
    @BindView(R.id.feedbackLayout)
    ConstraintLayout feedbackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        ButterKnife.bind(this);
        title.setText("Help & Support");
        mToolbar.setOnClickListener(this);
        contactSupportLayout.setOnClickListener(this);
        whatsAppSupportLayout.setOnClickListener(this);
        instagram.setOnClickListener(this);
        xcode.setOnClickListener(this);
        linkedln.setOnClickListener(this);
        facebook.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);
        youtube.setOnClickListener(this);
        setStatusBarGradiant(this);
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
        instagram.setOnClickListener(view -> {
           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/maxpe_payments/"));
            startActivity(intent);
        });
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
                email.setText(userData.getSupport_email());
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
            case R.id.instagram:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/maxpe_payments/"));
                startActivity(intent);
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

}