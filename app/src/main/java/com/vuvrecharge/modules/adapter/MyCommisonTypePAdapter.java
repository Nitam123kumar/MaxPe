package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.CommissionTypeData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCommisonTypePAdapter extends RecyclerView.Adapter<MyCommisonTypePAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<CommissionTypeData> mycommisonPackageData = new ArrayList<>();
    String typec = "";

    public MyCommisonTypePAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.typedatapackage_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bind(mycommisonPackageData.get(position));

    }

    @Override
    public int getItemCount() {
        return mycommisonPackageData.size();
    }

    public void addEvents(List<CommissionTypeData> mycommisonPackageData, String typec) {
        this.mycommisonPackageData = mycommisonPackageData;
        this.typec = typec;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;
        @BindView(R.id.top)
        TextView top;

        @BindView(R.id.commission)
        TextView commission;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.view)
        View view;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(CommissionTypeData mycommisonPackageData) {
            if (getAdapterPosition() == 0) {
                top.setVisibility(View.VISIBLE);
            } else {
                top.setVisibility(View.GONE);
            }

            commission.setText(mycommisonPackageData.getCommission());
            name.setText(mycommisonPackageData.getName());
        }
    }

}
