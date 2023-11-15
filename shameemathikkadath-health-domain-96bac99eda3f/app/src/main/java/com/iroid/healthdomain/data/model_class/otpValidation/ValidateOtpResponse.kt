package com.iroid.healthdomain.data.model_class.otpValidation

data class ValidateOtpResponse(
        val data: OtpData,
        val message: String,
        val status: Int
)