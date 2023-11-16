package com.iroid.patrickstore.model.categoryProdcut

import android.os.Parcel
import android.os.Parcelable

data class CategoryProductResponse(
    val `data`: CategoryProduct,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(CategoryProduct::class.java.classLoader)!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(data, flags)
        parcel.writeByte(if (error) 1 else 0)
        parcel.writeString(msg)
        parcel.writeInt(statusCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryProductResponse> {
        override fun createFromParcel(parcel: Parcel): CategoryProductResponse {
            return CategoryProductResponse(parcel)
        }

        override fun newArray(size: Int): Array<CategoryProductResponse?> {
            return arrayOfNulls(size)
        }
    }
}