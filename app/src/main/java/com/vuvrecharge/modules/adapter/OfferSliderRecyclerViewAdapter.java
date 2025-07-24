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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OfferSliderRecyclerViewAdapter extends RecyclerView.Adapter<OfferSliderRecyclerViewAdapter.OfferSliderViewHolder>{


    private Context context;
    private List<OfferSlider> imageList;
    private List<String> offer_Sliders;
    ItemClickListener listener;

    public OfferSliderRecyclerViewAdapter(Context context, List<String> offer_Sliders,  List<OfferSlider> imageList,ItemClickListener listener) {
        this.context = context;
        this.imageList = imageList;
        this.offer_Sliders=offer_Sliders;
        this.listener=listener;
    }

    @NonNull
    @Override
    public OfferSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.offer_slider_layout, parent, false);
        return new OfferSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferSliderViewHolder holder, int position) {
        OfferSlider offerSlider = imageList.get(position);

        String logoUrl = offerSlider.getLogo();

            logoUrl = OFFER_ZONE + logoUrl;


        Glide.with(context).load(logoUrl).into(holder.imgSlide);

        holder.itemView.setOnClickListener(v -> {
            try {
                JSONObject object = new JSONObject(offerSlider.getData());
//                JSONObject activityExtraData = new JSONObject();
                listener.onClickListener(offerSlider.getRedirection_type(),object.getString("intent_name"),object.getString("extra_data"),offerSlider.getUrl());
            } catch (JSONException e) {
                new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

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
    public interface ItemClickListener{
        void onClickListener(String redirection_type,String intent_name,String extra_data,String link) throws ClassNotFoundException;
    }
}
