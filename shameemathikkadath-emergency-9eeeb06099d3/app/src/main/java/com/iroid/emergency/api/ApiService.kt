package com.iroid.emergency.api

import com.iroid.emergency.modal.common.CommonResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun registerCustomer(
        @Field("name") name: String,
        @Field("mobile") mobile: Number
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("register/verify-otp")
    suspend fun verifyOtp(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String,
        @Field("fcm_token") fcm_token: String
    ): Response<CommonResponse>

    @GET("profile")
    suspend fun getProfile(): Response<CommonResponse>

    @GET("home")
    suspend fun getHome(): Response<CommonResponse>

    @FormUrlEncoded
    @POST("emergency")
    suspend fun updateEmergency(
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("update-user")
    suspend fun updateEmergencyProfile(
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("type") type: String
    ): Response<CommonResponse>


    @FormUrlEncoded
    @POST("emergency/approval")
    suspend fun emergencyApproval(
        @Field("emergency_id") emergency_id: String,
        @Field("status") status: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("emergency/complete")
    suspend fun emergencyComplete(@Field("emergency_id") emergency_id: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("emergency/secondary")
    suspend fun emergencySecondary(@Field("emergency_id") emergency_id: String): Response<CommonResponse>

    @GET("faq")
    suspend fun getFaq(): Response<CommonResponse>

    @FormUrlEncoded
    @POST("location/update")
    suspend fun locationUpdate(
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("feedback")
    suspend fun postFeedback(
        @Field("rating") rating: Float,
        @Field("comments") comments: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("profile/update")
    suspend fun profileUpdate(
        @Field("name") name: String,
        @Field("mobile") mobile: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("profile/verify-otp")
    suspend fun verifyOtpProfile(
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("otp")otp: String
    ): Response<CommonResponse>


    companion object {
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl("https://emergency.iroidtechnologies.in/api/")
                .build()
            return retrofit.create(ApiService::class.java)

        }

        private fun initializeClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            System.setProperty("http.keepAlive", "false")
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
                .addInterceptor(SupportInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)

            return builder.build()
        }
    }
}
