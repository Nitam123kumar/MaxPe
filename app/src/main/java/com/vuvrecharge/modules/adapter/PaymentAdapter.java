package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.PaymentModel;
import com.vuvrecharge.modules.model.PaymentSetting;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentVH> {

    Context context;
    ArrayList<PaymentModel> list;

    OnClickListener listener;


    public PaymentAdapter(Context context, OnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void addData(ArrayList<PaymentModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentAdapter.PaymentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentAdapter.PaymentVH(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.PaymentVH holder, int position) {
        PaymentModel model = list.get(position);

        holder.txtUpi.setText(model.getStr()+"");
        Glide.with(context)
                .load(model.getImage())
                .into(holder.imgUpi);

        if (model.isSelected()){
            holder.itemView.setBackgroundResource(R.drawable.payment_drawable_selected);
            if (!holder.itemView.isClickable()){
                listener.onClick(model.getStr2());
            }
        }else {
            holder.itemView.setBackgroundResource(R.drawable.payment_drawable);
        }
        holder.itemView.setOnClickListener(v -> {
            for (int i = 0; i < list.size(); i++){
                list.get(i).setSelected(false);
            }
            model.setSelected(true);
            holder.itemView.setBackgroundResource((model.isSelected())? R.drawable.payment_drawable_selected : R.drawable.payment_drawable);
            if (model.isSelected()){
                listener.onClick(model.getStr2());
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentVH extends RecyclerView.ViewHolder {
        ImageView imgUpi;
        TextView txtUpi;
        public PaymentVH(@NonNull View itemView) {
            super(itemView);
            imgUpi = itemView.findViewById(R.id.imgUpi);
            txtUpi = itemView.findViewById(R.id.txtUpi);
        }
    }

    public interface OnClickListener{
        void onClick(String name);
    }
}
