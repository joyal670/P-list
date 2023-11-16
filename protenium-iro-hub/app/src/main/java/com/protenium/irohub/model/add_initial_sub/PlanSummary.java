
package com.protenium.irohub.model.add_initial_sub;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanSummary {

    @SerializedName("carbs")
    private Carbs mCarbs;
    @SerializedName("duration")
    private Duration mDuration;
    @SerializedName("extras")
    private List<Object> mExtras;
    @SerializedName("non_stop")
    private NonStop mNonStop;
    @SerializedName("plan")
    private Plan mPlan;
    @SerializedName("protein")
    private Protein mProtein;
    @SerializedName("total")
    private Total mTotal;

    public Carbs getCarbs() {
        return mCarbs;
    }

    public void setCarbs(Carbs carbs) {
        mCarbs = carbs;
    }

    public Duration getDuration() {
        return mDuration;
    }

    public void setDuration(Duration duration) {
        mDuration = duration;
    }

    public List<Object> getExtras() {
        return mExtras;
    }

    public void setExtras(List<Object> extras) {
        mExtras = extras;
    }

    public NonStop getNonStop() {
        return mNonStop;
    }

    public void setNonStop(NonStop nonStop) {
        mNonStop = nonStop;
    }

    public Plan getPlan() {
        return mPlan;
    }

    public void setPlan(Plan plan) {
        mPlan = plan;
    }

    public Protein getProtein() {
        return mProtein;
    }

    public void setProtein(Protein protein) {
        mProtein = protein;
    }

    public Total getTotal() {
        return mTotal;
    }

    public void setTotal(Total total) {
        mTotal = total;
    }

}
