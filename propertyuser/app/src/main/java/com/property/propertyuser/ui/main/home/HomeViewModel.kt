package com.property.propertyuser.ui.main.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.filter_home.ResidentialCommercialResponse
import com.property.propertyuser.modal.property_list.PropertyListResponse
/*import com.property.propertyuser.data.api.ApiHelper*/
import com.property.propertyuser.pojo.test.TestResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import com.property.propertyuser.utils.isConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


class HomeViewModel : ViewModel() {
    private val propertylistResponseLiveData=MutableLiveData<Resource<PropertyListResponse>>()
    private val updateFavoritePropertyResponseLiveData=MutableLiveData<Resource<CommonResponse>>()
    private val logoutResponseLiveData=MutableLiveData<Resource<CommonResponse>>()
    private val homeFilterCategoryResponseLiveData=MutableLiveData<Resource<ResidentialCommercialResponse>>()
    private val filterPropertyListResponseLiveData=MutableLiveData<Resource<PropertyListResponse>>()

    fun fetchFilterPropertyList(page: String, property_to: String, type_id: ArrayList<Int>,category: String ,min_price: String, max_price: String,
                              bed_id: String,bed: ArrayList<Int>,max_bed: String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            filterPropertyListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getHomeFilterPropertyList(page, property_to ,type_id,category, min_price, max_price, bed_id,bed,max_bed).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        filterPropertyListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        filterPropertyListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        filterPropertyListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                filterPropertyListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getFilterPropertyListLiveData():LiveData<Resource<PropertyListResponse>>{
        return filterPropertyListResponseLiveData
    }

    fun getHomeFilterCategories(category: String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homeFilterCategoryResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getHomeFilterCategories(category).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        homeFilterCategoryResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        homeFilterCategoryResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        homeFilterCategoryResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                homeFilterCategoryResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getHomeFilterCategoriesResponse():LiveData<Resource<ResidentialCommercialResponse>>{
        return homeFilterCategoryResponseLiveData
    }

    fun userLogout(){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            logoutResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.userLogout().let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        logoutResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        logoutResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        logoutResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                logoutResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getUserLogoutResponse():LiveData<Resource<CommonResponse>>{
        return logoutResponseLiveData
    }

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

    fun getPropertyList(lat:String,lan:String,page:String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            propertylistResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.listProperty(lat, lan,page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        propertylistResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        propertylistResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        propertylistResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                propertylistResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getPropertyListLiveData():LiveData<Resource<PropertyListResponse>>{
        return propertylistResponseLiveData
    }

}