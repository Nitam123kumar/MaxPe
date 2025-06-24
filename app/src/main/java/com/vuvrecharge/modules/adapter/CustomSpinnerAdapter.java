package com.vuvrecharge.modules.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vuvrecharge.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    AppCompatActivity activity;
    LayoutInflater inflter;
    ArrayList<String> item_list;

    String selected_country = "";

    public CustomSpinnerAdapter(AppCompatActivity activity, LayoutInflater inflter, ArrayList<String> item_list) {
        super(activity, R.layout.select_items, item_list);
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

        view = inflter.inflate(R.layout.select_items, null);
        TextView title = view.findViewById(R.id.title);
        TextView footer = view.findViewById(R.id.footer);
        ImageView right_green = view.findViewById(R.id.right_green);
        if (i != item_list.size() - 1) {
            footer.setBackgroundColor(activity.getResources().getColor(R.color.color_c));
        }
        title.setText(item_list.get(i));

        if (item_list.get(i).equals(selected_country)) {
            right_green.setVisibility(View.VISIBLE);
        } else {
            right_green.setVisibility(View.GONE);
        }

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
        view = inflter.inflate(R.layout.select_items, null);
        TextView title = view.findViewById(R.id.title);
        ImageView right_green = view.findViewById(R.id.right_green);
        right_green.setVisibility(View.GONE);
        title.setText(item_list.get(i));
        title.setTextColor(getContext().getResources().getColor(R.color.black_2));
        return view;
    }
}

