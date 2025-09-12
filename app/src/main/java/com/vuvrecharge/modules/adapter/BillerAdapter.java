package com.vuvrecharge.modules.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.BillFetch;

import java.util.List;

public class BillerAdapter extends RecyclerView.Adapter<BillerAdapter.BillerVH>  {
    private List<BillFetch> infos;
    final private String title;
    final private String type;
     int size;
    public BillerAdapter(List<BillFetch> infos, String title, String type, int size) {
        this.infos = infos;
        this.title = title;
        this.type = type;
        this.size = size;
    }

    @NonNull
    @Override
    public BillerAdapter.BillerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillerAdapter.BillerVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rv_biller_info,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BillerAdapter.BillerVH holder, int position) {
        BillFetch item = infos.get(position); // ✅ safe now
        holder.txtCustomerInfo.setText(item.getKey());
        holder.txtCustomerInfoValue.setText(item.getValue());
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class BillerVH extends RecyclerView.ViewHolder {
        TextView txtCustomerInfo,txtCustomerInfoValue;
        public BillerVH(@NonNull View itemView) {
            super(itemView);
            txtCustomerInfo = itemView.findViewById(R.id.txtCustomerInfo);
            txtCustomerInfoValue = itemView.findViewById(R.id.txtCustomerInfoValue);
        }

        public void bindView(@NonNull BillFetch info){
            txtCustomerInfo.setText(info.getKey());
            txtCustomerInfoValue.setText(info.getValue());
        }
    }
}
