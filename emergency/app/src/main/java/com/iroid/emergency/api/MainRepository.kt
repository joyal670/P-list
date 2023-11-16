package com.iroid.emergency.api

import com.iroid.emergency.modal.common.CommonResponse
import retrofit2.Response

class MainRepository(private val apiHelper: ApiService) {
    suspend fun register(name: String, mobile: Long): Response<CommonResponse> =
        apiHelper.registerCustomer(name, mobile)

    suspend fun verifyOtp(mobile: String, otp: String,fcm_token:String) = apiHelper.verifyOtp(mobile, otp,fcm_token)

    suspend fun getProfile(): Response<CommonResponse> = apiHelper.getProfile()

    suspend fun getHome(): Response<CommonResponse> = apiHelper.getHome()

    suspend fun updateEmergency(latitude: String, longitude: String): Response<CommonResponse> =
        apiHelper.updateEmergency(latitude, longitude)

    suspend fun updateEmergencyProfile(
        name: String,
        mobile: String,
        type: String
    ): Response<CommonResponse> = apiHelper.updateEmergencyProfile(name, mobile, type)

    suspend fun emergencyApproval(emergency_id: String, status: String): Response<CommonResponse> =
        apiHelper.emergencyApproval(emergency_id, status)

    suspend fun emergencyComplete(emergency_id: String): Response<CommonResponse> =
        apiHelper.emergencyComplete(emergency_id)


    suspend fun emergencySecondary(emergency_id: String): Response<CommonResponse> =
        apiHelper.emergencySecondary(emergency_id)

    suspend fun getFaq(): Response<CommonResponse> =
        apiHelper.getFaq()

    suspend fun locationUpdate(latitude:String,longitude:String):Response<CommonResponse> =
        apiHelper.locationUpdate(latitude, longitude)

    suspend fun postFeedback(rating: Float, comments: String):Response<CommonResponse> =
        apiHelper.postFeedback(rating, comments)

    suspend fun profileUpdate(name:String,mobile:String):Response<CommonResponse> =
        apiHelper.profileUpdate(name, mobile)

    suspend fun verifyOtpProfile(name:String,mobile:String,otp: String):Response<CommonResponse> =
        apiHelper.verifyOtpProfile(name, mobile,otp)
}
