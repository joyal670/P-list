package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo;

import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceCourier;
import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceVendor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class CourierNotificationList {
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
    private ArrayList<PriceCourier> rateCardCourier;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ArrayList<PriceCourier> getRateCardCourier() {
        return rateCardCourier;
    }
}