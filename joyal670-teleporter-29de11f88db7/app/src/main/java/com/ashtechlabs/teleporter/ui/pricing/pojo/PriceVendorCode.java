package com.ashtechlabs.teleporter.ui.pricing.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public class PriceVendorCode {
    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("value")
    @Expose
    private PriceVendorList pricing;

    public String getCode() {
        return this.code;
    }

    public PriceVendorList getPricing() {
        return this.pricing;
    }
}
