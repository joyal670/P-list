package com.proteinium.proteiniumdietapp.data.api

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
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.chooseLanguage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class MainRepository(private val apiService: ApiService) {
    suspend fun addCustomerSignup(
        first_name: String,
        middle_name:String,
        last_name:String, email: String, phone: String,
        alternative_phone: String,
        password: String, gender: String, status: Int,
        device_token: String,


    ): Response<CommonResponse> =
        apiService.addCustomerSignup(
            first_name,
            middle_name,
            last_name,
            email,
            phone,
            alternative_phone,
            password,
            gender,
            status,
            chooseLanguage!!,
            device_token

        )

    suspend fun addInitialSubscriptionPlan(
        user_id:Int,
        start_date:String,
        meal_plan_id:Int,
        non_stop_delivery_price:String,
        carbs:String,
        carbs_price:String,
        proteins:String,
        proteins_price:String,
        comments:String,
        duration:Int,
        base_price:String,
        code:String,
        suspend:String,
        enable_modification:Int,
        extras:ArrayList<String>
    ): Response<AddSubscriptionResponse> = apiService.addInitialSubscriptionPlan(
        user_id,
        start_date,
        meal_plan_id,
        non_stop_delivery_price,
        carbs,
        carbs_price,
        proteins,
        proteins_price,
        comments,
        duration,
        base_price,
        code,
        suspend,
        chooseLanguage!!,
        enable_modification,
        extras
    )


    suspend fun getPrivacyPolicy(): Response<AboutResponse> =
        apiService.getPrivacyPolicy(chooseLanguage!!)

    suspend fun getTermsConditions(): Response<AboutResponse> =
        apiService.getTermsConditions(chooseLanguage!!)


    suspend fun getLoginDetails(
        email: String,
        password: String,
        device_token: String
    ): Response<LoginResponse> =
        apiService.getLoginDetails(email, password, chooseLanguage!!, device_token)

    suspend fun getGovernorates(): Response<GovernorateResponse> =
        apiService.getGovernorates(chooseLanguage!!)

    suspend fun getAreas(governorate_id: String): Response<AreaResponse> =
        apiService.getAreas(governorate_id, chooseLanguage!!)

    suspend fun getBlocks(area_id: String): Response<BlockResponse> =
        apiService.getBlocks(area_id, chooseLanguage!!)

    suspend fun addAddress(
        user_id: Int, governorate_id: Int, area_id: Int,
        block: String, avenue: String,
        street: String, building: String, floor: String, appartment: String, default: Int,
        additional_direction: String
    ): Response<CommonResponse> =
        apiService.addAddress(
            user_id,
            governorate_id,
            area_id,
            block,
            avenue,
            street,
            building,
            floor,
            appartment,
            default,
            chooseLanguage!!,
            additional_direction
        )

    suspend fun getUserAddresses(user_id: Int): Response<UserAddressesResponse> =
        apiService.getUserAddresses(user_id, chooseLanguage!!)

    suspend fun deleteAddress(addres_id: Int): Response<CommonResponse> =
        apiService.deleteAddress(addres_id, chooseLanguage!!)

    suspend fun setDefaultAddress(user_id: Int, address_id: Int): Response<CommonResponse> =
        apiService.setDefaultAddress(user_id, address_id, chooseLanguage!!)


    suspend fun getSingleAddressEdit(address_id: Int): Response<EditSingleAddressResponse> =
        apiService.getSingleAddressEdit(address_id, chooseLanguage!!)

    suspend fun updateAddress(
        id: Int, user_id: Int, governorate_id: Int, area_id: Int,
        block:String, avenue: String,
        street: String, building: String, floor: String, appartment: String, default: Int,
        additional_direction: String
    ): Response<CommonResponse> =
        apiService.updateAddress(
            id,
            user_id,
            governorate_id,
            area_id,
            block,
            avenue,
            street,
            building,
            floor,
            appartment,
            1,
            chooseLanguage!!,
            additional_direction
        )

    suspend fun getHomeDetails(lang_id: String,user_id:Int): Response<HomeResponse> =
        apiService.getHomeDetails(chooseLanguage!!,user_id)

    suspend fun getAbout(): Response<AboutResponse> =
        apiService.getAbout(chooseLanguage!!)

    suspend fun getMealPlan(id: Int, user_id:Int): Response<MealPlanResponse> =
        apiService.getMealPlan(id, chooseLanguage!!,user_id)

    suspend fun getUserInfo(user_id: Int): Response<MyProfileResponse> =
        apiService.getUserInfo(user_id, chooseLanguage!!)

    suspend fun updateUserInfo(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        image: MultipartBody.Part
    ): Response<UserResponse> =
        apiService.updateUserInfo(params, image)

    suspend fun updateUserInfoWithNoImage(
        params: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<UserResponse> =
        apiService.updateUserInfoWithNoImage(params)


    suspend fun contactUs(
        user_id: Int,
        name: String,
        phone: String,
        email: String,
        message: String
    ): Response<CommonResponse> =
        apiService.contactUs(user_id, name, phone, email, message, chooseLanguage!!)

    suspend fun setDislike(user_id: Int, tag_ids: ArrayList<Int>): Response<CommonResponse> =
        apiService.setDislikedTags(user_id, tag_ids, chooseLanguage!!)

    suspend fun getTags(user_id: Int): Response<TagsResponse> =
        apiService.getTags(user_id, chooseLanguage!!)

    suspend fun getSubscriptionInfo(user_id: Int): Response<SubscriptionResponse> =
        apiService.getSubscriptionInfo(user_id, chooseLanguage!!)

    suspend fun getMenuForDay(user_id: Int, date: String): Response<MenuResponse> =
        apiService.getMenuForDay(user_id, date, chooseLanguage!!)

    suspend fun addFinalSubscription(
        user_id: Int,
        meal_plan_subscription_id: Int,
        payment_status: Int,
        adress_id: Int,
        payment_method: String,
        promo_code: String,
        payment_reference: String,
        unique_key:String,
        renewal:Int
    ): Response<CommonResponse> =
        apiService.addFinalSubscription(
            user_id,
            meal_plan_subscription_id,
            payment_status,
            adress_id,
            payment_method,
            promo_code,
            chooseLanguage!!,
            payment_reference,
            unique_key,
            renewal
        )

    suspend fun getSubscriptionHistory(user_id: Int): Response<SubscriptionsHistoryResponse> =
        apiService.getSubscriptionHistory(user_id, chooseLanguage!!)

    suspend fun getSubscriptionPreviewDetails(meal_plan_subscription_id: Int,
                                              renewal_status:Boolean): Response<SubscriptionMealPlanDetailsResponse> =
        apiService.getSubscriptionPreviewDetails(meal_plan_subscription_id, chooseLanguage!!,renewal_status)

    suspend fun placeOrder(
        user_id: Int,
        date: String,
        foodId: ArrayList<Int>,
        meal_plan_subscription_id: String
    ): Response<PlaceOrderResponse> =
        apiService.getPlaceOrder(user_id, date, foodId, chooseLanguage!!,meal_plan_subscription_id)

    suspend fun getNotifications(user_id: Int): Response<NotificationsResponse> =
        apiService.getNotifications(user_id, chooseLanguage!!)

    suspend fun updateNotificationStatus(notification_id: Int): Response<CommonResponse> =
        apiService.updateNotificationStatus(notification_id, chooseLanguage!!)

    suspend fun suspendUnsuspendDelivery(user_id: Int, date: String,plan_end_date
    :String,upcoming_plan_start_date:String,upcoming_status:Boolean,active_plan_end_date:String): Response<CommonResponse> =
        apiService.suspendUnsuspendDelivery(user_id, date, chooseLanguage!!,plan_end_date,upcoming_plan_start_date,upcoming_status,active_plan_end_date)

    suspend fun checkPromoCode(user_id: Int, total: String, code: String, meals_plan_id: Int): Response<PrmocodeResponse> =
        apiService.checkPromoCode(user_id, total, code, chooseLanguage!!, meals_plan_id)

    suspend fun rateFood(user_id: Int, foodId: Int, rating: Int,date: String): Response<CommonResponse> =
        apiService.rateFood(user_id, foodId, rating, chooseLanguage!!,date)

    suspend fun resetPassword(email: String): Response<CommonResponse> =
        apiService.resetPassword(email, chooseLanguage!!)

    suspend fun setPushNotification(
        user_id: Int,
        notification_status: Int
    ): Response<CommonResponse> =
        apiService.setPushNotification(user_id, notification_status, chooseLanguage!!)

    suspend fun getPushNotification(user_id: Int): Response<GetNotificationResponse> =
        apiService.getPushNotification(user_id, chooseLanguage!!)

    suspend fun updatePassword( email: String,password: String,confirm_password:String):Response<CommonResponse> =
    apiService.updatePassword(email,password,confirm_password,chooseLanguage!!)

    suspend fun cancelSubcription(user_id: Int,meal_plan_subscription_id: Int,isRenewal: Boolean):Response<CommonResponse> =
        apiService.cancelSubcription(user_id,meal_plan_subscription_id, chooseLanguage!!,isRenewal)

    suspend fun getAddOnProteins(meal_plan_subscription_id: Int,renewal_status:Boolean):Response<AddOnResponse> =
        apiService.getAddOnProteins(chooseLanguage!!,meal_plan_subscription_id,renewal_status)

    suspend fun getExtras(meal_plan_subscription_id: Int,renewal_status:Boolean):Response<ExtrasUpdateResponse> =
        apiService.getExtras(AppPreferences.chooseLanguage!!,
        meal_plan_subscription_id,renewal_status,AppPreferences.user_id!!)

    suspend fun updateAddOn(user_id:Int,
        meal_plan_subscription_id:Int,
        carbs:String,
        carbs_price:String,
        proteins:String,
        proteins_price:String,
        payment_id:String,
        total:String,renewal_status:Boolean,
        extras:ArrayList<String>
    ):Response<CommonResponse> =apiService.updateAddOn(user_id, meal_plan_subscription_id, carbs, carbs_price, proteins, proteins_price, payment_id, total, chooseLanguage!!,renewal_status,extras)

    suspend fun getGlobalSuspension():Response<GlobalResponse> =apiService.getGlobalSuspension()


}
