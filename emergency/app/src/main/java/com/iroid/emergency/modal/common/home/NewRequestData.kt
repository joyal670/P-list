package com.iroid.emergency.modal.common.home

import android.os.Parcel
import android.os.Parcelable
import com.iroid.emergency.modal.common.location.Location

data class NewRequestData(
    val _id: String,
    val address: String,
    val mobile: Long,
    val patient_name: String,
    val location: Location
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readParcelable(Location::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(address)
        parcel.writeLong(mobile)
        parcel.writeString(patient_name)
        parcel.writeParcelable(location, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewRequestData> {
        override fun createFromParcel(parcel: Parcel): NewRequestData {
            return NewRequestData(parcel)
        }

        override fun newArray(size: Int): Array<NewRequestData?> {
            return arrayOfNulls(size)
        }
    }
}
