package com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.R
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_building_details.OwnerBuildingDetailsListResponse
import com.property.propertyagent.modal.owner.owner_submit_for_verification.OwnerSubmitForVerification
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.activity.PropertyOwnerBuildingDetailedActivity
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class OwnerBuildingListingViewModel(private var context : PropertyOwnerBuildingDetailedActivity) : ViewModel() {
    private val ownerBuildingListingResponseLiveData =
        MutableLiveData<Resource<OwnerBuildingDetailsListResponse>>()

    private val ownerBuildingForVerificationResponseLiveData =
        MutableLiveData<Resource<OwnerSubmitForVerification>>()

    fun buildingList(owner_property_id : Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerBuildingListingResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerBuildingDetailsList(owner_property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerBuildingListingResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerBuildingListingResponseLiveData.postValue(Resource.error(context.getString(
                            R.string.internal_server_error),
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerBuildingListingResponseLiveData.postValue(Resource.error(context.getString(R.string.bad_request) ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerBuildingListingResponseLiveData.postValue(Resource.error(context.getString(R.string.unauthorized) ,
                            response))
                    }
                }

            } catch (ex : Exception) {
                ownerBuildingListingResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    fun getOwnerBuildingListResponse() : LiveData<Resource<OwnerBuildingDetailsListResponse>> {
        return ownerBuildingListingResponseLiveData
    }


    fun buildingForVerification(property_id : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerBuildingForVerificationResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerBuildingForVerification(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerBuildingForVerificationResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerBuildingForVerificationResponseLiveData.postValue(Resource.error(context.getString(
                            R.string.internal_server_error),
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerBuildingForVerificationResponseLiveData.postValue(Resource.error(context.getString(R.string.bad_request) ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerBuildingForVerificationResponseLiveData.postValue(Resource.error(context.getString(R.string.unauthorized) ,
                            response))
                    }
                }

            } catch (ex : Exception) {
                ownerBuildingForVerificationResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    fun getOwnerBuildingForVerificationResponse() : LiveData<Resource<OwnerSubmitForVerification>> {
        return ownerBuildingForVerificationResponseLiveData
    }
}