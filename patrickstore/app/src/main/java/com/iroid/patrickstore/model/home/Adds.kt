package com.iroid.patrickstore.model.home

import android.os.Parcel
import android.os.Parcelable

data class Adds(
    val __v: Int,
    val category: String,
    val id: String,
    val mainImage: MainImage,
    val product: List<String>,
    val sellerId: String,
    val title: String,
    val type: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(MainImage::class.java.classLoader)!!,
        parcel.createStringArrayList()!!,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeString(category)
        parcel.writeString(id)
        parcel.writeParcelable(mainImage, flags)
        parcel.writeStringList(product)
        parcel.writeString(sellerId)
        parcel.writeString(title)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Adds> {
        override fun createFromParcel(parcel: Parcel): Adds {
            return Adds(parcel)
        }

        override fun newArray(size: Int): Array<Adds?> {
            return arrayOfNulls(size)
        }
    }
}
