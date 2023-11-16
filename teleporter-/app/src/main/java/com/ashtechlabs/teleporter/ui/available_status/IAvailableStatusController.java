package com.ashtechlabs.teleporter.ui.available_status;

/**
 * Created by VIDHU on 12/18/2017.
 */

public interface IAvailableStatusController {

    void onGetAvailableStatus(String token, int serviceType);

    void onSetAvailableStatus(String token, int serviceType, int dutyStatus);
}
