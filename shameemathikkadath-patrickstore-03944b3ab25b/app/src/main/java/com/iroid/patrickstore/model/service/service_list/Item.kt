package com.iroid.patrickstore.model.service.service_list

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Item(
    val __v: Int,
    val description: String,
    val id: String,
    val imgUrl: List<ImgUrl>,
    val maxPrice: Double,
    val minPrice: Double,
    val model: String,
    val sellerId: SellerId,
    val serviceName: String,
    val serviceTypeId: ServiceTypeId,
    val shopDiscount: Int,
    val tag: String,
    val time: String
):Parcelable,Serializable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(ImgUrl)!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readParcelable(SellerId::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.readParcelable(ServiceTypeId::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeString(description)
        parcel.writeString(id)
        parcel.writeTypedList(imgUrl)
        parcel.writeDouble(maxPrice)
        parcel.writeDouble(minPrice)
        parcel.writeString(model)
        parcel.writeParcelable(sellerId, flags)
        parcel.writeString(serviceName)
        parcel.writeParcelable(serviceTypeId, flags)
        parcel.writeInt(shopDiscount)
        parcel.writeString(tag)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}
