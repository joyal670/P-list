package com.property.propertyagent.data

import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_assigned_property_details.AgentAssignedPropertyDetailsResponse
import com.property.propertyagent.modal.agent.agent_assigned_property_list.AgentAssignedPropertyListResponse
import com.property.propertyagent.modal.agent.agent_builder_details.AgentBuilderDetailsResponse
import com.property.propertyagent.modal.agent.agent_calender_task_count.AgentCalenderTaskCountResponse
import com.property.propertyagent.modal.agent.agent_calender_task_list.AgentCalenderTaskListResponse
import com.property.propertyagent.modal.agent.agent_cash_in_hand.AgentCashInHandTotalResponse
import com.property.propertyagent.modal.agent.agent_cash_in_hand.CashInHandListResponse
import com.property.propertyagent.modal.agent.agent_common_contract.AgentCommonContractResponse
import com.property.propertyagent.modal.agent.agent_feedback.AgentFeedBackResponse
import com.property.propertyagent.modal.agent.agent_home.AgentHomeResponse
import com.property.propertyagent.modal.agent.agent_my_request_list.AgentMyRequestListResponse
import com.property.propertyagent.modal.agent.agent_notification_list.AgentNotificationActionResponse
import com.property.propertyagent.modal.agent.agent_notification_list.AgentNotificationListResponse
import com.property.propertyagent.modal.agent.agent_owner_completed_list.AgentOwnerCompletedListResponse
import com.property.propertyagent.modal.agent.agent_owner_ongoing_list.AgentOwnerOngoingListResponse
import com.property.propertyagent.modal.agent.agent_package_details.AgentPackageDetailsResponse
import com.property.propertyagent.modal.agent.agent_pending_amount.AgentFetchPendingAmountResponse
import com.property.propertyagent.modal.agent.agent_pending_task_list.AgentPendingTaskListResponse
import com.property.propertyagent.modal.agent.agent_proceed_book_details.AgentProceedBookDetails
import com.property.propertyagent.modal.agent.agent_profile.AgentProfileDetailsResponse
import com.property.propertyagent.modal.agent.agent_property_appointment_start_tour.AgentPropertyAppointmentResponse
import com.property.propertyagent.modal.agent.agent_property_pdf.AgentPropertyPdfResponse
import com.property.propertyagent.modal.agent.agent_property_rent_details.AgentPropertyRentDetailsResponse
import com.property.propertyagent.modal.agent.agent_property_sale_details.AgentPropertySaleDetailsResponse
import com.property.propertyagent.modal.agent.agent_start_owner_tour_property_details.AgentStartOwnerTourPropertyDetailsResponse
import com.property.propertyagent.modal.agent.agent_sub_package_list.AgentSubPackageList
import com.property.propertyagent.modal.agent.agent_terms_of_stay.TermsOfStayResponse
import com.property.propertyagent.modal.agent.agent_user_booking_property_completed.AgentUserBookingPropertyCompletedResponse
import com.property.propertyagent.modal.agent.agent_user_booking_property_ongoing.AgentUserBookingPropertyOngoingResponse
import com.property.propertyagent.modal.agent.agent_user_booking_property_view_details.AgentUserBookingPropertyViewDetailsResponse
import com.property.propertyagent.modal.agent.home_location_tour.AgentTourLocationViewResponse
import com.property.propertyagent.modal.agent.my_earnings.MyEarningsListResponse
import com.property.propertyagent.modal.agent.my_earnings.TotalEarningsResponse
import com.property.propertyagent.modal.commen.forgot_password.ForgotPasswordResponse
import com.property.propertyagent.modal.commen.login.LoginResponse
import com.property.propertyagent.modal.commen.logout.LogoutResponse
import com.property.propertyagent.modal.owner.owner_add_apartment.OwnerAddApartmentResponse
import com.property.propertyagent.modal.owner.owner_addproperty.OwnerAddPropertyResponse
import com.property.propertyagent.modal.owner.owner_amenities.new.AmenitiesNewResponse
import com.property.propertyagent.modal.owner.owner_building_details.OwnerBuildingDetailsListResponse
import com.property.propertyagent.modal.owner.owner_change_password.OwnerChangePasswordReponse
import com.property.propertyagent.modal.owner.owner_city.CityResponce
import com.property.propertyagent.modal.owner.owner_common_report.OwnerCommonPdfReportResponse
import com.property.propertyagent.modal.owner.owner_country.CountryResponse
import com.property.propertyagent.modal.owner.owner_home_monthly_account.OwnerHomeMonthlyAccountResponse
import com.property.propertyagent.modal.owner.owner_home_property.OwnerHomePropertyDetailsResponse
import com.property.propertyagent.modal.owner.owner_home_property_list.OwnerHomePropertyListResponse
import com.property.propertyagent.modal.owner.owner_home_statistics.OwnerHomeStatisticsResponse
import com.property.propertyagent.modal.owner.owner_list_requested_services.OwnerListRequestedServicesResponse
import com.property.propertyagent.modal.owner.owner_maintance.OwnerMaintenanceServiceResponse
import com.property.propertyagent.modal.owner.owner_maintance_status.OwnerMaintanceStatusResponse
import com.property.propertyagent.modal.owner.owner_notification.OwnerNotificationResponse
import com.property.propertyagent.modal.owner.owner_notification_update.OwnerNotificationStatusUpdateResponse
import com.property.propertyagent.modal.owner.owner_payment_history_details.OwnerPaymentHistoryDetailsResponse
import com.property.propertyagent.modal.owner.owner_payment_history_list.OwnerPaymentHistoryListResponse
import com.property.propertyagent.modal.owner.owner_payment_list_of_properties.OwnerPaymentListOfPropertiesResponse
import com.property.propertyagent.modal.owner.owner_payment_recevied.OwnerPaymentReceviedResponse
import com.property.propertyagent.modal.owner.owner_profile.Owner_ProfileResponse
import com.property.propertyagent.modal.owner.owner_properties_for_service.OwnerPropertiesForServiceResponse
import com.property.propertyagent.modal.owner.owner_property_listing.OwnerPropertyListResponse
import com.property.propertyagent.modal.owner.owner_property_main_details.new.OwnerPropertyMainDetailsNewResponse
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingResponse
import com.property.propertyagent.modal.owner.owner_property_payment_details.PropertyPaymentDetailsResponse
import com.property.propertyagent.modal.owner.owner_property_types.OwnerPropertyTypesResponse
import com.property.propertyagent.modal.owner.owner_propertytype_list.OwnerPropertyTypeResponse
import com.property.propertyagent.modal.owner.owner_request_service_details.OwnerRequestedServiceDetailsResponse
import com.property.propertyagent.modal.owner.owner_request_service_for_approval_list.OwnerRequestServiceForApprovalListResponse
import com.property.propertyagent.modal.owner.owner_states.StatesResponce
import com.property.propertyagent.modal.owner.owner_submit_for_verification.OwnerSubmitForVerification
import com.property.propertyagent.modal.owner.owner_zipcode.ZipcodeResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

