package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 18-Jan-17.
 */

public class VendorProfile {

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("message")
    @Expose
    private ArrayList<VendorProfileDetail> profile;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<VendorProfileDetail> getProfile() {
        return profile;
    }

    public void setProfile(ArrayList<VendorProfileDetail> profile) {
        this.profile = profile;
    }
}
