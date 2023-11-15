package com.property.propertyagent.owner_panel.ui.main.home.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_common_report.OwnerCommonPdfReportResponse
import com.property.propertyagent.modal.owner.owner_home_monthly_account.OwnerHomeMonthlyAccountResponse
import com.property.propertyagent.modal.owner.owner_home_property.OwnerHomePropertyDetailsResponse
import com.property.propertyagent.modal.owner.owner_home_property_list.OwnerHomePropertyListResponse
import com.property.propertyagent.modal.owner.owner_home_statistics.OwnerHomeStatisticsResponse
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class OwnerHomeViewModel : ViewModel() {

    private val homeStatisticsResponseLiveData =
        MutableLiveData<Resource<OwnerHomeStatisticsResponse>>()

    private val homePropertyListResponseLiveData =
        MutableLiveData<Resource<OwnerHomePropertyListResponse>>()

    private val homeMonthlyAccountResponseLiveData =
        MutableLiveData<Resource<OwnerHomeMonthlyAccountResponse>>()

    private val homePropertySearchResponseLiveData =
        MutableLiveData<Resource<OwnerPropertyMainListingResponse>>()

    private val homePropertyDetailsResponseLiveData =
        MutableLiveData<Resource<OwnerHomePropertyDetailsResponse>>()

    private val homeYearlyReportResponseLiveData =
        MutableLiveData<Resource<OwnerCommonPdfReportResponse>>()

    private val homeMonthlyReportResponseLiveData =
        MutableLiveData<Resource<OwnerCommonPdfReportResponse>>()

    /* home statistics api */
    fun homeStatistics(year : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homeStatisticsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerHomeStatistics(year).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        homeStatisticsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        homeStatisticsResponseLiveData.postValue(
                            Resource.error(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        homeStatisticsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        homeStatisticsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }
            } catch (ex : Exception) {
                homeStatisticsResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    fun getHomeStatisticsResponse() : LiveData<Resource<OwnerHomeStatisticsResponse>> {
        return homeStatisticsResponseLiveData
    }

    /* home property list api */
    fun homePropertyList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homePropertyListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerHomePropertyList().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        homePropertyListResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        homePropertyListResponseLiveData.postValue(
                            Resource.error(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        homePropertyListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        homePropertyListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }
            } catch (ex : Exception) {
                homePropertyListResponseLiveData.postValue(Resource.noInternet("" , null))
            }
        }
    }

    fun getHomePropertyListResponse() : LiveData<Resource<OwnerHomePropertyListResponse>> {
        return homePropertyListResponseLiveData
    }

    /* home monthly list api */
    fun homeMonthlyAccount(property_id : Int , date : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homeMonthlyAccountResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerHomeMonthlyAccount(property_id , date).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        homeMonthlyAccountResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        homeMonthlyAccountResponseLiveData.postValue(
                            Resource.error(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        homeMonthlyAccountResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        homeMonthlyAccountResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }
            } catch (ex : Exception) {
                homeMonthlyAccountResponseLiveData.postValue(Resource.noInternet("" , null))
            }
        }
    }

    fun getHomeMonthlyAccountResponse() : LiveData<Resource<OwnerHomeMonthlyAccountResponse>> {
        return homeMonthlyAccountResponseLiveData
    }

    /* home property search api */
    fun homeSearch(page : String , property_name : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homePropertySearchResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerHomePropertySearch(page , property_name).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        homePropertySearchResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        homePropertySearchResponseLiveData.postValue(
                            Resource.error(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        homePropertySearchResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        homePropertySearchResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }
            } catch (ex : Exception) {
                homePropertySearchResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    fun getHomeSearchResponse() : LiveData<Resource<OwnerPropertyMainListingResponse>> {
        return homePropertySearchResponseLiveData
    }

    /* home property details api */
    fun homePropertyDetails() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homePropertyDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerHomePropertyDetails().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        homePropertyDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        homePropertyDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        homePropertyDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        homePropertyDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }
            } catch (ex : Exception) {
                homePropertyDetailsResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    fun getHomePropertyDetailsResponse() : LiveData<Resource<OwnerHomePropertyDetailsResponse>> {
        return homePropertyDetailsResponseLiveData
    }

    /* home yearly report api */
    fun homeYearlyReport(year : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homeYearlyReportResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerHomeYearlyReport(year).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        homeYearlyReportResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        homeYearlyReportResponseLiveData.postValue(
                            Resource.error(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        homeYearlyReportResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        homeYearlyReportResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }
            } catch (ex : Exception) {
                homeYearlyReportResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    fun getHomeYearlyReportResponse() : LiveData<Resource<OwnerCommonPdfReportResponse>> {
        return homeYearlyReportResponseLiveData
    }

    /* home monthly report api */
    fun homeMonthlyReport(property_id : String , date : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homeMonthlyReportResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerHomeMonthlyReport(property_id , date).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        homeMonthlyReportResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        homeMonthlyReportResponseLiveData.postValue(
                            Resource.error(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        homeMonthlyReportResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        homeMonthlyReportResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }
            } catch (ex : Exception) {
                homeMonthlyReportResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    fun getHomeMonthlyReportResponse() : LiveData<Resource<OwnerCommonPdfReportResponse>> {
        return homeMonthlyReportResponseLiveData
    }
}