package com.property.propertyuser.data.api

import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.ReadNotificationResponse
import com.property.propertyuser.modal.book_property.BookPropertyResponse
import com.property.propertyuser.modal.book_property_details.BookPropertyDetailsResponse
import com.property.propertyuser.modal.book_tour_dialog.BookTourDialogResponse
import com.property.propertyuser.modal.booking_tour.BookingTourResponse
import com.property.propertyuser.modal.desired_property_request_details.DesiredPropertyRequestDetailsResponse
import com.property.propertyuser.modal.desired_property_request_list.DesiredPropertyRequestListResponse
import com.property.propertyuser.modal.event_book_package.EventBookPackageResponse
import com.property.propertyuser.modal.event_booking_package_details.EventBookingPackageDetailsResponse
import com.property.propertyuser.modal.event_details.EventDetailsResponse
import com.property.propertyuser.modal.event_package.EventPackageResponse
import com.property.propertyuser.modal.events_list.EventListResponse
import com.property.propertyuser.modal.faq.FAQResponse
import com.property.propertyuser.modal.favorite_list.FavoriteListResponse
import com.property.propertyuser.modal.fetch_requested_property_data.FetchRequestedPropertyDataResponse
import com.property.propertyuser.modal.filter_home.ResidentialCommercialResponse
import com.property.propertyuser.modal.google_login.GoogleLoginResponse
import com.property.propertyuser.modal.home_listing_property_location.HomeListingPropertyLocationResponse
import com.property.propertyuser.modal.legal_informations.LegalInformationResponse
import com.property.propertyuser.modal.list_requested_service_details.RequestedServiceDetailsResponse
import com.property.propertyuser.modal.main_filter_details.FilterDetailsResponse
import com.property.propertyuser.modal.map_near_places.GoogleResponseModel
import com.property.propertyuser.modal.my_property_list.MyPropertiesResponse
import com.property.propertyuser.modal.other_package.PackagesResponse
import com.property.propertyuser.modal.otp.OtpResponse
import com.property.propertyuser.modal.payment_history.PaymentHistoryResponse
import com.property.propertyuser.modal.pdf_property_details.PropertyDetailsPdfResponse
import com.property.propertyuser.modal.privacy_policy.PrivacyPolicyResponse
import com.property.propertyuser.modal.proceed_book_property_details.ProceedBookPropertyDetailsResponse
import com.property.propertyuser.modal.profile.ProfileDetailsResponse
import com.property.propertyuser.modal.property_details.PropertyDetilsResponse
import com.property.propertyuser.modal.property_list.PropertyListResponse
import com.property.propertyuser.modal.property_location_details.PropertyLocationDetailsResponse
import com.property.propertyuser.modal.property_rental_details.PropertyRentalDetailsResponse
import com.property.propertyuser.modal.referral.ReferralResponse
import com.property.propertyuser.modal.rental_user_property_view_details.RentalUserPropertyViewDetailsResponse
import com.property.propertyuser.modal.scheduled_property.ScheduledPropertyListResponse
import com.property.propertyuser.modal.service_list.ServiceListResponse
import com.property.propertyuser.modal.show_all_near_by_property_details.ShowAllNearbyPropertyDetailsMapResponse
import com.property.propertyuser.modal.show_all_property_location.ShowAllPropertyLocationResponse
import com.property.propertyuser.modal.signup.SignUpResponse
import com.property.propertyuser.modal.single_vacate_request_details.SingleVacateRequestDetailsResponse
import com.property.propertyuser.modal.status_requested_services_details.SingleRequestedServiceDetailsResponse
import com.property.propertyuser.modal.terms_of_stay.TermsOfStayResponse
import com.property.propertyuser.modal.update_profile.UpdateProfileResponse
import com.property.propertyuser.modal.user_notification.NotificationResponse
import com.property.propertyuser.modal.user_rewards.UserRewaredResponse
import com.property.propertyuser.modal.vacate_request_list.VacateRequestListResponse
import com.property.propertyuser.pojo.login_otp_verification.LoginOtpVerificationResponse
import com.property.propertyuser.pojo.login_phone_verification.PhoneVerificationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.math.BigInteger

class MainRepository(private val apiService: ApiService) {
    suspend fun getPhoneVerification(phone: String): Response<PhoneVerificationResponse> =
        apiService.getPhoneVerification(phone)

