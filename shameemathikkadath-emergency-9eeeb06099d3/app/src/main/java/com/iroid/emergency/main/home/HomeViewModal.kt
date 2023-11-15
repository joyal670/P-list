package com.iroid.emergency.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.emergency.api.ApiRepositoryProvider
import com.iroid.emergency.modal.common.CommonResponse
import com.iroid.emergency.start_up.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModal : ViewModel() {
    private val liveDataHome = MutableLiveData<Resource<CommonResponse>>()
    val homeLiveData: LiveData<Resource<CommonResponse>> get() = liveDataHome

    private val liveDataApproval = MutableLiveData<Resource<CommonResponse>>()
    val approvalLiveData: LiveData<Resource<CommonResponse>> get() = liveDataApproval

    private val liveDataEmergency = MutableLiveData<Resource<CommonResponse>>()
    val emergencyLiveData: LiveData<Resource<CommonResponse>> get() = liveDataEmergency

    private val liveDataCompleteNormal = MutableLiveData<Resource<CommonResponse>>()
    val completeNormalLiveData: LiveData<Resource<CommonResponse>> get() = liveDataCompleteNormal

    private val liveDataSecondary = MutableLiveData<Resource<CommonResponse>>()
    val completeSecondaryLiveData: LiveData<Resource<CommonResponse>> get() = liveDataSecondary

    private val liveDataUpdateLocation = MutableLiveData<Resource<CommonResponse>>()
    val updateLiveData: LiveData<Resource<CommonResponse>> get() = liveDataUpdateLocation

    fun completeSecondary(emergency_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        liveDataSecondary.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.emergencySecondary(emergency_id).let {
                    val response=it.body()
                    if(response!!.status){
                        liveDataSecondary.postValue(Resource.success(it.body()))
                    }else{
                        liveDataSecondary.postValue(Resource.error(response.message,null))
                    }

                }

            } catch (ex: Exception) {
                liveDataSecondary.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun completeNormal(emergency_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        liveDataCompleteNormal.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.emergencyComplete(emergency_id).let {
                    liveDataCompleteNormal.postValue(Resource.success(it.body()))
                }

            } catch (ex: Exception) {
                liveDataCompleteNormal.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun approvalLiveData(emergency_id: String, status: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        liveDataApproval.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.emergencyApproval(emergency_id, status).let {
                    val response=it.body()
                    if(response!!.status){
                        liveDataApproval.postValue(Resource.success(it.body()))
                    }else{
                        liveDataApproval.postValue(Resource.error(response.message,null))
                    }

                }

            } catch (ex: Exception) {
                liveDataApproval.postValue(Resource.noInterNet("", null))
            }
        }
    }
    fun updateLocation(latitude: String, longitude: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        liveDataUpdateLocation.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.locationUpdate(latitude, longitude).let {
                    val response = it.body()
                }
            } catch (ex: Exception) {
                liveDataUpdateLocation.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun updateEmergency(latitude: String, longitude: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        liveDataEmergency.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.updateEmergency(latitude, longitude).let {
                    val response = it.body()
                    liveDataEmergency.postValue(Resource.success(response))
                }
            } catch (ex: Exception) {
                liveDataEmergency.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun getHome() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        liveDataHome.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.getHome().let {
                    val response = it.body()
                    liveDataHome.postValue(Resource.success(response))
                }
            } catch (ex: Exception) {
                liveDataHome.postValue(Resource.noInterNet("", null))
            }
        }
    }
}
