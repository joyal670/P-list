package com.ashtechlabs.teleporter.ui.pricing.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 31-Jan-17.
 */

public class PriceDriverList {

    @SerializedName("deliveryserviceratecard")
    @Expose
    private ArrayList<PriceCourier> pricing;

   /* @SerializedName("mpratecard")
    @Expose
    private ArrayList<PriceCourier> pricingrate;*/

  /*  public ArrayList<PriceCourier> getPricingrate() {
        return this.pricingrate;
    }

    public void setPricingrate(ArrayList<PriceCourier> pricingrate) {
        this.pricingrate = pricingrate;
    }*/

    public ArrayList<PriceCourier> getPricingService() {
        return pricing;
    }

    public void setPricingService(ArrayList<PriceCourier> pricings) {
        pricing = pricings;
    }
}