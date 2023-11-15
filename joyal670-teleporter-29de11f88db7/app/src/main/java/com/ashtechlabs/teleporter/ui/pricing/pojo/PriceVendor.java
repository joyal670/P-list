package com.ashtechlabs.teleporter.ui.pricing.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

 public class PriceVendor implements Parcelable {

    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("VendorID")
    @Expose
    private String VendorID;
    @SerializedName("ServiceType")
    @Expose
    private String ServiceType;
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
    @SerializedName("transport")
    @Expose
    private String Transport;
    @SerializedName("Hazardous")
    @Expose
    private String Hazardous;
    @SerializedName("AddInsurance")
    @Expose
    private String AddInsurance;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("additional_info")
    @Expose
    private String additionalInfo;
    @SerializedName("perishable_det")
    @Expose
    private String perishableDet;
    @SerializedName("insurance_min_amt")
    @Expose
    private String insuranceMinAmt;
    @SerializedName("rate_validity")
    @Expose
    private String rateValidity;
    @SerializedName("ratecard_expiry_status")
    @Expose
    private String ratecardExpiryStatus;
    @SerializedName("RateCardID")
    @Expose
    private String rateCardID;
//    @SerializedName("shipment")
//    @Expose
//    private int shipment;


    protected PriceVendor(Parcel in) {
        id = in.readString();
        VendorID = in.readString();
        ServiceType = in.readString();
        FromLocation = in.readString();
        ToLocation = in.readString();
        MinAmount = in.readString();
        PerKGAmount = in.readString();
        Duration = in.readString();
        Transport = in.readString();
        Hazardous = in.readString();
        AddInsurance = in.readString();
        currency = in.readString();
        additionalInfo = in.readString();
        perishableDet = in.readString();
        insuranceMinAmt = in.readString();
        rateValidity = in.readString();
        ratecardExpiryStatus = in.readString();
        rateCardID = in.readString();
        //shipment = in.readInt();
    }

    public static final Creator<PriceVendor> CREATOR = new Creator<PriceVendor>() {
        @Override
        public PriceVendor createFromParcel(Parcel in) {
            return new PriceVendor(in);
        }

        @Override
        public PriceVendor[] newArray(int size) {
            return new PriceVendor[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getVendorID() {
        return VendorID;
    }

    public String getServiceType() {
        return ServiceType;
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

    public String getTransport() {
        return Transport;
    }

    public String getHazardous() {
        return Hazardous;
    }

    public String getAddInsurance() {
        return AddInsurance;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getPerishableDet() {
        return perishableDet;
    }

    public String getInsuranceMinAmt() {
        return insuranceMinAmt;
    }

    public String getRateValidity() {
        return rateValidity;
    }

    public String getRatecardExpiryStatus() {
        return ratecardExpiryStatus;
    }

    public String getRateCardID() {
        return rateCardID;
    }

//    public int getShipment() {
//        return shipment;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(VendorID);
        dest.writeString(ServiceType);
        dest.writeString(FromLocation);
        dest.writeString(ToLocation);
        dest.writeString(MinAmount);
        dest.writeString(PerKGAmount);
        dest.writeString(Duration);
        dest.writeString(Transport);
        dest.writeString(Hazardous);
        dest.writeString(AddInsurance);
        dest.writeString(currency);
        dest.writeString(additionalInfo);
        dest.writeString(perishableDet);
        dest.writeString(insuranceMinAmt);
        dest.writeString(rateValidity);
        dest.writeString(ratecardExpiryStatus);
        dest.writeString(rateCardID);
        //dest.writeInt(shipment);
    }
}