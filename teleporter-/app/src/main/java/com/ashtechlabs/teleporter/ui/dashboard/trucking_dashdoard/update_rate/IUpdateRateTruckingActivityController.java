package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.update_rate;

/**
 * Created by Jesna on 6/14/2017.
 */

public interface IUpdateRateTruckingActivityController {
    void getUpdateRateCard(int updateType, String token, int vechicleType, int subVehicleType, String fromLocation, String toLocation, String amount, String rateValidity, String insPercent, String insuranceMinAmt, String duration, int currency, String ID);
}
