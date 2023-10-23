package com.bhoju.app.ui.activity.googleInAppPurchase;


import java.util.ArrayList;

/**
 * Created by Harshit on 10-Mar-18.
 */

public class SubscriptionPackage {


    private String id;


    private String title;

    private String titlePlatformSlug;


    private String description;


    private Double basePrice;


    private Double listedPrice;


    private Integer durationDays;


    private String durationDaysText;


    private String createdBy;

    private String createdAt;

    private String itunesProductId;

    private String googlePlayProductId;

    private String subscriptionPlatform;

    private Category category;

    private ArrayList<String> paymentGatewaysAllowed;

    private String age;


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGooglePlayProductId() {
        return googlePlayProductId;
    }

    public void setGooglePlayProductId(String googlePlayProductId) {
        this.googlePlayProductId = googlePlayProductId;
    }

    public ArrayList<String> getPaymentGatewaysAllowed() {
        return paymentGatewaysAllowed;
    }

    public void setPaymentGatewaysAllowed(ArrayList<String> paymentGatewaysAllowed) {
        this.paymentGatewaysAllowed = paymentGatewaysAllowed;
    }

    public String getSubscriptionPlatform() {
        return subscriptionPlatform;
    }

    public void setSubscriptionPlatform(String subscriptionPlatform) {
        this.subscriptionPlatform = subscriptionPlatform;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePlatformSlug() {
        return titlePlatformSlug;
    }

    public void setTitlePlatformSlug(String titlePlatformSlug) {
        this.titlePlatformSlug = titlePlatformSlug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getListedPrice() {
        return listedPrice;
    }

    public void setListedPrice(Double listedPrice) {
        this.listedPrice = listedPrice;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getDurationDaysText() {
        return durationDaysText;
    }

    public void setDurationDaysText(String durationDaysText) {
        this.durationDaysText = durationDaysText;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getItunesProductId() {
        return itunesProductId;
    }

    public void setItunesProductId(String itunesProductId) {
        this.itunesProductId = itunesProductId;
    }


}