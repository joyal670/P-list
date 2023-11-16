package com.ashtechlabs.teleporter.ui.pricing.pricing_ware_house;

import com.ashtechlabs.teleporter.ui.ICommonCallback;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PricingWareHouse;


import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public interface PricingWarehouseControllerCallback extends ICommonCallback{
    void onGetPricingWareHouseDetails(ArrayList<PricingWareHouse> reviews);

    void onGetPricingFailed(String message);

    void onRateCardDeleted(String message);

}
