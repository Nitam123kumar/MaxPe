package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.activities.RechargeReportActivity;
import com.vuvrecharge.modules.model.ReportsData;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.UserPreferences;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentRechargesAdapter extends RecyclerView.Adapter<RecentRechargesAdapter.RechargeViewHolder> {

    Context context;
    private LayoutInflater mLayoutInflater;

    String from = "";
    String operator_dunmy_img = "";
    String operator_img = "";
    DefaultView mDefaultView;
    UserPreferences mDatabase;


    public List<ReportsData> dataList = new ArrayList<>();

    public RecentRechargesAdapter(LayoutInflater mLayoutInflater,Context context) {
        this.mLayoutInflater = mLayoutInflater;
        this.context=context;
    }

    @NonNull
    @Override
    public RechargeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.recent_recharges_layout, parent, false);
        return new RechargeViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RechargeViewHolder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addData(List<ReportsData> dataList, @NonNull String from, String operator_img,
                        String operator_dunmy_img, DefaultView mDefaultView, UserPreferences mDatabase) {
        this.from = from;
        this.operator_img = operator_img;
        this.operator_dunmy_img = operator_dunmy_img;
        this.mDefaultView = mDefaultView;
        this.mDatabase = mDatabase;
        if (from.equals("No")) {
            for (ReportsData walletData : dataList) {
                this.dataList.add(walletData);
            }
        } else {
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }

    public class RechargeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rechargeLogo)
        ImageView rechargeLogo;
        @BindView(R.id.rechargeTitle)
        TextView rechargeTitle;
        @BindView(R.id.recharge_mobile_number)
        TextView rechargeMobileNumber;
        @BindView(R.id.packPrice)
        TextView packPrice;
        @BindView(R.id.validity)
        TextView validity;

        public RechargeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bind(@NonNull ReportsData resultsBean) {

            if (resultsBean.getStatus().toUpperCase().equals("SUCCESS")) {

                packPrice.setText("\u20b9" + resultsBean.getAmount());
                rechargeTitle.setText(resultsBean.getOperator_name().trim());
                rechargeMobileNumber.setText(resultsBean.getNumber());
                if (resultsBean.getLogo() == null) {
                    setImageUser(context, operator_img + operator_dunmy_img, rechargeLogo);
                } else {
                    if (resultsBean.getLogo().equals("")) {
                        setImageUser(context, operator_img + operator_dunmy_img, rechargeLogo);
                    } else {
                        setImageUser(context, operator_img + resultsBean.getLogo(), rechargeLogo);
                    }
                }
            }


            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, RechargeReportActivity.class);
                Gson gson = new Gson();
                String mReportsData = gson.toJson(resultsBean);
                intent.putExtra("operator_img", operator_img);
                intent.putExtra("operator_dunmy_img", operator_dunmy_img);
                intent.putExtra("mReportsData", mReportsData);
                intent.putExtra("report", "1");
                /*if (resultsBean.getRecharge_type().toLowerCase().equals("prepaid") || resultsBean.getRecharge_type().toLowerCase().equals("dth")
                        || resultsBean.getRecharge_type().toLowerCase().equals("giftcards")){
                    intent.putExtra("bps", "0");
                }*/
                if (resultsBean.getStatus().toLowerCase().equals("success") || resultsBean.getStatus().toLowerCase().equals("failed")) {
                    intent.putExtra("bps", "0");
                } else {
                    intent.putExtra("bps", "1");
                }
                context.startActivity(intent);
            });

        }


    }

    public void setImageUser(Context mContext, @NonNull String image_url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no)
                .error(R.drawable.no);
        if (!image_url.equals("")) {
            Glide.with(mContext)
                    .load(image_url)
                    .apply(options)
                    .into(imageView);
        }
    }

}
