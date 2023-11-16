package com.ashtechlabs.teleporter.ui.pricing.prcing_driver;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface PricingDriverController {
    void onPricingController(String token);

    void onDeleteRateCard(String token, String id);
}

