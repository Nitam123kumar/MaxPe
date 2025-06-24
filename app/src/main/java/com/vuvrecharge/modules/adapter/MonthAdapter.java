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
import com.vuvrecharge.modules.model.MonthData;
import com.vuvrecharge.modules.view.DefaultView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<MonthData> mMonthData = new ArrayList<>();

    int scroll_position = -1;
    RecyclerView recyclerView;
    DefaultView mDefaultView;

    public MonthAdapter(LayoutInflater mLayoutInflater) {
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

        holder.bind(mMonthData.get(position));

    }

    @Override
    public int getItemCount() {
        return mMonthData.size();
    }

    public void addData(List<MonthData> mMonthData, int scroll_position,
                        RecyclerView recyclerView, DefaultView mDefaultView) {
        this.mMonthData = mMonthData;
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

        public void bind(MonthData mMonthData) {

            if (getAdapterPosition() == 0) {
                left.setVisibility(View.VISIBLE);
            } else {
                left.setVisibility(View.GONE);
            }

            Date today = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            Date start_date = calendar.getTime();

            if (BaseMethod.month.format(start_date).equals(mMonthData.getMonth())) {
                title.setText("This Month");
            } else {
                title.setText(mMonthData.getMonth());
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
                    mDefaultView.onSuccessOther((mMonthData.getStart_date().getTime()) + "",
                            (mMonthData.getEnd_date().getTime()) + "");
                }
            });

        }
    }

}
