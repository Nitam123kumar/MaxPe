package com.vuvrecharge.modules.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.databinding.MaixPointsMoreSetailsLayoutBinding;
import com.vuvrecharge.modules.adapter.MaxPointsAdapter;
import com.vuvrecharge.modules.model.CashBackPintsModel;
import com.vuvrecharge.modules.model.MaxPePointsData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.presenter.OnFragmentListener;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.utils.LocaleHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaxPointsActivity extends BaseActivity implements DefaultView {

    DefaultPresenter defaultPresenter;
    @BindView(R.id.back_home)
    ImageView back_to_home;

    @BindView(R.id.txtNoData)
    TextView txtNoData;
    @BindView(R.id.balance_maxPoints_TV)
    TextView balance_maxPoints_TV;
    @BindView(R.id.share1)
    ImageView share1;
    @BindView(R.id.maxPointRecyclerView)
    RecyclerView maxPointRecyclerView;
    @BindView(R.id.layoutFrom)
    ConstraintLayout select_from_date_img;
    @BindView(R.id.layoutEnd)
    ConstraintLayout select_to_date_img;
    @BindView(R.id.select_from_date)
    TextView select_from_date;
    @BindView(R.id.select_to_date)
    TextView select_to_date;
    @BindView(R.id.search)
    TextView search;
    @BindView(R.id.date_picker_layout)
    ConstraintLayout date_picker_layout;
    @BindView(R.id.filter_View)
    View filter_View;
    @BindView(R.id.ref_no)
    EditText ref_no;
    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refresh_layout;
    @BindView(R.id.view_all_maxPoints)
    TextView view_all_maxPoints;
    @BindView(R.id.maxPointsTV)
    TextView maxPointsTV;


    CashBackPintsModel data;
    private List<MaxPePointsData> mMaxPePointsData;
    MaxPointsAdapter maxPointsAdapter;

    static Date fromDate;
    static Date toDate;
    static String from_date = "";
    static String to_date = "";
    int page = 1;
    int remaining_pages = 0;
    int previousTotal = 0;
    int pastVisibleItems;
    int visibleItemCount;
    int totalItemCount;
    boolean isLoading = true;
    DialogFragment newFragment;
    OnFragmentListener listener = null;
    String usable_percentage="";
    Boolean isFrom = false;
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_points);
        ButterKnife.bind(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        defaultPresenter = new DefaultPresenter(this);
        initializeEventsList();
        back_to_home.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

//        Calendar cal = Calendar.getInstance();
//        toDate = cal.getTime();
//        Date today = new Date();
//        Calendar cal1 = new GregorianCalendar();
//        cal1.setTime(today);
//        cal1.add(Calendar.DAY_OF_MONTH, -0);
//        fromDate = cal1.getTime();
//        from_date = BaseMethod.format1.format(fromDate);
//        to_date = BaseMethod.format1.format(toDate);
        select_from_date.setText("DD MM YYYY");
        select_to_date.setText("DD MM YYYY");
        loadData("Yes");
        onclickListener();

        date_picker_layout.setVisibility(GONE);
        filter_View.setOnClickListener(v -> {
            if (isFrom == false) {
                date_picker_layout.setVisibility(GONE);
                isFrom = true;
            } else {
                date_picker_layout.setVisibility(VISIBLE);
                isFrom = false;
            }
        });
        refresh_layout.setOnRefreshListener(this::refreshData);
        refresh_layout.setRefreshing(true);

        if (mMaxPePointsData.isEmpty() || mMaxPePointsData.size() <= 6){
            view_all_maxPoints.setVisibility(GONE);
        }
        else {
            view_all_maxPoints.setVisibility(VISIBLE);
        }
        share1.setOnClickListener(v -> {
            pointsMoreDetails(usable_percentage+"%");
        });
        view_all_maxPoints.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StatementsActivity.class);
            intent.putExtra("maxPointsActivity", "maxPointsActivity");
            startActivity(intent);
        });

    }
    void refreshData() {
        defaultPresenter.cashbackPointsHistory(device_id + "",
                "",  "", "");
        select_from_date.setText("DD MM YYYY");
        select_to_date.setText("DD MM YYYY");
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onSuccess(String data) {

    }

    private void onclickListener() {
        search.setOnClickListener(v -> {

            page = 1;
            remaining_pages = 0;
            pastVisibleItems = 0;
            visibleItemCount = 0;
            totalItemCount = 0;
            previousTotal = 0;

            String ref_no_ = ref_no.getText().toString().trim();
            defaultPresenter.cashbackPointsHistory(device_id + "",
                    from_date + "", to_date + "", ref_no_ + "");
        });

        select_from_date_img.setOnClickListener(v -> {
            newFragment = new SelectDate(select_from_date, select_to_date);
            newFragment.show(getSupportFragmentManager(), "Select From Date");
        });
        select_to_date_img.setOnClickListener(v -> {
            newFragment = new SelectDate(select_from_date, select_to_date);
            newFragment.show(getSupportFragmentManager(), "Select To Date");
        });

    }

    private void initializeEventsList() {
        mMaxPePointsData = new ArrayList<>();
        maxPointsAdapter = new MaxPointsAdapter(this, mMaxPePointsData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        maxPointRecyclerView.setLayoutManager(linearLayoutManager);
        maxPointRecyclerView.setAdapter(maxPointsAdapter);
        maxPointRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        defaultPresenter.cashbackPointsHistory(device_id + "",
                /*from_date +*/ "", /*to_date +*/ "", "");
    }

    private void pointsMoreDetails(String discount) {
        try {
            MaixPointsMoreSetailsLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.maix_points_more_setails_layout, null, false);
          BottomSheetDialog  dialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
            dialog.setContentView(binding.getRoot());
            changeStatusBarColor(dialog);

            bottomSheet = dialog.findViewById(com.denzcoskun.imageslider.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }
            binding.txtSecond.setText(discount);
            binding.closeTextView.setOnClickListener(v -> dialog.dismiss());
            binding.detailsCancel.setOnClickListener(v -> dialog.dismiss());
            binding.needSupport.setOnClickListener(v ->
            {
                Intent intent = new Intent(getActivity(), SupportActivity.class);
                startActivity(intent);
            });
            dialog.show();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            refresh_layout.setRefreshing(false);
            JSONObject object = new JSONObject(data);
//            mDatabase.putString("max_points_history",data);
            JSONArray array = object.getJSONArray("cashbackLogs");
            String lifeTimeMaxPoints = object.getString("lifetimePointsEarn");
            maxPointsTV.setText(lifeTimeMaxPoints);
            Log.d("data",data);

            if (array.length() > 0) {
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
                    Log.d("dataTitle","MaixPoints"+object1);
                    maxPePointData.setOpening_points(object1.getString("opening_points"));
                    mMaxPePointsData.add(maxPePointData);
                    maxPointsAdapter.notifyDataSetChanged();
                    txtNoData.setVisibility(GONE);

                }
                if (mMaxPePointsData.isEmpty()|| mMaxPePointsData.size() <= 6){
                    view_all_maxPoints.setVisibility(GONE);
                }
                else {
                    view_all_maxPoints.setVisibility(VISIBLE);
                }
                maxPointRecyclerView.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            } else {
                maxPointRecyclerView.setVisibility(GONE);
                txtNoData.setVisibility(VISIBLE);
                view_all_maxPoints.setVisibility(GONE);
            }
            txtNoData.setVisibility(GONE);


            if (object.has("cashbackPoints")) {
                JSONObject pointsObject = object.getJSONObject("cashbackPoints");

                CashBackPintsModel model = new CashBackPintsModel();
                model.setMax_balance_slab(pointsObject.getString("max_balance_slab"));
                model.setCashback_points(pointsObject.getString("cashback_points"));
                model.setTxn_balance_count(pointsObject.getString("txn_balance_count"));
                balance_maxPoints_TV.setText(model.getCashback_points());


            }

            if (array.length() > 0) {
                Gson gson = new Gson();
                Type type_ = new TypeToken<List<MaxPePointsData>>() {
                }.getType();
                List<MaxPePointsData> passbookData = gson.fromJson(array.toString(), type_);
                if (data_other.equals("Yes")) {
                    initializeEventsList();
                }
                maxPointsAdapter.addEvents(passbookData, data_other);
                maxPointRecyclerView.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            } else {
//                onError("no Data found");
                txtNoData.setVisibility(VISIBLE);
                maxPointRecyclerView.setVisibility(GONE);
                view_all_maxPoints.setVisibility(GONE);
            }

            if (object.has("usable_percentage")){
                usable_percentage = object.getString("usable_percentage");
            }


        } catch (Exception e) {

            txtNoData.setVisibility(VISIBLE);
            view_all_maxPoints.setVisibility(GONE);
            maxPointRecyclerView.setVisibility(GONE);
        }
    }
    private void apiStoreData(JSONObject object){
        try {
            refresh_layout.setRefreshing(false);
//            mDatabase.putString("max_points_history");
            JSONArray array = object.getJSONArray("cashbackLogs");
            String lifeTimeMaxPoints = object.getString("lifetimePointsEarn");
            maxPointsTV.setText(lifeTimeMaxPoints);

            if (array.length() > 0) {
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
                    maxPointsAdapter.notifyDataSetChanged();
                    txtNoData.setVisibility(GONE);

                }
                if (mMaxPePointsData.isEmpty()|| mMaxPePointsData.size() <= 6){
                    view_all_maxPoints.setVisibility(GONE);
                }
                else {
                    view_all_maxPoints.setVisibility(VISIBLE);
                }
                maxPointRecyclerView.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            } else {
                maxPointRecyclerView.setVisibility(GONE);
                txtNoData.setVisibility(VISIBLE);
                view_all_maxPoints.setVisibility(GONE);
            }
            txtNoData.setVisibility(GONE);


            if (object.has("cashbackPoints")) {
                JSONObject pointsObject = object.getJSONObject("cashbackPoints");

                CashBackPintsModel model = new CashBackPintsModel();
                model.setMax_balance_slab(pointsObject.getString("max_balance_slab"));
                model.setCashback_points(pointsObject.getString("cashback_points"));
                model.setTxn_balance_count(pointsObject.getString("txn_balance_count"));
                balance_maxPoints_TV.setText(model.getCashback_points());


            }

//            if (array.length() > 0) {
//                Gson gson = new Gson();
//                Type type_ = new TypeToken<List<MaxPePointsData>>() {
//                }.getType();
//                List<MaxPePointsData> passbookData = gson.fromJson(array.toString(), type_);
//                if (data_other.equals("Yes")) {
//                    initializeEventsList();
//                }
//                maxPointsAdapter.addEvents(passbookData, data_other);
//                maxPointRecyclerView.setVisibility(VISIBLE);
//                txtNoData.setVisibility(GONE);
//            } else {
//                onError("no Data found");
//                txtNoData.setVisibility(VISIBLE);
//                maxPointRecyclerView.setVisibility(GONE);
//                view_all_maxPoints.setVisibility(GONE);
//            }


        } catch (Exception e) {

            txtNoData.setVisibility(VISIBLE);
            view_all_maxPoints.setVisibility(GONE);
            maxPointRecyclerView.setVisibility(GONE);
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

    }

    @Override
    public void onPrintLog(String message) {

    }

    public static class SelectDate extends DialogFragment {

        TextView from_;
        TextView to_;

        public SelectDate(TextView from_, TextView to_) {
            this.from_ = from_;
            this.to_ = to_;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance(); // Defaults to today's date

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(requireActivity(), R.style.DialogTheme,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
                        Date selectedDate = selectedCal.getTime();

                        String tag = getTag();
                        if ("Select From Date".equals(tag)) {
                            fromDate = selectedDate;
                            from_date = BaseMethod.format1.format(selectedDate);
                            String displayDate = BaseMethod.dateFormat.format(selectedDate);
                            from_.setText(displayDate);

                            // Adjust To Date if before From Date
                            if (toDate != null && toDate.before(fromDate)) {
                                toDate = fromDate;
                                to_date = BaseMethod.format1.format(toDate);
                                to_.setText(displayDate);
                            }
                        } else if ("Select To Date".equals(tag)) {
                            toDate = selectedDate;
                            to_date = BaseMethod.format1.format(selectedDate);
                            String displayDate = BaseMethod.dateFormat.format(selectedDate);
                            to_.setText(displayDate);
                        }
                    }, year, month, day);

            // Limit minimum selectable date for "To Date"
            if ("Select To Date".equals(getTag()) && fromDate != null) {
                dialog.getDatePicker().setMinDate(fromDate.getTime());
            }

            // Limit maximum date to today
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

            return dialog;
        }
    }
}