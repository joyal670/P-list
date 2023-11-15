
package com.protenium.irohub.model.home_detailed;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class MealPlan {

    @SerializedName("description")
    private String mDescription;
    @SerializedName("durations")
    private List<Duration> mDurations;
    @SerializedName("id")
    private Long mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("info_text")
    private String mInfoText;
    @SerializedName("meal_category_id")
    private Long mMealCategoryId;
    @SerializedName("meal_category_name")
    private String mMealCategoryName;
    @SerializedName("name")
    private String mName;
    @SerializedName("off_days")
    private List<String> mOffDays;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public List<Duration> getDurations() {
        return mDurations;
    }

    public void setDurations(List<Duration> durations) {
        mDurations = durations;
    }

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

    public String getInfoText() {
        return mInfoText;
    }

    public void setInfoText(String infoText) {
        mInfoText = infoText;
    }

    public Long getMealCategoryId() {
        return mMealCategoryId;
    }

    public void setMealCategoryId(Long mealCategoryId) {
        mMealCategoryId = mealCategoryId;
    }

    public String getMealCategoryName() {
        return mMealCategoryName;
    }

    public void setMealCategoryName(String mealCategoryName) {
        mMealCategoryName = mealCategoryName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<String> getOffDays() {
        return mOffDays;
    }

    public void setOffDays(List<String> offDays) {
        mOffDays = offDays;
    }

}
