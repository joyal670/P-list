package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.add_rate;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface IUpdateRateVendorsController {
    void onUpdateVendor(String token, String serviceType, String fromLocation, String toLocation, int additional_info, int perishable_det, String minAmount, String perKGAmount, String rateValidity, String insurancePercentage, String minInsuranceAmt, String duration, int currency);
}
