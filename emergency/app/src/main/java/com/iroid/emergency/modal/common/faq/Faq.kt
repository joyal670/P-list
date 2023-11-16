package com.iroid.emergency.modal.common.faq

import android.os.Parcel
import android.os.Parcelable

data class Faq(
    val _id: String,
    val answer: String,
    val question: String,
    var isExpand:Boolean
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(answer)
        parcel.writeString(question)
        parcel.writeByte(if (isExpand) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Faq> {
        override fun createFromParcel(parcel: Parcel): Faq {
            return Faq(parcel)
        }

        override fun newArray(size: Int): Array<Faq?> {
            return arrayOfNulls(size)
        }
    }
}
