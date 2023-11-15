package com.property.propertyagent.data

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
import okhttp3.RequestBody
import retrofit2.Response

class MainRepository(private val apiHelper: ApiService) {

    /* login */
    suspend fun login(
        email: String,
        password: String,
        type: String,
        device_token: String
    ): Response<LoginResponse> =
        apiHelper.login(email, password, type, device_token)

    /* forgot password
     common for both Agent and Owner*/
    suspend fun forgotPassword(
        email: String,
        user_type: String,
    ): Response<ForgotPasswordResponse> = apiHelper.forgotPassword(email, user_type)

    /* logout */
    suspend fun logout(): Response<LogoutResponse> =
        apiHelper.logout()

    /* owner add property types */
    suspend fun getPropertyTypes(type: Int): Response<OwnerPropertyTypeResponse> =
        apiHelper.getPropertyTypes(type)

    /* owner add property states list */
    suspend fun getStates(country: Int): Response<StatesResponce> =
        apiHelper.getStates(country)

    suspend fun getStates(): Response<StatesResponce> =
        apiHelper.getStates()

    /* owner add property city list */
    suspend fun getCity(state: Int): Response<CityResponce> =
        apiHelper.getCity(state)

    /* owner add property country list */
    suspend fun getCountry(): Response<CountryResponse> =
        apiHelper.getCountry()

    /* owner add property zip code list */
    suspend fun getZipcode(city: Int): Response<ZipcodeResponse> =
        apiHelper.getZipCode(city)

    /* owner add property amenities list */
    suspend fun getNewAmenities(type: Int): Response<AmenitiesNewResponse> =
        apiHelper.getNewAmenities(type)

    /* owner maintains list */
    suspend fun getMaintains(page: String): Response<OwnerMaintenanceServiceResponse> =
        apiHelper.getMaintance(page)

    /* owner profile */
    suspend fun ownerProfile(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>,
    ): Response<Owner_ProfileResponse> =
        apiHelper.ownerProfile(params, images)


    suspend fun getOwnerProfileDetails(): Response<Owner_ProfileResponse> =
        apiHelper.getOwnerProfileDetails()

    suspend fun getOwnerPropertList(): Response<OwnerPropertyListResponse> =
        apiHelper.getProperties()

    suspend fun ownerPropertiesForService(service_id: Int): Response<OwnerPropertiesForServiceResponse> =
        apiHelper.ownerPropertiesForService(service_id)

    suspend fun ownerRequestService(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: List<MultipartBody.Part>,
    ): Response<CommonResponse> =
        apiHelper.ownerRequestService(params, document)

    suspend fun ownerRequestedServiceList(page: String):
            Response<OwnerListRequestedServicesResponse> = apiHelper.ownerRequestedServiceList(page)

    suspend fun ownerRequestedServiceDetails(request_id: String):
            Response<OwnerRequestedServiceDetailsResponse> =
        apiHelper.ownerRequestedServiceDetails(request_id)

    suspend fun ownerServicePaymentBill(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: MultipartBody.Part,
    ): Response<CommonResponse> =
        apiHelper.ownerServicePaymentBill(params, document)

