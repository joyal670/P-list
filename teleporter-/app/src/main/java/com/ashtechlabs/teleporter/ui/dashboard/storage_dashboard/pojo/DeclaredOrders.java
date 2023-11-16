package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeclaredOrders {

    //Declared orders CommonServices
    @SerializedName("ID")
    private String id;
    @SerializedName("CustomerName")
    private String customerNmae;
    @SerializedName("MobileNumber")
    private String mobileNumber;
    @SerializedName("Token")
    private String token;
    @SerializedName("Weight")
    private String weight;
    @SerializedName("Date")
    private String date;
    @SerializedName("DeviceToken")
    private String deviceToken;
    private int serviceType;

    //TruckingService
    @SerializedName("ItemType")
    private int itemType;

    //TruckingMpService
    @SerializedName("VehicleType")
    private String vehicleType;
    @SerializedName("SubVehicleType")
    private String subVehicleType;

    //FreightForwardService
    @SerializedName("Hazardous")
    private int hazardous;
    @SerializedName("AddInsurance")
    private int addInsurance;

    //WarehousingService
    @SerializedName("Location")
    private String location;
    @SerializedName("FromDate")
    private String fromDate;
    @SerializedName("ToDate")
    private String toDate;

    //FreightForwardService & WarehousingService
    @SerializedName("CommodityType")
    private int commodityType;
    @SerializedName("CBM")
    private String cbm;
    @SerializedName("service_mode")
    private int serviceMode;
    @SerializedName("additional_info")
    private int additionalInfo;
    @SerializedName("perishable_det")
    private int perishableData;
    @SerializedName("shipment")
    private int shipment;


    //TruckingService & TruckingMpService & FreightForwardService
    @SerializedName("FromLocation")
    private String fromLocation;
    @SerializedName("ToLocation")
    private String toLocation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerNmae() {
        return customerNmae;
    }

    public void setCustomerNmae(String customerNmae) {
        this.customerNmae = customerNmae;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getHazardous() {
        return hazardous;
    }

    public void setHazardous(int hazardous) {
        this.hazardous = hazardous;
    }

    public int getAddInsurance() {
        return addInsurance;
    }

    public void setAddInsurance(int addInsurance) {
        this.addInsurance = addInsurance;
    }

    public int getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(int commodityType) {
        this.commodityType = commodityType;
    }

    public String getCbm() {
        return cbm;
    }

    public void setCbm(String cbm) {
        this.cbm = cbm;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(int serviceMode) {
        this.serviceMode = serviceMode;
    }

    public int getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(int additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public int getPerishableData() {
        return perishableData;
    }

    public void setPerishableData(int perishableData) {
        this.perishableData = perishableData;
    }

    public int getShipment() {
        return shipment;
    }

    public void setShipment(int shipment) {
        this.shipment = shipment;
    }

    public String getSubVehicleType() {
        return subVehicleType;
    }

    public void setSubVehicleType(String subVehicleType) {
        this.subVehicleType = subVehicleType;
    }
}
