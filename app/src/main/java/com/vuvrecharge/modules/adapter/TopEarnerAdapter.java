package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.TopEarnerData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopEarnerAdapter extends RecyclerView.Adapter<TopEarnerAdapter.ViewHolder> {
    private Context context;
    List<TopEarnerData> userDataList;

    public TopEarnerAdapter(Context context, List<TopEarnerData> userDataList) {
        this.context = context;
        this.userDataList = userDataList;
    }

    @NonNull
    @Override
    public TopEarnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_earner_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopEarnerAdapter.ViewHolder holder, int position) {
        TopEarnerData userData = userDataList.get(position);

        holder.ranker_name.setText(userData.getName());
        holder.ranker_amount.setText(userData.getAmount());


        int num = ++position ;
        holder.rank_no.setText(String.valueOf(num));

        if (num % 2 == 0) {
            holder.topEarnerBackground.setBackgroundResource(R.drawable.top_earner_item_shape_even);
        } else {
            holder.topEarnerBackground.setBackgroundResource(R.drawable.top_earner_item_shape);
        }
    }

        @Override
    public int getItemCount() {
        return userDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rank_no)
        TextView rank_no;
        @BindView(R.id.ranker_name)
        TextView ranker_name;
        @BindView(R.id.ranker_amount)
        TextView ranker_amount;
        @BindView(R.id.topEarnerBackground)
        ConstraintLayout topEarnerBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
