package com.ashtechlabs.teleporter.ui;

/**
 * Created by VIDHU on 3/30/2017.
 */

public interface OnOrderInfoAdapterCallback {

    void showProgressAdapter();

    void dismissProgressAdapter();

    void showMessageAlert(String title, String message);

}
