package com.vuvrecharge.modules.adapter;

import static com.vuvrecharge.api.ApiServices.SUBSCRIPTION_IMAGE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.Subscriptions;

import java.util.ArrayList;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubcriptionViewHolder> {
    ArrayList<Subscriptions> list;
    Context context;

    public SubscriptionAdapter(ArrayList<Subscriptions> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SubscriptionAdapter.SubcriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubcriptionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionAdapter.SubcriptionViewHolder holder, int position) {
        Subscriptions model = list.get(position);
        Glide.with(context)
                .load(SUBSCRIPTION_IMAGE_URL+model.getLogo())
                .into(holder.imgView);
        holder.txtSubscriptionTitle.setText(model.getOtt_name());
        if (model.getOtt_desc().equals("") || model.getOtt_desc().equals("N/A")){
            holder.txtSubscriptionDesc.setVisibility(View.GONE);
        }else {
            holder.txtSubscriptionDesc.setVisibility(View.VISIBLE);
            holder.txtSubscriptionDesc.setText(model.getOtt_desc());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubcriptionViewHolder extends RecyclerView.ViewHolder {

        CardView cardImageView;
        ImageView imgView;
        TextView txtSubscriptionTitle;
        TextView txtSubscriptionDesc;

        public SubcriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.cardImageView);
            imgView = itemView.findViewById(R.id.imgView);
            txtSubscriptionTitle = itemView.findViewById(R.id.txtSubscriptionTitle);
            txtSubscriptionDesc = itemView.findViewById(R.id.txtSubscriptionDesc);
        }
    }
}
