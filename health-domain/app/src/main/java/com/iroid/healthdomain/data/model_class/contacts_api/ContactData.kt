package com.iroid.healthdomain.data.model_class.contacts_api

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class ContactData(
    val hashed_mobile: String,
    val hds:String,
    val id: Int,
    val is_follower: Int,
    var is_following: Int,
    var name: String,
    val points: String,
    val profile_image_url: String
):Parcelable,Serializable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hashed_mobile)
        parcel.writeString(hds)
        parcel.writeInt(id)
        parcel.writeInt(is_follower)
        parcel.writeInt(is_following)
        parcel.writeString(name)
        parcel.writeString(points)
        parcel.writeString(profile_image_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactData> {
        override fun createFromParcel(parcel: Parcel): ContactData {
            return ContactData(parcel)
        }

        override fun newArray(size: Int): Array<ContactData?> {
            return arrayOfNulls(size)
        }
    }
}