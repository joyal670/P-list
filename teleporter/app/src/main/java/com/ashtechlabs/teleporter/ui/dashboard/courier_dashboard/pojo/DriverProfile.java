package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 18-Jan-17.
 */

public class DriverProfile {

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("message")
    @Expose
    private ArrayList<DriverProfileDetail> profile;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<DriverProfileDetail> getProfile() {
        return profile;
    }

    public void setProfile(ArrayList<DriverProfileDetail> profile) {
        this.profile = profile;
    }
}
