package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 24-Jan-17.
 */

public class VendorList implements Parcelable {

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
    @SerializedName("CommodityType")
    @Expose
    private String CommodityType;
    @SerializedName("FromLocation")
    @Expose
    private String FromLocation;
    @SerializedName("ToLocation")
    @Expose
    private String ToLocation;
    @SerializedName("Date")
    @Expose
    private String Date;
    @SerializedName("commodity_value")
    @Expose
    private double commodity_value;
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
    @SerializedName("AddInsurance")
    @Expose
    private int AddInsurance;
    @SerializedName("insuranceAmount")
    @Expose
    private String insuranceAmount;
    @SerializedName("order_number")
    @Expose
    private String order_number;
    @SerializedName("transport")
    @Expose
    private String transport;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("currency")
    @Expose
    private int currency;
    @SerializedName("Duration")
    @Expose
    private String Duration;
    @SerializedName("order_status")
    @Expose
    private String order_status;
    @SerializedName("additional_info")
    @Expose
    private String additionalInfo;
    @SerializedName("perishable_det")
    @Expose
    private String perishableData;
    @SerializedName("shipment")
    @Expose
    private int shipment;
    @SerializedName("CBM")
    @Expose
    private double volume;
    @SerializedName("volume_unit")
    @Expose
    private String volumeUnit;
    @SerializedName("load_details")
    @Expose
    private ArrayList<CargoLoad> cargoLoads;


    protected VendorList(Parcel in) {
        id = in.readString();
        ordertype = in.readString();
        orderid = in.readString();
        ratecard = in.readString();
        state = in.readString();
        CommodityType = in.readString();
        FromLocation = in.readString();
        ToLocation = in.readString();
        Date = in.readString();
        commodity_value = in.readDouble();
        commodity_description = in.readString();
        payment = in.readString();
        Weight = in.readDouble();
        weight_unit = in.readString();
        AddInsurance = in.readInt();
        insuranceAmount = in.readString();
        order_number = in.readString();
        transport = in.readString();
        price = in.readString();
        currency = in.readInt();
        Duration = in.readString();
        order_status = in.readString();
        additionalInfo = in.readString();
        perishableData = in.readString();
        shipment = in.readInt();
        volume = in.readDouble();
        volumeUnit = in.readString();
        cargoLoads = in.createTypedArrayList(CargoLoad.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ordertype);
        dest.writeString(orderid);
        dest.writeString(ratecard);
        dest.writeString(state);
        dest.writeString(CommodityType);
        dest.writeString(FromLocation);
        dest.writeString(ToLocation);
        dest.writeString(Date);
        dest.writeDouble(commodity_value);
        dest.writeString(commodity_description);
        dest.writeString(payment);
        dest.writeDouble(Weight);
        dest.writeString(weight_unit);
        dest.writeInt(AddInsurance);
        dest.writeString(insuranceAmount);
        dest.writeString(order_number);
        dest.writeString(transport);
        dest.writeString(price);
        dest.writeInt(currency);
        dest.writeString(Duration);
        dest.writeString(order_status);
        dest.writeString(additionalInfo);
        dest.writeString(perishableData);
        dest.writeInt(shipment);
        dest.writeDouble(volume);
        dest.writeString(volumeUnit);
        dest.writeTypedList(cargoLoads);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VendorList> CREATOR = new Creator<VendorList>() {
        @Override
        public VendorList createFromParcel(Parcel in) {
            return new VendorList(in);
        }

        @Override
        public VendorList[] newArray(int size) {
            return new VendorList[size];
        }
    };

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

    public String getCommodityType() {
        return CommodityType;
    }

    public String getFromLocation() {
        return FromLocation;
    }

    public String getToLocation() {
        return ToLocation;
    }

    public String getDate() {
        return Date;
    }

    public double getCommodity_value() {
        return commodity_value;
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

    public int getAddInsurance() {
        return AddInsurance;
    }

    public String getOrder_number() {
        return order_number;
    }

    public String getTransport() {
        return transport;
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

    public String getOrder_status() {
        return order_status;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getInsuranceAmount() {
        return insuranceAmount;
    }

    public String getPerishableData() {
        return perishableData;
    }

    public int getShipment() {
        return shipment;
    }

    public double getVolume() {
        return volume;
    }

    public String getVolumeUnit() {
        return volumeUnit;
    }

    public ArrayList<CargoLoad> getCargoLoads() {
        return cargoLoads;
    }

    public void setState(String state) {
        this.state = state;
    }
}
