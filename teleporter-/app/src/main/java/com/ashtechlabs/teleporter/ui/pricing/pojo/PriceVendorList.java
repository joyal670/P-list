package com.ashtechlabs.teleporter.ui.pricing.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public class PriceVendorList {
    @SerializedName("freightforwardratecard")
    @Expose
    private ArrayList<PriceVendor> pricing;


    public ArrayList<PriceVendor> getPricingService() {
        return pricing;
    }

    public void setPricingService(ArrayList<PriceVendor> pricings) {
        pricing = pricings;
    }
}
