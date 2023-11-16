package com.ashtechlabs.teleporter.ui.available_status;

/**
 * Created by VIDHU on 12/18/2017.
 */

public interface IAvailableStatusControllerCallback {

    void onAvailableStatusChanged(int status, String mobileNo, String message);

    void onAvailableStatusChanged(int status, String message);

}
