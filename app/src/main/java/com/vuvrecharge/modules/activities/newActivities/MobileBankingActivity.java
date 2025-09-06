package com.vuvrecharge.modules.activities.newActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.activities.ManualHistoryActivity;
import com.vuvrecharge.modules.activities.SupportActivity;
import com.vuvrecharge.modules.adapter.BankAdapter;
import com.vuvrecharge.modules.adapter.SearchableSpinneraBankAdapter;
import com.vuvrecharge.modules.adapter.SearchableSpinneraBankSelectionAdapter;
import com.vuvrecharge.modules.model.PaymentData;
import com.vuvrecharge.modules.model.PaymentModeData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.utils.LocaleHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MobileBankingActivity extends BaseActivity implements DefaultView, View.OnClickListener, SearchableSpinneraBankAdapter.OnItemClickListeners, SearchableSpinneraBankSelectionAdapter.OnItemClickListeners {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.select_payment_method_layout)
    LinearLayout select_payment_method_layout;
    @BindView(R.id.select_bank_layout)
    LinearLayout select_bank_layout;

    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.note)
    EditText note;
    @BindView(R.id.reference_number)
    EditText reference_number;
//    @BindView(R.id.select_payment_method)
//    SearchableSpinner select_payment_method;
//    @BindView(R.id.select_bank)
//    SearchableSpinner select_bank;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.txtBank)
    TextView txtBank;
    @BindView(R.id.txtType)
    TextView txtType;

    @BindView(R.id.help)
    TextView help;
    @BindView(R.id.list_item)
    RecyclerView list_item;
    @BindView(R.id.imgHistory)
    ImageView imgHistory;


    DefaultPresenter mDefaultPresenter;
    DefaultView mDefaultView;
    ArrayList<String> bank_list = new ArrayList<>();
    ArrayList<String> type_list = new ArrayList<>();
    private BankAdapter adapter;
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_banking);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        title.setText("Mobile Banking");
        mDefaultView = this;
        mDefaultPresenter = new DefaultPresenter(mDefaultView);

        bank_list.add("Select Bank");
        type_list.add("Select Payment Mode");
        txtType.setText("Select Payment Mode");
        txtBank.setText("Select Bank");
        loadSpinner();
        register.setOnClickListener(this);
        help.setOnClickListener(this);

        mDefaultPresenter.bankDetails(device_id + "");

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    register.setTextColor(Color.WHITE);
                    register.setBackgroundResource(R.drawable.ad_money_button_shape);
                    register.setTypeface(register.getTypeface(), Typeface.BOLD);
                }else {
                    register.setTextColor(getResources().getColor(R.color.black));
                    register.setTypeface(register.getTypeface(), Typeface.BOLD);
                    register.setBackgroundResource(R.drawable.proceed_to_pay);
                }
            }
        });

        imgHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ManualHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "mobilebanking");
    }

    String selected_payment_mode = "Select Payment Mode";
    String selected_bank = "Select Bank";
    private void loadSpinner() {
        select_payment_method_layout.setOnClickListener(this::openDialog);
        select_bank_layout.setOnClickListener(this::openDialog2);
//        OperatorSpinnerAdapter adapter1 = new OperatorSpinnerAdapter(getActivity(), type_list);
//        select_payment_method.setAdapter(adapter1);
//        select_payment_method.setTitle("Select Payment Mode");
//
//        select_payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

//        OperatorSpinnerAdapter adapter_circle = new OperatorSpinnerAdapter(getActivity(), bank_list);
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

        adapter = new BankAdapter(getLayoutInflater());
//        list_item.setHasFixedSize(true);
        list_item.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        list_item.setAdapter(adapter);
    }

    AlertDialog dialog1;
    private void openDialog(View v) {
        View view = getLayoutInflater().inflate(R.layout.searchable_spinner_layout, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog1 = builder.create();
        RecyclerView rv = view.findViewById(R.id.recyclerViewList);
        EditText editText = view.findViewById(R.id.editSearch);
        Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        view.findViewById(R.id.btnClose).setOnClickListener(v1 -> {
            dialog1.dismiss();
            dialog1.cancel();
        });

        SearchableSpinneraBankAdapter circleAdapter = new SearchableSpinneraBankAdapter(this, typeDataList, this);
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
                    ArrayList<PaymentModeData> data = new ArrayList<>();
                    for (PaymentModeData circleData: typeDataList){
                        if (circleData.getName().toUpperCase().toLowerCase().contains(s.toString().toUpperCase().toLowerCase())){
                            data.add(circleData);
                        }
                    }
                    if (data == null || data.isEmpty()){
                        showToast("Not data found");
                        circleAdapter.searchData(typeDataList);

                    }else {
                        circleAdapter.searchData(data);
                    }
                }
            }
        });
        dialog1.show();
    }

    private void openDialog2(View v) {
        View view = getLayoutInflater().inflate(R.layout.searchable_spinner_layout, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog1 = builder.create();
        RecyclerView rv = view.findViewById(R.id.recyclerViewList);
        EditText editText = view.findViewById(R.id.editSearch);
        Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        view.findViewById(R.id.btnClose).setOnClickListener(v1 -> {
            dialog1.dismiss();
            dialog1.cancel();
        });

        SearchableSpinneraBankSelectionAdapter circleAdapter = new SearchableSpinneraBankSelectionAdapter(this, bankDataList, this);
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
                    ArrayList<PaymentData> data = new ArrayList<>();
                    for (PaymentData bank: bankDataList){
                        if (bank.getBank_name().toUpperCase().toLowerCase().contains(s.toString().toUpperCase().toLowerCase())){
                            data.add(bank);
                        }
                    }
                    if (data == null || data.isEmpty()){
                        showToast("Not data found");
                        circleAdapter.searchData(bankDataList);

                    }else {
                        circleAdapter.searchData(data);
                    }
                }
            }
        });
        dialog1.show();
    }

    @Override
    public void onItemClick(@NonNull PaymentModeData model, int position) {
        txtType.setText(model.getName());
        dialog1.cancel();
        dialog1.dismiss();
        if (!txtType.getText().toString().equals("Select Payment Mode")){
            if (position == 0){
                selected_payment_mode = model.getName();
//                selected_circle = model.getId();
            }else {
                selected_payment_mode = model.getName();
//                selected_circle = model.getId();
            }
        }else {
            showError("Please Select Payment Mode.");
        }
    }

    @Override
    public void onItemClick(@NonNull PaymentData model, int position) {
        txtBank.setText(model.getBank_name()+" ("+ model.getAc_no()+")");
        dialog1.cancel();
        dialog1.dismiss();
        if (!txtType.getText().toString().equals("Select Bank")){
            if (position == 0){
                selected_bank = model.getBank_name()+" ("+ model.getAc_no()+")";
//                selected_circle = model.getId();
            }else {
                selected_bank = model.getBank_name()+" ("+ model.getAc_no()+")";
//                selected_circle = model.getId();
            }
        }else {
            showError("Please Select Bank.");
        }
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

        String ref_no_str = reference_number.getText().toString().trim();
        String amount_str = amount.getText().toString().trim();
        String comment_str = note.getText().toString().trim();

        if (reference_number.equals("")) {
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

        hideKeyBoard(reference_number);
        mDefaultPresenter.fundRequest(device_id + "",
                selected_payment_mode + "",
                selected_bank + "",
                amount_str + "",
                ref_no_str + "",
                comment_str + "");
    }

    @Override
    public void onBackPressed() {
        hideKeyBoard(amount);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
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
//                Log.d("TAG_DATA", "onSuccess: Bank Name: "+data.getBank_name()+"\n Account Holder: "+
//                        data.getAc_holder()+"\n Account Number: "+ data.getAc_no()+"\n IFSC Code: "+
//                        data.getIfsc_code()+"\n Account Type: "+data.getAc_type());
            }

            loadSpinner();
            adapter.addData(bankDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String data, String data_other) {

    }

    @Override
    public void onSuccessOther(String data) {
        sessionExpired(data);
    }

    public void sessionExpired(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialogInterface, i) ->
                new Handler().postDelayed(() -> {
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
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.register:
                validateCredentials();
                break;
            case R.id.help:
                Intent intent = new Intent(getActivity(), SupportActivity.class);
                startActivity(intent);
                break;
//            case R.id.txtCopy1:
//                copyReferRal(txtBankNameValue.getText().toString());
//                break;
//            case R.id.txtCopy2:
//                copyReferRal(txtAccountHolderValue.getText().toString());
//                break;
//            case R.id.txtCopy3:
//                copyReferRal(txtAccountNumberValue.getText().toString());
//                break;
//            case R.id.txtCopy4:
//                copyReferRal(txtIFSCCodeValue.getText().toString());
//                break;

            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }

}