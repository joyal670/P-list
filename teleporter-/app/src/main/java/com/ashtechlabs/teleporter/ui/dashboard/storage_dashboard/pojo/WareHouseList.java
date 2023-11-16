package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 23-Jan-17.
 */

public class WareHouseList implements Parcelable{
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
    private int CommodityType;
    @SerializedName("Location")
    @Expose
    private String Location;
    @SerializedName("FromDate")
    @Expose
    private String FromDate;
    @SerializedName("ToDate")
    @Expose
    private String ToDate;
    @SerializedName("Date")
    @Expose
    private String Date;
    @SerializedName("CBM")
    @Expose
    private String CBM;
    @SerializedName("TotalCBMAvailable")
    @Expose
    private String TotalCBMAvailable;
    @SerializedName("commodity_value")
    @Expose
    private double commodity_value;
    @SerializedName("currency")
    @Expose
    private int currency;
    @SerializedName("type_of_good")
    @Expose
    private int type_of_good;
    @SerializedName("commodity_description")
    @Expose
    private String commodity_description;
    @SerializedName("payment")
    @Expose
    private int payment;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("order_number")
    @Expose
    private String order_number;
    @SerializedName("additional_info")
    @Expose
    private String additionalInfo;
    @SerializedName("perishable_det")
    @Expose
    private String perishableData;
    @SerializedName("AddInsurance")
    @Expose
    private int AddInsurance;
    @SerializedName("insuranceAmount")
    @Expose
    private String insuranceAmount;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getRatecard() {
        return ratecard;
    }

    public void setRatecard(String ratecard) {
        this.ratecard = ratecard;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getCommodityType() {
        return CommodityType;
    }

    public void setCommodityType(int commodityType) {
        CommodityType = commodityType;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCBM() {
        return CBM;
    }

    public void setCBM(String CBM) {
        this.CBM = CBM;
    }

    public String getTotalCBMAvailable() {
        return TotalCBMAvailable;
    }

    public void setTotalCBMAvailable(String totalCBMAvailable) {
        TotalCBMAvailable = totalCBMAvailable;
    }

    public double getCommodity_value() {
        return commodity_value;
    }

    public void setCommodity_value(double commodity_value) {
        this.commodity_value = commodity_value;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getType_of_good() {
        return type_of_good;
    }

    public void setType_of_good(int type_of_good) {
        this.type_of_good = type_of_good;
    }

    public String getCommodity_description() {
        return commodity_description;
    }

    public void setCommodity_description(String commodity_description) {
        this.commodity_description = commodity_description;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getPerishableData() {
        return perishableData;
    }

    public int getAddInsurance() {
        return AddInsurance;
    }

    public String getInsuranceAmount() {
        return insuranceAmount;
    }

    protected WareHouseList(Parcel in) {
        id = in.readString();
        ordertype = in.readString();
        orderid = in.readString();
        ratecard = in.readString();
        state = in.readString();
        CommodityType = in.readInt();
        Location = in.readString();
        FromDate = in.readString();
        ToDate = in.readString();
        Date = in.readString();
        CBM = in.readString();
        TotalCBMAvailable = in.readString();
        commodity_value = in.readDouble();
        currency = in.readInt();
        type_of_good = in.readInt();
        commodity_description = in.readString();
        payment = in.readInt();
        price = in.readString();
        order_number = in.readString();
        additionalInfo = in.readString();
        perishableData = in.readString();
        AddInsurance = in.readInt();
        insuranceAmount = in.readString();
    }

    public static final Creator<WareHouseList> CREATOR = new Creator<WareHouseList>() {
        @Override
        public WareHouseList createFromParcel(Parcel in) {
            return new WareHouseList(in);
        }

        @Override
        public WareHouseList[] newArray(int size) {
            return new WareHouseList[size];
        }
    };

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
        dest.writeInt(CommodityType);
        dest.writeString(Location);
        dest.writeString(FromDate);
        dest.writeString(ToDate);
        dest.writeString(Date);
        dest.writeString(CBM);
        dest.writeString(TotalCBMAvailable);
        dest.writeDouble(commodity_value);
        dest.writeInt(currency);
        dest.writeInt(type_of_good);
        dest.writeString(commodity_description);
        dest.writeInt(payment);
        dest.writeString(price);
        dest.writeString(order_number);
        dest.writeString(additionalInfo);
        dest.writeString(perishableData);
        dest.writeInt(AddInsurance);
        dest.writeString(insuranceAmount);
    }
}
