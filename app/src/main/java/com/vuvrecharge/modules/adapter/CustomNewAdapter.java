package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.view.DefaultView;

import java.util.ArrayList;

public class CustomNewAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> arrayList;
    ArrayList<String> packageList;
    LayoutInflater inflter;
    ArrayList<Drawable> myImageList;
    DefaultView defaultView;


    public CustomNewAdapter(Context applicationContext, ArrayList<String> arrayList,ArrayList<Drawable> myImageList,ArrayList<String> packageList,DefaultView defaultView) {
        this.context = applicationContext;
        this.arrayList = arrayList;
        this.myImageList = myImageList;
        this.packageList = packageList;
        this.defaultView = defaultView;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.list_row, null); // inflate the layout
        TextView detail = view.findViewById(R.id.text); // get the reference of ImageView
        ImageView imageView = view.findViewById(R.id.img); // get the reference of ImageView
        LinearLayout layout_click = view.findViewById(R.id.layout_click); // get the reference of ImageView
        detail.setText(arrayList.get(i)); // set logo images
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.no)
                .error(R.drawable.no);
        Glide.with(context)
                .load(myImageList.get(i))
                .apply(options)
                .into(imageView);
        layout_click.setOnClickListener(v -> defaultView.onSuccess(packageList.get(i),"PhoneList"));
        return view;
    }
}