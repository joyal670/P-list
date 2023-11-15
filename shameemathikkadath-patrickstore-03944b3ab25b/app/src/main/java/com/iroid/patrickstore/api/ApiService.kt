package com.iroid.patrickstore.api

import com.google.gson.JsonObject
import com.iroid.patrickstore.model.add_address.AddAddressResponse
import com.iroid.patrickstore.model.add_review.AddReviewResponse
import com.iroid.patrickstore.model.add_to_cart.AddToCartResponse
import com.iroid.patrickstore.model.add_to_wishlist.AddToWishListResponse
import com.iroid.patrickstore.model.addresslist.AddressListResponse
import com.iroid.patrickstore.model.all_categories.AllCategoriesResponse
import com.iroid.patrickstore.model.cart_listing.CartListingResponse
import com.iroid.patrickstore.model.cashback.CashbackResponse
import com.iroid.patrickstore.model.categoryProdcut.CategoryProductResponse
import com.iroid.patrickstore.model.categoryProdcut.seller.SellerCategoryWiseResponse
import com.iroid.patrickstore.model.change_password.ChangePasswordResponse
import com.iroid.patrickstore.model.coupon.CouponResponse
import com.iroid.patrickstore.model.coupon.applyCoupon.ApplyCouponResponse
import com.iroid.patrickstore.model.daily_deal.DailyDealResponse
import com.iroid.patrickstore.model.delete_address.DeleteAddressResponse
import com.iroid.patrickstore.model.delivered_order.DeliveredOrderResponse
import com.iroid.patrickstore.model.edit_address.EditAddressResponse
import com.iroid.patrickstore.model.edit_review.EditReviewResponse
import com.iroid.patrickstore.model.festval_offers.FestivalOfferResponse
import com.iroid.patrickstore.model.forgotpassword.ForgotResponse
import com.iroid.patrickstore.model.home.HomeResponse
import com.iroid.patrickstore.model.location_request.LocationRequest
import com.iroid.patrickstore.model.location_request.response.LocationResponse
import com.iroid.patrickstore.model.login.LoginResponse
import com.iroid.patrickstore.model.my_orders.MyOrderResponse
import com.iroid.patrickstore.model.order_confirm.OrderConfirmResponse
import com.iroid.patrickstore.model.order_detail.OrderDetailResponse
import com.iroid.patrickstore.model.order_offer.OrderOfferResponse
import com.iroid.patrickstore.model.order_summary.OrderSummaryResponse
import com.iroid.patrickstore.model.otp.OtpResponse
import com.iroid.patrickstore.model.product_reviews.ProductReviewResponse
import com.iroid.patrickstore.model.remove_single_item_from_cart.RemoveSingleItemFromCartResponse
import com.iroid.patrickstore.model.remove_wish_list_all_item.RemoveWishlistAllItemsResponse
import com.iroid.patrickstore.model.remove_wishlist_single_item.RemoveSingleItemFromWishlistResponse
import com.iroid.patrickstore.model.return_order.ReturnResponse
import com.iroid.patrickstore.model.reward.RewardResponse
import com.iroid.patrickstore.model.seller.SingleSellerResponse
import com.iroid.patrickstore.model.service.ServiceCategoryResponse
import com.iroid.patrickstore.model.service.service_confirm.ServiceConfirmResponse
import com.iroid.patrickstore.model.service.service_list.ServicelistResponse
import com.iroid.patrickstore.model.service.service_order.ServiceOrderResponse
import com.iroid.patrickstore.model.service.service_order.confirm_order.ConfirmOrderResponse
import com.iroid.patrickstore.model.service.service_order_detail.ServiceOrderDetailResponse
import com.iroid.patrickstore.model.service.service_place.ServicePlaceOrderResponse
import com.iroid.patrickstore.model.service.service_request.ServiceRequestResponse
import com.iroid.patrickstore.model.service.service_update.ServiceUpdate
import com.iroid.patrickstore.model.signup.SignUpResponse
import com.iroid.patrickstore.model.single_address.SingleAddressResponse
import com.iroid.patrickstore.model.single_product.SingleProductResponse
import com.iroid.patrickstore.model.update_profile.ProfileUpdateResponse
import com.iroid.patrickstore.model.update_profile_picture.UpdateProfilePictureResponse
import com.iroid.patrickstore.model.view_profile.ViewProfileResponse
import com.iroid.patrickstore.model.wishlist_listing.WishListResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {
    @FormUrlEncoded
    @POST("customer-accounts/register")
    suspend fun registerCustomer(
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("referralCode")referralCode:String
    ): Response<SignUpResponse>

    @FormUrlEncoded
    @POST("customer-accounts/otp/verify")
    suspend fun otpVerify(@Field("otp") otp: String): Response<OtpResponse>

    @FormUrlEncoded
    @POST("customer-accounts/login")
    suspend fun login(
        @Field("userName") userName: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("customer-accounts/forgotpassword")
    suspend fun forgotPassword(
        @Field("isMobile") isMobile: Boolean,
        @Field("mobile") mobile: String
    ): Response<ForgotResponse>

    @FormUrlEncoded
    @POST("customer-accounts/address")
    suspend fun addAddress(
        @Field("name") name: String,
        @Field("address1") address1: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("country") country: String,
        @Field("pincode") pincode: String,
        @Field("landMark") landMark: String,
        @Field("label") label: String,
        @Field("lat") lat: String,
        @Field("lng") lng: String,
        @Field("contactNumber") contactNumber: String
    ): Response<AddAddressResponse>

    @GET("customer-accounts/dashboard")
    suspend fun dashboard(): Response<HomeResponse>

    @GET("customer-accounts/address/list/all")
    suspend fun getAddressList(): Response<AddressListResponse>

    @GET("customer-accounts/address/{id}")
    suspend fun getSingleAddress(@Path("id") id: String): Response<SingleAddressResponse>

    @DELETE("customer-accounts/address/delete/{id}")
    suspend fun deleteAddress(@Path("id") id: String): Response<DeleteAddressResponse>

    /* View Profile Api */
    @GET("customer-accounts/profile/view")
    suspend fun viewProfile(): Response<ViewProfileResponse>

    @FormUrlEncoded
    @PATCH("customer-accounts/address/update")
    suspend fun editAddress(
        @Field("addressId") addressId: String,
        @Field("name") name: String,
        @Field("address1") address1: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("country") country: String,
        @Field("pincode") pincode: String,
        @Field("landMark") landMark: String,
        @Field("label") label: String,
        @Field("lat") lat: String,
        @Field("lng") lng: String,
        @Field("contactNumber") contactNumber: String
    ): Response<EditAddressResponse>


    @POST("customer-product/cart/add")
    suspend fun addToCart(
        @Query("product") product: String,
        @Query("quantity") quantity: String
    ): Response<AddToCartResponse>


    @GET("customer-product/cart")
    suspend fun getCartList(@Query("defaultAddressId") defaultAddressId: String): Response<CartListingResponse>

    @DELETE("customer-product/cart/delete/{id}")
    suspend fun removeSingleItemFromCart(@Path("id") id: String): Response<RemoveSingleItemFromCartResponse>

    @FormUrlEncoded
    @POST("customer-product/wishlist/add")
    suspend fun addToWishlist(@Field("productId") productId: String): Response<AddToWishListResponse>

    /* Profile Update Api */
    @FormUrlEncoded
    @PATCH("customer-accounts/profile/update")
    suspend fun updateProfile(
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("profileImageId") profileImageId: String
    ): Response<ProfileUpdateResponse>

    @DELETE("customer-product/wishlist/delete/{id}")
    suspend fun removeSingleItemFromWishlist(@Path("id") id: String): Response<RemoveSingleItemFromWishlistResponse>

    @DELETE("customer-product/wishlist/delete")
    suspend fun removeAllFromWishlist(): Response<RemoveWishlistAllItemsResponse>

    @GET("customer-product/wishlist")
    suspend fun getWishlist(): Response<WishListResponse>

    @FormUrlEncoded
    @POST("customer-accounts/password/change")
    suspend fun changePassword(
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String
    ): Response<ChangePasswordResponse>

    /* Profile Picture Api */
    @Multipart
    @POST("mediaupload")
    suspend fun updateProfilePicture(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: List<MultipartBody.Part>
    ): Response<UpdateProfilePictureResponse>

    @GET("customer-product/cart/summary")
    suspend fun getOrderSummary(@Query("defaultAddressId") defaultAddressId: String,
                                @Query("isCoupenCodeUsed") isCouponCodeUsed: Boolean,
                                @Query("offerCode") offerCode: String,
                                @Query("isRewardUsed") isRewardUsed: Boolean,
                                @Query("isGiftCardUsed") isGiftCardUsed: Boolean,
                                @Query("giftCardCode") giftCardCode: String,
                                @Query("isCashBackUsed") isCashBackUsed: Boolean): Response<OrderSummaryResponse>

    @FormUrlEncoded
    @POST("customer-product/cart/confirm")
    suspend fun confirmOrder(
        @Field("paymentMethod") paymentMethod: String,
        @Field("paymentId") paymentId: String,
        @Field("isAnyDeliverySurge") isAnyDeliverySurge: Boolean,
        @Field("deliverySurgeCharge") deliverySurgeCharge: Double,
        @Field("deliveryCharge") deliveryCharge: Double,
        @Field("packingCharge") packingCharge: Double,
        @Field("serviceCharge") serviceCharge: Double,
        @Field("taxAmount") taxAmount: Double,
        @Field("itemTotal") itemTotal: Double,
        @Field("tip") tip: Double,
        @Field("grandTotal") grandTotal: Double,
        @Field("offerId") offerId: String,
        @Field("offerAmount") offerAmount: Double,
        @Field("cashbackAmount")cashbackAmount:Double,
        @Field("totalOfferAmount")totalOfferAmount:Double
    ): Response<OrderConfirmResponse>

    @GET("customer-product/orders/{page}/{count}")
    suspend fun getCustomerOrder(
        @Path("page") page: Int,
        @Path("count") count: Int
    ): Response<MyOrderResponse>

    @GET("customer-product/orders/{id}")
    suspend fun getSingleOrder(@Path("id") id: String): Response<OrderDetailResponse>

    @GET("customer-product/offer/list")
    suspend fun getOfferList(): Response<CouponResponse>

    @GET("customer-product/dailydeals")
    suspend fun getDailyDeals(): Response<DailyDealResponse>



    @GET("customer-product/categories")
    suspend fun getProductCategories(): Response<AllCategoriesResponse>

    @GET("customer-seller/single/{store_id}")
    suspend fun getSellerSingle(@Path("store_id") store_id: String): Response<SingleSellerResponse>

    @GET("customer-product/single")
    suspend fun getProductSingle(@Query("productId") productId: String): Response<SingleProductResponse>

    @GET("customer-product/bycat")
    suspend fun getCatProduct(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("categoryId") categoryId: String,
        @Query("isPerishable") isPerishable: Boolean,
        @Query("franchiseId")franchiseId:String,
        @Query("sellerId")sellerId:String
    ): Response<CategoryProductResponse>


    @GET("customer-product/search")
    suspend fun getProductSearch(
        @Query("searchText") searchText: String,
        @Query("page") page: String,
        @Query("limit") limit: String,
        @Query("franchiseId")franchiseId:String

        ): Response<CategoryProductResponse>

    @GET("customer-product/bycat")
    suspend fun getCatSeller(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("categoryId") categoryId: String,
        @Query("isPerishable") isPerishable: Boolean,
        @Query("franchiseId")franchiseId:String,
//        @Query("sellerId")sellerId:String
    ): Response<SellerCategoryWiseResponse>


    @PATCH("customer-product/offer-code/redeem")
    suspend fun redeemCode(
        @Body data: HashMap<String, Any>
    ): Response<ApplyCouponResponse>


    @GET("customer-seller/service-category/list")
    suspend fun mainService(): Response<ServiceCategoryResponse>

    @GET("customer-seller/quote/{quoteId}/confirm")
    suspend fun confirmServiceOrder(
        @Path("quoteId") quoteId: String,
        @Query("isCoupenCodeUsed") isCouponCodeUsed: Boolean,
        @Query("offerCode") offerCode: String,
        @Query("isRewardUsed") isRewardUsed: Boolean,
        @Query("isGiftCardUsed") isGiftCardUsed: Boolean,
        @Query("giftCardCode") giftCardCode: String,
        @Query("isCashBackUsed") isCashBackUsed: Boolean
    ): Response<ServiceConfirmResponse>


    @POST("customer-accounts/location/set")
    suspend fun setLocation(@Body locationRequest: LocationRequest): Response<LocationResponse>

    @GET("customer-seller/services")
    suspend fun getSellerService(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sellerId") sellerId: String
    ): Response<ServicelistResponse>

    @FormUrlEncoded
    @POST("customer-seller/quote")
    suspend fun newQuote(
        @Field("serviceId") serviceId: String,
        @Field("sellerId") sellerId: String,
        @Field("rate") rate: String,
        @Field("date") date: String,
        @Field("time") time: String,
        @Field("quantity") quantity: String,
        @Field("customerMsg") customerMsg: String
    ): Response<ServiceRequestResponse>



    @GET("customer-seller/myreview")
    suspend fun getMyReview(): Response<JsonObject>

    @GET("customer-seller/quote/list")
    suspend fun quoteList(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ServiceOrderResponse>

    @GET("customer-seller/quote/{id}/detail")
    suspend fun serviceOrderDetail(@Path("id") id: String): Response<ServiceOrderDetailResponse>

    @FormUrlEncoded
    @PATCH("customer-seller/quote/{id}/update")
    suspend fun updateService(
        @Path("id") id: String,
        @Field("responseType") responseType: String,
        @Field("rate") rate: String,
        @Field("comments") comments: String
    ): Response<ServiceUpdate>

    @GET("customer-seller/quote/{id}/summary")
    suspend fun getServiceOrderSummary(@Path("id") id: String): Response<ConfirmOrderResponse>

    @FormUrlEncoded
    @PATCH("customer-seller/quote/{id}/pay")
    suspend fun servicePlaceOrder(
        @Path("id") quoteId: String,
        @Field("paymentId") paymentId: String,
        @Field("itemTotal") itemTotal: String,
        @Field("sellerServiceCharge") sellerServiceCharge: String,
        @Field("grandTotal") grandTotal: String
    ): Response<ServicePlaceOrderResponse>

    @GET("customer-product/other/offer/check")
    suspend fun getOfferCheck():Response<OrderOfferResponse>

    @GET("customer-accounts/reward")
    suspend fun getCustomerReward():Response<RewardResponse>

    @FormUrlEncoded
    @POST("customer-product/return/request")
    suspend fun returnRequest(@Field("orderId")orderId:String,
    @Field("productId")productId:String,
    @Field("returnMode")returnMode:String,
    @Field("reason")reason:String,
    @Field("returnImageId")returnImageId:String,
    @Field("comments")comments:String):Response<ReturnResponse>

    @GET("customer-product/delivered/orders")
    suspend fun getDeliveredOrders():Response<DeliveredOrderResponse>

    @FormUrlEncoded
    @POST("customer-product/addreview")
    suspend fun addReview(@Field("title")title:String,
    @Field("productId")productId:String,
    @Field("rating")rating:Float,
    @Field("review")review:String):Response<AddReviewResponse>

    @GET("customer-product/myreview")
    suspend fun getProductReview(@Query("page")page:Int,
    @Query("limit")limit:Int):Response<ProductReviewResponse>

    @FormUrlEncoded
    @PATCH("customer-product/editreview")
    suspend fun  editReview(@Field("reviewId")reviewId:String,
    @Field("rating")rating:Float,
    @Field("title")title:String,
    @Field("review")review:String):Response<EditReviewResponse>


    @DELETE("customer-product/deletereview")
    suspend fun deleteReview(@Query("reviewId")reviewId:String):Response<EditReviewResponse>

    @GET("customer-accounts/cashbacks/list")
    suspend fun getCashBackList():Response<CashbackResponse>

    @GET("customer-product/festivals")
    suspend fun getFestivalOffers():Response<FestivalOfferResponse>



    companion object {
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl("http://35.244.28.193:8000/")
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
