package com.bhoju.app.network;

import com.bhoju.app.BuildConfig;

public class APIConstants {

    static {
        System.loadLibrary("keys");
    }
    //url
    public static native String getLiveUrl();
    public static native String getTestUrl();
    public static native String getCookiesUrl();
    public static native String getKey();
    public static native String getLiveCertificate();
    public static native String getTestCertificate();
    public static native String getMediaCertificate();

    //Constants
    public static native String getManualLogin();
    public static native String getGoogleLogin();
    public static native String getFacebookLogin();
    public static native String getAndroid();
    public static native String getSuccess();
    public static native String getError();
    public static native String getTrue();
    public static native String getFalse();
    public static native String getCountryCode();
    public static native String getCurrencyCode();

    public static native String getPageType();
    public static native String getCancelledOrCompleted();
    public static native String getDownloadStatus();
    public static native String getDownloadProgress();
    public static native String getDownloadUpdate();
    public static native String getVideoElapsed();


    //Apis
    public static native String getUserApi();
    public static native String getLoginUrl();
    public static native String getRegisterUrl();
    public static native String getForgotPasswordUrl();
    public static native String getChangePasswordUrl();
    public static native String getResetPasswordUrl();
    public static native String getDeleteAccountUrl();
    public static native String getVerifyOtpUrl();
    public static native String getLogoutUrl();
    public static native String getProfileUrl();
    public static native String getUpdateProfileUrl();
    public static native String getNotificationSettingUpdateUrl();
    public static native String getAddCardUrl();
    public static native String getPaymentListUrl();
    public static native String getDeleteCardUrl();
    public static native String getDefaultCardUrl();
    public static native String getAppConfigUrl();
    public static native String getStaticPagesUrl();
    public static native String getWishlistUrl();
    public static native String getSpamListUrl();
    public static native String getHistoryListUrl();
    public static native String getSubscriptionPlansUrl();
    public static native String getSubscribedPlansUrl();
    public static native String getPaidVideosUrl();
    public static native String getCategoriesUrl();
    public static native String getNotificationsUrl();
    public static native String getSubProfilesUrl();
    public static native String getAddSubProfileUrl();
    public static native String getEditSubProfileUrl();
    public static native String getSubProfileDeleteUrl();
    public static native String getCardDetailsUrl();
    public static native String getHomeSliderUrl();
    public static native String getHomeFirstSectionUrl();
    public static native String getAllCookiesUrl();
    public static native String getSingleVideoDataUrl();
    public static native String getPayPalPaymentUrl();
    public static native String getAddToWishListUrl();
    public static native String getClearWishListUrl();
    public static native String getAddToHistoryUrl();
    public static native String getClearHistoryUrl();
    public static native String getLikeVideoUrl();
    public static native String getUnlikeVideoUrl();
    public static native String getSearchVideoUrl();
    public static native String getAddSpamUrl();
    public static native String getRemoveSpamUrl();
    public static native String getSpamReasonUrl();
    public static native String getStripePaymentUrl();
    public static native String getCouponCodeUrl();
    public static native String getPVVCodeUrl();
    public static native String getStripePVVUrl();
    public static native String getPaypalPVVUrl();
    public static native String getPaytmPaymentUrl();
    public static native String getVerifyPaytmPaymentUrl();
    public static native String getAgreePayPaymentUrl();
    public static native String getGooglePlayPaymentUrl();
    public static native String getCancelSubscriptionUrl();
    public static native String getAutorenewalUrl();
    public static native String getClearSpamListUrl();
    public static native String getCastVideosUrl();
    public static native String getSingleVideoDataSecondUrl();
    public static native String getSeasonEpisodeUrl();
    public static native String getSeeAllUrl();
    public static native String getPPVEndUrl();
    public static native String getHomeSecondUrl();
    public static native String getDownloadedVideosUrl();
    public static native String getDownloadStatusUpdateUrl();
    public static native String getNotificationCountUrl();
    public static native String getPageListUrl();
    public static native String getSuggestionUrl();
    public static native String getContinueWatchingVideoUrl();
    public static native String getContinueWatchingVideoEndUrl();
    public static native String getReferralCodeUrl();
    public static native String getReferralCodeValidateUrl();
    public static native String getReferralCodeAmountUrl();
    public static native String getUpdateAgeUrl();
    public static native String getSupportDataUrl();
    public static native String getAddSupportUrl();
    public static native String getAddSupportequryUrl();
    public static native String getPaytmChecksumUrl();
    public static native String getUserSubscriptionUrl();
    public static native String getPaymentGatewayUrl();
    public static native String getDownloadUrl();
    public static native String getUpdateDownloadExpiryUrl();
    public static native String getPublicIpUrl();
    public static native String getPublicIp64Url();
    public static native String getUserStateUrl();
    public static native String getServerAppVersionUrl();
    public static native String getFCMTokenUrl();
    public static native String sendFCMTokenRefUrl();
    public static native String getUserLoggedInOtherDeviceUrl();

    public static native String sendShareUrlDetails();

    public static native String getUpComingVideo();
    public static native String getComingSoonNotification();
    public static native String getAppRatingUrl();
    public static native String getAppRatingUpdateUrl();

    //Params
    public static native String getIdField();
    public static native String getTokenField();
    public static native String getLoginTokenField();
    public static native String getLoginField();
    public static native String getNameField();
    public static native String getEmailField();
    public static native String getContactField();
    public static native String getPasswordField();
    public static native String getLoginByField();
    public static native String getFCMTokenField();
    public static native String getDeviceTokenField();
    public static native String getDeviceTypeField();
    public static native String getMessageField();
    public static native String getErrorMessageField();
    public static native String getDefaultEmailField();
    public static native String getDefaultMobileField();
    public static native String getUserIdField();
    public static native String getUserNameField();
    public static native String getResponseField();
    public static native String getSuccessField();
    public static native String getFailedField();
    public static native String getPictureField();
    public static native String getDescriptionField();
    public static native String getSubjectField();
    public static native String getHelpTypesField();
    public static native String getAppDetailsField();
    public static native String getAppVersionField();
    public static native String getDeviceField();
    public static native String getPlatformField();
    public static native String getPlatformOsField();
    public static native String getExtField();
    public static native String getBrandField();
    public static native String getModelField();
    public static native String getVersionField();
    public static native String getPlatField();
    public static native String getManufacturerField();
    public static native String getBoardField();
    public static native String getBootLoaderField();
    public static native String getCpuAbiField();
    public static native String getCpuAbi2Field();
    public static native String getDisplayField();
    public static native String getFingerprintField();
    public static native String getHardwareField();
    public static native String getHostField();
    public static native String getDeviceIdField();
    public static native String getSerialField();
    public static native String getProductField();
    public static native String getOsVersionField();
    public static native String getVersionCodeField();
    public static native String getScreenSizeField();
    public static native String getScreenDpiField();
    public static native String getRamField();
    public static native String getDeviceLanguageField();
    public static native String getOpenGlVersionField();
    public static native String getenquiryField();
    public static native String getapp_versionField();
    public static native String getUserEmailField();
    public static native String getUserPhoneField();
    public static native String getUserNameEnquiryField();
    public static native String getAppProviderField();
    public static native String getVersionCodeApkField();

