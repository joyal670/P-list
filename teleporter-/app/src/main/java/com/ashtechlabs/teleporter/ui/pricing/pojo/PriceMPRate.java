package com.ashtechlabs.teleporter.ui.pricing.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 01-Feb-17.
 */

public class PriceMPRate {
    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("DriverID")
    @Expose
    private String DriverID;
    @SerializedName("VehicleType")
    @Expose
    private String VehicleType;
    @SerializedName("FromLocation")
    @Expose
    private String FromLocation;
    @SerializedName("ToLocation")
    @Expose
    private String ToLocation;
    @SerializedName("MinAmount")
    @Expose
    private String MinAmount;
    @SerializedName("PerKGAmount")
    @Expose
    private String PerKGAmount;
    @SerializedName("Duration")
    @Expose
    private String Duration;

    @SerializedName("currency")
    @Expose
    private int currency;


    public String getVehicleType() {
        return this.VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.VehicleType = vehicleType;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverID() {
        return this.DriverID;
    }

    public void setDriverID(String driverID) {
        this.DriverID = driverID;
    }

    public String getFromLocation() {
        return this.FromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.FromLocation = fromLocation;
    }

    public String getToLocation() {
        return this.ToLocation;
    }

    public void setToLocation(String toLocation) {
        this.ToLocation = toLocation;
    }

    public String getMinAmount() {
        return this.MinAmount;
    }

    public void setMinAmount(String minAmount) {
        this.MinAmount = minAmount;
    }

    public String getPerKGAmount() {
        return this.PerKGAmount;
    }

    public void setPerKGAmount(String perKGAmount) {
        this.PerKGAmount = perKGAmount;
    }

    public String getDuration() {
        return this.Duration;
    }

    public void setDuration(String duration) {
        this.Duration = duration;
    }

    public int getCurrency() {
        return currency;
    }
}