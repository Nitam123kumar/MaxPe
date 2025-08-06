package com.vuvrecharge.api;

import com.vuvrecharge.modules.model.DefaultResponse;
import com.vuvrecharge.modules.model.YoutubeSlides;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServices {

////    mukesh
//    String SITE_URL = "http://192.168.0.10/vuvpayments.com/home/";
//    String BBPS_IMAGE_URL = "http://192.168.0.10/vuvpayments.com/images/services_logo";
//    String SUBSCRIPTION_IMAGE_URL = "http://192.168.0.10/vuvpayments.com/images/operator_plans/";
//    String BASE_URL = "http://192.168.0.10/vuvpayments.com/";
//    String IMAGE_LOGO = "http://192.168.0.10/vuvpayments.com/images/operators";
//    String IMAGE_APP = "http://192.168.0.10/vuvpayments.com/images/logo";
//    String IMAGE_SLIDER = "http://192.168.0.10/vuvpayments.com/images/recharge_slides";
//    String IMAGE_FOLLOWS = "http://192.168.0.10/vuvpayments.com/images/followers_logo";
//    String SLIDE = "http://192.168.0.10/vuvpayments.com/images/app_slides/";

////    chandan
    String SITE_URL = "http://192.168.0.22/vuvpayments.com/home/";
    String BBPS_IMAGE_URL = "http://192.168.0.22/vuvpayments.com/images/services_logo";
    String SUBSCRIPTION_IMAGE_URL = "http://192.168.0.22/vuvpayments.com/images/operator_plans/";
    String BASE_URL = "http://192.168.0.22/vuvpayments.com/";
    String IMAGE_LOGO = "http://192.168.0.22/vuvpayments.com/images/operators";
    String IMAGE_APP = "http://192.168.0.22/vuvpayments.com/images/logo";
    String IMAGE_SLIDER = "http://192.168.0.22/vuvpayments.com/images/recharge_slides";
    String IMAGE_FOLLOWS = "http://192.168.0.22/vuvpayments.com/images/followers_logo";
    String SLIDE = "http://192.168.0.22/vuvpayments.com/images/app_slides/";
    String YOUTUBE_IMAGE_PATH = "http://192.168.0.22/vuvpayments.com/images/youtube_thumbnail/";
    String OFFER_ZONE = "http://192.168.0.22/vuvpayments.com/images/offer_zone/";
    String OTT_SLIDES = "http://192.168.0.22/vuvpayments.com/images/home_page/";

////    live
//    String BBPS_IMAGE_URL = "http://vuvpayments.com/images/services_logo";
//    String SUBSCRIPTION_IMAGE_URL = "http://vuvpayments.com/images/operator_plans/";
//    String BASE_URL = "https://vuvpayments.com/apise/";
//    String IMAGE_LOGO = "https://vuvpayments.com/images/operators";
//    String IMAGE_APP = "https://vuvpayments.com/images/logo";
//    String IMAGE_SLIDER = "http://vuvpayments.com/images/recharge_slides";
//    String IMAGE_FOLLOWS = "http://vuvpayments.com/images/followers_logo";
//    String SLIDE = "http://vuvpayments.com/images/app_slides/";
//    String SITE_URL = "https://vuvpayments.com/home/";
//    String YOUTUBE_IMAGE_PATH = "http://vuvpayments.com/images/youtube_thumbnail/";
//    String OFFER_ZONE = "http://vuvpayments.com/images/offer_zone/";
//    String OTT_SLIDES = "http://vuvpayments.com/images/home_page/";


    String privacy = SITE_URL + "privacy_policy";
    String about_us = SITE_URL + "app_about";
    String term = SITE_URL + "terms";
    String knowledge = SITE_URL + "app_knowledge_base";
    String return_refund = SITE_URL + "return_policy";
    String services = SITE_URL + "app_services";
    String contact_us = SITE_URL + "app_contact";
    String verifySponsor = "verifySponsor";
    String registerUser = "registerUser";
    String validateRegisterOtp = "validateRegisterOtp";
    String forgotPassword = "forgotPassword";
    String forgotPasswordOtp = "forgotPasswordOtp";
    String resetPassword = "forgotPassword";
    String loginUser = "loginUser";
    String refer_earn = "refer_earn";
    String topRankers = "topRankers";
    String dashboardData = "dashboardData";
    String updatePassword = "updatePassword";
    String transferBalance = "transferBalance";
    String addNewMember = "addNewMember";
    String loadDataForNewMember = "loadDataForNewMember";
    String fetchCities = "fetchCities";
    String totalReferrals = "totalReferrals";
    String myReferralIncome = "myReferralIncome";
    String memberWalletHistory = "memberWalletHistory";
    String memberProfileDetail = "memberProfile";
    String membersList = "membersList";
    String updateSecurityCode = "updateSecurityCode";
    String updateProfile = "profileUpdate";
    String doComplaint = "doComplaint";
    String fetchPrepaidPlans = "fetchPrepaidPlans";
    String fetchDthPlans = "fetchDTHPlans";
    String dthCustomerInfo = "dthCustomerInfo";
    String whatsappAlerts = "whatsappAlerts";
    String historyCircleOperators = "operatorsList";
    String findOperatorCircle = "findOperatorCircle";
    String doMobileRecharge = "doTransaction";
    String userRechargeHistory = "rechargeHistory";
    String complainHistory = "complainHistory";
    String rechargeDetails = "rechargeDetails";
    String supportContacts = "supportContacts";
    String getReferrals = "getReferrals";
    String getPaymentSetting = "getPaymentSetting";
    String generateChecksum = "generateChecksum";
    String payTMResponse = "payTMResponse";
    String onlineDepositHistory = "onlineDepositHistory";
    String recentSuccessDepositHistory = "recentSuccessDepositHistory";
    String fundRequestHistory = "fundRequestHistory";
    String fundRequest = "fundRequest";
    String generateCashfreeChecksum = "generateCashfreeChecksum";
    String bankDetails = "bankDetails";
    String walletHistory = "walletHistory";
    String payupayments = "payupayments";
    String generateRazorPayOrder = "generateRazorPayOrder";
    String payuresponse = "payuresponse";
    String generatePayuHash = "generatePayuHash";
    String commissionReport = "commissionReport";
    String fetchBalance = "fetchBalance";
    String referAndEarnFaqs = "referAndEarnFaqs";
    String myReferrals = "myReferrals";
    String getCustomerCareNumber = "getCustomerCareNumber";
    String fetchRofferPlans = "fetchRofferPlans";
    String setMpin = "setMpin";
    String myCommissionPackage = "myCommissionPackage";
    String myDownlinePackages = "myDownlinePackages";
    String updateMpin = "updateMpin";
    String forgotMpin = "forgotMpin";
    String forgotMpinOtp = "forgotMpinOtp";
    String iciciDynamicQR = "iciciDynamicQR";
    String hdfcbankDynamicQR = "hdfcbankDynamicQR";
    String orderDetailsNew = "orderDetailsNew";
    String phonepeDynamicQR = "phonepeDynamicQR";

    //    TODO new APIs
    String totalReferralsNew = "totalReferralsNew";
    String getCommissions = "getCommissions";
    String getRechargeSlider = "getRechargeSlider";
    String operatorsDetails = "operatorDetails";
    String getAppLogo = "getAppLogo";
    String getBbpsPayBills = "getBbpsPayBills";
    String getAllRechargeServices = "getAllRechargeServices";
    String cashbackPointsHistory = "cashbackPointsHistory";
    String fetchBillInfo = "fetchBillInfo";
    String getFeedback = "getFeedback";
    String getFollowersList = "getFollowersList";
    String doBbpsComplaint = "doBbpsComplaint";
    String generateRazorPayOrderNew = "generateRazorPayOrderNew";
    String fetchPrepaidPlansLocal = "fetchPrepaidPlansLocal";
    String complaintTracking = "complaintTracking";
    String checkLoginWithOtpStatus = "checkLoginWithOtpStatus";
    String sendUserOtp = "sendUserOtp";
    String loginUserWithOtp = "loginUserWithOtp";
    String addInsufficientBalance = "addInsufficientBalance";

    @FormUrlEncoded
//    @POST("apisf/")
    @POST("apise")
    Call<DefaultResponse> defaultRequest(@Field("data") String data);

}