    public static native String getOldPasswordField();
    public static native String getPasswordConfirmationField();
    public static native String getSocialIdField();
    public static native String getNotificationTypeField();
    public static native String getNotificationSoundField();
    public static native String getNotificationVersionCodeField();
    public static native String getNotificationVersionNameField();
    public static native String getStatusField();
    public static native String getPushStatusField();
    public static native String getEmailStatusField();
    public static native String getCardTokenField();
    public static native String getCardIdField();
    public static native String getPaymentModeField();
    public static native String getCardField();
    public static native String getIsDefaultField();
    public static native String getImageField();
    public static native String getPageTypeField();
    public static native String getDataField();
    public static native String getHistoryField();
    public static native String getErrorCodeField();
    public static native String getLastFourField();
    public static native String getCardNameField();
    public static native String getMobileField();
    public static native String getMobileNumberField();
    public static native String getOTPField();
    public static native String getSubProfileIdField();
    public static native String getSkipField();
    public static native String getWishlistField();
    public static native String getVideoIdField();
    public static native String getDownloadIdField();
    public static native String getDownloadUrlsField();
    public static native String getDownloadPathField();
    public static native String getDownloadFileNameField();
    public static native String getTitleField();
    public static native String getEpisodeTitleField();
    public static native String getDefaultImageField();
    public static native String getBrowseImageField();
    public static native String getSubscriptionIdField();
    public static native String getPlanField();
    public static native String getPlanFormatField();
    public static native String getAmountField();
    public static native String getCreatedAtField();
    public static native String getCurrencyField();
    public static native String getCheckCountryField();
    public static native String getCodeField();
    public static native String getSymbolField();
    public static native String getProductIdField();
    public static native String getTotalAccountField();
    public static native String getPopularStatusField();
    public static native String getPaymentIdField();
    public static native String getCouponAmountField();
    public static native String getCouponCodeField();
    public static native String getCouponField();
    public static native String getActivePlanField();
    public static native String getCancelledStatusField();
    public static native String getPaymenStatusField();
    public static native String getExpiryDateField();
    public static native String getPaymenModeField();
    public static native String getTotalAmountField();
    public static native String getPPVIdField();
    public static native String getPaidDateField();
    public static native String getSubscriptionTypeField();
    public static native String getTypeUserField();
    public static native String getUserTypeField();
    public static native String getCategoriesField();
    public static native String getTimeField();
    public static native String getTimezoneField();
    public static native String getImgField();
    public static native String getSubProfileUserIdField();
    public static native String getPositionField();
    public static native String getCardNumberField();
    public static native String getCardMonthField();
    public static native String getCardYearField();
    public static native String getCardVVField();
    public static native String getCategoryIdField();
    public static native String getSubCategoryIdField();
    public static native String getGenreIdField();
    public static native String getSeasonIdField();
    public static native String getSeasonNameField();
    public static native String getParentIdField();
    public static native String getVideosIdField();
    public static native String getBannerField();
    public static native String getOriginalsField();
    public static native String getMobileImageField();
    public static native String getSeeAllField();
    public static native String getErrorMsgField();
    public static native String getAdminIdField();
    public static native String getPublishTimeField();
    public static native String getSubscriptionCountField();
    public static native String getAgeField();
    public static native String getUrlField();
    public static native String getRegionNameField();
    public static native String getVideoUrlField();
    public static native String getVideoLanguageField();
    public static native String getAudioLanguageField();
    public static native String getVideoSubtitleField();
    public static native String getVideoSubtitleLangField();
    public static native String getVideoDurationField();
    public static native String getVideoMainField();
    public static native String getVideoTrailerField();
    public static native String getVideoTrailerLanguageField();
    public static native String getAudioTrailerLanguageField();
    public static native String getVideoTrailerSubtitleField();
    public static native String getVideoTrailerSubtitleLangField();
    public static native String getVideoRatingField();
    public static native String getVideoARecordField();
    public static native String getSliderTypeField();
    public static native String getParentMediaField();
    public static native String getVideoWatchCountField();
    public static native String getVideoIsSingleField();
    public static native String getVideoIsMainField();
    public static native String getVideoIsTrailerFreeField();
    public static native String getTrailerVideoField();
    public static native String getTrailerDefaultImageField();
    public static native String getTrailerBrowseImageField();
    public static native String getTrailerMobileImageField();
    public static native String getVideoIsPPVField();
    public static native String getVideoTypeField();
    public static native String getRatingTimeField();
    public static native String getRatingTypeField();
    public static native String getRatingUrlField();
    public static native String getRatingTrailerTimeField();
    public static native String getRatingTrailerTypeField();
    public static native String getRatingTrailerUrlField();
    public static native String getVideoIsKidsField();
    public static native String getDownloadStatusField();
    public static native String getDetailsField();
    public static native String getWishlistStatusField();
    public static native String getHistoryStatusField();
    public static native String getIsLikedField();
    public static native String getLikesField();
    public static native String getCastCrewField();
    public static native String getCastCrewsIdField();
    public static native String getDirectorField();
    public static native String getGenerField();
    public static native String getSeasonField();
    public static native String getGenerMainField();
    public static native String getSearchField();
    public static native String getLikeCountField();
    public static native String getDislikeCountField();
    public static native String getShareLinkField();
    public static native String getHistoryidField();
    public static native String getWishListIdField();
    public static native String getValueField();
    public static native String getReasonField();
    public static native String getAmountRemainingField();
    public static native String getCategoryField();
    public static native String getSubCategoryField();
    public static native String getGenersField();
    public static native String getVideoGeneresField();
    public static native String getIsSelectedField();
    public static native String getIsSeriresField();
    public static native String getTrailerSectionField();
    public static native String getDownloadResolutionField();
    public static native String getResolutionField();
    public static native String getMainVideoResolutionField();
    public static native String getOriginalField();
    public static native String getUrlTypeField();
    public static native String getUrlPageIdField();
    public static native String getGenreNameField();
    public static native String getDownloadButtonStatusField();
    public static native String getDownloadingViewStatusField();
    public static native String getDownloadedViewStatusField();
    public static native String getDownloadUrlField();
    public static native String getTypeField();
    public static native String getLinkField();
    public static native String getPPVAmountField();
    public static native String getPPVPageTypeField();
    public static native String getPPVPageTypeContentField();
    public static native String getSeekTimeField();
    public static native String getDeleteSubProfileField();
    public static native String getClearStatusField();
    public static native String getCountField();
    public static native String getIsSpamField();
    public static native String getBannerImageField();
    public static native String getPPVDisplayField();
    public static native String getPrivacyField();
    public static native String getRefundField();
    public static native String getHelpField();
    public static native String getTermsField();
    public static native String getAboutField();
    public static native String getPageTypeIdField();
    public static native String getUrlTypeIdField();
    public static native String getReferralEarningsField();
    public static native String getReferralCodeField();
    public static native String getShareMsgField();
    public static native String getAmountTotalField();
    public static native String getRemainingField();
    public static native String getReferralAmountField();
    public static native String getPayAmountField();
    public static native String getReferralAmountFormatField();
    public static native String getPayAmountFormatField();
    public static native String getWalletAmountField();
    public static native String getListedPriceField();
    public static native String getSubscriptionAmountField();
    public static native String getWalletAppliedField();
    public static native String getLanguageField();
    public static native String getNextVideoIdField();
    public static native String getVideoField();
    public static native String getVideoResolutionField();
    public static native String getMobileBannerImgField();
    public static native String getPlayerJsonField();
    public static native String getTotalDaysField();
    public static native String getDownloadedExpiryField();
    public static native String getTransitionImageField();
    public static native String getTransitionTitleField();
    public static native String getDiscountField();
    public static native String getDaysField();
    public static native String getPercentageField();
    public static native String getNotify();
    public static native String getIsNotify();
    public static native String getAdminVideoId();
    public static native String getAppRating();
    public static native String getAppReview();

