package com.property.propertyuser.modal.property_details

import android.os.Parcel
import android.os.Parcelable

data class Document(
    val created_at: String,
    val document: String,
    val id: Int,
    val owner_id: Int,
    val property_id: Int,
    val type: Int,
    val updated_at: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(document)
        parcel.writeInt(id)
        parcel.writeInt(owner_id)
        parcel.writeInt(property_id)
        parcel.writeInt(type)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Document> {
        override fun createFromParcel(parcel: Parcel): Document {
            return Document(parcel)
        }

        override fun newArray(size: Int): Array<Document?> {
            return arrayOfNulls(size)
        }
    }
}