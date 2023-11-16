package com.property.propertyagent.owner_panel.ui.main.sidemenu.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_common_report.OwnerCommonPdfReportResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class ReportsViewModel : ViewModel() {

    private val ownerVacantReportResponseLiveData =
        MutableLiveData<Resource<OwnerCommonPdfReportResponse>>()

    private val ownerOccupiedReportResponseLiveData =
        MutableLiveData<Resource<OwnerCommonPdfReportResponse>>()

    private val ownerOverallReportResponseLiveData =
        MutableLiveData<Resource<OwnerCommonPdfReportResponse>>()

    fun ownerVacantReport() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerVacantReportResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerVacantReport().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerVacantReportResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response!!.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerVacantReportResponseLiveData.postValue(Resource.error(response.response ,
                            response))
                    }
                    if (response!!.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerVacantReportResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                    if (response!!.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerVacantReportResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                }
            } catch (ex : Exception) {
                ownerVacantReportResponseLiveData.postValue(Resource.noInternet("" , null))
            }
        }
    }

    fun getOwnerVacantReportResponse() : LiveData<Resource<OwnerCommonPdfReportResponse>> {
        return ownerVacantReportResponseLiveData
    }

    fun ownerOccupiedReport() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerOccupiedReportResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerOccupiedReport().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerOccupiedReportResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response!!.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerOccupiedReportResponseLiveData.postValue(Resource.error(response.response ,
                            response))
                    }
                    if (response!!.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerOccupiedReportResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                    if (response!!.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerOccupiedReportResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                }
            } catch (ex : Exception) {
                ownerOccupiedReportResponseLiveData.postValue(Resource.noInternet("" , null))
            }
        }
    }

    fun getOwnerOccupiedReportResponse() : LiveData<Resource<OwnerCommonPdfReportResponse>> {
        return ownerOccupiedReportResponseLiveData
    }

    fun ownerOverallReport() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerOverallReportResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerOverallReport().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerOverallReportResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response!!.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerOverallReportResponseLiveData.postValue(Resource.error(response.response ,
                            response))
                    }
                    if (response!!.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerOverallReportResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                    if (response!!.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerOverallReportResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                }
            } catch (ex : Exception) {
                ownerOverallReportResponseLiveData.postValue(Resource.noInternet("" , null))
            }
        }
    }

    fun getOwnerOverallReportResponse() : LiveData<Resource<OwnerCommonPdfReportResponse>> {
        return ownerOverallReportResponseLiveData
    }
}