    //Paytm
    public static native String getPaytmRespField();
    public static native String getOrderIdField();
    public static native String getCustomerIdField();
    public static native String getIndustryIdField();
    public static native String getWebsiteField();
    public static native String getChannelIdField();
    public static native String getTransactionAmountField();
    public static native String getCallbackUrlField();
    public static native String getPaytmCallbackUrlField();
    public static native String getPaytmStatusField();
    public static native String getTransactionIdField();
    public static native String getChecksumField();
    public static native String getTransactionToken();
    public static native String getBankNameField();
    public static native String getOrderIDField();
    public static native String getTransactionAmtField();
    public static native String getTransactionDateField();
    public static native String getMerchantIdField();
    public static native String getPaytmTransactionIdField();
    public static native String getResponseCodeField();
    public static native String getPaytmPaymentModeField();
    public static native String getBankTransactionIdField();
    public static native String getGatewayNameField();
    public static native String getResponseMessageField();
    public static native String getTestCallbackUrlField();
    public static native String getLiveCallbackUrlField();
    public static native String getParamOrderIdField();
    public static native String getParamCustomerIdField();
    public static native String getParamIndustryIdField();
    public static native String getParamChannelIdField();
    public static native String getParamTransactionAmtField();
    public static native String getParamCallbackUrlField();
    public static native String getPaymentGatewayName();
    public static native String getPaymentGatewayType();

    //Agreepay
    public static native String getAgreePayAPIKeyField();
    public static native String getAgreePayHostNameField();
    public static native String getAgreePayReturnUrlField();
    public static native String getAgreePayModeField();
    public static native String getAgreePayCurrencyField();
    public static native String getAgreePayCountryField();
    public static native String getAgreePayResponseMessageField();
    public static native String getAgreePayErrorMessageField();
    public static native String getAgreePayOrderIdField();
    public static native String getAgreePayTransactionIdField();
    public static native String getAgreePayPaymentMethodField();
    public static native String getAgreePayPaymentModeField();
    public static native String getAgreePayAmountField();

    //Google Play Billing
    public static native String getGooglePlayOrderIdField();
    public static native String getGooglePlayPackageNameField();
    public static native String getGooglePlayProductIdField();
    public static native String getGooglePlayPurchaseTimeField();
    public static native String getGooglePlayPurchaseStateField();
    public static native String getGooglePlayPurchaseTokenField();
    public static native String getGooglePlayQuantityField();
    public static native String getGooglePlayAutoRenewingField();
    public static native String getGooglePlayAcknowledgedField();

    public static native String getKeyPairField();
    public static native String getPolicyField();
    public static native String getSignatureField();

    public static native String getIPField();
    public static native String getDistributorField();
    public static native String getReferrerField();
    public static native String getInstallField();

    //Share URL_SEND_SHARE_URL_DETAILS
    public static native String getShareUrlCodeField();
    public static native String getShareUrlTypeField();
    public static native String getShareUrlUserIdField();
    //TYPE
    public static native String getShareTypeSubscribeField();
    public static native String getShareTypeLoginField();
    public static native String getShareTypeInstallField();

    private APIConstants() {

    }

    public static class Payments {

        public static class PayPal {
            public static final String CLIENT_ID = "AaXkweZD5g9s0X3BsO0Y4Q-kNzbmLZaog0mbmVGrTT5IX0O73LoLVcHp17e6pkG7Vm04JEUuG6up30LD";
        }
    }

    public static class Constants {
        public static final String DISTRIBUTOR = BuildConfig.DISTRIBUTOR;

        public static final String MANUAL_LOGIN = getManualLogin();
        public static final String GOOGLE_LOGIN = getGoogleLogin();
        public static final String FACEBOOK_LOGIN = getFacebookLogin();
        public static final String ANDROID = getAndroid();
        public static final String SUCCESS = getSuccess();
        public static final String ERROR = getError();
        public static final String TRUE = getTrue();
        public static final String FALSE = getFalse();
        public static final String COUNTRY_CODE = getCountryCode();
        public static final String CURRENCY_CODE = getCurrencyCode();


        public static final String PAGE_TYPE = getPageType();
        public static final String CANCELLED_OR_COMPLETED = getCancelledOrCompleted();
        public static final String DOWNLOAD_STATUS = getDownloadStatus();
        public static final String DOWNLOADED_PROGRESS = getDownloadProgress();
        public static final String DOWNLOAD_UPDATE = getDownloadUpdate();
        public static final String VIDEO_ELAPSED = getVideoElapsed();
    }


    public static class URLs {

