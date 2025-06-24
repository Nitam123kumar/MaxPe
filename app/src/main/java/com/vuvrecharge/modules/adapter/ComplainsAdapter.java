package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.model.ComplainsData;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.preferences.UserPreferences;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ComplainsAdapter extends RecyclerView.Adapter<ComplainsAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    public List<ComplainsData> dataList = new ArrayList<>();
    String from = "";
    String operator_dunmy_img = "";
    String operator_img = "";
    DefaultView mDefaultView;
    UserPreferences mDatabase;

    public ComplainsAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.complains_row, parent, false);
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

    public void addData(List<ComplainsData> dataList, @NonNull String from, String operator_img,
                        String operator_dunmy_img, DefaultView mDefaultView, UserPreferences mDatabase) {
        this.from = from;
        this.operator_img = operator_img;
        this.operator_dunmy_img = operator_dunmy_img;
        this.mDefaultView = mDefaultView;
        this.mDatabase = mDatabase;
        if (from.equals("No")) {
            for (ComplainsData walletData : dataList) {
                this.dataList.add(walletData);
            }
        } else {
            this.dataList = new ArrayList<>();
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;
        @BindView(R.id.top)
        TextView top;
        @BindView(R.id.date_time)
        TextView date_time;
        @BindView(R.id.date_time_complain)
        TextView date_time_complain;
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
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.complain_details)
        TextView complain_details;
        @BindView(R.id.operator_referance)
        TextView operator_referance;
        @BindView(R.id.status_bg)
        LinearLayout status_bg;
        @BindView(R.id.operator_img)
        CircleImageView operator_img_;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(ComplainsData resultsBean) {

            if (getAdapterPosition() == 0) {
                top.setVisibility(View.GONE);
            } else {
                top.setVisibility(View.VISIBLE);
            }
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

            if (resultsBean.getDescription() != null) {
                if (resultsBean.getDescription().equals("")) {
                    description.setVisibility(View.GONE);
                } else {
                    description.setVisibility(View.VISIBLE);
                    description.setText("Description : " + resultsBean.getDescription());
                }
            } else {
                description.setVisibility(View.GONE);
            }

            if (resultsBean.getRemarks() != null) {
                if (resultsBean.getRemarks().equals("")) {
                    complain_details.setVisibility(View.GONE);
                } else {
                    complain_details.setVisibility(View.VISIBLE);
                    complain_details.setText("Remark : " + resultsBean.getRemarks());
                }
            } else {
                complain_details.setVisibility(View.GONE);
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

            if (mDatabase.getUserData().getUser_type().toUpperCase().equals("ADMIN") ) {
                user_type.setVisibility(View.VISIBLE);
                user_type.setText(resultsBean.getUsername() + " (" + resultsBean.getName() + " - " + resultsBean.getUser_type() + ")");
            } else {
                user_type.setVisibility(View.GONE);
            }

            number.setText(resultsBean.getNumber());
            name.setText(resultsBean.getOperator_name().trim());
            status.setText(resultsBean.getStatus());
            if (resultsBean.getStatus().toUpperCase().equals("PENDING")) {
                status_bg.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.yellow_new)));
                status.setTextColor(mContext.getResources().getColor(R.color.white));
            } else if (resultsBean.getStatus().toUpperCase().equals("SUCCESS")) {
                status_bg.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.green_end)));
                status.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                status_bg.setBackgroundDrawable(BaseMethod.getGradientDrawableRe(mContext.getResources().getColor(R.color.red)));
                status.setTextColor(mContext.getResources().getColor(R.color.white));
            }

            long date_t = Long.parseLong(resultsBean.getRequest_time().trim());
            date_t = date_t * 1000;
            Date date = new Date(date_t);
            try {
                String dateFormatNew = "N/A";
                dateFormatNew = BaseMethod.dateFormatNew.format(date);
                date_time.setText("Recharge on : "+dateFormatNew);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long date_d = Long.parseLong(resultsBean.getDispute_date().trim());
            date_d = date_d * 1000;
            Date date_ = new Date(date_d);
            try {
                String dateFormatNew = "N/A";
                dateFormatNew = BaseMethod.dateFormatNew.format(date_);
                date_time_complain.setText("Complain on : "+dateFormatNew);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
