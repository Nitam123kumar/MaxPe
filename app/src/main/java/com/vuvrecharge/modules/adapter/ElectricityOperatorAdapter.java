package com.vuvrecharge.modules.adapter;

import static com.vuvrecharge.api.ApiServices.IMAGE_LOGO;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.activities.newActivities.ElectricityBillPayActivity;
import com.vuvrecharge.modules.model.OperatorData;

import java.util.ArrayList;

public class ElectricityOperatorAdapter extends RecyclerView.Adapter<ElectricityOperatorAdapter.ElectricityOperatorViewHolder> {
    private AppCompatActivity activity;
    private ArrayList<OperatorData> operatorData;

    String warning_message;

    String title;
    String type;

    public ElectricityOperatorAdapter(AppCompatActivity activity, ArrayList<OperatorData> operatorData,
                                      String warning_message, String title, String type) {
        this.activity = activity;
        this.operatorData = operatorData;
        this.warning_message = warning_message;
        this.title = title;
        this.type = type;
    }

    public void searchList(ArrayList<OperatorData> data){
        this.operatorData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ElectricityOperatorAdapter.ElectricityOperatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ElectricityOperatorViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_electricity_data_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ElectricityOperatorAdapter.ElectricityOperatorViewHolder holder, int position) {
        holder.bindView(operatorData.get(position));
    }

    @Override
    public int getItemCount() {
        return operatorData.size();
    }

    public class ElectricityOperatorViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        ImageView operator_img;

        public ElectricityOperatorViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.title);
            operator_img = itemView.findViewById(R.id.operator_img);
        }

        public void bindView(@NonNull OperatorData data) {
            txtTitle.setText(data.getName());
            String url = IMAGE_LOGO;
            if (data.getLogo() != null){
                Glide.with(activity)
                        .load(url+"/"+data.getLogo())
                        .into(operator_img);
            }else {
                if (type.equals("Landline")){
                    Glide.with(activity)
                            .load(R.drawable.landline_1)
                            .into(operator_img);
                }
            }

            itemView.setOnClickListener(v -> activity.startActivity(new Intent(activity, ElectricityBillPayActivity.class)
                    .putExtra("name", data.getName())
                    .putExtra("logo", url+"/"+data.getLogo())
                    .putExtra("id", data.getId())
                    .putExtra("type", type)
                    .putExtra("warning_message",warning_message)
                    .putExtra("title",title)
            ));
        }
    }
}
