package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.storage_spaces.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by VIDHU on 12/19/2017.
 */

public class StorageSpace implements Parcelable{


    @SerializedName("warehouse_det_id")
    private int id;
    @SerializedName("warehouse_profile_id")
    private int warehouseProfileId;
    @SerializedName("warehouse_capacity")
    private String warehouseCapacity;
    @SerializedName("space_available")
    private String spaceAvailable;
    @SerializedName("warehouse_location")
    private String warehouseLocation;
    @SerializedName("warehouse_name")
    private String warehouseName;
    @SerializedName("warehouse_picture")
    private String warehousePicture;
    @SerializedName("trade_license")
    private String tradeLicense;
    @SerializedName("trade_license_expiry_date")
    private String tradeLicenseExpiryDate;
    @SerializedName("insurance_number")
    private String insuranceNumber;
    @SerializedName("insurance_exp_date")
    private String insuranceExpDate;

    protected StorageSpace(Parcel in) {
        id = in.readInt();
        warehouseProfileId = in.readInt();
        warehouseCapacity = in.readString();
        spaceAvailable = in.readString();
        warehouseLocation = in.readString();
        warehouseName = in.readString();
        warehousePicture = in.readString();
        tradeLicense = in.readString();
        tradeLicenseExpiryDate = in.readString();
        insuranceNumber = in.readString();
        insuranceExpDate = in.readString();
    }

    public static final Creator<StorageSpace> CREATOR = new Creator<StorageSpace>() {
        @Override
        public StorageSpace createFromParcel(Parcel in) {
            return new StorageSpace(in);
        }

        @Override
        public StorageSpace[] newArray(int size) {
            return new StorageSpace[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getWarehouseProfileId() {
        return warehouseProfileId;
    }

    public String getWarehouseCapacity() {
        return warehouseCapacity;
    }

    public String getSpaceAvailable() {
        return spaceAvailable;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public String getWarehousePicture() {
        return warehousePicture;
    }

    public String getTradeLicense() {
        return tradeLicense;
    }

    public String getTradeLicenseExpiryDate() {
        return tradeLicenseExpiryDate;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public String getInsuranceExpDate() {
        return insuranceExpDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(warehouseProfileId);
        dest.writeString(warehouseCapacity);
        dest.writeString(spaceAvailable);
        dest.writeString(warehouseLocation);
        dest.writeString(warehouseName);
        dest.writeString(warehousePicture);
        dest.writeString(tradeLicense);
        dest.writeString(tradeLicenseExpiryDate);
        dest.writeString(insuranceNumber);
        dest.writeString(insuranceExpDate);
    }
}
