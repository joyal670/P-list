package com.iroid.patrickstore.model.seller

import android.os.Parcel
import android.os.Parcelable

data class SingleSeller(
    val __v: Int,
    val accountNo: String,
    val accountType: String,
    val addressDetails: AddressDetails,
    val businessType: String,
    val email: String,
    val endTime: String,
    val franchiseId: String,
    val fssai: String,
    val gstNo: String,
    val id: String,
    val idProof: String,
    val ifsc: String,
    val isAproved: Boolean,
    val isEnabled: Boolean,
    val isPaymentDone: Boolean,
    val isVerified: Boolean,
    val lat: Double,
    val lng: Double,
    val mobile: String,
    val nameInBank: String,
    val panNo: String,
    val passwordHash: String,
    val profileImageId: ProfileImageId,
    val sellerName: String,
    val sellingCategory: List<SellingCategory>,
    val startTime: String,
    val storeName: String,
    val storeStatus: String,
    val sellerBannerImageId:SellerBanner,
    val description:String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(AddressDetails::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(ProfileImageId::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.createTypedArrayList(SellingCategory)!!,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(SellerBanner::class.java.classLoader)!!,
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeString(accountNo)
        parcel.writeString(accountType)
        parcel.writeParcelable(addressDetails, flags)
        parcel.writeString(businessType)
        parcel.writeString(email)
        parcel.writeString(endTime)
        parcel.writeString(franchiseId)
        parcel.writeString(fssai)
        parcel.writeString(gstNo)
        parcel.writeString(id)
        parcel.writeString(idProof)
        parcel.writeString(ifsc)
        parcel.writeByte(if (isAproved) 1 else 0)
        parcel.writeByte(if (isEnabled) 1 else 0)
        parcel.writeByte(if (isPaymentDone) 1 else 0)
        parcel.writeByte(if (isVerified) 1 else 0)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
        parcel.writeString(mobile)
        parcel.writeString(nameInBank)
        parcel.writeString(panNo)
        parcel.writeString(passwordHash)
        parcel.writeParcelable(profileImageId,flags)
        parcel.writeString(sellerName)
        parcel.writeTypedList(sellingCategory)
        parcel.writeString(startTime)
        parcel.writeString(storeName)
        parcel.writeString(storeStatus)
        parcel.writeParcelable(sellerBannerImageId,flags)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SingleSeller> {
        override fun createFromParcel(parcel: Parcel): SingleSeller {
            return SingleSeller(parcel)
        }

        override fun newArray(size: Int): Array<SingleSeller?> {
            return arrayOfNulls(size)
        }
    }
}
