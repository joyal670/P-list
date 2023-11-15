package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.vehicle_type.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VIDHU on 12/19/2017.
 */

public class Vehicles {

    @SerializedName("trucking_veh_det_id")
    @Expose
    String deleteId;
    @SerializedName("TruckingProfileId")
    @Expose
    String profileId;
    @SerializedName("VehicleNumber")
    @Expose
    String vehicleNumber;
    @SerializedName("VehicleType")
    @Expose
    String vehicleType;
    @SerializedName("VehicleSubType")
    @Expose
    String vehicleSubType;
    @SerializedName("VehicleInsurance")
    @Expose
    String vehicleInsurance;
    @SerializedName("ExpireInsuranceDate")
    @Expose
    String expireInsuranceDate;

    public String getDeleteId() {
        return deleteId;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleSubType() {
        return vehicleSubType;
    }

    public String getVehicleInsurance() {
        return vehicleInsurance;
    }

    public String getExpireInsuranceDate() {
        return expireInsuranceDate;
    }
}
