package com.iroid.jeetmeet.modal.student.timetable

import android.os.Parcel
import android.os.Parcelable

data class StudentTimeTableX1(
    val day: String,
    val id: Int,
    val room: Int,
    val roomname: StudentTimeTableRoomname,
    val slot: Int,
    val subject: Int,
    val subjectname: StudentTimeTableSubjectname,
    val teacher: Int,
    val teachername: StudentTimeTableTeachername
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(StudentTimeTableRoomname::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(StudentTimeTableSubjectname::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readParcelable(StudentTimeTableTeachername::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(day)
        parcel.writeInt(id)
        parcel.writeInt(room)
        parcel.writeParcelable(roomname, flags)
        parcel.writeInt(slot)
        parcel.writeInt(subject)
        parcel.writeParcelable(subjectname, flags)
        parcel.writeInt(teacher)
        parcel.writeParcelable(teachername, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentTimeTableX1> {
        override fun createFromParcel(parcel: Parcel): StudentTimeTableX1 {
            return StudentTimeTableX1(parcel)
        }

        override fun newArray(size: Int): Array<StudentTimeTableX1?> {
            return arrayOfNulls(size)
        }
    }
}