package com.iroid.patrickstore.ui.dailydeal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.daily_deal.DailyDealResponse
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.launch

class DailyDealViewModal : ViewModel() {

    private val liveDataDailyDeal = MutableLiveData<Resource<DailyDealResponse>>()
    val dailyDealLiveData: LiveData<Resource<DailyDealResponse>> get() = liveDataDailyDeal

    fun getDailyDeal() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            liveDataDailyDeal.postValue(Resource.loading(null))
            try {
                repository.getDailyDeal().let {
                    val response = it.body()
                    if(response!=null){
                        when (response!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataDailyDeal.postValue(Resource.success(response))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataDailyDeal.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataDailyDeal.postValue(Resource.error(response.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataDailyDeal.postValue(Resource.noInterNet("", null))
                            }
                        }
                    }else{
                        liveDataDailyDeal.postValue(Resource.error("", null))
                    }

                }
            } catch (ex: Exception) {
                Log.e("#565656", "getDailyDeal: $ex" )
                liveDataDailyDeal.postValue(Resource.noInterNet("", null))
            }
        }

    }

}
