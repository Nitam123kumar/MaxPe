package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;

import java.util.List;

public class RecyclerViewSliderAdapter extends RecyclerView.Adapter<RecyclerViewSliderAdapter.SliderViewHolder> {

    private Context context;
    private List<Integer> imageList;

    public RecyclerViewSliderAdapter(Context context, List<Integer> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_layout, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
      holder.imgSlide.setImageResource(imageList.get(position));
    }

    @Override
    public int getItemCount() {
       return imageList.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSlide;
        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlide = itemView.findViewById(R.id.imgSlide);
        }
    }
}
