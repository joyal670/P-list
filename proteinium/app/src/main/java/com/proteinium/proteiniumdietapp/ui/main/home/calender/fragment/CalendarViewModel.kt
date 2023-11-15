package com.proteinium.proteiniumdietapp.ui.main.home.calender.fragment


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.pojo.globalresponse.GlobalResponse
import com.proteinium.proteiniumdietapp.pojo.subscption_info.SubscriptionResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch


class CalendarViewModel() : ViewModel() {

    private val subscriptionInfoResponseLiveData = MutableLiveData<Resource<SubscriptionResponse>>()
    private val suspendUnsuspendLiveData=MutableLiveData<Resource<CommonResponse>>()
    private val globalSuspensionLiveData= MutableLiveData<Resource<GlobalResponse>>()


    fun getGlobalSuspensionLiveData() {
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            globalSuspensionLiveData.postValue(Resource.loading(null))
            try {
                repository.getGlobalSuspension().let {
                    val response = it.body()
                    Log.e("123456", "getGlobalSuspensionLiveData: $response" )
                    globalSuspensionLiveData.postValue(Resource.success(response))
                }
            } catch (ex: Exception) {
                globalSuspensionLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }


    fun fetchSuspendUnsuspendLiveData(userId: Int,date:String,plan_end_date:String,upcoming_plan_start_date:String,upcoming_status:Boolean,
                                      active_plan_end_date:String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            subscriptionInfoResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.suspendUnsuspendDelivery(userId,date,plan_end_date,upcoming_plan_start_date,upcoming_status,active_plan_end_date).let {
                    val response=it.body()
                    if(response?.status!!){
                        suspendUnsuspendLiveData.postValue(Resource.success(response))
                    }else{
                        suspendUnsuspendLiveData.postValue(Resource.dataEmpty(response.message,response))
                    }
                }
            }catch (ex:Exception){
                suspendUnsuspendLiveData.postValue(Resource.noInternet("",null))
            }
        }

    }
    fun foodRating(userId: Int,foodId:Int,rating:Int,date: String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            subscriptionInfoResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.rateFood(userId,foodId,rating,date).let {
                    val response=it.body()
                    if(response?.status!!){
                        suspendUnsuspendLiveData.postValue(Resource.success(response))
                    }else{
                        suspendUnsuspendLiveData.postValue(Resource.dataEmpty(response.message,response))
                    }
                }
            }catch (ex:Exception){
                suspendUnsuspendLiveData.postValue(Resource.noInternet("",null))
            }
        }

    }


    fun fetchSubscriptionInfo(userId: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            subscriptionInfoResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getSubscriptionInfo(userId).let {
                    val response = it.body()
                    if (response?.status!!) {
                        subscriptionInfoResponseLiveData.postValue(Resource.success(response))
                    } else {
                        subscriptionInfoResponseLiveData.postValue(
                            Resource.dataEmpty(
                                response.message,
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                Log.e("123", "fetchMealPlanResponse: " + ex)
                subscriptionInfoResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }


    fun getSubscriptionResponse(): LiveData<Resource<SubscriptionResponse>> {
        return subscriptionInfoResponseLiveData
    }
    fun getSuspendUnsuspendResponse():LiveData<Resource<CommonResponse>>{
        return suspendUnsuspendLiveData
    }
    fun getGlobalSuspension():LiveData<Resource<GlobalResponse>>{
        return globalSuspensionLiveData
    }
}