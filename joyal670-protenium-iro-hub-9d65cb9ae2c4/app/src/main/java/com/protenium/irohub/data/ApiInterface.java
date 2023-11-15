package com.protenium.irohub.data;

import com.protenium.irohub.model.SubscriptionInfo.SubscriptionResponse;
import com.protenium.irohub.model.about_us.AboutUsResponse;
import com.protenium.irohub.model.addAddress.AddAddressResponse;
import com.protenium.irohub.model.add_initial_sub.AddSubscriptionResponse;
import com.protenium.irohub.model.common_response.CommonResponse;
import com.protenium.irohub.model.contactUs.ContactUsResponse;
import com.protenium.irohub.model.defaultAddress.SetDefaultAddressResponse;
import com.protenium.irohub.model.deleteAddress.DeleteAddressResponse;
import com.protenium.irohub.model.dislike.getdislike.GetDisLikeResponse;
import com.protenium.irohub.model.dislike.setDislike.SetDislikeResponse;
import com.protenium.irohub.model.finalSub.AddFinalSubscriptionResponse;
import com.protenium.irohub.model.forgotpassword.ForgotPasswordResponse;
import com.protenium.irohub.model.getAddress.GetAddressResponse;
import com.protenium.irohub.model.getArea.GetAreaResponse;
import com.protenium.irohub.model.getGovernate.GetGovernateResponse;
import com.protenium.irohub.model.home.HomeResponse;
import com.protenium.irohub.model.home_detailed.HomeDetailedResponse;
import com.protenium.irohub.model.login.LoginResponse;
import com.protenium.irohub.model.menu.GetMenuResponse;
import com.protenium.irohub.model.place_order.OrderPlaceResponse;
import com.protenium.irohub.model.profile.changePassword.ChangePasswordResponse;
import com.protenium.irohub.model.profile.profileDetails.GetProfileDetailsResponse;
import com.protenium.irohub.model.profile.profileUpdate.UpdateProfileResponse;
import com.protenium.irohub.model.register.RegisterResponse;
import com.protenium.irohub.model.subscription_details.SubscriptionDetailsResponse;
import com.protenium.irohub.model.subscription_history.GetSubscriptionHistoryResponse;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Single<LoginResponse> customerLogin(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST("forgotpassword")
    Single<ForgotPasswordResponse> forgotPassword(
            @Field("email") String email);

    @FormUrlEncoded
    @POST("register")
    Single<RegisterResponse> customerSignup(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("alternative_phone") String alternative_phone,
            @Field("password") String password,
            @Field("gender") String gender,
            @Field("status") int status,
            @Field("lang_id") String lang_id,
            @Field("device_token") String device_token
    );

    @FormUrlEncoded
    @POST("get-mealcategories")
    Single<HomeResponse> getHomeDetails(@Field("lang_id") String lang_id);

    @POST("get-mealplan-info/{meal_plan_id}")
    Single<HomeDetailedResponse> getMealPlan(@Path("meal_plan_id") int meal_plan_id, @Query("lang_id") String lang_id);


    @GET("get-tags/{user_id}")
    Single<GetDisLikeResponse> getTags(@Path("user_id") int user_id, @Query("lang_id") String lang_id);

    @GET("get-subscription-history/{user_id}")
    Single<GetSubscriptionHistoryResponse> getSubscriptionHistory(
            @Path("user_id") int user_id,
            @Query("lang_id") String lang_id
    );

    @FormUrlEncoded
    @POST("set-disliked-tags")
    Single<SetDislikeResponse> setDislikedTags(
            @Field("user_id") int user_id,
            @Field("tag_ids[]") List<Integer> tag_ids,
            @Field("lang_id") String lang_id);

    @FormUrlEncoded
    @POST("add-initial-subscription-info")
    Single<AddSubscriptionResponse> addInitialSubscriptionPlan(
            @Field("user_id") int user_id,
            @Field("start_date") String start_date,
            @Field("meal_plan_id") int meal_plan_id,
            @Field("non_stop_delivery_price") String non_stop_delivery_price,
            @Field("carbs") String carbs,
            @Field("carbs_price") String carbs_price,
            @Field("proteins") String proteins,
            @Field("proteins_price") String proteins_price,
            @Field("comments") String comments,
            @Field("duration") int duration,
            @Field("base_price") String base_price,
            @Field("code") String code,
            @Field("suspend") String suspend,
            @Field("lang_id") String lang_id,
            @Field("enable_modification") int enable_modification
    );

    @GET("get-addresses/{user_id}")
    Single<GetAddressResponse> getUserAddresses(@Path("user_id") int user_id, @Query("lang_id") String lang_id);

    @GET("get-governorates")
    Single<GetGovernateResponse> getGovernorates(@Query("lang_id") String lang_id);

    @GET("get-areas/{governorate_id}")
    Single<GetAreaResponse> getAreas(
            @Path("governorate_id") String governorate_id,
            @Query("lang_id") String lang_id);

    @FormUrlEncoded
    @POST("add-address")
    Single<AddAddressResponse> addAddress(
            @Field("user_id") int user_id,
            @Field("governorate_id") int governorate_id,
            @Field("area_id") int area_id,
            @Field("block") String block,
            @Field("avenue") String avenue,
            @Field("street") String street,
            @Field("building") String building,
            @Field("floor") String floor,
            @Field("appartment") String appartment,
            @Field("default") int default1,
            @Field("lang_id") String lang_id);

    @GET("delete-address/{addres_id}")
    Single<DeleteAddressResponse> deleteAddress(
            @Path("addres_id") int addres_id,
            @Query("lang_id") String lang_id);

    @FormUrlEncoded
    @POST("set-default-address")
    Single<SetDefaultAddressResponse> setDefaultAddress(
            @Field("user_id") int user_id,
            @Field("address_id") int address_id,
            @Field("lang_id") String lang_id
    );

    @FormUrlEncoded
    @POST("add-final-subscription-info")
    Single<AddFinalSubscriptionResponse> addFinalSubscription(
            @Field("user_id") int user_id,
            @Field("meal_plan_subscription_id") int meal_plan_subscription_id,
            @Field("payment_status") int payment_status,
            @Field("adress_id") int adress_id,
            @Field("payment_method") String payment_method,
            @Field("promo_code") String promo_code,
            @Field("lang_id") String lang_id,
            @Field("payment_reference") String payment_reference,
            @Field("unique_key") String unique_key,
            @Field("renewal") int renewal);

    @FormUrlEncoded
    @POST("get-userinfo")
    Single<GetProfileDetailsResponse> getUserInfo(
            @Field("user_id") int user_id,
            @Field("lang_id") String lang_id);

    @Multipart
    @POST("update-userinfo")
    Single<UpdateProfileResponse> updateUserInfo(@Part("id") RequestBody id,
                                                 @Part("name") RequestBody name,
                                                 @Part("gender") RequestBody gender,
                                                 @Part("phone") RequestBody phone,
                                                 @Part("lang_id") RequestBody lang_id,
                                                 @Part("alternative_phone") RequestBody alternative_phone,
                                                 @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("update-password")
    Single<ChangePasswordResponse> updatePassword(
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirm_password") String confirm_password,
            @Field("lang_id") String lang_id);

    @GET("about-us")
    Single<AboutUsResponse> getAbout(@Query("lang_id") String lang_id);

    @FormUrlEncoded
    @POST("contactus")
    Single<ContactUsResponse> contactUs(
            @Field("user_id") int user_id,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("message") String message,
            @Field("lang_id") String lang_id);

    @GET("privacy-policy")
    Single<AboutUsResponse> getPrivacyPolicy(@Query("lang_id") String lang_id);

    @GET("terms-conditions")
    Single<AboutUsResponse> getTermsConditions(@Query("lang_id") String lang_id);

    @FormUrlEncoded
    @POST("get-subscription-info")
    Single<SubscriptionResponse> getSubscriptionInfo(
            @Field("user_id") int user_id,
            @Field("lang_id") String lang_id);

    @FormUrlEncoded
    @POST("get-menu-for-day")
    Single<GetMenuResponse> getMenuForDay(
            @Field("user_id") int user_id,
            @Field("date") String date,
            @Field("lang_id") String lang_id);

    @FormUrlEncoded
    @POST("place-order")
    Single<OrderPlaceResponse> getPlaceOrder(
            @Field("user_id") int user_id,
            @Field("date") String date,
            @Field("food_ids[]") List<Integer> food_ids,
            @Field("lang_id") String lang_id,
            @Field("meal_plan_subscription_id") String meal_plan_subscription_id);

    @FormUrlEncoded
    @POST("suspend-unsuspend-delivery")
    Single<CommonResponse> suspendUnsuspendDelivery(
            @Field("user_id") int user_id,
            @Field("date") String date,
            @Field("lang_id") String lang_id);

    @GET("get-subscription-preview/{meal_plan_subscription_id}")
    Single<SubscriptionDetailsResponse> getSubscriptionPreviewDetails(
            @Path("meal_plan_subscription_id") int meal_plan_subscription_id,
            @Query("lang_id") String lang_id);
}
