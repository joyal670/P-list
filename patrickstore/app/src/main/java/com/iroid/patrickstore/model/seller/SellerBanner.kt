package com.iroid.patrickstore.model.seller

import android.os.Parcel
import android.os.Parcelable

data class SellerBanner(
    val _id:String,
    val publicUrl:String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(publicUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SellerBanner> {
        override fun createFromParcel(parcel: Parcel): SellerBanner {
            return SellerBanner(parcel)
        }

        override fun newArray(size: Int): Array<SellerBanner?> {
            return arrayOfNulls(size)
        }
    }
}
