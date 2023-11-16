package com.iroid.emergency.modal.common


import android.os.Parcel
import android.os.Parcelable
import com.iroid.emergency.modal.common.faq.Faq
import com.iroid.emergency.modal.common.home.AcceptedRequest
import com.iroid.emergency.modal.common.home.NewRequest
import com.iroid.emergency.modal.common.profile.Profile

data class CommonResponse(
    val errors: List<String>,
    val status: Boolean,
    val message: String,
    val profile: Profile?,
    val accessToken:String,
    val user: User,
    val accepted_requests: List<AcceptedRequest>,
    val new_request: NewRequest,
    val rejected_requests: List<RejectedRequest>,
    val faq:List<Faq>,
    val otp_status:Boolean,
    val user_type:String,
    val started_request: List<AcceptedRequest>,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readParcelable(Profile::class.java.classLoader),
        parcel.readString().toString(),
        parcel.readParcelable(User::class.java.classLoader)!!,
        parcel.createTypedArrayList(AcceptedRequest)!!,
        parcel.readParcelable(NewRequest::class.java.classLoader)!!,
        parcel.createTypedArrayList(RejectedRequest)!!,
        parcel.createTypedArrayList(Faq)!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(AcceptedRequest)!!,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(errors)
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeString(message)
        parcel.writeParcelable(profile, flags)
        parcel.writeString(accessToken)
        parcel.writeParcelable(user, flags)
        parcel.writeTypedList(accepted_requests)
        parcel.writeParcelable(new_request, flags)
        parcel.writeTypedList(rejected_requests)
        parcel.writeTypedList(faq)
        parcel.writeByte(if (otp_status) 1 else 0)
        parcel.writeString(user_type)
        parcel.writeTypedList(started_request)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CommonResponse> {
        override fun createFromParcel(parcel: Parcel): CommonResponse {
            return CommonResponse(parcel)
        }

        override fun newArray(size: Int): Array<CommonResponse?> {
            return arrayOfNulls(size)
        }
    }
}
