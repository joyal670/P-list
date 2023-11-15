package com.property.propertyuser.ui.main.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.favorite_list.FavoriteListResponse
import com.property.propertyuser.modal.property_list.PropertyListResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoritePropertyViewModel:ViewModel() {
    private val listFavoritePropertyResponseLiveData= MutableLiveData<Resource<FavoriteListResponse>>()
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

    fun fetchFavoriteProperty(page:Int,favourite_list:Int){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            listFavoritePropertyResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.listPropertyFavourite(page,favourite_list).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        listFavoritePropertyResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        listFavoritePropertyResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        listFavoritePropertyResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex: Exception){
                listFavoritePropertyResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getFavoritePropertyListResponse(): LiveData<Resource<FavoriteListResponse>> {
        return listFavoritePropertyResponseLiveData
    }
}