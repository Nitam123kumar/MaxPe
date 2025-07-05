
package com.vuvrecharge.modules.model;

import java.util.List;

public class UserData {

    private String mobile;
    private String name;
    private String email;
    private String earnings;
    private String date;
    private String user_type;
    private String news;
    private String today_total;
    private String today_count;
    private String yesterday_total;
    private String yesterday_count;
    private String this_month_total;
    private String this_month_count;
    private String last_month_total;
    private String last_month_count;
    private String paytm_getway;
    private String commission_earned;
    private String update_message = "";
    private String version_code = "1";
    private String update_version_code = "1";
    private String update_version_name = "1.0";
    private String game_banner = "";
    private String game_banner_link = "";
    private String cashfree;
    private  String whatsapp_number;
    private List<DthCommissionBean> prepaid_commission;
    private List<DthCommissionBean> dth_commission;
    private  List<SliderData> slides;
    private  List<YoutubeSlides> youtube_slides;
    private  List<OfferSlider> offer_slides;

    private List<SliderItems> recharge_slides_data;
    String referCode;
    private String shareText;
    private String offer_slides_path;
    private String main_hero_banner;
    private String support_number;
    private String support_email;
    private String address;
    private String company_name;
    private String support_message;
    private String is_mpin_set;
    private String callus_logo;
    private String whatsapp_logo;
    private String address_logo;
    private String email_logo;
    private String recharge_pay_bill_string;
    private String youtube_path;
    private String slide_path;


    public String getRecharge_pay_bill_string() {
        return recharge_pay_bill_string;
    }

    public void setRecharge_pay_bill_string(String recharge_pay_bill_string) {
        this.recharge_pay_bill_string = recharge_pay_bill_string;
    }

    public String getSlide_path() {
        return slide_path;
    }

    public void setMain_hero_banner(String main_hero_banner) {
        this.main_hero_banner = main_hero_banner;
    }

    public String getMain_hero_banner() {
        return main_hero_banner;
    }

    public void setSlide_path(String slide_path) {
        this.slide_path = slide_path;
    }


    public String getYoutube_path() {
        return youtube_path;
    }

    public void setYoutube_path(String youtube_path) {
        this.youtube_path = youtube_path;
    }

    public String getOffer_slides_path() {
        return offer_slides_path;
    }

    public void setOffer_slides_path(String offer_slides_path) {
        this.offer_slides_path = offer_slides_path;
    }


    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public String getReferPageText() {
        return referPageText;
    }

    public void setReferPageText(String referPageText) {
        this.referPageText = referPageText;
    }

    public String getRefer_and_earn_banner() {
        return refer_and_earn_banner;
    }

    public void setRefer_and_earn_banner(String refer_and_earn_banner) {
        this.refer_and_earn_banner = refer_and_earn_banner;
    }

    public List<SliderItems> getRecharge_slides_data() {
        return recharge_slides_data;
    }

    public void setRecharge_slides_data(List<SliderItems> recharge_slides_data) {
        this.recharge_slides_data = recharge_slides_data;
    }

    public String getCallus_logo() {
        return callus_logo;
    }

    public void setCallus_logo(String callus_logo) {
        this.callus_logo = callus_logo;
    }

    public String getWhatsapp_logo() {
        return whatsapp_logo;
    }

    public void setWhatsapp_logo(String whatsapp_logo) {
        this.whatsapp_logo = whatsapp_logo;
    }

    public String getAddress_logo() {
        return address_logo;
    }

    public void setAddress_logo(String address_logo) {
        this.address_logo = address_logo;
    }

    public String getEmail_logo() {
        return email_logo;
    }

    public void setEmail_logo(String email_logo) {
        this.email_logo = email_logo;
    }

    private String referPageText,refer_and_earn_banner;
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public List<DthCommissionBean> getPrepaid_commission() {
        return prepaid_commission;
    }

    public void setPrepaid_commission(List<DthCommissionBean> prepaid_commission) {
        this.prepaid_commission = prepaid_commission;
    }

    public List<DthCommissionBean> getDth_commission() {
        return dth_commission;
    }

    public void setDth_commission(List<DthCommissionBean> dth_commission) {
        this.dth_commission = dth_commission;
    }

