
package com.protenium.irohub.model.SubscriptionInfo;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Pending {

    @SerializedName("date")
    private String mDate;
    @SerializedName("foods")
    private List<Food> mFoods;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public List<Food> getFoods() {
        return mFoods;
    }

    public void setFoods(List<Food> foods) {
        mFoods = foods;
    }

}
