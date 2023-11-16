package com.ashtechlabs.teleporter.ui.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 31-Jan-17.
 */

public class ReviewList {
    String code;

    @SerializedName("value")
    @Expose
    private ArrayList<Review> reviews;


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Review> getReviewService() {
        return reviews;
    }

    public void setReviewService(ArrayList<Review> review) {
        reviews = review;
    }
}