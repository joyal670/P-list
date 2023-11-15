package com.iroid.patrickstore.model.delivered_order

import android.os.Parcel
import android.os.Parcelable

data class CartItem(
    val _id: String,
    val orderStatus: String,
    val price: Double,
    val product: Product,
    val quantity: Int,
    val sellerId: SellerId,
    val shopAmount: Double
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readParcelable(Product::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readParcelable(SellerId::class.java.classLoader)!!,
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(orderStatus)
        parcel.writeDouble(price)
        parcel.writeParcelable(product, flags)
        parcel.writeInt(quantity)
        parcel.writeParcelable(sellerId, flags)
        parcel.writeDouble(shopAmount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}
