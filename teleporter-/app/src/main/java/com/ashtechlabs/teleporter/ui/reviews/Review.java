package com.ashtechlabs.teleporter.ui.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 31-Jan-17.
 */

public class Review {
    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("JobID")
    @Expose
    private String JobID;
    @SerializedName("CustomerID")
    @Expose
    private String CustomerID;
    @SerializedName("DriverID")
    @Expose
    private String DriverID;
    @SerializedName("CustomerReview")
    @Expose
    private String CustomerReview;
    @SerializedName("CustomerRating")
    @Expose
    private String CustomerRating;
    @SerializedName("DriverReview")
    @Expose
    private String DriverReview;
    @SerializedName("DriverRating")
    @Expose
    private String DriverRating;
    @SerializedName("ReviewDate")
    @Expose
    private String ReviewDate;
    @SerializedName("CustomerName")
    @Expose
    private String CustomerName;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobID() {
        return this.JobID;
    }

    public void setJobID(String jobID) {
        this.JobID = jobID;
    }

    public String getCustomerID() {
        return this.CustomerID;
    }

    public void setCustomerID(String customerID) {
        this.CustomerID = customerID;
    }

    public String getDriverID() {
        return this.DriverID;
    }

    public void setDriverID(String driverID) {
        this.DriverID = driverID;
    }

    public String getCustomerReview() {
        return this.CustomerReview;
    }

    public void setCustomerReview(String customerReview) {
        this.CustomerReview = customerReview;
    }

    public String getCustomerRating() {
        return this.CustomerRating;
    }

    public void setCustomerRating(String customerRating) {
        this.CustomerRating = customerRating;
    }

    public String getDriverReview() {
        return this.DriverReview;
    }

    public void setDriverReview(String driverReview) {
        this.DriverReview = driverReview;
    }

    public String getDriverRating() {
        return this.DriverRating;
    }

    public void setDriverRating(String driverRating) {
        this.DriverRating = driverRating;
    }

    public String getReviewDate() {
        return this.ReviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.ReviewDate = reviewDate;
    }

    public String getCustomerName() {
        return this.CustomerName;
    }

    public void setCustomerName(String customerName) {
        this.CustomerName = customerName;
    }
}