        static final String LIVE_URL =  getLiveUrl();
        static final String TEST_URL = getTestUrl();
        public static final String COOKIES_URL = getCookiesUrl();
        public static final String BASE_URL = LIVE_URL;
        public static final String KEY = getKey();
        public static final String LIVE_CERTIFICATE = getLiveCertificate();
        public static final String TEST_CERTIFICATE = getTestCertificate();
        public static final String MEDIA_CERTIFICATE = getMediaCertificate();

        private URLs() {

        }
    }

    public static class ErrorCodes {
        public static final int NEED_TO_SUBSCRIBE_TO_MAKE_PAYMENT = 901;
        public static final int NO_DEFAULT_CARD_FOUND = 10000;
        static final int TOKEN_EXPIRED = 103;
        static final int SUB_PROFILE_DOESNT_EXIST = 3002;
        static final int USER_DOESNT_EXIST = 133;
        static final int USER_RECORD_DELETED_CONTACT_ADMIN = 3000;
        static final int INVALID_TOKEN = 104;
        static final int USER_LOGIN_DECLINED = 905;
        static final int EMAIL_NOT_ACTIVATED = 111;

        private ErrorCodes() {
        }

    }

    public static class APIs {

        static final String API_STR = getUserApi();
        public static final String LOGIN = API_STR + getLoginUrl();
        public static final String REGISTER = API_STR + getRegisterUrl();
        public static final String SOCIAL_LOGIN = REGISTER;
        public static final String FORGOT_PASSWORD = API_STR + getForgotPasswordUrl();
        public static final String CHANGE_PASSWORD = API_STR + getChangePasswordUrl();
        public static final String CHANGE_PASSWORD_NEW = API_STR + getResetPasswordUrl();
        public static final String DELETE_ACCOUNT = API_STR + getDeleteAccountUrl();
        public static final String VERIFY_OTP = API_STR + getVerifyOtpUrl();
        public static final String LOGOUT = API_STR + getLogoutUrl();
        public static final String USER_PROFILE = API_STR + getProfileUrl();
        public static final String UPDATE_USER_PROFILE = API_STR + getUpdateProfileUrl();
        public static final String NOTIFICATION_SETTING_UPDATE = API_STR + getNotificationSettingUpdateUrl();
        public static final String ADD_CARD = API_STR + getAddCardUrl();
        public static final String GET_PAYMENT_METHODS_LIST = API_STR + getPaymentListUrl();
        public static final String DELETE_CARD = API_STR + getDeleteCardUrl();
        public static final String MAKE_DEFAULT_CARD = API_STR + getDefaultCardUrl();
        public static final String GET_APP_CONFIG = getAppConfigUrl();
        public static final String GET_STATIC_PAGE = API_STR + getStaticPagesUrl();
        public static final String GET_WISHLIST = API_STR + getWishlistUrl();
        public static final String SPAM_VIDEOS = API_STR + getSpamListUrl();
        public static final String HISTORY_VIDEOS = API_STR + getHistoryListUrl();
        public static final String AVAILABLE_PLANS = API_STR + getSubscriptionPlansUrl();
        public static final String MY_PLANS = API_STR + getSubscribedPlansUrl();
        public static final String PAID_VIDEOS = API_STR + getPaidVideosUrl();
        public static final String CATEGORIES = API_STR + getCategoriesUrl();
        public static final String NOTIFICATIONS = API_STR + getNotificationsUrl();
        public static final String GET_SUB_PROFILES = API_STR + getSubProfilesUrl();
        public static final String ADD_SUB_PROFILE = API_STR + getAddSubProfileUrl();
        public static final String EDIT_SUB_PROFILE = API_STR + getEditSubProfileUrl();
        public static final String DELETE_SUB_PROFILE = API_STR + getSubProfileDeleteUrl();
        public static final String GET_CARDS = API_STR + getCardDetailsUrl();
        public static final String HOME_SLIDER = API_STR + getHomeSliderUrl();
        public static final String GET_VIDEO_CONTENT_FOR = API_STR + getHomeFirstSectionUrl();
        public static final String GET_ALL_COOKIES = API_STR + getAllCookiesUrl();
        public static final String GET_SINGLE_VIDEO_DATA = API_STR + getSingleVideoDataUrl();
        public static final String MAKE_PAY_PAL_PAYMENT = API_STR + getPayPalPaymentUrl();
        public static final String ADD_TO_WISH_LIST = API_STR + getAddToWishListUrl();
        public static final String CLEAR_WISH_LIST = API_STR + getClearWishListUrl();
        public static final String ADD_TO_HISTORY = API_STR + getAddToHistoryUrl();
        public static final String CLEAR_HISTORY = API_STR + getClearHistoryUrl();
        public static final String LIKE_VIDEO = API_STR + getLikeVideoUrl();
        public static final String UNLIKE_VIDEO = API_STR + getUnlikeVideoUrl();
        public static final String SEARCH_VIDEOS = API_STR + getSearchVideoUrl();
        public static final String ADD_TO_SPAM = API_STR + getAddSpamUrl();
        public static final String REMOVE_FROM_SPAM = API_STR + getRemoveSpamUrl();
        public static final String GET_SPAM_REASONS = API_STR + getSpamReasonUrl();
        public static final String MAKE_STRIPE_PAYMNET = API_STR + getStripePaymentUrl();
        public static final String APPLY_COUPON_CODE = API_STR + getCouponCodeUrl();
        public static final String APPLY_PPV_CODE = API_STR + getPVVCodeUrl();
        public static final String MAKE_STRIPE_PPV = API_STR + getStripePVVUrl();
        public static final String MAKE_PAYPAL_PPV = API_STR + getPaypalPVVUrl();
        public static final String MAKE_PAYTM_PAYMENT = API_STR + getPaytmPaymentUrl();
        public static final String VERIFY_UPI_PAYTM_PAYMENT = API_STR + getVerifyPaytmPaymentUrl();
        public static final String MAKE_AGREEPAY_PAYMENT = API_STR + getAgreePayPaymentUrl();
        public static final String MAKE_GOOGLE_PLAY_PAYMENT = API_STR + getGooglePlayPaymentUrl();
        public static final String CANCEL_SUBSCRIPTION = API_STR + getCancelSubscriptionUrl();
        public static final String AUTO_RENEWAL_ENABLE = API_STR + getAutorenewalUrl();
        public static final String CLEAR_SPAM_LIST = API_STR + getClearSpamListUrl();
        public static final String CAST_VIDEOS = API_STR + getCastVideosUrl();
        public static final String GET_SINGLE_VIDEO_RELATED_DATA = API_STR + getSingleVideoDataSecondUrl();
        public static final String  GET_VIDEOS_FOR_SEASON_NEW = API_STR + getSeasonEpisodeUrl();
        public static final String SEE_ALL_URL = API_STR + getSeeAllUrl();
        public static final String PPV_END = API_STR + getPPVEndUrl();
        public static final String GET_VIDEO_CONTENT_DYNAMIC_FOR = API_STR + getHomeSecondUrl();
        public static final String DOWNLOADED_VIDEOS = API_STR + getDownloadedVideosUrl();
        public static final String DOWNLOADED_STATUS_UPDATE = API_STR + getDownloadStatusUpdateUrl();
        public static final String NOTIFICATION_COUNT = API_STR + getNotificationCountUrl();
        public static final String PAGES = API_STR + getPageListUrl();
        public static final String SUGGESTION_VIDEOS = API_STR + getSuggestionUrl();
        static final String CONTINUE_WATCHING_STORE = API_STR + getContinueWatchingVideoUrl();
        static final String CONTINUE_WATCHING_END = API_STR + getContinueWatchingVideoEndUrl();
        public static final String REFERRAL_CODE = API_STR + getReferralCodeUrl();
        public static final String REFERRAL_CODE_VALIDATE = API_STR +  getReferralCodeValidateUrl();
        public static final String INVOICE_REFERRAL_AMOUNT = API_STR + getReferralCodeAmountUrl();

