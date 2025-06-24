package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.DownlinePackageData;
import com.vuvrecharge.modules.model.DownlineineerData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownlinePackageAdapter extends RecyclerView.Adapter<DownlinePackageAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<DownlinePackageData> mycommisonPackageData = new ArrayList<>();

    public DownlinePackageAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.downlinepackage_row, parent, false);
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

    public void addEvents(List<DownlinePackageData> mycommisonPackageData) {
        this.mycommisonPackageData = mycommisonPackageData;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;
        @BindView(R.id.top)
        TextView top;
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.type_tets)
        TextView type_tets;
        @BindView(R.id.list_item)
        RecyclerView list_item;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(DownlinePackageData mycommisonPackageData) {

            if (getAdapterPosition() == 0) {
                top.setVisibility(View.VISIBLE);
            } else {
                top.setVisibility(View.GONE);
            }
            type.setText(mycommisonPackageData.getType());

            type_tets.setVisibility(View.GONE);
            DownlineInerPackageAdapter downlineInerPackageAdapter = new DownlineInerPackageAdapter(mLayoutInflater);
            list_item.setHasFixedSize(true);
            list_item.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
            list_item.setAdapter(downlineInerPackageAdapter);
            List<DownlineineerData> allGroupOptions = mycommisonPackageData.getData();
            downlineInerPackageAdapter.addEvents(allGroupOptions, mycommisonPackageData.getBtnText(),mycommisonPackageData.getMemberType());
        }
    }
}
