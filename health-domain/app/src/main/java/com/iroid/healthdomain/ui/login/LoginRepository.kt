package com.iroid.healthdomain.ui.login

import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import com.iroid.healthdomain.ui.base.BaseRepository

class LoginRepository(
        private val api: ApiServices,
        private val preferences: UserPreferences? = null) : BaseRepository() {

    suspend fun apiToGenerate(number: String) = safeApiCall {
        api.generateOtp(number, "91")
    }

    suspend fun validateOtp(otpValue: String, phone: String) = safeApiCall {
        api.validateOtp(otpValue = otpValue, phone = phone, code = "91")
    }

    suspend fun saveAuthToken(token: String) {
        preferences?.saveAuthToken(token)
    }

    suspend fun saveNumber(number: String) {
        preferences?.savePhoneNumber(number)
    }

    suspend fun reSendOtp(number: String) = safeApiCall{
        api.resendOtp(number , "91")
    }

    suspend fun saveUserStatus(value: Boolean) {
        preferences?.saveUserStatus(value)
    }

}