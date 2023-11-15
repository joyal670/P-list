package com.iroid.jeetmeet.modal.student.timetable

import android.os.Parcel
import android.os.Parcelable

data class StudentTimeTableSubjectname(
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

    companion object CREATOR : Parcelable.Creator<StudentTimeTableSubjectname> {
        override fun createFromParcel(parcel: Parcel): StudentTimeTableSubjectname {
            return StudentTimeTableSubjectname(parcel)
        }

        override fun newArray(size: Int): Array<StudentTimeTableSubjectname?> {
            return arrayOfNulls(size)
        }
    }
}