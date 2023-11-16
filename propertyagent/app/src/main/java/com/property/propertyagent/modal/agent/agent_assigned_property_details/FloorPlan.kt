package com.property.propertyagent.modal.agent.agent_assigned_property_details

import android.os.Parcel
import android.os.Parcelable

data class FloorPlan(
    val document_image: String,
    val id: Int,
    val owner_id: Int,
    val property_id: Int
): Parcelable {
    constructor(parcel : Parcel) : this(
        parcel.readString().toString() ,
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
    }

    override fun describeContents() : Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FloorPlan> {
        override fun createFromParcel(parcel : Parcel) : FloorPlan {
            return FloorPlan(parcel)
        }

        override fun newArray(size : Int) : Array<FloorPlan?> {
            return arrayOfNulls(size)
        }
    }
}