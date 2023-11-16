package com.iroid.patrickstore.model.seller

import android.os.Parcel
import android.os.Parcelable

data class SingleSellerResponse(
    val `data`: SingleSeller,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(SingleSeller::class.java.classLoader)!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(data, flags)
        parcel.writeByte(if (error) 1 else 0)
        parcel.writeString(msg)
        parcel.writeInt(statusCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SingleSellerResponse> {
        override fun createFromParcel(parcel: Parcel): SingleSellerResponse {
            return SingleSellerResponse(parcel)
        }

        override fun newArray(size: Int): Array<SingleSellerResponse?> {
            return arrayOfNulls(size)
        }
    }
}