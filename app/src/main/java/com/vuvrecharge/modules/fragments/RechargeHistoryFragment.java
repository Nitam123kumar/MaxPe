package com.vuvrecharge.modules.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseFragment;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.activities.ComplainsActivity;
import com.vuvrecharge.modules.adapter.ReportsAdapter;
import com.vuvrecharge.modules.model.ReportsData;
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

public class RechargeHistoryFragment extends BaseFragment implements DefaultView {

    ViewGroup rootViewMain;
    private DefaultPresenter mDefaultPresenter;
    DefaultView mDefaultView;
    @BindView(R.id.rvRechargeDetails)
    RecyclerView rvRechargeDetails;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.select_to_date)
    TextView select_to_date;
    @BindView(R.id.select_from_date)
    TextView select_from_date;
    @BindView(R.id.txtNoData)
    TextView txtNoData;
    @BindView(R.id.layoutFrom)
    ConstraintLayout select_from_date_img;
    @BindView(R.id.layoutEnd)
    ConstraintLayout select_to_date_img;
    @BindView(R.id.ref_no)
    EditText ref_no;
    @BindView(R.id.search)
    TextView search;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refresh_layout;
//    TextView invoice;
    String device_id = "";

    static Date fromDate;
    static Date toDate;
    static String from_date = "";
    static String to_date = "";
    ReportsAdapter adapter;
    int page = 1;
    int remaining_pages = 0;
    int previousTotal = 0;
    int pastVisibleItems;
    int visibleItemCount;
    int totalItemCount;
    boolean isLoading = true;
    DialogFragment newFragment;
    OnFragmentListener listener = null;

    @SuppressLint("HardwareIds")
    @Override
    public View provideYourFragmentView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recharge_history, parent, false);
        this.rootViewMain = parent;
        ButterKnife.bind(this, rootView);
//        invoice = requireActivity().findViewById(R.id.invoice);
//        invoice.setVisibility(VISIBLE);
//        invoice.setText("View Complains");
        mDefaultPresenter = new DefaultPresenter(this);
        Calendar cal = Calendar.getInstance();
        toDate = cal.getTime();
        Date today = new Date();
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(today);
        cal1.add(Calendar.DAY_OF_MONTH, -0);
        fromDate = cal1.getTime();
        device_id = Settings.Secure.getString(requireActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        from_date = BaseMethod.format1.format(fromDate);
        to_date = BaseMethod.format1.format(toDate);
        select_from_date.setText("DD MM YYYY");
        select_to_date.setText("DD MM YYYY");
        initializeEventsList();

        onclickListener();

        refresh_layout.setOnRefreshListener(this::refreshData);
        refresh_layout.setColorSchemeResources(R.color.colorPrimaryB);
        refresh_layout.setRefreshing(true);
        refreshData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData("Yes");
    }


    void refreshData() {
        page = 1;
        remaining_pages = 0;
        previousTotal = 0;
        pastVisibleItems = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        isLoading = true;

        adapter.clearData(); // Clear previous data

        // Fetch fresh data
        mDefaultPresenter.userRechargeHistory(
                device_id + "",
                page + "",
                "", "", "", "Yes", "Yes"
        );
        select_from_date.setText("DD MM YYYY");
        select_to_date.setText("DD MM YYYY");
    }
    private void onclickListener() {
//        invoice.setOnClickListener(v -> {
//            try {
//                Intent intent = new Intent(getActivity(), ComplainsActivity.class);
//                startActivity(intent);
//            }catch (Exception e){
//                Log.d("TAG_DATA", "onclickListener: "+e.getMessage());
//            }
//        });

        search.setOnClickListener(v -> {
            page = 1;
            remaining_pages = 0;
            pastVisibleItems = 0;
            visibleItemCount = 0;
            totalItemCount = 0;
            previousTotal = 0;
            String ref_no_ = ref_no.getText().toString().trim();
            mDefaultPresenter.userRechargeHistory(device_id + "", page + "",
                    select_from_date.getText().toString().trim() + "",
                    select_to_date.getText().toString().trim() + "", ref_no_ + "", "Yes","Yes");
        });

        select_from_date_img.setOnClickListener(v -> {
            newFragment = new SelectDate(select_from_date, select_to_date);
            newFragment.show(getChildFragmentManager(), "Select From Date");
        });
        select_to_date_img.setOnClickListener(v -> {
            newFragment = new SelectDate(select_from_date, select_to_date);
            newFragment.show(getChildFragmentManager(), "Select To Date");
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener){
            listener = (OnFragmentListener) context;
        }else {

        }
    }

    private void initializeEventsList() {
        adapter = new ReportsAdapter(getLayoutInflater());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvRechargeDetails.setLayoutManager(linearLayoutManager);
        rvRechargeDetails.setAdapter(adapter);
        rvRechargeDetails.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    @Override
    public void onError(String error) {
        try {
            listener.onError(error);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    public void onSuccess(String data, String data_other) {
        try {

            refresh_layout.setRefreshing(false);
            JSONObject jsonObject = new JSONObject(data);
            printLog(jsonObject.toString());
            remaining_pages = Integer.parseInt(jsonObject.getString("remaining_pages"));
            JSONArray jsonArray = jsonObject.getJSONArray("history_data");
            if (jsonArray.length() > 0){
                Gson gson = new Gson();
                Type type_ = new TypeToken<List<ReportsData>>() {
                }.getType();
                List<ReportsData> passbookData = gson.fromJson(jsonArray.toString(), type_);
                if (data_other.equals("Yes")) {
                }
                adapter.addData(passbookData, data_other, jsonObject.getString("operator_img"),
                        jsonObject.getString("operator_dunmy_img"), mDefaultView, mDatabase);
                rvRechargeDetails.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            }else {
//                onError("no Data found");
                txtNoData.setVisibility(VISIBLE);
                rvRechargeDetails.setVisibility(GONE);
            }
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
        listener.onShowToast(message);
//        showToast(rootViewMain, message);
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
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