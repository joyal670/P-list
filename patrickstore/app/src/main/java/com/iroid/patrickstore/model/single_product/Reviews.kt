package com.iroid.patrickstore.model.single_product

import android.os.Parcel
import android.os.Parcelable

data class Reviews (
    val customerId:String,
    val id:String,
    val rating:Float,
    val review:String,
    val reviewTitle:String,
    val __v:String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readFloat(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(customerId)
        parcel.writeString(id)
        parcel.writeFloat(rating)
        parcel.writeString(review)
        parcel.writeString(reviewTitle)
        parcel.writeString(__v)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reviews> {
        override fun createFromParcel(parcel: Parcel): Reviews {
            return Reviews(parcel)
        }

        override fun newArray(size: Int): Array<Reviews?> {
            return arrayOfNulls(size)
        }
    }
}
