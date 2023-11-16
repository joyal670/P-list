package com.iroid.patrickstore.model.delivered_order

import android.os.Parcel
import android.os.Parcelable

data class SellerId(
    val id: String,
    val profileImageId: ProfileImageId,
    val storeName: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readParcelable(ProfileImageId::class.java.classLoader)!!,
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeParcelable(profileImageId, flags)
        parcel.writeString(storeName)
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
