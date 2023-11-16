package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_track_trace;

/**
 * Created by Jesna on 6/21/2017.
 */

public interface IDeliveryMileStoneActivityController {
   void onTrackTrace(String oderID, String orderStatus, String message);

   void getTrackTrace(String oderID);
}
