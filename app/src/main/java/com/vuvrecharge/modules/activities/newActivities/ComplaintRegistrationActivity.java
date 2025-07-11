package com.vuvrecharge.modules.activities.newActivities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComplaintRegistrationActivity  extends BaseActivity implements DefaultView,View.OnClickListener {

    private DefaultPresenter mDefaultPresenter;
    @BindView(R.id.toolbar_layout)
    LinearLayout toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.txtDisposition)
    TextView txtDisposition;
    @BindView(R.id.txtComplaintIdValue)
    TextView txtComplaintIdValue;
    @BindView(R.id.txtComplaintStatusValue)
    TextView txtComplaintStatusValue;
    @BindView(R.id.txtRemarkValue)
    TextView txtRemarkValue;
    @BindView(R.id.editTransactionRefId)
    EditText editTransactionRefId;
    @BindView(R.id.editComplaintId)
    EditText editComplaintId;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.btnTrackingSubmit)
    Button btnTrackingSubmit;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.complaintRegistered)
    ConstraintLayout complaintRegistered;
    @BindView(R.id.complaintTracking)
    ConstraintLayout complaintTracking;
    @BindView(R.id.complaintTrackingResponse)
    ConstraintLayout complaintTrackingResponse;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_registration);
        ButterKnife.bind(this);
        toolbar.setOnClickListener(this);
        title.setText("Complain Registration");

        mDefaultPresenter = new DefaultPresenter(this);
        setStatusBarGradiant(this);
        btnSubmit.setOnClickListener(v -> {
            if (editTransactionRefId.getText().toString().isEmpty()){
                showError("Please enter transaction reference id / Order Id");
            }else if (txtDisposition.getText().toString().equals("Complaint Disposition")){
                showError("Please select complaint disposition.");
            }else {
                hideKeyBoard(editTransactionRefId);
                hideKeyBoard(editComplaintId);
                mDefaultPresenter.doBbpsComplaint(
                        device_id,
                        txtDisposition.getText().toString().trim(),
                        editTransactionRefId.getText().toString().trim()
                );
            }
        });
        txtDisposition.setOnClickListener(this::openDispositionsPopUp);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioButton = findViewById(checkedId);
            if (radioButton.getText().toString().equals("Complaint Register")){
                complaintTracking.setVisibility(View.GONE);
                complaintRegistered.setVisibility(View.VISIBLE);
                complaintTrackingResponse.setVisibility(View.GONE);
                editTransactionRefId.setText("");
            }else if (radioButton.getText().toString().equals("Complaint Tracking")){
                editComplaintId.setText("");
                complaintRegistered.setVisibility(View.GONE);
                complaintTracking.setVisibility(View.VISIBLE);
                complaintTrackingResponse.setVisibility(View.GONE);
            }else {
                editComplaintId.setText("");
                editTransactionRefId.setText("");
                complaintRegistered.setVisibility(View.GONE);
                complaintTracking.setVisibility(View.GONE);
                complaintTrackingResponse.setVisibility(View.GONE);
                Toast.makeText(this, "None", Toast.LENGTH_SHORT).show();
            }
        });

        btnTrackingSubmit.setOnClickListener(v -> {
            if (editComplaintId.getText().toString().isEmpty()){
                showError("Please enter complaint registered id");
            }else {
                hideKeyBoard(editTransactionRefId);
                hideKeyBoard(editComplaintId);
                mDefaultPresenter.complaintTracking(
                        device_id,
                        editComplaintId.getText().toString().trim()
                );
            }
        });
    }

    private void openDispositionsPopUp(@NonNull View v) {
        View view = getLayoutInflater().inflate(R.layout.complaint_registered_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(view);
        AlertDialog dialog1 = builder.create();
        Window window = dialog1.getWindow();

        view.findViewById(R.id.complaintRegistered).setVisibility(View.GONE);
        view.findViewById(R.id.complaintDisposition).setVisibility(View.VISIBLE);
        ListView listView = view.findViewById(R.id.listView);
        ArrayList<String> list = new ArrayList<>();
        list.add("Complaint Disposition");
        list.add("Transaction Successful, account not updated");
        list.add("Amount deducted, biller account credited but transaction ID not received");
        list.add("Amount deducted, biller account not credited & transaction ID not received");
        list.add("Amount deducted multiple times");
        list.add("Double payment updated");
        list.add("Erroneously paid in wrong account");
        ListAdapter adapter = new ListAdapter(v.getContext(), R.layout.list_item_layout,list);
        listView.setAdapter(adapter);
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String name = list.get(position).toString();
            txtDisposition.setText(name);
            dialog1.dismiss();
            dialog1.cancel();
        });
        dialog1.show();
    }

    private void openDialog(String data) {
        View view = getLayoutInflater().inflate(R.layout.complaint_registered_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(view);
        AlertDialog dialog1 = builder.create();
        Window window = dialog1.getWindow();
        view.findViewById(R.id.complaintRegistered).setVisibility(View.VISIBLE);
        view.findViewById(R.id.complaintDisposition).setVisibility(View.GONE);
        view.findViewById(R.id.txt2)
                .setOnClickListener(v -> {
                    dialog1.cancel();
                    dialog1.dismiss();
                });

        TextView txt = view.findViewById(R.id.txt1);
        txt.setText(data);

        dialog1.setCancelable(false);
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog1.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "complaintRegistration");
    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(bottomSheet, error);
        }
        showErrorR(error);
    }

    @Override
    public void onSuccess(String data) {

    }

    @Override
    public void onSuccess(String data, String data_other) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.main_wallet_shape);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    @Override
    public void onSuccessOther(String data) {

    }

    @Override
    public void onSuccessOther(String data, @NonNull String data_other) {
        complaintTrackingResponse.setVisibility(View.GONE);
        if (data_other.equals("bbpsComplaint")){
            openDialog(data);
            complaintTrackingResponse.setVisibility(View.GONE);
        }else if (data_other.equals("tracking")){
            try {
                JSONObject object = new JSONObject(data);
                txtComplaintIdValue.setText(object.getString("complaintId"));
                txtComplaintStatusValue.setText(object.getString("complaintStatus"));
                txtRemarkValue.setText(object.getString("remark"));
            }catch (JSONException e){
                Log.e("TAG_DATA", "onSuccessOther: ", e);
            }
            complaintTrackingResponse.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShowDialog(String message) {
        if (bottomSheet != null) {
            showLoading(loading_dialog);
            submit.setVisibility(View.GONE);
        }
        showLoading(loading);
    }

    @Override
    public void onHideDialog() {
        if (bottomSheet != null) {
            hideLoading(loading_dialog);
            submit.setVisibility(View.VISIBLE);
        }
        hideLoading(loading);
    }

    @Override
    public void onShowToast(String message) {
        if (bottomSheet != null) {
            showToast(bottomSheet, message);
        }
        showToast(message);
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch(v.getId()){
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }

    class ListAdapter extends ArrayAdapter{
        List<String> list;
        public ListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);

            TextView txt = view.findViewById(R.id.textView);
            txt.setText(list.get(position).trim());

            if (position == list.size()-1){
                view.findViewById(R.id.viewLine).setVisibility(View.GONE);
            }else {
                view.findViewById(R.id.viewLine).setVisibility(View.VISIBLE);
            }

            return view;
        }
    }
}
