package com.vuvrecharge.modules.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vuvrecharge.api.ApiServices.BASE_URL;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import androidx.webkit.WebSettingsCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.ImageSliderAdapter;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareEarnActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    private DefaultPresenter mDefaultPresenter;

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.txtReferId)
    EditText referCode;
    @BindView(R.id.btnReferCopy)
    TextView btnReferCopy;
//    @BindView(R.id.txtReferSlug)
//    TextView txtReferSlug;

    @BindView(R.id.btnInvite)
    ConstraintLayout btnInvite;
    @BindView(R.id.available_referralTV)
    TextView txtMyReferralValue;
    @BindView(R.id.myBonus_availableTxt)
    TextView txtReferralIncomeValue;
    @BindView(R.id.available_SuccessfulReferralTV)
    TextView txtSuccessfulReferralCount;
//    @BindView(R.id.txtBonusEarnCount)
//    TextView txtBonusEarnCount;
//    @BindView(R.id.txtExpectedBonusCount)
//    TextView txtExpectedBonusCount;
//    @BindView(R.id.txtReferMessage)
//    TextView txtReferMessage;
//    @BindView(R.id.txtReferMaxSlug)
//    TextView txtReferMaxSlug;

    @BindView(R.id.txtTermsCondition)
    TextView txtTermsCondition;

    @BindView(R.id.SuccessfulReferralView)
    View viewMyReferral;

    @BindView(R.id.maxPoints_availableTxt)
    TextView maxPoints_availableTxt;
    @BindView(R.id.top_Earner)
    TextView top_EarnerTV;
    @BindView(R.id.top_earner_ImageSlider)
    ImageSlider top_earner_ImageSlider;
    @BindView(R.id.myBonusView)
    View viewReferralIncome;
    @BindView(R.id.available_earnTV)
    TextView available_earnTV;
    @BindView(R.id.viewPager)
    ViewPager2 viewPager;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.loading)
    LinearLayout loading;
    String shareText = "";
    TextView submit;
//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout refresh_layout;

    @BindView(R.id.worm_dots_indicator)
    DotsIndicator wormDotsIndicator;
    ImageSliderAdapter imageSliderAdapter;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;


    ArrayList<String> top_earner_list1 = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareearn);
        ButterKnife.bind(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        title.setText("Refer & Earn");
        mDefaultPresenter = new DefaultPresenter(this);
        mToolbar.setOnClickListener(this);
        btnInvite.setOnClickListener(this);
        btnReferCopy.setOnClickListener(this);
        top_EarnerTV.setOnClickListener(this);
        setStatusBarGradiant(this);
        viewMyReferral.setOnClickListener(this);
        viewReferralIncome.setOnClickListener(this);
        txtTermsCondition.setOnClickListener(this);
        WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView.getSettings().setSafeBrowsingEnabled(true);
        }

        if (mDatabase != null) {
            if (mDatabase.getUserData() != null) {
                if (mDatabase.getUserData().getReferCode() != null) {
                    referCode.setText(mDatabase.getUserData().getReferCode());
                }
            }
        }
        mDefaultPresenter.totalReferrals(device_id);

        imageSliderAdapter = new ImageSliderAdapter(top_earner_list1, this);
        viewPager.setAdapter(imageSliderAdapter);
        wormDotsIndicator.setViewPager2(viewPager);
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int totalItem = imageSliderAdapter.getItemCount();
                if (currentItem < totalItem - 1) {
                    viewPager.setCurrentItem(currentItem + 1, true);
                } else {
                    viewPager.setCurrentItem(0, true);
                }
                sliderHandler.postDelayed(this, 3000);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

//        refresh_layout.setOnRefreshListener(this::refreshData);
//        refresh_layout.setRefreshing(true);


        ArrayList<SlideModel> top_earner_list = new ArrayList<>();

        top_earner_list.add(new SlideModel(R.drawable.image_dummy, ScaleTypes.FIT));
        top_earner_list.add(new SlideModel(R.drawable.image_dummy, ScaleTypes.FIT));
        top_earner_list.add(new SlideModel(R.drawable.image_dummy, ScaleTypes.FIT));
        top_earner_list.add(new SlideModel(R.drawable.image_dummy, ScaleTypes.FIT));

        top_earner_ImageSlider.setImageList(top_earner_list, ScaleTypes.FIT);
