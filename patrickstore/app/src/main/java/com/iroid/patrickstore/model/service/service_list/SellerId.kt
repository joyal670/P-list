package com.iroid.patrickstore.model.service.service_list

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class SellerId(
    val id: String,
    val sellerName: String
):Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(sellerName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerId> {
        override fun createFromParcel(parcel: Parcel): SellerId {
            return SellerId(parcel)
        }

        override fun newArray(size: Int): Array<SellerId?> {
            return arrayOfNulls(size)
        }
    }
}
