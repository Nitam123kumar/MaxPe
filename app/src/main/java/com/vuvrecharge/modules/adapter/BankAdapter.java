package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.PaymentData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.Holder> {

    private LayoutInflater mLayoutInflater;

    public List<PaymentData> dataList = new ArrayList<>();

    public BankAdapter(LayoutInflater mLayoutInflater) {
        this.mLayoutInflater = mLayoutInflater;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.bank_row, parent, false);
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

    public void addData(List<PaymentData> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private Context mContext;

        @BindView(R.id.txtCopy1)
        TextView txtCopy1;
        @BindView(R.id.txtCopy2)
        TextView txtCopy2;
        @BindView(R.id.txtCopy3)
        TextView txtCopy3;
        @BindView(R.id.txtCopy4)
        TextView txtCopy4;
        @BindView(R.id.txtBankNameValue)
        TextView txtBankNameValue;
        @BindView(R.id.txtAccountHolderValue)
        TextView txtAccountHolderValue;
        @BindView(R.id.txtAccountNumberValue)
        TextView txtAccountNumberValue;
        @BindView(R.id.txtIFSCCodeValue)
        TextView txtIFSCCodeValue;
        @BindView(R.id.txtAccountTypeValue)
        TextView txtAccountTypeValue;


        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);


//            txtCopy2.setOnClickListener(this);
//            txtCopy3.setOnClickListener(this);
//            txtCopy4.setOnClickListener(this);
        }

        public void bind(PaymentData resultsBean) {

//            if (getAdapterPosition() == 0) {
//                top.setVisibility(View.GONE);
//            } else {
//                top.setVisibility(View.VISIBLE);
//            }
//            bank_count.setText("BANK DETAILS (" + (getAdapterPosition() + 1) + ")");
//            bank_name.setText(resultsBean.getBank_name());
            txtBankNameValue.setText(resultsBean.getBank_name());
//            ac_holder.setText(resultsBean.getAc_holder());
            txtAccountHolderValue.setText(resultsBean.getAc_holder());
//            ac_no.setText(resultsBean.getAc_no());
            txtAccountNumberValue.setText(resultsBean.getAc_no());
//            ifsc_code.setText(resultsBean.getIfsc_code());
            txtIFSCCodeValue.setText(resultsBean.getIfsc_code());
//            ac_type.setText(resultsBean.getAc_type());
            txtAccountTypeValue.setText(resultsBean.getAc_type());
//            copy_account.setOnClickListener(v -> {
//            });

            txtCopy1.setOnClickListener(v -> copyCode(resultsBean.getBank_name()));
            txtCopy2.setOnClickListener(v -> copyCode(resultsBean.getAc_holder()));
            txtCopy3.setOnClickListener(v -> copyCode(resultsBean.getAc_no()));
            txtCopy4.setOnClickListener(v -> copyCode(resultsBean.getIfsc_code()));
        }

        private void copyCode(String text){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text ", text);
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(mContext, "Copied "+text, Toast.LENGTH_SHORT).show();
        }

    }

}
