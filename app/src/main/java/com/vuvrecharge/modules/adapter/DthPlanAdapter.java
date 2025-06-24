package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.activities.newActivities.PlanDetailsActivity;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DthPlanAdapter extends RecyclerView.Adapter<DthPlanAdapter.Holder> {

    private LayoutInflater mLayoutInflater;
    DefaultView mDefaultView;
    public JSONArray dataList;

    String operatorId = "";
    String circleId = "";
    String operatorStr = "";
    String circleStr = "";
    String operatorImg = "";
    String mobileNumber = "";

    public DthPlanAdapter(LayoutInflater mLayoutInflater, String operatorId, String circleId,
                          String operatorStr, String circleStr, String operatorImg, String mobileNumber) {
        this.mLayoutInflater = mLayoutInflater;
        this.operatorId = operatorId;
        this.circleId = circleId;
        this.operatorStr = operatorStr;
        this.circleStr = circleStr;
        this.operatorImg = operatorImg;
        this.mobileNumber = mobileNumber;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.dth_plan_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {
            holder.bind(dataList.getJSONObject(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return dataList.length();
    }

    public void addData(JSONArray dataList, DefaultView mDefaultView) {
        this.dataList = dataList;
        this.mDefaultView = mDefaultView;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;

        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.validity)
        TextView validity;

        @BindView(R.id.description)
        TextView description;

        String keyStr = null;
        String amt = null;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(JSONObject resultsBean) {
            try {

                JSONObject obj = resultsBean.getJSONObject("rs");
                Iterator<String> key = obj.keys();
                while (key.hasNext()) {
                    keyStr = key.next();
                    validity.setText(keyStr);
                    amt = obj.getString(keyStr);
                    amount.setText("\u20b9 "+amt);
                }

                Log.d("TAG_DATA", "bind: "+resultsBean);
                description.setText(resultsBean.getString("plan_name") + "\n"+resultsBean.getString("desc").replaceAll("&amp;", "&").trim());
                Log.d("TAG_DATA", "onCreate: "+mobileNumber);
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, PlanDetailsActivity.class);
                    intent.putExtra("amount",amt);
                    intent.putExtra("number",mobileNumber);
                    intent.putExtra("url",operatorImg);
                    intent.putExtra("provider",operatorStr);
                    intent.putExtra("state",circleStr);
                    intent.putExtra("des",description.getText().toString().trim());
                    intent.putExtra("validity",validity.getText().toString().trim());
                    intent.putExtra("data","NA");
                    intent.putExtra("operator",operatorId);
                    intent.putExtra("circle",circleId);
                    intent.putExtra("pageType","DTH");
                    mContext.startActivity(intent);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            // Create an object of CustomAdapter and set Adapter to GirdView

        }

    }
}
