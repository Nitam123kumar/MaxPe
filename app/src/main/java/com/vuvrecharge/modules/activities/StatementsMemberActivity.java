package com.vuvrecharge.modules.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.adapter.StatementsMemberAdapter;
import com.vuvrecharge.modules.model.WalletData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatementsMemberActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.ref_no)
    EditText ref_no;
    @BindView(R.id.search)
    TextView search;
    @BindView(R.id.select_to_date)
    TextView select_to_date;
    @BindView(R.id.select_from_date_img)
    LinearLayout select_from_date_img;
    @BindView(R.id.select_from_date)
    TextView select_from_date;
    @BindView(R.id.select_to_date_img)
    LinearLayout select_to_date_img;

    StatementsMemberAdapter adapter;
    @BindView(R.id.list_item)
    RecyclerView list_item;
    @BindView(R.id.addMore)
    ImageView addMore;

    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    private DefaultPresenter mDefaultPresenter;

    @BindView(R.id.loading)
    LinearLayout loading;
    static Date fromDate;
    static Date toDate;
    String name = "";
    String mobile = "";
    static String from_date = "";
    static String to_date = "";
    int page = 1;
    int remaining_pages = 3;
    int pastVisibleItems;
    int visibleItemCount;
    int totalItemCount;
    int previousTotal = 0;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_statements);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);

        addMore.setOnClickListener(this);
        search.setOnClickListener(this);
        select_from_date.setOnClickListener(this);
        select_to_date.setOnClickListener(this);
        select_from_date_img.setOnClickListener(this);
        select_to_date_img.setOnClickListener(this);
        mDefaultPresenter = new DefaultPresenter(this);
        Calendar cal = Calendar.getInstance();
        toDate = cal.getTime();
        Date today = new Date();
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(today);
        cal1.add(Calendar.DAY_OF_MONTH, -30);
        fromDate = cal1.getTime();

        from_date = BaseMethod.format1.format(fromDate);
        to_date = BaseMethod.format1.format(toDate);
        String from_date_local = BaseMethod.dateFormat.format(fromDate);
        String to_date_local = BaseMethod.dateFormat.format(toDate);
        select_from_date.setText(from_date_local);
        select_to_date.setText(to_date_local);
        initializeEventsList();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        title.setText(name);
        mobile = intent.getStringExtra("mobile");
        mDefaultPresenter.memberWalletHistory(device_id + "", mobile, page + "",
                from_date + "", to_date + "", "", "Yes");
    }


    boolean isLoading = true;

    private void initializeEventsList() {
        addMore.setVisibility(View.GONE);
        addMore.setImageResource(R.drawable.add_money_shape);
        adapter = new StatementsMemberAdapter(getLayoutInflater());
        list_item.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        list_item.setLayoutManager(linearLayoutManager);
        list_item.setAdapter(adapter);

        list_item.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

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
                        if (remaining_pages > 0) {
                            page++;
                            mDefaultPresenter.memberWalletHistory(device_id + "", mobile, page + "",
                                    from_date + "", to_date + "", "", "No");
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "statementMember");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(@NonNull View v) {
        DialogFragment newFragment;
        switch (v.getId()) {
            case R.id.search:
                hideKeyBoard(ref_no);
                page = 1;
                remaining_pages = 0;
                pastVisibleItems = 0;
                visibleItemCount = 0;
                totalItemCount = 0;
                previousTotal = 0;
                String ref_no_ = ref_no.getText().toString().trim();
                mDefaultPresenter.memberWalletHistory(device_id + "", mobile, page + "",
                        from_date + "", to_date + "", ref_no_ + "", "Yes");
                break;
            case R.id.select_from_date_img:
                newFragment = new SelectDate(select_from_date, select_to_date);
                newFragment.show(getSupportFragmentManager(), "Select From Date");
                break;
            case R.id.select_to_date_img:
                newFragment = new SelectDate(select_from_date, select_to_date);
                newFragment.show(getSupportFragmentManager(), "Select To Date");
                break;
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(bottomSheet, error);
        } else {
            showError(error);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyBoard(ref_no);
    }

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void onSuccess(String message, String second_message) {
        try {
            isLoading = false;
            JSONObject jsonObject = new JSONObject(message);
            printLog(jsonObject.toString());
            remaining_pages = Integer.parseInt(jsonObject.getString("remaining_pages"));
            JSONArray jsonArray = jsonObject.getJSONArray("history_data");
            Gson gson = new Gson();
            Type type_ = new TypeToken<List<WalletData>>() {
            }.getType();
            List<WalletData> passbookData = gson.fromJson(jsonArray.toString(), type_);
            if (second_message.equals("Yes")) {
                initializeEventsList();
            }
            adapter.addData(passbookData, second_message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessOther(String data) {

    }

    String orderid = "";

    @Override
    public void onSuccessOther(String data, String data_other) {
        Intent intent = new Intent(getActivity(), AddBalanceActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
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

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(0);
                    cal.set(year, month, day, 0, 0, 0);
                    String tag = getTag();
                    if (tag != null) {
                        switch (tag) {
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


    @Override
    public void onShowDialog(String message) {
        if (bottomSheet != null) {
            showLoading(loading_dialog);
            submit.setVisibility(View.GONE);
        } else {
            showLoading(loading);
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
    public void onShowToast(String message) {
        if (bottomSheet != null) {
            showToast(bottomSheet, message);
        } else {
            showToast(message);
        }
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

}
