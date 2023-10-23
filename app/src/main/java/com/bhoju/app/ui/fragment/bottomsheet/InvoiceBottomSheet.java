package com.bhoju.app.ui.fragment.bottomsheet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bhoju.app.R;
import com.bhoju.app.listener.BottomSheetBackDismissListener;
import com.bhoju.app.model.Invoice;
import com.bhoju.app.model.Video;
import com.bhoju.app.ui.activity.MyPlansActivity;
import com.bhoju.app.ui.activity.VideoPageActivity;
import com.bhoju.app.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.bhoju.app.util.DisplayUtils.makeBottomSheetFullScreen;

public class InvoiceBottomSheet extends BottomSheetDialogFragment {

    private static final String INVOICE = "invoice";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.statusText)
    TextView statusText;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.paymentId)
    TextView paymentId;
    @BindView(R.id.paidAmt)
    TextView paidAmt;
    @BindView(R.id.couponCode)
    TextView couponCode;
    @BindView(R.id.couponCodeLayout)
    LinearLayout couponCodeLayout;
    @BindView(R.id.couponAmt)
    TextView couponAmt;
    @BindView(R.id.couponAmtLayout)
    LinearLayout couponAmtLayout;
    @BindView(R.id.totalAmt)
    TextView totalAmt;
    @BindView(R.id.myPlans)
    Button myPlans;
    @BindView(R.id.dismiss)
    Button dismiss;
    @BindView(R.id.totalAmtLayout)
    LinearLayout totalAmtLayout;

    Invoice invoice;
    InvoiceInterface invoiceInterface;
    Unbinder unbinder;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    private View.OnClickListener goToMyPlansListener = v -> {
        dismiss();
        Intent toMyPlans = new Intent(getActivity(), MyPlansActivity.class);
        startActivity(toMyPlans);
        if (getActivity() != null) getActivity().finish();
    };

    public static InvoiceBottomSheet getInstance(Invoice invoice) {
        InvoiceBottomSheet invoiceBottomSheet = new InvoiceBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putSerializable(InvoiceBottomSheet.INVOICE, invoice);
        invoiceBottomSheet.setArguments(bundle);
        return invoiceBottomSheet;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.layout_payment_invoice, null);
        unbinder = ButterKnife.bind(this, contentView);
        dialog.setContentView(contentView);
        dialog.setOnKeyListener(new BottomSheetBackDismissListener());
        makeBottomSheetFullScreen(getActivity(), mBottomSheetBehaviorCallback, contentView);
        setCancelable(false);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        showInvoiceData();
    }


    private void showInvoiceData() {
        Bundle bundle = getArguments();
        if (bundle != null)
            invoice = (Invoice) bundle.getSerializable(INVOICE);

        if (invoice != null) {
            statusText.setText(invoice.getStatus());
            titleText.setText(invoice.getTitle());
            title.setText(invoice.getTitle());
            paymentId.setText(invoice.getPaymentId());
            paidAmt.setText(String.format("%s%s", invoice.getCurrency(), invoice.getPaidAmount()));
            if (invoice.isCouponApplied()) {
                couponAmtLayout.setVisibility(View.GONE);
                couponAmtLayout.setVisibility(View.GONE);
                couponAmt.setText(String.format("%s%s", invoice.getCurrency(), invoice.getCouponAmount()));
                couponCode.setText(invoice.getCouponCode());
            } else {
                couponAmtLayout.setVisibility(View.GONE);
                couponCodeLayout.setVisibility(View.GONE);
            }
            totalAmt.setText(String.format("%s%s", invoice.getCurrency(), invoice.getTotalAmount()));

            //Button setup
            dismiss.setOnClickListener(v -> dismiss());
            if (invoice.isPayingForPlan())
                myPlans.setOnClickListener(goToMyPlansListener);
            else {
                myPlans.setVisibility(View.GONE);
                dismiss.setText(getActivity().getString(R.string.watch_video));
                invoiceInterface = (VideoPageActivity) getActivity();
                dismiss();
                invoiceInterface.playVideo(invoice.getVideo());
            }
        } else {
            UiUtils.showShortToast(getActivity(), getString(R.string.wrong_invoice));
            dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface InvoiceInterface {
        void playVideo(Video video);
    }
}
