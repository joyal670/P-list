package com.iroid.patrickstore.model.service.service_list

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class ServiceTypeId(
    val _id: String,
    val name: String
):Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ServiceTypeId> {
        override fun createFromParcel(parcel: Parcel): ServiceTypeId {
            return ServiceTypeId(parcel)
        }

        override fun newArray(size: Int): Array<ServiceTypeId?> {
            return arrayOfNulls(size)
        }
    }
}
