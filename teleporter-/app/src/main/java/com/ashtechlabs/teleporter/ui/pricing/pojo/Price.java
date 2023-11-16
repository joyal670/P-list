package com.ashtechlabs.teleporter.ui.pricing.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public class Price {

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("value")
    @Expose
    private PriceDriverList pricing;

    public String getCode() {
        return this.code;
    }

    public PriceDriverList getPricing() {
        return this.pricing;
    }
}
