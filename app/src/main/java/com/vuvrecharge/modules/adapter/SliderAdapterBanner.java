package com.vuvrecharge.modules.adapter;

import static com.vuvrecharge.api.ApiServices.SLIDE;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.OfferSlider;
import com.vuvrecharge.modules.model.SliderData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterBanner extends RecyclerView.Adapter<SliderAdapterBanner.SliderViewHolder> {

    private Context mContext;
//    private List<String> color;
    private List<SliderData> banners;
   ItemClickListener listener;
    public SliderAdapterBanner(Context context,List<SliderData> banners1,ItemClickListener listener) {
        this.mContext = context;
//        this.color = color;
        this.banners = banners1;
        this.listener=listener;
//        if (!banners1.isEmpty()) {
//            banners.add(banners1.get(banners1.size() - 1)); // last
//            banners.addAll(banners1);                           // all
//            banners.add(banners1.get(0));                       // first
//        }
    }


    @NonNull
    @Override
    public SliderAdapterBanner.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapterBanner.SliderViewHolder holder, int position) {
        SliderData sliderData = banners.get(position);
//        Log.d("logo",color.get(position));
//        if (!color.get(position).equals("")) {
            Glide.with(mContext)
                    .load(SLIDE + sliderData.getSlide())
//                    .apply(options)
                    .into(holder.imageView);
//        }
        holder.imageView.setOnClickListener(v -> {
            try {
//                if (!sliderData.getUrl().isEmpty()) {
//                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                    CustomTabsIntent customTabsIntent = builder.build();
//                    customTabsIntent.intent.setPackage("com.android.chrome");
//                    customTabsIntent.launchUrl(mContext, Uri.parse(banners.get(position).getUrl()));
//                }
                JSONObject object = new JSONObject(sliderData.getData());

                listener.onClickListener(sliderData.getRedirection_type(),object.getString("intent_name"),object.getString("extra_data"),sliderData.getUrl());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }
    public class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public interface ItemClickListener{
        void onClickListener(String redirection_type,String intent_name,String extra_data,String link) throws ClassNotFoundException;
    }
}
