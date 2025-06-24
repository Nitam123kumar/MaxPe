package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.Follows;

import java.util.ArrayList;

public class FollowsAdapter extends RecyclerView.Adapter<FollowsAdapter.FollowsVH> {

    Context context;
    ArrayList<Follows> list;
    OnClickListener listener;

    public FollowsAdapter(Context context, OnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void addData(ArrayList<Follows> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowsAdapter.FollowsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FollowsVH(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_us_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FollowsAdapter.FollowsVH holder, int position) {
        Follows follow = list.get(position);

        holder.txtFollow.setText(follow.getTitle());
        Glide.with(context)
                .load(follow.getLogo())
                .into(holder.imgFollow);
        holder.itemView.setOnClickListener(v -> listener.onFollowLinkClick(follow.getLink()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FollowsVH extends RecyclerView.ViewHolder {

        ImageView imgFollow;
        TextView txtFollow;
        public FollowsVH(@NonNull View itemView) {
            super(itemView);
            imgFollow = itemView.findViewById(R.id.imgFollow);
            txtFollow = itemView.findViewById(R.id.txtFollow);
        }
    }

    public interface OnClickListener{
        void onFollowLinkClick(String link);
    }
}
