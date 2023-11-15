package com.property.propertyuser.data.api

import com.androidnetworking.interceptors.HttpLoggingInterceptor
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
import com.property.propertyuser.pojo.test.TestResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.math.BigInteger
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

interface ApiService {

    @GET("posts")
    suspend fun getUsers(): Response<TestResponse>

    @FormUrlEncoded
    @POST("phone-verification")
    suspend fun getPhoneVerification(@Field("phone") phone: String): Response<PhoneVerificationResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun getLoginDetailsFromOtp(
        @Field("phone") phone: String,
        @Field("otp") otp: Int,
        @Field("lat") lat: String,
        @Field("lng") lng: String
    ): Response<LoginOtpVerificationResponse>

    @FormUrlEncoded
    @POST("sign-up")
    suspend fun signUp(
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("lat") lat: String,
        @Field("lan") lan: String,
        @Field("location") location: String,
        @Field("referral_code") referral_code: String
    ): Response<SignUpResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("phone") phone: String,
        @Field("otp") otp: String,
        @Field("device_key") device_token: String
    ): Response<OtpResponse>

    @FormUrlEncoded
    @POST("phone-verification")
    suspend fun phoneVerification(@Field("phone") phone: String): Response<SignUpResponse>

    //Resend otp
    @FormUrlEncoded
    @POST("resend-otp")
    suspend fun phoneVerificationResendOtp(@Field("phone") phone: String): Response<SignUpResponse>


    @FormUrlEncoded
    @POST("list-property")
    suspend fun listProperty(
        @Field("lat") lat: String,
        @Field("lan") lan: String,
        @Field("page") page: String
    ): Response<PropertyListResponse>

    @FormUrlEncoded
    @POST("property-details")
    suspend fun getPropertyDetailsResponse(@Field("property_id") property_id: Int): Response<PropertyDetilsResponse>

    //Add Property to Favourite
    @FormUrlEncoded
    @POST("update-favourite")
    suspend fun updateFavouriteProperty(@Field("property_id") property_id: Int): Response<CommonResponse>

    @FormUrlEncoded
    @POST("list-property")
    suspend fun listPropertyFavourite(
        @Field("page") page: Int,
        @Field("favourite_list") favourite_list: Int
    ): Response<FavoriteListResponse>

    //User Tour Booking
    @FormUrlEncoded
    @POST("book-tour")
    suspend fun bookTourAddDateTime(
        @Field("property_id") property_id: String,
        @Field("date") date: String,
        @Field("time_range") time_range: String
    ): Response<BookTourDialogResponse>

    //Tour booking Confirmation page Details
    @FormUrlEncoded
    @POST("confirm-booking-details")
    suspend fun bookingTour(
        @Field("tour_id") tour_id: String,
        @Field("property_id") property_id: String
    ): Response<BookingTourResponse>

    //Tour booking confirmation
    @FormUrlEncoded
    @POST("confirm-tour-booking")
    suspend fun bookingTourConfirmation(
        @Field("tour_id") tour_id: String
    ): Response<CommonResponse>

    //list events
    @FormUrlEncoded
    @POST("list-events")
    suspend fun listEvents(
        @Field("lat") lat: String,
        @Field("lan") lan: String,
        @Field("page") page: String
    ): Response<EventListResponse>

    //event details
    @FormUrlEncoded
    @POST("event-details")
    suspend fun eventDetails(
        @Field("event_id") event_id: String
    ): Response<EventDetailsResponse>

    //Book property details
    @GET("book-property")
    suspend fun bookPropertyDetails(
        @Query("property_id") property_id: String
    ): Response<BookPropertyDetailsResponse>

    //Book property
    @FormUrlEncoded
    @POST("book-property")
    suspend fun bookProperty(
        @Field("property_id") property_id: String,
        @Field("check_in") check_in: String,
        @Field("check_out") check_out: String,
        @Field("coupon") coupon: String
    ): Response<BookPropertyResponse>

    //Upload payment bill of booking
    @Multipart
    @POST("upload-book-paymentbill")
    suspend fun uploadBookPaymentBill(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: MultipartBody.Part
    ): Response<CommonResponse>

    //my properties User Property listing
    @GET("list-user-properties")
    suspend fun myPropertiesListDetails(
        @Query("page") page: String,
        @Query("user_property_status") user_property_status: String
    ): Response<MyPropertiesResponse>

    //Proceed Booked property details
    @GET("booked-property-details")
    suspend fun proceedBookedPropertyDetails(
        @Query("property_id") property_id: String
    ): Response<ProceedBookPropertyDetailsResponse>

    //Upload User property documents
    @Multipart
    @POST("upload-property-documents")
    suspend fun uploadPropertyDocuments(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: List<MultipartBody.Part>
    ): Response<CommonResponse>

    //Upload User paid documents
    @Multipart
    @POST("upload-user-property-paymentbill")
    suspend fun uploadUserPropertyBookPaymentBill(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: MultipartBody.Part
    ): Response<CommonResponse>

    //Cancel user booked property
    @Multipart
    @POST("cancel-booked-property")
    suspend fun cancelBookedProperty(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<CommonResponse>

    //service list
    @POST("list-services")
    suspend fun serviceListDetails(
        @Query("service_name") service_name: String,
        @Query("page") page: String,
        @Query("property_id") property_id: String
    ): Response<ServiceListResponse>

    //Request for service
    @Multipart
    @POST("request-services")
    suspend fun uploadRequestForServiceDetails(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: List<MultipartBody.Part>
    ): Response<CommonResponse>

    //List Requested services
    @Multipart
    @POST("list-requested-service")
    suspend fun listRequestService(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<RequestedServiceDetailsResponse>

    //Requested services details
    @Multipart
    @POST("requested-service")
    suspend fun requestedServiceDetails(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<SingleRequestedServiceDetailsResponse>

    //upload Pay for Requested service
    @Multipart
    @POST("user-service-paymentbill")
    suspend fun uploadUserPropertyServicePaymentBill(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: MultipartBody.Part
    ): Response<CommonResponse>

    //Sending request to cancel requested Service
    @Multipart
    @POST("cancel-service-request")
    suspend fun cancelServiceRequest(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<CommonResponse>

    //Vacate Request for a property
    @FormUrlEncoded
    @POST("vacate-request")
    suspend fun vacateRequestForProperty(
        @Field("user_property_id") user_property_id: String,
        @Field("vacate_date") vacate_date: String
    ): Response<CommonResponse>

    //List user vacate requests
    @POST("list-vacate-request")
    suspend fun vacateRequestList(
        @Query("page") page: String,
        @Query("user_property_id") property_id: String
    ): Response<VacateRequestListResponse>

    //List user vacate requests
    @POST("vacate-request-details")
    suspend fun singleVacateRequestDetails(
        @Query("vacate_id") vacate_id: String
    ): Response<SingleVacateRequestDetailsResponse>

    //Rental User property view details
    @POST("user-property-detail")
    suspend fun rentalUserPropertyViewDetails(
        @Query("user_property_id") user_property_id: String
    ): Response<RentalUserPropertyViewDetailsResponse>

    //upload User pay property rent
    @Multipart
    @POST("property-rent-paymentbill")
    suspend fun uploadUserPropertyRentalPaymentBill(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: MultipartBody.Part
    ): Response<CommonResponse>

    //View user profile details
    @GET("profile")
    suspend fun profileDetails(
    ): Response<ProfileDetailsResponse>

    //update user profile
    @Multipart
    @POST("update-profile")
    suspend fun updateProfileDetails(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part profile_pic: MultipartBody.Part
    ): Response<UpdateProfileResponse>

    @Multipart
    @POST("update-profile")
    suspend fun updateProfileDetails(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<UpdateProfileResponse>

    //remove-profilepic
    @POST("remove-profilepic")
    suspend fun removeProfilePic(
    ): Response<CommonResponse>

    //Listing FAQ
    @POST("faq")
    suspend fun faqListDetails(
        @Query("page") page: String
    ): Response<FAQResponse>

    //send feedback
    @POST("send-feedback")
    suspend fun sendFeedback(
        @Query("feedback") feedback: String
    ): Response<CommonResponse>

    //Listing Privacy Policy
    @POST("privacy-policy")
    suspend fun privacyPolicyListDetails(
        @Query("page") page: String
    ): Response<PrivacyPolicyResponse>

    //Listing Legal Information
    @POST("legal-information")
    suspend fun legalInformationListDetails(
        @Query("page") page: String
    ): Response<LegalInformationResponse>

    //User Logout
    @POST("logout")
    suspend fun userLogout(
    ): Response<CommonResponse>

    //Property rental details
    @POST("rental-details")
    suspend fun propertyRentalDetails(
        @Query("property_id") property_id: String
    ): Response<PropertyRentalDetailsResponse>

    //Event Packages
    @POST("event-packages")
    suspend fun eventPackageDetails(
        @Query("event_id") event_id: Int
    ): Response<EventPackageResponse>

    //Event package booking details
    @POST("package-details")
    suspend fun eventBookingPackageDetails(
        @Query("package_id") package_id: Int
    ): Response<EventBookingPackageDetailsResponse>

    //Booking event package
    @FormUrlEncoded
    @POST("book-package")
    suspend fun eventBookPackage(
        @Field("package_id") package_id: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("amount") amount: String,
        @Field("person_count") person_count: String
    ): Response<EventBookPackageResponse>

    //Upload event package amount document
    @Multipart
    @POST("user-event-package-paymentbill")
    suspend fun uploadEventPackagePaymentBill(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part document: MultipartBody.Part
    ): Response<CommonResponse>

    //Fetch data for requesting desired property
    @GET("fetch-request-property-data")
    suspend fun fetchRequestedPropertyData(
    ): Response<FetchRequestedPropertyDataResponse>

    //Requesting desired property
    @FormUrlEncoded
    @POST("request-desired-property")
    suspend fun requestDesiredProperty(
        @Field("property_to") property_to: String,
        @Field("category") category: String,
        @Field("type_id") type_id: String,
        @Field("area") area: String,
        @Field("min_price") min_price: BigInteger,
        @Field("max_price") max_price: BigInteger,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("description") description: String,
        @Field("location") location: String
    ): Response<CommonResponse>

    //Desired property request list
    @POST("desired-property-request-list")
    suspend fun desiredPropertyRequestList(
        @Query("page") page: String
    ): Response<DesiredPropertyRequestListResponse>

    // Desired property details
    @POST("desired-property-request-details")
    suspend fun desiredPropertyRequestDetails(
        @Query("request_id") request_id: String
    ): Response<DesiredPropertyRequestDetailsResponse>

    // Show referral code
    @POST("show-referral-code")
    suspend fun showReferralCode(
    ): Response<ReferralResponse>

    //Show each listing property location
    @POST("show-list-property-location")
    suspend fun listingPropertyLocationFromHome(
        @Query("property_id") property_id: String
    ): Response<HomeListingPropertyLocationResponse>

    //Show all near-by property details
    @POST("show-nearby-property-location")
    suspend fun showAllNearbyPropertyLocationMap(
        @Query("property_id") property_id: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): Response<ShowAllNearbyPropertyDetailsMapResponse>

    //Property details in location
    @POST("property-location-details")
    suspend fun propertyLocationDetailsMap(
        @Query("property_id") property_id: String
    ): Response<PropertyLocationDetailsResponse>

    //Show all property location
    @POST("all-property-locations")
    suspend fun showAllPropertyLocations(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): Response<ShowAllPropertyLocationResponse>

    //Notifications
    @POST("user-notifications")
    suspend fun notification(
        @Query("page") page: String
    ): Response<NotificationResponse>

    //Notifications Read Update
    @POST("read-notifications")
    suspend fun readNotificationUpdate(
        @Query("notification_id") notification_id: String
    ): Response<ReadNotificationResponse>

    //Terms of stay
    @POST("terms-of-stay/{property_id}")
    suspend fun getTermsOfStay(
        @Path("property_id") property_id: String
    ): Response<TermsOfStayResponse>

    //User Rewards
    @POST("user-rewards")
    suspend fun userRewards(): Response<UserRewaredResponse>

    //Categories list for filter
    @POST("categories")
    suspend fun getHomeFilterCategories(
        @Query("category") category: String
    ): Response<ResidentialCommercialResponse>

    //User Home screen filter
    @POST("list-property-sort")
    suspend fun getHomeFilterPropertyList(
        @Query("page") page: String,
        @Query("property_to") property_to: String,
        @Query("type_id[]") type_id: ArrayList<Int>,
        @Query("category") category: String,
        @Query("min_price") min_price: String,
        @Query("max_price") max_price: String,
        @Query("bed_id") bed_id: String,
        @Query("bed[]") bed: ArrayList<Int>,
        @Query("max_bed") max_bed: String
    ): Response<PropertyListResponse>

    //User Scheduled Tour list API
    @POST("scheduled-tour-list")
    suspend fun scheduledPropertyList(
        @Query("page") page: String
    ): Response<ScheduledPropertyListResponse>

    //Property Types for Filter
    @POST("filter-property-details")
    suspend fun mainFilterDetails(): Response<FilterDetailsResponse>

    //Main filter
    @POST("list-property-filter")
    suspend fun getMainFilterPropertyList(
        @Query("page") page: String,
        @Query("property_to") property_to: String,
        @Query("type_id[]") type_id: ArrayList<Int>,
        @Query("category") category: String,
        @Query("min_price") min_price: String,
        @Query("max_price") max_price: String,
        @Query("bed_id") bed_id: String,
        @Query("bed[]") bed: ArrayList<Int>,
        @Query("max_bed") max_bed: String,
        @Query("bathroom_id") bathroom_id: String,
        @Query("bathroom[]") bathroom: ArrayList<Int>,
        @Query("max_bathroom") max_bathroom: String,
        @Query("area_id") area_id: String,
        @Query("min_area") min_area: String,
        @Query("max_area") max_area: String,
        @Query("amenities[]") amenities: ArrayList<Int>,
        @Query("lat") lat: String,
        @Query("lan") lan: String
    ): Response<PropertyListResponse>

    //Search Property Name API
    @POST("list-property-sort")
    suspend fun searchNamePropertyList(
        @Query("page") page: String,
        @Query("property_name") property_name: String
    ): Response<PropertyListResponse>

    //Other package list API
    @POST("other-packages")
    suspend fun packagesDetails(
        @Query("property_id") property_id: String
    ): Response<PackagesResponse>

    //Become an Owner
    @FormUrlEncoded
    @POST("become-owner")
    suspend fun becomeAnOwner(
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("no_of_rental_properties") no_of_rental_properties: String,
        @Field("no_of_sale_properties") no_of_sale_properties: String,
        @Field("property_rel") property_rel: String
    ): Response<CommonResponse>

    //User payment History
    @POST("user-payment-history")
    suspend fun userPaymentHistory(
        @Query("page") page: String,
        @Query("user_property_id") user_property_id: String
    ): Response<PaymentHistoryResponse>

    //google map near by places
    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearByPlaces(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("type") type: String,
        @Query("keyword") keyword: String,
        @Query("key") key: String
    ): Response<GoogleResponseModel>

    //Updating Property rating and Agent Feedback API
    @POST("agent-feed-and-property-rating")
    suspend fun ratingAndFeedback(
        @Query("property_id") property_id: String,
        @Query("rating") rating: String,
        @Query("agent_feedback") agent_feedback: String
    ): Response<CommonResponse>

    //google login
    @FormUrlEncoded
    @POST("login-with-other")
    suspend fun googleLogin(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("google_id") google_id: String,
        @Field("facebook_id") facebook_id: String,
        @Field("apple_id") apple_id: String,
        @Field("login_type") login_type: String
    ): Response<GoogleLoginResponse>

    //Download pdf url of property details
    @FormUrlEncoded
    @POST("download-property-details-pdf")
    suspend fun downloadPropertyDetailsPdf(
        @Field("property_id") property_id: String
    ): Response<PropertyDetailsPdfResponse>

    companion object {
        private const val BASE_URL = "http://admin.siaaha.com/api/user/"
        fun create(apiType: String): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            val retrofitForGoogleMaps = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com/")
                .build()
            return if (apiType == "API")
                return retrofit.create(ApiService::class.java)
            else
                retrofitForGoogleMaps.create(ApiService::class.java)

        }

        fun initializeClient(): OkHttpClient {
            return try {
                // Create a trust manager that does not validate certificate chains
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
                val trustManager = trustManagers[0] as X509TrustManager

                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.HEADERS
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                System.setProperty("http.keepAlive", "false")
                val builder = OkHttpClient.Builder()
                builder
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier(HostnameVerifier { _, _ -> true })
                    .addInterceptor(interceptor)
                    .addInterceptor(SupportInterceptor())
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }


    }
}