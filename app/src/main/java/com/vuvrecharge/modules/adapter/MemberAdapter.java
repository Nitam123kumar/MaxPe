package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.activities.MemberProfileActivity;
import com.vuvrecharge.modules.activities.StatementsMemberActivity;
import com.vuvrecharge.modules.model.MemberData;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MainListHolder> {

    List<MemberData> dataList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    UserPreferences mDatabase;
    DefaultView mDefaultView;

    public MemberAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    public void addData(List<MemberData> dataList, @NonNull String second_message, UserPreferences mDatabase, DefaultView mDefaultView) {
        this.mDatabase = mDatabase;
        this.mDefaultView = mDefaultView;

        if (second_message.equals("No")) {
            for (MemberData MemberData : dataList) {
                this.dataList.add(MemberData);
            }
        } else {
            this.dataList = new ArrayList<>();
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }

    public void refreshItem(String second_message, int pos) {
        MemberData memberData = dataList.get(pos);
        memberData.setEarnings(second_message);
        dataList.set(pos, memberData);
        notifyDataSetChanged();
    }

    @Override
    public MainListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pmember_row, parent, false);
        MainListHolder listHolder = new MainListHolder(itemView);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainListHolder holder, final int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public class MainListHolder extends RecyclerView.ViewHolder {
        private Context mContext;

        @BindView(R.id.top)
        TextView top;

        @BindView(R.id.order_id)
        TextView order_id;
        @BindView(R.id.date_time)
        TextView date_time;
        @BindView(R.id.total_amount)
        TextView total_amount;
        @BindView(R.id.remaining_amount)
        TextView remaining_amount;
        @BindView(R.id.reason)
        TextView reason;
        @BindView(R.id.mobile)
        TextView mobile;
        @BindView(R.id.packages)
        TextView packages;
        @BindView(R.id.master)
        TextView master;
        @BindView(R.id.distri)
        TextView distri;
        @BindView(R.id.add_balance)
        TextView add_balance;
        @BindView(R.id.balance_history)
        TextView balance_history;

        @BindView(R.id.view_details)
        LinearLayout view_details;
        @BindView(R.id.status_bg)
        LinearLayout status_bg;
        @BindView(R.id.status)
        TextView status;

        public MainListHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(MemberData resultsBean) {

            if (getAdapterPosition() == 0) {
                top.setVisibility(View.GONE);
            } else {
                top.setVisibility(View.GONE);
            }
            order_id.setText(resultsBean.getName());
            reason.setText("(" + resultsBean.getUsername() + ")");
            mobile.setText("Mobile : " + resultsBean.getMobile());
            if (resultsBean.getPackage_name() != null) {
                packages.setText("Package : " + resultsBean.getPackage_name());
            } else {
                packages.setText("Package : N/A");
            }
            total_amount.setText("Wallet : \u20b9" + resultsBean.getEarnings());
            remaining_amount.setVisibility(View.GONE);
            master.setVisibility(View.GONE);
            distri.setVisibility(View.GONE);
            if (mDatabase.getUserData().getUser_type().equals("Admin")) {
                remaining_amount.setVisibility(View.VISIBLE);
                master.setVisibility(View.VISIBLE);
                distri.setVisibility(View.VISIBLE);
                remaining_amount.setText("Member Type : " + resultsBean.getUser_type());
                if (resultsBean.getMaster_distributor() != null) {
                    master.setText("Master : " + resultsBean.getMaster_distributor());
                } else {
                    master.setText("Master : N/A");
                }
                if (resultsBean.getDistributor() != null) {
                    distri.setText("Distributor : " + resultsBean.getDistributor());
                } else {
                    distri.setText("Distributor : N/A");
                }
            }
            date_time.setText("Date : " + resultsBean.getDate());
            view_details.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.colorPrimary_other)));
            add_balance.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.colorPrimary_other)));
            balance_history.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.colorPrimary_other)));
            status.setText(resultsBean.getStatus());
            if (resultsBean.getStatus().toUpperCase().equals("PENDING")) {
                status_bg.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.yellow_new)));
                status.setTextColor(mContext.getResources().getColor(R.color.white));
            } else if (resultsBean.getStatus().toUpperCase().equals("ACTIVE")) {
                status_bg.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.green_end)));
                status.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                status_bg.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.red)));
                status.setTextColor(mContext.getResources().getColor(R.color.white));
            }

            add_balance.setOnClickListener(v -> mDefaultView.onSuccessOther(resultsBean.getMobile() + "####" + getAdapterPosition()));

            balance_history.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, StatementsMemberActivity.class);
                intent.putExtra("name", resultsBean.getName());
                intent.putExtra("mobile", resultsBean.getMobile());
                mContext.startActivity(intent);
            });
            view_details.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, MemberProfileActivity.class);
                intent.putExtra("name", resultsBean.getName());
                intent.putExtra("mobile", resultsBean.getMobile());
                mContext.startActivity(intent);
            });
        }
    }
}

