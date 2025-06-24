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
import com.vuvrecharge.modules.model.PaymentData;
import com.vuvrecharge.modules.model.PaymentModeData;

import java.util.List;

public class SearchableSpinneraBankSelectionAdapter extends RecyclerView.Adapter<SearchableSpinneraBankSelectionAdapter.SearchableSpinnerBankSelectionViewHolder>{

    Context context;
    List<PaymentData> list;
    OnItemClickListeners listener;

    public SearchableSpinneraBankSelectionAdapter(Context context, List<PaymentData> list, OnItemClickListeners listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchableSpinnerBankSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchableSpinnerBankSelectionViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_searchable_spinner_layout, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SearchableSpinnerBankSelectionViewHolder holder, int position) {
        holder.txtTitle.setText(list.get(position).getBank_name()+" ("+list.get(position).getAc_no()+")");
        holder.imgProvider.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(list.get(position), position));
    }

    public void searchData(List<PaymentData> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SearchableSpinnerBankSelectionViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProvider;
        TextView txtTitle;
        View viewLine;
        public SearchableSpinnerBankSelectionViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProvider = itemView.findViewById(R.id.imgProvider);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            viewLine = itemView.findViewById(R.id.viewLine);
        }
    }

    public interface OnItemClickListeners{
        void onItemClick(PaymentData model, int position);
    }
}
