
package com.protenium.irohub.model.home_detailed;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Data {

    @SerializedName("carbs")
    private List<Carb> mCarbs;
    @SerializedName("current_subscription_id")
    private int mCurrentSubscriptionId;
    @SerializedName("extras")
    private List<Extra> mExtras;
    @SerializedName("meal_plan")
    private MealPlan mMealPlan;
    @SerializedName("non_stop_check_day")
    private String mNonStopCheckDay;
    @SerializedName("plan_start_buffer")
    private int mPlanStartBuffer;
    @SerializedName("proteins")
    private List<Protein> mProteins;
    @SerializedName("start_date_range")
    private int mStartDateRange;

    public List<Carb> getCarbs() {
        return mCarbs;
    }

    public void setCarbs(List<Carb> carbs) {
        mCarbs = carbs;
    }

    public int getCurrentSubscriptionId() {
        return mCurrentSubscriptionId;
    }

    public void setCurrentSubscriptionId(int currentSubscriptionId) {
        mCurrentSubscriptionId = currentSubscriptionId;
    }

    public List<Extra> getExtras() {
        return mExtras;
    }

    public void setExtras(List<Extra> extras) {
        mExtras = extras;
    }

    public MealPlan getMealPlan() {
        return mMealPlan;
    }

    public void setMealPlan(MealPlan mealPlan) {
        mMealPlan = mealPlan;
    }

    public String getNonStopCheckDay() {
        return mNonStopCheckDay;
    }

    public void setNonStopCheckDay(String nonStopCheckDay) {
        mNonStopCheckDay = nonStopCheckDay;
    }

    public int getPlanStartBuffer() {
        return mPlanStartBuffer;
    }

    public void setPlanStartBuffer(int planStartBuffer) {
        mPlanStartBuffer = planStartBuffer;
    }

    public List<Protein> getProteins() {
        return mProteins;
    }

    public void setProteins(List<Protein> proteins) {
        mProteins = proteins;
    }

    public int getStartDateRange() {
        return mStartDateRange;
    }

    public void setStartDateRange(int startDateRange) {
        mStartDateRange = startDateRange;
    }

}