interface ApiService {

    /* login */
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("type") type: String,
        @Field("device_key") device_token: String
    ): Response<LoginResponse>

    /* forgot password
     common for both Agent and Owner*/
    @FormUrlEncoded
    @POST("reset-password-mail")
    suspend fun forgotPassword(
        @Field("email") email: String,
        @Field("user_type") user_type: String,
    ): Response<ForgotPasswordResponse>

    /* logout */
    @GET("owner/logout")
    suspend fun logout(): Response<LogoutResponse>

    /* owner add property types */
    @GET("type-list")
    suspend fun getPropertyTypes(@Query("category") category: Int): Response<OwnerPropertyTypeResponse>

    /* owner add property state list */
    @GET("states")
    suspend fun getStates(): Response<StatesResponce>

    @GET("states")
    suspend fun getStates(@Query("country") country: Int): Response<StatesResponce>

    /* owner add property city list */
    @GET("cities")
    suspend fun getCity(@Query("state") state: Int): Response<CityResponce>

    /* owner add property country list */
    @GET("countries")
    suspend fun getCountry(): Response<CountryResponse>

    /* owner add property zip code list */
    @GET("zipcodes")
    suspend fun getZipCode(@Query("city") city: Int): Response<ZipcodeResponse>

    /* owner add property amenieties list */
    @GET("available-amenities")
    suspend fun getNewAmenities(@Query("typeId") type: Int): Response<AmenitiesNewResponse>

    /* owner maintenance list */
    @POST("owner/maintenance-list")
    suspend fun getMaintance(@Query("page") page: String): Response<OwnerMaintenanceServiceResponse>

    /* owner profile update */
    @Multipart
    @POST("owner/profile-update")
    suspend fun ownerProfile(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>,
    ): Response<Owner_ProfileResponse>

    /* owner profile details */
    @GET("owner/profile")
    suspend fun getOwnerProfileDetails(): Response<Owner_ProfileResponse>

    /* owner property list */
    @GET("owner/propertylist")
    suspend fun getProperties(): Response<OwnerPropertyListResponse>

    //owner submitting property for verification
    @FormUrlEncoded
    @POST("owner/properties-for-service")
    suspend fun ownerPropertiesForService(@Field("service_id") service_id: Int): Response<OwnerPropertiesForServiceResponse>

    //owner requesting for service
    @Multipart
    @POST("owner/request-service")
    suspend fun ownerRequestService(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: List<MultipartBody.Part>,
    ): Response<CommonResponse>

    //owner listing request services
    @POST("owner/list-requested-service")
    suspend fun ownerRequestedServiceList(
        @Query("page") page: String,
    ): Response<OwnerListRequestedServicesResponse>

    //owner request services details
    @POST("owner/requested-service")
    suspend fun ownerRequestedServiceDetails(
        @Query("request_id") request_id: String,
    ): Response<OwnerRequestedServiceDetailsResponse>

    //Owner pay for Requested service
    @Multipart
    @POST("owner/owner-service-paymentbill")
    suspend fun ownerServicePaymentBill(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: MultipartBody.Part,
    ): Response<CommonResponse>

    //Sending cancel request for requested service
    @Multipart
    @POST("owner/cancel-service-request")
    suspend fun cancelServiceRequest(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<CommonResponse>

    //List request service list for approval
    @POST("owner/requested-service-for-approval")
    suspend fun ownerRequestedServiceForApprovalList(
        @Query("page") page: String,
    ): Response<OwnerRequestServiceForApprovalListResponse>

    //Accept or approve requested service
    @Multipart
    @POST("owner/accept-requested-service")
    suspend fun ownerAcceptRequestedService(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<CommonResponse>

    //Reject or deny requested service
    @Multipart
    @POST("owner/reject-requested-service")
    suspend fun ownerRejectRequestedService(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<CommonResponse>

    /* owner payment list of properties */
    @FormUrlEncoded
    @POST("owner/list-property-payment")
    suspend fun ownerPaymentListOfProperties(
        @Field("property_to") property_to: Int,
        @Field("page") page: String,
    ): Response<OwnerPaymentListOfPropertiesResponse>

    /* owner property payment details */
    @FormUrlEncoded
    @POST("owner/property-payment-details")
    suspend fun ownerPropertyPaymentDetails(@Field("property_id") property_id: Int): Response<PropertyPaymentDetailsResponse>

    /* owner property payment history list */
    @FormUrlEncoded
    @POST("owner/list-payment-history")
    suspend fun ownerPropertyHistoryList(
        @Field("property_id") property_id: Int,
        @Field("page") page: String,
    ): Response<OwnerPaymentHistoryListResponse>

    /* owner payment history details */
    @FormUrlEncoded
    @POST("owner/payment-history-details")
    suspend fun ownerPaymentHistoryDetails(
        @Field("payment_id") payment_id: Int,
        @Field("property_id") property_id: Int,
    ): Response<OwnerPaymentHistoryDetailsResponse>

    /* owner home statistics */
    @FormUrlEncoded
    @POST("owner/yearly-accounts")
    suspend fun ownerHomeStatistics(@Field("year") year: String): Response<OwnerHomeStatisticsResponse>

    /* owner home property list */
    @POST("owner/list-accounts-filter-property")
    suspend fun ownerHomePropertyList(): Response<OwnerHomePropertyListResponse>

    /* owner home monthly account */
    @FormUrlEncoded
    @POST("owner/monthly-accounts")
    suspend fun ownerHomeMonthlyAccount(
        @Field("property_id") property_id: Int,
        @Field("date") date: String,
    ): Response<OwnerHomeMonthlyAccountResponse>

    /* owner payment received */
    @FormUrlEncoded
    @POST("owner/payment-received")
    suspend fun ownerPaymentReceived(
        @Field("property_id") property_id: Int,
        @Field("payment_id") payment_id: Int,
    ): Response<OwnerPaymentReceviedResponse>

    /* owner maintenance status */
    @FormUrlEncoded
    @POST("owner/list-requested-service")
    suspend fun ownerMaintenanceStatus(@Field("page") page: String): Response<OwnerMaintanceStatusResponse>

    /* owner home property search */
    @FormUrlEncoded
    @POST("owner/propertylist")
    suspend fun ownerHomePropertySearch(
        @Field("page") page: String,
        @Field("property_name") property_name: String,
    ): Response<OwnerPropertyMainListingResponse>

    /* owner home property details */
    @POST("owner/owner-home-count")
    suspend fun ownerHomePropertyDetails(): Response<OwnerHomePropertyDetailsResponse>

    /* owner builder add property */
    @Multipart
    @POST("owner/add-property")
    suspend fun ownerBuilderAddProperty(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>,
    ): Response<OwnerAddPropertyResponse>

    /* owner add property */
    @Multipart
    @POST("owner/add-property")
    suspend fun ownerApartmentOwnerAddProperty(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>,
        @Part floor_plans: List<MultipartBody.Part>,
        @Part videos: List<MultipartBody.Part>,
        @Part("amenities[]") amenities: List<Int>,
        @Part("detailskey[]") detailskey: ArrayList<Int>,
        @Part("detailsvalue[]") detailsvalue: ArrayList<Int>,
    ): Response<OwnerAddPropertyResponse>

    /* owner property main listing */
    @FormUrlEncoded
    @POST("owner/propertylist")
    suspend fun ownerPropertyMainListing(
        @Field("page") page: String,
        @Field("category") category: String,
        @Field("list_id") list_id: String,
        @Field("is_verified") verification_id: String,
    ): Response<OwnerPropertyMainListingResponse>

    /* owner builder details list page */
    @FormUrlEncoded
    @POST("owner/owner-building")
    suspend fun ownerBuildingDetailsList(@Field("owner_property_id") owner_property_id: Int): Response<OwnerBuildingDetailsListResponse>

    /* owner add apartment */
    @Multipart
    @POST("owner/add-apartment")
    suspend fun ownerAddApartment(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>,
        @Part floor_plans: List<MultipartBody.Part>,
        @Part videos: List<MultipartBody.Part>,
        @Part("amenities[]") amenities: List<Int>,
        @Part("detailskey[]") detailskey: ArrayList<Int>,
        @Part("detailsvalue[]") detailsvalue: ArrayList<Int>,
    ): Response<OwnerAddApartmentResponse>

    /* owner home page property listing details */
    @FormUrlEncoded
    @POST("owner/propertylist")
    suspend fun ownerHomePropertyListingDetails(
        @Field("page") page: String,
        @Field("list_id") list_id: String,
    ): Response<OwnerPropertyMainListingResponse>

    /* owner property filter types */
    @GET("types")
    suspend fun ownerPropertyTypes(): Response<OwnerPropertyTypesResponse>

    /* owner property filter list */
    @FormUrlEncoded
    @POST("owner/propertylist")
    suspend fun ownerPropertyFilterList(
        @Field("page") page: String,
        @Field("type_id") type_id: String,
    ): Response<OwnerPropertyMainListingResponse>

    /* owner notification */
    @FormUrlEncoded
    @POST("owner/notifications")
    suspend fun ownerNotification(@Field("page") page: String): Response<OwnerNotificationResponse>

    /* owner notification status up-date */
    @FormUrlEncoded
    @POST("owner/update-notification-status")
    suspend fun ownerNotificationStatusUpdate(@Field("notification_id") notification_id: String): Response<OwnerNotificationStatusUpdateResponse>

    //Owner Main property details
    @FormUrlEncoded
    @POST("owner/property-details")
    suspend fun ownerMainPropertyDetails(@Field("owner_property_id") owner_property_id: String): Response<OwnerPropertyMainDetailsNewResponse>

    //Owner Property Report
    @FormUrlEncoded
    @POST("owner/owner-property-report")
    suspend fun ownerPropertyReport(@Field("property_id") property_id: String): Response<OwnerCommonPdfReportResponse>

    //Owner Overall Report
    @POST("owner/owner-overall-report")
    suspend fun ownerOverallReport(): Response<OwnerCommonPdfReportResponse>

    //Owner Occupied Report
    @POST("owner/occupied-report")
    suspend fun ownerOccupiedReport(): Response<OwnerCommonPdfReportResponse>

    //Owner Vacant Report
    @POST("owner/vacant-report")
    suspend fun ownerVacantReport(): Response<OwnerCommonPdfReportResponse>

    //Owner Home Yearly Report
    @FormUrlEncoded
    @POST("owner/yearly-report")
    suspend fun ownerYearlyReport(@Field("year") year: String): Response<OwnerCommonPdfReportResponse>

    //Owner Home Monthly Report
    @FormUrlEncoded
    @POST("owner/monthly-report")
    suspend fun ownerMonthlyReport(
        @Field("property_id") property_id: String,
        @Field("date") date: String,
    ): Response<OwnerCommonPdfReportResponse>

    /* Owner Change Password */
    @FormUrlEncoded
    @POST("owner/change-password")
    suspend fun ownerChangePassword(
        @Field("current_password") current_password: String,
        @Field("new_password") new_password: String,
        @Field("confirm_new_password") confirm_new_password: String,
    ): Response<OwnerChangePasswordReponse>


    /* owner building for verification */
    @FormUrlEncoded
    @POST("owner/submit-for-verification")
    suspend fun ownerBuildingForVerification(@Field("property_id") property_id: String): Response<OwnerSubmitForVerification>


    /*agent api*/

    //Logout
    @GET("agent/logout")
    suspend fun logoutAgent(): Response<CommonResponse>

    //Agent Password Change
    @FormUrlEncoded
    @POST("agent/changepassword")
    suspend fun changePasswordAgent(
        @Field("current_password") current_password: String,
        @Field("new_password") new_password: String,
        @Field("confirm_new_password") confirm_new_password: String,
    ): Response<CommonResponse>

    //agent profile details
    @POST("agent/profile")
    suspend fun agentProfileDetails(): Response<AgentProfileDetailsResponse>

    //update profile details
    @Multipart
    @POST("agent/profile/update")
    suspend fun updateAgentProfileDetails(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part,
    ): Response<CommonResponse>

    //update profile details
    @Multipart
    @POST("agent/profile/update")
    suspend fun updateAgentProfileDetails(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<CommonResponse>

    //agent assigned property list
    @POST("agent/agentproperty")
    suspend fun agentAssignedPropertyList(
        @Query("page") page: String,
    ): Response<AgentAssignedPropertyListResponse>

    //agent assigned property details
    @FormUrlEncoded
    @POST("agent/agentpropertydetails")
    suspend fun agentAssignedPropertyDetails(
        @Field("property_id") property_id: String,
    ): Response<AgentAssignedPropertyDetailsResponse>

    //agent apartment details
    @FormUrlEncoded
    @POST("agent/building-unit-details")
    suspend fun agentApartmentDetails(
        @Field("property_id") property_id: String,
        @Field("building_id") building_id: String,
    ): Response<AgentAssignedPropertyDetailsResponse>

    //agent/agent-home
    @POST("agent/agent-home")
    suspend fun agentHomeDetails(
    ): Response<AgentHomeResponse>

    //agent/start-user-tour
    @FormUrlEncoded
    @POST("agent/start-user-tour")
    suspend fun agentPropertyAppointment(
        @Field("tour_id") tour_id: String,
    ): Response<AgentPropertyAppointmentResponse>

    //Details for Booking Property By Agent
    @FormUrlEncoded
    @POST("agent/proceed-booking-details")
    suspend fun agentProceedBookDetails(
        @Field("tour_id") tour_id: String,
    ): Response<AgentProceedBookDetails>

    //Booking Property By Agent
    @Multipart
    @POST("agent/proceed-booking")
    suspend fun uploadBookPropertyDetails(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: MultipartBody.Part,
    ): Response<CommonResponse>

    //Removing property document
    @FormUrlEncoded
    @POST("agent/remove-property-document")
    suspend fun agentRemovePropertyDocument(
        @Field("document_id") document_id: String,
    ): Response<CommonResponse>

    /* agent add property */
    @Multipart
    @POST("agent/propertydetailsupdate")
    suspend fun agentAddProperty(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>,
        @Part floor_plans: List<MultipartBody.Part>,
        @Part videos: List<MultipartBody.Part>,
        @Part("amenities[]") amenities: List<Int>,
        @Part("detailskey[]") detailskey: ArrayList<Int>,
        @Part("detailsvalue[]") detailsvalue: ArrayList<Int>,
    ): Response<CommonResponse>

    /* agent update Apartment */
    @Multipart
    @POST("agent/update-building-unit")
    suspend fun agentUpdateApartment(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>,
        @Part floor_plans: List<MultipartBody.Part>,
        @Part videos: List<MultipartBody.Part>,
        @Part("amenities[]") amenities: List<Int>,
        @Part("detailskey[]") detailskey: ArrayList<Int>,
        @Part("detailsvalue[]") detailsvalue: ArrayList<Int>,
    ): Response<CommonResponse>

    //Upload user property document by agent
    @Multipart
    @POST("agent/upload-user-property-document")
    suspend fun agentUploadPropertyDocuments(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: List<MultipartBody.Part>,
    ): Response<CommonResponse>

    //Request for Contract by agent
    @FormUrlEncoded
    @POST("agent/request-contract")
    suspend fun agentRequestContract(
        @Field("user_property_id") user_property_id: String,
        @Field("tour_id") tour_id: String,
    ): Response<CommonResponse>

    //Updating Tour Not Interested by agent
    @FormUrlEncoded
    @POST("agent/update-tour-not-interested")
    suspend fun agentUpdateTourNotInterested(
        @Field("tour_id") tour_id: String,
    ): Response<CommonResponse>

    //List agent - user booking property ongoing list
    @POST("agent/my-user-properties")
    suspend fun agentUserBookingPropertyOngoingList(
        @Query("page") page: String,
    ): Response<AgentUserBookingPropertyOngoingResponse>

    //List agent - user booking property completed list
    @Multipart
    @POST("agent/my-user-properties")
    suspend fun agentUserBookingPropertyCompletedList(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<AgentUserBookingPropertyCompletedResponse>

    //Agent Property Details
    @POST("agent/my-user-property-details")
    suspend fun agentUserPropertyViewDetails(
        @Query("tour_id") tour_id: String,
    ): Response<AgentUserBookingPropertyViewDetailsResponse>

    //Agent adding task with a specified date
    @POST("agent/add-task")
    suspend fun agentAddTask(
        @Query("title") title: String,
        @Query("task_date") task_date: String,
        @Query("task_time") task_time: String,
    ): Response<CommonResponse>

    //Agent pending task listing
    @POST("agent/task-list")
    suspend fun agentPendingTaskList(): Response<AgentPendingTaskListResponse>

    // Agent completed task listing
    @Multipart
    @POST("agent/task-list")
    suspend fun agentCompetedTaskList(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ):
            Response<AgentPendingTaskListResponse>

    // Agent updating task status to completed
    @Multipart
    @POST("agent/update-task-status")
    suspend fun agentUpdateTaskStatus(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ):
            Response<CommonResponse>

    // Agent updating task status back to pending
    @Multipart
    @POST("agent/update-task-status")
    suspend fun agentUpdateCompletedTaskStatus(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ):
            Response<CommonResponse>

    //Listing Agent request
    @POST("agent/my-request")
    suspend fun agentRequestList(): Response<AgentMyRequestListResponse>

    //Accept Agent request
    @Multipart
    @POST("agent/accept-my-request")
    suspend fun agentAcceptMyRequest(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<CommonResponse>

    //Reject Agent request
    @Multipart
    @POST("agent/reject-my-request")
    suspend fun agentRejectMyRequest(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<CommonResponse>

    //Agent task calendar task count
    @POST("agent/calendar-task-count")
    suspend fun agentCalenderTaskCount(
        @Query("date") date: String,
    ): Response<AgentCalenderTaskCountResponse>

    // Agent task calendar task list
    @POST("agent/calendar-task-list")
    suspend fun agentCalenderTaskList(
        @Query("date") date: String,
    ): Response<AgentCalenderTaskListResponse>

    // Ongoing agent Owner Properties
    @POST("agent/my-owner-properties")
    suspend fun agentOwnerOngoingPropertiesList(
        @Query("page") page: String,
    ): Response<AgentOwnerOngoingListResponse>

    // Completed agent owner properties
    @POST("agent/my-owner-properties")
    suspend fun agentOwnerCompletedPropertiesList(
        @Query("page") page: String,
        @Query("completed") completed: String,
    ): Response<AgentOwnerCompletedListResponse>

    // Start owner tour(property details owner)
    @POST("agent/start-owner-tour")
    suspend fun agentStartOwnerTourPropertyDetails(
        @Query("tour_id") tour_id: String,
    ): Response<AgentStartOwnerTourPropertyDetailsResponse>

    // Agent owner properties details
    @POST("agent/my-owner-property-details")
    suspend fun agentMyOwnerPropertyDetails(
        @Query("tour_id") tour_id: String,
    ): Response<AgentStartOwnerTourPropertyDetailsResponse>

    //Remove profile pic
    @POST("agent/remove-profile-image")
    suspend fun removeAgentProfilePic(): Response<CommonResponse>

    //Agent Cash in Hand Total
    @POST("agent/total-cash-in-hand")
    suspend fun agentTotalCashInHand(): Response<AgentCashInHandTotalResponse>

    //Agent Pay Cash in Hand Total
    @POST("agent/pay-cash-in-hand")
    suspend fun agentPayTotalCashInHand(): Response<CommonResponse>

    // Agent Cash in Hand list
    @POST("agent/cash-in-hand-list")
    suspend fun agentCashInHandList(
        @Query("page") page: String,
    ): Response<CashInHandListResponse>

    // Agent My Earnings Total
    @POST("agent/total-earnings")
    suspend fun agentMyEarningsTotal(): Response<TotalEarningsResponse>

    // Agent My Earnings List
    @POST("agent/my-earnings")
    suspend fun agentMyEarningsList(
        @Query("page") page: String,
    ): Response<MyEarningsListResponse>

    // Agent Tour Location Data
    @POST("agent/tour-location-details")
    suspend fun agentTourLocationData(
        @Query("tour_id") tour_id: String,
        @Query("user_type") user_type: String,
    ): Response<AgentTourLocationViewResponse>

    // Agent Property Rent Details
    @POST("agent/price-details")
    suspend fun agentPropertyRentDetails(
        @Query("property_id") property_id: String,
        @Query("type") type: String,
    ): Response<AgentPropertyRentDetailsResponse>

    // Agent Property Sale Details
    @POST("agent/price-details")
    suspend fun agentPropertySaleDetails(
        @Query("property_id") property_id: String,
        @Query("type") type: String,
    ): Response<AgentPropertySaleDetailsResponse>

    // Agent Notification list
    @POST("agent/agent-notifications")
    suspend fun agentNotificationList(
        @Query("page") page: String,
    ): Response<AgentNotificationListResponse>

    // Agent Notification Actions
    @POST("agent/read-notifications")
    suspend fun agentNotificationAction(
        @Query("notification_id") notification_id: String,
        @Query("status") status: String,
    ): Response<AgentNotificationActionResponse>

    // Agent FeedBack
    @POST("agent/feedbacks")
    suspend fun agentFeedBack(@Query("page") page: String): Response<AgentFeedBackResponse>

    // Agent Terms of Stay
    @POST("agent/terms-of-stay/{property_id}")
    suspend fun agentTermsOfStay(@Query("property_id") property_id: Int): Response<TermsOfStayResponse>

    // Agent My Earnings Request
    @POST("agent/request-earnings")
    suspend fun agentMyEarningsRequest(): Response<CommonResponse>

    // Agent Add Commission
    @POST("agent/add-commission")
    suspend fun agentAddCommission(
        @Query("tour_id") tour_id: Int,
        @Query("user_id") user_id: Int,
        @Query("property_id") property_id: Int,
        @Query("amount") amount: Int,
    ): Response<CommonResponse>

    // Agent My Earnings Request
    @POST("agent/edit-commission")
    suspend fun agentEditCommission(
        @Query("tour_id") tour_id: Int,
        @Query("amount") amount: Int,
    ): Response<CommonResponse>

    // Agent Packages
    @POST("agent/other-packages")
    suspend fun agentPackages(@Query("property_id") property_id: Int): Response<AgentPackageDetailsResponse>

    // Agent Package List
    @POST("agent/package-list")
    suspend fun agentPackageList(@Query("property_id") property_id: Int): Response<AgentSubPackageList>

    // Agent Property PDF Download
    @POST("agent/download-property-details-pdf")
    suspend fun agentPropertyPdfDownload(@Query("property_id") property_id: String): Response<AgentPropertyPdfResponse>

    //Agent Owner Contract
    @POST("agent/property-contract-details")
    suspend fun agentOwnerContract(@Query("property_id") property_id: String): Response<AgentCommonContractResponse>

    //Agent User Contract
    @POST("agent/user-property-contract-details")
    suspend fun agentUserContract(
        @Query("property_id") property_id: String,
        @Query("user_id") user_id: String,
    ): Response<AgentCommonContractResponse>

    //Agent builder details
    @POST("agent/building-details")
    suspend fun agentBuilderDetails(@Query("property_id") property_id: String): Response<AgentBuilderDetailsResponse>

    /*Agent Update whole building*/
    @Multipart
    @POST("agent/update-building-details")
    suspend fun agentUpdateBuilding(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part building_images: List<MultipartBody.Part>,
    ): Response<CommonResponse>

    //Agent Fetch pending amount
    @FormUrlEncoded
    @POST("agent/fetch-pending-amount")
    suspend fun agentGetPendingAmount(
        @Field("user_property_id") user_property_id: String,
        @Field("tour_id") tour_id: String,
    ): Response<AgentFetchPendingAmountResponse>

    //Agent Pay full Pending Amount
    @Multipart
    @POST("agent/pay-full-amount-bill")
    suspend fun agentPayFullAmount(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: MultipartBody.Part,
    ): Response<CommonResponse>

    /* retrofit builder */
    companion object {
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl("http://admin.siaaha.com/api/")
                .build()
            return retrofit.create(ApiService::class.java)
        }

        private fun initializeClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            System.setProperty("http.keepAlive", "false")

            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val trustManagerFactory: TrustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> =
                trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + trustManagers.contentToString()
            }

            val trustManager =
                trustManagers[0] as X509TrustManager

            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
                .addInterceptor(SupportInterceptor())
                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier(HostnameVerifier { _, _ -> true })
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
            return builder.build()
        }
    }
}