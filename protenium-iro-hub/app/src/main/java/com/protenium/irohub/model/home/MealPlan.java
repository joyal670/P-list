
package com.protenium.irohub.model.home;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MealPlan {

    @SerializedName("id")
    private Long mId;
    @SerializedName("meal_types")
    private String mMealTypes;
    @SerializedName("name")
    private String mName;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getMealTypes() {
        return mMealTypes;
    }

    public void setMealTypes(String mealTypes) {
        mMealTypes = mealTypes;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
