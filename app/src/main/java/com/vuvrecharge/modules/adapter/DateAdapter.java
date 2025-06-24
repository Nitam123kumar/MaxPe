package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.modules.model.DateData;
import com.vuvrecharge.modules.view.DefaultView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<DateData> mDateData = new ArrayList<>();

    int scroll_position = -1;
    RecyclerView recyclerView;
    DefaultView mDefaultView;

    public DateAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.date_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bind(mDateData.get(position));

    }

    @Override
    public int getItemCount() {
        return mDateData.size();
    }

    public void addData(List<DateData> mDateData, int scroll_position,
                        RecyclerView recyclerView, DefaultView mDefaultView) {
        this.mDateData = mDateData;
        this.scroll_position = scroll_position;
        this.recyclerView = recyclerView;
        this.mDefaultView = mDefaultView;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;

        @BindView(R.id.left)
        TextView left;
        @BindView(R.id.title)
        TextView title;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(DateData mDateData) {

            if (getAdapterPosition() == 0) {
                left.setVisibility(View.VISIBLE);
            } else {
                left.setVisibility(View.GONE);
            }

            Date date = mDateData.getDate();

            if (BaseMethod.dateFormat.format(date).equals(BaseMethod.dateFormat.format(new Date()))) {
                title.setText("Today");
            } else {
                title.setText(BaseMethod.dateFormat.format(date));
            }

            if (scroll_position == getAdapterPosition()) {
                title.setBackground(mContext.getResources().getDrawable(R.drawable.corner_circle_primary_dark));
            } else {
                title.setBackground(mContext.getResources().getDrawable(R.drawable.corner_circle_primary));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scroll_position = getAdapterPosition();
                    notifyDataSetChanged();
                    recyclerView.scrollToPosition(scroll_position);
                    mDefaultView.onSuccessOther((mDateData.getDate().getTime()) + "");
                }
            });

        }
    }

}
