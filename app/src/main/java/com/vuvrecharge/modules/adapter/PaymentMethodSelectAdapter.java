package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.vuvrecharge.modules.model.PaymentSettingModel;

import java.util.ArrayList;

public class PaymentMethodSelectAdapter extends RecyclerView.Adapter<PaymentMethodSelectAdapter.Holder> {

    Context context;
    ArrayList<PaymentSettingModel> list;
    OnClickListener listener;
    private int lastSelectedPosition = 0;
    private String amount = "";

    public PaymentMethodSelectAdapter(Context context, OnClickListener listener, ArrayList<PaymentSettingModel> listArray) {
        this.context = context;
        this.listener = listener;
        this.list = listArray;
    }

    public void addData(ArrayList<PaymentSettingModel> list, String amount) {
        this.list = list;
        this.amount = amount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PaymentSettingModel setting = list.get(position);

        holder.txtUpiTitle.setText(setting.getStr());

        // ✅ Handle Drawable or resource int
        if (setting.getImage() instanceof Integer) {
            Glide.with(context).load((int) setting.getImage()).into(holder.imgUpi);
        } else if (setting.getImage() instanceof Drawable) {
            holder.imgUpi.setImageDrawable((Drawable) setting.getImage());
        }

        boolean isSelected = (position == lastSelectedPosition);
        holder.radioButton.setChecked(isSelected);

        if (isSelected) {
            holder.add_money_btn.setVisibility(View.VISIBLE);
            holder.add_money_btn.setText("Pay ₹" + amount);
        } else {
            holder.add_money_btn.setVisibility(View.GONE);
        }

        holder.radioButton.setOnClickListener(v -> selectItem(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(v -> selectItem(holder.getAdapterPosition()));

        holder.add_money_btn.setOnClickListener(v ->
                listener.onClick(amount, setting.getPackageName())
        );
    }

    private void selectItem(int position) {
        int previousSelected = lastSelectedPosition;
        lastSelectedPosition = position;

        if (previousSelected != -1) notifyItemChanged(previousSelected);
        notifyItemChanged(lastSelectedPosition);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imgUpi;
        TextView txtUpiTitle;
        RadioButton radioButton;
        AppCompatButton add_money_btn;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imgUpi = itemView.findViewById(R.id.payment_icon);
            txtUpiTitle = itemView.findViewById(R.id.payment_title);
            add_money_btn = itemView.findViewById(R.id.add_money_btn);
            radioButton = itemView.findViewById(R.id.radioBtn);
        }
    }

    public interface OnClickListener {
        void onClick(String amount, String packageName);
    }
}
