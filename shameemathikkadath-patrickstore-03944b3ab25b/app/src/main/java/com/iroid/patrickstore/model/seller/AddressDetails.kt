package com.iroid.patrickstore.model.seller

import android.os.Parcel
import android.os.Parcelable

data class AddressDetails(
    val address: String,
    val district: String,
    val location: String,
    val pincode: String,
    val state: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeString(district)
        parcel.writeString(location)
        parcel.writeString(pincode)
        parcel.writeString(state)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddressDetails> {
        override fun createFromParcel(parcel: Parcel): AddressDetails {
            return AddressDetails(parcel)
        }

        override fun newArray(size: Int): Array<AddressDetails?> {
            return arrayOfNulls(size)
        }
    }


}