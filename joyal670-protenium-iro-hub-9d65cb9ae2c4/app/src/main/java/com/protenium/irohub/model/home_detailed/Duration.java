package com.protenium.irohub.model.home_detailed;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Duration {

    @SerializedName("duration")
    private String mDuration;
    @SerializedName("enable_modification")
    private String mEnableModification;
    @SerializedName("non_stop_price")
    private String mNonStopPrice;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("suspend")
    private String mSuspend;
    @SerializedName("isCheck")
    private Boolean isChecked;

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public String getEnableModification() {
        return mEnableModification;
    }

    public void setEnableModification(String enableModification) {
        mEnableModification = enableModification;
    }

    public String getNonStopPrice() {
        return mNonStopPrice;
    }

    public void setNonStopPrice(String nonStopPrice) {
        mNonStopPrice = nonStopPrice;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getSuspend() {
        return mSuspend;
    }

    public void setSuspend(String suspend) {
        mSuspend = suspend;
    }

    public void setIsChecked(boolean isCheck) {
        isChecked = isCheck;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

}
