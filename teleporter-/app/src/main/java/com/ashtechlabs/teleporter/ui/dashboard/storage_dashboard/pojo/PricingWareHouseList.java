package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public class PricingWareHouseList {

    @SerializedName("warehousingratecard")
    @Expose
    private ArrayList<PricingWareHouseParse> pricing;


    public ArrayList<PricingWareHouseParse> getPricingService() {
        return pricing;
    }

    public void setPricingService(ArrayList<PricingWareHouseParse> pricings) {
        pricing = pricings;
    }
}