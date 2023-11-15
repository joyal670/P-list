package com.iroid.patrickstore.model.seller

import android.os.Parcel
import android.os.Parcelable

data class SellingCategory(
    val _id: String,
    val isPerishable: Boolean,
    val name: String,
    val uniqueName: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeByte(if (isPerishable) 1 else 0)
        parcel.writeString(name)
        parcel.writeString(uniqueName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellingCategory> {
        override fun createFromParcel(parcel: Parcel): SellingCategory {
            return SellingCategory(parcel)
        }

        override fun newArray(size: Int): Array<SellingCategory?> {
            return arrayOfNulls(size)
        }
    }
}