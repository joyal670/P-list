package com.ashtechlabs.teleporter.ui.pricing.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public class PriceWareHouseList {

    @SerializedName("warehousingratecard")
    @Expose
    private ArrayList<PricingWareHouse> pricing;


    public ArrayList<PricingWareHouse> getPricingService() {
        return pricing;
    }

    public void setPricingService(ArrayList<PricingWareHouse> pricings) {
        pricing = pricings;
    }
}