package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 18-Jan-17.
 */

public class DriverOrder {

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("deliveryservice")
    @Expose
    private ArrayList<DeliveryService> deliveryService;


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<DeliveryService> getDeliveryService() {
        return this.deliveryService;
    }

    public void setDeliveryService(ArrayList<DeliveryService> deliveryService) {
        this.deliveryService = deliveryService;
    }



    public String getMessage() {
        return message;
    }
}
