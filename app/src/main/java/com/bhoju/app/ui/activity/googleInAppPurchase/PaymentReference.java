package com.bhoju.app.ui.activity.googleInAppPurchase;

public class PaymentReference {
    private String paymentGatewayRefID;
    private String paymentGatewayName;
    private String paymentDateTime;

    public String getPaymentGatewayRefID() {
        return paymentGatewayRefID;
    }

    public void setPaymentGatewayRefID(String paymentGatewayRefID) {
        this.paymentGatewayRefID = paymentGatewayRefID;
    }

    public String getPaymentGatewayName() {
        return paymentGatewayName;
    }

    public void setPaymentGatewayName(String paymentGatewayName) {
        this.paymentGatewayName = paymentGatewayName;
    }

    public String getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(String paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }
}
