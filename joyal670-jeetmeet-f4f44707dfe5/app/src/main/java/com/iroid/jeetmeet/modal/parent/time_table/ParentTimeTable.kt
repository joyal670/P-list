package com.iroid.jeetmeet.modal.parent.time_table

import android.os.Parcel
import android.os.Parcelable

data class ParentTimeTable(
    val day: String,
    val id: Int,
    val room: Int,
    val roomname: ParentTimeTableRoomname,
    val slot: Int,
    val subject: Int,
    val subjectname: ParentTimeTableSubjectname,
    val teacher: Int,
    val teachername: ParentTimeTableTeachername
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(ParentTimeTableRoomname::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(ParentTimeTableSubjectname::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readParcelable(ParentTimeTableTeachername::class.java.classLoader)!!
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

    companion object CREATOR : Parcelable.Creator<ParentTimeTable> {
        override fun createFromParcel(parcel: Parcel): ParentTimeTable {
            return ParentTimeTable(parcel)
        }

        override fun newArray(size: Int): Array<ParentTimeTable?> {
            return arrayOfNulls(size)
        }
    }
}