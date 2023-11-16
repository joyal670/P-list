package com.ashtechlabs.teleporter.ui.dashboard.map_controller;

import com.google.android.gms.maps.model.LatLng;
import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by IROID_ANDROID1 on 15-Feb-17.
 */

public interface MapFragmentControllerCallback extends ICommonProgressCallback{

    void onGetMapDetails(String message);

    void onGetFromLatLng(LatLng from);

    void onGetToLatLng(LatLng to);

    void onGetMapDetailsFailed(String message);

}
