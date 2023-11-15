package com.iroid.patrickstore.model.home


import android.os.Parcel
import android.os.Parcelable
import com.iroid.patrickstore.model.seller.SingleSeller

data class Shop(
    val __v: Int,
    val _id: String,
    val adminCommissionPercentage: Int,
    val createdAt: String,
    val franchiseCommissionPercentage: Int,
    val isPerishable: Boolean,
    val name: String,
    val parentId: String,
    val shops: List<SingleSeller>,
    val uniqueName: String,
    val updatedAt: String,
    val description:String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(SingleSeller)!!,
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
        parcel.writeByte(if (isPerishable) 1 else 0)
        parcel.writeString(name)
        parcel.writeString(parentId)
        parcel.writeTypedList(shops)
        parcel.writeString(uniqueName)
        parcel.writeString(updatedAt)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Shop> {
        override fun createFromParcel(parcel: Parcel): Shop {
            return Shop(parcel)
        }

        override fun newArray(size: Int): Array<Shop?> {
            return arrayOfNulls(size)
        }
    }
}
