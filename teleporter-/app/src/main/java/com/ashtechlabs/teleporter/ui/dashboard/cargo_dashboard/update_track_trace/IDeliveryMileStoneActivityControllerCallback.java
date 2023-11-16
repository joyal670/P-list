package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_track_trace;

import com.ashtechlabs.teleporter.ui.ICommonCallback;

/**
 * Created by Jesna on 6/21/2017.
 */

public interface IDeliveryMileStoneActivityControllerCallback extends ICommonCallback {

    void getTrackTrace(String message);

    void getTrackTraceFailed(String message);

    void onGetOrderStatus(int message);

    void onGetOrderStatus(int orderSattus, String message);


}
