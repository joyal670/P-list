package com.property.propertyagent.modal.agent.agent_assigned_property_details

import android.os.Parcel
import android.os.Parcelable

data class AmenityDetail(
    val id: Int,
    val image: String,
    val name: String
): Parcelable {
    constructor(parcel : Parcel) : this(
        parcel.readInt() ,
        parcel.readString().toString() ,
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel : Parcel , flags : Int) {
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(name)
    }

    override fun describeContents() : Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AmenityDetail> {
        override fun createFromParcel(parcel : Parcel) : AmenityDetail {
            return AmenityDetail(parcel)
        }

        override fun newArray(size : Int) : Array<AmenityDetail?> {
            return arrayOfNulls(size)
        }
    }
}