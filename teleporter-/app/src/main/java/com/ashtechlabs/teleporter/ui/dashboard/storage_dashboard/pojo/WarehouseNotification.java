package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class WarehouseNotification {

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("value")
    @Expose
    private ArrayList<WarehouseNotificationList> reviews;


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<WarehouseNotificationList> getReviewService() {
        return reviews;
    }

}