package com.proteinium.proteiniumdietapp.pojo.menu_day

import android.os.Parcel
import android.os.Parcelable

data class MealPlanLimit(
    val meal_type_id: String,
    val quantity: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(meal_type_id)
        parcel.writeString(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealPlanLimit> {
        override fun createFromParcel(parcel: Parcel): MealPlanLimit {
            return MealPlanLimit(parcel)
        }

        override fun newArray(size: Int): Array<MealPlanLimit?> {
            return arrayOfNulls(size)
        }
    }
}