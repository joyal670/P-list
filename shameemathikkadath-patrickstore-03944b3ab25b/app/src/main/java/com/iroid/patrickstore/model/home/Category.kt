package com.iroid.patrickstore.model.home

import android.os.Parcel
import android.os.Parcelable

data class Category(
    val __v: Int,
    val _id: String,
    val adminCommissionPercentage: Int,
    val createdAt: String,
    val franchiseCommissionPercentage: Int,
    val name: String,
    val uniqueName: String,
    val updatedAt: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeString(_id)
        parcel.writeInt(adminCommissionPercentage)
        parcel.writeString(createdAt)
        parcel.writeInt(franchiseCommissionPercentage)
        parcel.writeString(name)
        parcel.writeString(uniqueName)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}