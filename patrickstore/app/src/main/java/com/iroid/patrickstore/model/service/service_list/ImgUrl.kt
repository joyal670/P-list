package com.iroid.patrickstore.model.service.service_list

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class ImgUrl(
    val _id: String,
    val publicUrl: String
):Parcelable, Serializable {
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

    companion object CREATOR : Parcelable.Creator<ImgUrl> {
        override fun createFromParcel(parcel: Parcel): ImgUrl {
            return ImgUrl(parcel)
        }

        override fun newArray(size: Int): Array<ImgUrl?> {
            return arrayOfNulls(size)
        }
    }
}
