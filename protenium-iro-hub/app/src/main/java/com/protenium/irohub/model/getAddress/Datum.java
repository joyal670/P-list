
package com.protenium.irohub.model.getAddress;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Datum {

    @SerializedName("appartment")
    private String mAppartment;
    @SerializedName("area")
    private String mArea;
    @SerializedName("avenue")
    private String mAvenue;
    @SerializedName("block")
    private String mBlock;
    @SerializedName("building")
    private String mBuilding;
    @SerializedName("default")
    private Long mDefault;
    @SerializedName("floor")
    private String mFloor;
    @SerializedName("governorate")
    private String mGovernorate;
    @SerializedName("id")
    private int mId;
    @SerializedName("street")
    private String mStreet;
    @SerializedName("title")
    private Object mTitle;
    @SerializedName("isCheck")
    private Boolean isChecked;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getAppartment() {
        return mAppartment;
    }

    public void setAppartment(String appartment) {
        mAppartment = appartment;
    }

    public String getArea() {
        return mArea;
    }

    public void setArea(String area) {
        mArea = area;
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

    public Long getDefault() {
        return mDefault;
    }

    public void setDefault(Long defaultw) {
        mDefault = defaultw;
    }

    public String getFloor() {
        return mFloor;
    }

    public void setFloor(String floor) {
        mFloor = floor;
    }

    public String getGovernorate() {
        return mGovernorate;
    }

    public void setGovernorate(String governorate) {
        mGovernorate = governorate;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
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

}
