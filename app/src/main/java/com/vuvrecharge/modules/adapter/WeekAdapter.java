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
import com.vuvrecharge.modules.model.WeekData;
import com.vuvrecharge.modules.view.DefaultView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<WeekData> mWeekData = new ArrayList<>();

    int scroll_position = -1;
    RecyclerView recyclerView;
    DefaultView mDefaultView;

    public WeekAdapter(LayoutInflater mLayoutInflater) {
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

        holder.bind(mWeekData.get(position));

    }

    @Override
    public int getItemCount() {
        return mWeekData.size();
    }

    public void addData(List<WeekData> mWeekData, int scroll_position,
                        RecyclerView recyclerView, DefaultView mDefaultView) {
        this.mWeekData = mWeekData;
        this.scroll_position = scroll_position;
        this.mDefaultView = mDefaultView;
        this.recyclerView = recyclerView;
        notifyDataSetChanged();
    }

    public List<WeekData> getData() {
        return mWeekData;
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

        public void bind(WeekData mWeekData) {

            if (getAdapterPosition() == 0) {
                left.setVisibility(View.VISIBLE);
            } else {
                left.setVisibility(View.GONE);
            }

            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            c.setTime(new Date());
            c.setFirstDayOfWeek(Calendar.MONDAY);
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());

            Date start = c.getTime();

            c.add(Calendar.DAY_OF_YEAR, 6);
            Date end = c.getTime();

            Date start_date = mWeekData.getStart_date();
            Date end_date = mWeekData.getEnd_date();

            if (BaseMethod.format1.format(start_date).equals(BaseMethod.format1.format(start)) &&
                    BaseMethod.format1.format(end_date).equals(BaseMethod.format1.format(end))) {
                title.setText("This Week");
            } else {
                title.setText(BaseMethod.dateFormat.format(start_date) + " - " + BaseMethod.dateFormat.format(end_date));
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
                    mDefaultView.onSuccessOther((mWeekData.getStart_date().getTime()) + "",
                            (mWeekData.getEnd_date().getTime()) + "");
                }
            });

        }
    }

}
