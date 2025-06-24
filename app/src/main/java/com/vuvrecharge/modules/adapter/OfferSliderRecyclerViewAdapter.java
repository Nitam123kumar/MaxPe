package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;

import java.util.List;

public class OfferSliderRecyclerViewAdapter extends RecyclerView.Adapter<OfferSliderRecyclerViewAdapter.OfferSliderViewHolder>{


    private Context context;
    private List<Integer> imageList;

    public OfferSliderRecyclerViewAdapter(Context context, List<Integer> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public OfferSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.offer_slider_layout, parent, false);
        return new OfferSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferSliderViewHolder holder, int position) {
        holder.imgSlide.setImageResource(imageList.get(position));
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
