package com.vuvrecharge.modules.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.custom.ClassesTabLayout;
import com.vuvrecharge.modules.fragments.PlanSearchFragment;
import com.vuvrecharge.modules.fragments.SpecialOffers;
import com.vuvrecharge.modules.model.AllPlanItemData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanActivity extends BaseActivity implements DefaultView,View.OnClickListener {

  @BindView(R.id.toolbar_layout)
  LinearLayout mToolbar;
  @BindView(R.id.title)
  public TextView title;
  @BindView(R.id.tab_layout)
  ClassesTabLayout mClassesTabLayout;
  @BindView(R.id.pager)
  ViewPager mViewPager;
  DefaultPresenter mDefaultPresenter;
  @BindView(R.id.loading)
  LinearLayout loading;
  @BindView(R.id.no_internet)
  LinearLayout no_internet;
  @BindView(R.id.retry)
  TextView retry;
  @BindView(R.id.txtPhoneNumber)
  TextView txtPhoneNumber;
  @BindView(R.id.txtPhoneProvider)
  TextView txtPhoneProvider;
  @BindView(R.id.txtPhoneOrigin)
  TextView txtPhoneOrigin;
  @BindView(R.id.txtRecharge)
  TextView txtRecharge;
  @BindView(R.id.txtSearch)
  TextView txtSearch;
  @BindView(R.id.txtCancel)
  TextView txtCancel;
  @BindView(R.id.editSearch)
  EditText editSearch;
  @BindView(R.id.frameLayout)
  FrameLayout frameLayout;
  @BindView(R.id.imgOperator)
  ImageView imgOperator;
  @BindView(R.id.imgClose)
  ImageView imgClose;
  DefaultView mDefaultView;
  String selected_operator = "";
  String selected_circle = "";
  String selected_operator_str = "";
  String selected_circle_str = "";
  String operator_img = "";

  String type = "",titles="",mobile_number_str="";

  Intent intent;
  int pageIndex = 0;
  List<AllPlanItemData> planDataList;
  JSONArray jsonPlanArray;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_plan);
    ButterKnife.bind(getActivity());
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    mToolbar.setOnClickListener(this);
    setStatusBarGradiant(this);
    mDefaultView = this;
    title.setText("Plans Details");
    intent = getIntent();
    selected_operator = intent.getStringExtra("selected_operator");
    selected_circle = intent.getStringExtra("selected_circle");
    selected_operator_str = intent.getStringExtra("selected_operator_str");
    selected_circle_str = intent.getStringExtra("selected_circle_str");
    operator_img = intent.getStringExtra("operator_img");
    mobile_number_str = intent.getStringExtra("mobile_number_str");
    txtPhoneNumber.setText(mobile_number_str);
    txtPhoneProvider.setText(selected_operator_str);
    Glide.with(this)
            .load(operator_img)
            .into(imgOperator);
    txtPhoneOrigin.setText(selected_circle_str);
    type = getIntent().getStringExtra("type");
    titles = intent.getStringExtra("title");
    title.setText(titles);
    mDefaultPresenter = new DefaultPresenter(this);
    jsonPlanArray = new JSONArray();
    if (type.equals("plan")) {
      mDefaultPresenter.fetchPrepaidPlans(device_id + "", selected_operator + "", selected_circle + "", mobile_number_str);
    } else {
      mDefaultPresenter.fetchRofferPlans(device_id + "", selected_operator + "", selected_circle + "", mobile_number_str);
    }

    txtRecharge.setOnClickListener(v -> {
      intent = new Intent(getActivity(), RechargeActivity.class);
      intent.putExtra("title", "Prepaid Recharge");
      startActivity(intent);
    });

    mClassesTabLayout.setVisibility(View.VISIBLE);
    mViewPager.setVisibility(View.VISIBLE);
    editSearch.setVisibility(View.GONE);
    txtCancel.setVisibility(View.GONE);
    frameLayout.setVisibility(View.GONE);
    imgClose.setVisibility(View.GONE);
    txtSearch.setOnClickListener(v -> {
      if (mClassesTabLayout.getVisibility() == View.VISIBLE){
        mClassesTabLayout.setVisibility(View.GONE);
        editSearch.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        showSoftKeyboard(editSearch);
        editSearch.setFocusable(true);
        editSearch.requestFocus();

        Fragment fragment = PlanSearchFragment.getInstance(mDefaultView, editSearch, imgClose);
        Bundle bundle = new Bundle();
        bundle.putString("listData",jsonPlanArray.toString());
        bundle.putString("title",titles);
        bundle.putString("phone",mobile_number_str);
        bundle.putString("url",operator_img);
        bundle.putString("provider",selected_operator_str);
        bundle.putString("state",selected_circle_str);
        bundle.putString("selected_circle",selected_circle);
        bundle.putString("selected_operator",selected_operator);
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                        .replace(frameLayout.getId(),fragment)
                        .commit();
        txtCancel.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
        txtSearch.setVisibility(View.GONE);
      }else {
        Log.d("TAG_DATA", "onCreate: jsonPlanArray: "+jsonPlanArray.toString());
      }
    });

    txtCancel.setOnClickListener(v -> {
      if (mClassesTabLayout.getVisibility() == View.GONE){
        mViewPager.setVisibility(View.VISIBLE);
        txtSearch.setVisibility(View.VISIBLE);
        mClassesTabLayout.setVisibility(View.VISIBLE);
        editSearch.setVisibility(View.GONE);
        txtCancel.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        imgClose.setVisibility(View.GONE);
        hideKeyBoard(editSearch);
      }
    });

    imgClose.setOnClickListener(v -> {
      if (!editSearch.getText().toString().trim().isEmpty()){
          editSearch.setText("");
        mViewPager.setVisibility(View.VISIBLE);
        txtSearch.setVisibility(View.VISIBLE);
        mClassesTabLayout.setVisibility(View.VISIBLE);
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    setLayout(no_internet, retry, "plan");
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
    showError(error);
  }

  @Override
  public void onSuccess(String message) {
    onBackPressed();
  }

  @Override
  public void onSuccess(String message, String second_message) {
    try {
      JSONObject object = new JSONObject(message);
//      Log.d("TAG_DATA", "onSuccess: "+message);
      JSONObject jsonObject1 = object.getJSONObject("records");
      Iterator<String> keys = jsonObject1.keys();
      JSONArray jsonArray = new JSONArray();
      while (keys.hasNext()) {
        String key = keys.next();
        JSONArray array = jsonObject1.getJSONArray(key);
        for (int i=0; i < array.length(); i++){
//          Log.d("TAG_DATA", "onSuccess: "+array.getJSONObject(i));
          jsonPlanArray.put(array.getJSONObject(i));
        }

        if (jsonObject1.get(key) instanceof JSONArray) {
          JSONObject jsonObject2 = new JSONObject();
          jsonObject2.put("name", key);
          jsonObject2.put("list", jsonObject1.get(key));
          jsonArray.put(jsonObject2);
        }
      }
      Gson gson = new Gson();
      Type type_ = new TypeToken<List<AllPlanItemData>>() {
      }.getType();
      planDataList = gson.fromJson(jsonArray.toString(), type_);

      CustomTabAdapter mTabAdapter = new CustomTabAdapter(getSupportFragmentManager(), planDataList);
      mViewPager.setOffscreenPageLimit(0);
      mClassesTabLayout.setupWithViewPager(mViewPager);
      mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mClassesTabLayout));
      mClassesTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
          mViewPager.setCurrentItem(tab.getPosition());
//          mClassesTabLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_selected_bg));

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
          mClassesTabLayout.setBackground(null);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
      });
      mViewPager.setAdapter(mTabAdapter);
      mViewPager.setCurrentItem(pageIndex);
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
    showLoading(loading);
  }

  @Override
  public void onHideDialog() {
    hideLoading(loading);
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
    switch(v.getId()){
      case R.id.toolbar_layout:
        onBackPressed();
        getActivity().finish();
        break;
    }
  }

  public class CustomTabAdapter extends FragmentStatePagerAdapter {
    int count = 0;
    List<AllPlanItemData> planDataList;

    public CustomTabAdapter(FragmentManager fm, @NonNull List<AllPlanItemData> planDataList) {
      super(fm);
      this.count = planDataList.size();
      this.planDataList = planDataList;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
      return new SpecialOffers(mDefaultView, device_id, planDataList.get(position).getList(), titles,
              mobile_number_str,operator_img,selected_operator_str,selected_circle_str,selected_operator,selected_circle);
    }

    @Override
    public int getCount() {
      return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return planDataList.get(position).getName();
    }
  }

}
