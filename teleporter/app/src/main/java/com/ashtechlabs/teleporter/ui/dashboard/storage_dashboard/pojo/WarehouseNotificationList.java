package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceVendor;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PricingWareHouse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class WarehouseNotificationList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("rate_card_id")
    private int rateCardId;
    //    @SerializedName("date_added")
//    private int announcementDate;
    @SerializedName("notification_type")
    private int notificationType;
    @SerializedName("ratecard_details")
    private ArrayList<PricingWareHouse> rateCardWarehouse;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRateCardId() {
        return rateCardId;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public ArrayList<PricingWareHouse> getRateCardWarehouse() {
        return rateCardWarehouse;
    }
}