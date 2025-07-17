package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.MaxPePointsData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaxPointsAdapter extends RecyclerView.Adapter<MaxPointsAdapter.ViewHolder> {


    private Context mContext;
    private List<MaxPePointsData> mMaxPePointsData ;

    public MaxPointsAdapter(Context context ,List<MaxPePointsData> list) {
        this.mContext=context;
        this.mMaxPePointsData=list;
    }

    public void addEvents(List<MaxPePointsData> list){
        this.mMaxPePointsData=list;
        Log.d("pointsAmountTV", String.valueOf(list));
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MaxPointsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.max_points_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaxPointsAdapter.ViewHolder holder, int position) {

        MaxPePointsData data = mMaxPePointsData.get(position);
        holder.points_dateTV.setText(data.getDate_time());

        if (data.getCredit().equals("0.000")){
            holder.pointsAmountTV.setText("- \u20b9" + data.getDebit());
        }
        else {
            holder.pointsAmountTV.setText("+ \u20b9" + data.getCredit());
        }

        holder.pointsUsedTV.setText(data.getType());
        holder.points_orderIdTV.setText("Order Id : " + data.getOrder_id());

    }

    @Override
    public int getItemCount() {
        return mMaxPePointsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        @BindView(R.id.points_usedTV)
        TextView pointsUsedTV;
        @BindView(R.id.points_orderIdTV)
        TextView points_orderIdTV;
        @BindView(R.id.points_dateTV)
        TextView points_dateTV;
        @BindView(R.id.pointsAmountTV)
        TextView pointsAmountTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
