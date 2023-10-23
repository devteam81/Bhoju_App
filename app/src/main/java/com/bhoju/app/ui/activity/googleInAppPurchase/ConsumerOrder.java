package com.bhoju.app.ui.activity.googleInAppPurchase;


import com.bhoju.app.model.SubscriptionPlan;

/**
 * Created by Harshit on 27-Apr-18.
 */

public class ConsumerOrder {
    private String id;	//NOT Generated from 3rd party PaymentGateway. (i.e is a Kooku OrderId)
    private String consumerId;
    private PaymentReference paymentReference;

    private OrderDetails orderDetails;
    //private OrderStatus orderStatus;
    private SubscriptionPlan subscriptionPlan;

    private String paymentDateTime;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public PaymentReference getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(PaymentReference paymentReference) {
        this.paymentReference = paymentReference;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public String getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(String paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }




}
