package com.vuvrecharge.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.vuvrecharge.R;
import com.vuvrecharge.preferences.UserPreferences;
import com.vuvrecharge.preferences.UserPreferencesImpl;
import com.vuvrecharge.utils.permission.PermissionUtil;

import static android.content.Context.INPUT_METHOD_SERVICE;

import es.dmoral.toasty.Toasty;

public abstract class BaseFragment extends Fragment {

    ProgressDialog progressDialog = null;
    public UserPreferences mDatabase = new UserPreferencesImpl();
    Activity mActivity;
    Context mContext;
    public String device_id = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState) {
        View view = provideYourFragmentView(inflater, parent, savedInstanseState);
        if (getActivity()!= null){
            mActivity = getActivity();
        }
        if (getContext() != null){
            mContext = getContext();
        }
        device_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return view;
    }

    public abstract View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void showDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.layout_process_bar));
        }
        progressDialog.setMessage(message);

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    protected void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void hideAllDialog() {
        hideDialog();
    }

    protected void showToast(FrameLayout view, String message) {
        if (message == null) {
            message = "";
        }
        handler.removeCallbacks(runnable);
        changeStatusBarColorSuccess();
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        final Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        Snackbar.SnackbarLayout.LayoutParams params = (Snackbar.SnackbarLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        snackBarView.getChildAt(0).setLayoutParams(params);
        snackbarView.setBackgroundColor(getResources().getColor(R.color.green_end));
        TextView textView = snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.sucess);
        textView.setMaxLines(5);
        textView.setPadding(3, 3, 3, 3);
        textView.setGravity(Gravity.CENTER);
        snackbar.show();

    }

    protected void showError(FrameLayout view, String message) {
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (message == null) {
            message = "";
        }
        handler.removeCallbacks(runnable);
        changeStatusBarColorError();
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        final Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        Snackbar.SnackbarLayout.LayoutParams params = (Snackbar.SnackbarLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        snackBarView.getChildAt(0).setLayoutParams(params);
        snackBarView.setOverScrollMode(1);
        snackbarView.setBackgroundColor(getResources().getColor(R.color.bgg_e));
        TextView textView = snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.error);
        textView.setMaxLines(5);
        textView.setPadding(3, 3, 3, 3);
        textView.setGravity(Gravity.CENTER);
        snackbar.show();
    }

    protected void showToast(ViewGroup view, String message) {
        if (message == null) {
            message = "";
        }
        handler.removeCallbacks(runnable);
        changeStatusBarColorSuccess();
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        @SuppressLint("RestrictedApi") final Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        Snackbar.SnackbarLayout.LayoutParams params = (Snackbar.SnackbarLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        snackBarView.getChildAt(0).setLayoutParams(params);
        snackbarView.setBackgroundColor(getResources().getColor(R.color.green_end));
        TextView textView = snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.sucess);
        textView.setMaxLines(5);
        textView.setPadding(3, 3, 3, 3);
        textView.setGravity(Gravity.CENTER);
        snackbar.show();
    }

    protected void showError(ViewGroup view, String message) {
        try {
            if (message == null) {
                message = "";
            }
            handler.removeCallbacks(runnable);
            changeStatusBarColorError();
            snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.setDuration(Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            @SuppressLint("RestrictedApi") final Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
            Snackbar.SnackbarLayout.LayoutParams params = (Snackbar.SnackbarLayout.LayoutParams) snackBarView.getChildAt(0).getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            snackBarView.getChildAt(0).setLayoutParams(params);
            snackBarView.setOverScrollMode(1);
            snackbarView.setBackgroundColor(getResources().getColor(R.color.bgg_e));
            TextView textView = snackbarView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.error);
            textView.setMaxLines(5);
            textView.setPadding(3, 3, 3, 3);
            textView.setGravity(Gravity.CENTER);
            snackbar.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    Toast custom = null;
    CountDownTimer toastCountDown = null;
    protected void showError(Context mContext, String message) {
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
            custom = Toasty.custom(mContext, message + " ",
                    R.drawable.cross_new, R.color.errorColor, 5000, false,
                    true);
            custom.setGravity(Gravity.TOP, 0, 50);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BottomSheetDialog dialog = null;
    public FrameLayout bottomSheet = null;
    public TextView submit;
    public LinearLayout loading_dialog = null;

    public void changeStatusBarColor() {
        if (getActivity() != null){
            if (requireActivity().getWindow() != null) {
                new Handler().postDelayed(() -> requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryB)), 200);
            }
        }
    }

    public void changeStatusBarColorError() {
        if (getActivity() != null){
            Window win = getActivity().getWindow();
            if (win != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    new Handler().postDelayed(() -> win.setStatusBarColor(getResources().getColor(R.color.bgg_e)), 200);

                }
            }
        }
        handler.postDelayed(runnable, 2150);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            snackbar.dismiss();
            changeStatusBarColor();
        }
    };

    Runnable runnable_dialog = new Runnable() {
        @Override
        public void run() {
            snackbar.dismiss();
            changeStatusBarColor(dialog);
        }
    };

    Snackbar snackbar = null;
    public void changeStatusBarColorSuccess() {
        if (getActivity() != null){
            Window win = getActivity().getWindow();
            if (win != null) {
                win.setStatusBarColor(getResources().getColor(R.color.green_end));
            }
        }
        handler.postDelayed(runnable, 2000);
    }

    Handler handler = new Handler();
    protected void showLoading(@NonNull LinearLayout loading) {
        loading.setVisibility(View.VISIBLE);
    }

    protected void hideLoading(@NonNull LinearLayout loading) {
        loading.setVisibility(View.GONE);
    }

    public Void hideKeyBoard(Context mContext, EditText editText) {
        if (editText != null) {
            if (mContext != null) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            }
        }
        return null;
    }

    public void showSoftKeyboard(Context mContext, final EditText editText) {
        if (editText != null) {
            editText.postDelayed(() -> {
                try {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, 000);

        }

    }

    public void printLog(@NonNull String message) {
        if (message.isEmpty()) {
            Log.i(": message : ", "empty");
        } else {
            Log.i(": message : ", message);
        }
    }

    public void changeStatusBarColor(@NonNull BottomSheetDialog dialog) {
        Window win = dialog.getWindow();
        if (win != null) {
            win.setStatusBarColor(getResources().getColor(R.color.colorPrimaryA));
        }
    }

    public void shareIt(String share_id) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + " Download From Play Store. The download link is given bellow. \nhttps://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + "&referrer=" + share_id);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void shareIt(@NonNull Context mContext) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + " Download From Play Store. The download link is given bellow. \nhttps://play.google.com/store/apps/details?id=" + mContext.getPackageName() + "&referrer=8058625069"  /*+mDatabase.getUsername()*/);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void rateUs() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
    }

    PermissionUtil permissionUtil = new PermissionUtil();

    private static final int OPEN_CAMERA = 101, SELECT_FILE = 201, SELECT_PICTURE = 301,
            CROP_IMAGE = 401, CHECK_ALLOW = 501;

    public boolean openCamera() {
        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(getActivity(), permissionUtil.getCameraPermissions()))
                return true;
            else
                ActivityCompat.requestPermissions(getActivity(), permissionUtil.getCameraPermissions(), OPEN_CAMERA);
            return false;
        } else {
            return true;
        }
    }

    public boolean selectFile() {
        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(getActivity(), permissionUtil.getGalleryPermissions()))
                return true;
            else {
                ActivityCompat.requestPermissions(getActivity(), permissionUtil.getGalleryPermissions(), SELECT_FILE);
                return false;
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       if (requestCode == SELECT_FILE) {
            for (String result : permissions) {
                if (ActivityCompat.checkSelfPermission(requireContext(), result) != PackageManager.PERMISSION_GRANTED) {
                    if (getActivity() == null) {
                        return;
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), result)) {
                        selectFile();
                    } else {
                        openAppSetting();
                    }
                    return;
                }
            }
        } else if (requestCode == OPEN_CAMERA) {
            for (String result : permissions) {
                if (ActivityCompat.checkSelfPermission(getActivity(), result) != PackageManager.PERMISSION_GRANTED) {
                    if (getActivity() == null) {
                        return;
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), result)) {
                        openCamera();
                    } else {
                        openAppSetting();
                    }
                    return;
                }
            }
        }
    }

    public void openAppSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(mActivity
                .getResources().getText(R.string.permission));
        builder.setPositiveButton(mActivity
                .getResources().getText(R.string.allow), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }, 000);
            }
        });
        builder.setNegativeButton(mActivity
                .getResources().getText(R.string.no), (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();

        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.parseColor("#D6483F"));

        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#008000"));

    }

}
