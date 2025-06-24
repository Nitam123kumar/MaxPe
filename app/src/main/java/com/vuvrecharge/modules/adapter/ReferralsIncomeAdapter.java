package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.model.ReferralsIncomeData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReferralsIncomeAdapter extends RecyclerView.Adapter<ReferralsIncomeAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<ReferralsIncomeData> mReferralsData = new ArrayList<>();


    public ReferralsIncomeAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.referal_incomes_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(mReferralsData.get(position));
    }

    @Override
    public int getItemCount() {
        return mReferralsData.size();
    }

    public void addEvents(List<ReferralsIncomeData> mReferralsData, @NonNull String second_message) {
        this.mReferralsData = mReferralsData;
        if (second_message.equals("No")) {
            for (ReferralsIncomeData walletData : mReferralsData) {
                this.mReferralsData.add(walletData);
            }
        } else {
            this.mReferralsData = new ArrayList<>();
            this.mReferralsData = mReferralsData;
        }

        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;
        @BindView(R.id.order_id)
        TextView order_id;
        @BindView(R.id.typr)
        TextView typr;
        @BindView(R.id.income)
        TextView income;
        @BindView(R.id.date_time)
        TextView date_time;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(@NonNull ReferralsIncomeData mReferralsData) {

            income.setText("Income : "+mReferralsData.getIncome());
            typr.setText("Type : "+mReferralsData.getType());
            if (mReferralsData.getOrder_id()!=null){
                order_id.setText("Order Id : "+mReferralsData.getOrder_id());
            }else {
                order_id.setVisibility(View.GONE);
            }

            long date_t = Long.parseLong(mReferralsData.getTime().trim());
            date_t = date_t * 1000;
            Date date = new Date(date_t);
            try {
                String dateFormatNew = "N/A";
                dateFormatNew = BaseMethod.dateFormatNew.format(date);
                date_time.setText(dateFormatNew);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
