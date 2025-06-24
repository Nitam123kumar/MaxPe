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
import com.vuvrecharge.modules.model.SliderData;

import java.util.List;

public class SliderAdapterBanner extends PagerAdapter {

    private Context mContext;
    private List<String> color;
    private List<SliderData> banners;

    public SliderAdapterBanner(Context context, List<String> color, List<SliderData> banners) {
        this.mContext = context;
        this.color = color;
        this.banners = banners;

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
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.no)
                .error(R.drawable.no);

        if (!color.get(position).equals("")) {
            Glide.with(mContext)
                    .load(color.get(position))
                    .apply(options)
                    .into(image_view);
        }
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        image_view.setOnClickListener(v -> {
            try {
                if (!banners.get(position).getUrl().isEmpty()) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.intent.setPackage("com.android.chrome");
                    customTabsIntent.launchUrl(mContext, Uri.parse(banners.get(position).getUrl()));
                }
            }catch (ActivityNotFoundException e){
                Log.d("TAG_DATA", "instantiateItem: "+e.getMessage());
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
}
