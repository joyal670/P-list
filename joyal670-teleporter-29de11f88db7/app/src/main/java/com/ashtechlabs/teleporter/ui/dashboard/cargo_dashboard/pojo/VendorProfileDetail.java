package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 19-Jan-17.
 */

public class VendorProfileDetail {

    @SerializedName("ID")
    @Expose
    private String ID;
    @SerializedName("VendorType")
    @Expose
    private String VendorType;
    @SerializedName("RegistratedAddress")
    @Expose
    private String RegistratedAddress;
    @SerializedName("CompanyName")
    @Expose
    private String CompanyName;
    @SerializedName("ContactNumber")
    @Expose
    private String ContactNumber;
    @SerializedName("ContactEmail")
    @Expose
    private String ContactEmail;
    @SerializedName("TradeLicenseNumber ")
    @Expose
    private String TradeLicenseNumber;
    @SerializedName("InsuranceNumber")
    @Expose
    private String InsuranceNumber;
    @SerializedName("ExpireInsuranceDate")
    @Expose
    private String ExpireInsuranceDate;
    @SerializedName("ContactDetail")
    @Expose
    private String ContactDetail;
    @SerializedName("POC")
    @Expose
    private String POC;
    @SerializedName("Designation")
    @Expose
    private String Designation;
    @SerializedName("email")
    @Expose
    private String Email;
    @SerializedName("BackAccount")
    @Expose
    private String BackAccount;
    @SerializedName("WebsiteURL")
    @Expose
    private String WebsiteURL;
    @SerializedName("RegistrationID")
    @Expose
    private String RegistrationID;
    @SerializedName("profileimage")
    @Expose
    private String profileimage;
    @SerializedName("MobileNum")
    @Expose
    private String mobileNum;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getVendorType() {
        return VendorType;
    }

    public void setVendorType(String vendorType) {
        VendorType = vendorType;
    }

    public String getRegistratedAddress() {
        return RegistratedAddress;
    }

    public void setRegistratedAddress(String registratedAddress) {
        RegistratedAddress = registratedAddress;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
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

    public String getContactDetail() {
        return ContactDetail;
    }

    public void setContactDetail(String contactDetail) {
        ContactDetail = contactDetail;
    }

    public String getPOC() {
        return POC;
    }

    public void setPOC(String POC) {
        this.POC = POC;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBackAccount() {
        return BackAccount;
    }

    public void setBackAccount(String backAccount) {
        BackAccount = backAccount;
    }

    public String getWebsiteURL() {
        return WebsiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        WebsiteURL = websiteURL;
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

    public String getContactEmail() {
        return ContactEmail;
    }

    public String getMobileNum() {
        return mobileNum;
    }
}
