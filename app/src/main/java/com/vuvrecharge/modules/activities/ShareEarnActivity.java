package com.vuvrecharge.modules.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareEarnActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    private DefaultPresenter mDefaultPresenter;

    @BindView(R.id.toolbar_LinearLayout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.txtReferId)
    EditText referCode;

    @BindView(R.id.btnReferCopy)
    TextView btnReferCopy;
//    @BindView(R.id.txtReferSlug)
//    TextView txtReferSlug;

    @BindView(R.id.btnInvite)
    ConstraintLayout btnInvite;
    @BindView(R.id.txtMyReferralValue)
    TextView txtMyReferralValue;
    @BindView(R.id.txtReferralIncomeValue)
    TextView txtReferralIncomeValue;
//    @BindView(R.id.txtSuccessfulReferralCount)
//    TextView txtSuccessfulReferralCount;
//    @BindView(R.id.txtBonusEarnCount)
//    TextView txtBonusEarnCount;
//    @BindView(R.id.txtExpectedBonusCount)
//    TextView txtExpectedBonusCount;
//    @BindView(R.id.txtReferMessage)
//    TextView txtReferMessage;
//    @BindView(R.id.txtReferMaxSlug)
//    TextView txtReferMaxSlug;

    @BindView(R.id.viewMyReferral)
    View viewMyReferral;
    @BindView(R.id.top_earner_ImageSlider)
    ImageSlider top_earner_ImageSlider;
    @BindView(R.id.viewReferralIncome)
    View viewReferralIncome;
    @BindView(R.id.txtTermsCondition)
    TextView txtTermsCondition;
    @BindView(R.id.imageSlider)
    ImageSlider imageSlider;
    String shareText = "";
    TextView submit;
    LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareearn);
        ButterKnife.bind(this);
        title.setText("Refer & Earn");
        mDefaultPresenter = new DefaultPresenter(this);
        mToolbar.setOnClickListener(this);
        btnInvite.setOnClickListener(this);
        btnReferCopy.setOnClickListener(this);
        viewMyReferral.setOnClickListener(this);
        viewReferralIncome.setOnClickListener(this);
        if (mDatabase != null) {
            if (mDatabase.getUserData() != null) {
                if (mDatabase.getUserData().getReferCode() != null) {
                    referCode.setText(mDatabase.getUserData().getReferCode());
                }
            }
        }
        txtTermsCondition.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReferandEarnTermsActivity.class);
            startActivity(intent);
        });
        mDefaultPresenter.totalReferrals(device_id);

        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.refer_and_earn_img,ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.refer_and_earn_img,ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.refer_and_earn_img,ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.refer_and_earn_img,ScaleTypes.FIT));

        imageSlider.setImageList(imageList, ScaleTypes.FIT);

        ArrayList<SlideModel> top_earner_list = new ArrayList<>();

        top_earner_list.add(new SlideModel(R.drawable. top_earner, ScaleTypes.FIT));
        top_earner_list.add(new SlideModel(R.drawable. top_earner, ScaleTypes.FIT));
        top_earner_list.add(new SlideModel(R.drawable. top_earner, ScaleTypes.FIT));
        top_earner_list.add(new SlideModel(R.drawable. top_earner, ScaleTypes.FIT));

        top_earner_ImageSlider.setImageList(top_earner_list, ScaleTypes.FIT);
//        top_earner_ImageSlider.setIndicatorSelectedColor(Color.RED);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "shareEarn");
    }

    @Override
    protected void onPause() {
        hideAllDialog();
        super.onPause();
    }

    @Override
    public void onSuccess(String userData) {
        try {
            JSONObject jsonObject = new JSONObject(userData);
            txtMyReferralValue.setText(jsonObject.getString("totalReferrals"));
            txtReferralIncomeValue.setText("\u20b9 " + jsonObject.getString("totalReferralPaid"));
            String referText = jsonObject.getString("referPageText");
            JSONObject object = new JSONObject(referText);

//            txtSuccessfulReferralCount.setText(object.getString("successfull_referals"));
//            txtBonusEarnCount.setText("\u20b9 " + object.getString("bonus_earned"));
//            txtExpectedBonusCount.setText("\u20b9 " + object.getString("expected_bonus"));
//            txtReferSlug.setText(jsonObject.getString("referal_slug"));
//            txtRefer.setText(jsonObject.getString("referal_text"));
//            txtReferMessage.setText(object.getString("refer"));
//            txtReferMaxSlug.setText(object.getString("reward"));

            shareText = jsonObject.getString("shareText");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSuccess(String userData, String second_message) {

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
            showError(error);
            submit.setVisibility(View.VISIBLE);
        } else {
            showError(error);
        }
    }

    @Override
    public void onShowDialog(String message) {
        if (loading != null) {
            showLoading(loading);
            submit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHideDialog() {
        if (loading != null) {
            hideLoading(loading);
            submit.setVisibility(View.VISIBLE);
        }
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
    public void onClick(@NonNull View v) {

        UserData userData = mDatabase.getUserData();
        if (v.getId() == R.id.btnInvite) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = (shareText + "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName() + "&referrer=" + userData.getReferCode());
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (v.getId() == R.id.btnReferCopy) {
            if (mDatabase != null) {
                if (mDatabase.getUserData() != null) {
                    if (mDatabase.getUserData().getReferCode() != null) {
                        copyReferRal(mDatabase.getUserData().getReferCode());
                    }
                }
            }
        } else if (v.getId() == R.id.viewMyReferral) {
            Intent intent = new Intent(getActivity(), MyReferralsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.viewReferralIncome) {
            Intent intent = new Intent(getActivity(), ReferralsIncomeActivity.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.toolbar_LinearLayout) {
            onBackPressed();
            getActivity().finish();
        }


    }
}
