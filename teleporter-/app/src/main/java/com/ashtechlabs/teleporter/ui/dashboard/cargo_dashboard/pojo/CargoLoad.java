package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VIDHU on 2/13/2018.
 */

public class CargoLoad implements Parcelable{

    @SerializedName("load_det_id")
    @Expose
    private String id;
    @SerializedName("freightforwardorder_id")
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


    protected CargoLoad(Parcel in) {
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

    public static final Creator<CargoLoad> CREATOR = new Creator<CargoLoad>() {
        @Override
        public CargoLoad createFromParcel(Parcel in) {
            return new CargoLoad(in);
        }

        @Override
        public CargoLoad[] newArray(int size) {
            return new CargoLoad[size];
        }
    };

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
}
