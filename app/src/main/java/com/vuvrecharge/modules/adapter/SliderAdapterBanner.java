package com.vuvrecharge.modules.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.OfferSlider;
import com.vuvrecharge.modules.model.SliderData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SliderAdapterBanner extends PagerAdapter {

    private Context mContext;
    private List<String> color;
    private List<SliderData> banners;
   ItemClickListener listener;
    public SliderAdapterBanner(Context context, List<String> color, List<SliderData> banners,ItemClickListener listener) {
        this.mContext = context;
        this.color = color;
        this.banners = banners;
        this.listener=listener;
    }

    @Override
    public int getCount() {
        return color.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        ImageView image_view = view.findViewById(R.id.imageView);
//        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.no)
//                .error(R.drawable.no);
        SliderData sliderData = banners.get(position);
            Log.d("logo",color.get(position));
        if (!color.get(position).equals("")) {
            Glide.with(mContext)
                    .load(color.get(position))
//                    .apply(options)
                    .into(image_view);
        }
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        image_view.setOnClickListener(v -> {
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

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
    public interface ItemClickListener{
        void onClickListener(String redirection_type,String intent_name,String extra_data,String link) throws ClassNotFoundException;
    }
}
