package com.vuvrecharge.modules.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.vuvrecharge.R;
import com.vuvrecharge.modules.model.LanguageModel;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private List<LanguageModel> languageList;
    private Context context;
    private int selectedPosition = -1;

    public interface OnLanguageSelected {
        void onSelected(LanguageModel model);
    }

    OnLanguageSelected listener;

    public LanguageAdapter(Context context, List<LanguageModel> list, OnLanguageSelected listener) {
        this.context = context;
        this.languageList = list;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView langName, langSub;
        RadioButton radio;
        ConstraintLayout main;

        public ViewHolder(View itemView) {
            super(itemView);
            langName = itemView.findViewById(R.id.language_title);
            langSub = itemView.findViewById(R.id.change_language);
            radio = itemView.findViewById(R.id.radioButton);
            main = itemView.findViewById(R.id.language_layout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.language_selector_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LanguageModel model = languageList.get(position);
        holder.langName.setText(model.getLanguageName());
        holder.langSub.setText(model.getLanguageCode());
        holder.radio.setChecked(model.isSelected());

        if (model.isSelected()) {
            holder.main.setBackgroundResource(R.drawable.bg);
        }
        else {
            holder.main.setBackgroundResource(R.drawable.support_design_background_shape);
        }
        if (model.getLanguageCode().equals("te")){
            holder.langSub.setText("తెలుగు");
        }
//        else if (model.getLanguageCode().equals("hi")){
//            holder.langSub.setText("हिंदी");
//        }
        else if (model.getLanguageCode().equals("en")){
            holder.langSub.setText("English");
        }


        View.OnClickListener selectLanguage = v -> {
            for (LanguageModel m : languageList) {
                m.setSelected(false);
            }
            model.setSelected(true);
            notifyDataSetChanged();

            if (listener != null) {
                listener.onSelected(model);
            }



        };

        holder.radio.setOnClickListener(selectLanguage);
        holder.itemView.setOnClickListener(selectLanguage);

    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }
}

