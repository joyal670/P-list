
package com.protenium.irohub.model.dislike.getdislike;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Tag {

    @SerializedName("disliked")
    private Boolean mDisliked;
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;

    public Boolean getDisliked() {
        return mDisliked;
    }

    public void setDisliked(Boolean disliked) {
        mDisliked = disliked;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
