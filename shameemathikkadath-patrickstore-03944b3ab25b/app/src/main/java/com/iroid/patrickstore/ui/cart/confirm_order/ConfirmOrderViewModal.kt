package com.iroid.patrickstore.ui.cart.confirm_order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.coupon.CouponResponse
import com.iroid.patrickstore.model.coupon.applyCoupon.ApplyCouponResponse
import com.iroid.patrickstore.model.order_confirm.OrderConfirmResponse
import com.iroid.patrickstore.model.order_offer.OrderOfferResponse
import com.iroid.patrickstore.model.order_summary.OrderSummaryResponse
import com.iroid.patrickstore.model.service.service_confirm.ServiceConfirmResponse
import com.iroid.patrickstore.model.service.service_order.confirm_order.ConfirmOrderResponse
import com.iroid.patrickstore.model.service.service_place.ServicePlaceOrderResponse
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.launch

class ConfirmOrderViewModal : ViewModel() {
    private val liveDataOrderList = MutableLiveData<Resource<OrderSummaryResponse>>()
    private val liveDataConfirm=MutableLiveData<Resource<OrderConfirmResponse>>()
    private val liveDataCoupon=MutableLiveData<Resource<CouponResponse>>()
    private val liveDataCouponApply=MutableLiveData<Resource<ApplyCouponResponse>>()
    private val liveDataServiceConfirm=MutableLiveData<Resource<ServiceConfirmResponse>>()
    private val liveDataServicePlaceOrder=MutableLiveData<Resource<ServicePlaceOrderResponse>>()
    private val liveOfferCheck=MutableLiveData<Resource<OrderOfferResponse>>()


    private val liveServiceConfirmOrder=MutableLiveData<Resource<ConfirmOrderResponse>>()
    val serviceConfirmOrderLiveData:LiveData<Resource<ConfirmOrderResponse>>get() = liveServiceConfirmOrder
    val confirmServiceLiveData:LiveData<Resource<ServiceConfirmResponse>> get() = liveDataServiceConfirm
    val listOrderLiveData: LiveData<Resource<OrderSummaryResponse>> get() = liveDataOrderList
    val confirmLiveData:LiveData<Resource<OrderConfirmResponse>>get() = liveDataConfirm
    val couponLiveData:LiveData<Resource<CouponResponse>>get() = liveDataCoupon
    val couponApplyLiveData:LiveData<Resource<ApplyCouponResponse>>get() = liveDataCouponApply
    val servicePlaceOrderLiveData:LiveData<Resource<ServicePlaceOrderResponse>>get() = liveDataServicePlaceOrder
    val offerCheckLiveData:LiveData<Resource<OrderOfferResponse>>get() = liveOfferCheck



    fun getOfferCheck(){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveOfferCheck.postValue(Resource.loading(null))
                repository.getOfferCheck().let {
                    val response=it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveOfferCheck.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveOfferCheck.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveOfferCheck.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveOfferCheck.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex:Exception){

            }
        }
    }

    fun servicePlaceOrder(quoteId: String,
                          paymentId: String,
                          itemTotal: String,
                          sellerServiceCharge: String,
                          grandTotal: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                repository.servicePlaceOrder(
                    quoteId,
                    paymentId,
                    itemTotal,
                    sellerServiceCharge,
                    grandTotal
                ).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataServicePlaceOrder.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataServicePlaceOrder.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataServicePlaceOrder.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataServicePlaceOrder.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex:Exception){

            }
        }
    }


    fun confirmServiceOrder(quoteId: String,
        isCouponCodeUsed:Boolean,
                            offerCode:String,
                            isRewardUsed:Boolean,
                            isGiftCardUsed:Boolean,
                            giftCardCode:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                repository.confirmServiceOrder(quoteId, isCouponCodeUsed, offerCode, isRewardUsed, isGiftCardUsed, giftCardCode,false).let {
                    repository.confirmServiceOrder(quoteId,isCouponCodeUsed,offerCode,
                    isRewardUsed,isGiftCardUsed,giftCardCode,false).let {
                        val response = it.body()
                        when (response!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataServiceConfirm.postValue(Resource.success(response))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataServiceConfirm.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataServiceConfirm.postValue(Resource.error(response.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataServiceConfirm.postValue(Resource.noInterNet("", null))
                            }
                        }

                    }
                }
            }catch (ex:Exception){

            }
        }
    }

    fun serviceSummary(quoteId:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveServiceConfirmOrder.postValue(Resource.loading(null))
                repository.getServiceOrderSummary(quoteId).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveServiceConfirmOrder.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveServiceConfirmOrder.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveServiceConfirmOrder.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveServiceConfirmOrder.postValue(Resource.noInterNet("", null))
                        }
                    }

                }
            }catch (ex:Exception){
                liveServiceConfirmOrder.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun applyCoupon(data: HashMap<String,Any>) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataCouponApply.postValue(Resource.loading(null))
                repository.redeemCode(data).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataCouponApply.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataCouponApply.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataCouponApply.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataCouponApply.postValue(Resource.noInterNet("", null))
                        }
                    }

                }
            } catch (ex: Exception) {
                liveDataCouponApply.postValue(Resource.noInterNet("", null))
            }
        }
    }
    fun getOrderSummary(isCouponCodeUsed: Boolean,
                          offerCode: String,
                          isRewardUsed: Boolean,
                          isGiftCardUsed: Boolean,
                          giftCardCode: String,
                          isCashBackUsed: Boolean) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataOrderList.postValue(Resource.loading(null))
                repository.getOrderSummary(AppPreferences.addressId!!,
                isCouponCodeUsed, offerCode, isRewardUsed, isGiftCardUsed, giftCardCode, isCashBackUsed).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataOrderList.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataOrderList.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataOrderList.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataOrderList.postValue(Resource.noInterNet("", null))
                        }
                    }

                }
            } catch (ex: Exception) {
                Log.e("123456", ex.toString())
                liveDataOrderList.postValue(Resource.noInterNet("", null))
            }
        }
    }

     fun confirmOrder(
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
        grandTotal:Double,
        offerId:String,
        offerAmount:Double,
        cashbackAmount:Double,
        totalOfferAmount:Double
    ){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                repository.confirmOrder(paymentMethod, paymentId, isAnyDeliverySurge, deliverySurgeCharge, deliveryCharge, packingCharge, serviceCharge, taxAmount, itemTotal, tip,grandTotal,offerId,offerAmount,cashbackAmount,totalOfferAmount).let {
                    val response = it.body()
                    Log.e("156", "confirmOrder: ${it.body()}" )
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataConfirm.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataConfirm.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataConfirm.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataConfirm.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }
            catch (ex: Exception) {
                liveDataConfirm.postValue(Resource.noInterNet("", null))
            }
        }




    }

    fun getCoupon(){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataCoupon.postValue(Resource.loading(null))
                repository.getOfferList().let {
                    val response=it.body()
                    when(response!!.statusCode){
                        Constants.REQUEST_OK -> {
                            liveDataCoupon.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataCoupon.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataCoupon.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataCoupon.postValue(Resource.noInterNet("", null))
                        }
                    }


                }
            }catch (ex:Exception){

            }
        }
    }
}
