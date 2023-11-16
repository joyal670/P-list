
package com.protenium.irohub.model.home;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class MealCategory {

    @SerializedName("id")
    private Long mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("meal_plans")
    private List<MealPlan> mMealPlans;
    @SerializedName("name")
    private String mName;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public List<MealPlan> getMealPlans() {
        return mMealPlans;
    }

    public void setMealPlans(List<MealPlan> mealPlans) {
        mMealPlans = mealPlans;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
