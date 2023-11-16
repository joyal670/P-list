package com.iroid.patrickstore.model.home

import android.os.Parcel
import android.os.Parcelable
import com.iroid.patrickstore.model.product.Product

data class ProductCategory(
    val __v: Int,
    val _id: String,
    val adminCommissionPercentage: Int,
    val createdAt: String,
    val franchiseCommissionPercentage: Int,
    val name: String,
    val parentId: String,
    val products: List<Product>,
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
        parcel.createTypedArrayList(Product)!!,
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
        parcel.writeString(parentId)
        parcel.writeTypedList(products)
        parcel.writeString(uniqueName)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductCategory> {
        override fun createFromParcel(parcel: Parcel): ProductCategory {
            return ProductCategory(parcel)
        }

        override fun newArray(size: Int): Array<ProductCategory?> {
            return arrayOfNulls(size)
        }
    }
}