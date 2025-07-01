package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    public List<ReportsData> dataList = new ArrayList<>();
    String from = "";
    String operator_dunmy_img = "";
    String operator_img = "";
    DefaultView mDefaultView;
    UserPreferences mDatabase;

    public ReportsAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.reports_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
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

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;

        @BindView(R.id.date_time)
        TextView date_time;
        @BindView(R.id.order_id)
        TextView order_id;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.user_type)
        TextView user_type;
        @BindView(R.id.number)
        TextView number;

        @BindView(R.id.commission)
        TextView commission;
        @BindView(R.id.operator_referance)
        TextView operator_referance;

        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.operator_img)
        CircleImageView operator_img_;
        @BindView(R.id.loading)
        ImageView loading;
        @BindView(R.id.layoutDesignPattern)
        ConstraintLayout layoutDesignPattern;
        @BindView(R.id.status_cons)
        ConstraintLayout status_cons;

        @BindView(R.id.status_imageView)
        ImageView status_imageView;


        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(@NonNull ReportsData resultsBean) {
            if (resultsBean.getOperator_ref() != null) {
                if (resultsBean.getOperator_ref().equals("")) {
                    operator_referance.setVisibility(View.GONE);
                } else {
                    operator_referance.setVisibility(View.VISIBLE);
                    operator_referance.setText("Operator Ref : " + resultsBean.getOperator_ref());
                }
            } else {
                operator_referance.setVisibility(View.GONE);
            }

            order_id.setText("Order Id : " + resultsBean.getOrder_id());
            amount.setText("\u20b9" + resultsBean.getAmount());

            if (resultsBean.getLogo() == null) {
                setImageUser(mContext, operator_img + operator_dunmy_img, operator_img_);
            } else {
                if (resultsBean.getLogo().equals("")) {
                    setImageUser(mContext, operator_img + operator_dunmy_img, operator_img_);
                } else {
                    setImageUser(mContext, operator_img + resultsBean.getLogo(), operator_img_);
                }
            }

            DecimalFormat decimalFormat = new DecimalFormat("0.000");
            double charge = Double.parseDouble(resultsBean.getAmount()) - Double.parseDouble(resultsBean.getFinal_charge());
            commission.setText("Com : \u20b9" + decimalFormat.format(charge));
            number.setText(resultsBean.getNumber());
            name.setText(resultsBean.getOperator_name().trim());
            status.setText(resultsBean.getStatus());
            if (resultsBean.getStatus().toUpperCase().equals("PENDING")) {
                status.setText("Processing");
                operator_referance.setVisibility(View.GONE);
                user_type.setVisibility(View.GONE);
//                status_cons.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getDrawable(R.drawable.pending_history_bg)));
                status_cons.setBackgroundResource(R.drawable.pending_history_bg);
                status_imageView.setImageResource(R.drawable.pending_img);
                status.setTextColor(mContext.getResources().getColor(R.color.white));
                loading.setVisibility(View.VISIBLE);
                Glide.with(mContext).asGif().load(R.drawable.load).into(loading);
            } else if (resultsBean.getStatus().toUpperCase().equals("SUCCESS")) {
                if (mDatabase.getUserData() != null){
                    if (mDatabase.getUserData().getUser_type() != null){
                        if (mDatabase.getUserData().getUser_type().toUpperCase().equals("USER") || mDatabase.getUserData().getUser_type().toUpperCase().equals("RETAILER")) {
                            user_type.setVisibility(View.GONE);
                        } else {
                            user_type.setVisibility(View.VISIBLE);
                            user_type.setText(resultsBean.getUsername() + " (" + resultsBean.getName() + " - " + resultsBean.getUser_type() + ")");
                        }
//                        status_cons.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getDrawable(R.drawable.success_bg)));
                        status_cons.setBackgroundResource(R.drawable.success_bg);
                        status.setTextColor(mContext.getResources().getColor(R.color.white));
                        loading.setVisibility(View.GONE);
                    }
                }
            } else {
                user_type.setVisibility(View.GONE);
                operator_referance.setVisibility(View.GONE);
//                status_cons.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getDrawable(R.drawable.failed_history_img)));
                status.setTextColor(mContext.getResources().getColor(R.color.white));
                status_cons.setBackgroundResource(R.drawable.failed_history_img);
                status_imageView.setImageResource(R.drawable.failed_img);
                loading.setVisibility(View.GONE);
                commission.setText("Com : \u20b90.00");
            }

            long date_t = Long.parseLong(resultsBean.getRequest_time().trim());
            date_t = date_t * 1000;
            Date date = new Date(date_t);
            try {
                String dateFormatNew = "N/A";
                dateFormatNew = BaseMethod.dateFormatNew.format(date);
                date_time.setText(dateFormatNew);
            } catch (Exception e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, RechargeReportActivity.class);
                Gson gson = new Gson();
                String mReportsData = gson.toJson(resultsBean);
                intent.putExtra("operator_img", operator_img);
                intent.putExtra("operator_dunmy_img", operator_dunmy_img);
                intent.putExtra("mReportsData", mReportsData);
                intent.putExtra("report","1");
                /*if (resultsBean.getRecharge_type().toLowerCase().equals("prepaid") || resultsBean.getRecharge_type().toLowerCase().equals("dth")
                        || resultsBean.getRecharge_type().toLowerCase().equals("giftcards")){
                    intent.putExtra("bps", "0");
                }*/if (resultsBean.getStatus().toLowerCase().equals("success") || resultsBean.getStatus().toLowerCase().equals("failed")){
                    intent.putExtra("bps", "0");
                }else {
                    intent.putExtra("bps", "1");
                }
                mContext.startActivity(intent);
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
