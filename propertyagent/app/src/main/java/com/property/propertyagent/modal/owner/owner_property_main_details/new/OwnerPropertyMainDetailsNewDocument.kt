package com.property.propertyagent.modal.owner.owner_property_main_details.new

import android.os.Parcel
import android.os.Parcelable

data class OwnerPropertyMainDetailsNewDocument(
    val document: String,
    val property_id: Int,
    val type: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(document)
        parcel.writeInt(property_id)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OwnerPropertyMainDetailsNewDocument> {
        override fun createFromParcel(parcel: Parcel): OwnerPropertyMainDetailsNewDocument {
            return OwnerPropertyMainDetailsNewDocument(parcel)
        }

        override fun newArray(size: Int): Array<OwnerPropertyMainDetailsNewDocument?> {
            return arrayOfNulls(size)
        }
    }
}