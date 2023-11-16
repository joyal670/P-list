package com.ashtechlabs.teleporter.ui.pricing.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 31-Jan-17.
 */

public class PriceCourier implements Parcelable{
    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("DriverID")
    @Expose
    private String DriverID;
    @SerializedName("insurance_min_amt")
    @Expose
    private int insuranceMinAmt;
    @SerializedName("ins_percent")
    @Expose
    private String insPercent;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("DeliveryType")
    @Expose
    private String DeliveryType;

    @SerializedName("subVehicleType")
    @Expose
    private String subVehicleType;
    @SerializedName("rate_validity")
    @Expose
    private String rateValidity;
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
    @SerializedName("RateCardID")
    @Expose
    private String rateCardID;
    @SerializedName("ratecard_expiry_status")
    @Expose
    private String ratecardExpiryStatus;


    protected PriceCourier(Parcel in) {
        id = in.readString();
        DriverID = in.readString();
        insuranceMinAmt = in.readInt();
        insPercent = in.readString();
        vehicleType = in.readString();
        DeliveryType = in.readString();
        subVehicleType = in.readString();
        rateValidity = in.readString();
        FromLocation = in.readString();
        ToLocation = in.readString();
        MinAmount = in.readString();
        PerKGAmount = in.readString();
        Duration = in.readString();
        currency = in.readInt();
        rateCardID = in.readString();
        ratecardExpiryStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(DriverID);
        dest.writeInt(insuranceMinAmt);
        dest.writeString(insPercent);
        dest.writeString(vehicleType);
        dest.writeString(DeliveryType);
        dest.writeString(subVehicleType);
        dest.writeString(rateValidity);
        dest.writeString(FromLocation);
        dest.writeString(ToLocation);
        dest.writeString(MinAmount);
        dest.writeString(PerKGAmount);
        dest.writeString(Duration);
        dest.writeInt(currency);
        dest.writeString(rateCardID);
        dest.writeString(ratecardExpiryStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PriceCourier> CREATOR = new Creator<PriceCourier>() {
        @Override
        public PriceCourier createFromParcel(Parcel in) {
            return new PriceCourier(in);
        }

        @Override
        public PriceCourier[] newArray(int size) {
            return new PriceCourier[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getDriverID() {
        return DriverID;
    }

    public int getInsuranceMinAmt() {
        return insuranceMinAmt;
    }

    public String getInsPercent() {
        return insPercent;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getDeliveryType() {
        return DeliveryType;
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

    public int getCurrency() {
        return currency;
    }

    public String getRateCardID() {
        return rateCardID;
    }

    public String getRatecardExpiryStatus() {
        return ratecardExpiryStatus;
    }
}