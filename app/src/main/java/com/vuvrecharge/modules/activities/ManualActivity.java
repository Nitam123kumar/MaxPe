package com.vuvrecharge.modules.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.BankAdapter;
import com.vuvrecharge.modules.adapter.OperatorSpinnerAdapter;
import com.vuvrecharge.modules.model.PaymentData;
import com.vuvrecharge.modules.model.PaymentModeData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
//import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManualActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.list_item)
    RecyclerView list_item;
//    private BankAdapter adapter;
    ArrayList<String> bank_list = new ArrayList<>();
    ArrayList<String> type_list = new ArrayList<>();
//    @BindView(R.id.select_payment_mode)
//    SearchableSpinner select_payment_mode;
//    @BindView(R.id.select_bank)
//    SearchableSpinner select_bank;
    @BindView(R.id.ref_no)
    EditText ref_no;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.comment)
    EditText comment;

    DefaultPresenter mDefaultPresenter;
    DefaultView mDefaultView;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        title.setText("Manual History");
        mDefaultView = this;
        mDefaultPresenter = new DefaultPresenter(mDefaultView);
        bank_list.add("Select Bank");
        type_list.add("Select Payment Mode");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        loadSpinner();
        register.setOnClickListener(this);
        mDefaultPresenter.bankDetails(device_id + "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "manual");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
    }

    @Override
    public void onBackPressed() {
        hideKeyBoard(amount);
        super.onBackPressed();
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.register:
                validateCredentials();
                break;
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }

    String selected_payment_mode = "Select Payment Mode";
    String selected_bank = "Select Bank";

    private void loadSpinner() {
        OperatorSpinnerAdapter adapter1 = new OperatorSpinnerAdapter(getActivity(), type_list);
//        select_payment_mode.setAdapter(adapter1);
//        select_payment_mode.setTitle("Select Payment Mode");
//
//        select_payment_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selected_payment_mode = type_list.get(i);
//                adapter1.addData(selected_payment_mode);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        OperatorSpinnerAdapter adapter_circle = new OperatorSpinnerAdapter(getActivity(), bank_list);
//        select_bank.setAdapter(adapter_circle);
//        select_bank.setTitle("Select Bank");
//        select_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selected_bank = bank_list.get(i);
//                adapter_circle.addData(selected_bank);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        adapter = new BankAdapter(getLayoutInflater());
//        list_item.setHasFixedSize(true);
//        list_item.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
//        list_item.setAdapter(adapter);

    }


    private void validateCredentials() {
        if (selected_payment_mode.equals("Select Payment Mode")) {
            showError("Select payment mode");
            return;
        }
        if (selected_bank.equals("Select Bank")) {
            showError("Select Bank");
            return;
        }

        String ref_no_str = ref_no.getText().toString().trim();
        String amount_str = amount.getText().toString().trim();
        String comment_str = comment.getText().toString().trim();

        if (ref_no_str.equals("")) {
            showError("Enter reference no");
            return;
        }

        if (amount_str.equals("")) {
            showError("Enter amount");
            return;
        }
        if (amount_str.contains(" ")) {
            showError("Amount can't have space");
            return;
        }

        hideKeyBoard(ref_no);
        mDefaultPresenter.fundRequest(device_id + "",
                selected_payment_mode + "",
                selected_bank + "",
                amount_str + "",
                ref_no_str + "",
                comment_str + "");
    }

    List<PaymentData> bankDataList = new ArrayList<>();
    List<PaymentModeData> typeDataList = new ArrayList<>();

    @Override
    public void onSuccess(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            Gson gson = new Gson();
            Type type_ = new TypeToken<List<PaymentModeData>>() {
            }.getType();
            List<PaymentModeData> typeDataList = gson.fromJson(jsonObject.getString("payment_modes"), type_);

            Type type_1 = new TypeToken<List<PaymentData>>() {
            }.getType();
            List<PaymentData> bankDataList = gson.fromJson(jsonObject.getString("payment_details"), type_1);

            this.bankDataList = bankDataList;
            this.typeDataList = typeDataList;

            bank_list = new ArrayList<>();
            type_list = new ArrayList<>();
            bank_list.add("Select Bank");

            for (PaymentModeData operatorData : typeDataList) {
                type_list.add(operatorData.getName());
            }
            for (PaymentData data : bankDataList) {
                bank_list.add(data.getBank_name() + " (" + data.getAc_no() + ")");
            }
            loadSpinner();
//            adapter.addData(bankDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String message, String second_message) {

    }

    @Override
    public void onSuccessOther(String data) {
        sessionExpired(data);
    }

    public void sessionExpired(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialogInterface, i) -> new Handler().postDelayed(() -> {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), ManualHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }, 000));
        AlertDialog dialog = builder.create();
        dialog.show();

        Button nbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        nbutton.setTextColor(Color.parseColor("#008000"));

    }
    @Override
    public void onSuccessOther(String data, String data_other) {

    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(bottomSheet, error);
            submit.setVisibility(View.VISIBLE);
        } else {
            showError(error);
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
}