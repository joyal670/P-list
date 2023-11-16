package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.add_rate;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface IUpdateRateCourierController {
    void onUpdateDriver(String token, String mode, String fromLocation, String toLocation,
                        String minAmount, String perKGAmount, String rateValidity, String duration, int selected_currency);

}
