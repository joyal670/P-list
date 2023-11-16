package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo;

import com.ashtechlabs.teleporter.ui.pricing.pojo.PriceTrucking;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class TruckingNotificationList {

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
    private ArrayList<PriceTrucking> rateCardTrucking;

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

    public int getRateCardId() {
        return rateCardId;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public ArrayList<PriceTrucking> getRateCardTrucking() {
        return rateCardTrucking;
    }
}