package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_rate;

/**
 * Created by Jesna on 6/14/2017.
 */

public interface IUpdateRateVendorActivityController {
    void getUpdateVendorRateCard(int rateCardType, String token, String ID, String serviceType, String fromLocation, String toLocation, int additional_info, int perishable_det, String minAmount, String perKGAmount, String rateValidity, String insurancePercentage, String minInsuranceAmt, String duration, int currency);

}
