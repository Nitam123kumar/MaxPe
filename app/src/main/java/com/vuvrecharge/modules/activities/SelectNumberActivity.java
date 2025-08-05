package com.vuvrecharge.modules.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.adapter.ContactAdapter;
import com.vuvrecharge.modules.model.ContactData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectNumberActivity extends BaseActivity implements DefaultView {


    private ContactAdapter adapter;

    private DefaultPresenter mDefaultPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.total_amount)
    TextView total_amount;
    @BindView(R.id.no_data)
    TextView no_data;
    @BindView(R.id.coins_list)
    RecyclerView coins_list;
    @BindView(R.id.search_number)
    EditText search_number;

    @BindView(R.id.pbProgress)
    ProgressBar pbProgress;

    @BindView(R.id.cashback_bg)
    LinearLayout cashback_bg;

    DefaultView mDefaultView;

    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_number);
        ButterKnife.bind(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setToolbar(mToolbar);
        mDefaultView = this;
        title.setText("");
        initializeEventsList();
        search_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });
        mDefaultPresenter = new DefaultPresenter(this);

    }



    private class AsyncTaskResponse extends AsyncTask<String, String, List<ContactData>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<ContactData> doInBackground(String... strings) {
            List<ContactData> contactData = new ArrayList<>();
            try {
                contactData = getContactList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return contactData;
        }

        @Override
        protected void onPostExecute(List<ContactData> contactData) {
            Gson gson = new Gson();
            String s = gson.toJson(contactData);
            mDatabase.setContact(s);
            adapter.addEvents(contactData, mDefaultView);
            pbProgress.setVisibility(View.GONE);
        }
    }
    @NonNull
    @SuppressLint("Range")
    private List<ContactData> getContactList() {
        List<ContactData> contactData = new ArrayList<>();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String display_name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone_number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactData.add(new ContactData(display_name, phone_number));
        }
        phones.close();

        List<ContactData> callLogData_new = new ArrayList<>();
        List<ContactData> callLogData_other = new ArrayList<>();
        List<ContactData> callLogData_add = new ArrayList<>();
        List<ContactData> logMergeData = new ArrayList<>();

        callLogData_new = contactData;
        while (callLogData_new.size() > 0) {
            callLogData_other = new ArrayList<>();
            callLogData_add = new ArrayList<>();
            String number = callLogData_new.get(0).getName();
            for (ContactData callLogDatum : callLogData_new) {
                String number_new = callLogDatum.getName();
                if (number_new.equals(number)) {
                    callLogData_add.add(callLogDatum);
                } else {
                    callLogData_other.add(callLogDatum);
                }
            }
            callLogData_new = callLogData_other;
            logMergeData.add(callLogData_add.get(0));
        }

        printLog("logMergeData " + logMergeData.size());
        printLog("contactData " + contactData.size());
        return contactData;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "selectNumber");
    }

    private void initializeEventsList() {
        adapter = new ContactAdapter(getLayoutInflater());
        coins_list.setHasFixedSize(true);
        coins_list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        coins_list.setAdapter(adapter);
        adapter.addEvents(mDatabase.getContact(), mDefaultView);
        new AsyncTaskResponse().execute();
    }

    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void onSuccess(String data, String data_other) {

    }

    @Override
    public void onSuccessOther(String data) {
        try {
            data = data.replaceAll(" ", "");
            data = data.replaceAll("\\+91", "");
            String firstFourChars = data.substring(0, 1);
            if (firstFourChars.equals("0")) {
                data = data.substring(1, 11);
            }
            BaseMethod.mobile = data;
            BaseMethod.from = "Yes";
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessOther(String data, String data_other) {

    }

    @Override
    public void onShowDialog(String message) {
        showDialog(message);
    }

    @Override
    public void onHideDialog() {
        hideDialog();
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

}