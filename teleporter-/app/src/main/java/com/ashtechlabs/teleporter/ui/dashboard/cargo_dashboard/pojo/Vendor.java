package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 24-Jan-17.
 */

public class Vendor {

    @SerializedName("code")
    @Expose
    private String code;

//    @SerializedName("message")
//    @Expose
//    private String message;

    @SerializedName("0")
    @Expose
    private ArrayList<VendorList> vendors;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<VendorList> getVendorsService() {
        return vendors;
    }

    public void setVendorsService(ArrayList<VendorList> partner) {
        vendors = partner;
    }

//    public String getMessage() {
//        return message;
//    }
}