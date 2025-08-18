package com.vuvrecharge.modules.activities;

import static com.vuvrecharge.api.ApiServices.OFFER_ZONE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.custom.HtmlImageGetter;
import com.vuvrecharge.databinding.BottomAddWalletBalanceLayoutBinding;
import com.vuvrecharge.databinding.SetMpinAlertDailogLayoutBinding;
import com.vuvrecharge.databinding.WhatsappDialogBinding;
import com.vuvrecharge.modules.activities.newActivities.CommissionChartActivity;
import com.vuvrecharge.modules.activities.newActivities.ElectricityActivity;
import com.vuvrecharge.modules.activities.newActivities.MobileBankingActivity;
import com.vuvrecharge.modules.adapter.DashboardAdapter;
import com.vuvrecharge.modules.adapter.ImageSliderAdapter;
import com.vuvrecharge.modules.adapter.OTTSubscriptionsAdapter;
import com.vuvrecharge.modules.adapter.OfferSliderRecyclerViewAdapter;
import com.vuvrecharge.modules.adapter.PaymentSettingAdapter;
import com.vuvrecharge.modules.adapter.RecyclerViewSliderAdapter;
import com.vuvrecharge.modules.adapter.SliderAdapter;
import com.vuvrecharge.modules.adapter.SliderAdapterBanner;
import com.vuvrecharge.modules.adapter.SpotlightServicesAdapter;
import com.vuvrecharge.modules.model.DashboardMenu;
import com.vuvrecharge.modules.model.OTTSubscriptionsData;
import com.vuvrecharge.modules.model.OfferSlider;
import com.vuvrecharge.modules.model.PaymentSetting;
import com.vuvrecharge.modules.model.SliderData;
import com.vuvrecharge.modules.model.SliderItems;
import com.vuvrecharge.modules.model.SpotlightData;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.model.YoutubeSlides;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.utils.LocaleHelper;
import com.vuvrecharge.utils.permission.PermissionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint({"SetTextI18n", "NonConstantResourceId"})
public class MainActivity extends BaseActivity implements DefaultView,
        View.OnClickListener, DashboardAdapter.ItemClickListener,
        PaymentSettingAdapter.OnClickListener,
        OfferSliderRecyclerViewAdapter.ItemClickListener,
        SpotlightServicesAdapter.ItemClickListener,
        OTTSubscriptionsAdapter.ItemClickListener,
        SliderAdapterBanner.ItemClickListener,
        InstallStateUpdatedListener {

    @BindView(R.id.hide_keyboard)
    EditText hide_keyboard;
    DefaultPresenter mDefaultPresenter;
    @BindView(R.id.add_balance)
    TextView add_balance;
    @BindView(R.id.statements)
    TextView statements;
    @BindView(R.id.txtMobileRecharge)
    TextView txtMobileRecharge;
    @BindView(R.id.txtRefer)
    TextView txtRefer;
    @BindView(R.id.recyclerSlider)
    ViewPager2 image_slider;
    @BindView(R.id.available_balance)
    TextView available_balance;
    //    @BindView(R.id.txtTitleMain)
//    TextView txtTitleMain;
    @BindView(R.id.members_bg)
    LinearLayout members_bg;
    @BindView(R.id.downline_pckage)
    LinearLayout downline_pckage;
    @BindView(R.id.ott_recharge)
    ImageView ott_recharge;

    @BindView(R.id.spotlight_services_recyclerView)
    RecyclerView spotlight_services_recyclerView;
    @BindView(R.id.add_member)
    LinearLayout add_member;
    @BindView(R.id.open_account)
    View open_account;
    @BindView(R.id.share_earn_layout)
    View share_earn_layout;
    @BindView(R.id.refresh)
    ImageView refresh;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.whatsup_alert)
    ImageView whatsup_alert;
    @BindView(R.id.headerImage)
    ImageView headerImage;
    @BindView(R.id.news)
    TextView news;
    @BindView(R.id.ottRecyclerView)
    RecyclerView ottRecyclerView;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.total_ban)
    TextView total_ban;
    @BindView(R.id.recharge_and_txt)
    TextView recharge_and_txt;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.viewPager)
    ViewPager2 viewPager;
    @BindView(R.id.img)
    ImageView home_menu_imageView;
    @BindView(R.id.indicator)
    DotsIndicator indicator;
    @BindView(R.id.youtube_indicator)
    DotsIndicator youtube_indicator;
    @BindView(R.id.commission_report_layout)
    View commission_report_layout;
    @BindView(R.id.slider_layout)
    RelativeLayout slider_layout;
    @BindView(R.id.support_layout)
    View support_layout;
    @BindView(R.id.history)
    View history;
    //    @BindView(R.id.all_services)
