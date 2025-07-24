package com.vuvrecharge.modules.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.vuvrecharge.R;
import com.vuvrecharge.base.BaseActivity;
import com.vuvrecharge.base.BaseMethod;
import com.vuvrecharge.databinding.EnqueryDialogBinding;
import com.vuvrecharge.modules.adapter.BillerAdapter;
import com.vuvrecharge.modules.model.BillFetch;
import com.vuvrecharge.modules.model.ReportsData;
import com.vuvrecharge.modules.presenter.DefaultPresenter;
import com.vuvrecharge.modules.view.DefaultView;
import com.vuvrecharge.utils.ScreenshotUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RechargeReportActivity extends BaseActivity implements DefaultView, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_sub)
    TextView title_sub;
    @BindView(R.id.addMore)
    TextView addMore;
    @BindView(R.id.operator)
    TextView operator;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.gift_number)
    TextView gift_number;
    @BindView(R.id.tvCopy)
    TextView tvCopy;
    @BindView(R.id.order_id)
    TextView order_id;
    @BindView(R.id.remark)
    TextView remark;
    @BindView(R.id.debit)
    TextView debit;
    @BindView(R.id.commission)
    TextView commission;
    @BindView(R.id.amount_wallet)
    TextView amount_wallet;
    @BindView(R.id.amount_wallet_2)
    TextView amount_wallet_2;
    @BindView(R.id.electricity_notes)
    TextView electricity_notes;
    @BindView(R.id.complain_details)
    TextView complain_details;
    @BindView(R.id.commission_bg)
    ConstraintLayout commission_bg;
    @BindView(R.id.copy)
    RelativeLayout copy;
    @BindView(R.id.share_view)
    LinearLayout share_view;
    @BindView(R.id.second_layout)
    LinearLayout second_layout;
    @BindView(R.id.load)
    ImageView load;
    @BindView(R.id.operator_img)
    CircleImageView operator_img_;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.no_internet)
    LinearLayout no_internet;
    @BindView(R.id.retry)
    TextView retry;
    @BindView(R.id.done)
    TextView done;
    @BindView(R.id.imgResponse)
    ImageView imgResponse;
    @BindView(R.id.rvCustomerInfo)
    RecyclerView rvCustomerInfo;
    @BindView(R.id.imgPDF)
    ImageView imgPDF;
    @BindView(R.id.imgAssured)
    ImageView imgAssured;
    @BindView(R.id.amountDebit)
    RelativeLayout amountDebit;
    String operator_img = "";
    String operator_dunmy_img = "";
    String recharge_type;
    DefaultPresenter mDefaultPresenter;
    Handler handler;
    Runnable runnable;
    ReportsData mReportsData;
    List<BillFetch> billFetchList = new ArrayList<>();
    BillerAdapter adapter;
    RequestOptions options;
    String screen = "No";
    String bps = "";
    String report = "0";
    int i = 0;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_recharge_report);
        ButterKnife.bind(this);
        mToolbar.setOnClickListener(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        addMore.setOnClickListener(this);
        done.setOnClickListener(this);
        imgPDF.setOnClickListener(this);
        number.setOnClickListener(this);
        Intent intent = getIntent();
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.no)
                .error(R.drawable.no);
        operator_img = intent.getStringExtra("operator_img");
        operator_dunmy_img = intent.getStringExtra("operator_dunmy_img");
        String mReportsData1 = intent.getStringExtra("mReportsData");
        bps = intent.getStringExtra("bps");
        if (intent.hasExtra("report")){
            report = intent.getStringExtra("report");
        }
        Gson gson = new Gson();
        Type type_ = new TypeToken<ReportsData>() {}.getType();
        mReportsData = gson.fromJson(mReportsData1, type_);
        mDefaultPresenter = new DefaultPresenter(this);
        mDefaultPresenter.rechargeDetails(device_id + "", mReportsData.getOrder_id() + "");
        handler = new Handler();
        runnable = () -> {
            if (mReportsData.getStatus().equals("PENDING")) {
                mDefaultPresenter.rechargeDetails(device_id + "", mReportsData.getOrder_id() + "");
            }
        };
        BaseMethod.refresh = "Refresh";
        try {
            Glide.with(getActivity()).asGif().load(R.drawable.load).into(load);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(v -> onBackPressed());
            Objects.requireNonNull(mToolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            setTitle("");
        }
    }

    private void setData(ReportsData mReportsData) {
        try {
            if (mReportsData.getStatus().equals("PENDING")) {
                handler.postDelayed(runnable, 10000);
            }
            if (mReportsData.getStatus() != null){
                if (mReportsData.getStatus().equals("PENDING")) {
                    changeStatusBarColorProcess();
                    load.setVisibility(VISIBLE);
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.pending1));
                    title.setText("Transaction Processing");
                    Glide.with(this)
                            .asGif()
                            .load(R.drawable.animated_pending)
                            .into(imgResponse);
                    second_layout.setBackgroundResource(R.drawable.pending_transaction_drawable);
                    amountDebit.setVisibility(VISIBLE);
                    commission_bg.setVisibility(GONE);
                } else if (mReportsData.getStatus().equals("SUCCESS")) {
                    changeStatusBarColorGreen1();
                    amountDebit.setVisibility(VISIBLE);
                    commission_bg.setVisibility(VISIBLE);
                    second_layout.setBackgroundResource(R.drawable.green_transaction_drawable);
                    Glide.with(this)
                            .asGif()
                            .load(R.drawable.animated_right)
                            .into(imgResponse);
                    if (mReportsData.getRecharge_type() != null){
                        recharge_type = mReportsData.getRecharge_type();
                        if (!recharge_type.equals("null") && recharge_type.equals("GiftCards")) {
                            gift_number.setText(mReportsData.getOperator_ref());
                            gift_number.setVisibility(VISIBLE);
                            number.setVisibility(GONE);
                            remark.setVisibility(GONE);
                            tvCopy.setVisibility(VISIBLE);
                        } else {
                            number.setVisibility(VISIBLE);
                            gift_number.setVisibility(GONE);
                            tvCopy.setVisibility(GONE);
                        }
                    }

                    remark.setVisibility(GONE);
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.success1));
                    load.setVisibility(GONE);
                    title.setText("Transaction Successful");
                } else {
                    amountDebit.setVisibility(GONE);
                    changeStatusBarColorRed();
                    second_layout.setBackgroundResource(R.drawable.failed_transaction_drawable);
                    Glide.with(this)
                            .asGif()
                            .load(R.drawable.animated_wrong)
                            .into(imgResponse);
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.failed1));
                    title.setText("Transaction Failed");
                    load.setVisibility(GONE);
                    commission_bg.setVisibility(GONE);
                }
            }

            if (i == 0){
                if (mReportsData.getStatus().toLowerCase().equals("pending")) {
                    i = 0;
                }else if (mReportsData.getStatus().toLowerCase().equals("success")) {
                    if (bps.equals("1")){
                        i = 1;
                        ringtone();
                    }
                }else {
                    i = 1;
                }
            }
            else {
                Log.d("TAG_DATA", "setData: "+i);
            }

            if (mReportsData.getLogo() == null) {
                setImageUser(operator_img + operator_dunmy_img, operator_img_);
            }
            else {
                if (mReportsData.getLogo().equals("")) {
                    setImageUser(operator_img + operator_dunmy_img, operator_img_);
                } else {
                    setImageUser(operator_img + mReportsData.getLogo(), operator_img_);
                }
            }

            electricity_notes.setVisibility(GONE);
            if (mReportsData.getDisplay_message() != null) {
                if (!mReportsData.getDisplay_message().equals("")) {
                    electricity_notes.setVisibility(VISIBLE);
                    electricity_notes.setText(mReportsData.getDisplay_message());
                }
            }
            tvCopy.setOnClickListener(v -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(mReportsData.getOperator_ref());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", mReportsData.getOperator_ref());
                    clipboard.setPrimaryClip(clip);
                }
                  Toast.makeText(getActivity(), "Gift Card id copied", Toast.LENGTH_SHORT).show();

            });
            copy.setOnClickListener(v -> copyTextMain(mReportsData.getOrder_id()));

            long date_t = Long.parseLong(mReportsData.getRequest_time().trim());
            date_t = date_t * 1000;
            Date date = new Date(date_t);

            String dateFormatNew = "N/A";
            dateFormatNew = BaseMethod.dateFormatNew.format(date);
            title_sub.setText(dateFormatNew);
            operator.setText(mReportsData.getOperator_name());
            number.setText(mReportsData.getNumber());
            order_id.setText("Order Id : " + mReportsData.getOrder_id());
            amount_wallet.setText("\u20b9" + mReportsData.getAmount());
            amount_wallet_2.setText("\u20b9" + mReportsData.getAmount());
            DecimalFormat decimalFormat = new DecimalFormat("0.000");
            double final_charge = Double.parseDouble(mReportsData.getFinal_charge());
           double points = Double.parseDouble(mReportsData.getPoints());
            double amount = Double.parseDouble(mReportsData.getAmount());
            double commission_details = amount - final_charge-points;
            commission.setText("\u20b9" + decimalFormat.format(commission_details));
            debit.setText("\u20b9" + mReportsData.getFinal_charge());

            if (mReportsData.getStatus().equals("FAILED")) {
                if (mReportsData.getOperator_ref().equals("")) {
                    remark.setText("Reason : N/A");
                } else {
                    remark.setText("Reason : " + mReportsData.getOperator_ref());
                }
                remark.setVisibility(VISIBLE);
            }
            else {
                if (mReportsData.getStatus().toUpperCase().equals("PENDING")){
                    remark.setVisibility(GONE);
                } else {
                    remark.setVisibility(VISIBLE);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageUser(@NonNull String image_url, ImageView imageView) {
        if (!image_url.equals("")) {
            try {
                Glide.with(this)
                        .load(image_url)
                        .apply(options)
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setLayout(no_internet, retry, "rechargeReport");
        setData(mReportsData);
    }

    @Override
    public void onBackPressed() {
        if (report.equals("0")){
            Intent intent = new Intent(this, MainActivity.class)
                    .addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    );
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            startActivity(intent);
        }
        else {
            super.onBackPressed();
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
        }
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.addMore:
                screen = "No";
                if (!selectFile()) {
                    setToolbarFalse(mToolbar);
                    takeScreenshot(share_view);
                    new Handler().postDelayed(() -> setToolbar(mToolbar), 1000);
                }
                break;
            case R.id.toolbar_layout:
                onBackPressed();
                getActivity().finish();
                break;
            case R.id.number:
                copyTextMain(mReportsData.getNumber());
                break;
            case R.id.done:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.imgPDF:
                if (!selectFile()){
                    setToolbarFalse(mToolbar);
                    pdf(share_view);
                    new Handler().postDelayed(() -> setToolbar(mToolbar), 1000);
                }
                break;
        }
    }

    private void pdf(View view) {
        Bitmap b = null;
        b = ScreenshotUtils.takeScreenshotForView(view);
        try {
            if(b!=null){
                File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if(!file.isDirectory()){
                    file.mkdir();
                }
                File path =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"IMG_"+System.currentTimeMillis()+".jpg");

                    FileOutputStream fileOutputStream=new FileOutputStream(path);
                    if(b.compress(Bitmap.CompressFormat.JPEG,100, fileOutputStream))
                    {
                        createPdf(path);
                    }
                    else{
                        Log.d("TAG_PDF", "pdf: "+new Exception().getMessage());
                    }
            }

        }catch (Exception e){
            Log.e("TAG_PDF", e + " ");
        }
    }

    private void createPdf(File path) {
        try {

            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"PDF_"+System.currentTimeMillis()+".pdf");
            Log.d("TAG_PDF", "pdf: working..."+dir);
            FileOutputStream fOut = new FileOutputStream(dir);
            Rectangle size = new Rectangle(PageSize.A3.getWidth(),PageSize.A3.getHeight());
            Document document = new Document();
            PdfWriter.getInstance(document, fOut);


            document.setPageSize(size);
            document.setPageCount(1);
            //open the document
            document.open();

            //Draw the bitmap onto the page
            Image img = Image.getInstance(path.toString());
            img.scaleToFit(600,  850);
            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 45.0f, Font.BOLD, BaseColor.BLACK);
            Chunk c = new Chunk("MAXPE");
            Paragraph paragraph = new Paragraph(c);
            document.add(paragraph);
            document.add(new Paragraph("\n"));
            document.add(img);
            document.close();
            path.delete();

            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", dir);
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(share);

        }catch (Exception e){
            Log.d("TAG_PDF", "pdf: error..."+e.getMessage());
        }
    }

    public void needHelp() {
        try {
            EnqueryDialogBinding binding_ = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                    R.layout.enquery_dialog, null, false);
            dialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
            dialog.setContentView(binding_.getRoot());
            dialog.setCancelable(false);
            changeStatusBarColor(dialog);

            binding_.query.setHint("Describe you problem");
            submit = binding_.submit;
            loading_dialog = binding_.loading;
            bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setSkipCollapsed(false);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(600);
            }
            binding_.dialogImage.setOnClickListener(v -> {
                if (submit.getVisibility() == VISIBLE) {
                    hideKeyBoard(binding_.query);
                    new Handler().postDelayed(() -> {
                        dialog.dismiss();
                        bottomSheet = null;
                        dialog = null;
                    }, 200);
                }
            });
            binding_.submit.setOnClickListener(v -> {

                String query = binding_.query.getText().toString();
                if (TextUtils.isEmpty(query.trim())) {
                    showError(bottomSheet, "Please enter query");
                    return;
                }
                binding_.submit.setVisibility(GONE);
                hideKeyBoard(binding_.query);
                mDefaultPresenter.doComplaint(device_id + "", query + "",
                        mReportsData.getOrder_id() + "");
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String data) {
        billFetchList.clear();
        try {
            JSONObject object = new JSONObject(data);
            mReportsData.setNumber(object.getString("number"));
            mReportsData.setAmount(object.getString("amount"));
            mReportsData.setFinal_charge(object.getString("final_charge"));
            mReportsData.setStatus(object.getString("status"));
            mReportsData.setOperator_name(object.getString("operator_name"));
            mReportsData.setOrder_id(object.getString("order_id"));
            mReportsData.setRequest_time(object.getString("request_time"));
            mReportsData.setLogo(object.getString("logo"));
            mReportsData.setDisplay_message(object.getString("display_message"));
            mReportsData.setComplaint_count(object.getString("complaint_count"));
            mReportsData.setIs_bbps(object.getString("is_bbps"));
            JSONObject object1 = new JSONObject(object.getString("bill_data"));
            mReportsData.setOperator_ref(object.getString("operator_ref"));

            if (object1 != null){
                Iterator<String> keys = object1.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = object1.getString(key);
                    if (!value.isEmpty()) {
                        billFetchList.add(new BillFetch(key, value));
                    }
                }
                adapter = new BillerAdapter(
                        billFetchList, "",
                        mReportsData.getRecharge_type(),
                        billFetchList.size());
                rvCustomerInfo.setAdapter(adapter);
                rvCustomerInfo.setVisibility(VISIBLE);
                adapter.notifyDataSetChanged();
            }

            if (mReportsData.getIs_bbps() != null){
                if (mReportsData.getIs_bbps().equals("true")) {
                    String text_ = "BConnect Txn id: ";
                    String text_new = text_ + mReportsData.getOperator_ref();
                    SpannableString ss = new SpannableString(text_new);
                    StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                    ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 0, 0));

                    ss.setSpan(fcs, (text_.length() - 1), (text_new.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(boldSpan, (text_.length() - 1), (text_new.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    remark.setText(ss);
                    imgAssured.setVisibility(VISIBLE);
                } else {
                    String text = "For any queries regarding recharge please contact ";
                    String text1 = text + mReportsData.getOperator_name();
                    String text_ = text1 + " with reference no ";
                    String text_new = text_ + mReportsData.getOperator_ref();
                    SpannableString ss = new SpannableString(text_new);
                    StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                    ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 0, 0));

                    ss.setSpan(fcs, (text.length() - 1), (text1.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(boldSpan, (text.length() - 1), (text1.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    ss.setSpan(fcs, (text_.length() - 1), (text_new.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(boldSpan, (text_.length() - 1), (text_new.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    remark.setText(ss);
                    imgAssured.setVisibility(GONE);
                }
            }

            setData(mReportsData);

        }catch (Exception e){
            Log.d("TAG_DATA", "onSuccess: "+e.getMessage());
        }
    }

    @Override
    public void onSuccess(String data, String data_other) {

    }

    @Override
    public void onSuccessOther(String data) {
        complain_details.setText(data);
        complain_details.setVisibility(VISIBLE);
//        help.setVisibility(View.GONE);
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onSuccessOther(String data, String data_other) {

    }

    @Override
    public void onError(String error) {
        if (bottomSheet != null) {
            showError(bottomSheet, error);
            submit.setVisibility(VISIBLE);
        } else {
            showError(error);
        }
    }

    @Override
    public void onShowDialog(String message) {
        if (bottomSheet != null) {
            showLoading(loading_dialog);
            submit.setVisibility(GONE);
        } else {
            showLoading(loading);
        }

    }

    @Override
    public void onHideDialog() {
        if (bottomSheet != null) {
            hideLoading(loading_dialog);
            submit.setVisibility(VISIBLE);
        } else {
            hideLoading(loading);
        }
    }

    @Override
    public void onShowToast(String message) {
        if (bottomSheet != null) {
            showToast(bottomSheet, message);
            submit.setVisibility(VISIBLE);
        } else {
            showToast(message);
        }
    }

    @Override
    public void onPrintLog(String message) {
        printLog(message);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideAllDialog();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public void ringtone() {
        try {
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.bharat_connect_mogo);
            Ringtone ringtone = RingtoneManager.getRingtone(this, sound);
            ringtone.play();
            new Handler(Objects.requireNonNull(Looper.myLooper())).postDelayed(ringtone::stop,3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

