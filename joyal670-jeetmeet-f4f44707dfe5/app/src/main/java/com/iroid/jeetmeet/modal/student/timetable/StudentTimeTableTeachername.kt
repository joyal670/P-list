package com.iroid.jeetmeet.modal.student.timetable

import android.os.Parcel
import android.os.Parcelable

data class StudentTimeTableTeachername(
    val first_name: String,
    val id: Int,
    val last_name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(first_name)
        parcel.writeInt(id)
        parcel.writeString(last_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentTimeTableTeachername> {
        override fun createFromParcel(parcel: Parcel): StudentTimeTableTeachername {
            return StudentTimeTableTeachername(parcel)
        }

        override fun newArray(size: Int): Array<StudentTimeTableTeachername?> {
            return arrayOfNulls(size)
        }
    }
}