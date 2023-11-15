package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class TruckingNotifications {

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("value")
    @Expose
    private ArrayList<TruckingNotificationList> reviews;


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<TruckingNotificationList> getReviewService() {
        return reviews;
    }
}