package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VIDHU on 2/13/2018.
 */

public class TruckingLoad implements Parcelable{

    @SerializedName("mporder_load_det_id")
    @Expose
    private String  id;
    @SerializedName("mpoder_id")
    @Expose
    private String mpoderId;
    @SerializedName("packing_type")
    @Expose
    private String packingType;
    @SerializedName("package_weight")
    @Expose
    private String packageWeight;
    @SerializedName("package_unit")
    @Expose
    private String packageUnit;
    @SerializedName("package_qty")
    @Expose
    private String packageQty;
    @SerializedName("package_dimension_length")
    @Expose
    private String packageDimensionLength;
    @SerializedName("package_dimension_breadth")
    @Expose
    private String packageDimensionBreadth;
    @SerializedName("package_dimension_height")
    @Expose
    private String packageDimensionHeight;
    @SerializedName("package_dimension_unit")
    @Expose
    private String packageDimensionUnit;

    protected TruckingLoad(Parcel in) {
        id = in.readString();
        mpoderId = in.readString();
        packingType = in.readString();
        packageWeight = in.readString();
        packageUnit = in.readString();
        packageQty = in.readString();
        packageDimensionLength = in.readString();
        packageDimensionBreadth = in.readString();
        packageDimensionHeight = in.readString();
        packageDimensionUnit = in.readString();
    }

    public static final Creator<TruckingLoad> CREATOR = new Creator<TruckingLoad>() {
        @Override
        public TruckingLoad createFromParcel(Parcel in) {
            return new TruckingLoad(in);
        }

        @Override
        public TruckingLoad[] newArray(int size) {
            return new TruckingLoad[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getMpoderId() {
        return mpoderId;
    }

    public String getPackingType() {
        return packingType;
    }

    public String getPackageWeight() {
        return packageWeight;
    }

    public String getPackageUnit() {
        return packageUnit;
    }

    public String getPackageQty() {
        return packageQty;
    }

    public String getPackageDimensionLength() {
        return packageDimensionLength;
    }

    public String getPackageDimensionBreadth() {
        return packageDimensionBreadth;
    }

    public String getPackageDimensionHeight() {
        return packageDimensionHeight;
    }

    public String getPackageDimensionUnit() {
        return packageDimensionUnit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(mpoderId);
        dest.writeString(packingType);
        dest.writeString(packageWeight);
        dest.writeString(packageUnit);
        dest.writeString(packageQty);
        dest.writeString(packageDimensionLength);
        dest.writeString(packageDimensionBreadth);
        dest.writeString(packageDimensionHeight);
        dest.writeString(packageDimensionUnit);
    }
}
