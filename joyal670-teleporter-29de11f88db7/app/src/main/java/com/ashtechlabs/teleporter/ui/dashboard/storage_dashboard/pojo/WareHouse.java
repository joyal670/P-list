package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 23-Jan-17.
 */

public class WareHouse {

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("0")
    @Expose
    private ArrayList<WareHouseList> wareHouseLises;


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<WareHouseList> getWareHouseService() {
        return wareHouseLises;
    }

    public void setWareHouseService(ArrayList<WareHouseList> wareHouseLis) {
        wareHouseLises = wareHouseLis;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<WareHouseList> getWareHouseLises() {
        return wareHouseLises;
    }
}
