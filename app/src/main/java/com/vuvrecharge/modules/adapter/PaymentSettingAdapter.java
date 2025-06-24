package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.PaymentSetting;

import java.util.ArrayList;

public class PaymentSettingAdapter extends RecyclerView.Adapter<PaymentSettingAdapter.PaymentSettingVH> {

    Context context;
    ArrayList<PaymentSetting> list;

    OnClickListener listener;


    public PaymentSettingAdapter(Context context, OnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void addData(ArrayList<PaymentSetting> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentSettingAdapter.PaymentSettingVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentSettingVH(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_setting_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentSettingAdapter.PaymentSettingVH holder, int position) {
        PaymentSetting setting = list.get(position);

        holder.txtUpi.setText(setting.getStr());
        holder.txtUpi2.setText(setting.getStr2());
        Glide.with(context)
                .load(setting.getImage())
                .into(holder.imgUpi);
        holder.itemView.setOnClickListener(v -> listener.onClick(setting.getStr()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentSettingVH extends RecyclerView.ViewHolder {

        ImageView imgUpi;
        TextView txtUpi;
        TextView txtUpi2;
        public PaymentSettingVH(@NonNull View itemView) {
            super(itemView);
            imgUpi = itemView.findViewById(R.id.imgUpi);
            txtUpi = itemView.findViewById(R.id.txtUpi);
            txtUpi2 = itemView.findViewById(R.id.txtUpi2);
        }
    }

    public interface OnClickListener{
        void onClick(String name);
    }
}
