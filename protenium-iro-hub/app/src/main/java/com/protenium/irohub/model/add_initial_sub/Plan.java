
package com.protenium.irohub.model.add_initial_sub;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Plan {

    @SerializedName("name")
    private String mName;
    @SerializedName("value")
    private Double mValue;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Double getValue() {
        return mValue;
    }

    public void setValue(Double value) {
        mValue = value;
    }

}
