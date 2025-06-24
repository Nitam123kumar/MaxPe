package com.vuvrecharge.modules.fragments.intro;

import static com.vuvrecharge.preferences.IntroPreferences.TAG_SLIDE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.activities.LoginActivity;
import com.vuvrecharge.modules.activities.ReferandEarnTermsActivity;
import com.vuvrecharge.preferences.IntroPreferences;

import java.util.Objects;

import butterknife.ButterKnife;

public class ThirdFragment extends Fragment {

//    private IntroPreferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

//        preferences = new IntroPreferences(requireContext());
//
//        view.findViewById(R.id.btnNext)
//                .setOnClickListener(v -> {
//
//                });
//
//        view.findViewById(R.id.txtSkip)
//                .setOnClickListener(v -> {
//                    preferences.setIntroStatus("false");
//                    Intent intent = new Intent(getActivity(), ReferandEarnTermsActivity.class);
//                    startActivity(intent);
//                });


        return view;
    }
}