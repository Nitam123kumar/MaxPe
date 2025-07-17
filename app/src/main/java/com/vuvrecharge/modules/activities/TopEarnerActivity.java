package com.vuvrecharge.modules.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopEarnerActivity extends AppCompatActivity {
    @BindView(R.id.top_earner_RecyclerView)
    RecyclerView top_earner_RecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_earner);
        ButterKnife.bind(this);

    }
}