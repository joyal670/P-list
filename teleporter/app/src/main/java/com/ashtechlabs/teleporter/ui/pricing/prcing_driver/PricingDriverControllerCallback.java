package com.ashtechlabs.teleporter.ui.pricing.prcing_driver;

import com.ashtechlabs.teleporter.ui.ICommonCallback;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceDriverList;
import com.ashtechlabs.teleporter.util.Constants;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface PricingDriverControllerCallback extends ICommonCallback{
    void onGetPricingDetails(PriceDriverList reviews);

    void onGetPricingFailed(String message);

    void onRateCardDeleted(String message);

}
