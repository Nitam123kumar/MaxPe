package com.vuvrecharge.modules.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vuvrecharge.api.ApiServices.IMAGE_LOGO;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.databinding.CustomerInfoBinding;
import com.vuvrecharge.databinding.RechargeDialogBinding;
import com.vuvrecharge.databinding.ScreenRechargePaymentLayoutBinding;
import com.vuvrecharge.databinding.TransactionDialogBinding;
import com.vuvrecharge.databinding.WalletTransactionBottonDialogBinding;
import com.vuvrecharge.modules.adapter.BillerInfoAdapter;
import com.vuvrecharge.modules.adapter.OpreatorAdapter;
import com.vuvrecharge.modules.adapter.RecentRechargesAdapter;
import com.vuvrecharge.modules.adapter.ReportsAdapter;
import com.vuvrecharge.modules.adapter.SearchableSpinnerCircleAdapter;
import com.vuvrecharge.modules.model.BillInfo;
import com.vuvrecharge.modules.model.CircleData;
import com.vuvrecharge.modules.model.OperatorData;
import com.vuvrecharge.modules.model.PaymentModel;
import com.vuvrecharge.modules.model.ReportsData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.OperatorPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class RechargeActivity extends BaseActivity implements DefaultView,
        SearchableSpinnerCircleAdapter.OnItemClickListeners,View.OnClickListener{

    DefaultView mDefaultView;
    private DefaultPresenter mDefaultPresenter;
    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.txtCircleOperator)
    TextView txtCircleOperator;
    @BindView(R.id.wallet_amount)
    TextView wallet_amount;
    @BindView(R.id.view_cus_info)
    TextView view_cus_info;
    @BindView(R.id.view_plan)
    TextView view_plan;
//    @BindView(R.id.select_circle_bg)
//    LinearLayout select_circle_bg;
    @BindView(R.id.mobile_number)
    TextInputEditText mobile_number;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
//    @BindView(R.id.mobile_number_layout)
//    TextInputLayout mobile_number_layout;
    @BindView(R.id.recyclerViewBillerInfo)
    RecyclerView recyclerViewBillerInfo;
    @BindView(R.id.amount)
    TextInputEditText amount;
    @BindView(R.id.select_operator)
    Spinner select_operator;
    @BindView(R.id.recent_recharges_recyclerView)
    RecyclerView recent_recharges_recyclerView;
    String string = "";
    ArrayList<String> operator_list = new ArrayList<>();
    ArrayList<String> circle_list = new ArrayList<>();
    String type = "";
    ArrayList<String> operator_list_img = new ArrayList<>();
    ArrayList<ReportsData> recent_recharges_List = new ArrayList<>();
    RecentRechargesAdapter recentRechargesAdapter;
    String operator_img = "";
    @BindView(R.id.search_number)
    ImageView search_number;

    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.btnRoffer)
    TextView btnRoffer;
    @BindView(R.id.txtOperator)
    TextView txtOperator;
