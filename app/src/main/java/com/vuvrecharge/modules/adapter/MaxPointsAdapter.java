package com.vuvrecharge.modules.adapter;

import android.content.Context;
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


    private LayoutInflater mLayoutInflater;

    private List<MaxPePointsData> mMaxPePointsData = new ArrayList<>();

    public MaxPointsAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }


    @NonNull
    @Override
    public MaxPointsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.max_points_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaxPointsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mMaxPePointsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       private Context mContext;

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
            mContext=itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

    }
}
