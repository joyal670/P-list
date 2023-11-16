
package com.protenium.irohub.model.menu;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class MealType {

    @SerializedName("foods")
    private List<Food> mFoods;
    @SerializedName("id")
    private Long mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("ordered_count")
    private Long mOrderedCount;

    public List<Food> getFoods() {
        return mFoods;
    }

    public void setFoods(List<Food> foods) {
        mFoods = foods;
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

    public Long getOrderedCount() {
        return mOrderedCount;
    }

    public void setOrderedCount(Long orderedCount) {
        mOrderedCount = orderedCount;
    }

}
