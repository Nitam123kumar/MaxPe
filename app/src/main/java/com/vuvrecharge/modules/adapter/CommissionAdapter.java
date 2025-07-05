package com.vuvrecharge.modules.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.DTHCommission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommissionAdapter extends RecyclerView.Adapter<CommissionAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<DTHCommission> dataList = new ArrayList<>();

    String url = "";

    public CommissionAdapter(LayoutInflater mLayoutInflater, String url) {
        this.mLayoutInflater = mLayoutInflater;
        this.url = url;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.rv_commission_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addEvents(List<DTHCommission> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private Context mContext;

        @BindView(R.id.imgLogo)
        CircleImageView imgLogo;

        @BindView(R.id.hightLight)
        ConstraintLayout hightLight;
        @BindView(R.id.txtOperator)
        TextView txtOperator;
        @BindView(R.id.txtOperatorValue)
        TextView txtOperatorValue;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(@NonNull DTHCommission mCommissionData) {
            txtOperator.setText(mCommissionData.getName());
            txtOperatorValue.setText(mCommissionData.getCommission());
            Glide.with(mContext)
                    .load(url+"/"+mCommissionData.getLogo())
                    .into(imgLogo);

            if (mCommissionData.getName().equals("Vi ( Voda & Idea )")
                    || mCommissionData.getName().equals("VIDEOCON DTH TV")
                    || mCommissionData.getName().equals("AIRTEL DIGITAL DTH TV")
                    ){
                hightLight.setVisibility(GONE);
            }
            else {
                hightLight.setVisibility(VISIBLE);
            }


        }
    }

    private void setScaleAnimation(@NonNull View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }

}
