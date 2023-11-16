package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 19-Jan-17.
 */

public class WareHouseProfileDetail {
    @SerializedName("ID")
    @Expose
    private String ID;
    @SerializedName("CompanyName")
    @Expose
    private String CompanyName;
    @SerializedName("RegistratedAddress")
    @Expose
    private String RegistratedAddress;
    @SerializedName("PicOfWarehouse")
    @Expose
    private String picOfWarehouse;
    @SerializedName("ContactNumber")
    @Expose
    private String ContactNumber;
    @SerializedName("MobileNum")
    @Expose
    private String MobileNum;
    @SerializedName("ContactEmail")
    @Expose
    private String ContactEmail;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("RegistrationID")
    @Expose
    private String RegistrationID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getRegistratedAddress() {
        return RegistratedAddress;
    }

    public String getContactNumber() {
        return ContactNumber;
    }


    public String getPicOfWarehouse() {
        return picOfWarehouse;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNum() {
        return MobileNum;
    }

    public String getContactEmail() {
        return ContactEmail;
    }
}
