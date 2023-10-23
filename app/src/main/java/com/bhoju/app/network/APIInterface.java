package com.bhoju.app.network;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface APIInterface {

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getAppConfigs(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> signUpUser(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> loginUser(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> socialLoginUser(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> logOutUser(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getNotificationCount(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> deleteAccount(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> forgotPassword(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> verifyOtp(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> changePasswordNew(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> changePassword(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @Multipart
    @POST("{fullUrl}")
    Call<String> updateUserProfile(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @PartMap() Map<String, RequestBody> partMap
            , @Part MultipartBody.Part picture);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> updateNotificationSetting(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getSubProfiles(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @Multipart
    @POST("{fullUrl}")
    Call<String> addSubProfile(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @PartMap() Map<String, RequestBody> partMap
            , @Part MultipartBody.Part picture);

    @Multipart
    @POST("{fullUrl}")
    Call<String> editSubProfile(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @PartMap() Map<String, RequestBody> partMap
            , @Part MultipartBody.Part picture);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> deleteSubProfile(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getCards(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> addCard(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> deleteCard(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> makeCardDefault(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @GET()
    Call<String> getStaticPage(@Url String url);

    @GET("{fullUrl}")
    Call<String> getSpamReasons(@Path(value = "fullUrl", encoded = true) String fullUrl);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> addToSpam(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    /*@FormUrlEncoded
    @POST(APIs.REMOVE_FROM_SPAM)
    Call<String> removeFromSpam(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.ADMIN_VIDEO_ID) int adminVideoId
            , @Field(Params.LANGUAGE) String language);*/


    /*@FormUrlEncoded
    @POST(APIs.GET_WISHLIST)
    Call<String> getWishListItems(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.SKIP) int skip
            , @Field(Params.LANGUAGE) String language);*/


    /*@FormUrlEncoded
    @POST(APIs.GET_VIDEOS_FOR_SEASON)
    Call<String> getVideosForSeason(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.PAGE_TYPE) String pageType
            , @Field(Params.CATEGORY_ID) int categoryId
            , @Field(Params.SUB_CATEGORY_ID) int subCategoryId
            , @Field(Params.GENRE_ID) int genreId
            , @Field(Params.SKIP) int skip
            , @Field(Params.LANGUAGE) String language);*/

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getVideosForSeason(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    /*@FormUrlEncoded
    @POST(APIs.SPAM_VIDEOS)
    Call<String> getSpamVideos(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.SKIP) int skip
            , @Field(Params.LANGUAGE) String language);*/

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getCastVideos(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    /*@FormUrlEncoded
    @POST(APIs.HISTORY_VIDEOS)
    Call<String> getHistoryVideos(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.SKIP) int skip
            , @Field(Params.LANGUAGE) String language);*/

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getAvailablePlans(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getMyPlans(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    /*@FormUrlEncoded
    @POST(APIs.PAID_VIDEOS)
    Call<String> getMyPaidVideos(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.SKIP) int skip
            , @Field(Params.LANGUAGE) String language);*/

    /*@FormUrlEncoded
    @POST(APIs.CATEGORIES)
    Call<String> getCategories(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.SKIP) int skip
            , @Field(Params.LANGUAGE) String language);*/

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getNotifications(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @GET()
    Call<String> getAllCookies(@Url String url);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getVideoData(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @HeaderMap Map<String, String> headers
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getVideoRelatedData(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @HeaderMap Map<String, String> headers
            , @FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getHomeSliderContentFor(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);
    //@GET()
    //Call<String> getHomeSliderContentFor(@Url String url);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getVideoContentFor(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getVideoContentDynamicFor(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    /*@FormUrlEncoded
    @POST(APIs.CONTINUE_WATCHING_STORE)
    Call<String> continueWatchingStorePos(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.ADMIN_VIDEO_ID) int adminVideoId
            , @Field(Params.DURATION) int elapsed
            , @Field(Params.LANGUAGE) String language);*/

    /*@FormUrlEncoded
    @POST(APIs.CONTINUE_WATCHING_END)
    Call<String> continueWatchingEnd(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.ADMIN_VIDEO_ID) int adminVideoId
            , @Field(Params.LANGUAGE) String language);*/

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> makePayPalPlanPayment(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> makeStripePayment(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> makeStripePPV(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> makePayPalPPV(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> makePaytmPayment(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> verifyUpiPaytmPayment(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> applyCouponCode(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> applyPPVCode(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> autoRenewalEnable(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> cancelSubscription(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> downloadedVideos(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> downloadStatusUpdate(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    /*@FormUrlEncoded
    @POST(APIs.ADD_TO_WISH_LIST)
    Call<String> toggleWishList(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.ADMIN_VIDEO_ID) int adminVideoId
            , @Field(Params.STATUS) int isInWishList, @Field(Params.LANGUAGE) String language);*/

    /*@FormUrlEncoded
    @POST(APIs.ADD_TO_HISTORY)
    Call<String> addToHistory(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.ADMIN_VIDEO_ID) int adminVideoId, @Field(Params.LANGUAGE) String language);*/

    /*@FormUrlEncoded
    @POST(APIs.LIKE_VIDEO)
    Call<String> likeVideo(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.SUB_PROFILE_ID) int subProfileId
            , @Field(Params.ADMIN_VIDEO_ID) int adminVideoId
            , @Field(Params.LANGUAGE) String language);*/

    /*@FormUrlEncoded
    @POST(APIs.UNLIKE_VIDEO)
    Call<String> unLikeVideo
            (@Field(Params.ID) int id
                    , @Field(Params.TOKEN) String token
                    , @Field(Params.SUB_PROFILE_ID) int subProfileId
                    , @Field(Params.ADMIN_VIDEO_ID) int adminVideoId
                    , @Field(Params.LANGUAGE) String language);*/

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> clearHistory(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> clearWishList(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> searchVideos(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> clearSpamList(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> moreVideosList(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getSuggestionVideos(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    /*@FormUrlEncoded
    @POST(APIs.PPV_END)
    Call<String> ppvEnd(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.ADMIN_VIDEO_ID) int adminVideoId
            , @Field(Params.LANGUAGE) String language);*/

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getReferralCode(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    /*@FormUrlEncoded
    @POST(APIs.REFERRAL_CODE_VALIDATE)
    Call<String> applyReferralCode(@Field(Params.REFERRAL_CODE) String referralCode);*/

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getInvoiceAmount(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    /*@FormUrlEncoded
    @POST(APIs.INVOICE_REFERRAL_AMOUNT)
    Call<String> getInvoiceAmount(@Field(Params.ID) int id
            , @Field(Params.TOKEN) String token
            , @Field(Params.AMOUNT) String amount);*/

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getPlayerJson(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getPaytmChecksum(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> updateAge(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);


    @GET()
    Call<String> getHelpQueries(@Url String url);

//    @FormUrlEncoded
//    @POST("{fullUrl}")
//    Call<String> sendQueryToBackend(@Path(value = "fullUrl", encoded = true) String fullUrl
//            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> sendQuery(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getSubscriptionStatus(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @GET()
    Call<String> getPaymentGatewayCountryWise(@Url String url);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> updateDownloadExpiry(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @GET()
    Call<String> getPublicIpAddress(@Url String url);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getUsersState(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @GET()
    Call<String> getAppVersionFromServer(@Url String url);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> sendFCMTokenToServer(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @GET()
    Call<String> getUserLoggedInStatus(@Url String url);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getUpcomingVideos(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getComeComingUpNotification(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getAppRating(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("{fullUrl}")
    Call<String> getAppRatingUpdate(@Path(value = "fullUrl", encoded = true) String fullUrl
            , @FieldMap Map<String, Object> params);
}
