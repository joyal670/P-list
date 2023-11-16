package com.iroid.patrickstore.model.home

import android.os.Parcel
import android.os.Parcelable

data class MainImage (
    val _id: String,
    val publicUrl: String
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

    companion object CREATOR : Parcelable.Creator<MainImage> {
        override fun createFromParcel(parcel: Parcel): MainImage {
            return MainImage(parcel)
        }

        override fun newArray(size: Int): Array<MainImage?> {
            return arrayOfNulls(size)
        }
    }
}