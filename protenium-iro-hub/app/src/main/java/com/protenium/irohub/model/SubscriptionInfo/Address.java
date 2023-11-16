
package com.protenium.irohub.model.SubscriptionInfo;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Address {

    @SerializedName("appartment")
    private String mAppartment;
    @SerializedName("area_id")
    private Long mAreaId;
    @SerializedName("avenue")
    private String mAvenue;
    @SerializedName("block")
    private String mBlock;
    @SerializedName("building")
    private String mBuilding;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("default")
    private Long mDefault;
    @SerializedName("floor")
    private String mFloor;
    @SerializedName("governorate_id")
    private Long mGovernorateId;
    @SerializedName("id")
    private Long mId;
    @SerializedName("street")
    private String mStreet;
    @SerializedName("title")
    private Object mTitle;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_id")
    private Long mUserId;

    public String getAppartment() {
        return mAppartment;
    }

    public void setAppartment(String appartment) {
        mAppartment = appartment;
    }

    public Long getAreaId() {
        return mAreaId;
    }

    public void setAreaId(Long areaId) {
        mAreaId = areaId;
    }

    public String getAvenue() {
        return mAvenue;
    }

    public void setAvenue(String avenue) {
        mAvenue = avenue;
    }

    public String getBlock() {
        return mBlock;
    }

    public void setBlock(String block) {
        mBlock = block;
    }

    public String getBuilding() {
        return mBuilding;
    }

    public void setBuilding(String building) {
        mBuilding = building;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public Long getDefault() {
        return mDefault;
    }

    public void setDefault(Long default1) {
        mDefault = default1;
    }

    public String getFloor() {
        return mFloor;
    }

    public void setFloor(String floor) {
        mFloor = floor;
    }

    public Long getGovernorateId() {
        return mGovernorateId;
    }

    public void setGovernorateId(Long governorateId) {
        mGovernorateId = governorateId;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        mStreet = street;
    }

    public Object getTitle() {
        return mTitle;
    }

    public void setTitle(Object title) {
        mTitle = title;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public Long getUserId() {
        return mUserId;
    }

    public void setUserId(Long userId) {
        mUserId = userId;
    }

}
