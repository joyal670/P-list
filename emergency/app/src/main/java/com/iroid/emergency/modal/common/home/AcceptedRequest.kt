package com.iroid.emergency.modal.common.home

import android.os.Parcel
import android.os.Parcelable
import com.iroid.emergency.modal.common.location.Location

data class AcceptedRequest(
    val _id: String,
    val address: String,
    val patient_id: String,
    val patient_name: String,
    val location: Location,
    val mobile: Long
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(Location::class.java.classLoader)!!,
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(address)
        parcel.writeString(patient_id)
        parcel.writeString(patient_name)
        parcel.writeParcelable(location, flags)
        parcel.writeLong(mobile)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AcceptedRequest> {
        override fun createFromParcel(parcel: Parcel): AcceptedRequest {
            return AcceptedRequest(parcel)
        }

        override fun newArray(size: Int): Array<AcceptedRequest?> {
            return arrayOfNulls(size)
        }
    }
}
