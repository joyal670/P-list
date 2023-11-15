package com.ashtechlabs.teleporter.ui.dashboard.map_controller;

/**
 * Created by IROID_ANDROID1 on 15-Feb-17.
 */

public interface IMapFragmentController {
    void onUpdateDriverLocation(String token,int mode, String latitude, String longitude);

    void onUpdateVendorLocation(String token, String latitude, String longitude);

    void getFromLatLng(String locationName);

    void getToLatLng(String locationName);
}
