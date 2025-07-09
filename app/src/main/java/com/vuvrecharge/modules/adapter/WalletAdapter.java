package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.WalletData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MainListHolder> {

    List<WalletData> dataList = new ArrayList<>();

    public WalletAdapter() {
    }

    public void addData(List<WalletData> dataList, @NonNull String second_message) {
        if (second_message.equals("No")) {
            this.dataList.addAll(dataList);
        } else {
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }


    @Override
    public MainListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.passbook_row, parent, false);
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

        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.order_id)
        TextView order_id;
        @BindView(R.id.date_time)
        TextView date_time;
//        @BindView(R.id.total_amount)
//        TextView total_amount;
        @BindView(R.id.remaining_amount)
        TextView remaining_amount;
        @BindView(R.id.reason)
        TextView reason;
        @BindView(R.id.imgDone)
        ImageView imgDone;
//        @BindView(R.id.layoutDesignPattern)
//        ConstraintLayout layoutDesignPattern;

        public MainListHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(@NonNull WalletData resultsBean) {


            if (resultsBean.getTxid() != null){
                order_id.setText("Order Id : " + resultsBean.getTxid());
            }else {
                order_id.setText("Order Id : N/A");
            }
            reason.setText(resultsBean.getComment());
//            total_amount.setText("Amount : \u20b9" + resultsBean.getTxn_amount());

            if (resultsBean.getType().equals("Cr")) {
                amount.setText("+ \u20b9" + resultsBean.getAmount());
                amount.setTextColor(mContext.getResources().getColor(R.color.success1));
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_history_1);
                imgDone.setImageResource(R.drawable.wallet_receved_svg);
            } else {
                amount.setText("- \u20b9" + resultsBean.getAmount());
                amount.setTextColor(mContext.getResources().getColor(R.color.failed1));
                imgDone.setImageResource(R.drawable.walled_svg_img);
//                layoutDesignPattern.setBackgroundResource(R.drawable.pattern_report_1);
            }
            remaining_amount.setText("Closing Balance \u20b9" + resultsBean.getRemaining());
            date_time.setText(resultsBean.getDate_time());
        }
    }
}
