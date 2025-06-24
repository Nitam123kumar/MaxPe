package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.ContactData;
import com.vuvrecharge.modules.view.DefaultView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<ContactData> dataList = new ArrayList<>();
    private List<ContactData> dataListFiltered = new ArrayList<>();

    DefaultView mDefaultView;

    public ContactAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.contact_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bind(dataListFiltered.get(position));

    }

    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }

    public void addEvents(List<ContactData> dataList, DefaultView mDefaultView) {
        this.dataList = dataList;
        this.dataListFiltered = dataList;
        this.mDefaultView = mDefaultView;
        notifyDataSetChanged();
    }

    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {

                dataListFiltered = (List<ContactData>) results.values;
                notifyDataSetChanged();
            }

            @NonNull
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                List<ContactData> callLogData = new ArrayList<>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < dataList.size(); i++) {
                    if (constraint.toString().matches("[0-9]+")) {
                        String number = dataList.get(i).getNumber();
                        if (number.toLowerCase().contains(constraint.toString())) {
                            callLogData.add(dataList.get(i));
                        }
                    } else {
                        String name = dataList.get(i).getName();
                        if (name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            callLogData.add(dataList.get(i));
                        }
                    }
                }

                results.count = callLogData.size();
                results.values = callLogData;

                return results;
            }
        };

        return filter;
    }


    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;
        @BindView(R.id.top)
        TextView top;
        @BindView(R.id.footer)
        TextView footer;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.number)
        TextView number;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(ContactData dataList) {

            if (getAdapterPosition() == 0) {
                top.setVisibility(View.GONE);
            } else {
                top.setVisibility(View.GONE);
            }

            if (getAdapterPosition() == (dataListFiltered.size() - 1)) {
                footer.setVisibility(View.VISIBLE);
            } else {
                footer.setVisibility(View.GONE);
            }
            if (dataList.getName().equals("")) {
                name.setText("Unknown Person");
            } else {
                name.setText(dataList.getName());
            }
            number.setText(dataList.getNumber());
            itemView.setOnClickListener(v -> {
                try {
                    mDefaultView.onSuccessOther(dataList.getNumber());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
