
package com.protenium.irohub.model.SubscriptionInfo;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Food {

    @SerializedName("average_rating")
    private double mAverageRating;
    @SerializedName("calories")
    private int mCalories;
    @SerializedName("carbs")
    private int mCarbs;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("fat")
    private int mFat;
    @SerializedName("id")
    private int mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("my_rating")
    private int mMyRating;
    @SerializedName("name")
    private String mName;
    @SerializedName("proteins")
    private int mProteins;
    @SerializedName("tagline")
    private String mTagline;
    @SerializedName("tags")
    private List<Tag> mTags;

    public double getAverageRating() {
        return mAverageRating;
    }

    public void setAverageRating(double averageRating) {
        mAverageRating = averageRating;
    }

    public int getCalories() {
        return mCalories;
    }

    public void setCalories(int calories) {
        mCalories = calories;
    }

    public int getCarbs() {
        return mCarbs;
    }

    public void setCarbs(int carbs) {
        mCarbs = carbs;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getFat() {
        return mFat;
    }

    public void setFat(int fat) {
        mFat = fat;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public int getMyRating() {
        return mMyRating;
    }

    public void setMyRating(int myRating) {
        mMyRating = myRating;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getProteins() {
        return mProteins;
    }

    public void setProteins(int proteins) {
        mProteins = proteins;
    }

    public String getTagline() {
        return mTagline;
    }

    public void setTagline(String tagline) {
        mTagline = tagline;
    }

    public List<Tag> getTags() {
        return mTags;
    }

    public void setTags(List<Tag> tags) {
        mTags = tags;
    }

}
