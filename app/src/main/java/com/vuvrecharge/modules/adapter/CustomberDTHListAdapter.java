package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.CustomberListData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomberDTHListAdapter extends RecyclerView.Adapter<CustomberDTHListAdapter.Holder> {

    private LayoutInflater mLayoutInflater;
    public List<CustomberListData> dataList = new ArrayList<>();
    String image_url = "";

    public CustomberDTHListAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.help_line_dth_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addDataDTH(List<CustomberListData> dataList, String image_url) {
        this.dataList = dataList;
        this.image_url = image_url;

        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private Context mContext;

        @BindView(R.id.imgProvider)
        ImageView imgProvider;
        @BindView(R.id.txtNumber)
        TextView txtNumber;
        @BindView(R.id.txtProvider)
        TextView txtProvider;

        @BindView(R.id.txtCare)
        TextView txtCare;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(@NonNull CustomberListData resultsBean) {

            txtProvider.setText(resultsBean.getName());
            txtNumber.setText(resultsBean.getCustomer_care_number());
            txtCare.setText(resultsBean.getCustomer_care_comment());


                Picasso.get().load(image_url + resultsBean.getLogo())
                        .placeholder(R.drawable.no).into(imgProvider);

        }
    }

}
