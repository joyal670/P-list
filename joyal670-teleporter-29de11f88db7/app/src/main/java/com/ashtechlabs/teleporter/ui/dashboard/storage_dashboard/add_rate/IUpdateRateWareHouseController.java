package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.add_rate;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface IUpdateRateWareHouseController {
    void onUpdateWareHouseDriver(String token, int wareHouseId, String perCBMAmount, String dateFrom, String dateTo,int additionalInfo, int perishableDet,String minInsuranceAmt, String insPercent,String rateValidity, String totalCBMAvailable,int currency);
    }
