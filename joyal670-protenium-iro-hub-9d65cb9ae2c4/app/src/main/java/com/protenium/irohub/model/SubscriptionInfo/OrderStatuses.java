package com.protenium.irohub.model.SubscriptionInfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class OrderStatuses {

    @SerializedName("admin_suspended")
    private List<Pending> mAdminSuspended;
    @SerializedName("canceled")
    private List<Pending> mCanceled;
    @SerializedName("completed")
    private List<Pending> mCompleted;
    @SerializedName("pending")
    private List<Pending> mPending;
    @SerializedName("user_suspended")
    private List<Pending> mUserSuspended;

    public List<Pending> getAdminSuspended() {
        return mAdminSuspended;
    }

    public void setAdminSuspended(List<Pending> adminSuspended) {
        mAdminSuspended = adminSuspended;
    }

    public List<Pending> getCanceled() {
        return mCanceled;
    }

    public void setCanceled(List<Pending> canceled) {
        mCanceled = canceled;
    }

    public List<Pending> getCompleted() {
        return mCompleted;
    }

    public void setCompleted(List<Pending> completed) {
        mCompleted = completed;
    }

    public List<Pending> getPending() {
        return mPending;
    }

    public void setPending(List<Pending> pending) {
        mPending = pending;
    }

    public List<Pending> getUserSuspended() {
        return mUserSuspended;
    }

    public void setUserSuspended(List<Pending> userSuspended) {
        mUserSuspended = userSuspended;
    }

}
