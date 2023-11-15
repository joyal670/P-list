
package com.protenium.irohub.model.dislike.getdislike;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Data {

    @SerializedName("dislike_duration")
    private Long mDislikeDuration;
    @SerializedName("plan_active_status")
    private Boolean mPlanActiveStatus;
    @SerializedName("tags")
    private List<Tag> mTags;

    public Long getDislikeDuration() {
        return mDislikeDuration;
    }

    public void setDislikeDuration(Long dislikeDuration) {
        mDislikeDuration = dislikeDuration;
    }

    public Boolean getPlanActiveStatus() {
        return mPlanActiveStatus;
    }

    public void setPlanActiveStatus(Boolean planActiveStatus) {
        mPlanActiveStatus = planActiveStatus;
    }

    public List<Tag> getTags() {
        return mTags;
    }

    public void setTags(List<Tag> tags) {
        mTags = tags;
    }

}
