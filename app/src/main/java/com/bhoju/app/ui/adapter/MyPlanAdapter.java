package com.bhoju.app.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhoju.app.R;
import com.bhoju.app.listener.OnLoadMoreVideosListener;
import com.bhoju.app.model.SubscriptionPlan;
import com.bhoju.app.ui.activity.MyPlansActivity;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyPlanAdapter extends RecyclerView.Adapter<MyPlanAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SubscriptionPlan> myPlans;
    private LayoutInflater inflater;
    private DisplayAutoRenewal displayAutoRenewal;
    private OnLoadMoreVideosListener listener;


    public MyPlanAdapter(MyPlansActivity myPlansActivity, OnLoadMoreVideosListener listener, ArrayList<SubscriptionPlan> myPlansList, DisplayAutoRenewal displayAutoRenewal) {
        this.context = myPlansActivity;
        this.listener = listener;
        this.myPlans = myPlansList;
        this.displayAutoRenewal = displayAutoRenewal;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_plan, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SubscriptionPlan plans = myPlans.get(i);

        viewHolder.title.setText(plans.getTitle());
        viewHolder.perMonth.setText(String.format(Locale.ENGLISH,"/%d", plans.getMonths()));

        viewHolder.viewDetails.setOnClickListener(view -> viewFullSubscription(plans, i));
        viewHolder.noOfAcc.setText(String.format("%s%s",
                context.getString(R.string.no_of_accounts), plans.getNoOfAccounts()));
        viewHolder.amount.setText(plans.getAmountWithCurrency());
        viewHolder.date.setText(plans.getExpires());
        viewHolder.backAmt.setText(plans.getAmountWithCurrency());

        if (plans.getActivePlan() && plans.getOriginalAmount() == 0) {
            displayAutoRenewal.dataChanged(plans.getOriginalAmount());
        }
    }

    @Override
    public int getItemCount() {
        return myPlans.size();
    }

    //My Plans full details
    private void viewFullSubscription(SubscriptionPlan plans, int i) {
        Dialog dialog = new Dialog(context, R.style.AppTheme_NoActionBar);
        dialog.setContentView(R.layout.layout_payment_select);
        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        TextView planName = dialog.findViewById(R.id.planName);
        TextView amount = dialog.findViewById(R.id.amount);
        TextView desc = dialog.findViewById(R.id.desc);
        TextView select = dialog.findViewById(R.id.select);
        TextView noOfAcc = dialog.findViewById(R.id.noOfAcc);
        TextView expiry = dialog.findViewById(R.id.expiry);
        TextView totalAmt = dialog.findViewById(R.id.totalAmt);
        TextView paymentId = dialog.findViewById(R.id.paymentId);
        TextView paymentMode = dialog.findViewById(R.id.paymentMode);
        TextView couponAmt = dialog.findViewById(R.id.couponAmt);
        TextView perMonth = dialog.findViewById(R.id.perMonth);
        TextView couponCodeText = dialog.findViewById(R.id.couponCodeText);
        TextView couponAmtText = dialog.findViewById(R.id.couponAmtText);
        TextView couponCode = dialog.findViewById(R.id.couponCode);
        TextView referralDiscountAmt = dialog.findViewById(R.id.referralDiscountAmount);
        toolbar.setNavigationOnClickListener(v -> dialog.cancel());
        desc.setText(Html.fromHtml(plans.getDescription()));
        select.setVisibility(View.GONE);
        planName.setText(plans.getTitle());
        amount.setText(plans.getAmountWithCurrency());
        perMonth.setText(String.format(Locale.ENGLISH,"/%d", plans.getMonths()));
        expiry.setText(plans.getExpires());
        paymentMode.setText(plans.getPaymentMode());
        noOfAcc.setText(String.format("%s %s",context.getString(R.string.no_of_accounts), plans.getNoOfAccounts()));
        totalAmt.setText(String.format("%s%s", plans.getCurrency(), plans.getTotalAmt()));
        couponAmt.setText(String.format("%s%s", plans.getCurrency(), plans.getCouponAmt()));
        referralDiscountAmt.setText(String.format("%s%s", plans.getCurrency(), plans.getReferralDiscountAmt()));
        paymentId.setText(plans.getPaymentId());
        couponCode.setText(plans.getCouponCode());
        if (couponCode.getText().toString().equalsIgnoreCase("")) {
            couponAmt.setVisibility(View.GONE);
            couponCode.setVisibility(View.GONE);
            couponAmtText.setVisibility(View.GONE);
            couponCodeText.setVisibility(View.GONE);
        }
        dialog.show();
    }

    public void showLoading() {
        if (listener != null)
            listener.onLoadMore(myPlans.size());
    }

    public interface DisplayAutoRenewal {
        void dataChanged(double orginalAmt);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.noOfAcc)
        TextView noOfAcc;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.perMonth)
        TextView perMonth;
        @BindView(R.id.viewDetails)
        TextView viewDetails;
        @BindView(R.id.backAmt)
        TextView backAmt;
        @BindView(R.id.date)
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
