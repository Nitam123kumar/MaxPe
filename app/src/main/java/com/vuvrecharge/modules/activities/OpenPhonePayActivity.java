package com.vuvrecharge.modules.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.modules.adapter.CustomNewAdapter;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenPhonePayActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    private DefaultPresenter mDefaultPresenter;

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;


    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    DefaultView defaultView;
    @BindView(R.id.list)
    ListView list;
    String phonePackage = "", amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openphonepay);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        title.setText("UPI");
        mDefaultPresenter = new DefaultPresenter(this);
        getInstalledUPIApps();
        Intent intent1 = getIntent();
        amount= intent1.getStringExtra("Amount");
    }



    @NonNull
    private ArrayList<String> getInstalledUPIApps() {
        ArrayList<String> upiList = new ArrayList<>();
        ArrayList<String> upiListName = new ArrayList<>();
        ArrayList<Drawable> myImageList = new ArrayList<>();

        Uri uri = Uri.parse(String.format("%s://%s", "upi", "pay"));
        Intent upiUriIntent = new Intent();
        upiUriIntent.setData(uri);
        PackageManager packageManager = getApplication().getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(upiUriIntent, PackageManager.MATCH_DEFAULT_ONLY);

        try {
            if (resolveInfoList != null) {
                int i = 0;
                for (ResolveInfo resolveInfo : resolveInfoList) {
                    upiList.add(resolveInfo.activityInfo.packageName);
                    Drawable icon = getPackageManager().getApplicationIcon(upiList.get(i));
                    String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(upiList.get(i), PackageManager.GET_META_DATA));
                    upiListName.add(appName);
                    myImageList.add(icon);
                    i++;
                }

            }
            CustomNewAdapter customAdapter = new CustomNewAdapter(this, upiListName, myImageList, upiList, defaultView);
            list.setAdapter(customAdapter);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return upiList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "openPhonePay");
    }

    boolean isLoading = true;


    @Override
    public void onError(String error) {
        showError(error);
    }

    @Override
    public void onSuccess(String message) {


    }

    private static int B2B_PG_REQUEST_CODE = 777;

    @Override
    public void onSuccess(String data, String data_other) {
        try {
            if (data_other.equals("PhoneList")) {
                phonePackage = data;
                mDefaultPresenter.iciciDynamicQR(device_id, amount, phonePackage);
            } else if (data_other.equals("PhonePay")) {
                JSONObject jsonObject = new JSONObject(data);
                String orderid = jsonObject.getString("orderid");
                String uri = jsonObject.getString("upi_uri");
                System.out.println("Uri==" + uri);
                System.out.println("Uri=" + phonePackage);

                Intent intent = new Intent();
                Intent upiIntent = new Intent(Intent.ACTION_VIEW);
                String uriString = uri;
                upiIntent.setData(Uri.parse(uriString.trim()));
                intent.setPackage(phonePackage);
                startActivityForResult(intent, B2B_PG_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == B2B_PG_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(OpenPhonePayActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getActivity(), "Transaction Cancelled", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onSuccessOther(String data) {

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

    @Override
    public void onClick(@NonNull View v) {
        switch(v.getId()){
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
        }
    }
}