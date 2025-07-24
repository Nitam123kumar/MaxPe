package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.SpotlightData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SpotlightServicesAdapter  extends RecyclerView.Adapter<SpotlightServicesAdapter.SpotlightViewHolder>{

    Context mCOntext;
    List<SpotlightData> spotlight_servicesList;
    List<String> servicesList;
    ItemClickListener listener;
    public SpotlightServicesAdapter(Context context,List<String> stringList, List<SpotlightData> List, ItemClickListener listener) {
        this.mCOntext = context;
        this.spotlight_servicesList = List;
        this.servicesList= stringList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public SpotlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCOntext).inflate(R.layout.spotlight_services_row, parent, false);
        return new SpotlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpotlightViewHolder holder, int position) {

        SpotlightData spotlightData = spotlight_servicesList.get(position);
        holder.spotlight_servicesTxt.setText(spotlightData.getTitle());
        Glide.with(mCOntext).load(spotlightData.getLogo()).into(holder.imgSlide);

        if (spotlightData.getTitle().equals("DTH")){
            holder.prepaid.setBackgroundResource(R.drawable.dth_shape);
        } else if (spotlightData.getTitle().equals("Electricity") ) {
            holder.prepaid.setBackgroundResource(R.drawable.electricity_shape);
        }else if (spotlightData.getTitle().equals("Credit Card") ) {
            holder.prepaid.setBackgroundResource(R.drawable.credit_card_shape);
        }

        holder.itemView.setOnClickListener(v -> {
            try {
                JSONObject object = new JSONObject(spotlightData.getData());
                listener.onClickListener(spotlightData.getType(),object.getString("intent_name"),object.getString("extra_data"),spotlightData.getRedirectLink());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public int getItemCount() {
        return spotlight_servicesList.size();

    }

    static class SpotlightViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSlide;
        TextView spotlight_servicesTxt;
        LinearLayout prepaid;
        SpotlightViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlide = itemView.findViewById(R.id.spotlight_services_img);
            spotlight_servicesTxt = itemView.findViewById(R.id.spotlight_servicesTxt);
            prepaid = itemView.findViewById(R.id.prepaid);
        }
    }
    public interface ItemClickListener{
        void onClickListener(String redirection_type,String intent_name,String extra_data,String link) throws ClassNotFoundException;
    }

}
