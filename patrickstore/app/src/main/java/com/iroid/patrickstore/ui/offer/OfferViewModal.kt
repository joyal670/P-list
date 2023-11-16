package com.iroid.patrickstore.ui.offer

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_address.AddAddressResponse
import com.iroid.patrickstore.model.delivered_order.DeliveredOrderResponse
import com.iroid.patrickstore.model.festval_offers.FestivalOfferResponse
import com.iroid.patrickstore.utils.*
import kotlinx.coroutines.launch

class OfferViewModal : ViewModel() {
    private val liveDataFestival=MutableLiveData<Resource<FestivalOfferResponse>>()
    val festivalLiveData:LiveData<Resource<FestivalOfferResponse>> get() = liveDataFestival
    fun getFestivalLiveData(){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataFestival.postValue(Resource.loading(null))
                repository.getFestival().let {
                    val response=it.body()
                    if(response!=null){
                        when (response!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataFestival.postValue(Resource.success(response))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataFestival.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataFestival.postValue(Resource.error(response.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataFestival.postValue(Resource.noInterNet("", null))
                            }
                        }
                    }else{
                        liveDataFestival.postValue(Resource.error("", null))
                    }

                }
            }catch (ex:Exception){
                Log.e("#12346","$ex")
                liveDataFestival.postValue(Resource.noInterNet("", null))
            }
        }
    }
}
