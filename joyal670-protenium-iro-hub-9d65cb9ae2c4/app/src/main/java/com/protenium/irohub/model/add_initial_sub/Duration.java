
package com.protenium.irohub.model.add_initial_sub;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Duration {

    @SerializedName("name")
    private String mName;
    @SerializedName("value")
    private String mValue;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}