    public String getUpdate_message() {
        return update_message;
    }

    public void setUpdate_message(String update_message) {
        this.update_message = update_message;
    }

    public String getToday_total() {
        return today_total;
    }

    public void setToday_total(String today_total) {
        this.today_total = today_total;
    }

    public String getToday_count() {
        return today_count;
    }

    public void setToday_count(String today_count) {
        this.today_count = today_count;
    }

    public String getYesterday_total() {
        return yesterday_total;
    }

    public void setYesterday_total(String yesterday_total) {
        this.yesterday_total = yesterday_total;
    }

    public String getYesterday_count() {
        return yesterday_count;
    }

    public void setYesterday_count(String yesterday_count) {
        this.yesterday_count = yesterday_count;
    }

    public String getThis_month_total() {
        return this_month_total;
    }

    public void setThis_month_total(String this_month_total) {
        this.this_month_total = this_month_total;
    }

    public String getPaytm_getway() {
        return paytm_getway;
    }

    public void setPaytm_getway(String paytm_getway) {
        this.paytm_getway = paytm_getway;
    }

    public String getThis_month_count() {
        return this_month_count;
    }

    public void setThis_month_count(String this_month_count) {
        this.this_month_count = this_month_count;
    }

    public String getLast_month_total() {
        return last_month_total;
    }

    public void setLast_month_total(String last_month_total) {
        this.last_month_total = last_month_total;
    }

    public String getLast_month_count() {
        return last_month_count;
    }

    public void setLast_month_count(String last_month_count) {
        this.last_month_count = last_month_count;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getUpdate_version_code() {
        return update_version_code;
    }

    public void setUpdate_version_code(String update_version_code) {
        this.update_version_code = update_version_code;
    }

    public String getUpdate_version_name() {
        return update_version_name;
    }

    public void setUpdate_version_name(String update_version_name) {
        this.update_version_name = update_version_name;
    }

    public String getCommission_earned() {
        return commission_earned;
    }

    public void setCommission_earned(String commission_earned) {
        this.commission_earned = commission_earned;
    }

    public String getGame_banner() {
        return game_banner;
    }

    public void setGame_banner(String game_banner) {
        this.game_banner = game_banner;
    }

    public String getGame_banner_link() {
        return game_banner_link;
    }

    public void setGame_banner_link(String game_banner_link) {
        this.game_banner_link = game_banner_link;
    }

    public String getCashfree() {
        return cashfree;
    }

    public void setCashfree(String cashfree) {
        this.cashfree = cashfree;
    }

    public String getWhatsapp_number() {
        return whatsapp_number;
    }

    public void setWhatsapp_number(String whatsapp_number) {
        this.whatsapp_number = whatsapp_number;
    }

    public List<YoutubeSlides> getYoutubeVideoSliders() {
        return youtube_slides;
    }
    public List<OfferSlider> getOffer_slides() {
        return offer_slides;
    }

    public List<SliderData> getSlides() {
        return slides;
    }

    public void setSlides(List<SliderData> slides) {
        this.slides = slides;
    }
    public void setYoutubeVideoSliders(List<YoutubeSlides> youtubeVideos) {
        this.youtube_slides = youtubeVideos;
    }
    public void setOffer_slides(List<OfferSlider> offer_slides) {
        this.offer_slides = offer_slides;
    }

    public List<SliderItems> getSliderItemsList() {
        return recharge_slides_data;
    }

    public void setSliderItemsList(List<SliderItems> recharge_slides_data) {
        this.recharge_slides_data = recharge_slides_data;
    }

    public String getSupport_number() {
        return support_number;
    }

    public void setSupport_number(String support_number) {
        this.support_number = support_number;
    }

    public String getSupport_email() {
        return support_email;
    }

    public void setSupport_email(String support_email) {
        this.support_email = support_email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getSupport_message() {
        return support_message;
    }

    public void setSupport_message(String support_message) {
        this.support_message = support_message;
    }

    public String getIs_mpin_set() {
        return is_mpin_set;
    }

    public void setIs_mpin_set(String is_mpin_set) {
        this.is_mpin_set = is_mpin_set;
    }
}
