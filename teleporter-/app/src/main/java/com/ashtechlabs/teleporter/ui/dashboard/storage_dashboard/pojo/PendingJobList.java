package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 08-Mar-17.
 */

public class PendingJobList {
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

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getFromlocation() {
        return fromlocation;
    }

    public void setFromlocation(String fromlocation) {
        this.fromlocation = fromlocation;
    }

    public String getTolocation() {
        return tolocation;
    }

    public void setTolocation(String tolocation) {
        this.tolocation = tolocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
