package com.ashtechlabs.teleporter.ui.pricing.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IROID_ANDROID1 on 02-Feb-17.
 */

public class PricingWareHouse implements Parcelable {

    @SerializedName("ID")
    @Expose
    private String ID;
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
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("StorageType")
    @Expose
    private String StorageType;
    @SerializedName("additional_info")
    @Expose
    private String additional_info;
    @SerializedName("perishable_det")
    @Expose
    private String perishable_det;
    @SerializedName("min_insurance_amt")
    @Expose
    private String min_insurance_amt;
    @SerializedName("ins_percent")
    @Expose
    private String ins_percent;
    @SerializedName("rate_validity")
    @Expose
    private String rate_validity;
    @SerializedName("ratecard_expiry_status")
    @Expose
    private String ratecard_expiry_status;
    @SerializedName("RateCardID")
    @Expose
    private String RateCardID;
    @SerializedName("warehouse_name")
    @Expose
    private String warehouseName;
    @SerializedName("warehouse_det_id")
    @Expose
    private int warehouseDetId;

    protected PricingWareHouse(Parcel in) {
        ID = in.readString();
        Location = in.readString();
        PCBM = in.readString();
        FromDate = in.readString();
        ToDate = in.readString();
        TotalCBMAvailable = in.readString();
        currency = in.readString();
        StorageType = in.readString();
        additional_info = in.readString();
        perishable_det = in.readString();
        min_insurance_amt = in.readString();
        ins_percent = in.readString();
        rate_validity = in.readString();
        ratecard_expiry_status = in.readString();
        RateCardID = in.readString();
        warehouseName = in.readString();
        warehouseDetId = in.readInt();
    }

    public static final Creator<PricingWareHouse> CREATOR = new Creator<PricingWareHouse>() {
        @Override
        public PricingWareHouse createFromParcel(Parcel in) {
            return new PricingWareHouse(in);
        }

        @Override
        public PricingWareHouse[] newArray(int size) {
            return new PricingWareHouse[size];
        }
    };

    public String getID() {
        return ID;
    }

    public String getLocation() {
        return Location;
    }

    public String getPCBM() {
        return PCBM;
    }

    public String getFromDate() {
        return FromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public String getTotalCBMAvailable() {
        return TotalCBMAvailable;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStorageType() {
        return StorageType;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public String getPerishable_det() {
        return perishable_det;
    }

    public String getMin_insurance_amt() {
        return min_insurance_amt;
    }

    public String getIns_percent() {
        return ins_percent;
    }

    public String getRate_validity() {
        return rate_validity;
    }

    public String getRatecard_expiry_status() {
        return ratecard_expiry_status;
    }

    public String getRateCardID() {
        return RateCardID;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public int getWarehouseDetId() {
        return warehouseDetId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Location);
        dest.writeString(PCBM);
        dest.writeString(FromDate);
        dest.writeString(ToDate);
        dest.writeString(TotalCBMAvailable);
        dest.writeString(currency);
        dest.writeString(StorageType);
        dest.writeString(additional_info);
        dest.writeString(perishable_det);
        dest.writeString(min_insurance_amt);
        dest.writeString(ins_percent);
        dest.writeString(rate_validity);
        dest.writeString(ratecard_expiry_status);
        dest.writeString(RateCardID);
        dest.writeString(warehouseName);
        dest.writeInt(warehouseDetId);
    }
}
