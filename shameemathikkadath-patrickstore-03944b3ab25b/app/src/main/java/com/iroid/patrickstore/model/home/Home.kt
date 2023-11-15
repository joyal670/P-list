package com.iroid.patrickstore.model.home

import android.os.Parcel
import android.os.Parcelable

data class Home(
    val banner: List<Banner>,
    val item: String,
    val position: Int,
    val productCategory: List<ProductCategory>,
    val shop: List<Shop>,
    val slider: List<Slider>,
    val title: String,
    val adds: List<Adds>,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Banner)!!,
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.createTypedArrayList(ProductCategory)!!,
        parcel.createTypedArrayList(Shop)!!,
        parcel.createTypedArrayList(Slider)!!,
        parcel.readString().toString(),
        parcel.createTypedArrayList(Adds)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(banner)
        parcel.writeString(item)
        parcel.writeInt(position)
        parcel.writeTypedList(productCategory)
        parcel.writeTypedList(shop)
        parcel.writeTypedList(slider)
        parcel.writeString(title)
        parcel.writeTypedList(adds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Home> {
        override fun createFromParcel(parcel: Parcel): Home {
            return Home(parcel)
        }

        override fun newArray(size: Int): Array<Home?> {
            return arrayOfNulls(size)
        }
    }
}
