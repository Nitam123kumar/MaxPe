package com.vuvrecharge.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vuvrecharge.R;
import com.vuvrecharge.databinding.FeedbackBinding;
import com.vuvrecharge.modules.activities.LoginActivity;
import com.vuvrecharge.preferences.UserPreferences;
import com.vuvrecharge.preferences.UserPreferencesImpl;
import com.vuvrecharge.utils.ScreenshotUtils;
import com.vuvrecharge.utils.network.CheckNet;
import com.vuvrecharge.utils.network.NetListener;
import com.vuvrecharge.utils.network.NetworkChangeReceiver;
import com.vuvrecharge.utils.permission.PermissionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;


public class BaseActivity extends AppCompatActivity implements NetListener {

    ProgressDialog progressDialog = null;

    AppCompatActivity activity;
    private CheckNet mCheckNet;
    private NetListener mNetListener;

    public UserPreferences mDatabase = new UserPreferencesImpl();

    private AlertDialog mInternetDialog;
    private static final int WIFI_ENABLE_REQUEST = 110;

    public static String fcmToken="";

    static {
        getFcmToken();
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        device_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        getFcmToken();
        FirebaseInAppMessaging.getInstance().setAutomaticDataCollectionEnabled(true);
        FirebaseInAppMessaging.getInstance();
    }

    public String device_id = "";

    private static void getFcmToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (task.getResult() != null){
                    fcmToken = task.getResult();
                }
            }
        });
    }

    protected void initializeLanguage() {
        Locale locale = new Locale(mDatabase.getAppLanguage());
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());

    }

