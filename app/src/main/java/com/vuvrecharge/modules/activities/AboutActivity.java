package com.vuvrecharge.modules.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.vuvrecharge.R;
import com.vuvrecharge.api.ApiServices;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.utils.LocaleHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private String postUrl = "";

    @BindView(R.id.toolbar_layout)
    LinearLayout mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.bg)
    LinearLayout bg;

    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;

    String from = "";
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        title.setText(from);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView.getSettings().setSafeBrowsingEnabled(true);
        }

        switch (from) {
            case "About Us":
                postUrl = ApiServices.about_us;
                break;
            case "Terms & Conditions":
                postUrl = ApiServices.term;
                break;
            case "Privacy Policy":
                postUrl = ApiServices.privacy;
                break;
            case "Return, Refund and Cancellation policy":
                postUrl = ApiServices.return_refund;
                break;
            case "Services we offering":
                postUrl = ApiServices.services;
                break;
            case "Contact Us":
                postUrl = ApiServices.contact_us;
                break;
            case "Knowledge Base":
                postUrl = ApiServices.knowledge;
                break;
            default:
                title.setText("EG Pay");
                postUrl = from;
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(postUrl);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setHorizontalScrollbarOverlay(false);
        webView.setWebViewClient(new MyBrowser());

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "about");
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

    class MyBrowser extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(@NonNull WebView view, String Url) {
            view.loadUrl(Url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            bg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        if (webView.getUrl().equals(postUrl) || webView.getUrl().equals(postUrl + "/")) {
            super.onBackPressed();
        } else {
            webView.goBack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
    }
}