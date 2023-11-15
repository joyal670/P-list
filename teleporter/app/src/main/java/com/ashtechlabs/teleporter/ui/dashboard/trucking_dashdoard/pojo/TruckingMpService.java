package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 19-Jan-17.
 */

public class TruckingMpService implements Parcelable {


    public static final Creator<TruckingMpService> CREATOR = new Creator<TruckingMpService>() {
        @Override
        public TruckingMpService createFromParcel(Parcel in) {
            return new TruckingMpService(in);
        }

        @Override
        public TruckingMpService[] newArray(int size) {
            return new TruckingMpService[size];
        }
    };
    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("OrderType")
    @Expose
    private String ordertype;
    @SerializedName("OrderID")
    @Expose
    private String orderid;
    @SerializedName("RateCardID")
    @Expose
    private String ratecard;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("VehicleType")
    @Expose
    private String vehicletype;
    @SerializedName("SubVehicleType")
    @Expose
    private String SubVehicleType;
    @SerializedName("FromLocation")
    @Expose
    private String fromlocation;
    @SerializedName("ToLocation")
    @Expose
    private String tolocation;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("commodity_description")
    @Expose
    private String commodity_description;
    @SerializedName("payment")
    @Expose
    private String payment;
    @SerializedName("Weight")
    @Expose
    private double Weight;
    @SerializedName("weight_unit")
    @Expose
    private String weight_unit;
    @SerializedName("order_number")
    @Expose
    private String order_number;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("currency")
    @Expose
    private int currency;
    @SerializedName("Duration")
    @Expose
    private String Duration;
    @SerializedName("AddInsurance")
    @Expose
    private int addInsurance;
    @SerializedName("insuranceAmount")
    @Expose
    private String insuranceAmount;
    @SerializedName("insurance_min_amt")
    @Expose
    private String insuranceMinAmt;
    @SerializedName("insurance_percent")
    @Expose
    private String insurancePercent;
    @SerializedName("CommodityValue")
    @Expose
    private String commodityValue;
    @SerializedName("CommodityType")
    @Expose
    private int commodityType;
    @SerializedName("Volume")
    @Expose
    private double volume;
    @SerializedName("volume_unit")
    @Expose
    private String volumeUnit;
    @SerializedName("load_details")
    @Expose
    private ArrayList<TruckingLoad> truckingLoads;

    protected TruckingMpService(Parcel in) {
        id = in.readString();
        ordertype = in.readString();
        orderid = in.readString();
        ratecard = in.readString();
        state = in.readString();
        vehicletype = in.readString();
        SubVehicleType = in.readString();
        fromlocation = in.readString();
        tolocation = in.readString();
        date = in.readString();
        commodity_description = in.readString();
        payment = in.readString();
        Weight = in.readDouble();
        weight_unit = in.readString();
        order_number = in.readString();
        price = in.readString();
        currency = in.readInt();
        Duration = in.readString();
        addInsurance = in.readInt();
        insuranceAmount = in.readString();
        insuranceMinAmt = in.readString();
        insurancePercent = in.readString();
        commodityValue = in.readString();
        commodityType = in.readInt();
        volume = in.readDouble();
        volumeUnit = in.readString();
        truckingLoads = in.createTypedArrayList(TruckingLoad.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ordertype);
        dest.writeString(orderid);
        dest.writeString(ratecard);
        dest.writeString(state);
        dest.writeString(vehicletype);
        dest.writeString(SubVehicleType);
        dest.writeString(fromlocation);
        dest.writeString(tolocation);
        dest.writeString(date);
        dest.writeString(commodity_description);
        dest.writeString(payment);
        dest.writeDouble(Weight);
        dest.writeString(weight_unit);
        dest.writeString(order_number);
        dest.writeString(price);
        dest.writeInt(currency);
        dest.writeString(Duration);
        dest.writeInt(addInsurance);
        dest.writeString(insuranceAmount);
        dest.writeString(insuranceMinAmt);
        dest.writeString(insurancePercent);
        dest.writeString(commodityValue);
        dest.writeInt(commodityType);
        dest.writeDouble(volume);
        dest.writeString(volumeUnit);
        dest.writeTypedList(truckingLoads);
    }

    public String getId() {
        return id;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getRatecard() {
        return ratecard;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public String getSubVehicleType() {

        if (TextUtils.isEmpty(SubVehicleType)) {
            return "-1";
        }
        return SubVehicleType;
    }

    public String getFromlocation() {
        return fromlocation;
    }

    public String getTolocation() {
        return tolocation;
    }

    public String getDate() {
        return date;
    }

    public String getCommodity_description() {
        return commodity_description;
    }

    public String getPayment() {
        return payment;
    }

    public double getWeight() {
        return Weight;
    }

    public String getWeight_unit() {
        return weight_unit;
    }

    public String getOrder_number() {
        return order_number;
    }

    public String getPrice() {
        return price;
    }

    public int getCurrency() {
        return currency;
    }

    public String getDuration() {
        return Duration;
    }

    public int getAddInsurance() {
        return addInsurance;
    }

    public String getInsuranceAmount() {
        return insuranceAmount;
    }

    public String getInsuranceMinAmt() {
        return insuranceMinAmt;
    }

    public String getInsurancePercent() {
        return insurancePercent;
    }

    public String getCommodityValue() {
        return commodityValue;
    }

    public double getVolume() {
        return volume;
    }

    public String getVolumeUnit() {
        return volumeUnit;
    }

    public int getCommodityType() {
        return commodityType;
    }

    public ArrayList<TruckingLoad> getTruckingLoads() {
        return truckingLoads;
    }
}
