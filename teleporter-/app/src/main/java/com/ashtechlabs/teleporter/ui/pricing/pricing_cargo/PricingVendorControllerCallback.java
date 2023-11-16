package com.ashtechlabs.teleporter.ui.pricing.pricing_cargo;

import com.ashtechlabs.teleporter.ui.ICommonCallback;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceVendor;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceVendorList;
import com.ashtechlabs.teleporter.util.Constants;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public interface PricingVendorControllerCallback extends ICommonCallback{
    void onGetPricingVendor(ArrayList<PriceVendor> reviews);

    void onGetPricingFailed(String message);

    void onRateCardDeleted(String message);
}
