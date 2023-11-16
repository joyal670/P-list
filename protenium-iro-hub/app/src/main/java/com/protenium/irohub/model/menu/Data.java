
package com.protenium.irohub.model.menu;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Data {

    @SerializedName("disliked_tags")
    private List<Object> mDislikedTags;
    @SerializedName("extras")
    private List<Object> mExtras;
    @SerializedName("meal_plan_limits")
    private List<MealPlanLimit> mMealPlanLimits;
    @SerializedName("meal_type_id_combinations")
    private List<List<String>> mMealTypeIdCombinations;
    @SerializedName("meal_types")
    private List<MealType> mMealTypes;

    public List<Object> getDislikedTags() {
        return mDislikedTags;
    }

    public void setDislikedTags(List<Object> dislikedTags) {
        mDislikedTags = dislikedTags;
    }

    public List<Object> getExtras() {
        return mExtras;
    }

    public void setExtras(List<Object> extras) {
        mExtras = extras;
    }

    public List<MealPlanLimit> getMealPlanLimits() {
        return mMealPlanLimits;
    }

    public void setMealPlanLimits(List<MealPlanLimit> mealPlanLimits) {
        mMealPlanLimits = mealPlanLimits;
    }

    public List<List<String>> getMealTypeIdCombinations() {
        return mMealTypeIdCombinations;
    }

    public void setMealTypeIdCombinations(List<List<String>> mealTypeIdCombinations) {
        mMealTypeIdCombinations = mealTypeIdCombinations;
    }

    public List<MealType> getMealTypes() {
        return mMealTypes;
    }

    public void setMealTypes(List<MealType> mealTypes) {
        mMealTypes = mealTypes;
    }

}
