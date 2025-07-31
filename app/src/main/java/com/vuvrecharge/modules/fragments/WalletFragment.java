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
import com.vuvrecharge.modules.adapter.WalletAdapter;
import com.vuvrecharge.modules.model.MaxPePointsData;
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

public class WalletFragment extends BaseFragment implements DefaultView {

    private DefaultPresenter mDefaultPresenter;
    ViewGroup rootViewMain;
    @BindView(R.id.rvWalletDetails)
    RecyclerView rvWalletDetails;
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
    @BindView(R.id.txtNoData)
    TextView txtNoData;

    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refresh_layout;
    WalletAdapter adapter;
//    TextView invoice;
    String device_id = "";

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
    List<WalletData> list= new ArrayList<>();

    @SuppressLint("HardwareIds")
    @Override
    public View provideYourFragmentView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wallet, parent, false);
        this.rootViewMain = parent;
        ButterKnife.bind(this, rootView);
        mDefaultPresenter = new DefaultPresenter(this);
        device_id = Settings.Secure.getString(requireActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
//        invoice = requireActivity().findViewById(R.id.invoice);
//        invoice.setVisibility(GONE);

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
        initializeEventsList();

        onclickListener();

        refresh_layout.setOnRefreshListener(this::refreshData);
        refresh_layout.setRefreshing(true);

        return rootView;
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
            mDefaultPresenter.userPassbook(device_id + "", page + "",
                    from_date + "", to_date + "", ref_no_ + "", "Yes");

            Log.d("from_date", from_date);
            Log.d("to_date", to_date);

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
    void refreshData() {
        refresh_layout.setRefreshing(true);
        page = 1;
        remaining_pages = 0;
        previousTotal = 0;
        pastVisibleItems = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        isLoading = true;

        mDefaultPresenter.userPassbook(device_id + "", page + "",
                "", "", "", "No");
        select_from_date.setText("DD MM YYYY");
        select_to_date.setText("DD MM YYYY");
    }
    private void initializeEventsList() {
        adapter = new WalletAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvWalletDetails.setLayoutManager(linearLayoutManager);
        rvWalletDetails.setAdapter(adapter);
        rvWalletDetails.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        mDefaultPresenter.userPassbook(device_id + "", page + "",
                /*from_date +*/ "", /*to_date +*/ "", "", value);
    }

    @Override
    public void onError(String error) {
//        showError(rootViewMain, error);
        listener.onError(error);
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

            Log.d("history_data", String.valueOf(jsonArray));
            if (jsonArray.length() > 0) {
                Gson gson = new Gson();
                Type type_ = new TypeToken<List<WalletData>>() {}.getType();
                List<WalletData> passbookData = gson.fromJson(jsonArray.toString(), type_);

                if (page == 1) {
                    adapter.setData(passbookData); // clear and add fresh data
                } else {
                    adapter.addData(passbookData); // add data for pagination
                }
                rvWalletDetails.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            } else {
                if (page == 1) {
                    rvWalletDetails.setVisibility(GONE);
                    txtNoData.setVisibility(VISIBLE);
                }
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
            Calendar calendar = Calendar.getInstance(); // Defaults to today's date

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(requireContext(), R.style.DialogTheme,
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener){
            listener = (OnFragmentListener) context;
        }else {

        }
    }
}