package com.ncomfortsagent.ui.main.home.home.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncomfortsagent.R
import com.ncomfortsagent.data.ApiRepositoryProvider
import com.ncomfortsagent.model.enquiry.AgentHomeEnquiryResponse
import com.ncomfortsagent.model.home_count.AgentHomeCountResponse
import com.ncomfortsagent.model.logout.AgentLogoutResponse
import com.ncomfortsagent.model.property.PropertyNewResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class HomeViewModel(@SuppressLint("StaticFieldLeak") private val activity: Activity) : ViewModel() {

    private val logoutLiveData = MutableLiveData<Resource<AgentLogoutResponse>>()
    private val myPropertyLiveData = MutableLiveData<Resource<PropertyNewResponse>>()
    private val enquiryLiveData = MutableLiveData<Resource<AgentHomeEnquiryResponse>>()
    private val homeCountLiveData = MutableLiveData<Resource<AgentHomeCountResponse>>()
    private val homeSearchLiveData = MutableLiveData<Resource<PropertyNewResponse>>()

    /* logout */
    fun getAgentLogoutResponse(): LiveData<Resource<AgentLogoutResponse>> {
        return logoutLiveData
    }

    fun logout() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            logoutLiveData.postValue(Resource.loading(null))
            try {
                repository.logout().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        logoutLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        logoutLiveData.postValue(Resource.error(response.response, response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        logoutLiveData.postValue(Resource.error(response.response, response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        logoutLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: Exception) {
                logoutLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }


    /* my property */
    fun getAgentMyPropertyResponse(): LiveData<Resource<PropertyNewResponse>> {
        return myPropertyLiveData
    }

    fun myProperty(page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            myPropertyLiveData.postValue(Resource.loading(null))
            try {
                repository.myProperty(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        myPropertyLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        myPropertyLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        myPropertyLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        myPropertyLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                myPropertyLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                myPropertyLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                myPropertyLiveData.postValue(Resource.error("", null))
            }
        }
    }


    /* enquiry */
    fun getAgentEnquiryResponse(): LiveData<Resource<AgentHomeEnquiryResponse>> {
        return enquiryLiveData
    }

    fun enquiry(page: String,enquiryStatus:Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            enquiryLiveData.postValue(Resource.loading(null))
            try {
                repository.enquiry(page,enquiryStatus).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        enquiryLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        enquiryLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        enquiryLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        enquiryLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                enquiryLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                enquiryLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                Log.e("TAG", "enquiry: "+ ex )
                enquiryLiveData.postValue(Resource.error("", null))
            }
        }
    }

    /* home count */
    fun getAgentHomeCountResponse(): LiveData<Resource<AgentHomeCountResponse>> {
        return homeCountLiveData
    }

    fun homeCount() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homeCountLiveData.postValue(Resource.loading(null))
            try {
                repository.homeCount().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        homeCountLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        Log.e("TAG", "homeCount: REQUEST_BAD_REQUEST" + response.response )
                        homeCountLiveData.postValue(
                            Resource.error(
                                response.response.toString(),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        Log.e("TAG", "homeCount: REQUEST_UNAUTHORIZED" + response.response )
                        homeCountLiveData.postValue(
                            Resource.error(
                                response.response.toString(),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        Log.e("TAG", "homeCount: INTERNAL_SERVER_ERROR" + response.response )
                        homeCountLiveData.postValue(
                            Resource.dataEmpty(
                                response.response.toString(),
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                Log.e("TAG", "homeCount: " + ex )
                homeCountLiveData.postValue(Resource.error("", null))
            }
        }
    }

    /* my property search */
    fun getAgentMyPropertySearchResponse(): LiveData<Resource<PropertyNewResponse>> {
        return homeSearchLiveData
    }

    fun myPropertySearch(property_name: String, page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homeSearchLiveData.postValue(Resource.loading(null))
            try {
                repository.myPropertySearch(property_name, page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        homeSearchLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        homeSearchLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        homeSearchLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        homeSearchLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                homeSearchLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                homeSearchLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                homeSearchLiveData.postValue(Resource.error("", null))
            }
        }
    }

}