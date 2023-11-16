package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 08-Mar-17.
 */

public class TruckingPendingJob {
    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("deliveryservice")
    @Expose
    private ArrayList<TruckingService> truckingService;


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<TruckingService> getTruckingService() {
        return this.truckingService;
    }

    public void setTruckingService(ArrayList<TruckingService> truckingService) {
        this.truckingService = truckingService;
    }

}