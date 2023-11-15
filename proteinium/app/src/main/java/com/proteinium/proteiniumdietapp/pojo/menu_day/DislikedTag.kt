package com.proteinium.proteiniumdietapp.pojo.menu_day

import android.os.Parcel
import android.os.Parcelable

data class DislikedTag(
    val tag_id: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(tag_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DislikedTag> {
        override fun createFromParcel(parcel: Parcel): DislikedTag {
            return DislikedTag(parcel)
        }

        override fun newArray(size: Int): Array<DislikedTag?> {
            return arrayOfNulls(size)
        }
    }

}