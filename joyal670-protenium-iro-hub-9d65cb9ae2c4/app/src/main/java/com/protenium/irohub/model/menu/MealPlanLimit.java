
package com.protenium.irohub.model.menu;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class MealPlanLimit implements Parcelable {

    @SerializedName("meal_type_id")
    private String mMealTypeId;
    @SerializedName("quantity")
    private String mQuantity;

    protected MealPlanLimit(Parcel in) {
        mMealTypeId = in.readString();
        mQuantity = in.readString();
    }

    public static final Creator<MealPlanLimit> CREATOR = new Creator<MealPlanLimit>() {
        @Override
        public MealPlanLimit createFromParcel(Parcel in) {
            return new MealPlanLimit(in);
        }

        @Override
        public MealPlanLimit[] newArray(int size) {
            return new MealPlanLimit[size];
        }
    };

    public String getMealTypeId() {
        return mMealTypeId;
    }

    public void setMealTypeId(String mealTypeId) {
        mMealTypeId = mealTypeId;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMealTypeId);
        dest.writeString(mQuantity);
    }
}