        public static final String UPDATE_AGE = API_STR + getUpdateAgeUrl();

        public static final String GETHELPQUERIES = API_STR + getSupportDataUrl();
        public static final String SUPPORTHELP = API_STR + getAddSupportUrl();
        public static final String SUPPORTENQURIY = API_STR + getAddSupportequryUrl();
        //Paytm
        public static final String PAYTM_CHECKSUM = API_STR + getPaytmChecksumUrl();

        public static final String USER_SUBSCRIPTION = API_STR + getUserSubscriptionUrl();
        public static final String GET_PAYMENT_GATEWAY = API_STR + getPaymentGatewayUrl();
        public static final String DOWNLOAD_URLS = API_STR + getDownloadUrl();
        public static final String UPDATE_DOWNLOAD_EXPIRY = API_STR + getUpdateDownloadExpiryUrl();

        public static final String GET_PUBLIC_IP = getPublicIpUrl();
        public static final String GET_PUBLIC_IP_64 = getPublicIp64Url();
        public static final String GET_USER_STATE = API_STR + getUserStateUrl();
        public static final String GET_SERVER_APP_VERSION = API_STR + getServerAppVersionUrl();
        public static final String GET_FCM_TOKEN = API_STR + getFCMTokenUrl();
        public static final String SEND_FCM_TOKEN_REF = API_STR + sendFCMTokenRefUrl();

        public static final String GET_USER_LOGGED_STATUS = API_STR + getUserLoggedInOtherDeviceUrl();
        public static final String SEND_SHARE_URL_DETAILS = API_STR + sendShareUrlDetails();

        public static final String GETUP_COMING_VIDEO = API_STR + getUpComingVideo();
        public static final String GETUP_COMING_NOTIFICATION = API_STR + getComingSoonNotification();
        public static final String GET_APP_RATING = API_STR + getAppRatingUrl();
        public static final String GET_APP_RATING_UPDATE = API_STR + getAppRatingUpdateUrl();

        private APIs() {
        }
    }

    public static class DownloadStatus {
        public static final int DOWNLOAD_INITIATE_STATUS = 1;
        public static final int DOWNLOAD_PROGRESS_STATUS = 2;
        public static final int DOWNLOAD_PAUSE_STATUS = 3;
        public static final int DOWNLOAD_COMPLETE_STATUS = 4;
        public static final int DOWNLOAD_CANCEL_STATUS = 5;
        public static final int DOWNLOAD_DELETED_STATUS = 6;
    }

    public static class Params {
        //Extra
        public static final String ID = getIdField();
        public static final String TOKEN = getTokenField();
        public static final String LOGIN_TOKEN = getLoginTokenField();
        public static final String LOGIN = getLoginField();
        public static final String NAME = getNameField();
        public static final String EMAIL = getEmailField();
        public static final String CONTACT = getContactField();
        public static final String PASSWORD = getPasswordField();
        public static final String LOGIN_BY = getLoginByField();
        public static final String FCM_TOKEN = getFCMTokenField();
        public static final String DEVICE_TOKEN = getDeviceTokenField();
        public static final String DEVICE_TYPE = getDeviceTypeField();
        public static final String MESSAGE = getMessageField();
        public static final String ERROR_MESSAGE = getErrorMessageField();
        public static final String DEFAULT_EMAIL = getDefaultEmailField();
        public static final String DEFAULT_MOBILE = getDefaultMobileField();
        public static final String USER_ID = getUserIdField();
        public static final String USER_NAME = getUserNameField();
        public static final String RESPONSE = getResponseField();
        public static final String SUCCESS = getSuccessField();
        public static final String FAILED = getFailedField();
        public static final String PICTURE = getPictureField();
        public static final String DESCRIPTION = getDescriptionField();
        public static final String SUBJECT = getSubjectField();
        public static final String HELPTYPE = getHelpTypesField();
        public static final String PHONEDETAILS = getAppDetailsField();
        public static final String APPVERSION = getAppVersionField();
        public static final String DEVICE = getDeviceField();
        public static final String PLATFORM = getPlatformField();
        public static final String PLATFORMOSVERSION = getPlatformOsField();
        public static final String BEBUEXT = getExtField();
        public static final String BRAND = getBrandField();
        public static final String MODEL = getModelField();
        public static final String VERSION = getVersionField();
        public static final String PLAT = getPlatField();
        public static final String MANUFACTURER = getManufacturerField();
        public static final String BOARD = getBoardField();
        public static final String BOOTLOADER = getBootLoaderField();
        public static final String CPU_ABI = getCpuAbiField();
        public static final String CPU_ABI2 = getCpuAbi2Field();
        public static final String DISPLAY = getDisplayField();
        public static final String FINGERPRINT = getFingerprintField();
        public static final String HARDWARE = getHardwareField();
        public static final String HOST = getHostField();
        public static final String DEVICE_ID = getDeviceIdField();
        public static final String SERIAL = getSerialField();
        public static final String PRODUCT = getProductField();
        public static final String OS_VERSION = getOsVersionField();
        public static final String VERSION_CODE = getVersionCodeField();
        public static final String SCREEN_SIZE = getScreenSizeField();
        public static final String SCREEN_DPI = getScreenDpiField();
        public static final String RAM = getRamField();
        public static final String DEVICE_LANGUAGE = getDeviceLanguageField();
        public static final String OPEN_GL_VERSION = getOpenGlVersionField();
        public static final String ENQUIRY = getenquiryField();
        public static final String APP_VERSION = getapp_versionField();
        public static final String USEREMAIL = getUserEmailField();
        public static final String USERPHONE = getUserPhoneField();
        public static final String USERNAME = getUserNameEnquiryField();
        public static final String AUTHPROVIDER = getAppProviderField();
        public static final String VERSION_CODE_APK = getVersionCodeApkField();

