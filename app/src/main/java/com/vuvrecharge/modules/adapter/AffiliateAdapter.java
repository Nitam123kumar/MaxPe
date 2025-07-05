package com.vuvrecharge.modules.adapter;

//import static com.vuvrecharge.api.ApiServices.SLIDE;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.SliderData;

import java.util.List;

public class AffiliateAdapter extends RecyclerView.Adapter<AffiliateAdapter.AffiliateViewHolder> {

    Context context;
    List<SliderData> list;

    public AffiliateAdapter(Context context, List<SliderData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AffiliateAdapter.AffiliateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AffiliateViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_layout, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AffiliateAdapter.AffiliateViewHolder holder, int position) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.no)
                .error(R.drawable.no);

//        if (!list.get(position).getSlide().isEmpty()) {
//            Glide.with(context)
//                    .load(SLIDE+list.get(position).getSlide())
//                    .apply(options)
//                    .into(holder.image_view);
//        }

        holder.image_view.setOnClickListener(v -> {
            try {
                if (!list.get(position).getUrl().isEmpty()) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.intent.setPackage("com.android.chrome");
                    customTabsIntent.launchUrl(context, Uri.parse(list.get(position).getUrl()));
                }
            }catch (ActivityNotFoundException e){
                Log.d("TAG_DATA", "instantiateItem: "+e.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AffiliateViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view;
        public AffiliateViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view = itemView.findViewById(R.id.imageView);
        }
    }
}
