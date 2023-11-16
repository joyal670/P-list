package com.iroid.jeetmeet.modal.parent.time_table

import android.os.Parcel
import android.os.Parcelable

data class ParentTimeTableSubjectname(
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

    companion object CREATOR : Parcelable.Creator<ParentTimeTableSubjectname> {
        override fun createFromParcel(parcel: Parcel): ParentTimeTableSubjectname {
            return ParentTimeTableSubjectname(parcel)
        }

        override fun newArray(size: Int): Array<ParentTimeTableSubjectname?> {
            return arrayOfNulls(size)
        }
    }
}