        public static final String OLD_PASSWORD = getOldPasswordField();
        public static final String CONFIRM_PASSWORD = getPasswordConfirmationField();
        public static final String SOCIAL_UNIQUE_ID = getSocialIdField();
        public static final String NOTIFICATION_TYPE = getNotificationTypeField();
        public static final String NOTIFICATION_SOUND = getNotificationSoundField();
        public static final String NOTIFICATION_VERSION_CODE = getNotificationVersionCodeField();
        public static final String NOTIFICATION_VERSION_NAME = getNotificationVersionNameField();
        public static final String STATUS = getStatusField();
        public static final String NOTIF_PUSH_STATUS = getPushStatusField();
        public static final String NOTIF_EMAIL_STATUS = getEmailStatusField();
        public static final String CARD_TOKEN = getCardTokenField();
        public static final String USER_CARD_ID = getCardIdField();
        public static final String PAYMENT_MODES = getPaymentModeField();
        public static final String CARDS = getCardField();
        public static final String IS_DEFAULT = getIsDefaultField();
        public static final String PAYMENT_MODE_IMAGE = getImageField();
        public static final String PAGE_TYPE = getPageTypeField();
        public static final String DATA = getDataField();
        public static final String HISTORY = getHistoryField();
        public static final String ERROR_CODE = getErrorCodeField();
        public static final String CARD_LAST_FOUR = getLastFourField();
        public static final String CARD_NAME = getCardNameField();
        public static final String MOBILE = getMobileField();
        public static final String MOBILENO = getMobileNumberField();
        public static final String OTP =  getOTPField();
        public static final String SUB_PROFILE_ID = getSubProfileIdField();
        public static final String SKIP = getSkipField();
        public static final String WISHLIST = getWishlistField();
        public static final String ADMIN_VIDEO_ID = getVideoIdField();
        public static final String DOWNLOAD_ID = getDownloadIdField();
        public static final String DOWNLOAD_URL = getDownloadUrlsField();
        public static final String DOWNLOAD_SAVE_PATH = getDownloadPathField();
        public static final String DOWNLOAD_FILE_NAME = getDownloadFileNameField();
        public static final String TITLE = getTitleField();
        public static final String EP_TITLE =  getEpisodeTitleField();
        public static final String DEFAULT_IMAGE = getDefaultImageField();
        public static final String BROWSE_IMAGE = getBrowseImageField();
        public static final String SUBSCRIPTION_ID = getSubscriptionIdField();
        public static final String PLAN = getPlanField();
        public static final String PLAN_FORMATTED = getPlanFormatField();
        public static final String AMOUNT = getAmountField();
        public static final String CREATED_AT = getCreatedAtField();
        public static final String CURRENCY = getCurrencyField();
        public static final String CHK_COUNTRY = getCheckCountryField();
        public static final String CODE = getCodeField();
        public static final String SYMBOL = getSymbolField();
        public static final String PRODUCT_ID = getProductIdField();
        public static final String NO_OF_ACCOUNTS = getTotalAccountField();
        public static final String POPULAR_STATUS = getPopularStatusField();
        public static final String PAYMENT_ID = getPaymentIdField();
        public static final String COUPON_AMT = getCouponAmountField();
        public static final String COUPON_CODE = getCouponCodeField();
        public static final String COUPON = getCouponField();
        public static final String ACTIVE_PLAN = getActivePlanField();
        public static final String CANCEL_STATUS = getCancelledStatusField();
        public static final String PAYMENT_STATUS = getPaymenStatusField();
        public static final String EXPIRIES_ON = getExpiryDateField();
        public static final String PAYMENT_MODE = getPaymenModeField();
        public static final String TOTAL_AMT = getTotalAmountField();
        public static final String PAY_PER_VIEW_ID = getPPVIdField();
        public static final String PAID_DATE = getPaidDateField();
        public static final String TYPE_OF_SUBSCRIPTION = getSubscriptionTypeField();
        public static final String TYPE_OF_USER = getTypeUserField();
        public static final String USER_TYPE = getUserTypeField();
        public static final String CATEGORIES = getCategoriesField();
        public static final String TIME = getTimeField();
        public static final String TIME_ZONE = getTimezoneField();
        public static final String IMG = getImgField();
        public static final String SUB_PROFILE_USER_ID = getSubProfileUserIdField();
        public static final String POSITION = getPositionField();
        public static final String CARD_NUMBER = getCardNumberField();
        public static final String CARD_MONTH = getCardMonthField();
        public static final String CARD_YEAR = getCardYearField();
        public static final String CARD_CVV = getCardVVField();
        public static final String CATEGORY_ID = getCategoryIdField();
        public static final String SUB_CATEGORY_ID = getSubCategoryIdField();
        public static final String GENRE_ID = getGenreIdField();
        public static final String SEASON_ID = getSeasonIdField();
        public static final String SEASON_NAME = getSeasonNameField();
        public static final String PARENT_ID = getParentIdField();
        public static final String VIDEO_ID = getVideosIdField();
        public static final String BANNER = getBannerField();
        public static final String ORIGINALS = getOriginalsField();
        public static final String MOBILE_IMAGE = getMobileImageField();
        public static final String SEE_ALL_URL = getSeeAllField();
        public static final String ERROR_MSG = getErrorMsgField();
        public static final String ADMIN_UNIQUE_ID = getAdminIdField();
        public static final String PUBLISH_TIME = getPublishTimeField();
        public static final String SUBSCRIPTION_COUNT = getSubscriptionCountField();
        public static final String AGE = getAgeField();
        public static final String URL = getUrlField();
        public static final String REGION_NAME = getRegionNameField();
        public static final String VIDEO_URL = getVideoUrlField();
        public static final String VIDEO_URL_LANGUAGE = getVideoLanguageField();
        public static final String AUDIO_URL_LANGUAGE = getAudioLanguageField();
        public static final String SUBTITLE_URL = getVideoSubtitleField();
        public static final String SUBTITLE_LANG = getVideoSubtitleLangField();
        public static final String DURATION = getVideoDurationField();
        public static final String MAIN = getVideoMainField();
        public static final String TRAILER = getVideoTrailerField();
        public static final String TRAILER_VIDEO_URL_LANGUAGE = getVideoTrailerLanguageField();
        public static final String TRAILER_AUDIO_URL_LANGUAGE = getAudioTrailerLanguageField();
        public static final String TRAILER_SUBTITLE = getVideoTrailerSubtitleField();
        public static final String TRAILER_SUBTITLE_LANG = getVideoTrailerSubtitleLangField();
        public static final String RATINGS = getVideoRatingField();
        public static final String A_RECORD = getVideoARecordField();
        public static final String SLIDER_TYPE = getSliderTypeField();
        public static final String PARENT_MEDIA = getParentMediaField();
        public static final String WATCH_COUNT = getVideoWatchCountField();
        public static final String IS_SINGLE =  getVideoIsSingleField();
        public static final String IS_MAIN = getVideoIsMainField();
        public static final String IS_TRAILER_FREE = getVideoIsTrailerFreeField();
        public static final String TRAILER_VIDEO = getTrailerVideoField();
        public static final String TRAILER_DEFAULT_IMAGE = getTrailerDefaultImageField();
        public static final String TRAILER_BROWSE_IMAGE = getTrailerBrowseImageField();
        public static final String TRAILER_MOBILE_IMAGE = getTrailerMobileImageField();
        public static final String IS_PPV = getVideoIsPPVField();
        public static final String VIDEO_TYPE = getVideoTypeField();
        public static final String RATING_TIME = getRatingTimeField();
        public static final String RATING_TYPE = getRatingTypeField();
        public static final String RATING_URL = getRatingUrlField();
        public static final String RATING_TRAILER_TIME = getRatingTrailerTimeField();
        public static final String RATING_TRAILER_TYPE = getRatingTrailerTypeField();
        public static final String RATING_TRAILER_URL = getRatingTrailerUrlField();
        public static final String IS_KIDS = getVideoIsKidsField();
        public static final String DOWNLOAD_STATUS = getDownloadStatusField();
        public static final String DETAILS = getDetailsField();
        public static final String WISHLIST_STATUS = getWishlistStatusField();
        public static final String HISTORY_STATUS = getHistoryStatusField();
        public static final String IS_LIKED = getIsLikedField();
        public static final String LIKES = getLikesField();
        public static final String CAST_CREW = getCastCrewField();
        public static final String CAST_CREW_ID = getCastCrewsIdField();
        public static final String DIRECTOR = getDirectorField();
        public static final String GENRE = getGenerField();
        public static final String SEASON = getSeasonField();
        public static final String GENREMAIN = getGenerMainField();
        public static final String SEARCH_TERM = getSearchField();
        public static final String LIKE_COUNT = getLikeCountField();
        public static final String UN_LIKE_COUNT = getDislikeCountField();
        public static final String SHARE_LINK = getShareLinkField();
        public static final String HISTORY_ID = getHistoryidField();
        public static final String WISHLIST_ID = getWishListIdField();
        public static final String VALUE = getValueField();
        public static final String REASON = getReasonField();
        public static final String REMAINING_AMT = getAmountRemainingField();
        public static final String CATEGORY = getCategoryField();
        public static final String SUB_CATEGORY = getSubCategoryField();
        public static final String GENRES = getGenersField();
        public static final String GENRE_VIDEOS = getVideoGeneresField();
        public static final String IS_SELECTED = getIsSelectedField();
        public static final String IS_SEASON_VIDEO = getIsSeriresField();
        public static final String TRAILER_SECTION = getTrailerSectionField();
        public static final String DOWNLOAD_RESOLUTION = getDownloadResolutionField();
        public static final String IMAGE = getImageField();
        public static final String RESOLUTIONS = getResolutionField();
        public static final String MAIN_VIDEO_RESOLUTIONS = getMainVideoResolutionField();
        public static final String ORIGINAL = getOriginalField();
        public static final String URL_TYPE = getUrlTypeField();
        public static final String URL_PAGE_ID = getUrlPageIdField();
        public static final String GENRE_NAME = getGenreNameField();
        public static final String DOWNLOAD_BUTTON_STATUS = getDownloadButtonStatusField();
        public static final String DOWNLOAD_VIEW_STATUS = getDownloadingViewStatusField();
        public static final String DOWNLOADED_VIEW_STATUS = getDownloadedViewStatusField();
        public static final String DOWNLOAD_URLS = getDownloadUrlField();
        public static final String TYPE = getTypeField();
        public static final String LINK = getLinkField();
        public static final String PPV_AMOUNT = getPPVAmountField();
        public static final String PPV_PAGE_TYPE = getPPVPageTypeField();
        public static final String PPV_TYPE_CONTENT =getPPVPageTypeContentField();
        public static final String SEEK_HERE = getSeekTimeField();
        public static final String DELETE_SUB_PROFILE = getDeleteSubProfileField();
        public static final String CLEAR_ALL_STATUS = getClearStatusField();
        public static final String COUNT = getCountField();
        public static final String IS_SPAM = getIsSpamField();
        public static final String BANNER_IMAGE = getBannerImageField();
        public static final String SHOULD_DISPLAY_PPV = getPPVDisplayField();
        public static final String PRIVACY_POLICY = getPrivacyField();
        public static final String REFUND_POLICY = getRefundField();
        public static final String HELP = getHelpField();
        public static final String TERMS = getTermsField();
        public static final String ABOUT = getAboutField();
        public static final String PAGE_TYPE_ID = getPageTypeIdField();
        public static final String URL_TYPE_ID = getUrlTypeIdField() ;
        public static final String REFERRAL_EARNNINGS = getReferralEarningsField();
        public static final String REFERRAL_CODE = getReferralCodeField();
        public static final String SHARE_MESSAGE = getShareMsgField();
        public static final String AMOUNT_TOTAL = getAmountTotalField();
        public static final String REMAINING = getRemainingField();
        public static final String REFERRAL_AMOUNT = getReferralAmountField();
        public static final String PAY_AMOUNT = getPayAmountField();
        public static final String REFERRAL_AMOUNT_FORMATTED = getReferralAmountFormatField();
        public static final String PAY_AMOUNT_FORMATTED = getPayAmountFormatField();
        public static final String WALLET_AMOUNT = getWalletAmountField();
        public static final String LISTEDPRICE = getListedPriceField();
        public static final String SUBSCRIPTION_AMOUNT = getSubscriptionAmountField();
        public static final String IS_WALLET_CREDIT_APPLIED = getWalletAppliedField();
        public static final String LANGUAGE = getLanguageField();
        public static final String NEXT_VIDEO_ID = getNextVideoIdField();
        public static final String VIDEO = getVideoField();
        public static final String RESOLUTION = getVideoResolutionField();
        public static final String MOBILE_BANNER_IMAGE = getMobileBannerImgField();
        public static final String PLAYER_JSON = getPlayerJsonField();
        public static final String NO_OF_DAYS = getTotalDaysField();
        public static final String DOWNLOAD_EXPIRY = getDownloadedExpiryField();
        public static final String TRANSITION_IMAGE = getTransitionImageField();
        public static final String TRANSITION_TITLE = getTransitionTitleField();
        public static final String DISCOUNT = getDiscountField();
        public static final String DAYS = getDaysField();
        public static final String PERCENTAGE = getPercentageField();
        public static final String TYPE_PAGE = getNotify();
        public static final String IS_NOTIFY = getIsNotify();
        public static final String COMING_SOON_ADMIN_VIDEO_ID = getAdminVideoId();
        public static final String APPR_RATING = getAppRating();
        public static final String APP_REVIEW = getAppReview();

