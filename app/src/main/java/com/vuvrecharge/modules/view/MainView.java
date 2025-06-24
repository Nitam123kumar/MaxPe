package com.vuvrecharge.modules.view;


public interface MainView {
    void openHome();

    void openSetting();

    void openBrand();

    void openCall();

    void openMessage();

    void onSuccess();

    void makeVisible();

    void onSuccess(String message);
}
