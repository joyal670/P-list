package com.ncomfortsagent.data

import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.ncomfortsagent.model.add_bank_details.AgentAddBankDetailsResponse
import com.ncomfortsagent.model.building_details.AgentBuildingDetailsResponse
import com.ncomfortsagent.model.change_password.AgentChangePasswordResponse
import com.ncomfortsagent.model.edit_bank_details.AgentEditBankDetailsResponse
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryResponse
import com.ncomfortsagent.model.enquiry_change_status.AgentEnquiryChangeStatsResponse
import com.ncomfortsagent.model.enquiry_details.AgentEnquiryDetailsResponse
import com.ncomfortsagent.model.faq.AgentFaqResponse
import com.ncomfortsagent.model.feedback.AgentFeedbackResponse
import com.ncomfortsagent.model.forgot_password.AgentForgotPasswordResponse
import com.ncomfortsagent.model.home_count.AgentHomeCountResponse
import com.ncomfortsagent.model.login.AgentLoginResponse
import com.ncomfortsagent.model.logout.AgentLogoutResponse
import com.ncomfortsagent.model.profile.AgentProfileResponse
import com.ncomfortsagent.model.profile_image_remove.AgentProfileImageRemoveResponse
import com.ncomfortsagent.model.profile_update.AgentProfileUpdateResponse
import com.ncomfortsagent.model.property.PropertyNewResponse
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsResponse
import com.ncomfortsagent.model.remove_bank_details.AgentRemoveBankDetailsResponse
import com.ncomfortsagent.model.view_bank_details.AgentViewBankDetailsResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    /* API' s */

    /* Login Api */
    @FormUrlEncoded
    @POST("agent/login")
    suspend fun login(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<AgentLoginResponse>

    /* Logout Api */
    @POST("agent/logout")
    suspend fun logout(): Response<AgentLogoutResponse>

    /* Profile Api */
    @POST("agent/profile")
    suspend fun profile(): Response<AgentProfileResponse>

    /* Profile image remove Api */
    @POST("agent/remove-profile-image")
    suspend fun profileImageRemove(): Response<AgentProfileImageRemoveResponse>

    /* FAQ Api */
    @FormUrlEncoded
    @POST("agent/faq")
    suspend fun faq(
        @Field("lang") lang: String,
        @Field("page") page: String
    ): Response<AgentFaqResponse>

    /* Profile Update Api */
    @Multipart
    @POST("agent/profile/update")
    suspend fun profileUpdate(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<AgentProfileUpdateResponse>

    /* Property Details Api */
    @FormUrlEncoded
    @POST("agent/agentpropertydetails")
    suspend fun propertyDetails(@Field("property_id") property_id: Int): Response<AgentPropertyDetailsResponse>

    /* Enquiry Api */
    @FormUrlEncoded
    @POST("agent/enquiry-properties")
    suspend fun enquiry(
        @Field("page") page: String,
        @Field("enquiry_status") enquiry_status: Int
    ): Response<AgentHomeEnquiryResponse>

    /* Add Bank Details Api */
    @FormUrlEncoded
    @POST("agent/bankdetails/add")
    suspend fun addBankDetails(
        @Field("account_name") account_name: String,
        @Field("account_number") account_number: String,
        @Field("bank_name") bank_name: String,
        @Field("branch_name") branch_name: String,
        @Field("ifsc") ifsc: String
    ): Response<AgentAddBankDetailsResponse>

    /* Forgot Password Api */
    @FormUrlEncoded
    @POST("agent/reset-password-phone")
    suspend fun forgotPassword(@Field("phone") phone: String): Response<AgentForgotPasswordResponse>

    /* Change Password Api */
    @FormUrlEncoded
    @POST("agent/changepassword")
    suspend fun changePassword(
        @Field("current_password") current_password: String,
        @Field("new_password") new_password: String,
        @Field("confirm_new_password") confirm_new_password: String
    ): Response<AgentChangePasswordResponse>

    /* View Bank Details Api */
    @FormUrlEncoded
    @POST("agent/bankdetails")
    suspend fun viewBankDetails(@Field("page") page: String): Response<AgentViewBankDetailsResponse>

    /* Edit Bank Details Api */
    @FormUrlEncoded
    @POST("agent/bankdetails/update")
    suspend fun editBankDetails(
        @Field("account_name") account_name: String,
        @Field("account_number") account_number: String,
        @Field("bank_name") bank_name: String,
        @Field("branch_name") branch_name: String,
        @Field("ifsc") ifsc: String,
        @Field("table_id") table_id: String
    ): Response<AgentEditBankDetailsResponse>

    /* Remove Bank Details Api */
    @FormUrlEncoded
    @POST("agent/bankdetails/remove")
    suspend fun removeBankDetails(@Field("table_id") table_id: String): Response<AgentRemoveBankDetailsResponse>

    /* Enquiry Details Api */
    @FormUrlEncoded
    @POST("agent/enquiry-details")
    suspend fun enquiryDetails(@Field("enquiry_id") enquiry_id: String): Response<AgentEnquiryDetailsResponse>

    /* Home Count */
    @GET("agent/enquiry-count")
    suspend fun homeCount(): Response<AgentHomeCountResponse>

    /* My Property Search Api */
    @FormUrlEncoded
    @POST("agent/agentproperty")
    suspend fun myPropertySearch(
        @Field("property_name") property_name: String,
        @Field("page") page: String
    ): Response<PropertyNewResponse>

    /* Feedback Api */
    @FormUrlEncoded
    @POST("agent/send-feedback")
    suspend fun feedback(@Field("feedback") feedback: String): Response<AgentFeedbackResponse>

    /* My Property Api */
    @FormUrlEncoded
    @POST("agent/agentproperty")
    suspend fun myProperty(@Field("page") page: String): Response<PropertyNewResponse>

    /* Building Details Api */
    @FormUrlEncoded
    @POST("agent/building-details")
    suspend fun buildingDetails(@Field("property_id") property_id: String): Response<AgentBuildingDetailsResponse>

    /* Enquiry Change Status */
    @Multipart
    @POST("agent/changes-status")
    suspend fun enquiryChangeStatus(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<AgentEnquiryChangeStatsResponse>


    /* retrofit builder */
    companion object {
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl("http://ncomfort.iroidtechnologies.in/api/")
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