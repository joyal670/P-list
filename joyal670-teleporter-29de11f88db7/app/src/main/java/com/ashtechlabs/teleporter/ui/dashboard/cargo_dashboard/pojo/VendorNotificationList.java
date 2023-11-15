package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo;

import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceVendor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class VendorNotificationList {

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("type")
    private String type;
    @SerializedName("rate_card_id")
    private int rateCardId;
    @SerializedName("notification_type")
    private int notificationType;
    @SerializedName("ratecard_details")
    private ArrayList<PriceVendor> rateCardVendor;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRateCardId() {
        return rateCardId;
    }

//    public int getAnnouncementDate() {
//        return announcementDate;
//    }

    public int getNotificationType() {
        return notificationType;
    }

    public ArrayList<PriceVendor> getRateCardVendor() {
        return rateCardVendor;
    }
}