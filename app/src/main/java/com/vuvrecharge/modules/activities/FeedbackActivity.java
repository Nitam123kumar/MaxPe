package com.vuvrecharge.modules.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.vuvrecharge.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.toolbar_layout)
    LinearLayout mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setStatusBarGradiant(this);
        mToolBar.setOnClickListener(this);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);     // Empty stars
                stars.getDrawable(1).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);   // Half filled (if any)
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);   // Full filled
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_layout:
               onBackPressed();
               finish();
                break;
        }
    }
}