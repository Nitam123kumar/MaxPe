package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.ExtraCashBackPoints;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExtraCashBackAdapter extends RecyclerView.Adapter<ExtraCashBackAdapter.ViewHolder> {

    Context context;
    List<ExtraCashBackPoints> list;

    private String currentAmount = "";

    public ExtraCashBackAdapter(Context context, List<ExtraCashBackPoints> list) {
        this.context = context;
        this.list = list;
    }

    public void filterByAmount(String amount) {
        this.currentAmount = amount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExtraCashBackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_applied_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraCashBackAdapter.ViewHolder holder, int position) {
        ExtraCashBackPoints extraCashBackPoints = list.get(position);
        holder.extraPointsTxt.setText("Extra "+extraCashBackPoints.getBonus()+" points");
        holder.addTargetAmount.setText("Add ₹"+extraCashBackPoints.getMin()+" or More");

        try {
            if (!currentAmount.isEmpty()) {
                int enteredAmount = Integer.parseInt(currentAmount);
                int itemMin = Integer.parseInt(extraCashBackPoints.getMin());
                int itemMax = Integer.parseInt(extraCashBackPoints.getMax());

                boolean isLastItem = (position == list.size() - 1);

                if (enteredAmount >= itemMin && enteredAmount <= itemMax || (isLastItem && enteredAmount > itemMax)) {
                    holder.applied_amount.setVisibility(View.VISIBLE);
                    holder.select.setBackgroundResource(R.drawable.discount_applied_shape);
                }
           else {
                holder.applied_amount.setVisibility(View.GONE);
                holder.select.setBackgroundResource(R.drawable.apply_discount_shape);
            }
            } else {
                holder.applied_amount.setVisibility(View.GONE);
                holder.select.setBackgroundResource(R.drawable.apply_discount_shape);
            }
        } catch (Exception e) {
            holder.applied_amount.setVisibility(View.GONE);
            holder.select.setBackgroundResource(R.drawable.apply_discount_shape);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.extraPointsTxt)
        TextView extraPointsTxt;
        @BindView(R.id.applied_amount)
        TextView applied_amount;
        @BindView(R.id.addTargetAmount)
        TextView addTargetAmount;
        @BindView(R.id.select)
        ConstraintLayout select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
