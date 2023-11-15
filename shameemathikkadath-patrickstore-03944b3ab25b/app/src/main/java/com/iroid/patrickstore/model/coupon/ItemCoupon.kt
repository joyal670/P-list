package com.iroid.patrickstore.model.coupon

import android.os.Parcel
import android.os.Parcelable

data class ItemCoupon(
    val code: String,
    val expiryDate: Long,
    val id: String,
    val name: String,
    val percentage: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeLong(expiryDate)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeInt(percentage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemCoupon> {
        override fun createFromParcel(parcel: Parcel): ItemCoupon {
            return ItemCoupon(parcel)
        }

        override fun newArray(size: Int): Array<ItemCoupon?> {
            return arrayOfNulls(size)
        }
    }
}