package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.PaymentSetting;

import java.util.ArrayList;

public class PaymentMethodSelectAdapter extends RecyclerView.Adapter<PaymentMethodSelectAdapter.Holder> {


    Context context;
    ArrayList<PaymentSetting> list;

    OnClickListener listener;
    private int lastSelectedPosition = 0;
    private String amount="";

    public PaymentMethodSelectAdapter(Context context, OnClickListener listener, ArrayList<PaymentSetting> listArray) {
        Log.d("list", String.valueOf(list));
        this.context = context;
        this.listener = listener;
        this.list=listArray;
        Log.d("list", String.valueOf(listArray));
    }
    public void addData(ArrayList<PaymentSetting> list,String amount){
        this.list = list;
        this.amount=amount;
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
//        holder.txtUpiTitle2.setText(setting.getStr2());
        Log.d("Name Data", setting.getStr());
        Glide.with(context)
                .load(setting.getImage())
                .into(holder.imgUpi);
        holder.add_money_btn.setOnClickListener(v -> listener.onClick(amount,setting.getStr()));

        holder.radioButton.setChecked(position == lastSelectedPosition);

        if (position == lastSelectedPosition) {
            holder.add_money_btn.setVisibility(View.VISIBLE);
            holder.radioButton.setSelected(true);
            holder.add_money_btn.setText("Add ₹"+amount);
        } else {
            holder.add_money_btn.setVisibility(View.GONE);
            holder.radioButton.setSelected(false);
        }

        holder.radioButton.setOnClickListener(v -> {
            int previousSelected = lastSelectedPosition;
            lastSelectedPosition = holder.getAdapterPosition();


            notifyItemChanged(previousSelected); // Unselect previous
            notifyItemChanged(lastSelectedPosition);
            // Select current

        });

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = lastSelectedPosition;
            lastSelectedPosition = holder.getAdapterPosition();

            notifyItemChanged(previousSelected);
            notifyItemChanged(lastSelectedPosition);

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView imgUpi;
        TextView txtUpiTitle;
//        TextView txtUpiTitle2;
        RadioGroup radioGroup1;
        RadioButton radioButton;
        AppCompatButton add_money_btn;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imgUpi = itemView.findViewById(R.id.payment_icon);
            txtUpiTitle = itemView.findViewById(R.id.payment_title);
            add_money_btn = itemView.findViewById(R.id.add_money_btn);
            radioGroup1 = itemView.findViewById(R.id.radioGroup1);
            radioButton = itemView.findViewById(R.id.radioBtn);


        }
    }
    public interface OnClickListener{
        void onClick(String amount,String name);
    }

}
