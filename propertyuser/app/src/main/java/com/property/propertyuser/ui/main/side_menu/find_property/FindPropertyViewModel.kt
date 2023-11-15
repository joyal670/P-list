package com.property.propertyuser.ui.main.side_menu.find_property

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.event_booking_package_details.EventBookingPackageDetailsResponse
import com.property.propertyuser.modal.fetch_requested_property_data.FetchRequestedPropertyDataResponse
import com.property.propertyuser.modal.filter_home.ResidentialCommercialResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import java.math.BigInteger

class FindPropertyViewModel:ViewModel() {

    private val fetchRequestedPropertyDataResponseLiveData=
        MutableLiveData<Resource<FetchRequestedPropertyDataResponse>>()

    fun fetchRequestedPropertyData(){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            fetchRequestedPropertyDataResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.fetchRequestedPropertyData().let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        fetchRequestedPropertyDataResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        fetchRequestedPropertyDataResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        fetchRequestedPropertyDataResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                fetchRequestedPropertyDataResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getRequestedPropertyDataResponse(): LiveData<Resource<FetchRequestedPropertyDataResponse>> {
        return fetchRequestedPropertyDataResponseLiveData
    }

    private val requestDesiredPropertyResponseLiveData=
        MutableLiveData<Resource<CommonResponse>>()

    fun requestDesiredProperty(property_to: String, category: String, type_id: String,
                               area: String, min_price: BigInteger, max_price: BigInteger, latitude: String,
                               longitude: String, description: String,location: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            requestDesiredPropertyResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.requestDesiredProperty(property_to,category, type_id, area,
                    min_price, max_price, latitude, longitude, description,location).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        requestDesiredPropertyResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        requestDesiredPropertyResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        requestDesiredPropertyResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                requestDesiredPropertyResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getRequestedDesiredPropertyResponse(): LiveData<Resource<CommonResponse>> {
        return requestDesiredPropertyResponseLiveData
    }

    private val mainFilterCategoryResponseLiveData=MutableLiveData<Resource<ResidentialCommercialResponse>>()

    fun fetchMainFilterCategories(category: String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            mainFilterCategoryResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getHomeFilterCategories(category).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        mainFilterCategoryResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        mainFilterCategoryResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        mainFilterCategoryResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex: java.lang.Exception){
                mainFilterCategoryResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getMainFilterCategoriesResponse():LiveData<Resource<ResidentialCommercialResponse>>{
        return mainFilterCategoryResponseLiveData
    }
}