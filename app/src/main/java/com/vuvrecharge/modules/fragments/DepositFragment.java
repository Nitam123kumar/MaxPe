package com.vuvrecharge.modules.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
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

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseFragment;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.adapter.DepositAdapter;
import com.vuvrecharge.modules.model.DepositData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.presenter.OnFragmentListener;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DepositFragment extends BaseFragment implements DefaultView {
    public Context mContext;
    private DefaultPresenter mDefaultPresenter;
    ViewGroup rootViewMain;
    @BindView(R.id.list_item)
    RecyclerView list_item;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.txtNoData)
    TextView txtNoData;

    @BindView(R.id.layoutFrom)
    ConstraintLayout select_from_date_img;
    @BindView(R.id.layoutEnd)
    ConstraintLayout select_to_date_img;
    @BindView(R.id.select_from_date)
    TextView select_from_date;
    @BindView(R.id.select_to_date)
    TextView select_to_date;
    @BindView(R.id.ref_no)
    EditText ref_no;
    @BindView(R.id.search)
    TextView search;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refresh_layout;
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
    private List<DepositData> depositList = new ArrayList<>();
    DepositAdapter adapter;
    String device_id = null;
    OnFragmentListener listener = null;
    @SuppressLint("HardwareIds")
    @Override
    public View provideYourFragmentView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deposit, parent, false);
        mContext = this.getActivity();
        rootViewMain = parent;
        ButterKnife.bind(this, rootView);
        mDefaultPresenter = new DefaultPresenter(this);
        device_id = Settings.Secure.getString(requireActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        initializeDepositList();
        mDefaultPresenter.onlineDepositHistory(device_id + "","","","");

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

        refresh_layout.setOnRefreshListener(this::refreshData);
        refresh_layout.setRefreshing(true);

        return rootView;

    }

    void refreshData() {
        mDefaultPresenter.onlineDepositHistory(device_id + "","","","");
        select_from_date.setText("DD MM YYYY");
        select_to_date.setText("DD MM YYYY");
    }

    private void initializeDepositList() {
        adapter = new DepositAdapter(getLayoutInflater());
        list_item.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list_item.setLayoutManager(linearLayoutManager);
        list_item.setAdapter(adapter);

        list_item.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    @Override
    public void onError(String error) {
//        showError(requireContext(), error);
        try {
            listener.onError(error);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void loadData(String value) {
        mDefaultPresenter.onlineDepositHistory(device_id + "",
                /*from_date +*/ "", /*to_date +*/ "", "");
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
            mDefaultPresenter.onlineDepositHistory(device_id + "" ,
                    from_date + "", to_date + "", ref_no_ );
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
    public void onSuccess(String data) {
        try {
            refresh_layout.setRefreshing(false);
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("history_data");
            if (array.length() > 0){
                depositList.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    DepositData depositData = new DepositData();
                    depositData.setOrder_id(object1.getString("order_id"));
                    depositData.setTotal_amount(object1.getString("total_amount"));
                    depositData.setCharge(object1.getString("charge"));
                    depositData.setFinal_amount(object1.getString("final_amount"));
                    depositData.setPayment_status(object1.getString("payment_status"));
                    depositData.setOrder_time(object1.getString("order_time"));
                    depositList.add(depositData);
                    adapter.addEvents(depositList);
                    adapter.notifyDataSetChanged();
                    txtNoData.setVisibility(GONE);
                }
                list_item.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            }else {
                list_item.setVisibility(GONE);
                txtNoData.setVisibility(VISIBLE);
            }


        }catch (Exception e){
            Log.d("TAG_DATA", "onSuccessOther: "+e.getMessage());
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
//        showToast(rootViewMain, message);
        listener.onShowToast(message);
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener){
            listener = (OnFragmentListener) context;
        }else {

        }
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