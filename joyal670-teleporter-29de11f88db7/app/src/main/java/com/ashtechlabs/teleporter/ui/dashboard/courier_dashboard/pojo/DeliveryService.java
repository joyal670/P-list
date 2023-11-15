package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 18-Jan-17.
 */

public class DeliveryService {
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
    @SerializedName("ItemType")
    @Expose
    private String itemtype;
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
    @SerializedName("currency")
    @Expose
    private int currency;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("Duration")
    @Expose
    private String Duration;




    public String getOrdertype() {
        return this.ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return this.orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getRatecard() {
        return this.ratecard;
    }

    public void setRatecard(String ratecard) {
        this.ratecard = ratecard;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getItemtype() {
        return this.itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getFromlocation() {
        return this.fromlocation;
    }

    public void setFromlocation(String fromlocation) {
        this.fromlocation = fromlocation;
    }

    public String getTolocation() {
        return this.tolocation;
    }

    public void setTolocation(String tolocation) {
        this.tolocation = tolocation;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommodity_description() {
        return commodity_description;
    }

    public void setCommodity_description(String commodity_description) {
        this.commodity_description = commodity_description;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public String getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public int getCurrency() {
        return currency;
    }
}
