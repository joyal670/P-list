package com.ashtechlabs.teleporter.ui.pricing.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public class PriceTruckingList {
    @SerializedName("mpratecard")
    @Expose
    private ArrayList<PriceTrucking> pricing;

    public ArrayList<PriceTrucking> getPricingServices() {
        return pricing;
    }

    public void setPricingService(ArrayList<PriceTrucking> pricings) {
        pricing = pricings;
    }
}
