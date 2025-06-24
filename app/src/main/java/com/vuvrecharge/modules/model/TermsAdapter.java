package com.vuvrecharge.modules.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<FundRequestData> mFundRequestData = new ArrayList<>();

    public TermsAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.terms_item_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bind(mFundRequestData.get(position));

    }

    @Override
    public int getItemCount() {
        return mFundRequestData.size();
    }

    public void addEvents(List<FundRequestData> mFundRequestData) {
        this.mFundRequestData = mFundRequestData;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;
        @BindView(R.id.tvQuestion)
        TextView tvQuestion;
        @BindView(R.id.tvAnswer)
        TextView tvAnswer;


        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(FundRequestData mFundRequestData) {

            tvQuestion.setText( mFundRequestData.getQuestion());
            tvAnswer.setText(mFundRequestData.getAnswer());





        }
    }

}
