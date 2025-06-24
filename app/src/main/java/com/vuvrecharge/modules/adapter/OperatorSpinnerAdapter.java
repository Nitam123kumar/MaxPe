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

public class OperatorSpinnerAdapter extends ArrayAdapter<String> {
    AppCompatActivity activity;
    LayoutInflater inflter;
    ArrayList<String> country_list;

    String selected_country = "";

    public OperatorSpinnerAdapter(AppCompatActivity activity, ArrayList<String> country_list) {
        super(activity, R.layout.select_items, country_list);
        this.country_list = country_list;
        this.activity = activity;
        this.inflter = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return country_list.size();
    }


    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.select_items, null);
        TextView title = view.findViewById(R.id.title);
        TextView footer = view.findViewById(R.id.footer);
        ImageView right_green = view.findViewById(R.id.right_green);
        if (i != country_list.size() - 1) {
            footer.setBackgroundColor(activity.getResources().getColor(R.color.color_c));
        }
        title.setText(country_list.get(i).trim());

        if (country_list.get(i).equals(selected_country)) {
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
        title.setText(country_list.get(i).trim());
        title.setTextColor(getContext().getResources().getColor(R.color.black_4));
        return view;
    }
}

