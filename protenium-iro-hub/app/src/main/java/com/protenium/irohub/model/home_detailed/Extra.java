
package com.protenium.irohub.model.home_detailed;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Extra {

    @SerializedName("data")
    private List<Datum> mData;
    @SerializedName("id")
    private Long mId;
    @SerializedName("name")
    private String mName;

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
        mData = data;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
