package com.vuvrecharge.modules.adapter;

import static com.vuvrecharge.api.ApiServices.IMAGE_SLIDER;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vuvrecharge.R;
import com.vuvrecharge.modules.activities.BillActivity;
import com.vuvrecharge.modules.activities.RechargeActivity;
import com.vuvrecharge.modules.activities.newActivities.ElectricityActivity;
import com.vuvrecharge.modules.activities.newActivities.ElectricityBillPayActivity;
import com.vuvrecharge.modules.model.SliderItems;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    Context context;
    private final List<SliderItems> sliderItems;
    private final ViewPager2 viewPager2;
    public SliderAdapter(Context context, List<SliderItems> sliderItems, ViewPager2 viewPager2) {
        this.context = context;
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }
    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false));
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
        }
    };
    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
        if (position == sliderItems.size() - 2){
            viewPager2.post(runnable);
        }
    }
    @Override
    public int getItemCount() {
        return sliderItems.size();
    }
    class SliderViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView imageView;
        TextView txtTitle;
        TextView txtTitleSub;
        TextView btnClick;
        ConstraintLayout constraint_layout;
        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtTitleSub = itemView.findViewById(R.id.txtTitleSub);
            btnClick = itemView.findViewById(R.id.btnClick);
            constraint_layout = itemView.findViewById(R.id.constraint_layout);
        }
        void setImage(@NonNull SliderItems sliderItems){
                //use glide or picasso in case you get image from internet
            txtTitle.setText(sliderItems.getDiscount_text());
            txtTitleSub.setText(sliderItems.getRecharge_text());
            btnClick.setText(sliderItems.getRecharge_btn_text());
            if (!sliderItems.getColor_code().isEmpty()){
                if (sliderItems.getColor_code().contains("#")){
                    constraint_layout.setBackgroundColor(Color.parseColor(sliderItems.getColor_code()));
                }
            }
            Glide.with(context)
                    .load(IMAGE_SLIDER+"/"+sliderItems.getRecharge_logo())
                    .into(imageView);
            btnClick.setOnClickListener(v -> {
                    Intent intent = null;
                    if (sliderItems.getRedirect().equals("Postpaid")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Postpaid Recharge");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("DTH")){
                        intent = new Intent(context,RechargeActivity.class);
                        intent.putExtra("title", "DTH Recharge");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Landline")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Broadband/Landline");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Prepaid")){
                        intent = new Intent(context,RechargeActivity.class);
                        intent.putExtra("title", "Prepaid Recharge");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Electricity")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Electricity Bill");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Gas")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Gas Bill");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Water")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Water Bill");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Google Play")){
                        intent = new Intent(context, BillActivity.class);
                        intent.putExtra("title", "Purchase Gift cards");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Insurance")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Insurance");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Fastag")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Fastag");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Cylinder")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Cylinder Bill");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("CreditCardPayment")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Credit Card Payment");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("LoanRePayment")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Loan Re payment");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("CableTV")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Cable TV");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("MunicipalTax")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Municipal Tax");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("HousingSociety")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Housing Society");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("ClubAssociation")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Club Association");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("HospitalPathology")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Hospital Pathology");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Subscriptions")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Subscription Fees");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Donation")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Donation");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("RecurringDeposit")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Recurring Deposit");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("PrepaidMeter")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Prepaid Meter");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("NCMCRecharge")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "NCMC Recharge");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("MunicipalServices")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Municipal Services");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("EducationFees")){
                        intent = new Intent(context, ElectricityActivity.class);
                        intent.putExtra("title", "Education Fees");
                        context.startActivity(intent);
                    }else if (sliderItems.getRedirect().equals("Other")){
                        if (!sliderItems.getOther_redirect_url().isEmpty() || !sliderItems.getOther_redirect_url().isBlank()){
                            String url = sliderItems.getOther_redirect_url();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            context.startActivity(i);
                        }
                    }


                });
        }

    }
}
