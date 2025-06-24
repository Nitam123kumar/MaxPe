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

public class AllServiceAdapter extends RecyclerView.Adapter<AllServiceAdapter.AllServiceVH> {

    Context context;
    List<DashboardMenu> list;
    ItemClickListener listener;

    public AllServiceAdapter(Context context, List<DashboardMenu> list, ItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AllServiceAdapter.AllServiceVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllServiceVH(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.bbps_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AllServiceAdapter.AllServiceVH holder, int position) {
        DashboardMenu menu = list.get(position);
        if(checkString(list.get(position).getTitle()) == true){
            String title = list.get(position).getTitle().replaceAll("(?<!^)(?=[A-Z])", " ");
            holder.title.setText(title);
        }else  {
            holder.title.setText(list.get(position).getTitle());
        }

        holder.shimmerTextView.setText(menu.getHighlight_text());
        Glide.with(context)
                .load(BBPS_IMAGE_URL+"/"+menu.getLogo())
                .into(holder.imgLogo);

        if (menu.getHighlight_text().equals("")){
            holder.shimmerTextView.setVisibility(View.INVISIBLE);
        }else {
            holder.shimmerTextView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (menu.getUp_down_msg().equals("")){
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

    public class AllServiceVH extends RecyclerView.ViewHolder {

        TextView title;
        ImageView imgLogo;
        ShimmerTextView shimmerTextView;
        Shimmer shimmer;
        public AllServiceVH(@NonNull View itemView) {
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
