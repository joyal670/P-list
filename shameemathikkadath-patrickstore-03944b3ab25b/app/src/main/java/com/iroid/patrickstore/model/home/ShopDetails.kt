package com.iroid.patrickstore.model.home

import android.os.Parcel
import android.os.Parcelable

data class ShopDetails(
    val id: String,
    val profileImageId: ProfileImageId,
    val sellerName: String,
    val shopCatId: ShopCatId
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readParcelable(ProfileImageId::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.readParcelable(ShopCatId::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeParcelable(profileImageId, flags)
        parcel.writeString(sellerName)
        parcel.writeParcelable(shopCatId, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopDetails> {
        override fun createFromParcel(parcel: Parcel): ShopDetails {
            return ShopDetails(parcel)
        }

        override fun newArray(size: Int): Array<ShopDetails?> {
            return arrayOfNulls(size)
        }
    }
}