    suspend fun getLoginDetailsFromOtp(
        phone: String,
        otp: Int,
        lat: String,
        lng: String
    ): Response<LoginOtpVerificationResponse> =
        apiService.getLoginDetailsFromOtp(phone, otp, lat, lng)

    suspend fun signUp(
        phone: String,
        email: String,
        name: String,
        lat: String,
        lan: String, location: String, referral: String
    ): Response<SignUpResponse> =
        apiService.signUp(phone, email, name, lat, lan, location, referral)

    suspend fun login(
        phone: String,
        otp: String,
        device_token: String
    ): Response<OtpResponse> = apiService.login(phone, otp, device_token)

    suspend fun phoneVerification(phone: String): Response<SignUpResponse> =
        apiService.phoneVerification(phone)

    suspend fun phoneVerificationResendOtp(phone: String): Response<SignUpResponse> =
        apiService.phoneVerificationResendOtp(phone)

    suspend fun listProperty(lat: String, lan: String, page: String):
            Response<PropertyListResponse> = apiService.listProperty(lat, lan, page)

    suspend fun getPropertyDetails(property_id: Int): Response<PropertyDetilsResponse> =
        apiService.getPropertyDetailsResponse(property_id)

    suspend fun updateFavouriteProperty(property_id: Int):
            Response<CommonResponse> = apiService.updateFavouriteProperty(property_id)

    suspend fun listPropertyFavourite(
        page: Int,
        favourite_list: Int
    ): Response<FavoriteListResponse> = apiService.listPropertyFavourite(page, favourite_list)

    suspend fun bookTourAddDateTime(
        property_id: String,
        date: String,
        time_range: String
    ): Response<BookTourDialogResponse> =
        apiService.bookTourAddDateTime(property_id, date, time_range)

    suspend fun bookingTour(tour_id: String, property_id: String): Response<BookingTourResponse> =
        apiService.bookingTour(tour_id, property_id)

    suspend fun bookingTourConfirmation(tour_id: String): Response<CommonResponse> =
        apiService.bookingTourConfirmation(tour_id)

    suspend fun listEvents(lat: String, lan: String, page: String): Response<EventListResponse> =
        apiService.listEvents(lat, lan, page)

    suspend fun eventDetails(event_id: String): Response<EventDetailsResponse> =
        apiService.eventDetails(event_id)

    suspend fun bookPropertyDetails(property_id: String): Response<BookPropertyDetailsResponse> =
        apiService.bookPropertyDetails(property_id)

    suspend fun bookProperty(
        property_id: String,
        check_in: String,
        check_out: String,
        coupon: String
    ): Response<BookPropertyResponse> =
        apiService.bookProperty(property_id, check_in, check_out, coupon)

    suspend fun uploadBookPaymentBill(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: MultipartBody.Part
    ): Response<CommonResponse> =
        apiService.uploadBookPaymentBill(params, document)

    suspend fun myPropertiesListDetails(
        page: String,
        user_property_status: String
    ): Response<MyPropertiesResponse> =
        apiService.myPropertiesListDetails(page, user_property_status)

    suspend fun proceedBookedPropertyDetails(property_id: String): Response<ProceedBookPropertyDetailsResponse> =
        apiService.proceedBookedPropertyDetails(property_id)

    suspend fun uploadPropertyDocuments(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: List<MultipartBody.Part>
    ): Response<CommonResponse> =
        apiService.uploadPropertyDocuments(params, document)

    suspend fun uploadUserPropertyBookPaymentBill(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: MultipartBody.Part
    ): Response<CommonResponse> =
        apiService.uploadUserPropertyBookPaymentBill(params, document)

