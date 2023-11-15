package com.proteinium.proteiniumdietapp.pojo.menu_day

import android.os.Parcel
import android.os.Parcelable

data class MealType(
    val foods: List<Food>,
    val id: Int,
    val name: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Food)!!,
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(foods)
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealType> {
        override fun createFromParcel(parcel: Parcel): MealType {
            return MealType(parcel)
        }

        override fun newArray(size: Int): Array<MealType?> {
            return arrayOfNulls(size)
        }
    }
}