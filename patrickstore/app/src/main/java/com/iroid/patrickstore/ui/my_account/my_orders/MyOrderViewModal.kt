package com.iroid.patrickstore.ui.my_account.my_orders

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.my_orders.MyOrderResponse
import com.iroid.patrickstore.model.order_confirm.OrderConfirmResponse
import com.iroid.patrickstore.model.order_summary.OrderSummaryResponse
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.launch

class MyOrderViewModal : ViewModel() {
    private val liveDataMyOrder = MutableLiveData<Resource<MyOrderResponse>>()
    val myOrderLiveData:LiveData<Resource<MyOrderResponse>>get() = liveDataMyOrder


    fun getMyOrder() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataMyOrder.postValue(Resource.loading(null))
                repository.getCustomerOrder(1,10).let {
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
                Log.e("#123456", "getMyOrder: $ex" )
                liveDataMyOrder.postValue(Resource.noInterNet("", null))
            }
        }
    }





}
