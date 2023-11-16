package com.iroid.emergency.modal.common.home

import android.os.Parcel
import android.os.Parcelable
import com.iroid.emergency.modal.common.location.Location


data class NewRequest(
    val new_request: NewRequestData,
    val status: Boolean,


):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(NewRequestData::class.java.classLoader)!!,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(new_request, flags)
        parcel.writeByte(if (status) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewRequest> {
        override fun createFromParcel(parcel: Parcel): NewRequest {
            return NewRequest(parcel)
        }

        override fun newArray(size: Int): Array<NewRequest?> {
            return arrayOfNulls(size)
        }
    }
}
