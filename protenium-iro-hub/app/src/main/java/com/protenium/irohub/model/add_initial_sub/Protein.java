
package com.protenium.irohub.model.add_initial_sub;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Protein {

    @SerializedName("name")
    private String mName;
    @SerializedName("value")
    private Long mValue;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Long getValue() {
        return mValue;
    }

    public void setValue(Long value) {
        mValue = value;
    }

}
