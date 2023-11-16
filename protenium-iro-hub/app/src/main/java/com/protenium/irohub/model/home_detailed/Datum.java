
package com.protenium.irohub.model.home_detailed;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Datum {

    @SerializedName("amount")
    private String mAmount;
    @SerializedName("quantity")
    private String mQuantity;

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }

}
