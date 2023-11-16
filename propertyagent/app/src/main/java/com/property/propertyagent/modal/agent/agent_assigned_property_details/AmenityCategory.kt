package com.property.propertyagent.modal.agent.agent_assigned_property_details

import android.os.Parcel
import android.os.Parcelable

data class AmenityCategory(
    var amenity_details: List<AmenityDetail>,
    val id: Int,
    val name: String
): Parcelable {
    constructor(parcel : Parcel) : this(
        parcel.createTypedArrayList(AmenityDetail) as ArrayList ,
        parcel.readInt() ,
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel : Parcel , flags : Int) {
        parcel.writeTypedList(amenity_details)
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents() : Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AmenityCategory> {
        override fun createFromParcel(parcel : Parcel) : AmenityCategory {
            return AmenityCategory(parcel)
        }

        override fun newArray(size : Int) : Array<AmenityCategory?> {
            return arrayOfNulls(size)
        }
    }
}