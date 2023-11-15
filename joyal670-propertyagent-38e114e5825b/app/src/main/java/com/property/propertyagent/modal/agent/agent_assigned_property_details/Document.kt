package com.property.propertyagent.modal.agent.agent_assigned_property_details

import android.os.Parcel
import android.os.Parcelable

data class Document(
    val document_image: String,
    val id: Int,
    val owner_id: Int,
    val property_id: Int,
    val type: Int
): Parcelable {
    constructor(parcel : Parcel) : this(
        parcel.readString().toString() ,
        parcel.readInt() ,
        parcel.readInt() ,
        parcel.readInt() ,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel : Parcel , flags : Int) {
        parcel.writeString(document_image)
        parcel.writeInt(id)
        parcel.writeInt(owner_id)
        parcel.writeInt(property_id)
        parcel.writeInt(type)
    }

    override fun describeContents() : Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Document> {
        override fun createFromParcel(parcel : Parcel) : Document {
            return Document(parcel)
        }

        override fun newArray(size : Int) : Array<Document?> {
            return arrayOfNulls(size)
        }
    }
}