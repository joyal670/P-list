package com.iroid.patrickstore.model.delivered_order

import android.os.Parcel
import android.os.Parcelable

data class OrderHistory(
    val _id: String,
    val currentOrderStatus: String,
    val tsCreatedAt: Long
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(currentOrderStatus)
        parcel.writeLong(tsCreatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderHistory> {
        override fun createFromParcel(parcel: Parcel): OrderHistory {
            return OrderHistory(parcel)
        }

        override fun newArray(size: Int): Array<OrderHistory?> {
            return arrayOfNulls(size)
        }
    }
}
