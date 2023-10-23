package com.bhoju.app.model;

import java.io.Serializable;

/**
 * Created by codegama on 14/10/17.
 */

public class SubscriptionPlan implements Serializable {

    private int id;
    private int months;
    private int noOfAccounts;
    private double totalAmt;
    private double originalAmount;
    private double amount;
    private double listedPrice;
    private String title;
    private String description;
    private String currency;
    private String status;
    private String accounts;
    private String expires;
    private String couponRemainingAmt;
    private String couponAmt;
    private String paymentStatus;
    private String couponCode;
    private String paymentMode;
    private String paymentId;
    private String coupon_status;
    private String referralDiscountAmt;
    private boolean isCancelled;
    private boolean isActivePlan;
    private boolean isPopular;
    private boolean isSubscribed;
    private String monthFormatted;
    private int discount;
    private int days;
    private double percentage;

    private String chkCountry;
    private String code;
    private String symbol;

    private String googlePlayProductId;

    public String getMonthFormatted() {
        return monthFormatted;
    }

    public void setMonthFormatted(String monthFormatted) {
        this.monthFormatted = monthFormatted;
    }

    public SubscriptionPlan() {

    }

    public String getReferralDiscountAmt() {
        return referralDiscountAmt;
    }

    public void setReferralDiscountAmt(String referralDiscountAmt) {
        this.referralDiscountAmt = referralDiscountAmt;
    }

    public SubscriptionPlan(String title) {
        this.title = title;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public boolean getPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        this.isPopular = popular;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCouponRemainingAmt() {
        return couponRemainingAmt;
    }

    public void setCouponRemainingAmt(String couponRemainingAmt) {
        this.couponRemainingAmt = couponRemainingAmt;
    }

    public String getCoupon_status() {
        return coupon_status;
    }

    public void setCoupon_status(String coupon_status) {
        this.coupon_status = coupon_status;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public boolean getActivePlan() {
        return isActivePlan;
    }

    public void setActivePlan(boolean activePlan) {
        this.isActivePlan = activePlan;
    }

    public String getCouponAmt() {
        return couponAmt;
    }

    public void setCouponAmt(String couponAmt) {
        this.couponAmt = couponAmt;
    }

    public double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public String getAmountWithCurrency() {
        return getCurrency() + amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        setOriginalAmount(amount);
    }

    public double getListedPrice() {
        return listedPrice;
    }

    public void setListedPrice(double listedPrice) {
        this.listedPrice = listedPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getNoOfAccounts() {
        return noOfAccounts;
    }

    public void setNoOfAccounts(int noOfAccounts) {
        this.noOfAccounts = noOfAccounts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccounts() {
        return accounts + " Accounts";
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.isSubscribed = subscribed;
    }

    public String getChkCountry() {
        return chkCountry;
    }

    public void setChkCountry(String chkCountry) {
        this.chkCountry = chkCountry;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getGooglePlayProductId() {
        return googlePlayProductId;
    }

    public void setGooglePlayProductId(String googlePlayProductId) {
        this.googlePlayProductId = googlePlayProductId;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "SubscriptionPlan{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", months=" + months +
                ", amount=" + amount +
                ", originalAmount=" + originalAmount +
                ", currency='" + currency + '\'' +
                ", status='" + status + '\'' +
                ", accounts='" + accounts + '\'' +
                ", expires='" + expires + '\'' +
                ", isCancelled='" + isCancelled + '\'' +
                ", couponRemainingAmt='" + couponRemainingAmt + '\'' +
                ", isActivePlan='" + isActivePlan + '\'' +
                ", couponAmt='" + couponAmt + '\'' +
                ", totalAmt='" + totalAmt + '\'' +
                ", noOfAccounts='" + noOfAccounts + '\'' +
                ", isPopular='" + isPopular + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", couponCode='" + couponCode + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", coupon_status='" + coupon_status + '\'' +
                ", isSubscribed=" + isSubscribed +
                '}';
    }
}
