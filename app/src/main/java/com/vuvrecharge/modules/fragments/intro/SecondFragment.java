package com.vuvrecharge.modules.fragments.intro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.activities.LoginActivity;
import com.vuvrecharge.preferences.IntroPreferences;

import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;


public class SecondFragment extends Fragment {


    GifImageView imageView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

//        frameLayout = requireActivity().findViewById(R.id.frameLayoutIntro);

        imageView = view.findViewById(R.id.imgHandWithCard);
        imageView.setImageResource(R.drawable.cashback_animation);
//        imageView.setGifImageResource(R.drawable.cashback_animation);
//        Glide.with(requireContext())
//                        .load(R.drawable.cashback_animation)
//                                .into(imageView);
//        view.findViewById(R.id.txtSkip)
//                .setOnClickListener(v -> {
//                    preferences.setIntroStatus("true");
//                    startActivity(new Intent(requireContext(), LoginActivity.class));
//                    requireActivity().finish();
//                });
//        view.findViewById(R.id.btnNext)
//                .setOnClickListener(v ->{
//                    preferences.setIntroStatus("false");
//                    getChildFragmentManager()
//                            .beginTransaction()
//                            .replace(frameLayout.getId(), new ThirdFragment())
//                            .addToBackStack(null)
//                            .commit();
//                });

        return view;
    }
}