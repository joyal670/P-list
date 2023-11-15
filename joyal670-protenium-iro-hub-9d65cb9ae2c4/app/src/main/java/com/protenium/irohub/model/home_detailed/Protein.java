
package com.protenium.irohub.model.home_detailed;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Protein {

    @SerializedName("proteins")
    private String mProteins;
    @SerializedName("proteins_price")
    private String mProteinsPrice;

    public String getProteins() {
        return mProteins;
    }

    public void setProteins(String proteins) {
        mProteins = proteins;
    }

    public String getProteinsPrice() {
        return mProteinsPrice;
    }

    public void setProteinsPrice(String proteinsPrice) {
        mProteinsPrice = proteinsPrice;
    }

}
