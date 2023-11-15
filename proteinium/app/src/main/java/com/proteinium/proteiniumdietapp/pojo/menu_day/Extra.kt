package com.proteinium.proteiniumdietapp.pojo.menu_day

import android.os.Parcel
import android.os.Parcelable

data class Extra(
    val meal_type_id: Int,
    val quantity: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(meal_type_id)
        parcel.writeString(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Extra> {
        override fun createFromParcel(parcel: Parcel): Extra {
            return Extra(parcel)
        }

        override fun newArray(size: Int): Array<Extra?> {
            return arrayOfNulls(size)
        }
    }
}