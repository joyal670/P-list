
package com.protenium.irohub.model.home_detailed;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Carb {

    @SerializedName("carbs")
    private String mCarbs;
    @SerializedName("carbs_price")
    private String mCarbsPrice;

    public String getCarbs() {
        return mCarbs;
    }

    public void setCarbs(String carbs) {
        mCarbs = carbs;
    }

    public String getCarbsPrice() {
        return mCarbsPrice;
    }

    public void setCarbsPrice(String carbsPrice) {
        mCarbsPrice = carbsPrice;
    }

}
