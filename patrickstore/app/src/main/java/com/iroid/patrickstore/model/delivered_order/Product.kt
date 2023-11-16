package com.iroid.patrickstore.model.delivered_order

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val __v: Int,
    val actualPrice: Double,
    val adminCommission: Int,
    val canSubtractStock: Boolean,
    val category: String,
    val costPrice: Int,
    val description: String,
    val discountDateFrom: String,
    val discountDateTo: String,
    val franchiseCommissionAmount: Double,
    val franchiseId: String,
    val height: String,
    val id: String,
    val imgUrl: List<ImgUrl>,
    val isApproved: Boolean,
    val isEnabled: Boolean,
    val isRejected: Boolean,
    val length: String,
    val maxDiscount: Int,
    val maxQuantity: Int,
    val minQunatity: Int,
    val model: String,
    val name: String,
    val options: List<Option>,
    val productId: Int,
    val quantity: Int,
    val sellerId: String,
    val shopAmount: Double,
    val shopDiscount: Int,
    val tag: String,
    val type: String,
    val weight: String,
    val width: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(ImgUrl)!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(Option)!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeDouble(actualPrice)
        parcel.writeInt(adminCommission)
        parcel.writeByte(if (canSubtractStock) 1 else 0)
        parcel.writeString(category)
        parcel.writeInt(costPrice)
        parcel.writeString(description)
        parcel.writeString(discountDateFrom)
        parcel.writeString(discountDateTo)
        parcel.writeDouble(franchiseCommissionAmount)
        parcel.writeString(franchiseId)
        parcel.writeString(height)
        parcel.writeString(id)
        parcel.writeTypedList(imgUrl)
        parcel.writeByte(if (isApproved) 1 else 0)
        parcel.writeByte(if (isEnabled) 1 else 0)
        parcel.writeByte(if (isRejected) 1 else 0)
        parcel.writeString(length)
        parcel.writeInt(maxDiscount)
        parcel.writeInt(maxQuantity)
        parcel.writeInt(minQunatity)
        parcel.writeString(model)
        parcel.writeString(name)
        parcel.writeTypedList(options)
        parcel.writeInt(productId)
        parcel.writeInt(quantity)
        parcel.writeString(sellerId)
        parcel.writeDouble(shopAmount)
        parcel.writeInt(shopDiscount)
        parcel.writeString(tag)
        parcel.writeString(type)
        parcel.writeString(weight)
        parcel.writeString(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