        //Paytm
        public static final String PAYMENT_RESP = getPaytmRespField();
        public static final String ORDER_ID = getOrderIdField();
        public static final String CUST_ID = getCustomerIdField();
        public static final String INDUSTRY_TYPE_ID = getIndustryIdField();
        public static final String WEBSITE = getWebsiteField();
        public static final String CHANNEL_ID = getChannelIdField();
        public static final String TXN_AMOUNT = getTransactionAmountField();
        public static final String CALLBACK_URL = getCallbackUrlField();
        public static final String PAYTM_STATUS = getPaytmStatusField();
        public static final String TRANSACTION_ID = getTransactionIdField();
        public static final String CHECKSUMHASH = getChecksumField();
        public static final String TXN_TOKEN = getTransactionToken();
        public static final String BANK_NAME = getBankNameField();
        public static final String ORDERID = getOrderIDField();
        public static final String TXNAMOUNT = getTransactionAmtField();
        public static final String TXNDATE = getTransactionDateField();
        public static final String MID = getMerchantIdField();
        public static final String TXNID = getPaytmTransactionIdField();
        public static final String RESPCODE = getResponseCodeField();
        public static final String PAYMENTMODE = getPaytmPaymentModeField();
        public static final String BANKTXNID = getBankTransactionIdField();
        public static final String GATEWAYNAME = getGatewayNameField();
        public static final String RESPMSG = getResponseMessageField();
        public static final String PAYTM_CALLBACK_URL = getPaytmCallbackUrlField();
        public static final String TEST_CALLBACK_URL = getTestCallbackUrlField();
        public static final String LIVE_CALLBACK_URL = getLiveCallbackUrlField();

