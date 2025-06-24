package com.vuvrecharge.modules.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.BillInfo;

import java.util.List;

public class BillerInfoAdapter extends RecyclerView.Adapter<BillerInfoAdapter.BillerInfoVH> {

    private List<BillInfo> infos;
    final private String title;
    final private String type;

    public BillerInfoAdapter(List<BillInfo> infos, String title, String type) {
        this.infos = infos;
        this.title = title;
        this.type = type;
    }

    @NonNull
    @Override
    public BillerInfoAdapter.BillerInfoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillerInfoVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rv_biller_info,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BillerInfoAdapter.BillerInfoVH holder, int position) {
            holder.bindView(infos.get(position));
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class BillerInfoVH extends RecyclerView.ViewHolder {
        TextView txtCustomerInfo,txtCustomerInfoValue;
        public BillerInfoVH(@NonNull View itemView) {
            super(itemView);
            txtCustomerInfo = itemView.findViewById(R.id.txtCustomerInfo);
            txtCustomerInfoValue = itemView.findViewById(R.id.txtCustomerInfoValue);
//            txtBillNumber = itemView.findViewById(R.id.txtBillNumber);
//            txtBillNumberValue = itemView.findViewById(R.id.txtBillNumberValue);
//            txtBillDate = itemView.findViewById(R.id.txtBillDate);
//            txtBillDateValue = itemView.findViewById(R.id.txtBillDateValue);
//            txtDueDate = itemView.findViewById(R.id.txtDueDate);
//            txtDueDateValue = itemView.findViewById(R.id.txtDueDateValue);
//            txtAmount = itemView.findViewById(R.id.txtAmount);
//            txtAmountValue = itemView.findViewById(R.id.txtAmountValue);
//            viewBill = itemView.findViewById(R.id.viewBill);
        }

        public void bindView(@NonNull BillInfo info){
//            if (info.getCustomerName().equals("") || info.getCustomerName().isEmpty()){
//                txtCustomerInfo.setVisibility(GONE);
//                txtCustomerInfoValue.setVisibility(GONE);
//            }else {
//                txtCustomerInfo.setVisibility(VISIBLE);
//                txtCustomerInfoValue.setVisibility(VISIBLE);
//                txtCustomerInfoValue.setText(info.getCustomerName());
//            }
//
//            if (info.getBillNumber().equals("") || info.getBillNumber().isEmpty()){
//                txtBillNumber.setVisibility(GONE);
//                txtBillNumberValue.setVisibility(GONE);
//            }else {
//                txtBillNumber.setVisibility(VISIBLE);
//                txtBillNumberValue.setVisibility(VISIBLE);
//                txtBillNumberValue.setText(info.getBillNumber());
//            }
//
//            if (info.getBillDate().equals("") || info.getBillDate().isEmpty()){
//                txtBillDate.setVisibility(GONE);
//                txtBillDateValue.setVisibility(GONE);
//            }else {
//                txtBillDate.setVisibility(VISIBLE);
//                txtBillDateValue.setVisibility(VISIBLE);
//                txtBillDateValue.setText(info.getBillDate());
//            }
//
//            if (info.getDueDate().equals("") || info.getDueDate().isEmpty()){
//                txtDueDate.setVisibility(GONE);
//                txtDueDateValue.setVisibility(GONE);
//            }else {
//                txtDueDate.setVisibility(VISIBLE);
//                txtDueDateValue.setVisibility(VISIBLE);
//                txtDueDateValue.setText(info.getDueDate());
//            }
//
//            if (type.equals("Fastag")){
//                if (info.getWalletBalance().equals("") || info.getWalletBalance().isEmpty()){
//                    txtAmount.setVisibility(GONE);
//                    txtAmountValue.setVisibility(GONE);
//                    viewBill.setVisibility(GONE);
//                }else {
//                    txtAmount.setText("Available Amount");
//                    txtAmount.setVisibility(VISIBLE);
//                    txtAmountValue.setVisibility(VISIBLE);
//                    txtAmountValue.setText("Rs. "+info.getWalletBalance());
//                    viewBill.setVisibility(VISIBLE);
//                }
//            }
//            else {
//                if (info.getBillAmount().equals("") || info.getBillAmount().isEmpty()){
//                    txtAmount.setVisibility(GONE);
//                    txtAmountValue.setVisibility(GONE);
//                    viewBill.setVisibility(GONE);
//                }else {
//                    viewBill.setVisibility(VISIBLE);
//                    txtAmount.setVisibility(VISIBLE);
//                    txtAmountValue.setVisibility(VISIBLE);
//                    txtAmountValue.setText("Rs. "+info.getBillAmount());
//                }
//
//            }

        }
    }
}
