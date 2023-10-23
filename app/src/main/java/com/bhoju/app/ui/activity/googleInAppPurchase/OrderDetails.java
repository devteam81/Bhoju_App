package com.bhoju.app.ui.activity.googleInAppPurchase;

/**
 * Created by Harshit on 27-Apr-18.
 */

public class OrderDetails {
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    private String orderId;
    private String currency;
    private Double amount;
    private String consumerId;

}