//    View all_services;
    @BindView(R.id.offer_for_you_recyclerView)
    RecyclerView offer_for_you_recyclerView;
    @BindView(R.id.main_layout)
    LinearLayout main_layout;
    @BindView(R.id.viewPagerImageSlider)
    ViewPager2 viewPagerImageSlider;
    @BindView(R.id.into_tab_layout)
    TabLayout into_tab_layout;
    @BindView(R.id.recyclerviewDashboard)
    RecyclerView recyclerviewDashboard;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;
    @BindView(R.id.prepaid)
    LinearLayout prepaid;
    @BindView(R.id.postpaid)
    LinearLayout postpaid;
    @BindView(R.id.DTH)
    LinearLayout dTH;
    @BindView(R.id.Landline)
    LinearLayout landline;
    @BindView(R.id.Electricity)
    LinearLayout electricity;
    @BindView(R.id.Fastag)
    LinearLayout fasTag;
    @BindView(R.id.Cylinder)
    LinearLayout cylinder;
    @BindView(R.id.ViewAll)
    LinearLayout viewAll;
    @BindView(R.id.headerImage_layout)
    ConstraintLayout headerImage_layout;
    @BindView(R.id.imgReferAndEarn)
    ImageView imgReferAndEarn;
    @BindView(R.id.prepaidTxt)
    TextView prepaidTxt;

    List<String> color = new ArrayList<>();
    List<YoutubeSlides> youtubeVideosList = new ArrayList<>();
    List<String> imageList_offer;
    List<String> spotlight;
    DashboardAdapter adapter;
    List<DashboardMenu> menus = new ArrayList<>();
    List<OfferSlider> offerSliderList = new ArrayList<>();
    List<SliderItems> sliderItemsList = new ArrayList<>();
    Timer timer1 = new Timer();
    ArrayList<PaymentSetting> list = new ArrayList<>();
    ArrayList<YoutubeSlides> youtubeVideos = new ArrayList<>();
    String data = null;
    String allServices = null;
    String dataPayment = null;
    JSONObject json;
    Animation rotation = null;
    OfferSlider offerSlider;
    private AppUpdateManager appUpdateManager;
    private static final int MY_REQUEST_CODE = 100;
    SharedPreferences preferences;
    PermissionUtil permissionUtil = new PermissionUtil();
    BottomSheetDialog dialog = null;
    AlertDialog dialog1 = null;
    RecyclerViewSliderAdapter sliderAdapter;
    OfferSliderRecyclerViewAdapter offerSliderAdapter;
    SpotlightServicesAdapter spotlightServicesAdapter;
    OTTSubscriptionsAdapter oTTAdapter;
    List<String> ott_List;
    List<OTTSubscriptionsData> oTTList = new ArrayList<>();
    List<SliderData> slider_banner = new ArrayList<>();
    String intentName = null;
    String extraData = null;
    String redirection_type = null;
    String redirection_url = null;
    int lastSelectedPosition = 0;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;
    SliderAdapterBanner imageSliderAdapter;
    private boolean recreatedOnce = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }

    @Override
    protected void onCreate(
            @Nullable Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(getActivity());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setStatusBarGradiant(this);
        add_balance.setOnClickListener(this);
        statements.setOnClickListener(this);
        open_account.setOnClickListener(this);
        refresh.setOnClickListener(this);
        share.setOnClickListener(this);
        members_bg.setOnClickListener(this);
        add_member.setOnClickListener(this);
        whatsup_alert.setOnClickListener(this);
        commission_report_layout.setOnClickListener(this);
        share_earn_layout.setOnClickListener(this);
        support_layout.setOnClickListener(this);
        home_menu_imageView.setOnClickListener(this);
        prepaid.setOnClickListener(this);
        postpaid.setOnClickListener(this);
        dTH.setOnClickListener(this);
        landline.setOnClickListener(this);
        imgReferAndEarn.setOnClickListener(this);
        electricity.setOnClickListener(this);
        fasTag.setOnClickListener(this);
        cylinder.setOnClickListener(this);
        viewAll.setOnClickListener(this);
        history.setOnClickListener(this);
//        all_services.setOnClickListener(this);
        downline_pckage.setOnClickListener(this);
        mDefaultPresenter = new DefaultPresenter(this);
        appUpdate();
        notifications();
        offer_for_you_recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        image_slider.setClipChildren(false);
        image_slider.setClipToPadding(false);
        image_slider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        image_slider.setOffscreenPageLimit(2);

        // Page transformer for spacing
        int pageMarginPx = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        int offsetPx = getResources().getDimensionPixelOffset(R.dimen.offset);

        image_slider.setPageTransformer((page, position) -> {
            float offset = position * -(2 * offsetPx + pageMarginPx);
            page.setTranslationX(offset);
        });

        sliderAdapter = new RecyclerViewSliderAdapter(this, MainActivity.this, youtubeVideosList);
        image_slider.setAdapter(sliderAdapter);
        youtube_indicator.setViewPager2(image_slider);
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = image_slider.getCurrentItem();
                int totalItem = sliderAdapter.getItemCount();
                if (currentItem < totalItem - 1) {
                    image_slider.setCurrentItem(currentItem + 1, true);
                } else {
                    image_slider.setCurrentItem(0, true);
                }
                sliderHandler.postDelayed(this, 2000);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 2000);

        image_slider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000);
            }
        });
        image_slider.post(() -> {
            image_slider.setCurrentItem(0, false);
            youtube_indicator.setViewPager2(image_slider);
        });

        spotlight_services_recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        ottRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        refresh_layout.setOnRefreshListener(this::refreshData);
        refresh_layout.setRefreshing(true);
        headerImage_layout.setOnClickListener(v -> {
            onClickMainHeroBanner(redirection_type, intentName, extraData, redirection_url);
        });


        imageSliderAdapter = new SliderAdapterBanner(getActivity(), slider_banner, this);
        viewPager.setAdapter(imageSliderAdapter);
        indicator.setViewPager2(viewPager);
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

        viewPager.post(() -> {
            viewPager.setCurrentItem(0, false);
            indicator.setViewPager2(viewPager);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshData();
    }


    private boolean notifications() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            if (permissionUtil.checkMarshMellowPermission()) {
                if (permissionUtil.verifyPermissions(this, permissionUtil.getAllPermissions()))
                    return true;
                else {
                    ActivityCompat.requestPermissions(MainActivity.this, permissionUtil.getAllPermissions(), ALL_PERMISSION);
                    return false;
                }
            }
        }
        return true;
    }

    private void appUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, this, MY_REQUEST_CODE);
                    // Register the install state listener
                    appUpdateManager.registerListener(this);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onStateUpdate(
            @NonNull InstallState installState
    ) {

    }

    public void showMessageDialogActivityNotCancel(
            String message
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void setData(
            String from
    ) {
        try {
            refresh_layout.setRefreshing(false);
            UserData userData = mDatabase.getUserData();
            available_balance.setText("\u20b9" + userData.getEarnings());
            total_ban.setText("\u20b9" + userData.getEarnings());
            name_tv.setText("Welcome, " + userData.getName());
//            txtTitleMain.setText(userData.getRecharge_pay_bill_string());

            if (mDatabase.getUserData().getUser_type().equals("Distributor") ||
                    mDatabase.getUserData().getUser_type().equals("Master_Distributor") ||
                    mDatabase.getUserData().getUser_type().equals("Admin")) {
                members_bg.setVisibility(View.VISIBLE);
                main_layout.setVisibility(View.VISIBLE);
            }
            if (mDatabase.getUserData().getUser_type().equals("Distributor") ||
                    mDatabase.getUserData().getUser_type().equals("Master_Distributor")) {
                add_member.setVisibility(View.VISIBLE);
                downline_pckage.setVisibility(View.VISIBLE);
                main_layout.setVisibility(View.VISIBLE);
            }

//            color = new ArrayList<>();
//            slider_banner.clear();
//            List<SliderData> slide = (mDatabase.getUserData().getSlides());
//            for (SliderData image : slide) {
//                color.add(mDatabase.getUserData().getSlide_path() + "/" + image.getSlide());
//                Log.d("Url Data", mDatabase.getUserData().getSlide_path());
//                slider_banner.addAll(slide);
//            }
//            imageSliderAdapter = new SliderAdapterBanner(this, color,slider_banner, this);
//            viewPager.setAdapter(imageSliderAdapter);
//            indicator.setViewPager2(viewPager);
//            imageSliderAdapter.notifyDataSetChanged();

//            youtubeVideosList = new ArrayList<>();
//            List<YoutubeSlides> youtubeSlidesImages = (mDatabase.getUserData().getYoutubeVideoSliders());
//            youtubeVideosList.addAll(youtubeSlidesImages);
//
//            sliderAdapter = new RecyclerViewSliderAdapter(this, MainActivity.this, youtubeVideosList);
//            image_slider.setAdapter(sliderAdapter);
//            youtube_indicator.setViewPager2(image_slider);
//            Log.d("youtubeVideosList", String.valueOf(youtubeVideosList));
//            sliderAdapter.notifyDataSetChanged();

            imageList_offer = new ArrayList<>();
            List<OfferSlider> offer_sliders = (mDatabase.getUserData().getOffer_slides());
            for (OfferSlider offerSlides : offer_sliders) {
                imageList_offer.add(mDatabase.getUserData().getOffer_slides_path() + "/" + offerSlides.getLogo() + getTitle());

                if (offerSlides.getData() != null) {
                    String intentData = offerSlides.getData();
                    Log.d("djfhsdjfgbsdj", intentData);
                    JSONObject activityData = new JSONObject(offerSlides.getData());
                    JSONObject activityExtraData = new JSONObject(activityData.getString("extra_data"));

                    if (activityExtraData.length() > 0) {

                        Iterator<String> keys = activityExtraData.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String value = activityExtraData.optString(key, "");

                        }

                    }
                    Log.d("activityExtraData", "Data Title: " + activityExtraData);
//                    Log.d("OfferSlider", "Data Intent: " + object.getString("intent_name"));

                }
            }

            ott_List = new ArrayList<>();
            List<OTTSubscriptionsData> ottItem = (mDatabase.getUserData().getLogoSliders());
            for (OTTSubscriptionsData ottData : ottItem) {
                ott_List.add(mDatabase.getUserData().getOtt_slides_path() + "/" + ottData.getLogo());

                Log.d("OTTData", ottData.getLogo() + " " + ottData.getTitle());
            }
            oTTList.clear();
            oTTList.addAll(ottItem);
            oTTAdapter = new OTTSubscriptionsAdapter(this, oTTList, ott_List, this);
            ottRecyclerView.setAdapter(oTTAdapter);

            spotlight = new ArrayList<>();
            List<SpotlightData> spotlightData = (mDatabase.getUserData().getSpotlight_services());
            for (SpotlightData spotlightData1 : spotlightData) {
                spotlight.add(spotlightData1.getLogo() + getTitle());

                if (spotlightData1.getData() != null) {
                    String intentData = spotlightData1.getData();
                    JSONObject object = new JSONObject(spotlightData1.getData());

                    Log.d("Spotlight", "Data Title: " + object.getString("extra_data"));
                    Log.d("Spotlight", "Data Intent: " + object.getString("intent_name"));
                }
            }

            spotlightServicesAdapter = new SpotlightServicesAdapter(this, spotlight, mDatabase.getUserData().getSpotlight_services(), this);
            spotlight_services_recyclerView.setAdapter(spotlightServicesAdapter);

            offerSliderAdapter = new OfferSliderRecyclerViewAdapter(this, imageList_offer, userData.getOffer_slides(), this);
            offer_for_you_recyclerView.setAdapter(offerSliderAdapter);
            Log.d("imageList_offer", String.valueOf(mDatabase.getUserData().getOffer_slides()));


            if (from.equals("Api")) {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                int versionCode = pInfo.versionCode;
                int onlineVersionCode = Integer.parseInt(userData.getVersion_code());
                int update_version_code = Integer.parseInt(userData.getUpdate_version_code());
                if (versionCode < update_version_code) {
                    if (versionCode < onlineVersionCode) {
                        initiatePopupWindow_update(userData.getUpdate_message() + "", false);
                    } else {
                        if (BaseMethod.call_first.equals("Yes")) {
                            BaseMethod.call_first = "No";
                            initiatePopupWindow_update(userData.getUpdate_message() + "", true);
                        }
                    }
                }
            }
            sliderItemsList.clear();
            sliderItemsList = mDatabase.getUserData().getSliderItemsList();

            if (!sliderItemsList.isEmpty()) {
                slider_layout.setVisibility(View.GONE);
            } else {
                slider_layout.setVisibility(View.GONE);
            }

            viewPagerImageSlider.setVisibility(View.GONE);
            viewPagerImageSlider.setAdapter(new SliderAdapter(this, sliderItemsList, viewPagerImageSlider));
            viewPagerImageSlider.setOffscreenPageLimit(3);
            viewPagerImageSlider.setClipChildren(false);
            viewPagerImageSlider.setClipToPadding(false);
            viewPagerImageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            CompositePageTransformer pageTransformer = new CompositePageTransformer();
            pageTransformer.addTransformer(new MarginPageTransformer(10));
            pageTransformer.addTransformer((page, position) -> {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            });
            viewPagerImageSlider.setPageTransformer(pageTransformer);
            new TabLayoutMediator(into_tab_layout, viewPagerImageSlider, (tab, position) -> {
            }).attach();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickListener(String name) {
        Intent intent;
        switch (name) {
            case "Prepaid":
                intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("title", "Prepaid Recharge");
                startActivity(intent);
                break;
            case "Postpaid":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Postpaid Recharge");
                startActivity(intent);
                break;
            case "DTH":
                intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("title", "DTH Recharge");
                startActivity(intent);
                break;
            case "Electricity":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Electricity Bill");
                startActivity(intent);
                break;
            case "Water":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Water Bill");
                startActivity(intent);
                break;
            case "Cylinder":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Cylinder Bill");
                startActivity(intent);
                break;
            case "Landline":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Broadband/Landline");
                startActivity(intent);
                break;
            case "Fastag":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Fastag");
                startActivity(intent);
                break;
            case "Gas":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Gas Bill");
                startActivity(intent);
                break;
            case "Insurance":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Insurance");
                startActivity(intent);
                break;
            case "GiftCards":
                intent = new Intent(getActivity(), BillActivity.class);
                intent.putExtra("title", "Purchase Gift cards");
                startActivity(intent);
                break;
            case "BBPS":
                intent = new Intent(getActivity(), BharatBillPayActivity.class);
                intent.putExtra("title", "Bharat Bill Pay");
                intent.putExtra("data", data);
                startActivity(intent);
                break;
            case "CreditCardPayment":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Credit Card Payment");
                startActivity(intent);
                break;
            case "LoanRepayment":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Loan Re payment");
                startActivity(intent);
                break;
            case "CableTV":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Cable TV");
                startActivity(intent);
                break;
            case "MunicipalTax":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Municipal Tax");
                startActivity(intent);
                break;
            case "HousingSociety":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Housing Society");
                startActivity(intent);
                break;
            case "ClubAssociation":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Club Association");
                startActivity(intent);
                break;
            case "HospitalPathology":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Hospital Pathology");
                startActivity(intent);
                break;
            case "Subscriptions":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Subscription Fees");
                startActivity(intent);
                break;
            case "Donation":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Donation");
                startActivity(intent);
                break;
            case "Hospital":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Hospital");
                startActivity(intent);
                break;
            case "RecurringDeposit":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Recurring Deposit");
                startActivity(intent);
                break;
            case "PrepaidMeter":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Prepaid Meter");
                startActivity(intent);
                break;
            case "NCMCRecharge":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "NCMC Recharge");
                startActivity(intent);
                break;
            case "MunicipalServices":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Municipal Services");
                startActivity(intent);
                break;
            case "EducationFees":
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Education Fees");
                startActivity(intent);
                break;
            default:
                showToast("Coming Soon");
        }
    }

    private void onClickMainHeroBanner(String redirection_type, String intent_name, String activityExtraData, String link) {
        Intent intent;
        Class<?> clazz = null;
        if (redirection_type.equals("activity")) {
            if (intent_name != null) {
                try {
                    clazz = Class.forName(intent_name);
                    intent = new Intent(getActivity(), clazz);

                    JSONObject object = new JSONObject(activityExtraData);

                    if (object.length() > 0) {

                        Iterator<String> keys = object.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String value = object.optString(key, "");

                            intent.putExtra(key, value);
                        }

                    }
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        } else if (redirection_type.equals("in_app")) {
            if (redirection_type != null && !redirection_type.isEmpty()) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();

                String chromePackage = "com.android.chrome";
                PackageManager pm = getActivity().getPackageManager();
                try {
                    pm.getPackageInfo(chromePackage, 0);
                    customTabsIntent.intent.setPackage(chromePackage);
                } catch (PackageManager.NameNotFoundException e) {

                }

                customTabsIntent.launchUrl(getActivity(), Uri.parse(link));
            } else {

            }

        } else if (redirection_type.equals("external")) {
            if (redirection_type != null && !redirection_type.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                try {
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {

                }
            } else {

            }
        }
    }

    private class SliderTimer
            extends TimerTask {
        @Override
        public void run() {
            try {
                getActivity().runOnUiThread(() -> {
                    if (viewPager.getCurrentItem() < slider_banner.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void whatsAppDialog(
            String data
    ) {
        try {
            WhatsappDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.whatsapp_dialog, null, false);
            Dialog dialog = new Dialog(this);
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(false);
            JSONObject jsonObject = new JSONObject(data);
            binding.message.setText(jsonObject.getString("message"));
            binding.action.setText(jsonObject.getString("buttonText"));

            String whatsappMsg = jsonObject.getString("whatsappMsg");
            String number = jsonObject.getString("number");
            binding.close.setOnClickListener(v -> dialog.dismiss());
            binding.action.setOnClickListener(v -> {
                boolean installed = appInstalledOrNot();
                if (installed) {
                    Intent waIntent = new Intent(Intent.ACTION_VIEW);
                    waIntent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+91" + number + "&text=" + whatsappMsg));
                    startActivity(waIntent);
                } else {
                    onShowToast("WhatsApp not install in your phone. Please install WhatsApp first.");
                }
                dialog.dismiss();
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean appInstalledOrNot() {
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void initiatePopupWindow_update(
            String message,
            boolean cancel_status
    ) {
        try {
            final Dialog openDialog = new Dialog(this);
            openDialog.setContentView(R.layout.update_app_status_popup);
            openDialog.setCancelable(cancel_status);
            TextView feature_title = openDialog.findViewById(R.id.feature_title);
            TextView not_now = openDialog.findViewById(R.id.not_now);
            TextView update = openDialog.findViewById(R.id.update);
            if (cancel_status) {
                not_now.setVisibility(View.VISIBLE);
            } else {
                not_now.setVisibility(View.GONE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                feature_title.setText(Html.fromHtml(message.trim(),
                        Html.FROM_HTML_MODE_COMPACT, new HtmlImageGetter(this, feature_title), null));
            } else {
                feature_title.setText(Html.fromHtml(message.trim(),
                        new HtmlImageGetter(this, feature_title), null));
            }

            not_now.setOnClickListener(v -> openDialog.dismiss());

            update.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                startActivity(i);
            });
            openDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBalance() {
        try {
            FrameLayout bottomSheet = null;
            TextView submit;
            LinearLayout loading_dialog;
            list.clear();
            BottomAddWalletBalanceLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.bottom_add_wallet_balance_layout, null, false);

            dialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(true);
            changeStatusBarColor(dialog);
            bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }
            if (json.getString("phonepe_getway").equals("Yes")) {
                dialog.cancel();
                list.add(new PaymentSetting(R.drawable.phonepe_logo_2, "PhonePe", "Payment Gateway"));
            }
            if (json.getString("hdfc_dynamic_getway").equals("Yes")) {
                dialog.cancel();
                list.add(new PaymentSetting(R.drawable.upi_logo_2, "UPI", "Payment Gateway"));
            }
            if (json.getString("razorpay_getway").equals("Yes")) {
                dialog.cancel();
                list.add(new PaymentSetting(R.drawable.razorpay_logo_2, "Razorpay", "Payment Gateway"));
            }
//            if (json.getString("cashfree_getway").equals("Yes")){
//                dialog.cancel();
//                list.add(new PaymentSetting(R.drawable.cashfree_logo,"CashFree","Payment Gateway"));
//            }
            if (json.getString("paytm_getway").equals("Yes")) {
                dialog.cancel();
                list.add(new PaymentSetting(R.drawable.paytm_logo_2, "Paytm", "Payment Gateway"));
            }
            if (json.getString("manual_getway").equals("Yes")) {
                dialog.cancel();
                list.add(new PaymentSetting(R.drawable.mobile_banking_logo_2, "Mobile", "Banking (IMPS)"));
            }

            if (!list.isEmpty()) {
                LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                binding.recyclerViewPaymentSetting.setLayoutManager(manager);
            }
            if (list.size() > 1) {
                LinearLayoutManager manager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                binding.recyclerViewPaymentSetting.setLayoutManager(manager2);
            }

            if (list.size() > 2) {
                binding.recyclerViewPaymentSetting.setLayoutManager(new GridLayoutManager(this, 3));
            }
            if (list.size() > 3) {
                binding.recyclerViewPaymentSetting.setLayoutManager(new GridLayoutManager(this, 4));
            }
            PaymentSettingAdapter adapter1 = new PaymentSettingAdapter(this, this);
            adapter1.addData(list);
            binding.recyclerViewPaymentSetting.setAdapter(adapter1);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG_DATA", "addBalance: " + e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 25) {
                Bundle b = data.getExtras();
                if (b != null) {

                    String pidOpt = "<PidOptions><Opts fCount=\"1\" fType=\"0\" iCount=\"0\" pCount=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"20000\" otp=\"\" posh=\"LEFT_INDEX\" env=\"S\" wadh=\"\" /> <Demo></Demo> <CustOpts> <Param name=\"Param1\" value=\"\" /> </CustOpts> </PidOptions>";
                    Intent intent = new Intent("in.gov.uidai.rdservice.fp.CAPTURE");
                    intent.setPackage("com.scl.rdservice");
                    intent.putExtra("PID_OPTIONS", pidOpt);
                    startActivityForResult(intent, 26);
                }
            } else if (requestCode == 26) {
                Bundle b = data.getExtras();
                if (b != null) {
                    String pidData = b.getString("PID_DATA"); // in this variable you will get Pid data String dnc = b.getString("DNC", ""); // you will get value in this variable when your finger print device not connected
                    String dnr = b.getString("DNR", ""); // you will get value in this variable when your finger print device not registered.
                    Log.d("pidDatapidData", pidData + " ");
                    Log.d("dnrdnrdnrdnr", dnr + " ");
                }
            }
        }
    }

    @Override
    public void onError(
            String error
    ) {
        if (bottomSheet != null) {
            showError(bottomSheet, error);
        } else {
            showErrorR(error);
        }
    }

    @Override
    public void onSuccess(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            mDatabase.setUserData(jsonObject.getString("userdata"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String message, String data_other) {
        try {
            if (data_other.equals("bbps")) {
                data = message;
            } else if (data_other.equals("allServices")) {
                allServices = message;
            } else {
                mDatabase.setUserData(message);
                refresh_layout.setRefreshing(false);
                JSONObject message1 = new JSONObject(message);
                JSONObject data = message1.getJSONObject("main_hero_banner");
                JSONArray slideArray = message1.getJSONArray("slides");
                JSONArray youtube_slides = message1.getJSONArray("youtube_slides");

                String dashboard_banner=message1.getString("dashboard_banner");
                Glide.with(getActivity()).load(dashboard_banner).into(ott_recharge);
                String footer_banner_text=message1.getString("footer_banner_text");
                String recharge_pay_bill_string=message1.getString("recharge_pay_bill_string");
                recharge_and_txt.setText(recharge_pay_bill_string);

                String part1 = "";
                String part2 = "";

                String[] parts = footer_banner_text.split("&");
                if (parts.length == 2) {
                    part1 = parts[0].trim() + " &";
                    part2 = parts[1].trim();
                }

                txtMobileRecharge.setText(part1);
                txtRefer.setText(part2);

                slider_banner.clear();
                if (slideArray.length() > 0) {

                    for (int i = 0; i < slideArray.length(); i++) {
                        JSONObject object1 = slideArray.getJSONObject(i);
                        SliderData sliderData = new SliderData();
                        sliderData.setSlide(object1.getString("slide"));
                        sliderData.setUrl(object1.getString("url"));
                        sliderData.setRedirection_type(object1.getString("redirection_type"));
                        sliderData.setData(object1.getString("data"));
                        slider_banner.add(sliderData);
                    }
                    imageSliderAdapter.notifyDataSetChanged();


                }
                youtubeVideosList.clear();
                if (youtube_slides.length() > 0) {

                    for (int i = 0; i < youtube_slides.length(); i++) {
                        JSONObject objectYoutube_slides = youtube_slides.getJSONObject(i);
                        YoutubeSlides youtubeSlides = new YoutubeSlides();
                        youtubeSlides.setLink(objectYoutube_slides.getString("link"));
                        youtubeSlides.setTitle(objectYoutube_slides.getString("title"));
                        youtubeSlides.setThumbnail(objectYoutube_slides.getString("thumbnail"));
                        youtubeVideosList.add(youtubeSlides);
                    }
                    sliderAdapter.notifyDataSetChanged();


                }
                mainHeroBanner(data);
                setData("Api");
                JSONObject object = new JSONObject(message);
                JSONArray array = object.getJSONArray("recharge_pay_data");
                adapter = new DashboardAdapter(this, this);
                menus.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    DashboardMenu menu = new DashboardMenu();
                    String logo = object1.getString("logo");
                    String title = object1.getString("title");
                    String redirect_link = object1.getString("redirect_link");
                    String service_type = object1.getString("service_type");
                    String highlight_text = object1.getString("highlight_text");
                    String up_down_msg = object1.getString("up_down_msg");
                    menu.setLogo(logo);
                    menu.setTitle(title);
                    menu.setRedirect_link(redirect_link);
                    menu.setService_type(service_type);
                    menu.setHighlight_text(highlight_text);
                    menu.setUp_down_msg(up_down_msg);
                    menus.add(menu);
                }

                menus.add(new DashboardMenu("Bharat Bill Pay", "View All", "BBPS", "bbps", "", ""));
                adapter.addData(menus);
                recyclerviewDashboard.setAdapter(adapter);
                Log.d("TAG_DATA", "onSuccess: " + mDatabase.getUserData().getIs_mpin_set());
                if (mDatabase.getUserData() != null) {
                    if (mDatabase.getUserData().getIs_mpin_set().equals("false")) {
                        if (dialog1 == null) {
                            setMPinDialog();
                        }
                    }
                }

//                JSONObject sliderObject = data.getJSONObject("slides");
//                Log.d("onSuccess", "onSuccess: " + sliderObject);
//                slider_banner.clear();
//                if (sliderObject.length() > 0) {
//
//                    for (int i = 0; i < sliderObject.length(); i++) {
//                        JSONObject object1 = sliderObject.getJSONObject(String.valueOf(i));
//                        SliderData sliderData = new SliderData();
//                        sliderData.setSlide(object1.getString("slide"));
//                        sliderData.setUrl(object1.getString("url"));
//                        sliderData.setRedirection_type(object1.getString("redirection_type"));
//                        sliderData.setData(object1.getString("data"));
//                        slider_banner.add(sliderData);
//                    }
//                    imageSliderAdapter.notifyDataSetChanged();
//
//
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void mainHeroBanner(JSONObject data) throws JSONException {

        String title = data.getString("title");
        String logo = data.getString("logo");
        String data1 = data.getString("data");
        redirection_type = data.getString("redirection_type");
        redirection_url = data.getString("url");
        Glide.with(getActivity()).load(OFFER_ZONE + logo).into(headerImage);

        if (data1 != null) {
            JSONObject activityData = new JSONObject(data1);
            extraData = activityData.getString("extra_data");
            intentName = activityData.getString("intent_name");

            Log.d("activityExtraData", "Data Title: " + extraData);
            Log.d("intent_Name", "Data Intent: " + intentName);
        }
    }

    @Override
    public void onSuccessOther(
            @NonNull String data
    ) {
        if (data.equals("EXPIRED")) {
            sessionExpired("Session Expired. PLease login again");
        } else {
            showMessageDialogActivityNotCancel(data);
        }
    }


    @Override
    public void onSuccessOther(
            String data,
            String data_other
    ) {
        try {
            Log.d("message", data);
            if (data_other.equals("fetchBalance")) {
                JSONObject jsonObject = new JSONObject(data);
                available_balance.setText("\u20b9" + jsonObject.getString("earnings"));
                mDatabase.setEarnings(jsonObject.getString("earnings"));
                if (!jsonObject.getString("news").isEmpty()) {
                    news.setText("                                                            " + jsonObject.getString("news") + "                                           "
                            + "   " + "                                           "
                            + jsonObject.getString("news") + "                                                "
                            + "   " + "                                           "
                            + jsonObject.getString("news"));
                    news.setVisibility(View.VISIBLE);
                    news.setSelected(true);
                    news.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    news.setSingleLine(true);
                } else {
                    news.setVisibility(View.GONE);
                }
            } else if (data_other.equals("UPI Payment")) {
                dataPayment = data;
                Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", data);
                intent.putExtra("title", "UPI Payment");
                startActivity(intent);
            } else if (data_other.equals("PhonePe Payment")) {
                Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", data);
                intent.putExtra("title", "PhonePe Payment");
                startActivity(intent);
            } else if (data_other.equals("Razorpay Payment")) {
                Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", data);
                intent.putExtra("title", "Razorpay Payment");
                startActivity(intent);
            }
//            else if (data_other.equals("CashFree Payment")) {
//                Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
//                intent.putExtra("data", data);
//                intent.putExtra("title", "CashFree Payment");
//                startActivity(intent);
//            }
            else if (data_other.equals("Paytm Payment")) {
                Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", data);
                intent.putExtra("title", "Paytm Payment");
                startActivity(intent);
            } else if (data_other.equals("MpinSucess")) {
                if (data.equals("M Pin updated successfully")) {
                    Toast.makeText(this, data + "", Toast.LENGTH_SHORT).show();
                } else {
                    onError(data);
                    setMPinDialog();
                }
            } else if (data_other.equals("timepass")) {
                dataPayment = data;
                json = new JSONObject(data);
            } else {
                whatsAppDialog(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onShowDialog(
            String message
    ) {
        if (bottomSheet != null) {
            showLoading(loading_dialog);
            submit.setVisibility(View.GONE);
        } else {
            showLoading(loading);
            setStatusBarGradiant(this);
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
    public void onShowToast(
            String message
    ) {
        if (bottomSheet != null) {
            showToast(bottomSheet, message);
        } else {
            showToast(message);
            setStatusBarGradiant(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyBoard(hide_keyboard);
        setLayout(no_internet, retry, "main");
        preferences = getSharedPreferences("debug", Context.MODE_PRIVATE);
        setStatusBarGradiant(this);
/*
        if (!Objects.equals(preferences.getString("state", ""), "cancel")){
            if (isDeveloperModeEnabled()) {
                openDeveloperDialog();
            }
        }
        sliderAdapter = new RecyclerViewSliderAdapter(this, MainActivity.this, youtubeVideosList);
        image_slider.setAdapter(sliderAdapter);

        youtube_indicator.setViewPager2(image_slider);

        imageSliderAdapter = new SliderAdapterBanner(this,slider_banner, this);
        viewPager.setAdapter(imageSliderAdapter);
        indicator.setViewPager2(viewPager);

*/
        youtube_indicator.post(() -> {
            youtube_indicator.requestLayout(); // force refresh
        });
        indicator.post(() -> {
            indicator.requestLayout(); // force refresh
        });
        sliderHandler.postDelayed(sliderRunnable, 1000);
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        hideKeyBoard(hide_keyboard);
        backAlert();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideAllDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideAllDialog();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!recreatedOnce) {
            String currentLang = Locale.getDefault().getLanguage(); // returns "te" for Telugu

            if (currentLang.equals("te") && !prepaidTxt.getText().toString().equals("ప్రీపెయిడ్")) {
                recreatedOnce = true;
                recreate();
            } else if (currentLang.equals("en") && !prepaidTxt.getText().toString().equals("Prepaid")) {
                recreatedOnce = true;
                recreate();
            }
            else if (currentLang.equals("ben") && !prepaidTxt.getText().toString().equals("প্রিপেইড")) {
                recreatedOnce = true;
                recreate();
            } else if (currentLang.equals("hin") && !prepaidTxt.getText().toString().equals("प्रीपेड")) {
                recreatedOnce = true;
                recreate();
            }
        }
        youtube_indicator.post(() -> {
            youtube_indicator.requestLayout(); // force refresh
        });
        indicator.post(() -> {
            indicator.requestLayout(); // force refresh
        });
    }

    //    TODO Service Click
    @Override
    public void onClickListener(String redirection_type, String intent_name, String activityExtraData, String link) throws ClassNotFoundException {
        Intent intent;
        Class<?> clazz = null;
        if (redirection_type.equals("activity")) {
            if (intent_name != null) {
                try {
                    clazz = Class.forName(intent_name);
                    intent = new Intent(getActivity(), clazz);

                    JSONObject object = new JSONObject(activityExtraData);

                    if (object.length() > 0) {

                        Iterator<String> keys = object.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String value = object.optString(key, "");

                            intent.putExtra(key, value);
                        }

                    }
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        } else if (redirection_type.equals("in_app")) {
            if (redirection_type != null && !redirection_type.isEmpty()) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();

                String chromePackage = "com.android.chrome";
                PackageManager pm = getActivity().getPackageManager();
                try {
                    pm.getPackageInfo(chromePackage, 0);
                    customTabsIntent.intent.setPackage(chromePackage);
                } catch (PackageManager.NameNotFoundException e) {

                }

                customTabsIntent.launchUrl(getActivity(), Uri.parse(link));
            } else {

            }

        } else if (redirection_type.equals("external")) {
            if (redirection_type != null && !redirection_type.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                try {
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {

                }
            } else {

            }
        }

    }

    //    TODO Normal Click
    @Override
    public void onClick(
            @NonNull View v
    ) {
        Intent intent;
        String jsonString;
        switch (v.getId()) {
            case R.id.members_bg:
                intent = new Intent(getActivity(), MembersActivity.class);
                startActivity(intent);
                break;
            case R.id.add_member:
                intent = new Intent(getActivity(), DownlinePackageActivity.class);
                intent.putExtra("data", "Select Package");
                startActivity(intent);
                break;
//            case R.id.commission_report_layout:
//                intent = new Intent(getActivity(), CommissionNewActivity.class);
//                startActivity(intent);
//                break;
            case R.id.downline_pckage:
                intent = new Intent(getActivity(), DownlinePackageActivity.class);
                intent.putExtra("data", "Downline Packages");
                startActivity(intent);
                break;
            case R.id.statements:
                intent = new Intent(getActivity(), CommissionChartActivity.class);
                startActivity(intent);
                break;
//            case R.id.history:
//                intent = new Intent(getActivity(), StatementsActivity.class);
//                startActivity(intent);
//                break;
            case R.id.history:
                intent = new Intent(getActivity(), StatementsActivity.class);
                intent.putExtra("statementsActivity", "StatementsActivity");
                startActivity(intent);
                break;
//            case R.id.all_services:
//                intent = new Intent(getActivity(), AllServicesActivity.class);
//                intent.putExtra("data", allServices);
//                startActivity(intent);
//                break;
            case R.id.add_balance:
                addBalance();
                break;
            case R.id.open_account:
                intent = new Intent(getActivity(), SupportActivity.class);
                startActivity(intent);
                break;
            case R.id.prepaid:
                intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("title", "Prepaid Recharge");
                intent.putExtra("type", "Prepaid");
                startActivity(intent);

                break;
            case R.id.postpaid:

                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Postpaid Recharge");
                intent.putExtra("type", "Postpaid");
                startActivity(intent);
                break;
            case R.id.DTH:
                intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra("title", "DTH Recharge");
                intent.putExtra("type", "DTH");
                startActivity(intent);

                break;
            case R.id.Landline:
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Landline");
                intent.putExtra("type", "Landline");
                startActivity(intent);
                break;
            case R.id.Electricity:

                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Electricity Bill");
                intent.putExtra("type", "Electricity");
                startActivity(intent);
                break;
            case R.id.Fastag:

                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Fastag");
                intent.putExtra("type", "Fastag");
                startActivity(intent);
                break;
            case R.id.Cylinder:
                intent = new Intent(getActivity(), ElectricityActivity.class);
                intent.putExtra("title", "Cylinder Bill");
                intent.putExtra("type", "Cylinder");
                startActivity(intent);
                break;
            case R.id.ViewAll:
                intent = new Intent(getActivity(), BharatBillPayActivity.class);
                intent.putExtra("title", "Bharat Bill Pay");
                intent.putExtra("data", data);
                startActivity(intent);
                break;
            case R.id.img:
                intent = new Intent(getActivity(), AccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.share, R.id.share_earn_layout:
                intent = new Intent(getActivity(), ShareEarnActivity.class);
                startActivity(intent);
                break;
            case R.id.support_layout:
                intent = new Intent(getActivity(), SupportActivity.class);
                startActivity(intent);
                break;

            case R.id.commission_report_layout:
                intent = new Intent(getActivity(), CommissionChartActivity.class);
                startActivity(intent);
                break;
            case R.id.refresh:
                intent = new Intent(this, MaxPointsActivity.class);
                startActivity(intent);
//                refreshDataWithoutLoader();
                break;
//            case R.id.whatsup_alert:
//                try{
//                    if (mDatabase.getUserData() != null){
//                        if (mDatabase.getUserData().getWhatsapp_number() != null){
//                            String url = "https://api.whatsapp.com/send/?phone=91" + mDatabase.getUserData().getWhatsapp_number() + "&text=" + "Hello MaxPe Support, My registered mobile number is " + mDatabase.getUserData().getMobile();
//                            Intent i = new Intent(Intent.ACTION_VIEW);
//                            i.setData(Uri.parse(url));
//                            startActivity(i);
//                        }
//                    }
//                }
//                catch (ActivityNotFoundException e){
//                    Log.d("TAG_DATA", "onClick: "+e.getMessage());
//                }
//            break;

            case R.id.whatsup_alert:
                intent = new Intent(getActivity(), WalletActivity.class);
                startActivity(intent);
                break;
            case R.id.imgReferAndEarn:
                intent = new Intent(getActivity(), ShareEarnActivity.class);
                startActivity(intent);
                break;

        }
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

    private void refreshData() {
        rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation);
        rotation.setRepeatCount(100);
//        refresh.startAnimation(rotation);
        new Handler().postDelayed(() -> mDefaultPresenter.dashboardDataWithoutRefresh(fcmToken + "", device_id + "", null), 500);
        new Handler().postDelayed(() -> {
            mDefaultPresenter.getPaymentSetting2(device_id + "", "timepass");
            if (fcmToken == null) {
                fcmToken = "";
            }
            mDefaultPresenter.getBbpsPayBills(device_id + "");
            mDefaultPresenter.getAllRechargeServices(device_id + "");
            mDefaultPresenter.fetchBalance(fcmToken + "", device_id + "", rotation);
            setStatusBarGradiant(this);
        }, 1200);

        youtube_indicator.post(() -> {
            youtube_indicator.requestLayout(); // force refresh
        });
        indicator.post(() -> {
            indicator.requestLayout(); // force refresh
        });

    }

    private void refreshDataWithoutLoader() {
        rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation);
        rotation.setRepeatCount(100);
        refresh.startAnimation(rotation);
        mDefaultPresenter.getPaymentSetting2(device_id + "", "timepass");
        new Handler().postDelayed(() -> {
            if (fcmToken == null) {
                fcmToken = "";
            }
            mDefaultPresenter.getBbpsPayBills(device_id + "");
            mDefaultPresenter.getAllRechargeServices(device_id + "");
            mDefaultPresenter.fetchBalance(fcmToken + "", device_id + "", rotation);
        }, 1200);
    }

    //    TODO Payment Listener
    @Override
    public void onClick(
            @NonNull String name
    ) {
        Intent intent;
        switch (name) {
            case "Paytm":
                intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", dataPayment);
                intent.putExtra("title", "Paytm Payment");
                startActivity(intent);
                dialog.dismiss();
                dialog.cancel();
                break;
            case "Razorpay":
                intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", dataPayment);
                intent.putExtra("title", "Razorpay Payment");
                startActivity(intent);
                dialog.dismiss();
                dialog.cancel();
                break;
            case "UPI":
                intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", dataPayment);
                intent.putExtra("title", "UPI Payment");
                startActivity(intent);
                dialog.dismiss();
                dialog.cancel();
                break;
            case "PhonePe":
                intent = new Intent(getActivity(), AddBalanceActivity.class);
                intent.putExtra("data", dataPayment);
                intent.putExtra("title", "PhonePe Payment");
                startActivity(intent);
                dialog.dismiss();
                dialog.cancel();
                break;
            case "Mobile":
                intent = new Intent(getActivity(), MobileBankingActivity.class);
                startActivity(intent);
                dialog.dismiss();
                dialog.cancel();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ALL_PERMISSION) {
            for (String result : permissions) {
                if (ActivityCompat.checkSelfPermission(this, result) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }
    }

    private void setMPinDialog() {
        SetMpinAlertDailogLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.set_mpin_alert_dailog_layout, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(binding.getRoot());
        dialog1 = builder.create();
        dialog1.setCancelable(false);
        binding.submit.setOnClickListener(v -> {
            if (binding.edNewMpin.getText().toString().isEmpty()) {
                onError("Enter new M-Pin");
                return;
            }
            if (binding.edConfirmPin.getText().toString().isEmpty()) {
                onError("Enter Confirm M-Pin");
                return;
            }

            if (!binding.edNewMpin.getText().toString().equals(binding.edConfirmPin.getText().toString())) {
                onError("M-pin is not match");
                return;
            }

            hideKeyBoard(binding.edNewMpin);
            hideKeyBoard(binding.edConfirmPin);
            dialog1.cancel();
            dialog1.dismiss();
            mDefaultPresenter.setMpin(device_id, binding.edNewMpin.getText().toString().trim());
        });
        dialog1.show();
    }

}