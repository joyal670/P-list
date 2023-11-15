package com.proteinium.proteiniumdietapp.data.api

import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.google.gson.JsonObject
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.pojo.about.AboutResponse
import com.proteinium.proteiniumdietapp.pojo.add_subscription.AddSubscriptionResponse
import com.proteinium.proteiniumdietapp.pojo.addon.AddOnResponse
import com.proteinium.proteiniumdietapp.pojo.areas.AreaResponse
import com.proteinium.proteiniumdietapp.pojo.blocks.BlockResponse
import com.proteinium.proteiniumdietapp.pojo.dislike_tags.TagsResponse
import com.proteinium.proteiniumdietapp.pojo.edit_address.EditSingleAddressResponse
import com.proteinium.proteiniumdietapp.pojo.extras_update.ExtrasUpdateResponse
import com.proteinium.proteiniumdietapp.pojo.getNotification.GetNotificationResponse
import com.proteinium.proteiniumdietapp.pojo.get_addresses.UserAddressesResponse
import com.proteinium.proteiniumdietapp.pojo.globalresponse.GlobalResponse
import com.proteinium.proteiniumdietapp.pojo.governorate.GovernorateResponse
import com.proteinium.proteiniumdietapp.pojo.home.HomeResponse
import com.proteinium.proteiniumdietapp.pojo.login.LoginResponse
import com.proteinium.proteiniumdietapp.pojo.meal_plan.MealPlanResponse
import com.proteinium.proteiniumdietapp.pojo.menu_day.MenuResponse
import com.proteinium.proteiniumdietapp.pojo.myprofile.MyProfileResponse
import com.proteinium.proteiniumdietapp.pojo.notifications.NotificationsResponse
import com.proteinium.proteiniumdietapp.pojo.placeorder.PlaceOrderResponse
import com.proteinium.proteiniumdietapp.pojo.promocode.PrmocodeResponse
import com.proteinium.proteiniumdietapp.pojo.subscption_info.SubscriptionResponse
import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.SubscriptionMealPlanDetailsResponse
import com.proteinium.proteiniumdietapp.pojo.subscriptions_history.SubscriptionsHistoryResponse
import com.proteinium.proteiniumdietapp.pojo.user.UserResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    //sign up
    @FormUrlEncoded
    @POST("register")
    suspend fun addCustomerSignup(
        @Field("first_name") first_name: String,
        @Field("middle_name") middle_name: String,
        @Field("last_name") last_name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("alternative_phone") alternative_phone: String,
        @Field("password") password: String,
        @Field("gender") gender: String,
        @Field("status") status: Int,
        @Field("lang_id") lang_id: String,
        @Field("device_token") device_token: String
    ): Response<CommonResponse>

    //login
    @FormUrlEncoded
    @POST("login")
    suspend fun getLoginDetails(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("lang_id") lang_id: String,
        @Field("device_token") device_token: String
    ): Response<LoginResponse>

    //governorate
    @GET("get-governorates")
    suspend fun getGovernorates(@Query("lang_id") lang_id: String): Response<GovernorateResponse>

    //get areas
    @GET("get-areas/{governorate_id}")
    suspend fun getAreas(
        @Path("governorate_id") governorate_id: String,
        @Query("lang_id") lang_id: String
    ): Response<AreaResponse>

    //get-blocks
    @GET("get-blocks/{area_id}")
    suspend fun getBlocks(
        @Path("area_id") area_id: String,
        @Query("lang_id") lang_id: String
    ): Response<BlockResponse>

    //add address
    @FormUrlEncoded
    @POST("add-address")
    suspend fun addAddress(
        @Field("user_id") user_id: Int,
        @Field("governorate_id") governorate_id: Int,
        @Field("area_id") area_id: Int,
        @Field("block") block: String,
        @Field("avenue") avenue: String,
        @Field("street") street: String,
        @Field("building") building: String,
        @Field("floor") floor: String,
        @Field("appartment") appartment: String,
        @Field("default") default: Int,
        @Field("lang_id") lang_id: String,
        @Field("additonal_directions") additional_direction: String
    ): Response<CommonResponse>

    //update-address
    @FormUrlEncoded
    @POST("update-address")
    suspend fun updateAddress(
        @Field("id") id: Int,
        @Field("user_id") user_id: Int,
        @Field("governorate_id") governorate_id: Int,
        @Field("area_id") area_id: Int,
        @Field("block") block: String,
        @Field("avenue") avenue: String,
        @Field("street") street: String,
        @Field("building") building: String,
        @Field("floor") floor: String,
        @Field("appartment") appartment: String,
        @Field("default") default: Int,
        @Field("lang_id") lang_id: String,
        @Field("additonal_directions") additional_direction: String
    ): Response<CommonResponse>

    //get-addresses/{user_id}
    @GET("get-addresses/{user_id}")
    suspend fun getUserAddresses(
        @Path("user_id") user_id: Int,
        @Query("lang_id") lang_id: String
    ): Response<UserAddressesResponse>

    //Delete Address
    @GET("delete-address/{addres_id}")
    suspend fun deleteAddress(
        @Path("addres_id") addres_id: Int,
        @Query("lang_id") lang_id: String
    ): Response<CommonResponse>

    //set-default-address
    @FormUrlEncoded
    @POST("set-default-address")
    suspend fun setDefaultAddress(
        @Field("user_id") user_id: Int,
        @Field("address_id") address_id: Int,
        @Field("lang_id") lang_id: String
    ): Response<CommonResponse>

    //get-address/{address_id}
    @GET("get-address-to-edit/{address_id}")
    suspend fun getSingleAddressEdit(
        @Path("address_id") address_id: Int,
        @Query("lang_id") lang_id: String
    ): Response<EditSingleAddressResponse>

    //get-mealcategories
    @FormUrlEncoded
    @POST("get-mealcategories")
    suspend fun getHomeDetails(
        @Field("lang_id") lang_id: String,
        @Field("user_id") user_id: Int
    ): Response<HomeResponse>

    @GET("about-us")
    suspend fun getAbout(@Query("lang_id") lang_id: String): Response<AboutResponse>

    @FormUrlEncoded
    @POST("get-mealplan-info/{meal_plan_id}")
    suspend fun getMealPlan(
        @Path("meal_plan_id") meal_plan_id: Int,
        @Field("lang_id") lang_id: String,
        @Field("user_id") user_id: Int
    ): Response<MealPlanResponse>

    //get-userinfo
    @FormUrlEncoded
    @POST("get-userinfo")
    suspend fun getUserInfo(
        @Field("user_id") user_id: Int,
        @Field("lang_id") lang_id: String
    ): Response<MyProfileResponse>

    //update-address
    @Multipart
    @POST("update-userinfo")
    suspend fun updateUserInfo(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part
    ): Response<UserResponse>

    @Multipart
    @POST("update-userinfo")
    suspend fun updateUserInfoWithNoImage(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<UserResponse>


    //contact us
    @FormUrlEncoded
    @POST("contactus")
    suspend fun contactUs(
        @Field("user_id") user_id: Int,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("message") message: String,
        @Field("lang_id") lang_id: String
    ): Response<CommonResponse>

    @GET("get-tags/{user_id}")
    suspend fun getTags(
        @Path("user_id") user_id: Int,
        @Query("lang_id") lang_id: String
    ): Response<TagsResponse>


    @FormUrlEncoded
    @POST("add-initial-subscription-info")
    suspend fun addInitialSubscriptionPlan(
        @Field("user_id") user_id: Int,
        @Field("start_date") start_date: String,
        @Field("meal_plan_id") meal_plan_id: Int,
        @Field("non_stop_delivery_price") non_stop_delivery_price: String,
        @Field("carbs") carbs: String,
        @Field("carbs_price") carbs_price: String,
        @Field("proteins") proteins: String,
        @Field("proteins_price") proteins_price: String,
        @Field("comments") comments: String,
        @Field("duration") duration: Int,
        @Field("base_price") base_price: String,
        @Field("code") code: String,
        @Field("suspend") suspend: String,
        @Field("lang_id") lang_id: String,
        @Field("enable_modification") enable_modification: Int,
        @Field("extras[]") extras: ArrayList<String>
    ): Response<AddSubscriptionResponse>

    @GET("privacy-policy")
    suspend fun getPrivacyPolicy(@Query("lang_id") lang_id: String): Response<AboutResponse>


    @GET("terms-conditions")
    suspend fun getTermsConditions(@Query("lang_id") lang_id: String): Response<AboutResponse>

    @FormUrlEncoded
    @POST("set-disliked-tags")
    suspend fun setDislikedTags(
        @Field("user_id") user_id: Int,
        @Field("tag_ids[]") tag_ids: ArrayList<Int>,
        @Field("lang_id") lang_id: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("get-subscription-info")
    suspend fun getSubscriptionInfo(
        @Field("user_id") user_id: Int,
        @Field("lang_id") lang_id: String
    ): Response<SubscriptionResponse>

    @FormUrlEncoded
    @POST("get-menu-for-day")
    suspend fun getMenuForDay(
        @Field("user_id") user_id: Int,
        @Field("date") date: String,
        @Field("lang_id") lang_id: String
    ): Response<MenuResponse>

    @FormUrlEncoded
    @POST("add-final-subscription-info")
    suspend fun addFinalSubscription(
        @Field("user_id") user_id: Int,
        @Field("meal_plan_subscription_id") meal_plan_subscription_id: Int,
        @Field("payment_status") payment_status: Int,
        @Field("adress_id") adress_id: Int,
        @Field("payment_method") payment_method: String,
        @Field("promo_code") promo_code: String,
        @Field("lang_id") lang_id: String,
        @Field("payment_reference") payment_reference: String,
        @Field("unique_key") unique_key: String,
        @Field("renewal") renewal: Int
    ): Response<CommonResponse>


    //get meal plan subscriptions history
    @GET("get-subscription-history/{user_id}")
    suspend fun getSubscriptionHistory(
        @Path("user_id") user_id: Int,
        @Query("lang_id") lang_id: String
    ): Response<SubscriptionsHistoryResponse>

    //get meal plan subscription preview info
    @GET("get-subscription-preview/{meal_plan_subscription_id}")
    suspend fun getSubscriptionPreviewDetails(
        @Path("meal_plan_subscription_id") meal_plan_subscription_id: Int,
        @Query("lang_id") lang_id: String,
        @Query("renewal_status") renewal_status: Boolean
    ): Response<SubscriptionMealPlanDetailsResponse>
    //place order
    @FormUrlEncoded

    @POST("place-order")
    suspend fun getPlaceOrder(
        @Field("user_id") user_id: Int,
        @Field("date") date: String,
        @Field("food_ids[]") food_ids: ArrayList<Int>,
        @Field("lang_id") lang_id: String,
        @Field("meal_plan_subscription_id") meal_plan_subscription_id: String
    ): Response<PlaceOrderResponse>

    //get notifications list
    @GET("get-notifications/{user_id}")
    suspend fun getNotifications(
        @Path("user_id") user_id: Int,
        @Query("lang_id") lang_id: String
    ): Response<NotificationsResponse>

    //Update notification seen status
    @GET("update-notification-status/{notification_id}")
    suspend fun updateNotificationStatus(
        @Path("notification_id") notification_id: Int,
        @Query("lang_id") lang_id: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("suspend-unsuspend-delivery")
    suspend fun suspendUnsuspendDelivery(
        @Field("user_id") user_id: Int,
        @Field("date") date: String,
        @Field("lang_id") lang_id: String,
        @Field("plan_end_date") plan_end_date: String,
        @Field("upcoming_plan_start_date") upcoming_plan_start_date: String,
        @Field("upcoming_status") upcoming_status: Boolean,
        @Field("active_plan_end_date") active_plan_end_date: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("check-promo-code")
    suspend fun checkPromoCode(
        @Field("user_id") user_id: Int,
        @Field("total") total: String,
        @Field("code") code: String,
        @Field("lang_id") lang_id: String,
        @Field("meal_plan_id") meals_plan_id: Int

    ): Response<PrmocodeResponse>

    @FormUrlEncoded
    @POST("rate-food")
    suspend fun rateFood(
        @Field("user_id") user_id: Int,
        @Field("food_id") food_id: Int,
        @Field("rating") rating: Int,
        @Field("lang_id") lang_id: String,
        @Field("date") date: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("get-password-reset-form")
    suspend fun resetPassword(
        @Field("email") email: String,
        @Field("lang_id") lang_id: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("set-push-notification-status")
    suspend fun setPushNotification(
        @Field("user_id") user_id: Int,
        @Field("notification_status") notification_status: Int,
        @Field("lang_id") lang_id: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("get-push-notification-status")
    suspend fun getPushNotification(
        @Field("user_id") user_id: Int,
        @Field("lang_id") lang_id: String
    ): Response<GetNotificationResponse>

    @FormUrlEncoded
    @POST("update-password")
    suspend fun updatePassword(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String,
        @Field("lang_id") lang_id: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("cancel-subscription")
    suspend fun cancelSubcription(
        @Field("user_id") user_id: Int,
        @Field("meal_plan_subscription_id") meal_plan_subscription_id: Int,
        @Field("lang_id") lang_id: String,
        @Field("renewal_status") renewal_status: Boolean
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("get-addons-for-updating")
    suspend fun getAddOnProteins(
        @Field("lang_id") lang_id: String,
        @Field("meal_plan_subscription_id") meal_plan_subscription_id: Int,
        @Field("renewal_status") renewal_status: Boolean
    ): Response<AddOnResponse>

    @FormUrlEncoded
    @POST("update-addons")
    suspend fun updateAddOn(
        @Field("user_id") user_id: Int,
        @Field("meal_plan_subscription_id") meal_plan_subscription_id: Int,
        @Field("carbs") carbs: String,
        @Field("carbs_price") carbs_price: String,
        @Field("proteins") proteins: String,
        @Field("proteins_price") proteins_price: String,
        @Field("payment_id") payment_id: String,
        @Field("total") total: String,
        @Field("lang_id") lang_id: String,
        @Field("renewal_status") renewal_status: Boolean,
        @Field("extras[]") extras: ArrayList<String>
    ): Response<CommonResponse>

    @GET("get-global-suspensions")
    suspend fun getGlobalSuspension(): Response<GlobalResponse>

    @FormUrlEncoded
    @POST("get-extras")
    suspend fun getExtras(
        @Field("lang_id") lang_id: String,
        @Field("meal_plan_subscription_id") meal_plan_subscription_id: Int,
        @Field("renewal_status") renewal_status: Boolean,
        @Field("user_id")user_id:Int
    ):Response<ExtrasUpdateResponse>

    companion object {
        private const val LOCAL_BASE_URL = "https://staging.proteiniumkw.com/api/v1/"
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl(LOCAL_BASE_URL)
                .build()
            return retrofit.create(ApiService::class.java)

        }

        private fun initializeClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            System.setProperty("http.keepAlive", "false");
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
                .addInterceptor(SupportInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)


            return builder.build()
        }


    }

}
