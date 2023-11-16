
package com.protenium.irohub.model.add_initial_sub;


import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("meal_plan_subscription_id")
    private Long mMealPlanSubscriptionId;
    @SerializedName("plan_summary")
    private PlanSummary mPlanSummary;
    @SerializedName("renewal")
    private Boolean mRenewal;
    @SerializedName("unique_key")
    private String mUniqueKey;

    public Long getMealPlanSubscriptionId() {
        return mMealPlanSubscriptionId;
    }

    public void setMealPlanSubscriptionId(Long mealPlanSubscriptionId) {
        mMealPlanSubscriptionId = mealPlanSubscriptionId;
    }

    public PlanSummary getPlanSummary() {
        return mPlanSummary;
    }

    public void setPlanSummary(PlanSummary planSummary) {
        mPlanSummary = planSummary;
    }

    public Boolean getRenewal() {
        return mRenewal;
    }

    public void setRenewal(Boolean renewal) {
        mRenewal = renewal;
    }

    public String getUniqueKey() {
        return mUniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        mUniqueKey = uniqueKey;
    }

}
