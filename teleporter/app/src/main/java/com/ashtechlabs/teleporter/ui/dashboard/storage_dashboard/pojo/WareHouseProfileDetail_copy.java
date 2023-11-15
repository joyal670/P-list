package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 19-Jan-17.
 */

public class WareHouseProfileDetail_copy {
    @SerializedName("ID")
    @Expose
    private String ID;
    @SerializedName("StorageType")
    @Expose
    private String StorageType;
    @SerializedName("PicOfWarehouse")
    @Expose
    private String PicOfWarehouse;
    @SerializedName("CompanyName")
    @Expose
    private String CompanyName;
    @SerializedName("RegistratedAddress")
    @Expose
    private String RegistratedAddress;
    @SerializedName("ContactNumber")
    @Expose
    private String ContactNumber;
    @SerializedName("TradeLicenseNumber")
    @Expose
    private String TradeLicenseNumber;
    @SerializedName("InsuranceNumber")
    @Expose
    private String InsuranceNumber;
    @SerializedName("ExpireInsuranceDate")
    @Expose
    private String ExpireInsuranceDate;
    @SerializedName("RegistrationID")
    @Expose
    private String RegistrationID;
    @SerializedName("email")
    @Expose
    private String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStorageType() {
        return StorageType;
    }

    public void setStorageType(String storageType) {
        StorageType = storageType;
    }

    public String getPicOfWarehouse() {
        return PicOfWarehouse;
    }

    public void setPicOfWarehouse(String picOfWarehouse) {
        PicOfWarehouse = picOfWarehouse;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getRegistratedAddress() {
        return RegistratedAddress;
    }

    public void setRegistratedAddress(String registratedAddress) {
        RegistratedAddress = registratedAddress;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getTradeLicenseNumber() {
        return TradeLicenseNumber;
    }

    public void setTradeLicenseNumber(String tradeLicenseNumber) {
        TradeLicenseNumber = tradeLicenseNumber;
    }

    public String getInsuranceNumber() {
        return InsuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        InsuranceNumber = insuranceNumber;
    }

    public String getExpireInsuranceDate() {
        return ExpireInsuranceDate;
    }

    public void setExpireInsuranceDate(String expireInsuranceDate) {
        ExpireInsuranceDate = expireInsuranceDate;
    }

    public String getRegistrationID() {
        return RegistrationID;
    }

    public void setRegistrationID(String registrationID) {
        RegistrationID = registrationID;
    }
}
