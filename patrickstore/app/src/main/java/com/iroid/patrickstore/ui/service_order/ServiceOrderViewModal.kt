package com.iroid.patrickstore.ui.service_order

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_address.AddAddressResponse
import com.iroid.patrickstore.model.add_to_cart.AddToCartResponse
import com.iroid.patrickstore.model.all_categories.AllCategoriesResponse
import com.iroid.patrickstore.model.cart_listing.CartListingResponse
import com.iroid.patrickstore.model.categoryProdcut.CategoryProductResponse
import com.iroid.patrickstore.model.categoryProdcut.seller.SellerCategoryWiseResponse
import com.iroid.patrickstore.model.service.service_order.ServiceOrderResponse
import com.iroid.patrickstore.model.service.service_order_detail.ServiceOrderDetailResponse
import com.iroid.patrickstore.model.service.service_update.ServiceUpdate
import com.iroid.patrickstore.utils.*
import kotlinx.coroutines.launch

class ServiceOrderViewModal : ViewModel() {
    private val liveServiceOrder= MutableLiveData<Resource<ServiceOrderResponse>>()
    val serviceOrderLiveData:LiveData<Resource<ServiceOrderResponse>> get() = liveServiceOrder

    private val liveServiceOrderDetail= MutableLiveData<Resource<ServiceOrderDetailResponse>>()
    val serviceOrderDetailData:LiveData<Resource<ServiceOrderDetailResponse>> get() = liveServiceOrderDetail

    private val liveServiceUpdate= MutableLiveData<Resource<ServiceUpdate>>()
    val serviceUpdateLiveData:LiveData<Resource<ServiceUpdate>> get() = liveServiceUpdate

    fun updateServiceLiveData(quoteId:String,
                              responseType:String,
                              rate:String,
                              comments:String){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            liveServiceUpdate.postValue(Resource.loading(null))
            try {
                repository.updateService(quoteId, responseType, rate, comments).let {
                    val response=it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveServiceUpdate.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveServiceUpdate.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveServiceUpdate.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveServiceUpdate.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_ERROR -> {
                            liveServiceUpdate.postValue(Resource.error("", null))
                        }
                    }

                }
            }catch (ex:Exception){
                Log.e("#565656", ex.toString())
            }
        }

    }


    fun getServiceDetail(serviceQuoteId:String){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            liveServiceOrderDetail.postValue(Resource.loading(null))
            try {
                repository.serviceOrderDetail(serviceQuoteId).let {
                    val response=it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveServiceOrderDetail.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveServiceOrderDetail.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveServiceOrderDetail.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveServiceOrderDetail.postValue(Resource.noInterNet("", null))
                        }
                    }

                }
            }catch (ex:Exception){

            }
        }
    }
    fun getServiceOrder(){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            liveServiceOrder.postValue(Resource.loading(null))
            try {
                repository.quoteList(1,10).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveServiceOrder.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveServiceOrder.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveServiceOrder.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveServiceOrder.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e("123", "getCategorySeller: $ex" )
                liveServiceOrder.postValue(Resource.noInterNet("", null))
            }
        }
    }

}
