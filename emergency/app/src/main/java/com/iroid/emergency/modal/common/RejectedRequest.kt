package com.iroid.emergency.modal.common

import android.os.Parcel
import android.os.Parcelable

data class RejectedRequest(
    val _id: String,
    val address: String,
    val patient_id: String,
    val patient_name: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(address)
        parcel.writeString(patient_id)
        parcel.writeString(patient_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RejectedRequest> {
        override fun createFromParcel(parcel: Parcel): RejectedRequest {
            return RejectedRequest(parcel)
        }

        override fun newArray(size: Int): Array<RejectedRequest?> {
            return arrayOfNulls(size)
        }
    }
}
