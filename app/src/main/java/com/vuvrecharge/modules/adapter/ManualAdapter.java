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
import com.vuvrecharge.modules.model.FundRequestData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<FundRequestData> mFundRequestData = new ArrayList<>();

    public ManualAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.manual_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bind(mFundRequestData.get(position));

    }

    @Override
    public int getItemCount() {
        return mFundRequestData.size();
    }

    public void addEvents(List<FundRequestData> mFundRequestData) {
        this.mFundRequestData = mFundRequestData;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;
        @BindView(R.id.date_time)
        TextView date_time;
        @BindView(R.id.order_id)
        TextView order_id;
        @BindView(R.id.imgDone)
        ImageView imgDone;
        @BindView(R.id.remaining_amount)
        TextView remaining_amount;
        @BindView(R.id.total_amount)
        TextView total_amount;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.remark)
        TextView remark;
//        @BindView(R.id.status)
//        TextView status;
        @BindView(R.id.reason)
        TextView reason;
        @BindView(R.id.layoutDesignPattern)
        ConstraintLayout layoutDesignPattern;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(@NonNull FundRequestData mFundRequestData) {

            order_id.setText("Order Id : " + mFundRequestData.getRef_no());
            amount.setText("\u20b9" + mFundRequestData.getAmount());
            reason.setText("Bank : " + mFundRequestData.getBank());
            total_amount.setText("Mode : " + mFundRequestData.getMode());
            if (!mFundRequestData.getComment().equals("")) {
                remark.setText("Remarks : " + mFundRequestData.getComment());
                remark.setVisibility(View.VISIBLE);
            } else {
                remark.setVisibility(View.GONE);
            }
//            if (mFundRequestData.getReason() != null) {
//                if (!mFundRequestData.getReason().equals("")) {
//                    reason.setText(mFundRequestData.getReason());
//                    reason.setVisibility(View.VISIBLE);
//                } else {
//                    reason.setVisibility(View.GONE);
//                }
//            } else {
//                reason.setVisibility(View.GONE);
//            }

            if (mFundRequestData.getStatus().toUpperCase().equals("PENDING")) {
                remaining_amount.setText(mFundRequestData.getStatus().toUpperCase());
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_pending_1);
                Glide.with(mContext)
                        .load(R.drawable.pending_new_icon)
                        .into(imgDone);
            } else if (mFundRequestData.getStatus().toUpperCase().equals("ACCEPTED")) {
                Glide.with(mContext)
                        .load(R.drawable.done_new_icon)
                        .into(imgDone);
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_history_1);
                remaining_amount.setText(mFundRequestData.getStatus().toUpperCase());
            } else {
                Glide.with(mContext)
                        .load(R.drawable.cancle_new_icon)
                        .into(imgDone);
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_report_1);
                remaining_amount.setText(mFundRequestData.getStatus().toUpperCase());
            }
//            status.setText(mFundRequestData.getStatus().toUpperCase());

            try {
                String dateFormatNew = "N/A";
                Date date = new Date((Long.parseLong(mFundRequestData.getRequest_time())) * 1000);
                dateFormatNew = BaseMethod.dateFormatNew.format(date);
                date_time.setText(dateFormatNew);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