//    @BindView(R.id.imgBBPS)
//    ImageView imgBBPS;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.btnBillFetch)
    TextView btnBillFetch;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    String image = "";
    String operator_dummy = "", mobile_number_str = "";
    OperatorPreferences operatorPreferences;
    OperatorPreferences operatorPreferences2;
    HashMap<String, String> map,map2;
    String selected_operator = "Select Operator";
    String selected_operator_str = "";
    String selected_circle = "Select Circle";
    String selected_circle_str = "";
    String selected_operator_img = "";
    AlertDialog dialog1;
    String warning_message = "";
    String findOperator = "0";
    List<BillInfo> infos = new ArrayList<>();
    String billAmount = "";
    BillerInfoAdapter adapter;

    int page = 1;
    int remaining_pages = 0;
    int previousTotal = 0;
    int pastVisibleItems;
    int visibleItemCount;
    int totalItemCount;
    boolean isLoading = true;
    List<OperatorData> operatorDataList = new ArrayList<>();
    List<CircleData> circleDataList = new ArrayList<>();
    public BottomSheetDialog dialog = null;
    public FrameLayout bottomSheet = null;
    String m_id = "",order_id = "",mPin = "";
    ReportsData mReportsData;
    Handler handler;
    Runnable runnable;
    BottomSheetDialog dialog2 = null;
    ArrayList<PaymentModel> list = new ArrayList<>();
    double releaseAmount = 0.000;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyBoard(mobile_number);
        hideKeyBoard(amount);
        return super.onTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initializeEventsList();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        string = getIntent().getStringExtra("title");
        title.setText(string);
        handler = new Handler();
        operator_list.add("Select Operator");
        circle_list.add("Select Circle");
        BaseMethod.amount = "";
        BaseMethod.mobile = "";
        mToolbar.setOnClickListener(this);
        mDefaultPresenter = new DefaultPresenter(this);
        setStatusBarGradiant(this);

        scrollView.setOnTouchListener((v, event) -> {
            hideKeyBoard(mobile_number);
            hideKeyBoard(amount);
            return false;
        });
        wallet_amount.setText("Your balance : \u20b9" + mDatabase.getEarnings());
        switch (string) {
            case "Prepaid Recharge":
                type = "Prepaid";
//                select_circle_bg.setVisibility(VISIBLE);
                txtOperator.setText("Select Circle");
                view_plan.setVisibility(VISIBLE);
                view_cus_info.setVisibility(GONE);
                search_number.setVisibility(VISIBLE);
                btnRoffer.setVisibility(VISIBLE);
//                imgBBPS.setVisibility(GONE);
//                btnRoffer.setTextColor(Color.BLACK);
//                view_plan.setTextColor(Color.BLACK);
//                view_plan.setBackgroundResource(R.drawable.btn_drawable_disable);
                mobile_number.setHint("Mobile Number");
                txtTitle.setText("Mobile Number");
                operatorPreferences = new OperatorPreferences(this,type);
                operatorPreferences2 = new OperatorPreferences(this,type+"2");
                map = operatorPreferences.getData();
                map2 = operatorPreferences2.getData();
                if (map.get("type") != null){
                    operatorDataList.clear();
                    circleDataList.clear();
                    Gson gson = new Gson();
                    Type type_ = new TypeToken<List<OperatorData>>() {}.getType();
                    List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
                    this.operatorDataList = operatorDataList;
                    operator_list = new ArrayList<>();
                    operator_list_img = new ArrayList<>();
                    circle_list = new ArrayList<>();
                    operator_list.add("Select Operator");
                    circle_list.add("Select Circle");
                    operator_list_img.add("Select Image");
                    findOperator = "1";
                    for (OperatorData operatorData : operatorDataList) {
                        image = operatorData.getLogo();
                        if (image != null && !image.isEmpty() && !image.equals("null")) {
                            operator_list_img.add(IMAGE_LOGO+"/" + operatorData.getLogo());
                            operator_list.add(operatorData.getName());
                        } else {
                            operator_list_img.add(IMAGE_LOGO +"/" + operator_dummy);
                            operator_list.add(operatorData.getName());
                        }
                    }
                    Gson gson2 = new Gson();
                    Type type_2 = new TypeToken<List<CircleData>>() {
                    }.getType();
                    List<CircleData> circleDataList = gson2.fromJson(map2.get("list"), type_2);
                    this.circleDataList = circleDataList;
                    loadOperatorSpinner();
                }else {
                    mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
                }



                view_plan.setOnClickListener(v -> {
                    mobile_number_str = mobile_number.getText().toString();
                    if (mobile_number_str.trim().isEmpty()) {
                        showError("Please enter mobile");
                        mobile_number.setText("");
                        return;
                    }
                    if (mobile_number_str.trim().length() < 10) {
                        showError("please enter 10 digit mobile number");
                        return;
                    }
                    if (selected_operator.equals("Select Operator")) {
                        showError("Please select operator");
                        return;
                    }
                    if (selected_circle.equals("Select Circle")) {
                        showError("Please select circle");
                        return;
                    }
                    BaseMethod.amount = Objects.requireNonNull(amount.getText()).toString();
                    BaseMethod.mobile = mobile_number.getText().toString();
                    Intent intent = new Intent(getActivity(), PlanActivity.class);
                    intent.putExtra("selected_operator", selected_operator);
                    intent.putExtra("selected_circle", selected_circle);
                    intent.putExtra("mobile_number_str", mobile_number_str);
                    intent.putExtra("selected_operator_str", selected_operator_str);
                    intent.putExtra("selected_circle_str", selected_circle_str);
                    intent.putExtra("operator_img", selected_operator_img);
                    intent.putExtra("title", "Plans Details");
                    intent.putExtra("type", "plan");
                    intent.putExtra("type2", type);
                    startActivity(intent);
                });

                btnRoffer.setOnClickListener(v -> {
                    mobile_number_str = Objects.requireNonNull(mobile_number.getText()).toString();
                    if (mobile_number_str.trim().isEmpty()) {
                        showError("Please enter mobile");
                        mobile_number.setText("");
                        return;
                    }
                    if (mobile_number_str.trim().length() < 10) {
                        showError("please enter 10 digit mobile number");
                        return;
                    }
                    if (selected_operator.equals("Select Operator")) {
                        showError("Please select operator");
                        return;
                    }
                    if (selected_circle.equals("Select Circle")) {
                        showError("Please select circle");
                        return;
                    }

                    BaseMethod.amount = amount.getText().toString();
                    BaseMethod.mobile = mobile_number.getText().toString();
                    Intent intent = new Intent(getActivity(), PlanActivity.class);
                    intent.putExtra("selected_operator", selected_operator);
                    intent.putExtra("selected_circle", selected_circle);
                    intent.putExtra("mobile_number_str", mobile_number_str);
                    intent.putExtra("selected_operator_str", selected_operator_str);
                    intent.putExtra("selected_circle_str", selected_circle_str);
                    intent.putExtra("operator_img", selected_operator_img);
                    intent.putExtra("title", "R Offer");
                    intent.putExtra("type", "rplan");
                    startActivity(intent);
                });

                InputFilter[] filters = new InputFilter[1];
                filters[0] = new InputFilter.LengthFilter(10);
                mobile_number.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().startsWith("+91")){
                            String str = s.toString().replace("+91","");
                            mobile_number.setText(str);
                            mobile_number.setFilters(filters);
                            if (findOperator.equals("1")) {
                                if (s.toString().length() > 9) {
                                    mDefaultPresenter.findOperatorCircle(device_id, s.toString());
                                }else {
                                    select_operator.setSelection(0);
                                    amount.setText("");
                                    txtOperator.setText("Select Circle");
                                }
                            }
                        }else {
                            if (findOperator.equals("1")) {
                                if (s.toString().length() > 9) {
                                    mobile_number.setFilters(filters);
                                    mDefaultPresenter.findOperatorCircle(device_id, s.toString());
//                                    btnRoffer.setTextColor(getResources().getColor(R.color.colorPrimaryB));
//                                btnRoffer.setBackgroundResource(R.drawable.btn_drawable);
                                    view_cus_info.setBackgroundResource(R.drawable.btn_drawable);
                                    view_cus_info.setTextColor(Color.WHITE);

//                                    view_plan.setTextColor(Color.WHITE);
//                                    view_plan.setBackgroundResource(R.drawable.btn_drawable);
                                }else {
                                    select_operator.setSelection(0);
                                    amount.setText("");
                                    txtOperator.setText("Select Circle");
                                    InputFilter[] filters2 = new InputFilter[1];
                                    filters2[0] = new InputFilter.LengthFilter(13);
                                    mobile_number.setFilters(filters2);
//                                    btnRoffer.setTextColor(Color.BLACK);
//                                btnRoffer.setBackgroundResource(R.drawable.btn_drawable_disable);

                                    view_cus_info.setBackgroundResource(R.drawable.btn_drawable_disable);
                                    view_cus_info.setTextColor(Color.BLACK);

//                                    view_plan.setTextColor(Color.BLACK);
//                                    view_plan.setBackgroundResource(R.drawable.btn_drawable_disable);
                                    if (s.toString().startsWith("+91")){
                                        String str = s.toString().replace("+91","");
                                        mobile_number.setText(str);
                                        mobile_number.setFilters(filters);
                                    }
                                }
                            }
                        }
                    }
                });
                btnBillFetch.setVisibility(GONE);
                break;
            case "Postpaid Recharge":
                type = "Postpaid";
