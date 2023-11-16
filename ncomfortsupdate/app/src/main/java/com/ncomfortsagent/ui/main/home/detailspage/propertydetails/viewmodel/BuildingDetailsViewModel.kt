package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncomfortsagent.R
import com.ncomfortsagent.data.ApiRepositoryProvider
import com.ncomfortsagent.model.building_details.AgentBuildingDetailsResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class BuildingDetailsViewModel(@SuppressLint("StaticFieldLeak") private val activity: Activity) :
    ViewModel() {

    private val buildingDetailsLiveData = MutableLiveData<Resource<AgentBuildingDetailsResponse>>()

    fun getAgentBuildingDetailsResponse(): LiveData<Resource<AgentBuildingDetailsResponse>> {
        return buildingDetailsLiveData
    }

    fun buildingDetails(property_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            buildingDetailsLiveData.postValue(Resource.loading(null))
            try {
                repository.buildingDetails(property_id.toString()).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        buildingDetailsLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        buildingDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        buildingDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        buildingDetailsLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                buildingDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                buildingDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                buildingDetailsLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }
}