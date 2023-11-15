package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 08-Mar-17.
 */

public class PendingJob {
    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("data")
    @Expose
    private ArrayList<VendorList> deliveryService;


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<VendorList> getDeliveryService() {
        return this.deliveryService;
    }

    public void setDeliveryService(ArrayList<VendorList> deliveryService) {
        this.deliveryService = deliveryService;
    }

}