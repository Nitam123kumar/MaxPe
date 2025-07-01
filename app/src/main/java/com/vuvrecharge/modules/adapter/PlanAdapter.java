package com.vuvrecharge.modules.adapter;

import static com.vuvrecharge.api.ApiServices.SUBSCRIPTION_IMAGE_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.databinding.PlanLayoutBinding;
import com.vuvrecharge.modules.activities.newActivities.PlanDetailsActivity;
import com.vuvrecharge.modules.model.PlanItemData;
import com.vuvrecharge.modules.model.Subscriptions;
import com.vuvrecharge.modules.view.DefaultView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.Holder> {

    private LayoutInflater mLayoutInflater;
    DefaultView mDefaultView;
    public List<PlanItemData> dataList = new ArrayList<>();
    String title = "";
    String phone = "";
    String url = "";
    String provider = "";
    String state = "";
    String selected_operator = "";
    String selected_circle = "";

    public PlanAdapter(LayoutInflater mLayoutInflater, String title, String phone, String url,
                       String provider, String state, String selected_operator,
                       String selected_circle) {
        this.mLayoutInflater = mLayoutInflater;
        this.title = title;
        this.phone = phone;
        this.url = url;
        this.provider = provider;
        this.state = state;
        this.selected_operator = selected_operator;
        this.selected_circle = selected_circle;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.plan_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(dataList.get(position), title);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addData(List<PlanItemData> dataList, DefaultView mDefaultView) {
        this.dataList = dataList;
        this.mDefaultView = mDefaultView;
        notifyDataSetChanged();
    }

    public void searchList(List<PlanItemData> dataList,DefaultView mDefaultView){
        this.dataList = dataList;
        this.mDefaultView = mDefaultView;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        Context mContext;
        @BindView(R.id.validity)
        TextView validity;
        @BindView(R.id.validityValue)
        TextView validityValue;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.txtMore)
        TextView txtMore;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.dataValue)
        TextView dataValue;
        @BindView(R.id.data)
        TextView data;
//        @BindView(R.id.btnSelect)
//        TextView btnSelect;
        @BindView(R.id.txtSubscription)
        TextView txtSubscription;
        @BindView(R.id.imgSubscription)
        ImageView imgSubscription;
        @BindView(R.id.txtMoreSubscription)
        TextView txtMoreSubscription;
        @BindView(R.id.viewDescription)
        View viewDescription;
        @BindView(R.id.cardImage)
        CardView cardImage;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void bind(@NonNull PlanItemData resultsBean, @NonNull String title) {
            validity.setText(resultsBean.getValidity());
            amount.setText("\u20b9 " + resultsBean.getRs().trim());
            if (title.equals("Plans Details")){
                if (resultsBean.getData().equals("N/A")){
                    dataValue.setVisibility(View.GONE);
                    data.setVisibility(View.GONE);
                }else {
                    dataValue.setVisibility(View.VISIBLE);
                    data.setVisibility(View.VISIBLE);
                    dataValue.setText(resultsBean.getData());
                }
                String [] temp = resultsBean.getDesc().split("\\|");
                if (temp[0].trim().equals("N/A")){
                        if (resultsBean.getSms_value().trim().equals("N/A") && resultsBean.getTalktime().trim().equals("N/A")){
                            description.setText(temp[1].trim());
                            maxLines(resultsBean);
                            return;
                        }else if (resultsBean.getTalktime().trim().equals("N/A")){
                            description.setText(temp[1].trim()+"\nSMS: "+resultsBean.getSms_value());
                            maxLines(resultsBean);
                            return;
                        }else if (resultsBean.getSms_value().trim().equals("N/A")){
                            description.setText(temp[1].trim()+"\ntalktime: "+resultsBean.getTalktime());
                            maxLines(resultsBean);
                            return;
                        } else {
                            description.setText(temp[1].trim()+"\nSMS: "+resultsBean.getSms_value()+"\ntalktime: "+resultsBean.getTalktime());
                            maxLines(resultsBean);
                            return;
                        }
                    } else {
                        if (resultsBean.getSms_value().trim().equals("N/A") && resultsBean.getTalktime().trim().equals("N/A")){
                            description.setText(resultsBean.getDesc().replaceAll("&amp;", "&").trim());
                            maxLines(resultsBean);
                            return;
                        }else if (resultsBean.getTalktime().trim().equals("N/A")){
                            description.setText(resultsBean.getDesc().replaceAll("&amp;", "&").trim()+"\nSMS: "+resultsBean.getSms_value());
                            maxLines(resultsBean);
                            return;
                        }else if (resultsBean.getSms_value().trim().equals("N/A")){
                            description.setText(resultsBean.getDesc().replaceAll("&amp;", "&").trim()+"\ntalktime: "+resultsBean.getTalktime());
                            maxLines(resultsBean);
                            return;
                        } else {
                            description.setText(resultsBean.getDesc().replaceAll("&amp;", "&").trim()+" SMS: "+resultsBean.getSms_value()+"\ntalktime: "+resultsBean.getTalktime());
                            maxLines(resultsBean);
                            return;
                        }
                    }

            }else if (title.equals("R Offer")){
                dataValue.setText("NA");
                validity.setText("Not Available");
                validity.setVisibility(View.GONE);
                validityValue.setVisibility(View.INVISIBLE);
                description.setText(resultsBean.getDesc());
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, PlanDetailsActivity.class);
                    intent.putExtra("amount",resultsBean.getRs());
                    intent.putExtra("number",phone);
                    intent.putExtra("url",url);
                    intent.putExtra("provider",provider);
                    intent.putExtra("state",state);
                    intent.putExtra("des",description.getText().toString());
                    intent.putExtra("subscription",resultsBean.getSubscription());
                    intent.putExtra("validity",resultsBean.getValidity());
                    intent.putExtra("data",dataValue.getText().toString());
                    intent.putExtra("operator",selected_operator);
                    intent.putExtra("circle",selected_circle);
                    intent.putExtra("pageType","Prepaid");
                    mContext.startActivity(intent);
                });
            }else {
                Log.d("TAG_PlAN", "bind: "+title);
            }
        }

        private void maxLines(@NonNull PlanItemData resultsBean){
            description.post(()->{
                Paint paint = new Paint();
                float textPixelLenght = paint.measureText(description.getText().toString());
                int textviewWidth = description.getMeasuredWidth();
                float numberOfLines = textPixelLenght / textviewWidth;
                if (numberOfLines > 0.95){
//                    Log.d("TAG_DATA", "bind: "+numberOfLines);
                    description.setMaxLines(3);
                    description.setEllipsize(TextUtils.TruncateAt.END);
                    txtMore.setText("View More");
                }else {
                    txtMore.setText("");
                }
            });
            txtMore.setOnClickListener(v -> openPlan(
                    resultsBean.getRs(),
                    url,
                    description.getText().toString(),
                    resultsBean.getValidity(),
                    dataValue.getText().toString(),resultsBean,"view_more")
            );

            txtMoreSubscription.setOnClickListener(v -> openPlan(
                    resultsBean.getRs(),
                    url,
                    description.getText().toString(),
                    resultsBean.getValidity(),
                    dataValue.getText().toString(),resultsBean,"more"));

            try{
                JSONArray array = new JSONArray(resultsBean.getSubscription());
                if (array.length() > 0){
                    JSONObject object = array.getJSONObject(0);
                    Glide.with(mContext)
                            .load(SUBSCRIPTION_IMAGE_URL+object.getString("logo"))
                            .into(imgSubscription);
                    txtSubscription.setText(object.getString("ott_name"));
                    txtSubscription.setMaxLines(1);
                    txtSubscription.setEllipsize(TextUtils.TruncateAt.END);
                    txtMoreSubscription.setVisibility(View.VISIBLE);
                    txtSubscription.setVisibility(View.VISIBLE);
                    cardImage.setVisibility(View.VISIBLE);
                    viewDescription.setVisibility(View.VISIBLE);
                }else {
                    txtSubscription.setMaxLines(1);
                    txtSubscription.setEllipsize(TextUtils.TruncateAt.END);
                    txtMoreSubscription.setVisibility(View.GONE);
                    txtSubscription.setVisibility(View.GONE);
                    cardImage.setVisibility(View.GONE);
                    viewDescription.setVisibility(View.GONE);
                }
            }catch (JSONException e){
                Log.d("TAG_DATA", "bind: "+e.getMessage());
            }

            amount.setOnClickListener(v -> {
                BaseMethod.amount = resultsBean.getRs();
                BaseMethod.from = "No";
                mDefaultView.onSuccess("");
            });

//            btnSelect.setOnClickListener(v -> {
//                Intent intent = new Intent(mContext, PlanDetailsActivity.class);
//                intent.putExtra("amount",resultsBean.getRs());
//                intent.putExtra("number",phone);
//                intent.putExtra("url",url);
//                intent.putExtra("provider",provider);
//                intent.putExtra("state",state);
//                intent.putExtra("des",description.getText().toString());
//                intent.putExtra("subscription",resultsBean.getSubscription());
//                intent.putExtra("validity",resultsBean.getValidity());
//                intent.putExtra("data",dataValue.getText().toString());
//                intent.putExtra("operator",selected_operator);
//                intent.putExtra("circle",selected_circle);
//                intent.putExtra("pageType","Prepaid");
//                mContext.startActivity(intent);
//            });

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, PlanDetailsActivity.class);
                intent.putExtra("amount",resultsBean.getRs());
                intent.putExtra("number",phone);
                intent.putExtra("url",url);
                intent.putExtra("provider",provider);
                intent.putExtra("state",state);
                intent.putExtra("des",description.getText().toString());
                intent.putExtra("subscription",resultsBean.getSubscription());
                intent.putExtra("validity",resultsBean.getValidity());
                intent.putExtra("data",dataValue.getText().toString());
                intent.putExtra("operator",selected_operator);
                intent.putExtra("circle",selected_circle);
                intent.putExtra("pageType","Prepaid");
                mContext.startActivity(intent);
            });
        }

        private void openPlan(String rs, String url, String desc, String validity, String data, PlanItemData resultsBean, String more) {
            try {
                FrameLayout bottomSheet = null;
                BottomSheetDialog dialog = null;

                PlanLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                        R.layout.plan_layout, null, false);
                dialog = new BottomSheetDialog(mContext, R.style.AppBottomSheetDialogTheme);
                dialog.setContentView(binding.getRoot());
                dialog.setCancelable(true);
                bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setSkipCollapsed(false);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    behavior.setPeekHeight(600);
                }

                binding.txtAmount.setText(rs);

                Glide.with(mContext)
                        .load(url)
                        .into(binding.imgProvider);

                if (more.equals("more")){
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.txt.setVisibility(View.VISIBLE);
                }else {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.txt.setVisibility(View.GONE);
                }

                try {
                    ArrayList<Subscriptions> list = new ArrayList<>();
                    JSONArray array = new JSONArray(resultsBean.getSubscription());
                    for(int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        Subscriptions subscriptions = new Subscriptions();
                        subscriptions.setOtt_name(object.getString("ott_name"));
                        subscriptions.setOtt_desc(object.getString("ott_desc"));
                        subscriptions.setLogo(object.getString("logo"));
                        list.add(subscriptions);
                    }
                    SubscriptionAdapter adapter = new SubscriptionAdapter(list, mContext);
                    binding.recyclerView.setAdapter(adapter);
                }catch (JSONException e){
                    Log.d("TAG_DATA", "openPlan: "+e.getMessage());
                }
                binding.txtDes.setText(desc);

                binding.txtDataValue.setText(data);
                binding.txtValidityValue.setText(validity);

                BottomSheetDialog finalDialog = dialog;

                binding.btnSubmit.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, PlanDetailsActivity.class);
                    intent.putExtra("amount",rs);
                    intent.putExtra("number",phone);
                    intent.putExtra("url",url);
                    intent.putExtra("provider",provider);
                    intent.putExtra("state",state);
                    intent.putExtra("des",desc);
                    intent.putExtra("subscription",resultsBean.getSubscription());
                    intent.putExtra("validity",validity);
                    intent.putExtra("data",dataValue.getText().toString());
                    intent.putExtra("operator",selected_operator);
                    intent.putExtra("circle",selected_circle);
                    intent.putExtra("pageType","Prepaid");
                    mContext.startActivity(intent);
                    finalDialog.cancel();
                });

                dialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
