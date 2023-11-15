package com.iroid.patrickstore.model.home

import android.os.Parcel
import android.os.Parcelable

data class ShopCatId(
    val _id: String,
    val name: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopCatId> {
        override fun createFromParcel(parcel: Parcel): ShopCatId {
            return ShopCatId(parcel)
        }

        override fun newArray(size: Int): Array<ShopCatId?> {
            return arrayOfNulls(size)
        }
    }
}