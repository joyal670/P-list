package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 19-Jan-17.
 */

public class OrderTrucking {

    @SerializedName("OrderID")
    public String order_number;
    @SerializedName("Date")
    public String date;
    @SerializedName("FromLocation")
    public String location_from;
    @SerializedName("ToLocation")
    public String location_to;
    @SerializedName("State")
    public String state;
    @SerializedName("ItemType")
    public String item_type;
    @SerializedName("RateCardID")
    public String jobID;
    @SerializedName("OrderType")
    public String orderType;

    public String getOrder_number() {
        return this.order_number;
    }

    public String getDate() {
        return this.date;
    }

    public String getLocation_from() {
        return this.location_from;
    }

    public String getLocation_to() {
        return this.location_to;
    }

    public String getState() {
        return this.state;
    }

    public String getItem_type() {
        return this.item_type;
    }

    public String getJobID() {
        return this.jobID;
    }

    public String getOrderType() {
        return this.orderType;
    }
}
