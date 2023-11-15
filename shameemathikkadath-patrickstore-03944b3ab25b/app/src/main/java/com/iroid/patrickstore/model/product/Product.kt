package com.iroid.patrickstore.model.product

import android.os.Parcel
import android.os.Parcelable
import com.iroid.patrickstore.model.home.ImgUrl

data class Product(
    val __v: Int,
    val actualPrice: Double,
    val adminCommission: Int,
    val adminCommissionAmount: Double,
    val canSubtractStock: Boolean,
    val color: String,
    val costPrice: Double,
    val description: String,
    val discountDateFrom: String,
    val discountDateTo: String,
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
    val offerPrice: Double,
    val productId: Int,
    val productTypeId: String,
    val quantity: Int,
    val shopAmount: Double,
    val shopDiscount: Int,
    val size: String,
    val tag: String,
    val weight: String,
    val width: String,
    val displayPrice:Double,
    val averageRating:Float
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
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
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeDouble(actualPrice)
        parcel.writeInt(adminCommission)
        parcel.writeDouble(adminCommissionAmount)
        parcel.writeByte(if (canSubtractStock) 1 else 0)
        parcel.writeString(color)
        parcel.writeDouble(costPrice)
        parcel.writeString(description)
        parcel.writeString(discountDateFrom)
        parcel.writeString(discountDateTo)
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
        parcel.writeDouble(offerPrice)
        parcel.writeInt(productId)
        parcel.writeString(productTypeId)
        parcel.writeInt(quantity)
        parcel.writeDouble(shopAmount)
        parcel.writeInt(shopDiscount)
        parcel.writeString(size)
        parcel.writeString(tag)
        parcel.writeString(weight)
        parcel.writeString(width)
        parcel.writeDouble(displayPrice)
        parcel.writeFloat(averageRating)
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
