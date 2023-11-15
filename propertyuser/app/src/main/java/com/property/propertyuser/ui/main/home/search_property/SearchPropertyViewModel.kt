package com.property.propertyuser.ui.main.home.search_property

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.property_list.PropertyListResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchPropertyViewModel: ViewModel() {
    private val searchPropertyListResponseLiveData= MutableLiveData<Resource<PropertyListResponse>>()

    fun fetchSearchNamePropertyList(page:String,property_name:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            searchPropertyListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.searchNamePropertyList(page,property_name).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        searchPropertyListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        searchPropertyListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        searchPropertyListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex: Exception){
                searchPropertyListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getSearchNamePropertyListLiveData(): LiveData<Resource<PropertyListResponse>> {
        return searchPropertyListResponseLiveData
    }

    private val updateFavoritePropertyResponseLiveData=MutableLiveData<Resource<CommonResponse>>()

    fun updateFavoriteProperty(property_id:Int){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            updateFavoritePropertyResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.updateFavouriteProperty(property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        updateFavoritePropertyResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        updateFavoritePropertyResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        updateFavoritePropertyResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                updateFavoritePropertyResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun updateFavoritePropertyResponse():LiveData<Resource<CommonResponse>>{
        return updateFavoritePropertyResponseLiveData
    }
}