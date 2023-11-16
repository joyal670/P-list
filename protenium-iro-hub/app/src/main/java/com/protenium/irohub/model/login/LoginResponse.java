
package com.protenium.irohub.model.login;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LoginResponse {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("plan_active_status")
    private Boolean mPlanActiveStatus;
    @SerializedName("status")
    private Boolean mStatus;
    @SerializedName("user")
    private User mUser;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Boolean getPlanActiveStatus() {
        return mPlanActiveStatus;
    }

    public void setPlanActiveStatus(Boolean planActiveStatus) {
        mPlanActiveStatus = planActiveStatus;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

}
