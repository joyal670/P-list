package com.proteinium.proteiniumdietapp.pojo.menu_day

import android.os.Parcel
import android.os.Parcelable

data class MealLimit(
    val combination1: Int,
    val combination2: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(combination1)
        parcel.writeInt(combination2)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealLimit> {
        override fun createFromParcel(parcel: Parcel): MealLimit {
            return MealLimit(parcel)
        }

        override fun newArray(size: Int): Array<MealLimit?> {
            return arrayOfNulls(size)
        }
    }
}