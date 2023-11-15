package com.proteinium.proteiniumdietapp.pojo.menu_day

import android.os.Parcel
import android.os.Parcelable

data class Food(
    var ordered_status:Boolean,
    val average_rating: Float,
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val tagline:String,
    val tags: List<Tag>,
    val fat:Int,
    val calories:Int,
    val proteins:Float,
    val carbs:Float,
    val nutrition_include:Boolean
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readFloat(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(Tag)!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readByte() != 0.toByte(),

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (ordered_status) 1 else 0)
        parcel.writeFloat(average_rating)
        parcel.writeString(description)
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(tagline)
        parcel.writeTypedList(tags)
        parcel.writeInt(fat)
        parcel.writeInt(calories)
        parcel.writeFloat(proteins)
        parcel.writeFloat(carbs)
        parcel.writeByte(if (nutrition_include) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }
}
