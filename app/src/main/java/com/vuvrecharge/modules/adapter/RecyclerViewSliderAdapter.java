package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.youtube_slides;

import java.util.List;

public class RecyclerViewSliderAdapter extends RecyclerView.Adapter<RecyclerViewSliderAdapter.SliderViewHolder> {

    private static Context context;
    private List<youtube_slides> imageList;

    public RecyclerViewSliderAdapter(Context context, List<youtube_slides> imageList) {
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
        youtube_slides data = imageList.get(position);
     Glide.with(context).load(data.getThumbnail()).placeholder(R.drawable.youtube_2).into(holder.imgSlide);
    holder.title.setText(data.getTitle());
// "http://192.168.0.16/vuvpayments.com/images/youtube_thumbnail/cd656116413d4cf85cbb39223cdfd224.png"
    }

    @Override
    public int getItemCount() {
       return imageList.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSlide;
        TextView title;
        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlide = itemView.findViewById(R.id.imgSlide);
            title = itemView.findViewById(R.id.title);
        }
    }
}
