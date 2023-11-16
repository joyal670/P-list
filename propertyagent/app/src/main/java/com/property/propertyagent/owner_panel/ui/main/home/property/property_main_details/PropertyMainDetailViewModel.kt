package com.property.propertyagent.owner_panel.ui.main.home.property.property_main_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_common_report.OwnerCommonPdfReportResponse
import com.property.propertyagent.modal.owner.owner_property_main_details.new.OwnerPropertyMainDetailsNewResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class PropertyMainDetailViewModel : ViewModel() {

    private val ownerPropertyMainDetailsResponseLiveData =
        MutableLiveData<Resource<OwnerPropertyMainDetailsNewResponse>>()

    private val ownerPropertyReportResponseLiveData =
        MutableLiveData<Resource<OwnerCommonPdfReportResponse>>()

    fun ownerPropertyDetails(id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPropertyMainDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPropertyMainDetails(id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerPropertyMainDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerPropertyMainDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerPropertyMainDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerPropertyMainDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                ownerPropertyMainDetailsResponseLiveData.postValue(Resource.noInternet("", null))
                Log.e("TAG", "paymentListOfProperties: $ex")
            }
        }
    }

    fun getOwnerPropertyDetailsResponse(): LiveData<Resource<OwnerPropertyMainDetailsNewResponse>> {
        return ownerPropertyMainDetailsResponseLiveData
    }

    fun ownerPropertyReport(id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPropertyReportResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPropertyReport(id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerPropertyReportResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerPropertyReportResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerPropertyReportResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerPropertyReportResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                ownerPropertyReportResponseLiveData.postValue(Resource.noInternet("", null))
                Log.e("TAG", "paymentListOfProperties: $ex")
            }
        }
    }

    fun getOwnerPropertyReportResponse(): LiveData<Resource<OwnerCommonPdfReportResponse>> {
        return ownerPropertyReportResponseLiveData
    }
}