package com.vuvrecharge.modules.presenter;

public interface OnFragmentListener {
    void onShowToast(String message);
    void onError(String error);
    void onShowDialog(String message);
    void onHideDialog();
}
