package com.vuvrecharge.modules.adapter;

import static com.vuvrecharge.api.ApiServices.OFFER_ZONE;
import static com.vuvrecharge.api.ApiServices.YOUTUBE_IMAGE_PATH;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.OfferSlider;
import com.vuvrecharge.modules.model.YoutubeSlides;

import java.util.List;

public class OfferSliderRecyclerViewAdapter extends RecyclerView.Adapter<OfferSliderRecyclerViewAdapter.OfferSliderViewHolder>{


    private Context context;
    private List<OfferSlider> imageList;
    private List<String> offer_Sliders;

    public OfferSliderRecyclerViewAdapter(Context context, List<String> offer_Sliders,  List<OfferSlider> imageList) {
        this.context = context;
        this.imageList = imageList;
        this.offer_Sliders=offer_Sliders;
    }

    @NonNull
    @Override
    public OfferSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.offer_slider_layout, parent, false);
        return new OfferSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferSliderViewHolder holder, int position) {
        OfferSlider data = imageList.get(position);

        String logoUrl = data.getLogo();
        if (!logoUrl.startsWith("http")) {
            logoUrl = OFFER_ZONE + logoUrl;
        }

        Glide.with(context).load(logoUrl).placeholder(R.drawable.airtel_svg).into(holder.imgSlide);

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class OfferSliderViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSlide;
        OfferSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlide = itemView.findViewById(R.id.imgOfferSlide);
        }
    }
}
