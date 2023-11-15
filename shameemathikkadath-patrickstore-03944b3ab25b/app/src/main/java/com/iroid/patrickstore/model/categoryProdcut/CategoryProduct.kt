package com.iroid.patrickstore.model.categoryProdcut

import android.os.Parcel
import android.os.Parcelable
import com.iroid.patrickstore.model.product.Product

data class CategoryProduct(
    val hasNextPage: Boolean,
    val items: List<Product>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.createTypedArrayList(Product)!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (hasNextPage) 1 else 0)
        parcel.writeTypedList(items)
        parcel.writeInt(perPage)
        parcel.writeInt(totalCount)
        parcel.writeInt(totalPages)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryProduct> {
        override fun createFromParcel(parcel: Parcel): CategoryProduct {
            return CategoryProduct(parcel)
        }

        override fun newArray(size: Int): Array<CategoryProduct?> {
            return arrayOfNulls(size)
        }
    }
}