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

public class PaymentMethodSelectAdapter extends RecyclerView.Adapter<PaymentMethodSelectAdapter.Holder> {


    Context context;
    ArrayList<PaymentSetting> list;

    PaymentSettingAdapter.OnClickListener listener;


    public PaymentMethodSelectAdapter(Context context, PaymentSettingAdapter.OnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public void addData(ArrayList<PaymentSetting> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentMethodSelectAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item_layout,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodSelectAdapter.Holder holder, int position) {
        PaymentSetting setting = list.get(position);

        holder.txtUpiTitle.setText(setting.getStr());
        holder.txtUpiTitle2.setText(setting.getStr2());
        Glide.with(context)
                .load(setting.getImage())
                .into(holder.imgUpi);
        holder.itemView.setOnClickListener(v -> listener.onClick(setting.getStr()));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView imgUpi;
        TextView txtUpiTitle;
        TextView txtUpiTitle2;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imgUpi = itemView.findViewById(R.id.payment_icon);
            txtUpiTitle = itemView.findViewById(R.id.payment_title);
            txtUpiTitle2 = itemView.findViewById(R.id.payment_getaway);
        }
    }
    public interface OnClickListener{
        void onClick(String name);
    }

}
