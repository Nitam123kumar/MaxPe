package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.activities.AddMemberActivity;
import com.vuvrecharge.modules.activities.MyCommisionPackageActivity;
import com.vuvrecharge.modules.model.DownlineineerData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownlineInerPackageAdapter extends RecyclerView.Adapter<DownlineInerPackageAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    private List<DownlineineerData> mycommisonPackageData = new ArrayList<>();
    String btnText = "";
    String memberType = "";

    public DownlineInerPackageAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.dwnlineinnerpackage_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bind(mycommisonPackageData.get(position));

    }

    @Override
    public int getItemCount() {
        return mycommisonPackageData.size();
    }

    public void addEvents(List<DownlineineerData> mycommisonPackageData, String btnText,String memberType) {
        this.mycommisonPackageData = mycommisonPackageData;
        this.btnText = btnText;
        this.memberType = memberType;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;
        @BindView(R.id.top)
        TextView top;

        @BindView(R.id.commission)
        TextView commission;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.membertype)
        TextView membertype;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bind(DownlineineerData mycommisonPackageData) {
            if (getAdapterPosition() == 0) {
                top.setVisibility(View.VISIBLE);
            } else {
                top.setVisibility(View.GONE);
            }

            membertype.setText(btnText);
            name.setText(mycommisonPackageData.getPackage_name());
            membertype.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, AddMemberActivity.class);
                intent.putExtra("memberType",memberType);
                intent.putExtra("id",mycommisonPackageData.getId());
                intent.putExtra("package_name",mycommisonPackageData.getPackage_name());
                mContext.startActivity(intent);
            });
            commission.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, MyCommisionPackageActivity.class);
                intent.putExtra("id",mycommisonPackageData.getId());
                intent.putExtra("data","Commission "+"("+mycommisonPackageData.getPackage_name()+")");
                mContext.startActivity(intent);
            });

        }
    }

}
