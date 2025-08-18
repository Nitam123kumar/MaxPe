package com.vuvrecharge.modules.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
import com.vuvrecharge.modules.adapter.MaxPointFragmentAdapter;
import com.vuvrecharge.modules.model.CashBackPintsModel;
import com.vuvrecharge.modules.model.MaxPePointsData;
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


public class MaxPointFragment extends BaseFragment implements DefaultView {
    ViewGroup rootViewMain;

    DefaultPresenter defaultPresenter;

    @BindView(R.id.rvMaxPoints)
    RecyclerView rvMaxPoints;
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
    @BindView(R.id.filter_View)
    View filter_View;
    @BindView(R.id.statements_header)
    View date_picker_layout;
    MaxPointFragmentAdapter adapter;

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
    String device_id;
    boolean isLoading = true;
    DialogFragment newFragment;
    OnFragmentListener listener = null;
    private List<MaxPePointsData> mPointsList = new ArrayList<>();
    Boolean isFrom = false;
    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_max_point, parent, false);
        this.rootViewMain = parent;
        ButterKnife.bind(this, rootView);
        device_id = Settings.Secure.getString(requireActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        defaultPresenter = new DefaultPresenter(this);
//        defaultPresenter.cashbackPointsHistory(device_id + "",
//                  "",  "", "");

        initializeEventsList();
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

        return rootView;
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    public void onSuccess(String data, String data_other) {

    }

    @Override
    public void onSuccessOther(String data) {

    }
    void refreshData() {
        defaultPresenter.cashbackPointsHistory(device_id + "",
                "",  "", "");
        select_from_date.setText("DD MM YYYY");
        select_to_date.setText("DD MM YYYY");
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
            newFragment.show(getChildFragmentManager(), "Select From Date");
        });
        select_to_date_img.setOnClickListener(v -> {
            newFragment = new SelectDate(select_from_date, select_to_date);
            newFragment.show(getChildFragmentManager(), "Select To Date");
        });

    }


    @Override
    public void onSuccessOther(String data, String data_other) {
        Log.d("cashbackLogs", String.valueOf(data));
        try {
            refresh_layout.setRefreshing(false);
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("cashbackLogs");


            if (array.length() > 0) {
                mPointsList.clear();
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
                    mPointsList.add(maxPePointData);
                    adapter.notifyDataSetChanged();
                    txtNoData.setVisibility(GONE);

                }

                rvMaxPoints.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            } else {
                rvMaxPoints.setVisibility(GONE);
                txtNoData.setVisibility(VISIBLE);
            }
            txtNoData.setVisibility(GONE);


            if (object.has("cashbackPoints")) {
                JSONObject pointsObject = object.getJSONObject("cashbackPoints");

                CashBackPintsModel model = new CashBackPintsModel();
                model.setMax_balance_slab(pointsObject.getString("max_balance_slab"));
                model.setCashback_points(pointsObject.getString("cashback_points"));
                model.setTxn_balance_count(pointsObject.getString("txn_balance_count"));


            }

            if (array.length() > 0) {
                Gson gson = new Gson();
                Type type_ = new TypeToken<List<MaxPePointsData>>() {
                }.getType();
                List<MaxPePointsData> passbookData = gson.fromJson(array.toString(), type_);
                if (data_other.equals("Yes")) {
                    initializeEventsList();
                }
                adapter.addEvents(passbookData, data_other);
                rvMaxPoints.setVisibility(VISIBLE);
                txtNoData.setVisibility(GONE);
            } else {
//                onError("no Data found");
                txtNoData.setVisibility(VISIBLE);
                rvMaxPoints.setVisibility(GONE);
            }


        } catch (Exception e) {

            txtNoData.setVisibility(VISIBLE);
        }
    }

    private void initializeEventsList() {

        adapter = new MaxPointFragmentAdapter(requireContext(), mPointsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        rvMaxPoints.setLayoutManager(linearLayoutManager);
        rvMaxPoints.setAdapter(adapter);
        rvMaxPoints.addOnScrollListener(new RecyclerView.OnScrollListener() {
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