package com.vuvrecharge.modules.adapter;

import static com.vuvrecharge.api.ApiServices.OTT_SLIDES;
import static com.vuvrecharge.api.ApiServices.YOUTUBE_IMAGE_PATH;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.OTTSubscriptionsData;

import java.util.List;

public class OTTSubscriptionsAdapter extends RecyclerView.Adapter<OTTSubscriptionsAdapter.ViewHolder> {

    private Context mContext;
    List<OTTSubscriptionsData> ottSubscriptionsDataList;
    List<String> ott_slides;

    public OTTSubscriptionsAdapter(Context context,List<OTTSubscriptionsData> ottSubscriptionsDataList,List<String> ott_slides){
        this.mContext=context;
        this.ottSubscriptionsDataList=ottSubscriptionsDataList;
        this.ott_slides=ott_slides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater.from(mContext).inflate(R.layout.ott_service_item_layout,parent,false);
        return new ViewHolder(layout);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OTTSubscriptionsData ottData=ottSubscriptionsDataList.get(position);

        String ottLogo = ottData.getLogo();
        if (!ottLogo.startsWith("http")) {
            ottLogo = OTT_SLIDES + ottLogo;
        }

        Glide.with(mContext).load(ottLogo).placeholder(R.drawable.sony_live_logo).into(holder.ottLogo);
        Log.d("imageLogo",ottLogo);
        holder.ottTitle.setText(ottData.getTitle());

    }

    @Override
    public int getItemCount() {
        return ottSubscriptionsDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ottLogo;
        TextView ottTitle;
        ViewHolder(@Nullable View itemVIew){
            super(itemVIew);
            ottLogo=itemVIew.findViewById(R.id.imgLogo);
            ottTitle=itemVIew.findViewById(R.id.txtTitle);
        }

    }

}
