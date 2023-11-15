package com.iroid.patrickstore.model.home

import android.os.Parcel
import android.os.Parcelable

data class HomeResponse(
    val `data`: List<Home>,
    val error: Boolean,
    val msg: String,
    val statusCode: Int,
    val franchiseId:String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Home)!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(data)
        parcel.writeByte(if (error) 1 else 0)
        parcel.writeString(msg)
        parcel.writeInt(statusCode)
        parcel.writeString(franchiseId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeResponse> {
        override fun createFromParcel(parcel: Parcel): HomeResponse {
            return HomeResponse(parcel)
        }

        override fun newArray(size: Int): Array<HomeResponse?> {
            return arrayOfNulls(size)
        }
    }
}
