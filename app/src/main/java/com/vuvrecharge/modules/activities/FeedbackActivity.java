package com.vuvrecharge.modules.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    private DefaultPresenter mDefaultPresenter;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.toolbar_layout)
    LinearLayout mToolBar;

    @BindView(R.id.user_TextView)
    TextView maxPe_user;
    @BindView(R.id.user_mobile_number)
    TextView user_mobile_number;
    @BindView(R.id.user_feedback_EditText)
    EditText user_feedback;
    @BindView(R.id.submit_feedback_Button)
    AppCompatButton submit_feedback_Btn;
    FrameLayout bottomSheet = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setStatusBarGradiant(this);
        mToolBar.setOnClickListener(this);
        submit_feedback_Btn.setOnClickListener(this);
        mDefaultPresenter = new DefaultPresenter(this);


        try {
            UserData userData = mDatabase.getUserData();
            maxPe_user.setText(userData.getName());
            user_mobile_number.setText(userData.getMobile());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    Toast.makeText(FeedbackActivity.this, "You selected " + (int) rating + " stars", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void openDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        View layout = getLayoutInflater().inflate(R.layout.feedback_dailog_layout, null, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        AppCompatButton okay = layout.findViewById(R.id.okay_btn);

        bottomSheet = bottomSheetDialog.findViewById(com.denzcoskun.imageslider.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(false);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setPeekHeight(600);
        }
        okay.setOnClickListener(v -> {
            bottomSheetDialog.cancel();
        });

        bottomSheetDialog.setContentView(layout);

        bottomSheetDialog.show();


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_layout:
                onBackPressed();
                finish();
                break;
            case R.id.submit_feedback_Button:
                if (TextUtils.isEmpty(user_feedback.getText().toString().trim())) {
                    showError("Please provide feedback");
                    return;
                }

                float rating = ratingBar.getRating();

                if (rating == 0.0f) {
                    Toast.makeText(FeedbackActivity.this, "Please select a rating before submitting.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(FeedbackActivity.this, "Thanks for rating " + (int) rating + " stars!", Toast.LENGTH_LONG).show();

                hideKeyBoard(user_feedback);
                mDefaultPresenter.getFeedback(device_id, user_feedback.getText().toString().trim(), (int) rating);
                hideLoading(loading_dialog);
                openDialog();
                break;
        }
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

    @Override
    public void onSuccessOther(String data, String data_other) {

    }


    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(bottomSheet, error);
        }
        showError(error);
    }

    @Override
    public void onShowDialog(String message) {
//        if (bottomSheet != null) {
//            showLoading(loading);
//        }
//        showLoading(loading);
    }

    @Override
    public void onHideDialog() {

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

    }
}