package com.property.propertyuser.ui.main.my_property.rental

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.my_property_list.MyPropertiesResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class RentalViewModel:ViewModel() {
    private val myPropertiesListResponseLiveData= MutableLiveData<Resource<MyPropertiesResponse>>()

    fun fetchMyPropertiesList(page: String,user_property_status: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            myPropertiesListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.myPropertiesListDetails(page,user_property_status).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        myPropertiesListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        myPropertiesListResponseLiveData.postValue(Resource.dataEmpty("",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        myPropertiesListResponseLiveData.postValue(Resource.dataEmpty("",response))
                    }
                    if(response!!.status==Constants.INTERNAL_SERVER_ERROR){
                        myPropertiesListResponseLiveData.postValue(Resource.error("",response))
                    }
                }

            }catch (ex:Exception){
                myPropertiesListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getMyPropertiesListResponse(): LiveData<Resource<MyPropertiesResponse>> {
        return myPropertiesListResponseLiveData
    }
}