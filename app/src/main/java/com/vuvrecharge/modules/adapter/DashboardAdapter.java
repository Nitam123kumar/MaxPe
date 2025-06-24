package com.vuvrecharge.modules.adapter;

import static com.vuvrecharge.api.ApiServices.BBPS_IMAGE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.DashboardMenu;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardVH> {

    List<DashboardMenu> list;
    Context context;
    ItemClickListener listener;

    public DashboardAdapter( Context context,ItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void addData(List<DashboardMenu> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DashboardAdapter.DashboardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardVH(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardVH holder, int position) {
        DashboardMenu menu = list.get(position);
        if(checkString(list.get(position).getTitle()) == true){
            String title = list.get(position).getTitle().replaceAll("(?<!^)(?=[A-Z])", " ");
            holder.title.setText(title);
        }else {
            holder.title.setText(list.get(position).getTitle());
        }
        holder.imgLogo.setBackgroundResource(R.drawable.rounded_shape);
        if (!menu.getLogo().equals("Bharat Bill Pay")){
            Glide.with(context)
                    .load(BBPS_IMAGE_URL+"/"+menu.getLogo())
                    .into(holder.imgLogo);
        }

        holder.shimmerTextView.setText(menu.getHighlight_text());

        if (menu.getHighlight_text().isEmpty()){
            holder.shimmerTextView.setVisibility(View.INVISIBLE);
        }else {
            holder.shimmerTextView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (menu.getUp_down_msg().isEmpty()){
                listener.onClickListener(menu.getRedirect_link());
            }else {
                Toast.makeText(context, "Service is down", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static boolean checkString(@NonNull String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DashboardVH extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imgLogo;
        ShimmerTextView shimmerTextView;
        Shimmer shimmer;
        public DashboardVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            shimmerTextView = itemView.findViewById(R.id.shimmerTextView);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            shimmer = new Shimmer();
            shimmer.start(shimmerTextView);
        }
    }
    public interface ItemClickListener{
        void onClickListener(String name);
    }
}
