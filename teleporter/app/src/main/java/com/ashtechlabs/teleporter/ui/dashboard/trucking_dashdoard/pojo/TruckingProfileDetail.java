package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 19-Jan-17.
 */

public class TruckingProfileDetail {

    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("CityOfWork")
    @Expose
    private String CityOfWork;
    @SerializedName("CompanyAddress")
    @Expose
    private String CompanyAddress;
    @SerializedName("ZipCode")
    @Expose
    private String ZipCode;
    @SerializedName("ExpireLicenseDate")
    @Expose
    private String ExpireLicenseDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("LicensePlate")
    @Expose
    private String licensePlate;
    @SerializedName("profileimage")
    @Expose
    private String profileImage;
    @SerializedName("ContactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("ContactEmail")
    @Expose
    private String contactEmail;
    @SerializedName("email")
    @Expose
    private String email;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityOfWork() {
        return CityOfWork;
    }

    public void setCityOfWork(String cityOfWork) {
        CityOfWork = cityOfWork;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        CompanyAddress = companyAddress;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getExpireLicenseDate() {
        return ExpireLicenseDate;
    }

    public void setExpireLicenseDate(String expireLicenseDate) {
        ExpireLicenseDate = expireLicenseDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getContactEmail() {
        return contactEmail;
    }
}