//        top_earner_ImageSlider.setIndicatorSelectedColor(Color.RED);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "shareEarn");
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
    void refreshData() {
        mDefaultPresenter.totalReferrals(device_id);
    }
    @Override
    protected void onPause() {
        hideAllDialog();
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
    @SuppressLint({"SetJavaScriptEnabled", "NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onSuccess(String userData) {
        try {
//            refresh_layout.setRefreshing(false);
            JSONObject jsonObject = new JSONObject(userData);
            txtMyReferralValue.setText(jsonObject.getString("totalReferrals"));
            txtReferralIncomeValue.setText("\u20b9 " + jsonObject.getString("totalReferralPaid"));
            String referText = jsonObject.getString("referPageText");
            JSONObject object = new JSONObject(referText);
            maxPoints_availableTxt.setText(mDatabase.getUserData().getCashbackPoints());

            txtSuccessfulReferralCount.setText(object.getString("successfull_referals"));

            available_earnTV.setText(object.getString("refer"));
//            txtBonusEarnCount.setText("\u20b9 " + object.getString("bonus_earned"));
//            txtExpectedBonusCount.setText("\u20b9 " + object.getString("expected_bonus"));
//            txtReferSlug.setText(jsonObject.getString("referal_slug"));
//            txtRefer.setText(jsonObject.getString("referal_text"));
//            txtReferMessage.setText(object.getString("refer"));
//            txtReferMaxSlug.setText(object.getString("reward"));

            String webViewHtml = jsonObject.getString("referal_html");
            webView.loadDataWithBaseURL(BASE_URL, webViewHtml, "text/html", "UTF-8", null);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setBuiltInZoomControls(false);
            webView.getSettings().setDisplayZoomControls(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setHorizontalScrollbarOverlay(false);
            webView.setWebViewClient(new WebViewClient());

            Log.d("referal_slides", webViewHtml);
            shareText = jsonObject.getString("shareText");

            JSONObject sliderObject = jsonObject.getJSONObject("referal_slides");
            top_earner_list1.clear();
            if (sliderObject.length() > 0) {

                for (int i = 0; i < sliderObject.length(); i++) {
                    top_earner_list1.add(sliderObject.getString(String.valueOf(i)));
                }
                imageSliderAdapter.notifyDataSetChanged();


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
            showError(error);
            submit.setVisibility(View.VISIBLE);
        } else {
            showError(error);
        }
    }

    @Override
    public void onShowDialog(String message) {
        if (bottomSheet != null) {
            showLoading(loading_dialog);
            submit.setVisibility(GONE);
        } else {
            showLoading(loading);
        }
    }

    @Override
    public void onHideDialog() {
        if (bottomSheet != null) {
            hideLoading(loading_dialog);
            submit.setVisibility(VISIBLE);
        } else {
            hideLoading(loading);
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
    public void onClick(@NonNull View v) {

        UserData userData = mDatabase.getUserData();
        if (v.getId() == R.id.btnInvite) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = (shareText + "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName() + "&referrer=" + userData.getReferCode());
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (v.getId() == R.id.btnReferCopy) {
            if (mDatabase != null) {
                if (mDatabase.getUserData() != null) {
                    if (mDatabase.getUserData().getReferCode() != null) {
                        copyReferRal(mDatabase.getUserData().getReferCode());
                    }
                }
            }
        } else if (v.getId() == R.id.SuccessfulReferralView) {
            Intent intent = new Intent(getActivity(), MyReferralsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.myBonusView) {
            Intent intent = new Intent(getActivity(), ReferralsIncomeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.toolbar_layout) {
            onBackPressed();
            getActivity().finish();
        } else if (v.getId() == R.id.top_Earner) {
            Intent intent = new Intent(this, TopEarnerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.txtTermsCondition) {
            Intent intent = new Intent(this, ReferandEarnTermsActivity.class);
            startActivity(intent);
        }
    }

    @SuppressLint({"ObsoleteSdkInt,UseCompatLoadingForDrawables"})
    @androidx.annotation.RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    static void setStatusBarGradiant(Activity activity) {
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
