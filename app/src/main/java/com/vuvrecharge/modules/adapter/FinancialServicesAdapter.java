package com.vuvrecharge.modules.adapter;

import static com.vuvrecharge.api.ApiServices.BBPS_IMAGE_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import com.vuvrecharge.modules.model.FinancialServicesData;

import java.util.List;

public class FinancialServicesAdapter extends RecyclerView.Adapter<FinancialServicesAdapter.ViewHolder> {


        Context context;
        List<FinancialServicesData> list;

        ItemClickListener listener;

        public FinancialServicesAdapter(Context context, List<FinancialServicesData> list, ItemClickListener listener) {
            this.context = context;
            this.list = list;
            this.listener = listener;

        }

        public void searchData(List<FinancialServicesData> provider){
            this.list = provider;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.ott_service_item_layout, parent, false)
            );
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.title.setText(list.get(position).getTitle());
            Log.d("FinancialServicesAdapter", String.valueOf(list));
            if(checkString(list.get(position).getTitle()) == true){
                String title = list.get(position).getTitle().replaceAll("(?<!^)(?=[A-Z])", " ");
                holder.title.setText(title);
            }else  {
                holder.title.setText(list.get(position).getTitle());
            }
            Glide.with(context)
                    .load(BBPS_IMAGE_URL+"/"+list.get(position).getLogo())
                    .into(holder.imgLogo);
            holder.itemView.setOnClickListener(v -> {
                if (list.get(position).getUp_down_msg().isEmpty()){
                    listener.onClickListener(list.get(position).getRedirect_link());
                }else {
                    Toast.makeText(context, "Service is down", Toast.LENGTH_SHORT).show();
                }
            });
//            holder.shimmerTextView.setText(list.get(position).getHighlight_text());
//            if (list.get(position).getHighlight_text().isEmpty()){
//                holder.shimmerTextView.setVisibility(View.INVISIBLE);
//            }else {
//                holder.shimmerTextView.setVisibility(View.VISIBLE);
//            }
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

        static class ViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            ImageView imgLogo;
//            ShimmerTextView shimmerTextView;
//            Shimmer shimmer;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.txtTitle);
                imgLogo = itemView.findViewById(R.id.imgLogo);
//                shimmerTextView = itemView.findViewById(R.id.shimmerTextView);
//                shimmer = new Shimmer();
//                shimmer.start(shimmerTextView);
            }
        }

        public interface ItemClickListener{
            void onClickListener(String name);
        }
    }


