package com.ncomfortsagent.data

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
import okhttp3.RequestBody
import retrofit2.Response

class MainRepository(private val apiHelper: ApiService) {

    /* Login Api */
    suspend fun login(phone: String, password: String): Response<AgentLoginResponse> =
        apiHelper.login(phone, password)

    /* logout Api */
    suspend fun logout(): Response<AgentLogoutResponse> = apiHelper.logout()

    /* Profile Api */
    suspend fun profile(): Response<AgentProfileResponse> = apiHelper.profile()

    /* Profile image remove Api */
    suspend fun profileImageRemove(): Response<AgentProfileImageRemoveResponse> =
        apiHelper.profileImageRemove()

    /* FAQ Api */
    suspend fun faq(lang: String, page: String): Response<AgentFaqResponse> =
        apiHelper.faq(lang, page)

    /* Profile Update Api */
    suspend fun profileUpdate(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>,
    ): Response<AgentProfileUpdateResponse> = apiHelper.profileUpdate(params, images)

    /* Property Details Api */
    suspend fun propertyDetails(property_id: Int): Response<AgentPropertyDetailsResponse> =
        apiHelper.propertyDetails(property_id)

    /* Enquiry Api */
    suspend fun enquiry(page: String, enquiryStatus:Int): Response<AgentHomeEnquiryResponse> = apiHelper.enquiry(page,enquiryStatus)

    /* Add Bank Details Api */
    suspend fun addBankAccount(
        account_name: String,
        account_number: String,
        bank_name: String,
        branch_name: String,
        ifsc: String
    ): Response<AgentAddBankDetailsResponse> =
        apiHelper.addBankDetails(account_name, account_number, bank_name, branch_name, ifsc)

    /* Forgot Password Api */
    suspend fun forgotPassword(phone: String): Response<AgentForgotPasswordResponse> =
        apiHelper.forgotPassword(phone)

    /* Change Password Api */
    suspend fun changePassword(
        current_password: String,
        new_password: String,
        confirm_new_password: String
    ): Response<AgentChangePasswordResponse> =
        apiHelper.changePassword(current_password, new_password, confirm_new_password)

    /* View Bank Details Api */
    suspend fun viewBankDetails(page: String): Response<AgentViewBankDetailsResponse> =
        apiHelper.viewBankDetails(page)

    /* Edit Bank Details Api */
    suspend fun editBankDetails(
        account_name: String,
        account_number: String,
        bank_name: String,
        branch_name: String,
        ifsc: String,
        table_id: String
    ): Response<AgentEditBankDetailsResponse> =
        apiHelper.editBankDetails(
            account_name,
            account_number,
            bank_name,
            branch_name,
            ifsc,
            table_id
        )

    /* Remove Bank Details Api */
    suspend fun removeBankDetails(table_id: String): Response<AgentRemoveBankDetailsResponse> =
        apiHelper.removeBankDetails(table_id)

    /* Enquiry Details Api */
    suspend fun enquiryDetails(enquiry_id: String): Response<AgentEnquiryDetailsResponse> =
        apiHelper.enquiryDetails(enquiry_id)


    /* Home Count */
    suspend fun homeCount(): Response<AgentHomeCountResponse> = apiHelper.homeCount()

    /* My Property Search Api */
    suspend fun myPropertySearch(
        property_name: String,
        page: String
    ): Response<PropertyNewResponse> =
        apiHelper.myPropertySearch(property_name, page)

    /* Feedback Api */
    suspend fun feedback(feedback: String): Response<AgentFeedbackResponse> =
        apiHelper.feedback(feedback)

    /* My Property Api */
    suspend fun myProperty(page: String): Response<PropertyNewResponse> =
        apiHelper.myProperty(page)

    /* Building Details Api */
    suspend fun buildingDetails(property_id: String): Response<AgentBuildingDetailsResponse> =
        apiHelper.buildingDetails(property_id)

    /* Enquiry Change Status */
    suspend fun enquiryChangeStatus(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>
    ): Response<AgentEnquiryChangeStatsResponse> =
        apiHelper.enquiryChangeStatus(params, images)
}