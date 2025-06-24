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
import com.vuvrecharge.modules.model.CircleData;
import com.vuvrecharge.modules.model.OperatorData;

import java.util.List;

public class SearchableSpinnerCircleAdapter extends RecyclerView.Adapter<SearchableSpinnerCircleAdapter.SearchableSpinnerCircleViewHolder>{
    Context context;
    List<CircleData> list;
    OnItemClickListeners listener;

    public SearchableSpinnerCircleAdapter(Context context, List<CircleData> list, OnItemClickListeners listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchableSpinnerCircleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchableSpinnerCircleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_searchable_spinner_layout, parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SearchableSpinnerCircleViewHolder holder, int position) {
        holder.txtTitle.setText(list.get(position).getState_name());
        holder.imgProvider.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(list.get(position), position));
    }
    public void searchData(List<CircleData> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SearchableSpinnerCircleViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProvider;
        TextView txtTitle;
        View viewLine;
        public SearchableSpinnerCircleViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProvider = itemView.findViewById(R.id.imgProvider);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            viewLine = itemView.findViewById(R.id.viewLine);
        }
    }

    public interface OnItemClickListeners{
        void onItemClick(CircleData model, int position);
    }
}