//                select_circle_bg.setVisibility(GONE);
                view_plan.setVisibility(GONE);
                view_cus_info.setVisibility(GONE);
//                imgBBPS.setVisibility(VISIBLE);
                search_number.setVisibility(VISIBLE);
                btnRoffer.setVisibility(GONE);
                operatorPreferences = new OperatorPreferences(this,type);
                map = operatorPreferences.getData();
                if (map.get("type") != null){
                    operatorDataList.clear();
                    findOperator = "1";
                    Gson gson = new Gson();
                    Type type_ = new TypeToken<List<OperatorData>>() {
                    }.getType();
                    List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
                    this.operatorDataList = operatorDataList;
                    operator_list = new ArrayList<>();
                    operator_list_img = new ArrayList<>();
                    operator_list.add("Select Operator");
                    operator_list_img.add("Select Image");
                    for (OperatorData operatorData : operatorDataList) {
                        image = operatorData.getLogo();
                        if (image != null && !image.isEmpty() && !image.equals("null")) {
                            operator_list_img.add(IMAGE_LOGO+"/" + operatorData.getLogo());
                            operator_list.add(operatorData.getName());
                        } else {
                            operator_list_img.add(IMAGE_LOGO+"/" + operator_dummy);
                            operator_list.add(operatorData.getName());
                        }
                    }
                }else {
                    mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
                }
                InputFilter[] filters2 = new InputFilter[1];
                filters2[0] = new InputFilter.LengthFilter(10);
                mobile_number.setFilters(filters2);
                mobile_number.setHint("Postpaid Mobile Number");
                txtTitle.setText("Postpaid Mobile Number");
                btnBillFetch.setOnClickListener(v -> billFetch(selected_operator,
                        mobile_number.getText().toString().trim()));
                mobile_number.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (findOperator.equals("1")) {
                            if (s.toString().length() == 10) {
//                                btnRoffer.setTextColor(Color.WHITE);
//                                btnRoffer.setBackgroundResource(R.drawable.btn_drawable);

                                view_cus_info.setBackgroundResource(R.drawable.btn_drawable);
                                view_cus_info.setTextColor(Color.WHITE);

//                                view_plan.setTextColor(Color.WHITE);
//                                view_plan.setBackgroundResource(R.drawable.btn_drawable);
                            }else {
//                                btnRoffer.setBackgroundResource(R.drawable.btn_drawable_disable);
//                                btnRoffer.setTextColor(Color.BLACK);

                                view_cus_info.setBackgroundResource(R.drawable.btn_drawable_disable);
                                view_cus_info.setTextColor(Color.BLACK);

//                                view_plan.setTextColor(Color.BLACK);
//                                view_plan.setBackgroundResource(R.drawable.btn_drawable_disable);
                            }
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
            case "DTH Recharge":
                type = "DTH";
                btnBillFetch.setVisibility(GONE);
                txtOperator.setVisibility(GONE);
                txtCircleOperator.setVisibility(GONE);
                view_plan.setVisibility(VISIBLE);
                view_cus_info.setVisibility(VISIBLE);
//                imgBBPS.setVisibility(GONE);
                view_cus_info.setBackgroundResource(R.drawable.btn_drawable_disable);
                view_cus_info.setTextColor(Color.BLACK);
//                view_plan.setBackgroundResource(R.drawable.btn_drawable_disable);
//                view_plan.setTextColor(Color.BLACK);
                search_number.setVisibility(GONE);
                btnRoffer.setVisibility(GONE);
                mobile_number.setHint("DTH Number");
                txtTitle.setText("DTH Number");
                operatorPreferences = new OperatorPreferences(this,type);
                map = operatorPreferences.getData();
                if (map.get("type") != null){
                    operatorDataList.clear();
                    Gson gson = new Gson();
                    Type type_ = new TypeToken<List<OperatorData>>() {
                    }.getType();
                    findOperator = "1";
                    List<OperatorData> operatorDataList = gson.fromJson(map.get("list"), type_);
                    this.operatorDataList = operatorDataList;
                    operator_list = new ArrayList<>();
                    operator_list_img = new ArrayList<>();
                    operator_list.add("Select Operator");
                    operator_list_img.add("Select Image");
                    for (OperatorData operatorData : operatorDataList) {
                        image = operatorData.getLogo();
                        if (image != null && !image.isEmpty() && !image.equals("null")) {
                            operator_list_img.add(IMAGE_LOGO+"/" + operatorData.getLogo());
                            operator_list.add(operatorData.getName());
                        } else {
                            operator_list_img.add(IMAGE_LOGO+"/" + operator_dummy);
                            operator_list.add(operatorData.getName());
                        }
                    }
                }else {
                    mDefaultPresenter.historyCircleOperators(device_id + "", type + "");
                }
                view_cus_info.setOnClickListener(v -> {
                    String mobile_number_str = mobile_number.getText().toString();
                    if (mobile_number_str.trim().length() == 0) {
                        showError("Please enter  DTH number");
                        mobile_number.setText("");
                        return;
                    }
                    if (selected_operator.equals("Select Operator")) {
                        showError("Please select operator");
                        return;
                    }
                    BaseMethod.amount = amount.getText().toString();
                    BaseMethod.mobile = mobile_number.getText().toString();
                    hideKeyBoard(mobile_number);
                    mDefaultPresenter.dthCustomerInfo(device_id + "", mobile_number_str + "", selected_operator + "");
                });
                view_plan.setOnClickListener(v -> {
                    if (selected_operator.equals("Select Operator")) {
                        showError("Please select operator");
                        return;
                    }
                    BaseMethod.amount = amount.getText().toString();
                    BaseMethod.mobile = mobile_number.getText().toString();
                    Intent intent = new Intent(getActivity(), DthPlanActivity.class);
                    intent.putExtra("selected_operator", selected_operator);
                    intent.putExtra("mobile_number_str", mobile_number.getText().toString());
                    intent.putExtra("selected_operator_str", selected_operator_str);
                    intent.putExtra("selected_circle_str", selected_circle_str);
                    intent.putExtra("operator_img", selected_operator_img);
                    startActivity(intent);
                });
                mobile_number.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (findOperator.equals("1")) {
                            if (s.toString().length() >= 10) {
//                                btnRoffer.setTextColor(Color.WHITE);
//                                btnRoffer.setBackgroundResource(R.drawable.btn_drawable);

                                view_cus_info.setBackgroundResource(R.drawable.btn_drawable);
                                view_cus_info.setTextColor(Color.WHITE);

//                                view_plan.setTextColor(Color.WHITE);
//                                view_plan.setBackgroundResource(R.drawable.btn_drawable);
                            }else {
//                                btnRoffer.setBackgroundResource(R.drawable.btn_drawable_disable);
//                                btnRoffer.setTextColor(Color.BLACK);

                                view_cus_info.setBackgroundResource(R.drawable.btn_drawable_disable);
                                view_cus_info.setTextColor(Color.BLACK);

//                                view_plan.setTextColor(Color.BLACK);
//                                view_plan.setBackgroundResource(R.drawable.btn_drawable_disable);
                            }
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                break;
        }

        search_number.setOnClickListener(v -> {
            if (selectContact()) {
                Intent intent = new Intent(getActivity(), SelectNumberActivity.class);
                startActivity(intent);
            }
        });

//        submit.setTextColor(getResources().getColor(R.color.black_4));
//        submit.setTypeface(submit.getTypeface(), Typeface.BOLD);
//        submit.setBackgroundResource(R.drawable.btn_drawable_disable);
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                    if (!s.toString().isEmpty()){
//                        submit.setTextColor(Color.WHITE);
//                        submit.setBackgroundResource(R.drawable.btn_drawable);
//                        submit.setTypeface(submit.getTypeface(), Typeface.BOLD);
//                    }else {
//                        submit.setTextColor(getResources().getColor(R.color.black_4));
//                        submit.setTypeface(submit.getTypeface(), Typeface.BOLD);
//                        submit.setBackgroundResource(R.drawable.btn_drawable_disable);
//                    }
            }
        });
        loadOperatorSpinner();

    }

    private void billFetch(String operatorId, String phoneNumber) {
        mDefaultPresenter.fetchBillPostpaidInfo(device_id, operatorId, phoneNumber,"");
    }

    void pendingstatus() {
        mDefaultPresenter.orderDetailspending(device_id + "", order_id);
    }

    private void setData(ReportsData mReportsData) {
//        try {
//            if (mReportsData.getStatus().equals("pending")) {
//                handler.postDelayed(runnable, 10000);
//            }
//            if (mReportsData.getStatus().equals("SUCCESS")) {
//                handler.postDelayed(runnable, 10000);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void initializeEventsList() {
        recentRechargesAdapter = new RecentRechargesAdapter(getLayoutInflater(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recent_recharges_recyclerView.setLayoutManager(linearLayoutManager);
        recent_recharges_recyclerView.setAdapter(recentRechargesAdapter);
        recent_recharges_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (dy > 0){
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems)) {
                        isLoading = true;
                        page++;
                        if (remaining_pages > 0) {
                            loadData("No");
                        }
                    }
                }
            }
        });
    }

    private void loadData(String value) {
        mDefaultPresenter.userRechargeHistory(device_id + "", page + "",
                "", "", "", value, value);
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

    private void loadOperatorSpinner() {
        OpreatorAdapter adapter = new OpreatorAdapter(getActivity(), operator_list, operator_list_img);
        select_operator.setAdapter(adapter);
        select_operator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    selected_operator = operator_list.get(i);
                    selected_operator_str = operator_list.get(i);
                } else {
                    selected_operator = operatorDataList.get(i - 1).getId();
                    selected_operator_str = operatorDataList.get(i - 1).getName();
                    selected_operator_img = operator_list_img.get(i);
                }
                adapter.addData(selected_operator);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        select_circle_bg.setOnClickListener(this::openDialog);
    }

    private void openDialog(View vi) {
        View view = getLayoutInflater().inflate(R.layout.searchable_spinner_layout, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog1 = builder.create();
        RecyclerView rv = view.findViewById(R.id.recyclerViewList);
        EditText editText = view.findViewById(R.id.editSearch);
        view.findViewById(R.id.btnClose).setOnClickListener(v -> {
            dialog1.dismiss();
            dialog1.cancel();
        });
        Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        SearchableSpinnerCircleAdapter circleAdapter = new SearchableSpinnerCircleAdapter(this, circleDataList, this);
        rv.setAdapter(circleAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    ArrayList<CircleData> data = new ArrayList<>();
                    for (CircleData circleData: circleDataList){
                        if (circleData.getState_name().toUpperCase().toLowerCase().contains(s.toString().toUpperCase().toLowerCase())){
                            data.add(circleData);
                        }
                    }
                    if (data == null || data.isEmpty()){
                        showToast("Not data found");
                        circleAdapter.searchData(circleDataList);

                    }else {
                        circleAdapter.searchData(data);
                    }
                }
            }
        });
        dialog1.show();
    }

    @Override
    public void onItemClick(@NonNull CircleData model, int position) {
        txtOperator.setText(model.getState_name());
        dialog1.cancel();
        dialog1.dismiss();
        if (!txtOperator.getText().toString().equals("Select Circle")){
            if (position == 0){
                selected_circle_str = model.getState_name();
                selected_circle = model.getId();
            }else {
                selected_circle_str = model.getState_name();
                selected_circle = model.getId();
            }
        }
        else {
            showError("Please Select service provider.");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyBoard(mobile_number);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "recharge");
        try {
            wallet_amount.setText("Your balance : \u20b9" + mDatabase.getEarnings());

            amount.setText(BaseMethod.amount);
            if (BaseMethod.from.equals("Yes")) {
                mobile_number.setText(BaseMethod.mobile);
                new Handler().postDelayed(() -> BaseMethod.from = "No", 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseMethod.amount = amount.getText().toString();
        BaseMethod.mobile = mobile_number.getText().toString();
    }

    public void doRecharge(View view) {
        String mobile_number_str = mobile_number.getText().toString();
        String amount_str = amount.getText().toString();

        if (mobile_number_str.trim().isEmpty()) {
            if (type.equals("DTH")) {
                showError("Please enter  DTH number");
            } else {
                showError("Please enter mobile number");
            }
            mobile_number.setText("");
            return;
        }

        if (selected_operator.equals("Select Operator")) {
            showError("Please select operator");
            return;
        }

        if (type.equals("Prepaid")) {
            if (selected_circle.equals("Select Circle")) {
                showError("Please select circle");
                return;
            }
        } else {
            selected_circle = "1";
        }

        if (amount_str.trim().isEmpty()) {
            showError("Please enter amount");
            amount.setText("");
            return;
        }
        hideKeyBoard(mobile_number);
        rechargeDialog(mobile_number_str + "", selected_operator + "",
                amount_str + "", type + "",
                "0", "0", selected_circle + "",
                "0", selected_operator_str + "");
    }

    public void rechargeDialog(String number, String operator, String amount, String type,
                               String std_code, String dob, String circle, String ac_number,
                               @NonNull String selected_operator_str) {

        try{
            RechargeDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                    R.layout.recharge_dialog, null, false);

            Dialog dialog = new Dialog(getActivity(), R.style.CustomAlertDialog);
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(true);

            binding.operator.setText(selected_operator_str.trim());
            binding.number.setText(number);
            binding.amount.setText("\u20b9" + amount);
            if (warning_message.equals("")) {
                binding.warningMessage.setVisibility(VISIBLE);
                binding.warningMessage.setText("Read Carefully! Wrong number successful recharge will not " +
                        "be refunded. By confirming you are agree with our Terms & Conditions!");
            } else {
                binding.warningMessage.setVisibility(VISIBLE);
                binding.warningMessage.setText(warning_message);
            }

            binding.mpinTxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()) {
                        mPin = s.toString();
                    }
                }
            });

            binding.close.setOnClickListener(v -> dialog.dismiss());
            binding.confirm.setOnClickListener(v -> {
                mPin = binding.mpinTxt.getText().toString();
                if (TextUtils.isEmpty(mPin)) {
                    Toasty.error(getActivity(), "Please enter your M Pin", Toasty.LENGTH_LONG, false).show();
                    return;
                }
                if (mPin.length() != 4) {
                    Toasty.error(getActivity(), "MPin length should be 4 digit", Toasty.LENGTH_LONG, false).show();
                    return;
                }
                mDefaultPresenter.doMobileRecharge(number + "", operator + "",
                        amount + "", type + "",
                        std_code + "", dob + "",
                        circle + "", ac_number + "", device_id + "", mPin + "");
                dialog.dismiss();
            });
            dialog.show();
        }catch (Exception e){
            Log.d("TAG_DATA", "rechargeDialog: "+e.getMessage());
        }
    }

    public void successDialog(String data, String message) {
        try {
            TransactionDialogBinding _binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.transaction_dialog, null, false);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(_binding.getRoot());
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);

            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            String status = "";
            try {
                JSONObject jsonObject = new JSONObject(data);
                status = jsonObject.getString("status");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (status.toUpperCase().equals("FAILED")) {
                _binding.txtTitle.setText("Failed");
//                _binding.txtMessage.setText("Your transaction has failed.\nPlease go back and try again.");
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("Oops!");
                _binding.txtSlug.setVisibility(GONE);
//                _binding.imgGif.setGifImageResource(R.drawable.animated_wrong);
                Glide.with(this).asGif().load(R.drawable.animated_wrong).into(_binding.imgGif);
                _binding.btnComplete.setBackgroundResource(R.drawable.failed_button);
            } else if (status.toUpperCase().equals("PENDING")){
                _binding.btnComplete.setBackgroundResource(R.drawable.pending_button);
                _binding.txtTitle.setText("Pending");
//                _binding.txtMessage.setText("Your transaction is under process.\nPlease check your history after a few min.");
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("CHILL!");
//                _binding.imgGif.setGifImageResource(R.drawable.animated_pending);
                Glide.with(this).asGif().load(R.drawable.animated_pending).into(_binding.imgGif);
            }else {
                _binding.btnComplete.setBackgroundResource(R.drawable.green_button);
                _binding.txtTitle.setText("Success");
//                _binding.txtMessage.setText("Your transaction has been\ncompleted successfully.");
                _binding.txtMessage.setText(message);
                _binding.txtSlug.setText("Thank You!");
//                _binding.imgGif.setGifImageResource(R.drawable.animated_right);
                Glide.with(this).asGif().load(R.drawable.animated_right).into(_binding.imgGif);
            }
            String finalStatus = status;
            _binding.btnComplete.setOnClickListener(v -> {
                if (finalStatus.toUpperCase().equals("FAILED")) {
                    dialog.dismiss();
                } else {
                    try {
                        dialog.dismiss();
                        JSONObject jsonObject = new JSONObject(data);
                        Intent intent = new Intent(getActivity(), RechargeReportActivity.class);
                        intent.putExtra("operator_img", jsonObject.getString("operator_img"));
                        intent.putExtra("operator_dunmy_img", jsonObject.getString("operator_dunmy_img"));
                        if (jsonObject.getString("status").toLowerCase().equals("success")){
                            intent.putExtra("bps", "1");
                        }else {
                            intent.putExtra("bps", "0");
                        }
                        intent.putExtra("mReportsData", data);
                        intent.putExtra("type",type);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            mDatabase.setUserData(jsonObject.getString("userdata"));
            wallet_amount.setText("Your balance : \u20b9" + mDatabase.getUserData().getEarnings());
            amount.setText(BaseMethod.amount);
            mobile_number.setText(BaseMethod.mobile);
//            String mobile_number_str = mobile_number.getText().toString();
//            String amount_str = amount.getText().toString();
////            mDefaultPresenter.doMobileRecharge(mobile_number_str + "", selected_operator + "",
////                    amount_str + "", type + "",
////                    0 + "", 0 + "", selected_circle + "", 0 + "", device_id + "","");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String message, String second_message) {
        infos.clear();
        try {
            if (second_message.equals("razorPayMethod")){
                JSONObject jsonObject = new JSONObject(message);
                String orderid = jsonObject.getString("orderid");
                this.order_id = orderid;
                String uri = jsonObject.getString("upi_uri");
                Intent upiIntent = new Intent(Intent.ACTION_VIEW);
                String uriString = uri;
                upiIntent.setData(Uri.parse(uriString.trim()));
                Intent chooser = Intent.createChooser(upiIntent, "Pay with...");
                startActivityForResult(chooser, 104, null);
            }else if (second_message.equals("recharge")){
                JSONObject jsonObject = new JSONObject(message);
                String orderid = jsonObject.getString("orderid");
                this.order_id = orderid;
                String uri = jsonObject.getString("upi_uri");
                Intent upiIntent = new Intent(Intent.ACTION_VIEW);
                String uriString = uri;
                upiIntent.setData(Uri.parse(uriString.trim()));
                Intent chooser = Intent.createChooser(upiIntent, "Pay with...");
                startActivityForResult(chooser, 108, null);
            }
            else if (second_message.equals("orderDetails")){
                Gson gson = new Gson();
                Type type_ = new TypeToken<ReportsData>() {
                }.getType();
                JSONObject object = new JSONObject(message);
                String payment_status = object.getString("payment_status");
                mReportsData = gson.fromJson(message, type_);
//                setData(mReportsData);
                mReportsData.setStatus(payment_status);
//                if (mReportsData.getStatus().equals("pending")) {
//                    handler.postDelayed(runnable, 10000);
//                }
//                runnable = () -> {
//                    if (mReportsData.getStatus().equals("pending")) {
//                        pendingstatus();
//                    }
//                };

                if (payment_status.toLowerCase().equals("pending")){
                    openPendingDialog();
                }else if (payment_status.toLowerCase().equals("success")){
                    mDefaultPresenter.doMobileRecharge(
                            mobile_number.getText().toString().trim(),selected_operator,amount.getText().toString().trim(),type,
                            "0", "0",
                            selected_circle,"0",device_id, mPin);
                }else if (payment_status.toLowerCase().equals("failed")){
                    Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
                }else {
                    openPendingDialog();
                }
            }
            else if (second_message.equals("orderDetailsPending")){
                openPendingDialog();
            }
            else if (second_message.equals("postpaid")){
                JSONObject object = new JSONObject(message);
                BillInfo info = new BillInfo();

                billAmount = object.getString("billAmount");

                info.setBillAmount(object.getString("billAmount"));

                info.setBillDate(object.getString("billDate"));

                info.setBillNumber(object.getString("billNumber"));

                info.setRequestId(object.getString("requestId"));

                info.setCustomerName(object.getString("customerName"));

                info.setDueDate(object.getString("dueDate"));

                info.setDealerName(object.getString("dealerName"));

                info.setFetchType(object.getString("fetchType"));

                infos.add(info);
                adapter = new BillerInfoAdapter(infos, string, type);
                recyclerViewBillerInfo.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                btnBillFetch.setVisibility(GONE);
                recyclerViewBillerInfo.setVisibility(VISIBLE);
            }
            else {
                JSONObject jsonObject = new JSONObject(message);
                Gson gson = new Gson();
                Type type_ = new TypeToken<List<OperatorData>>() {
                }.getType();
                List<OperatorData> operatorDataList = gson.fromJson(jsonObject.getString("operators"), type_);
                operatorPreferences.setOperator(type,jsonObject.getString("operators"));
                Type type_1 = new TypeToken<List<CircleData>>() {
                }.getType();
                List<CircleData> circleDataList = gson.fromJson(jsonObject.getString("circles"), type_1);
                if (type.equals("Prepaid")){
                    operatorPreferences2.setOperator(type+"2",jsonObject.getString("circles"));
                }
                warning_message = jsonObject.getString("message");
                findOperator = jsonObject.getString("findOperator");
                operator_img = jsonObject.getString("operator_img");
                this.operatorDataList = operatorDataList;
                this.circleDataList = circleDataList;

                operator_list = new ArrayList<>();
                operator_list_img = new ArrayList<>();
                circle_list = new ArrayList<>();
                operator_list.add("Select Operator");
                circle_list.add("Select Circle");
                operator_list_img.add("Select Image");
                for (OperatorData operatorData : operatorDataList) {
                    image = operatorData.getLogo();
                    if (image != null && !image.isEmpty() && !image.equals("null")) {
                        image = operatorData.getLogo();
                        if (image != null && !image.isEmpty() && !image.equals("null")) {
                            operator_list_img.add(operator_img + operatorData.getLogo());
                        } else {
                            operator_list_img.add(operator_img + operator_dummy);
                        }
                        operator_list.add(operatorData.getName());
                    } else {
                        operator_list_img.add(operator_img + operator_dummy);
                        operator_list.add(operatorData.getName());
                    }
                }
                for (CircleData circleData : circleDataList) {
                    circle_list.add(circleData.getState_name());
                }
                loadOperatorSpinner();
            }

            JSONObject jsonObject = new JSONObject(message);
            printLog(jsonObject.toString());
            remaining_pages = Integer.parseInt(jsonObject.getString("remaining_pages"));
            JSONArray jsonArray = jsonObject.getJSONArray("history_data");
            if (jsonArray.length() > 0){
                Gson gson = new Gson();
                Type type_ = new TypeToken<List<ReportsData>>() {
                }.getType();
                List<ReportsData> passbookData = gson.fromJson(jsonArray.toString(), type_);
                if (second_message.equals("Yes")) {
                }
                recentRechargesAdapter.addData(passbookData, second_message, jsonObject.getString("operator_img"),
                        jsonObject.getString("operator_dunmy_img"), mDefaultView, mDatabase);
                recent_recharges_recyclerView.setVisibility(VISIBLE);
            }else {
                recent_recharges_recyclerView.setVisibility(GONE);
            }

        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    Timer timer;
    private void openPendingDialog() {
        try {
            timer = new Timer();
            WalletTransactionBottonDialogBinding _binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.wallet_transaction_botton_dialog, null, false);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            bottomSheetDialog.setContentView(_binding.getRoot());
            bottomSheetDialog.setCancelable(false);
            changeStatusBarColor(bottomSheetDialog);
            bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }

            _binding.btnComplete.setBackgroundResource(R.drawable.pending_button);
            _binding.txtTitle.setText("Waiting for payment");
            _binding.txtMessage.setText("Your transaction is under processing.\nPlease do not press the back button.");
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            mDefaultPresenter.orderDetails(device_id, order_id);
                        }
                    },10000
            );

            _binding.btnComplete.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                bottomSheetDialog.cancel();
                timer.cancel();
                showPending("You can recharge after sometime.");
            });

            bottomSheetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessOther(String data) {
        customerInfo(data);
    }

    @Override
    public void onSuccessOther(String data, @NonNull String data_other) {
        try {
            if (data_other.equals("INSUFFICIENT")) {
                openGatewaysDialog(data);
            } else if (data_other.equals("operator_circle")) {
                hideKeyBoard(mobile_number);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String operator = jsonObject.getString("operator");
                    String circle = jsonObject.getString("circle");
                    int op_count = 0;
                    int cr_count = 0;
                    if (!operator.equals("0")) {
                        for (OperatorData operatorData : operatorDataList) {
                            op_count++;
                            if (operatorData.getId().equals(operator)) {
                                break;
                            }
                        }
                    }
                    if (!circle.equals("0")) {
                        for (CircleData circleData : circleDataList) {
                            cr_count++;
                            if (circleData.getId().equals(circle)) {
                                txtOperator.setText(circleData.getState_name());
                                selected_circle = circleData.getId();
                                selected_circle_str = circleData.getState_name();
                                break;
                            }
                        }
                    }
                    select_operator.setSelection(op_count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                successDialog(data, data_other);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
    }

    public void customerInfo(String data) {
        try {
            CustomerInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.customer_info, null, false);
            Dialog dialog = new Dialog(this);
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(true);

            try {
                JSONObject jsonObject = new JSONObject(data);
                binding.customerName.setText(jsonObject.getString("customerName"));
                binding.monthlyRecharge.setText(jsonObject.getString("monthlyRecharge"));
                binding.balance.setText("\u20b9" + jsonObject.getString("balance"));
                binding.status.setText(jsonObject.getString("status"));
                binding.nextDate.setText(jsonObject.getString("nextRechargeDate"));
                binding.lastDate.setText(jsonObject.getString("lastRechargeDate").replaceAll("T", " "));
                binding.lastAmount.setText("\u20b9" + jsonObject.getString("lastRechargeAmount"));
                binding.planName.setText(jsonObject.getString("planname").trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
            binding.close.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGatewaysDialog(String data) {
        try {
            list.clear();
            ScreenRechargePaymentLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.screen_recharge_payment_layout, null, false);

            dialog2 = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
            dialog2.setContentView(binding.getRoot());
            dialog2.setCancelable(true);
            changeStatusBarColor(dialog2);
            bottomSheet = dialog2.findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }
            JSONObject json = new JSONObject(data);
            binding.txtTransactionAmountValue.setText("\u20b9"+amount.getText().toString().trim());
            if (mDatabase != null){
                if (mDatabase.getEarnings() != null){
                    binding.txtWalletAmountValue.setText("₹"+mDatabase.getEarnings());
                }
            }
            binding.txtTotalDiscountValue.setText("₹"+json.getString("discount"));
            releaseAmount = (Double.parseDouble(amount.getText().toString().trim()) - json.getDouble("discount"));
            binding.txtPayableAmtValue.setText("₹"+releaseAmount);

            binding.btnPay.setText("Add on ₹"+json.getInt("required"));

//            if(binding.checkWallet.isChecked()){
//                binding.txtMessage.setVisibility(VISIBLE);
//                amt = String.valueOf(Math.round(Double.parseDouble(amount.getText().toString().trim())- releaseAmount));
//                binding.txtPayableAmountValue.setText("\u20b9"+amt);
//                binding.btnPay.setText("Pay \u20b9"+amt);
//            }
//            binding.checkWallet.setOnClickListener(v -> {
//                if (binding.checkWallet.isChecked()){
//                    amt = String.valueOf(Math.round(Double.parseDouble(amount.getText().toString().trim())- releaseAmount));
//                    binding.txtPayableAmountValue.setText("\u20b9"+amt);
//                    binding.txtMessage.setVisibility(VISIBLE);
//                    binding.btnPay.setText("Pay \u20b9"+amt);
//                }else {
//                    amt = amount.getText().toString().trim();
//                    binding.txtPayableAmountValue.setText("\u20b9"+amount.getText().toString().trim());
//                    binding.txtMessage.setVisibility(GONE);
//                    binding.btnPay.setText("Pay \u20b9"+amount.getText().toString().trim());
//                }
//            });
//            if (list.size() > 2) {
//                binding.rvGateways.setLayoutManager(new GridLayoutManager(this, 3));
//            }
//            if (list.size() > 3) {
//                binding.rvGateways.setLayoutManager(new GridLayoutManager(this, 4));
//            }
//            if (list.size() > 0){
//                list.get(0).setSelected(true);
//            }
//            PaymentAdapter adapter1 = new PaymentAdapter(this, this);
//            adapter1.addData(list);
//            binding.rvGateways.setAdapter(adapter1);
            binding.btnPay.setOnClickListener(v -> {
                try {
                    mDefaultPresenter.addInsufficientBalance(device_id, json.getString("required"));
                    dialog2.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
//                binding.btnPay.setText("Pay \u20b9"+amt);
//                try{
//                    JSONObject jsonObject = new JSONObject(dataPayment);
//                    m_id = jsonObject.getString("merchant_id");
//                    min = jsonObject.getString("gateway_min_amount");
//                    max = jsonObject.getString("gateway_max_amount");
//                    payu_getway = jsonObject.getString("payu_getway");
//                    paumax = jsonObject.getString("payu_max_amount");
//                    paumin = jsonObject.getString("payu_min_amount");
//                    razorpay_min_amount = jsonObject.getString("razorpay_min_amount");
//                    razorpay_max_amount = jsonObject.getString("razorpay_max_amount");
//                    razorpay_merchant_key = jsonObject.getString("razorpay_merchant_key");
//                    hdfcminamount = jsonObject.getString("hdfc_min_amount");
//                    hdfcmaxamount = jsonObject.getString("hdfc_max_amount");
//                    if (Double.parseDouble(amount.getText().toString().trim()) > releaseAmount){
//                        if (!name.isEmpty()){
//                            switch (name) {
//                                case "razorpay":
//                                    dialog2.cancel();
//                                    mDefaultPresenter.generateRazorPayOrder(device_id, amt,"true");
//                                    break;
//                                case "upi":
//                                    dialog2.cancel();
//                                    mDefaultPresenter.hdfcbankDynamicQR(device_id, amt,"true");
//                                    break;
//                                case "phonepe":
//                                    dialog2.cancel();
//                                    mDefaultPresenter.phonepeDynamicQR(device_id, amt,"true");
//                                    break;
//                            }
//                        }else {
//                            showError("select gateway");
//                        }
//                    }
//                }catch (Exception e){
//                    Log.d("TAG_DATA", "onClick: "+e.getMessage());
//                }
            });
            dialog2.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG_DATA", "addBalance: "+e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 108){
            if (resultCode == RESULT_OK){
                mDefaultPresenter.orderDetails(device_id, order_id);
            } else {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == 102){
            if (resultCode == RESULT_OK){
                mDefaultPresenter.orderDetails(device_id, order_id);
            } else {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == 103){
            if (resultCode == RESULT_OK){
                mDefaultPresenter.orderDetails(device_id, order_id);
            } else {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
            }
        }
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