package com.vuvrecharge.modules.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.adapter.MaxPointsAdapter;
import com.vuvrecharge.modules.adapter.WalletAdapter;
import com.vuvrecharge.modules.fragments.RechargeHistoryFragment;
import com.vuvrecharge.modules.fragments.WalletFragment;
import com.vuvrecharge.modules.model.CashBackPintsModel;
import com.vuvrecharge.modules.model.DepositData;
import com.vuvrecharge.modules.model.MaxPePointsData;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.model.WalletData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.presenter.OnFragmentListener;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
    @BindView(R.id.maxPointRecyclerView)
    RecyclerView maxPointRecyclerView;
    @BindView(R.id.select_from_date_img)
    ImageView select_from_date_img;
    @BindView(R.id.select_to_date_img)
    ImageView select_to_date_img;
    @BindView(R.id.select_from_date)
    TextView select_from_date;
    @BindView(R.id.select_to_date)
    TextView select_to_date;
    @BindView(R.id.search)
    TextView search;
    @BindView(R.id.ref_no)
    EditText ref_no;
    @BindView(R.id.loading)
    LinearLayout loading;
    CashBackPintsModel data;
    private List<MaxPePointsData> mMaxPePointsData = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_points);
        ButterKnife.bind(this);
        defaultPresenter = new DefaultPresenter(this);
//        defaultPresenter.cashbackPointsHistory(device_id+"","03 Jul 2025","18 Jul 2025","175281101911308");
        initializeEventsList();
        back_to_home.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        Calendar cal = Calendar.getInstance();
        toDate = cal.getTime();
        Date today = new Date();
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(today);
        cal1.add(Calendar.DAY_OF_MONTH, -0);
        fromDate = cal1.getTime();
        from_date = BaseMethod.format1.format(fromDate);
        to_date = BaseMethod.format1.format(toDate);
        select_from_date.setText("DD MM YYYY");
        select_to_date.setText("DD MM YYYY");
        loadData("Yes");
        onclickListener();

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
        maxPointsAdapter = new MaxPointsAdapter(this,mMaxPePointsData);
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
        defaultPresenter.cashbackPointsHistory(device_id + "",
                /*from_date +*/ "", /*to_date +*/ "", "");
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
                    maxPointsAdapter.notifyDataSetChanged();
                    txtNoData.setVisibility(GONE);

                }

                maxPointRecyclerView.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            }else {
                maxPointRecyclerView.setVisibility(GONE);
                txtNoData.setVisibility(VISIBLE);
            }
            txtNoData.setVisibility(GONE);



                if (object.has("cashbackPoints")) {
                    JSONObject pointsObject = object.getJSONObject("cashbackPoints");

                    CashBackPintsModel model = new CashBackPintsModel();
                    model.setMax_balance_slab(pointsObject.getString("max_balance_slab"));
                    model.setCashback_points(pointsObject.getString("cashback_points"));
                    model.setTxn_balance_count(pointsObject.getString("txn_balance_count"));
                    Log.d("cashbackLogs", "onSuccessOther: "+pointsObject);
                    balance_maxPoints_TV.setText(model.getCashback_points());

                }

            if (array.length() > 0){
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
            }else {
//                onError("no Data found");
                txtNoData.setVisibility(VISIBLE);
                maxPointRecyclerView.setVisibility(GONE);
            }


        }catch (Exception e){

            txtNoData.setVisibility(VISIBLE);
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
            final Calendar calendar = Calendar.getInstance();
            String tag = getTag();
            if (tag != null) {
                switch (tag) {
                    case "Select From Date":
                        calendar.setTime(fromDate);
                        break;
                    case "Select To Date":
                        calendar.setTime(toDate);
                        break;
                }
            }

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(requireContext(), R.style.DialogTheme, (view, year1, month1, day1) -> {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(0);
                cal.set(year1, month1, day1, 0, 0, 0);
                String tag1 = getTag();
                if (tag1 != null) {
                    switch (tag1) {
                        case "Select From Date":
                            fromDate = cal.getTime();
                            from_date = BaseMethod.format1.format(fromDate);
                            String from_date_local = BaseMethod.dateFormat.format(fromDate);
                            if (toDate.before(fromDate)) {
                                toDate = cal.getTime();
                                to_date = BaseMethod.format1.format(toDate);
                                to_.setText(from_date_local);
                            }
                            from_.setText(from_date_local);
                            break;
                        case "Select To Date":
                            toDate = cal.getTime();
                            to_date = BaseMethod.format1.format(toDate);
                            String to_date_local = BaseMethod.dateFormat.format(toDate);
                            to_.setText(to_date_local);
                            break;
                    }
                }
            }, year, month, day);
            if (tag != null) {
                if ("Select To Date".equals(tag)) {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(fromDate);
                    dpd.getDatePicker().setMinDate(calendar1.getTimeInMillis());
                }
            }
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            return dpd;
        }
    }

}