package com.property.propertyuser.ui.main.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.filter_home.ResidentialCommercialResponse
import com.property.propertyuser.modal.main_filter_details.FilterDetailsResponse
import com.property.propertyuser.modal.property_list.PropertyListResponse
import com.property.propertyuser.modal.user_notification.NotificationResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class FilterViewModel: ViewModel() {

    private val filterDetailsResponseLiveData= MutableLiveData<Resource<FilterDetailsResponse>>()
    private val mainFilterCategoryResponseLiveData=MutableLiveData<Resource<ResidentialCommercialResponse>>()

    private val filterMainPropertyListResponseLiveData=MutableLiveData<Resource<PropertyListResponse>>()

    fun fetchMainFilterPropertyList(page: String, property_to: String, type_id: ArrayList<Int>,category: String, min_price: String, max_price: String,
                                bed_id: String,bed: ArrayList<Int>,max_bed: String,bathroom_id: String, bathroom: ArrayList<Int>,
                                max_bathroom: String, area_id: String, min_area: String, max_area: String, amenities: ArrayList<Int>,
                                lat: String, lan: String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            filterMainPropertyListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getMainFilterPropertyList(page, property_to ,type_id,category, min_price, max_price, bed_id,bed,max_bed,
                    bathroom_id, bathroom, max_bathroom, area_id, min_area, max_area, amenities, lat, lan).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        filterMainPropertyListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        filterMainPropertyListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        filterMainPropertyListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex: java.lang.Exception){
                filterMainPropertyListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getMainFilterPropertyListLiveData():LiveData<Resource<PropertyListResponse>>{
        return filterMainPropertyListResponseLiveData
    }

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

    fun fetchFilterDetails(){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            filterDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.mainFilterDetails().let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        filterDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        filterDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        filterDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                filterDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getFilterDetails(): LiveData<Resource<FilterDetailsResponse>> {
        return filterDetailsResponseLiveData
    }
}