package com.iroid.patrickstore.model.coupon

import android.os.Parcel
import android.os.Parcelable

data class Coupon(
    val hasNextPage: Boolean,
    val items: List<ItemCoupon>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.createTypedArrayList(ItemCoupon)!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (hasNextPage) 1 else 0)
        parcel.writeTypedList(items)
        parcel.writeInt(perPage)
        parcel.writeInt(totalCount)
        parcel.writeInt(totalPages)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Coupon> {
        override fun createFromParcel(parcel: Parcel): Coupon {
            return Coupon(parcel)
        }

        override fun newArray(size: Int): Array<Coupon?> {
            return arrayOfNulls(size)
        }
    }
}