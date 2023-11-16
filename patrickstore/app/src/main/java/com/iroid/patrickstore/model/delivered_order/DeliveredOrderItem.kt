package com.iroid.patrickstore.model.delivered_order

import android.os.Parcel
import android.os.Parcelable

data class DeliveredOrderItem(
    val __v: Int,
    val _id: String,
    val billingAddressId: String,
    val cartItems: List<CartItem>,
    val createdAt: String,
    val dateOfPurchase: String,
    val dateOfPurchaseTs: String,
    val deliveredAt: Long,
    val deliveryCharge: Int,
    val deliverySurgeCharge: Int,
    val franchiseId: String,
    val grandTotal: Double,
    val isAnyDeliverySurge: Boolean,
    val isConvertedToOrder: Boolean,
    val isOrderByCall: Boolean,
    val isReturnOrder: Boolean,
    val itemTotal: Double,
    val orderHistory: List<OrderHistory>,
    val orderId: Int,
    val orderStatus: String,
    val packingCharge: Int,
    val paymentMethod: String,
    val serviceCharge: Int,
    val shippingAddressId: String,
    val shopTypeId: String,
    val shopTypeUniqueName: String,
    val taxAmount: Double,
    val tip: Int,
    val updatedAt: String,
    val user: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(CartItem)!!,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.createTypedArrayList(OrderHistory)!!,
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeString(_id)
        parcel.writeString(billingAddressId)
        parcel.writeTypedList(cartItems)
        parcel.writeString(createdAt)
        parcel.writeString(dateOfPurchase)
        parcel.writeString(dateOfPurchaseTs)
        parcel.writeLong(deliveredAt)
        parcel.writeInt(deliveryCharge)
        parcel.writeInt(deliverySurgeCharge)
        parcel.writeString(franchiseId)
        parcel.writeDouble(grandTotal)
        parcel.writeByte(if (isAnyDeliverySurge) 1 else 0)
        parcel.writeByte(if (isConvertedToOrder) 1 else 0)
        parcel.writeByte(if (isOrderByCall) 1 else 0)
        parcel.writeByte(if (isReturnOrder) 1 else 0)
        parcel.writeDouble(itemTotal)
        parcel.writeTypedList(orderHistory)
        parcel.writeInt(orderId)
        parcel.writeString(orderStatus)
        parcel.writeInt(packingCharge)
        parcel.writeString(paymentMethod)
        parcel.writeInt(serviceCharge)
        parcel.writeString(shippingAddressId)
        parcel.writeString(shopTypeId)
        parcel.writeString(shopTypeUniqueName)
        parcel.writeDouble(taxAmount)
        parcel.writeInt(tip)
        parcel.writeString(updatedAt)
        parcel.writeString(user)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeliveredOrderItem> {
        override fun createFromParcel(parcel: Parcel): DeliveredOrderItem {
            return DeliveredOrderItem(parcel)
        }

        override fun newArray(size: Int): Array<DeliveredOrderItem?> {
            return arrayOfNulls(size)
        }
    }
}