    suspend fun cancelBookedProperty(params: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<CommonResponse> = apiService.cancelBookedProperty(params)

    suspend fun serviceListDetails(service_name: String, page: String, property_id: String):
            Response<ServiceListResponse> =
        apiService.serviceListDetails(service_name, page, property_id)

    suspend fun uploadRequestForServiceDetails(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: List<MultipartBody.Part>
    ): Response<CommonResponse> =
        apiService.uploadRequestForServiceDetails(params, document)

    suspend fun listRequestService(params: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<RequestedServiceDetailsResponse> = apiService.listRequestService(params)

    suspend fun requestedServiceDetails(params: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<SingleRequestedServiceDetailsResponse> =
        apiService.requestedServiceDetails(params)

    suspend fun uploadUserPropertyServicePaymentBill(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: MultipartBody.Part
    ): Response<CommonResponse> =
        apiService.uploadUserPropertyServicePaymentBill(params, document)

    suspend fun cancelServiceRequest(params: Map<String, @JvmSuppressWildcards RequestBody>):
            Response<CommonResponse> = apiService.cancelServiceRequest(params)

    suspend fun vacateRequestForProperty(
        user_property_id: String,
        vacate_date: String
    ): Response<CommonResponse> = apiService.vacateRequestForProperty(user_property_id, vacate_date)

    suspend fun vacateRequestList(page: String, property_id: String):
            Response<VacateRequestListResponse> = apiService.vacateRequestList(page, property_id)

    suspend fun singleVacateRequestDetails(vacate_id: String):
            Response<SingleVacateRequestDetailsResponse> =
        apiService.singleVacateRequestDetails(vacate_id)

    suspend fun rentalUserPropertyViewDetails(user_property_id: String):
            Response<RentalUserPropertyViewDetailsResponse> =
        apiService.rentalUserPropertyViewDetails(user_property_id)

    suspend fun uploadUserPropertyRentalPaymentBill(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: MultipartBody.Part
    ): Response<CommonResponse> =
        apiService.uploadUserPropertyRentalPaymentBill(params, document)

    suspend fun profileDetails(): Response<ProfileDetailsResponse> = apiService.profileDetails()

    suspend fun updateProfileDetails(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        profile_pic: MultipartBody.Part
    ): Response<UpdateProfileResponse> =
        apiService.updateProfileDetails(params, profile_pic)

    suspend fun updateProfileDetails(
        params: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<UpdateProfileResponse> =
        apiService.updateProfileDetails(params)

    suspend fun removeProfilePic(): Response<CommonResponse> =
        apiService.removeProfilePic()

    suspend fun faqListDetails(page: String):
            Response<FAQResponse> = apiService.faqListDetails(page)

    suspend fun sendFeedback(feedback: String):
            Response<CommonResponse> = apiService.sendFeedback(feedback)

    suspend fun privacyPolicyListDetails(page: String):
            Response<PrivacyPolicyResponse> = apiService.privacyPolicyListDetails(page)

    suspend fun legalInformationListDetails(page: String):
            Response<LegalInformationResponse> = apiService.legalInformationListDetails(page)

    suspend fun userLogout():
            Response<CommonResponse> = apiService.userLogout()

    suspend fun propertyRentalDetails(property_id: String):
            Response<PropertyRentalDetailsResponse> = apiService.propertyRentalDetails(property_id)

    suspend fun eventPackageDetails(event_id: Int):
            Response<EventPackageResponse> = apiService.eventPackageDetails(event_id)

    suspend fun eventBookingPackageDetails(package_id: Int):
            Response<EventBookingPackageDetailsResponse> =
        apiService.eventBookingPackageDetails(package_id)

    suspend fun eventBookPackage(
        package_id: String, name: String, email: String,
        phone: String, amount: String, person_count: String
    ):
            Response<EventBookPackageResponse> = apiService.eventBookPackage(
        package_id,
        name, email, phone, amount, person_count
    )

    suspend fun uploadEventPackagePaymentBill(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        document: MultipartBody.Part
    ): Response<CommonResponse> =
        apiService.uploadEventPackagePaymentBill(params, document)

    suspend fun fetchRequestedPropertyData(): Response<FetchRequestedPropertyDataResponse> =
        apiService.fetchRequestedPropertyData()

    suspend fun requestDesiredProperty(
        property_to: String, category: String, type_id: String,
        area: String, min_price: BigInteger, max_price: BigInteger, latitude: String,
        longitude: String, description: String, location: String
    ):
            Response<CommonResponse> = apiService.requestDesiredProperty(
        property_to, category, type_id, area,
        min_price, max_price, latitude, longitude, description, location
    )

    suspend fun desiredPropertyRequestList(page: String): Response<DesiredPropertyRequestListResponse> =
        apiService.desiredPropertyRequestList(page)

    suspend fun desiredPropertyRequestDetails(request_id: String): Response<DesiredPropertyRequestDetailsResponse> =
        apiService.desiredPropertyRequestDetails(request_id)

    suspend fun getNearByPlaces(
        location: String, radius: String,
        type: String, keyword: String, key: String
    ): Response<GoogleResponseModel> =
        apiService.getNearByPlaces(location, radius, type, keyword, key)

    suspend fun showReferralCode(): Response<ReferralResponse> =
        apiService.showReferralCode()

    suspend fun listingPropertyLocationFromHome(property_id: String): Response<HomeListingPropertyLocationResponse> =
        apiService.listingPropertyLocationFromHome(property_id)

    suspend fun showAllNearbyPropertyLocationMap(
        property_id: String,
        latitude: String,
        longitude: String
    ):
            Response<ShowAllNearbyPropertyDetailsMapResponse> =
        apiService.showAllNearbyPropertyLocationMap(property_id, latitude, longitude)

    suspend fun propertyLocationDetailsMap(property_id: String): Response<PropertyLocationDetailsResponse> =
        apiService.propertyLocationDetailsMap(property_id)

    suspend fun showAllPropertyLocations(latitude: String, longitude: String):
            Response<ShowAllPropertyLocationResponse> =
        apiService.showAllPropertyLocations(latitude, longitude)

    suspend fun notification(page: String): Response<NotificationResponse> =
        apiService.notification(page)

    suspend fun readNotificationUpdate(notification_id: String): Response<ReadNotificationResponse> =
        apiService.readNotificationUpdate(notification_id)

    suspend fun getTermsOfStay(property_id: String): Response<TermsOfStayResponse> =
        apiService.getTermsOfStay(property_id)

    suspend fun userRewards(): Response<UserRewaredResponse> =
        apiService.userRewards()

    suspend fun getHomeFilterCategories(category: String): Response<ResidentialCommercialResponse> =
        apiService.getHomeFilterCategories(category)

    suspend fun getHomeFilterPropertyList(
        page: String,
        property_to: String,
        type_id: ArrayList<Int>,
        category: String,
        min_price: String,
        max_price: String,
        bed_id: String,
        bed: ArrayList<Int>,
        max_bed: String
    ): Response<PropertyListResponse> =
        apiService.getHomeFilterPropertyList(
            page,
            property_to,
            type_id,
            category,
            min_price,
            max_price,
            bed_id,
            bed,
            max_bed
        )

    suspend fun scheduledPropertyList(page: String): Response<ScheduledPropertyListResponse> =
        apiService.scheduledPropertyList(page)

    suspend fun mainFilterDetails(): Response<FilterDetailsResponse> =
        apiService.mainFilterDetails()

    suspend fun getMainFilterPropertyList(
        page: String,
        property_to: String,
        type_id: ArrayList<Int>,
        category: String,
        min_price: String,
        max_price: String,
        bed_id: String,
        bed: ArrayList<Int>,
        max_bed: String,
        bathroom_id: String,
        bathroom: ArrayList<Int>,
        max_bathroom: String,
        area_id: String,
        min_area: String,
        max_area: String,
        amenities: ArrayList<Int>,
        lat: String,
        lan: String
    ): Response<PropertyListResponse> =
        apiService.getMainFilterPropertyList(
            page, property_to, type_id, category, min_price, max_price, bed_id, bed, max_bed,
            bathroom_id, bathroom, max_bathroom, area_id, min_area, max_area, amenities, lat, lan
        )

    suspend fun searchNamePropertyList(
        page: String,
        property_name: String
    ): Response<PropertyListResponse> =
        apiService.searchNamePropertyList(page, property_name)

    suspend fun packagesDetails(property_id: String): Response<PackagesResponse> =
        apiService.packagesDetails(property_id)

    suspend fun becomeAnOwner(
        name: String,
        phone: String,
        email: String,
        no_of_rental_properties: String,
        no_of_sale_properties: String,
        property_rel: String
    ):
            Response<CommonResponse> = apiService.becomeAnOwner(
        name,
        phone,
        email,
        no_of_rental_properties,
        no_of_sale_properties,
        property_rel
    )

    suspend fun userPaymentHistory(
        page: String,
        user_property_id: String
    ): Response<PaymentHistoryResponse> =
        apiService.userPaymentHistory(page, user_property_id)

    suspend fun ratingAndFeedback(property_id: String, rating: String, agent_feedback: String):
            Response<CommonResponse> =
        apiService.ratingAndFeedback(property_id, rating, agent_feedback)

    suspend fun googleLogin(
        name: String, email: String, phone: String, google_id: String,
        facebook_id: String, apple_id: String, login_type: String
    ):
            Response<GoogleLoginResponse> =
        apiService.googleLogin(name, email, phone, google_id, facebook_id, apple_id, login_type)

    suspend fun downloadPropertyDetailsPdf(property_id: String):
            Response<PropertyDetailsPdfResponse> =
        apiService.downloadPropertyDetailsPdf(property_id)

}
