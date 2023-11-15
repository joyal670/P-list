package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 18-Jan-17.
 */

public class WareHouseProfile {

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("message")
    @Expose
    private ArrayList<WareHouseProfileDetail> profile;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<WareHouseProfileDetail> getProfile() {
        return profile;
    }

    public void setProfile(ArrayList<WareHouseProfileDetail> profile) {
        this.profile = profile;
    }
}
