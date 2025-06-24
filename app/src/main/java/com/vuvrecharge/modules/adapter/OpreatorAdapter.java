package com.vuvrecharge.modules.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.OperatorData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OpreatorAdapter extends ArrayAdapter<String> {
    AppCompatActivity activity;
    LayoutInflater inflter;
    ArrayList<String> operator_list;
    ArrayList<String> imagelist;
    ArrayList<String> dummyimagelist;
    String dummy_image,image;
    OperatorData operatorData;

    String selected_country = "";
    public OpreatorAdapter(AppCompatActivity activity, ArrayList<String> operator_list, ArrayList<String> imagelist) {
        super(activity, R.layout.opreator_item_layout, operator_list);
        this.operator_list = operator_list;
        this.imagelist = imagelist;
        this.dummy_image = dummy_image;

        this.activity = activity;
        this.inflter = activity.getLayoutInflater();
    }
    @Override
    public int getCount() {
        return operator_list.size();
    }


    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.opreator_item_layout, null);
        TextView title = view.findViewById(R.id.title);
        TextView footer = view.findViewById(R.id.footer);
        ImageView right_green = view.findViewById(R.id.right_green);
        ImageView operator_img = view.findViewById(R.id.operator_img);

        if (i>0){
            operator_img.setVisibility(View.VISIBLE);
            Picasso.get().load(imagelist.get(i)).into(operator_img);
        }else {
            operator_img.setVisibility(View.GONE);
        }
        title.setText(operator_list.get(i).trim());

        if (operator_list.get(i).equals(selected_country)) {
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
        view = inflter.inflate(R.layout.opreator_item_layout, null);
        TextView title = view.findViewById(R.id.title);
        ImageView right_green = view.findViewById(R.id.right_green);
        ImageView opreator_img = view.findViewById(R.id.operator_img);
        right_green.setVisibility(View.GONE);
        title.setText(operator_list.get(i).trim());
        if (i>0){
            opreator_img.setVisibility(View.VISIBLE);
            Picasso.get().load(imagelist.get(i)).into(opreator_img);
        }
        if(operator_list.get(i).trim().equals("")){
            opreator_img.setVisibility(View.GONE);
        }
        title.setTextColor(getContext().getResources().getColor(R.color.black_4));
        return view;
    }
}
