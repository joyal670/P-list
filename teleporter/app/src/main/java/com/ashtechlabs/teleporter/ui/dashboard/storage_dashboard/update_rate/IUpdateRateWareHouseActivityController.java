package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.update_rate;

/**
 * Created by Jesna on 6/14/2017.
 */

public interface IUpdateRateWareHouseActivityController {
    void getUpdateWareHouseRateCard(int updateType, String token,String ID,int wareHouseId, String perCBMAmount, String dateFrom, String dateTo,int additionalInfo, int perishableDet,String minInsuranceAmt, String insPercent,String rateValidity, String totalCBMAvailable,int currency);

}
