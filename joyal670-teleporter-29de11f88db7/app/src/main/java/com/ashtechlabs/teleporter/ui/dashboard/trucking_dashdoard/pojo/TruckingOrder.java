package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 18-Jan-17.
 */

public class TruckingOrder {

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("mpservice")
    @Expose
    private ArrayList<TruckingMpService> truckingMpServices;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public ArrayList<TruckingMpService> getTruckingMpServices() {
        return this.truckingMpServices;
    }

    public void setTruckingMpServices(ArrayList<TruckingMpService> truckingMpServices) {
        this.truckingMpServices = truckingMpServices;
    }

    public String getMessage() {
        return message;
    }
}
