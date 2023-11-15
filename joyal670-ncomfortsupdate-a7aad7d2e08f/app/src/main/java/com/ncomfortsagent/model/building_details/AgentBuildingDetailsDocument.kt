package com.ncomfortsagent.model.building_details

import android.os.Parcel
import android.os.Parcelable

data class AgentBuildingDetailsDocument(
    val document: String,
    val property_id: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(document)
        parcel.writeInt(property_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AgentBuildingDetailsDocument> {
        override fun createFromParcel(parcel: Parcel): AgentBuildingDetailsDocument {
            return AgentBuildingDetailsDocument(parcel)
        }

        override fun newArray(size: Int): Array<AgentBuildingDetailsDocument?> {
            return arrayOfNulls(size)
        }
    }
}