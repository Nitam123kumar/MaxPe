package com.vuvrecharge.modules.adapter;

import static com.vuvrecharge.api.ApiServices.YOUTUBE_IMAGE_PATH;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.YoutubeSlides;
import com.vuvrecharge.utils.SvgSoftwareLayerSetter;

import java.util.List;

public class RecyclerViewSliderAdapter extends RecyclerView.Adapter<RecyclerViewSliderAdapter.SliderViewHolder> {

    private final Context context;
    private Activity mActivity;
    private List<YoutubeSlides> imageList;
    private List<String> youtube_Sliders;
    private RequestBuilder<PictureDrawable> requestBuilder;
    public RecyclerViewSliderAdapter(Context context,Activity activity,List<YoutubeSlides> imageList) {
        this.context = context;
        this.mActivity = activity;
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
        YoutubeSlides data = imageList.get(position);

        String thumbnailUrl = data.getThumbnail()+YOUTUBE_IMAGE_PATH;

        Glide.with(context).load(thumbnailUrl).placeholder(R.drawable.youtube_v_img).into(holder.imgSlide);
        holder.title.setText(data.getTitle());

      /*  GlideApp.with(context)
                .as(PictureDrawable.class)
                .listener(new SvgSoftwareLayerSetter())
                .load(thumbnailUrl)
                .into(holder.imgSlide);*/

// "http://192.168.0.16/vuvpayments.com/images/youtube_thumbnail/cd656116413d4cf85cbb39223cdfd224.png"


        holder.itemView.setOnClickListener(v -> {
            try {
                if (!data.getLink().isEmpty()) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.intent.setPackage("com.android.chrome");
                    customTabsIntent.launchUrl(context, Uri.parse(data.getLink()));
                }
            }catch (ActivityNotFoundException e){
                Log.d("TAG_DATA", "instantiateItem: "+e.getMessage());
            }
        });

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
