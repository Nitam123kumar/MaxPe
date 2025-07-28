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
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.model.DepositData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPaymentHistoryAdapter extends RecyclerView.Adapter<AddPaymentHistoryAdapter.AddPaymentHistoryViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<DepositData> mDepositData = new ArrayList<>();

    public AddPaymentHistoryAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    public interface OnItemClickListener {
        void onItemClick(DepositData depositData);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public AddPaymentHistoryAdapter.AddPaymentHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.latest_transitions_history_item_layout, parent, false);
        return new AddPaymentHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddPaymentHistoryAdapter.AddPaymentHistoryViewHolder holder, int position) {
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

    public class AddPaymentHistoryViewHolder extends RecyclerView.ViewHolder{

        private Context mContext;
        @BindView(R.id.payment_ImageView)
        ImageView imgPayment;
        @BindView(R.id.payment_tittle_TV)
        TextView paymentTittleTV;
        @BindView(R.id.payment_time_TV)
        TextView paymentTimeTV;
        @BindView(R.id.payment_price)
        TextView paymentPriceTV;
        AddPaymentHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(mDepositData.get(getAdapterPosition()));
                }
            });

        }

        public void bind(@NonNull DepositData mDepositData) {

//        txtOrderId.setText("Order Id : " + mDepositData.getOrder_id());
            paymentPriceTV.setText("\u20b9" + mDepositData.getTotal_amount());
//        txtCharge.setText("Charge : \u20b9" + mDepositData.getCharge());
//        txtTotalAmount.setText("Final Amount : \u20b9" + mDepositData.getFinal_amount());


            if (mDepositData.getPayment_status().toUpperCase().equals("PENDING")) {
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_pending);
                Glide.with(mContext)
                        .load(R.drawable.pending_2)
                        .into(imgPayment);
//                txtStatus.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.pending)));
            } else if (mDepositData.getPayment_status().toUpperCase().equals("SUCCESS")) {
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_history_1);
                Glide.with(mContext)
                        .load(R.drawable.phone_pay_svg)
                        .into(imgPayment);
//                txtStatus.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.success)));
            } else {
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_report_1);
                Glide.with(mContext)
                        .load(R.drawable.close_1)
                        .into(imgPayment);
//                txtStatus.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.failed)));
            }
//            txtStatus.setText(mDepositData.getPayment_status().toUpperCase());

            try {
                String dateFormatNew = "N/A";
                Date date = new Date((Long.parseLong(mDepositData.getOrder_time())) * 1000);
                dateFormatNew = BaseMethod.dateFormatNew.format(date);
                paymentTimeTV.setText(dateFormatNew);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
