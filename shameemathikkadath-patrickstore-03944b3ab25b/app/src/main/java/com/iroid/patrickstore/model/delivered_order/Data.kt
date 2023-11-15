package com.iroid.patrickstore.model.delivered_order

import android.os.Parcel
import android.os.Parcelable

data class Data(
    val hasNextPage: Boolean,
    val items: List<DeliveredOrderItem>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.createTypedArrayList(DeliveredOrderItem)!!,
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

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }
}
