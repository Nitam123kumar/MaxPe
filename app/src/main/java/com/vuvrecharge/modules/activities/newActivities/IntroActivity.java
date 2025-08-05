package com.vuvrecharge.modules.activities.newActivities;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.vuvrecharge.api.ApiServices.term;
import static com.vuvrecharge.preferences.IntroPreferences.TAG_SLIDE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.activities.LoginActivity;
import com.vuvrecharge.modules.activities.ReferandEarnTermsActivity;
import com.vuvrecharge.modules.fragments.intro.FirstFragment;
import com.vuvrecharge.modules.fragments.intro.SecondFragment;
import com.vuvrecharge.modules.fragments.intro.ThirdFragment;
import com.vuvrecharge.preferences.IntroPreferences;
import com.vuvrecharge.utils.LocaleHelper;

import java.util.Objects;

public class IntroActivity extends AppCompatActivity {


    private FrameLayout frameLayout;
    private TextView btnNext;
    private TextView txtSkip;
    private IntroPreferences preferences;
    private int fragmentCount = 0;
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        preferences = new IntroPreferences(this);
        frameLayout = findViewById(R.id.frameLayoutIntro);
        btnNext = findViewById(R.id.btnNext);
        txtSkip = findViewById(R.id.txtSkip);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(),new FirstFragment())
                .commit();
        txtSkip.setVisibility(VISIBLE);
        txtSkip.setText("Skip");
        txtSkip.setOnClickListener(v -> {
            if (fragmentCount == 1 || fragmentCount == 0){
                preferences.setIntroStatus("true");
                startActivity(new Intent(this, LoginActivity.class));
                txtSkip.setText("Skip");
                finish();
            }else {
                preferences.setIntroStatus("false");
                Intent intent = new Intent(this, ReferandEarnTermsActivity.class);
                startActivity(intent);
                fragmentCount = 0;
                txtSkip.setText("Skip");
            }
        });

        btnNext.setOnClickListener(v -> {
            if (fragmentCount == 0){
                preferences.setIntroStatus("false");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(frameLayout.getId(), new SecondFragment())
                        .addToBackStack(null)
                        .commit();
                fragmentCount = 1;
                txtSkip.setText("Skip");
                txtSkip.setVisibility(VISIBLE);
            }else if (fragmentCount == 1){
                preferences.setIntroStatus("false");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(frameLayout.getId(), new ThirdFragment())
                        .addToBackStack(null)
                        .commit();
                fragmentCount = 2;
                txtSkip.setText("*T&C Apply");
                if (txtSkip.getText().toString().equals("*T&C Apply")){
                    txtSkip.setOnClickListener(v1 -> {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(term));
                        startActivity(i);
                    });
                }
                txtSkip.setVisibility(VISIBLE);
            }else if (fragmentCount == 2){
                if(Objects.equals(preferences.mapData().get(TAG_SLIDE), "false")){
                    preferences.setIntroStatus("true");
                    startActivity(new Intent(this, LoginActivity.class));
                    txtSkip.setText("Skip");
                    txtSkip.setVisibility(INVISIBLE);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentCount > 0){
            fragmentCount = fragmentCount - 1;
            txtSkip.setText("Skip");
        }else {
            finish();
        }
    }
}