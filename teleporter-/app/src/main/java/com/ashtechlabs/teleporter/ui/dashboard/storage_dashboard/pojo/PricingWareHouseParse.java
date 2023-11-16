package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public class PricingWareHouseParse {
    @SerializedName("ID")
    @Expose
    private String ID;
    @SerializedName("WarehouseID")
    @Expose
    private String WarehouseID;
    @SerializedName("StorageType")
    @Expose
    private String StorageType;
    @SerializedName("Location")
    @Expose
    private String Location;
    @SerializedName("PCBM")
    @Expose
    private String PCBM;
    @SerializedName("FromDate")
    @Expose
    private String FromDate;
    @SerializedName("ToDate")
    @Expose
    private String ToDate;
    @SerializedName("TotalCBMAvailable")
    @Expose
    private String TotalCBMAvailable;


    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWarehouseID() {
        return this.WarehouseID;
    }

    public void setWarehouseID(String warehouseID) {
        this.WarehouseID = warehouseID;
    }

    public String getStorageType() {
        return this.StorageType;
    }

    public void setStorageType(String storageType) {
        this.StorageType = storageType;
    }

    public String getLocation() {
        return this.Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getPCBM() {
        return this.PCBM;
    }

    public void setPCBM(String PCBM) {
        this.PCBM = PCBM;
    }

    public String getFromDate() {
        return this.FromDate;
    }

    public void setFromDate(String fromDate) {
        this.FromDate = fromDate;
    }

    public String getToDate() {
        return this.ToDate;
    }

    public void setToDate(String toDate) {
        this.ToDate = toDate;
    }

    public String getTotalCBMAvailable() {
        return this.TotalCBMAvailable;
    }


    public void setTotalCBMAvailable(String totalCBMAvailable) {
        this.TotalCBMAvailable = totalCBMAvailable;
    }
}
