package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.add_rate;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface IUpdateRateTruckingController {

    void onUpdateTruckingRateCard(String token, int vehicleType, int subVehicleType, String fromLocation, String toLocation, String amount, String rateValidity, String insPercent, String insuranceMinAmt, String duration, int selected_currency);

}