    suspend fun cancelServiceRequest(params: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<CommonResponse> = apiHelper.cancelServiceRequest(params)

    suspend fun ownerRequestedServiceForApprovalList(page: String):
            Response<OwnerRequestServiceForApprovalListResponse> =
        apiHelper.ownerRequestedServiceForApprovalList(page)

    suspend fun ownerAcceptRequestedService(params: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<CommonResponse> = apiHelper.ownerAcceptRequestedService(params)

    suspend fun ownerRejectRequestedService(params: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<CommonResponse> = apiHelper.ownerRejectRequestedService(params)

    /* owner payment list of properties */
    suspend fun ownerPaymentListOfProperties(
        property_to: Int,
        page: String,
    ): Response<OwnerPaymentListOfPropertiesResponse> =
        apiHelper.ownerPaymentListOfProperties(property_to, page)

    /* owner property payment details */
    suspend fun ownerPropertyPaymentDetails(property_id: Int): Response<PropertyPaymentDetailsResponse> =
        apiHelper.ownerPropertyPaymentDetails(property_id)

    /* owner property payment history list */
    suspend fun ownerPropertyHistoryList(
        property_id: Int,
        page: String,
    ): Response<OwnerPaymentHistoryListResponse> =
        apiHelper.ownerPropertyHistoryList(property_id, page)

    /* owner payment history details */
    suspend fun ownerPaymentHistoryDetails(
        payment_id: Int,
        property_id: Int,
    ): Response<OwnerPaymentHistoryDetailsResponse> =
        apiHelper.ownerPaymentHistoryDetails(payment_id, property_id)

    /* owner home statistics */
    suspend fun ownerHomeStatistics(year: String): Response<OwnerHomeStatisticsResponse> =
        apiHelper.ownerHomeStatistics(year)

    /* owner home property list */
    suspend fun ownerHomePropertyList(): Response<OwnerHomePropertyListResponse> =
        apiHelper.ownerHomePropertyList()

    /* owner home monthly account */
    suspend fun ownerHomeMonthlyAccount(
        property_id: Int,
        date: String,
    ): Response<OwnerHomeMonthlyAccountResponse> =
        apiHelper.ownerHomeMonthlyAccount(property_id, date)

    /* owner payment received */
    suspend fun ownerPaymentReceived(
        property_id: Int,
        payment_id: Int,
    ): Response<OwnerPaymentReceviedResponse> =
        apiHelper.ownerPaymentReceived(property_id, payment_id)

    /* owner maintenance status */
    suspend fun ownerMaintenanceStatus(page: String): Response<OwnerMaintanceStatusResponse> =
        apiHelper.ownerMaintenanceStatus(page)

    /* owner home property search */
    suspend fun ownerHomePropertySearch(
        page: String,
        property_name: String,
    ): Response<OwnerPropertyMainListingResponse> =
        apiHelper.ownerHomePropertySearch(page, property_name)

    /* owner home property details */
    suspend fun ownerHomePropertyDetails(): Response<OwnerHomePropertyDetailsResponse> =
        apiHelper.ownerHomePropertyDetails()

    /* owner add property */
    suspend fun ownerBuilderAddProperty(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>,
    ): Response<OwnerAddPropertyResponse> = apiHelper.ownerBuilderAddProperty(
        params,
        images
    )

    /* owner add property */
    suspend fun ownerApartmentOwnerAddProperty(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>,
        floor_plans: List<MultipartBody.Part>,
        videos: List<MultipartBody.Part>,
        amenities: List<Int>,
        detailskey: ArrayList<Int>,
        detailsvalue: ArrayList<Int>,
    ): Response<OwnerAddPropertyResponse> = apiHelper.ownerApartmentOwnerAddProperty(
        params,
        images,
        floor_plans,
        videos,
        amenities,
        detailskey,
        detailsvalue
    )

    /* owner property main listing */
    suspend fun ownerPropertyMainListing(
        page: String,
        category: String,
        list_id: String,
        verification_id: String
    ): Response<OwnerPropertyMainListingResponse> =
        apiHelper.ownerPropertyMainListing(page, category, list_id, verification_id)

    /* owner builder details list page */
    suspend fun ownerBuildingDetailsList(owner_property_id: Int): Response<OwnerBuildingDetailsListResponse> =
        apiHelper.ownerBuildingDetailsList(owner_property_id)

    /* owner add apartment */
    suspend fun ownerAddApartment(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>,
        floor_plans: List<MultipartBody.Part>,
        videos: List<MultipartBody.Part>,
        amenities: List<Int>,
        detailskey: ArrayList<Int>,
        detailsvalue: ArrayList<Int>,
    ): Response<OwnerAddApartmentResponse> = apiHelper.ownerAddApartment(
        params,
        images,
        floor_plans,
        videos,
        amenities,
        detailskey,
        detailsvalue
    )

    /* owner home page property listing details */
    suspend fun ownerHomePropertyListingDetails(
        page: String,
        list_id: String,
    ): Response<OwnerPropertyMainListingResponse> =
        apiHelper.ownerHomePropertyListingDetails(page, list_id)

    /* owner property filter types */
    suspend fun ownerPropertyTypes(): Response<OwnerPropertyTypesResponse> =
        apiHelper.ownerPropertyTypes()

    /* owner property filter list */
    suspend fun ownerPropertyFilterList(
        page: String,
        type_id: String,
    ): Response<OwnerPropertyMainListingResponse> =
        apiHelper.ownerPropertyFilterList(page, type_id)

    /* owner notification */
    suspend fun ownerNotification(page: String): Response<OwnerNotificationResponse> =
        apiHelper.ownerNotification(page)

    /* owner notification status up-date */
    suspend fun ownerNotificationStatusUpdate(notification_id: String): Response<OwnerNotificationStatusUpdateResponse> =
        apiHelper.ownerNotificationStatusUpdate(notification_id)

    //Owner Main Property Detail Module
    suspend fun ownerPropertyMainDetails(id: String): Response<OwnerPropertyMainDetailsNewResponse> =
        apiHelper.ownerMainPropertyDetails(id)

    //Owner Report Module
    suspend fun ownerPropertyReport(id: String): Response<OwnerCommonPdfReportResponse> =
        apiHelper.ownerPropertyReport(id)

    suspend fun ownerVacantReport(): Response<OwnerCommonPdfReportResponse> =
        apiHelper.ownerVacantReport()

    suspend fun ownerOccupiedReport(): Response<OwnerCommonPdfReportResponse> =
        apiHelper.ownerOccupiedReport()

    suspend fun ownerOverallReport(): Response<OwnerCommonPdfReportResponse> =
        apiHelper.ownerOverallReport()

    suspend fun ownerHomeYearlyReport(year: String): Response<OwnerCommonPdfReportResponse> =
        apiHelper.ownerYearlyReport(year)

    suspend fun ownerHomeMonthlyReport(
        property_id: String,
        date: String,
    ): Response<OwnerCommonPdfReportResponse> =
        apiHelper.ownerMonthlyReport(property_id, date)

    suspend fun ownerChangePassword(
        current_password: String,
        new_password: String,
        confirm_new_password: String
    ): Response<OwnerChangePasswordReponse> =
        apiHelper.ownerChangePassword(current_password, new_password, confirm_new_password)

    suspend fun ownerBuildingForVerification(property_id: String): Response<OwnerSubmitForVerification> =
        apiHelper.ownerBuildingForVerification(property_id)


    /*agent api*/
    suspend fun logoutAgent(): Response<CommonResponse> =
        apiHelper.logoutAgent()

    suspend fun changePasswordAgent(
        current_password: String,
        new_password: String,
        confirm_new_password: String,
    ): Response<CommonResponse> =
        apiHelper.changePasswordAgent(current_password, new_password, confirm_new_password)

    suspend fun agentProfileDetails(): Response<AgentProfileDetailsResponse> =
        apiHelper.agentProfileDetails()

    suspend fun updateAgentProfileDetails(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        image: MultipartBody.Part,
    ): Response<CommonResponse> =
        apiHelper.updateAgentProfileDetails(params, image)

    suspend fun updateAgentProfileDetails(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<CommonResponse> =
        apiHelper.updateAgentProfileDetails(params)

    suspend fun agentAssignedPropertyList(page: String): Response<AgentAssignedPropertyListResponse> =
        apiHelper.agentAssignedPropertyList(page)

    suspend fun agentAssignedPropertyDetails(
        property_id: String,
    ): Response<AgentAssignedPropertyDetailsResponse> =
        apiHelper.agentAssignedPropertyDetails(property_id)

    suspend fun agentApartmentDetails(
        property_id: String,
        building_id: String,
    ): Response<AgentAssignedPropertyDetailsResponse> =
        apiHelper.agentApartmentDetails(property_id, building_id)

    suspend fun agentHomeDetails(): Response<AgentHomeResponse> =
        apiHelper.agentHomeDetails()

    suspend fun agentPropertyAppointment(
        tour_id: String,
    ): Response<AgentPropertyAppointmentResponse> =
        apiHelper.agentPropertyAppointment(tour_id)

    suspend fun agentProceedBookDetails(
        tour_id: String,
    ): Response<AgentProceedBookDetails> =
        apiHelper.agentProceedBookDetails(tour_id)

    suspend fun uploadBookPropertyDetails(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: MultipartBody.Part,
    ): Response<CommonResponse> =
        apiHelper.uploadBookPropertyDetails(params, document)

    suspend fun agentRemovePropertyDocument(
        document_id: String,
    ): Response<CommonResponse> =
        apiHelper.agentRemovePropertyDocument(document_id)

    suspend fun agentAddProperty(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>,
        floor_plans: List<MultipartBody.Part>,
        videos: List<MultipartBody.Part>,
        amenities: List<Int>,
        detailskey: ArrayList<Int>,
        detailsvalue: ArrayList<Int>,
    ): Response<CommonResponse> = apiHelper.agentAddProperty(
        params,
        images,
        floor_plans,
        videos,
        amenities,
        detailskey,
        detailsvalue
    )

    suspend fun agentUpdateApartment(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>,
        floor_plans: List<MultipartBody.Part>,
        videos: List<MultipartBody.Part>,
        amenities: List<Int>,
        detailskey: ArrayList<Int>,
        detailsvalue: ArrayList<Int>,
    ): Response<CommonResponse> = apiHelper.agentUpdateApartment(
        params,
        images,
        floor_plans,
        videos,
        amenities,
        detailskey,
        detailsvalue
    )

    suspend fun agentUploadPropertyDocuments(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: List<MultipartBody.Part>,
    ): Response<CommonResponse> =
        apiHelper.agentUploadPropertyDocuments(params, document)

    suspend fun agentRequestContract(
        user_property_id: String, tour_id: String,
    ): Response<CommonResponse> =
        apiHelper.agentRequestContract(user_property_id, tour_id)

    suspend fun agentUpdateTourNotInterested(
        tour_id: String,
    ): Response<CommonResponse> =
        apiHelper.agentUpdateTourNotInterested(tour_id)

    suspend fun agentUserBookingPropertyOngoingList(
        page: String,
    ):
            Response<AgentUserBookingPropertyOngoingResponse> =
        apiHelper.agentUserBookingPropertyOngoingList(
            page
        )

    suspend fun agentUserBookingPropertyCompletedList(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
    ):
            Response<AgentUserBookingPropertyCompletedResponse> =
        apiHelper.agentUserBookingPropertyCompletedList(
            params
        )

    suspend fun agentUserPropertyViewDetails(
        tour_id: String,
    ): Response<AgentUserBookingPropertyViewDetailsResponse> =
        apiHelper.agentUserPropertyViewDetails(tour_id)

    suspend fun agentAddTask(
        title: String, task_date: String, task_time: String,
    ): Response<CommonResponse> =
        apiHelper.agentAddTask(title, task_date, task_time)

    suspend fun agentPendingTaskList(): Response<AgentPendingTaskListResponse> =
        apiHelper.agentPendingTaskList()

    suspend fun agentCompetedTaskList(params: Map<String, @JvmSuppressWildcards RequestBody>): Response<AgentPendingTaskListResponse> =
        apiHelper.agentCompetedTaskList(params)

    suspend fun agentUpdateTaskStatus(params: Map<String, @JvmSuppressWildcards RequestBody>): Response<CommonResponse> =
        apiHelper.agentUpdateTaskStatus(params)

    suspend fun agentUpdateCompletedTaskStatus(params: Map<String, @JvmSuppressWildcards RequestBody>): Response<CommonResponse> =
        apiHelper.agentUpdateCompletedTaskStatus(params)

    suspend fun agentRequestList(): Response<AgentMyRequestListResponse> =
        apiHelper.agentRequestList()

    suspend fun agentAcceptMyRequest(params: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<CommonResponse> = apiHelper.agentAcceptMyRequest(params)

    suspend fun agentRejectMyRequest(params: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<CommonResponse> = apiHelper.agentRejectMyRequest(params)

    suspend fun agentCalenderTaskCount(date: String): Response<AgentCalenderTaskCountResponse> =
        apiHelper.agentCalenderTaskCount(date)

    suspend fun agentCalenderTaskList(date: String): Response<AgentCalenderTaskListResponse> =
        apiHelper.agentCalenderTaskList(date)

    suspend fun agentOwnerOngoingPropertiesList(page: String): Response<AgentOwnerOngoingListResponse> =
        apiHelper.agentOwnerOngoingPropertiesList(page)

    suspend fun agentOwnerCompletedPropertiesList(
        page: String,
        completed: String,
    ): Response<AgentOwnerCompletedListResponse> =
        apiHelper.agentOwnerCompletedPropertiesList(page, completed)

    suspend fun agentStartOwnerTourPropertyDetails(tour_id: String): Response<AgentStartOwnerTourPropertyDetailsResponse> =
        apiHelper.agentStartOwnerTourPropertyDetails(tour_id)

    suspend fun agentMyOwnerPropertyDetails(tour_id: String): Response<AgentStartOwnerTourPropertyDetailsResponse> =
        apiHelper.agentMyOwnerPropertyDetails(tour_id)

    /* logout */
    suspend fun removeAgentProfileImage(): Response<CommonResponse> =
        apiHelper.removeAgentProfilePic()

    //Cash in Hand module
    suspend fun agentTotalCashInHand(): Response<AgentCashInHandTotalResponse> =
        apiHelper.agentTotalCashInHand()

    suspend fun agentPayTotalCashInHand(): Response<CommonResponse> =
        apiHelper.agentPayTotalCashInHand()

    suspend fun agentCashInHandList(page: String): Response<CashInHandListResponse> =
        apiHelper.agentCashInHandList(page)

    //My Earnings module
    suspend fun agentTotalMyEarnings(): Response<TotalEarningsResponse> =
        apiHelper.agentMyEarningsTotal()

    suspend fun agentMyEarningsList(page: String): Response<MyEarningsListResponse> =
        apiHelper.agentMyEarningsList(page)

    suspend fun agentRequestMyEarnings(): Response<CommonResponse> =
        apiHelper.agentMyEarningsRequest()

    //Home Tour Location data
    suspend fun agentHomeTourLocationViewData(
        tour_id: String,
        user_type: String,
    ): Response<AgentTourLocationViewResponse> =
        apiHelper.agentTourLocationData(tour_id, user_type)

    //Rent Details Module
    suspend fun agentPropertyRentDetails(
        property_id: String,
        type: String,
    ): Response<AgentPropertyRentDetailsResponse> =
        apiHelper.agentPropertyRentDetails(property_id, type)

    suspend fun agentPropertySaleDetails(
        property_id: String,
        type: String,
    ): Response<AgentPropertySaleDetailsResponse> =
        apiHelper.agentPropertySaleDetails(property_id, type)

    //Agent Notification module
    suspend fun agentNotificationList(page: String): Response<AgentNotificationListResponse> =
        apiHelper.agentNotificationList(page)

    suspend fun agentNotificationAction(
        notification_id: String,
        status: String,
    ): Response<AgentNotificationActionResponse> =
        apiHelper.agentNotificationAction(notification_id, status)

    //Agent FeedBack
    suspend fun agentFeedBack(page: String): Response<AgentFeedBackResponse> =
        apiHelper.agentFeedBack(page)

    //Agent Terms and Stay
    suspend fun agentTermsOfStay(property_id: Int): Response<TermsOfStayResponse> =
        apiHelper.agentTermsOfStay(property_id)

    //Agent Commission Module
    suspend fun agentAddCommission(
        tour_id: Int,
        user_id: Int,
        property_id: Int,
        amount: Int,
    ): Response<CommonResponse> =
        apiHelper.agentAddCommission(tour_id, user_id, property_id, amount)

    suspend fun agentEditCommission(tour_id: Int, amount: Int): Response<CommonResponse> =
        apiHelper.agentEditCommission(tour_id, amount)

    //Agent Packages Module
    suspend fun agentPackages(property_id: Int): Response<AgentPackageDetailsResponse> =
        apiHelper.agentPackages(property_id)

    suspend fun agentPackageList(property_id: Int): Response<AgentSubPackageList> =
        apiHelper.agentPackageList(property_id)

    //Agent Property Pdf
    suspend fun agentPropertyPdf(property_id: String): Response<AgentPropertyPdfResponse> =
        apiHelper.agentPropertyPdfDownload(property_id)

    //Agent Contract download module
    suspend fun agentOwnerContract(property_id: String): Response<AgentCommonContractResponse> =
        apiHelper.agentOwnerContract(property_id)

    suspend fun agentUserContract(
        property_id: String,
        user_id: String,
    ): Response<AgentCommonContractResponse> =
        apiHelper.agentUserContract(property_id, user_id)

    //Agent builder details
    suspend fun agentBuilderDetails(property_id: String): Response<AgentBuilderDetailsResponse> =
        apiHelper.agentBuilderDetails(property_id)

    //Agent Update Building
    suspend fun agentUpdateBuilding(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>,
    ): Response<CommonResponse> = apiHelper.agentUpdateBuilding(
        params,
        images,
    )

    //Agent full payment module
    suspend fun agentFetchPendingAmount(
        user_property_id: String, tour_id: String,
    ): Response<AgentFetchPendingAmountResponse> =
        apiHelper.agentGetPendingAmount(user_property_id, tour_id)

    suspend fun agentPayFullAmount(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: MultipartBody.Part,
    ): Response<CommonResponse> =
        apiHelper.agentPayFullAmount(params, document)

}


