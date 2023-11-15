package com.iroid.patrickstore.ui.my_account.my_orders.order_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.my_orders.MyOrderResponse
import com.iroid.patrickstore.model.order_confirm.OrderConfirmResponse
import com.iroid.patrickstore.model.order_detail.OrderDetailResponse
import com.iroid.patrickstore.model.order_summary.OrderSummaryResponse
import com.iroid.patrickstore.model.return_order.ReturnResponse
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.launch

class OrderDetailViewModal : ViewModel() {
    private val liveDataMyOrder = MutableLiveData<Resource<OrderDetailResponse>>()
    val myOrderLiveData:LiveData<Resource<OrderDetailResponse>>get() = liveDataMyOrder

    private val liveDataReturn = MutableLiveData<Resource<ReturnResponse>>()
    val returnLiveData:LiveData<Resource<ReturnResponse>>get() = liveDataReturn

    fun returnOrder(orderId:String,productId:String,returnMode:String,reason:String,returnImageId:String,comments:String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                repository.returnRequest(orderId, productId, returnMode, reason, returnImageId, comments).let {
                    liveDataReturn.postValue(Resource.loading(null))
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataReturn.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataReturn.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataReturn.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataReturn.postValue(Resource.noInterNet("", null))
                        }
                    }
                }

            }catch (ex:Exception){

            }
        }
    }


    fun getSingleOrder(orderId: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataMyOrder.postValue(Resource.loading(null))
                repository.getSingleOrder(orderId).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataMyOrder.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataMyOrder.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataMyOrder.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataMyOrder.postValue(Resource.noInterNet("", null))
                        }
                    }

                }
            } catch (ex: Exception) {
                Log.e("#56565656","$ex")
                liveDataMyOrder.postValue(Resource.noInterNet("", null))
            }
        }
    }





}
