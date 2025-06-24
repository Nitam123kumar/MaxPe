package com.vuvrecharge.modules.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vuvrecharge.R;

import java.util.ArrayList;

public class ShortSpinnerAdapter extends ArrayAdapter<String> {
    AppCompatActivity activity;
    LayoutInflater inflter;
    ArrayList<String> item_list;

    String selected_country = "";

    public ShortSpinnerAdapter(AppCompatActivity activity, LayoutInflater inflter, ArrayList<String> item_list) {
        super(activity, R.layout.short_items, item_list);
        this.item_list = item_list;
        this.activity = activity;
        this.inflter = inflter;
    }

    @Override
    public int getCount() {
        return item_list.size();
    }


    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.short_items, null);
        TextView title = view.findViewById(R.id.title);
        title.setText(item_list.get(i));
        title.setTextColor(getContext().getResources().getColor(R.color.black_2));
        return view;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addData(String selected_country) {
        this.selected_country = selected_country;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.short_items, null);
        TextView title = view.findViewById(R.id.title);
        title.setText(item_list.get(i));
        title.setTextColor(getContext().getResources().getColor(R.color.black_2));
        return view;
    }
}

