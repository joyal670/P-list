package com.iroid.jeetmeet.modal.parent.time_table

import android.os.Parcel
import android.os.Parcelable

data class ParentTimeTableRoomname(
    val id: Int,
    val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParentTimeTableRoomname> {
        override fun createFromParcel(parcel: Parcel): ParentTimeTableRoomname {
            return ParentTimeTableRoomname(parcel)
        }

        override fun newArray(size: Int): Array<ParentTimeTableRoomname?> {
            return arrayOfNulls(size)
        }
    }
}