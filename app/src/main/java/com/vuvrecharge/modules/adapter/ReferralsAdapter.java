package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.model.ReferralsData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReferralsAdapter extends RecyclerView.Adapter<ReferralsAdapter.Holder> {

    private LayoutInflater mLayoutInflater;
    private List<ReferralsData> mReferralsData = new ArrayList<>();
    String fixMobile = "";

    public ReferralsAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.coins_row, parent, false);
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

    public void addEvents(List<ReferralsData> mReferralsData, @NonNull String second_message, String fixMobile) {
        this.mReferralsData = mReferralsData;
        this.fixMobile = fixMobile;
        if (second_message.equals("No")) {
            for (ReferralsData walletData : mReferralsData) {
                this.mReferralsData.add(walletData);
            }
        } else {
            this.mReferralsData = mReferralsData;
        }

        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;
        @BindView(R.id.first_mobile)
        TextView first_mobile;
        @BindView(R.id.last_mobile)
        TextView last_mobile;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date_time)
        TextView date_time;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(@NonNull ReferralsData mReferralsData) {
            Log.d("TAG_DATA", "bind: "+mReferralsData);


            first_mobile.setText(mReferralsData.getFirst_mobile() + fixMobile);
            last_mobile.setText(mReferralsData.getLast_mobile());
            name.setText(mReferralsData.getName());

            try {
                String dateFormatNew = "N/A";
                Date date = BaseMethod.format1.parse(mReferralsData.getDate());
                dateFormatNew = BaseMethod.dateFormat.format(date);
                date_time.setText(dateFormatNew);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
