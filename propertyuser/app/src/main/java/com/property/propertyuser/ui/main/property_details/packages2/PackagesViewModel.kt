package com.property.propertyuser.ui.main.property_details.packages2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.other_package.PackagesResponse
import com.property.propertyuser.modal.property_list.PropertyListResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class PackagesViewModel:ViewModel() {

    private val packagesListResponseLiveData= MutableLiveData<Resource<PackagesResponse>>()

    fun fetchPackagesList(property_id:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            packagesListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.packagesDetails(property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        packagesListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        packagesListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        packagesListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex: Exception){
                packagesListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getPackagesListLiveData(): LiveData<Resource<PackagesResponse>> {
        return packagesListResponseLiveData
    }
}