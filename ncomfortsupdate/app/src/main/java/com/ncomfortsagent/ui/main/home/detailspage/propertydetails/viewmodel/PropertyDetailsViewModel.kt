package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.viewmodel

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
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class PropertyDetailsViewModel(@SuppressLint("StaticFieldLeak") private val activity: Activity) :
    ViewModel() {

    private val propertyDetailsLiveData = MutableLiveData<Resource<AgentPropertyDetailsResponse>>()

    fun getAgentPropertyDetailsResponse(): LiveData<Resource<AgentPropertyDetailsResponse>> {
        return propertyDetailsLiveData
    }

    fun propertyDetails(property_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            propertyDetailsLiveData.postValue(Resource.loading(null))
            try {
                repository.propertyDetails(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        propertyDetailsLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        propertyDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        propertyDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        propertyDetailsLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                propertyDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                propertyDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                Log.e("TAG", "propertyDetails: "+ ex )
                propertyDetailsLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }
}