package com.ashtechlabs.teleporter.ui.pricing.prcing_trucking;

import com.ashtechlabs.teleporter.ui.ICommonCallback;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceDriverList;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceTrucking;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceTruckingList;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface PricingTruckingControllerCallback extends ICommonCallback{
    void onGetTruckingPricingDetails(ArrayList<PriceTrucking> cartItems);

    void onGetPricingFailed(String message);

    void onRateCardDeleted(String message);
}
