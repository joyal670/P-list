package com.ashtechlabs.teleporter.ui.pricing.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 31-Jan-17.
 */

public class PriceTrucking implements Parcelable {

    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("DriverID")
    @Expose
    private String DriverID;
    @SerializedName("VehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("subVehicleType")
    @Expose
    private String subVehicleType;
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
    @SerializedName("insurance_min_amt")
    @Expose
    private String insuranceMinAmt;
    @SerializedName("insurance_percent")
    @Expose
    private String insurancePercent;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("rate_validity")
    @Expose
    private String rateValidity;
    @SerializedName("ratecard_expiry_status")
    @Expose
    private String ratecard_expiry_status;
    @SerializedName("RateCardID")
    @Expose
    private String RateCardID;
    @SerializedName("trucking_veh_det_id")
    @Expose
    private String truckId;

    protected PriceTrucking(Parcel in) {
        id = in.readString();
        DriverID = in.readString();
        insuranceMinAmt = in.readString();
        insurancePercent = in.readString();
        vehicleType = in.readString();
        subVehicleType = in.readString();
        rateValidity = in.readString();
        FromLocation = in.readString();
        ToLocation = in.readString();
        MinAmount = in.readString();
        PerKGAmount = in.readString();
        Duration = in.readString();
        currency = in.readString();
        ratecard_expiry_status = in.readString();
        RateCardID = in.readString();
        truckId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(DriverID);
        dest.writeString(insuranceMinAmt);
        dest.writeString(insurancePercent);
        dest.writeString(vehicleType);
        dest.writeString(subVehicleType);
        dest.writeString(rateValidity);
        dest.writeString(FromLocation);
        dest.writeString(ToLocation);
        dest.writeString(MinAmount);
        dest.writeString(PerKGAmount);
        dest.writeString(Duration);
        dest.writeString(currency);
        dest.writeString(ratecard_expiry_status);
        dest.writeString(RateCardID);
        dest.writeString(truckId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PriceTrucking> CREATOR = new Creator<PriceTrucking>() {
        @Override
        public PriceTrucking createFromParcel(Parcel in) {
            return new PriceTrucking(in);
        }

        @Override
        public PriceTrucking[] newArray(int size) {
            return new PriceTrucking[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getDriverID() {
        return DriverID;
    }

    public String getInsuranceMinAmt() {
        return insuranceMinAmt;
    }

    public String getInsPercent() {
        return insurancePercent;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getSubVehicleType() {
        return subVehicleType;
    }

    public String getRateValidity() {
        return rateValidity;
    }

    public String getFromLocation() {
        return FromLocation;
    }

    public String getToLocation() {
        return ToLocation;
    }

    public String getMinAmount() {
        return MinAmount;
    }

    public String getPerKGAmount() {
        return PerKGAmount;
    }

    public String getDuration() {
        return Duration;
    }

    public String getCurrency() {
        return currency;
    }

    public String getRatecard_expiry_status() {
        return ratecard_expiry_status;
    }

    public String getRateCardID() {
        return RateCardID;
    }

    public String getInsurancePercent() {
        return insurancePercent;
    }

    public String getTruckId() {
        return truckId;
    }
}