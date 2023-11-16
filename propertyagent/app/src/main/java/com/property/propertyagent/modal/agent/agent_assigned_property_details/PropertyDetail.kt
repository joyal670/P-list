package com.property.propertyagent.modal.agent.agent_assigned_property_details

import android.os.Parcel
import android.os.Parcelable

data class PropertyDetail(
    val detail_id: Int,
    val laravel_through_key: Int,
    val name: String,
    val property_id: Int,
    val value: Int
): Parcelable {
    constructor(parcel : Parcel) : this(
        parcel.readInt() ,
        parcel.readInt() ,
        parcel.readString().toString() ,
        parcel.readInt() ,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel : Parcel , flags : Int) {
        parcel.writeInt(detail_id)
        parcel.writeInt(laravel_through_key)
        parcel.writeString(name)
        parcel.writeInt(property_id)
        parcel.writeInt(value)
    }

    override fun describeContents() : Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PropertyDetail> {
        override fun createFromParcel(parcel : Parcel) : PropertyDetail {
            return PropertyDetail(parcel)
        }

        override fun newArray(size : Int) : Array<PropertyDetail?> {
            return arrayOfNulls(size)
        }
    }
}