        public static final String PARAM_ORDER_ID = getParamOrderIdField();
        public static final String PARAM_CUST_ID = getParamCustomerIdField();
        public static final String PARAM_INDUSTRY_TYPE_ID = getParamIndustryIdField();
        public static final String PARAM_CHANNEL_ID = getParamChannelIdField();
        public static final String PARAM_TXN_AMOUNT = getParamTransactionAmtField();
        public static final String PARAM_CALLBACK_URL = getParamCallbackUrlField();
        public static final String PAYMENT_GATEWAY_NAME = getPaymentGatewayName();
        public static final String PAYMENT_GATEWAY_TYPE = getPaymentGatewayType();

        //Agreepay
        public static final String AP_API_KEY = getAgreePayAPIKeyField();
        public static final String AP_HOST_NAME = getAgreePayHostNameField();
        public static final String AP_RETURN_URL = getAgreePayReturnUrlField();
        public static final String AP_MODE = getAgreePayModeField();
        public static final String AP_CURRENCY = getAgreePayCurrencyField();
        public static final String AP_COUNTRY = getAgreePayCountryField();

        public static final String AP_RESP_MSG = getAgreePayResponseMessageField();
        public static final String AP_ERROR_MSG = getAgreePayErrorMessageField();
        public static final String AP_ORDER_ID = getAgreePayOrderIdField();
        public static final String AP_TRANSACTION_ID = getAgreePayTransactionIdField();
        public static final String AP_PAYMENT_METHOD = getAgreePayPaymentMethodField();
        public static final String AP_PAYMENT_MODE = getAgreePayPaymentModeField();
        public static final String AP_AMOUNT = getAgreePayAmountField();

        //Google Play Billing
        public static final String GOOGLE_PLAY_ORDERID = getGooglePlayOrderIdField();
        public static final String GOOGLE_PLAY_PACKAGE_NAME = getGooglePlayPackageNameField();
        public static final String GOOGLE_PLAY_PRODUCTID = getGooglePlayProductIdField();
        public static final String GOOGLE_PLAY_PURCHASE_TIME = getGooglePlayPurchaseTimeField();
        public static final String GOOGLE_PLAY_PURCHASE_STATE = getGooglePlayPurchaseStateField();
        public static final String GOOGLE_PLAY_PURCHASE_TOKEN = getGooglePlayPurchaseTokenField();
        public static final String GOOGLE_PLAY_QUANTITY = getGooglePlayQuantityField();
        public static final String GOOGLE_PLAY_AUTO_RENEWING = getGooglePlayAutoRenewingField();
        public static final String GOOGLE_PLAY_ACKNOWLEDGED = getGooglePlayAcknowledgedField();

        //Cookie
        public static final String COOKIE_KEY_PAIR = getKeyPairField();
        public static final String COOKIE_POLICY = getPolicyField();
        public static final String COOKIE_SIGNATURE = getSignatureField();

        public static final String IP = getIPField();
        public static final String DISTRIBUTOR = getDistributorField();
        public static final String REFERRER = getReferrerField();
        public static final String DEVICE_CODE = getInstallField();

        //Share link
        public static final String SHARE_URL_CODE = getShareUrlCodeField();
        public static final String SHARE_URL_TYPE = getShareUrlTypeField();
        public static final String SHARE_USER_ID = getShareUrlUserIdField();

        //TYPE
        public static final String SHARE_TYPE_SUBSCRIBE = getShareTypeSubscribeField();
        public static final String SHARE_TYPE_LOGIN = getShareTypeLoginField();
        public static final String SHARE_TYPE_INSTALL = getShareTypeInstallField();

        Params() {

        }
    }

    public static class STATIC_PAGES {

        public static final String ABOUT_URL = "http://demo.bhoju.app/#/page/3";
        public static final String TERMS_URL = "http://demo.bhoju.app/#/page/7";
        public static final String HELP_URL = "http://demo.bhoju.app/#/page/5";
        public static final String SPEED_TEST_URL = "https://fast.com";
        public static final String PRIVACY_URL = "http://demo.bhoju.app/#/page/6";
        public static final String REFUND_URL = "http://demo.bhoju.app/#/page/6";
    }


    //ADD try catch to all API calls
}
