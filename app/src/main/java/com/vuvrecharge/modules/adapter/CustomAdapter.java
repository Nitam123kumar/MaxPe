package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vuvrecharge.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> arrayList;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, ArrayList<String> arrayList) {
        this.context = applicationContext;
        this.arrayList = arrayList;
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
        view = inflter.inflate(R.layout.plan_detail_new, null); // inflate the layout
        TextView detail = (TextView) view.findViewById(R.id.detail); // get the reference of ImageView
        detail.setText(arrayList.get(i)); // set logo images
        return view;
    }
}