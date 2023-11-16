package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public class PricingWarehouse {
    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("value")
    @Expose
    private PricingWareHouseList pricing;

    public String getCode() {
        return this.code;
    }

    public PricingWareHouseList getPricing() {
        return this.pricing;
    }
}