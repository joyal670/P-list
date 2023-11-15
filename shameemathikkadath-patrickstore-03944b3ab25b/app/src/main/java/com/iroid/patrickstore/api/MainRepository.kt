package com.iroid.patrickstore.api

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
import com.iroid.patrickstore.preference.AppPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class MainRepository(private val apiHelper: ApiService) {

    suspend fun registerCustomer(
        firstName: String, lastName: String,
        email: String, mobile: String, password: String,
        referralCode: String
    ): Response<SignUpResponse> = apiHelper.registerCustomer(
        firstName, lastName,
        email, mobile, password, referralCode
    )

    suspend fun otpVerify(otp: String): Response<OtpResponse> = apiHelper.otpVerify(otp)

    suspend fun login(
        email: String,
        password: String
    ): Response<LoginResponse> = apiHelper.login(email, password)


    suspend fun forgotPassword(
        isMobile: Boolean,
        mobile: String
    ): Response<ForgotResponse> = apiHelper.forgotPassword(isMobile, mobile)

    suspend fun addAddress(
        name: String,
        address1: String,
        city: String,
        state: String,
        country: String,
        pincode: String,
        landMark: String,
        label: String,
        lat: String,
        lng: String,
        contactNumber: String
    ): Response<AddAddressResponse> = apiHelper.addAddress(
        name,
        address1,
        city,
        state,
        country,
        pincode,
        landMark,
        label,
        lat,
        lng,
        contactNumber
    )

    suspend fun getDashboard(): Response<HomeResponse> = apiHelper.dashboard()

    suspend fun getAddressList(): Response<AddressListResponse> = apiHelper.getAddressList()

    suspend fun getSingleAddress(id: String): Response<SingleAddressResponse> =
        apiHelper.getSingleAddress(id)

    suspend fun deleteAddress(id: String): Response<DeleteAddressResponse> =
        apiHelper.deleteAddress(id)

    /* View Profile */
    suspend fun viewProfile(): Response<ViewProfileResponse> = apiHelper.viewProfile()

    suspend fun editAddress(
        addressId: String,
        name: String,
        address1: String,
        city: String,
        state: String,
        country: String,
        pincode: String,
        landMark: String,
        label: String,
        lat: String,
        lng: String,
        contactNumber: String
    ): Response<EditAddressResponse> = apiHelper.editAddress(
        addressId,
        name,
        address1,
        city,
        state,
        country,
        pincode,
        landMark,
        label,
        lat,
        lng,
        contactNumber
    )

    suspend fun addToCart(product: String, quantity: String): Response<AddToCartResponse> =
        apiHelper.addToCart(product, quantity)

    /* Profile Update Api */
    suspend fun updateProfile(
        firstName: String,
        lastName: String,
        email: String,
        mobile: String,
        profileImageId: String
    ): Response<ProfileUpdateResponse> =
        apiHelper.updateProfile(firstName, lastName, email, mobile, profileImageId)

    suspend fun getCartList(defaultAddressId: String): Response<CartListingResponse> =
        apiHelper.getCartList(defaultAddressId)

    suspend fun removeSingleItemFromCart(id: String): Response<RemoveSingleItemFromCartResponse> =
        apiHelper.removeSingleItemFromCart(id)

    suspend fun addToWishList(id: String): Response<AddToWishListResponse> =
        apiHelper.addToWishlist(id)

    suspend fun removeWishListSingleItem(id: String): Response<RemoveSingleItemFromWishlistResponse> =
        apiHelper.removeSingleItemFromWishlist(id)

    suspend fun removeWishListAllItem(): Response<RemoveWishlistAllItemsResponse> =
        apiHelper.removeAllFromWishlist()

    suspend fun getWishlist(): Response<WishListResponse> = apiHelper.getWishlist()

    suspend fun changePassword(oldPass: String, newPass: String): Response<ChangePasswordResponse> =
        apiHelper.changePassword(oldPass, newPass)

    /* Profile Picture Api */
    suspend fun updateProfilePicture(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        images: List<MultipartBody.Part>
    ): Response<UpdateProfilePictureResponse> =
        apiHelper.updateProfilePicture(params, images)

    suspend fun getOrderSummary(
        defaultAddressId: String,
        isCouponCodeUsed: Boolean,
        offerCode: String,
        isRewardUsed: Boolean,
        isGiftCardUsed: Boolean,
        giftCardCode: String,
        isCashBackUsed: Boolean
    ): Response<OrderSummaryResponse> =
        apiHelper.getOrderSummary(
            defaultAddressId,
            isCouponCodeUsed,
            offerCode,
            isRewardUsed,
            isGiftCardUsed,
            giftCardCode,
            isCashBackUsed
        )

    suspend fun confirmOrder(
        paymentMethod: String,
        paymentId: String,
        isAnyDeliverySurge: Boolean,
        deliverySurgeCharge: Double,
        deliveryCharge: Double,
        packingCharge: Double,
        serviceCharge: Double,
        taxAmount: Double,
        itemTotal: Double,
        tip: Double,
        grandTotal: Double,
        offerId: String,
        offerAmount: Double,
        cashbackAmount:Double,
        totalOfferAmount:Double
    ): Response<OrderConfirmResponse> =
        apiHelper.confirmOrder(
            paymentMethod,
            paymentId,
            isAnyDeliverySurge,
            deliverySurgeCharge,
            deliveryCharge,
            packingCharge,
            serviceCharge,
            taxAmount,
            itemTotal,
            tip,
            grandTotal,
            offerId,
            offerAmount,
            cashbackAmount,
            totalOfferAmount
        )

    suspend fun getCustomerOrder(page: Int, count: Int): Response<MyOrderResponse> =
        apiHelper.getCustomerOrder(page, count)

    suspend fun getSingleOrder(orderId: String): Response<OrderDetailResponse> =
        apiHelper.getSingleOrder(orderId)

    suspend fun getOfferList(): Response<CouponResponse> =
        apiHelper.getOfferList()

    suspend fun getDailyDeal(): Response<DailyDealResponse> =
        apiHelper.getDailyDeals()

    suspend fun getFestival(): Response<FestivalOfferResponse> =
        apiHelper.getFestivalOffers()

    suspend fun getProductCategories(): Response<AllCategoriesResponse> =
        apiHelper.getProductCategories()

    suspend fun getSellerSingle(store_id: String): Response<SingleSellerResponse> =
        apiHelper.getSellerSingle(store_id)

    suspend fun getSingleProduct(productId: String): Response<SingleProductResponse> =
        apiHelper.getProductSingle(productId)

    suspend fun getCategoryProduct(
        page: Int,
        limit: Int,
        categoryId: String,
        isPerishable: Boolean,
        sellerId:String
    ): Response<CategoryProductResponse> =
        apiHelper.getCatProduct(page, limit, categoryId, isPerishable,AppPreferences.franchiseId,
        sellerId)

    suspend fun getSearchProduct(
        searchText: String,
        page: String,
        limit: String
    ): Response<CategoryProductResponse> =
        apiHelper.getProductSearch(searchText, page, limit,AppPreferences.franchiseId)

    suspend fun getCategorySeller(
        page: Int,
        limit: Int,
        categoryId: String,
        isPerishable: Boolean,
        sellerId:String
    ): Response<SellerCategoryWiseResponse> =
        apiHelper.getCatSeller(page, limit, categoryId, isPerishable,AppPreferences.franchiseId)

    suspend fun redeemCode(data: HashMap<String, Any>): Response<ApplyCouponResponse> =
        apiHelper.redeemCode(data)

    suspend fun mainService(): Response<ServiceCategoryResponse> =
        apiHelper.mainService()

    suspend fun setLocation(locationRequest: LocationRequest): Response<LocationResponse> =
        apiHelper.setLocation(locationRequest)

    suspend fun getSellerService(
        page: Int,
        limit: Int,
        sellerId: String
    ): Response<ServicelistResponse> =
        apiHelper.getSellerService(page, limit, sellerId)


    suspend fun newQuote(
        serviceId: String,
        sellerId: String,
        rate: String,
        date: String,
        time: String,
        quantity: String,
        customerMsg: String
    ): Response<ServiceRequestResponse> =
        apiHelper.newQuote(serviceId, sellerId, rate, date, time, quantity, customerMsg)


    suspend fun quoteList(
        page: Int,
        limit: Int
    ): Response<ServiceOrderResponse> = apiHelper.quoteList(page, limit)

    suspend fun serviceOrderDetail(id: String): Response<ServiceOrderDetailResponse> =
        apiHelper.serviceOrderDetail(id)

    suspend fun updateService(
        quoteId: String,
        responseType: String,
        rate: String,
        comments: String
    ): Response<ServiceUpdate> = apiHelper.updateService(
        quoteId,
        responseType,
        rate,
        comments
    )


    suspend fun getServiceOrderSummary(quoteId: String): Response<ConfirmOrderResponse> =
        apiHelper.getServiceOrderSummary(quoteId)

    suspend fun confirmServiceOrder(
        quoteId: String,
        isCouponCodeUsed: Boolean,
        offerCode: String,
        isRewardUsed: Boolean,
        isGiftCardUsed: Boolean,
        giftCardCode: String,
        isCashBackUsed: Boolean
    ): Response<ServiceConfirmResponse> =
        apiHelper.confirmServiceOrder(
            quoteId,
            isCouponCodeUsed,
            offerCode,
            isRewardUsed,
            isGiftCardUsed,
            giftCardCode,
            isCashBackUsed
        )

    suspend fun servicePlaceOrder(
        quoteId: String,
        paymentId: String,
        itemTotal: String,
        sellerServiceCharge: String,
        grandTotal: String
    ): Response<ServicePlaceOrderResponse> =
        apiHelper.servicePlaceOrder(quoteId, paymentId, itemTotal, sellerServiceCharge, grandTotal)


    suspend fun getOfferCheck(): Response<OrderOfferResponse> =
        apiHelper.getOfferCheck()

    suspend fun getCustomerReward(): Response<RewardResponse> =
        apiHelper.getCustomerReward()

    suspend fun returnRequest(
        orderId: String,
        productId: String,
        returnMode: String,
        reason: String,
        returnImageId: String,
        comments: String
    ): Response<ReturnResponse> =
        apiHelper.returnRequest(orderId, productId, returnMode, reason, returnImageId, comments)

    suspend fun getDeliveredOrders(): Response<DeliveredOrderResponse> =
        apiHelper.getDeliveredOrders()

    suspend fun addReview(
        title: String,
        productId: String,
        rating: Float,
        review: String
    ): Response<AddReviewResponse> = apiHelper.addReview(title, productId, rating, review)

    suspend fun getProductReview(page: Int, limit: Int): Response<ProductReviewResponse> =
        apiHelper.getProductReview(page, limit)

    suspend fun updateReview(
        reviewId: String,
        rating: Float,
        title: String,
        review: String
    ): Response<EditReviewResponse> = apiHelper.editReview(reviewId, rating, title, review)


    suspend fun deleteReview(
        reviewId: String
    ): Response<EditReviewResponse> = apiHelper.deleteReview(reviewId)

    suspend fun getCashBackList():Response<CashbackResponse> =apiHelper.getCashBackList()



}