//    public boolean isDeveloperModeEnabled() {
//        return Utils.isDevMode(getApplicationContext());
//    }

    @Override
    protected void onResume() {
        super.onResume();
        mCheckNet = CheckNet.getInstance();
        if (mCheckNet != null){
            mNetListener = this;
            if (mNetListener != null) {
                mCheckNet.addInternetConnectivityListener(mNetListener);
            }
        }

    }

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public void openDeveloperDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Developer Mode");
        builder.setIcon(R.drawable.setting_debug);
        builder.setMessage("Dear Users,\nPlease disable the developer option, Go to mobile settings, then press turn off to disable \"Developer Option\", and use MaxPe without any hurdle.\n\n" +
                "प्रिय ग्राहक,\nकृपया डेवलपर ऑप्शन को बंद करे! इसके लिए आपको मोबाइल की सेटिंग्स से डेवेलपर ऑप्शन को बंद करना होगा, जिससे आप MaxPe का इस्तेमाल आराम से कर सकते है");
        builder.setPositiveButton("Settings", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
                dialog.dismiss();
                dialog.cancel();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            preferences = getSharedPreferences("debug",Context.MODE_PRIVATE);
            editor = preferences.edit();
            preferences.getString("state", "");
            editor.putString("state", "cancel");
            editor.apply();
            dialog.cancel();
            dialog.dismiss();
        });

        builder.setNeutralButton("Need Help?", (dialog, which) -> {
            dialog.cancel();
            dialog.dismiss();
            makeACall("+917390903230");
        });
        builder.show();
    }

    public void makeACall(String phoneNo) {
        isPermissionGranted();
        String callInfo = "tel:" + phoneNo;
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(callInfo));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        startActivity(callIntent);
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public void backAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity
                .getResources().getText(R.string.exit));
        builder.setMessage(activity
                .getResources().getText(R.string.sure));

        builder.setPositiveButton(activity
                .getResources().getText(R.string.yes), (dialog, which) -> new Handler().postDelayed(() -> {
                    // TODO Auto-generated method stub
                    if (runnable != null) {
                        handler.removeCallbacks(runnable);
                    }
                    finishAffinity();
                    System.exit(0);
                }, 000));
        builder.setNegativeButton(activity
                .getResources().getText(R.string.no), (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();

        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.parseColor("#D6483F"));

        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#008000"));

    }

    public void sessionExpired(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("Login", (dialogInterface, i) -> new Handler().postDelayed(() -> {
            // TODO Auto-generated method stub
            mDatabase.clearUser();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        }, 000));
        AlertDialog dialog = builder.create();
        dialog.show();

        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.parseColor("#D6483F"));

    }

    public void makeLogout(){
        try {

            BottomSheetDialog dialogLogout = new BottomSheetDialog(getActivity(),R.style.AppBottomSheetDialogTheme);
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.logout_layout,null,false);
            ImageView cancelImg=view.findViewById(R.id.cancel_img);
            TextView logoutYes=view.findViewById(R.id.yes);
            TextView logoutNo=view.findViewById(R.id.no);
            dialogLogout.setContentView(view);
            dialogLogout.setCancelable(false);
            changeStatusBarColor(dialogLogout);

            bottomSheet = dialogLogout.findViewById(com.denzcoskun.imageslider.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }


            logoutYes.setOnClickListener(v -> {
                mDatabase.clearUser();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();


            });
            logoutNo.setOnClickListener(v -> {
                dialogLogout.cancel();

            });
            cancelImg.setOnClickListener(v -> {
                dialogLogout.cancel();

            });

            dialogLogout.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeDeleteAccount() {

        try {

            BottomSheetDialog dialogDeleteAccount = new BottomSheetDialog(getActivity(),R.style.AppBottomSheetDialogTheme);
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.account_delete_layout,null,false);
            ImageView cancelImg=view.findViewById(R.id.cancel_img);
            TextView accountDeleteYes=view.findViewById(R.id.accountDelete);
            TextView accountDeleteNo=view.findViewById(R.id.no);
            dialogDeleteAccount.setContentView(view);
            dialogDeleteAccount.setCancelable(false);
            changeStatusBarColor(dialogDeleteAccount);

            bottomSheet = dialogDeleteAccount.findViewById(com.denzcoskun.imageslider.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }


            accountDeleteYes.setOnClickListener(v -> {
//                    mDatabase.clearUser();
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    getActivity().finish();


            });
            accountDeleteNo.setOnClickListener(v -> {
                dialogDeleteAccount.cancel();

            });
            cancelImg.setOnClickListener(v -> {
                dialogDeleteAccount.cancel();

            });

            dialogDeleteAccount.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    LinearLayout no_internet;
    TextView retry;
    String string_from = "";
    String click = "";
    public BottomSheetDialog dialog = null;
    public FrameLayout bottomSheet = null;
    public TextView submit;
    public LinearLayout loading_dialog = null;
    Handler handler = new Handler();
    TSnackbar snackbar = null;

    public void setLayout(@NonNull LinearLayout no_internet, TextView retry, String string_from) {
        if (no_internet == null || retry == null) {
            return;
        }
        if (NetworkChangeReceiver.isNetworkConnected(getApplicationContext())){
            no_internet.setVisibility(View.GONE);
        }
        this.no_internet = no_internet;
        this.retry = retry;
        this.string_from = string_from;
        this.retry.setOnClickListener(v -> {
            no_internet.setVisibility(View.GONE);
            if (string_from.equals("main")) {
                click = "yes";
                recreate();
            } else {
                mCheckNet.onNetworkChange(NetworkChangeReceiver.isNetworkConnected(getApplicationContext()));
            }
        });
        this.no_internet.setOnClickListener(v -> {
            this.no_internet.setClickable(false);
            this.no_internet.setEnabled(false);
        });
    }

    PermissionUtil permissionUtil = new PermissionUtil();

    public static final int SELECT_CALL = 99, SELECT_CONTACT = 100, SELECT_FILE = 101, REQUEST_APP_DETAILS = 111, SELECT_PICTURE = 201, CROP_IMAGE = 301,
            LOCATION_CHANGE = 401,EMAIL_PERMISSION = 123, ALL_PERMISSION = 401, MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 501;
    public boolean selectContact() {
        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(getActivity(), permissionUtil.getContactPermissions()))
                return true;
            else {
                ActivityCompat.requestPermissions(getActivity(), permissionUtil.getContactPermissions(), SELECT_CONTACT);
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean selectCall() {
        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(getActivity(), permissionUtil.getCallPermissions()))
                return true;
            else {
                ActivityCompat.requestPermissions(getActivity(), permissionUtil.getCallPermissions(), SELECT_CALL);
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean selectEmail() {
        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(getActivity(), permissionUtil.getEmailPermissions()))
                return true;
            else {
                ActivityCompat.requestPermissions(getActivity(), permissionUtil.getEmailPermissions(), EMAIL_PERMISSION);
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean selectFile() {
        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(getActivity(), permissionUtil.getGalleryPermissions())) {
                return true;
            }else {
                ActivityCompat.requestPermissions(getActivity(), permissionUtil.getGalleryPermissions(), SELECT_FILE);
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean checkLocation() {
        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(getActivity(), permissionUtil.getLocationPermissions()))
                return true;
            else {
                ActivityCompat.requestPermissions(getActivity(), permissionUtil.getLocationPermissions(), LOCATION_CHANGE);
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean checkFitPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);
            return false;
        } else {
            return true;
        }
    }

    public boolean checkAllow() {
        if (permissionUtil.checkMarshMellowPermission()) {
            return permissionUtil.verifyPermissions(getActivity(), permissionUtil.getGalleryPermissions());
        } else {
            return true;
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    public void copyReferRal(String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(getActivity(), "Copied Code "+text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (requestCode == SELECT_CONTACT) {
            for (String result : permissions) {
                if (ActivityCompat.checkSelfPermission(getActivity(), result) != PackageManager.PERMISSION_GRANTED) {
                    if (getActivity() == null) {
                        return;
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), result)) {
                        selectContact();
                    }/* else {
                        openAppSetting();
                    }*/
                    return;
                }
            }
        }

      if (requestCode == SELECT_FILE) {
            for (String result : permissions) {
                if (ActivityCompat.checkSelfPermission(getActivity(), result) != PackageManager.PERMISSION_GRANTED) {
                    if (getActivity() == null) {
                        return;
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), result)) {
                        selectContact();
                    }/* else {
                        openAppSetting();
                    }*/
                    return;
                }
            }
        }

      if (requestCode == SELECT_CALL) {
            for (String result : permissions) {
                if (ActivityCompat.checkSelfPermission(getActivity(), result) != PackageManager.PERMISSION_GRANTED) {
                    if (getActivity() == null) {
                        return;
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), result)) {
                        selectCall();
                    }/* else {
                        openAppSetting();
                    }*/
                    return;
                }
            }
        }
        if (requestCode == EMAIL_PERMISSION) {
            for (String result : permissions) {
                if (ActivityCompat.checkSelfPermission(getActivity(), result) != PackageManager.PERMISSION_GRANTED) {
                    if (getActivity() == null) {
                        return;
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), result)) {
                        selectEmail();
                    }/* else {
                        openAppSetting();
                    }*/
                    return;
                }
            }
        }
      if (requestCode == LOCATION_CHANGE) {
            for (String result : permissions) {
                if (ActivityCompat.checkSelfPermission(getActivity(), result) != PackageManager.PERMISSION_GRANTED) {
                    if (getActivity() == null) {
                        return;
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), result)) {
                        checkLocation();
                    }/* else {
                        openAppSetting();
                    }*/
                    return;
                }
            }
        }
      if (requestCode == MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION) {
            for (String result : permissions) {
                if (ActivityCompat.checkSelfPermission(getActivity(), result) != PackageManager.PERMISSION_GRANTED) {
                    if (getActivity() == null) {
                        return;
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), result)) {
                        checkFitPermission();
                    }/* else {
//                        openAppSetting();
                    }*/
                    return;
                }
            }
        }
    }

    public void copyTextMain(String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
    }

    public void openAppSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity
                .getResources().getText(R.string.permission));
        builder.setPositiveButton(activity
                .getResources().getText(R.string.allow), (dialog, which) -> new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        //  Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_APP_DETAILS);
                    }
                }, 000));

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.parseColor("#D6483F"));
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#008000"));
    }

    public boolean logout_click = false;
    
    public void showLogoutToast() {
        int toastDurationInMilliSeconds = 2000;
        showToast("Are you sure for logout? Click Again");
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                logout_click = true;
                showToast("Are you sure for logout? Click Again");

            }

            public void onFinish() {
                logout_click = false;
            }
        };

        toastCountDown.start();
    }

    public void showShik() {
        TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), "Snacking Left & Right", TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setDuration(TSnackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        final TSnackbar.SnackbarLayout snackBarView = (TSnackbar.SnackbarLayout) snackbar.getView();
        TSnackbar.SnackbarLayout.LayoutParams params = (TSnackbar.SnackbarLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();
        params.setMargins(params.leftMargin + 100,
                params.topMargin + 150,
                params.rightMargin + 100,
                params.bottomMargin + 100);
        snackBarView.getChildAt(0).setLayoutParams(params);
        snackbarView.setBackgroundColor(Color.parseColor("#00000000"));
        TextView textView = snackbarView.findViewById(com.denzcoskun.imageslider.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.corner_primary_gr);
        textView.setPadding(10, 10, 10, 10);
        textView.setGravity(Gravity.CENTER);
        snackbar.show();
    }

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            setTitle("");
        }
    }

    public void setToolbarFalse(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setTitle("");
        }
    }

    public void setToolbarWhite(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            setTitle("");
        }
    }

    public void setToolbarBlack(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            setTitle("");

        }
    }

    public void hideNoInternetDialog() {
        if (mInternetDialog != null && mInternetDialog.isShowing()) {
            mInternetDialog.dismiss();
        }
    }

    private void showNoInternetDialog() {

        if (mInternetDialog != null && mInternetDialog.isShowing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getText(R.string.no_internet));
        builder.setCancelable(false);
        builder.setMessage(getResources().getText(R.string.no_network_message));
        builder.setPositiveButton(getResources().getText(R.string.enable), (dialog_, which) -> {
            Intent gpsOptionsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivityForResult(gpsOptionsIntent, WIFI_ENABLE_REQUEST);
        });
        mInternetDialog = builder.create();
        if (!isFinishing()) {
            mInternetDialog.show();
        }
    }

    public void onShowMessageDialogBase(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton(getResources().getText(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog_, int which) {
                dialog_.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onShowMessageDialogActivity(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void showDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.layout_process_bar));
        }
        progressDialog.setMessage(message);
        if (!isFinishing()) {
            progressDialog.show();
        }
    }

    protected void showLoading(@NonNull LinearLayout loading) {
        if (loading != null){
            loading.setVisibility(View.VISIBLE);
        }
    }

    protected void hideLoading(@NonNull LinearLayout loading) {
        if (loading != null){
            loading.setVisibility(View.GONE);
        }
    }

    protected void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void hideAllDialog() {
        hideNoInternetDialog();
        hideDialog();
    }

    protected void showToast(String message) {
        try {
            if (message == null) {
                message = "";
            }
            handler.removeCallbacks(runnable);
            changeStatusBarColorSuccess();
            snackbar = TSnackbar.make(findViewById(android.R.id.content), message, TSnackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.setDuration(TSnackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            final TSnackbar.SnackbarLayout snackBarView = (TSnackbar.SnackbarLayout) snackbar.getView();
            TSnackbar.SnackbarLayout.LayoutParams params = (TSnackbar.SnackbarLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();
            params.setMargins(0,
                    0,
                    0,
                    0);
            snackBarView.getChildAt(0).setLayoutParams(params);
            snackbarView.setBackgroundColor(getResources().getColor(R.color.green_end));
            TextView textView = snackbarView.findViewById(com.denzcoskun.imageslider.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(5);
            textView.setPadding(3, 3, 3, 3);
            textView.setGravity(Gravity.CENTER);
            snackbar.show();
            new Handler(Objects.requireNonNull(Looper.myLooper())).postDelayed(this::changeStatusBarColorNormal,2150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showToast(FrameLayout view, String message) {
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        try {
            if (message == null) {
                message = "";
            }
            handler.removeCallbacks(runnable_dialog);
            changeStatusBarColorDialogSuccess();
            snackbar = TSnackbar.make(view, message, TSnackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.setDuration(TSnackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            final TSnackbar.SnackbarLayout snackBarView = (TSnackbar.SnackbarLayout) snackbar.getView();
            TSnackbar.SnackbarLayout.LayoutParams params = (TSnackbar.SnackbarLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();
            params.setMargins(0,
                    0,
                    0,
                    0);
            snackBarView.getChildAt(0).setLayoutParams(params);
            snackbarView.setBackgroundColor(getResources().getColor(R.color.green_end));
            TextView textView = snackbarView.findViewById(com.denzcoskun.imageslider.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(5);
            textView.setPadding(3, 3, 3, 3);
            textView.setGravity(Gravity.CENTER);
            snackbar.show();

            new Handler(Objects.requireNonNull(Looper.myLooper())).postDelayed(this::changeStatusBarColorNormal,2150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Toast custom = null;
    CountDownTimer toastCountDown = null;
    protected void showErrorR(String message) {
        if (message == null) {
            message = "";
        }
        try {
            if (custom != null) {
                custom.cancel();
            }
            if (toastCountDown != null) {
                toastCountDown.cancel();
            }
            custom = Toasty.custom(getActivity(), message + " ",
                    R.drawable.cross_new, es.dmoral.toasty.R.color.errorColor, 5000, false,
                    true);
            custom.setGravity(Gravity.TOP, 0, 10);
            toastCountDown = new CountDownTimer(5000, 1000 /*Tick duration*/) {
                public void onTick(long millisUntilFinished) {
                    custom.show();
                }

                public void onFinish() {
                    custom.cancel();
                }
            };
            custom.show();
            toastCountDown.start();
            new Handler(Objects.requireNonNull(Looper.myLooper())).postDelayed(this::changeStatusBarColorNormal,2150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showError(String message) {
        try {
            if (message == null) {
                message = "";
            }

            handler.removeCallbacks(runnable);
            changeStatusBarColorError();

            snackbar = TSnackbar.make(findViewById(android.R.id.content), message, TSnackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.WHITE);

            View snackbarView = snackbar.getView();

            // Set background color
            snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.bgg_e));

            // Access Tsnackbar text view safely
            TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            if (textView != null) {
                textView.setTextColor(Color.WHITE);
                textView.setMaxLines(5);
                textView.setPadding(16, 16, 16, 16);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            // Show the Tsnackbar
            snackbar.show();


            new Handler(Objects.requireNonNull(Looper.myLooper()))
                    .postDelayed(this::changeStatusBarColorNormal, 2150);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void showPending(String message) {
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        try {
            if (message == null) {
                message = "";
            }
            handler.removeCallbacks(runnable);
            changeStatusBarColorProcess();
            snackbar = TSnackbar.make(findViewById(android.R.id.content), message, TSnackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.setDuration(TSnackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            final TSnackbar.SnackbarLayout snackBarView = (TSnackbar.SnackbarLayout) snackbar.getView();
            TSnackbar.SnackbarLayout.LayoutParams params = (TSnackbar.SnackbarLayout.LayoutParams)
                    snackBarView.getChildAt(1).getLayoutParams();
            params.setMargins(0,
                    0,
                    0,
                    0);
            snackBarView.getChildAt(1).setLayoutParams(params);
            snackBarView.setOverScrollMode(1);
            snackbarView.setBackgroundColor(getResources().getColor(R.color.pending));
            TextView textView = snackbarView.findViewById(com.denzcoskun.imageslider.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(5);
            textView.setPadding(3, 0, 3, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            snackbar.show();
            new Handler(Objects.requireNonNull(Looper.myLooper())).postDelayed(this::changeStatusBarColorNormal,2150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showError(FrameLayout view, String message) {
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        try {
            if (message == null) {
                message = "";
            }
            handler.removeCallbacks(runnable_dialog);
            changeStatusBarColorDialogError();
            snackbar = TSnackbar.make(view, message, TSnackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.setDuration(TSnackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            final TSnackbar.SnackbarLayout snackBarView = (TSnackbar.SnackbarLayout) snackbar.getView();
            TSnackbar.SnackbarLayout.LayoutParams params = (TSnackbar.SnackbarLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();
            params.setMargins(0,
                    0,
                    0,
                    0);
            snackBarView.getChildAt(0).setLayoutParams(params);
            snackBarView.setOverScrollMode(1);
            snackbarView.setBackgroundColor(getResources().getColor(R.color.bgg_e));
            TextView textView = snackbarView.findViewById(com.denzcoskun.imageslider.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(5);
            textView.setPadding(3, 8, 3, 3);
            textView.setGravity(Gravity.CENTER);
            snackbar.show();
            new Handler(Objects.requireNonNull(Looper.myLooper())).postDelayed(this::changeStatusBarColorNormal,2150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void changeStatusBarColor(BottomSheetDialog dialog) {
        try {
            Window win = dialog.getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.colorPrimaryB));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatusBarColorDialogError() {
        try {
            Window win = dialog.getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.bgg_e));
            }
            handler.postDelayed(runnable_dialog, 2150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatusBarColorDialogSuccess() {
        try {
            Window win = dialog.getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.green_end));
            }

            handler.postDelayed(runnable_dialog, 2150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatusBarColor() {
        try {
            Window win = getWindow();
            if (win != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    win.setStatusBarColor(getResources().getColor(R.color.colorPrimaryB));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void statusBarColor() {
        try {
            Window win = getWindow();
            if (win != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    win.setStatusBarColor(getResources().getColor(R.color.white));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatusBarColorError() {
        try {
            Window win = getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.bgg_e));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Runnable runnable = () -> {
        snackbar.dismiss();
        changeStatusBarColor();
    };
    Runnable runnable_dialog = () -> {
        snackbar.dismiss();
        changeStatusBarColor(dialog);
    };

    public void changeStatusBarColorGreen() {
        try {
            Window win = getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.success));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatusBarColorGreen1() {
        try {
            Window win = getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.success1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatusBarColorRed() {
        try {
            Window win = getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.failed1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void changeStatusBarColorProcess() {
        try {
            Window win = getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.pending1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatusBarColorNormal() {
        try {
            Window win = getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.colorPrimaryB));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatusBarColorSuccess() {
        try {
            Window win = getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.green_end));
            }

            handler.postDelayed(runnable, 2150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideSoftKeyboard() {
        new Handler().postDelayed(() -> {
            try {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 200);
    }

    public Void hideKeyBoard(@NonNull EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        return null;
    }

    public void showSoftKeyboard(@NonNull final EditText appCompatEditText) {
        appCompatEditText.postDelayed(() -> {
            try {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(appCompatEditText, InputMethodManager.SHOW_IMPLICIT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 10);
    }

    public void printLog(@NonNull String message) {
        if (message.isEmpty()) {
            Log.i(": message : ", "empty");
        } else {
            Log.i(": message : ", message);
        }
    }

    public Typeface getFonts() {
        return Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Regular.ttf");
    }

    public Typeface getFontsBold() {
        return Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Bold.ttf");
    }

    public String getCurrentId() {
        return mDatabase.getUserData().getMobile();
    }

    private void clearApplicationCache() {
        File dir = getCacheDir();

        if (dir != null && dir.isDirectory()) {
            try {
                ArrayList<File> stack = new ArrayList<File>();

                // Initialise the list
                File[] children = dir.listFiles();
                assert children != null;
                for (File child : children) {
                    stack.add(child);
                }

                while (!stack.isEmpty()) {
                    Log.v("TAG", "Clearing the stack - " + stack.size());
                    File f = stack.get(stack.size() - 1);
                    if (f.isDirectory()) {
                        boolean empty = f.delete();

                        if (empty == false) {
                            File[] files = f.listFiles();
                            if (files.length != 0) {
                                for (File tmp : files) {
                                    stack.add(tmp);
                                }
                            }
                        } else {
                            stack.remove(stack.size() - 1);
                        }
                    } else {
                        f.delete();
                        stack.remove(stack.size() - 1);
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "Failed to clean the cache");
            }
        }
    }

    public void fullScreen() {
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setStatusBarColor(getResources().getColor(R.color.transparent));

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);
    }

    public void fullScreenNew() {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.white));
        final int flags;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE;
        } else {
            flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE;
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(flags);
        transparentStatusAndNavigation();
    }

    public void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void shareIt() {
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + " Download From Play Store. The download link is given bellow. \nhttps://play.google.com/store/apps/details?id=" + getPackageName() + "&referrer=" + mDatabase.getUsername());
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences("IQTradePref", Context.MODE_PRIVATE);
    }

    public void saveReferrerCode(String referrer) {
        getSharedPreferences().edit().putString("USER_REFERRER", referrer).apply();
    }

    public String getReferrerCode() {
        return getSharedPreferences().getString("USER_REFERRER", "");
    }

    public void rateUs() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
    }

    public void takeScreenshot(View view) {
        Bitmap b = null;
        b = ScreenshotUtils.takeScreenshotForView(view);
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), b, "Title", null);
            Uri imageUri = Uri.parse(path);
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
        }
    }

    public void takeScreenshotPDF(View view) {
        Bitmap b = null;
        b = ScreenshotUtils.takeScreenshotForView(view);
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), b, "Title", null);
            Uri imageUri = Uri.parse(path);
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
        }
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            if (string_from.equals("main")) {
                if (click.equals("yes")) {
                    no_internet.setVisibility(View.GONE);
                }
                click = "no";
            }
            if (no_internet != null) {
                no_internet.setVisibility(View.GONE);
            }
        } else {
            if (no_internet == null || retry == null) {
                return;
            }else {
                no_internet.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WIFI_ENABLE_REQUEST) {
            new Handler().postDelayed(() -> mCheckNet.addInternetConnectivityListener(mNetListener), 000);
        }
    }

}
