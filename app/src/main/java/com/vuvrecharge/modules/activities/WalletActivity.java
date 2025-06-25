package com.vuvrecharge.modules.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.adapter.AddPaymentHistoryAdapter;
import com.vuvrecharge.modules.model.DepositData;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletActivity extends BaseActivity implements View.OnClickListener, DefaultView {

    @BindView(R.id.recyclerView_latest_transitions)
    RecyclerView recyclerView_latest_transitions;
    @BindView(R.id.name_tv1)
    TextView name_TV;
    @BindView(R.id.onBack)
    LinearLayout onBack;
    @BindView(R.id.history_layout)
    LinearLayout history_layout;

    @BindView(R.id.add_amount_Btn)
    AppCompatButton add_amount_Btn;
    @BindView(R.id.balance_TV)
    TextView balance_TV;
    @BindView(R.id.no_data_layout)
    ConstraintLayout no_data_layout;
    @BindView(R.id.add_Money_Layout)
    LinearLayout add_Money_Layout;
    @BindView(R.id.viewAll)
    ConstraintLayout viewAll;
    @BindView(R.id.loading)
    LinearLayout loading;

    DefaultPresenter mDefaultPresenter;
    String device_id;

    AddPaymentHistoryAdapter addPaymentHistoryAdapter;
    private List<DepositData> depositList = new ArrayList<>();

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(getActivity());
        setStatusBarGradiant(this);
        mDefaultPresenter = new DefaultPresenter(this);
        device_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        history_layout.setOnClickListener(this);
        add_amount_Btn.setOnClickListener(this);
        add_Money_Layout.setOnClickListener(this);
        recyclerView_latest_transitions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addPaymentHistoryAdapter = new AddPaymentHistoryAdapter(getLayoutInflater());
        recyclerView_latest_transitions.setAdapter(addPaymentHistoryAdapter);
        mDefaultPresenter.onlineDepositHistory(device_id + "");

        try {
            UserData userData = mDatabase.getUserData();
            name_TV.setText("Hello, \n" + userData.getName());
            balance_TV.setText("₹" + userData.getEarnings());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        onBack.setOnClickListener(this);
        addPaymentHistoryAdapter.setOnItemClickListener(new AddPaymentHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DepositData data) {
                showBottomSheet(data);
            }
        });

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.main_wallet_shape);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }
    private void showBottomSheet(DepositData data) {

        FrameLayout bottomSheet = null;

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        View layout = getLayoutInflater().inflate(R.layout.add_amount_history_details_layout, null,false);
        ImageView onSuccess = layout.findViewById(R.id.on_success);
        ImageView details_cancel = layout.findViewById(R.id.details_cancel);
        TextView success_amount = layout.findViewById(R.id.success_amount);
        TextView order_Id = layout.findViewById(R.id.order_Id);
        TextView charge_textView = layout.findViewById(R.id.charge_textView);
        TextView payment_time = layout.findViewById(R.id.payment_time);
        TextView needSupport = layout.findViewById(R.id.need_support);
        TextView close_textView = layout.findViewById(R.id.close_textView);
        TextView more_details = layout.findViewById(R.id.more_details);
        TextView transaction_successful_textView = layout.findViewById(R.id.transaction_successful_textView);

        bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(false);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setPeekHeight(600);
        }

        success_amount.setText("\u20b9" + data.getTotal_amount());
        order_Id.setText(data.getOrder_id());
        charge_textView.setText("\u20b9" + data.getCharge());

        details_cancel.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });
        close_textView.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });
        needSupport.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SupportActivity.class);
            startActivity(intent);
        });
        more_details.setOnClickListener(v -> {
            Toast.makeText(getActivity(),"Coming Soon",Toast.LENGTH_SHORT).show();
        });

        try {
            String dateFormatNew = "N/A";
            Date date = new Date((Long.parseLong(data.getOrder_time())) * 1000);
            dateFormatNew = BaseMethod.dateFormatNew.format(date);
            payment_time.setText(dateFormatNew);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (data.getPayment_status().toUpperCase().equals("PENDING")) {
            Glide.with(getActivity())
                    .load(R.drawable.pending_2)
                    .into(onSuccess);
            transaction_successful_textView.setText("This transaction was pennding");
        } else if (data.getPayment_status().toUpperCase().equals("SUCCESS")) {
            Glide.with(getActivity())
                    .load(R.drawable.success_img)
                    .into(onSuccess);
            transaction_successful_textView.setText("This transaction was successful");
        } else {
            Glide.with(getActivity())
                    .load(R.drawable.close_1)
                    .into(onSuccess);
            transaction_successful_textView.setText("This transaction was failed");
        }

        bottomSheetDialog.setContentView(layout);

        bottomSheetDialog.show();

    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.onBack:
                onBackPressed();
                getActivity().finish();
                break;
            case R.id.history_layout:
                Intent intent = new Intent(getActivity(), StatementsActivity.class);
                startActivity(intent);
                break;
            case R.id.add_amount_Btn:
                Intent addAmountIntent = new Intent(getActivity(), AddBalanceActivity.class);
                startActivity(addAmountIntent);
                break;
            case R.id.add_Money_Layout:
                Intent addMoneyIntent = new Intent(getActivity(), AddBalanceActivity.class);
                startActivity(addMoneyIntent);
                break;
        }
    }

    @Override
    public void onError(String error) {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccess(String data) {
        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("history_data");

            if (array.length() > 0) {
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
                    addPaymentHistoryAdapter.addEvents(depositList);
                    addPaymentHistoryAdapter.notifyDataSetChanged();
                    no_data_layout.setVisibility(GONE);
                }
                recyclerView_latest_transitions.setVisibility(VISIBLE);
                no_data_layout.setVisibility(GONE);
            }
        } catch (Exception e) {
            Log.d("TAG_DATA", "onSuccess: " + e.getMessage());
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

    }

    @Override
    public void onPrintLog(String message) {

    }
}