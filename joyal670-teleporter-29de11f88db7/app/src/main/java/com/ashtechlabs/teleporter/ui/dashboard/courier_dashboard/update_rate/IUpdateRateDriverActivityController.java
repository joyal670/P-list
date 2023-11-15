package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.update_rate;

/**
 * Created by Jesna on 6/14/2017.
 */

public interface IUpdateRateDriverActivityController {
    void getUpdateRateCard(int updateType, String token, int deliveryType, String fromLocation, String toLocation, String minAmount, String perKGAmount, String rateValidity,
                           String duration, int currency, String ID);

}
