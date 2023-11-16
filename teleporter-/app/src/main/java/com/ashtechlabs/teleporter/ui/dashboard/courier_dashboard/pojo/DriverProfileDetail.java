package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 19-Jan-17.
 */

public class DriverProfileDetail {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("VehicleType")
    @Expose
    private String VehicleType;
    @SerializedName("CityOfWork")
    @Expose
    private String CityOfWork;
    @SerializedName("CompanyAddress")
    @Expose
    private String CompanyAddress;
    @SerializedName("ZipCode")
    @Expose
    private String ZipCode;
    @SerializedName("LicensePlate")
    @Expose
    private String LicensePlate;
    @SerializedName("ExpireLicenseDate")
    @Expose
    private String ExpireLicenseDate;
    @SerializedName("VehicleNumber")
    @Expose
    private String VehicleNumber;
    @SerializedName("VehicleInsurance")
    @Expose
    private String VehicleInsurance;
    @SerializedName("ExpireInsuranceDate")
    @Expose
    private String ExpireInsuranceDate;
    @SerializedName("RegistrationID")
    @Expose
    private String RegistrationID;
    @SerializedName("profileimage")
    @Expose
    private String profileimage;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("ContactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("ContactEmail")
    @Expose
    private String contactEmail;
    @SerializedName("MobileNum")
    @Expose
    private String MobileNum;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
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

    public String getLicensePlate() {
        return LicensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        LicensePlate = licensePlate;
    }

    public String getExpireLicenseDate() {
        return ExpireLicenseDate;
    }

    public void setExpireLicenseDate(String expireLicenseDate) {
        ExpireLicenseDate = expireLicenseDate;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getVehicleInsurance() {
        return VehicleInsurance;
    }

    public void setVehicleInsurance(String vehicleInsurance) {
        VehicleInsurance = vehicleInsurance;
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

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getMobileNum() {
        return MobileNum;
    }
}
