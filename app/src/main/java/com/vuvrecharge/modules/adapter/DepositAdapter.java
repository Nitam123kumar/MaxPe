package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.model.DepositData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DepositAdapter extends RecyclerView.Adapter<DepositAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<DepositData> mDepositData = new ArrayList<>();

    public DepositAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.deposit_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bind(mDepositData.get(position));

    }

    @Override
    public int getItemCount() {
        return mDepositData.size();
    }

    public void addEvents(List<DepositData> mDepositData) {
        this.mDepositData = mDepositData;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;

        @BindView(R.id.txtDateTime)
        TextView txtDateTime;
        @BindView(R.id.txtOrderId)
        TextView txtOrderId;
        @BindView(R.id.txtCharge)
        TextView txtCharge;
        @BindView(R.id.txtTotalAmount)
        TextView txtTotalAmount;
        @BindView(R.id.txtAmount)
        TextView txtAmount;
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.imgDone)
        ImageView imgDone;
        @BindView(R.id.layoutDesignPattern)
        ConstraintLayout layoutDesignPattern;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(@NonNull DepositData mDepositData) {

            txtOrderId.setText("Order Id : " + mDepositData.getOrder_id());
            txtAmount.setText("\u20b9" + mDepositData.getTotal_amount());
            txtCharge.setText("Charge : \u20b9" + mDepositData.getCharge());
            txtTotalAmount.setText("Final Amount : \u20b9" + mDepositData.getFinal_amount());


            if (mDepositData.getPayment_status().toUpperCase().equals("PENDING")) {
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_pending);
                Glide.with(mContext)
                        .load(R.drawable.pending_icon_svg)
                        .into(imgDone);
                txtStatus.setBackgroundDrawable(BaseMethod.getGradientDrawableRe1(mContext.getResources().getColor(R.color.pending)));
                txtAmount.setTextColor(mContext.getResources().getColor(R.color.black));
            } else if (mDepositData.getPayment_status().toUpperCase().equals("SUCCESS")) {
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_history_1);
                Glide.with(mContext)
                        .load(R.drawable.deposit_susccess_image_svg)
                        .into(imgDone);
                txtStatus.setBackgroundDrawable(BaseMethod.getGradientDrawableRe1(mContext.getResources().getColor(R.color.success)));
                txtAmount.setTextColor(mContext.getResources().getColor(R.color.black));
            } else {
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_report_1);
                Glide.with(mContext)
                        .load(R.drawable.failed_icon_svg)
                        .into(imgDone);
                txtStatus.setBackgroundDrawable(BaseMethod.getGradientDrawableRe1(mContext.getResources().getColor(R.color.failed)));
                txtAmount.setTextColor(mContext.getResources().getColor(R.color.black));
            }
            txtStatus.setText(mDepositData.getPayment_status().toUpperCase());

            try {
                String dateFormatNew = "N/A";
                Date date = new Date((Long.parseLong(mDepositData.getOrder_time())) * 1000);
                dateFormatNew = BaseMethod.dateFormatNew.format(date);
                txtDateTime.setText(dateFormatNew);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
