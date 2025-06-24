package com.vuvrecharge.modules.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.OperatorSpinnerAdapter;
import com.vuvrecharge.modules.model.StateData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddMemberActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add_member)
    TextView add_member;

    @BindView(R.id.full_name)
    EditText full_name;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.enter_mobile_no)
    EditText enter_mobile_no;
    @BindView(R.id.form_name)
    EditText form_name;
    @BindView(R.id.member_des)
    EditText member_des;
    @BindView(R.id.land_mark)
    EditText land_mark;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.pincode)
    EditText pincode;
    @BindView(R.id.gst_no)
    EditText gst_no;
    @BindView(R.id.description)
    EditText description;
//    @BindView(R.id.select_member)
//    SearchableSpinner select_member;
//    @BindView(R.id.select_state)
//    SearchableSpinner select_state;
//    @BindView(R.id.select_district)
//    SearchableSpinner select_district;

    DefaultPresenter mDefaultPresenter;
    DefaultView mDefaultView;

    @BindView(R.id.loading)
    LinearLayout loading;

    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;

    String memberType = "", id = "",package_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        mDefaultView = this;
        mDefaultPresenter = new DefaultPresenter(mDefaultView);

        try {
            Intent intent = getIntent();
            memberType = intent.getStringExtra("memberType");
            package_name = intent.getStringExtra("package_name");
            id = intent.getStringExtra("id");
            System.out.println("memberType"+memberType);
            title.setText("New Member"+" ("+ memberType +"-"+package_name+")");
        }catch (Exception e){
            e.printStackTrace();
        }

        loadSpinner();
        add_member.setOnClickListener(this);
        mDefaultPresenter.loadDataForNewMember(device_id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "addMember");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyBoard(enter_mobile_no);
    }

    @Override
    public void onClick(@NonNull View view) {

        switch (view.getId()) {
            case R.id.add_member:
                validateCredentials();
                break;
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }

    }

    private void validateCredentials() {
        hideKeyBoard(enter_mobile_no);
        mDefaultPresenter.addNewMember(device_id + "",
                full_name.getText().toString() + "",
                email.getText().toString() + "",
                enter_mobile_no.getText().toString() + "",
                form_name.getText().toString() + "",
                member_des.getText().toString() + "",
                selected_state + "",
                selected_city + "",
                land_mark.getText().toString() + "",
                address.getText().toString() + "",
                pincode.getText().toString() + "",
                gst_no.getText().toString() + "",
                description.getText().toString() + "",memberType,id);
    }

    ArrayList<String> state_list = new ArrayList<>();
    ArrayList<String> cities_list = new ArrayList<>();
    ArrayList<String> member_type = new ArrayList<>();


    @Override
    public void onSuccess(String message) {

        try {
            JSONObject jsonObject = new JSONObject(message);
            mDefaultView.onPrintLog(jsonObject.toString());
            member_type.add(jsonObject.getString("member_type"));
            Gson gson = new Gson();
            Type type_ = new TypeToken<List<StateData>>() {
            }.getType();
            statesData = gson.fromJson(jsonObject.getString("states"), type_);
            loadSpinner();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    List<StateData> statesData = new ArrayList<>();
    List<StateData> citiesData = new ArrayList<>();

    OperatorSpinnerAdapter adapter_circle = null;
    OperatorSpinnerAdapter adapter_district = null;
    String state_code = "0";
    String selected_state = "Select State";
    String selected_city = "Select City";

    private void loadSpinner() {
        state_list.clear();
        state_list.add("Select State");
        for (StateData statesDatum : statesData) {
            state_list.add(statesDatum.getName());
        }
        OperatorSpinnerAdapter adapter1 = new OperatorSpinnerAdapter(getActivity(), member_type);
//        select_member.setAdapter(adapter1);
//        select_member.setEnabled(false);
//        select_member.setTitle("Select Member");
        adapter_circle = new OperatorSpinnerAdapter(getActivity(), state_list);
//        select_state.setAdapter(adapter_circle);
//        select_state.setTitle("Select State");
//        select_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                citiesData.clear();
//                if (position > 0) {
//                    state_code = statesData.get(position - 1).getId();
//                    selected_state = statesData.get(position - 1).getId();
//                    mDefaultPresenter.fetchCities(device_id, state_code);
//                } else {
//                    selected_state = "Select State";
//                }
//                selected_city = "Select City";
//                loadCity();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void loadCity() {
        cities_list.clear();
        cities_list.add("Select City");
        for (StateData statesDatum : citiesData) {
            cities_list.add(statesDatum.getName());
        }
        adapter_district = new OperatorSpinnerAdapter(getActivity(), cities_list);
//        select_district.setAdapter(adapter_district);
//        select_district.setTitle("Select City");
//        select_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position > 0) {
//                    selected_city = citiesData.get(position - 1).getId();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    @Override
    public void onSuccess(String message, String second_message) {
        onShowMessageDialogMember(message);
    }

    public void onShowMessageDialogMember(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton(getResources().getText(R.string.btn_ok), (dialog_, which) -> {
            Intent intent = new Intent(getActivity(), MembersActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onSuccessOther(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            mDefaultView.onPrintLog(jsonObject.toString());
            Gson gson = new Gson();
            Type type_ = new TypeToken<List<StateData>>() {
            }.getType();
            citiesData = gson.fromJson(jsonObject.getString("cities"), type_);
            loadCity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessOther(String data, String data_other) {

    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(error);
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
            showToast(message);
        } else {
            showToast(message);
        }
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }

}