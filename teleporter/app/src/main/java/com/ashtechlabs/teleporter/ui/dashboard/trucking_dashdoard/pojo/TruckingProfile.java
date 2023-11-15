package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 18-Jan-17.
 */

public class TruckingProfile {

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("message")
    @Expose
    private ArrayList<TruckingProfileDetail> profile;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<TruckingProfileDetail> getProfile() {
        return profile;
    }

    public void setProfile(ArrayList<TruckingProfileDetail> profile) {
        this.profile = profile;
    }
}
