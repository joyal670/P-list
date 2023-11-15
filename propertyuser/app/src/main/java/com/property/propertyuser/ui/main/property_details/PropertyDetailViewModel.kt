package com.property.propertyuser.ui.main.property_details

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.book_property_details.BookPropertyDetailsResponse
import com.property.propertyuser.modal.pdf_property_details.PropertyDetailsPdfResponse
import com.property.propertyuser.modal.property_details.PropertyDetilsResponse
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


class PropertyDetailViewModel : ViewModel() {
    private val propertyDetailResponseLiveData=MutableLiveData<Resource<PropertyDetilsResponse>>()
    private val updateFavoritePropertyResponseLiveData=MutableLiveData<Resource<CommonResponse>>()
    private val downloadPropertyDetailsPdfResponseLiveData=MutableLiveData<Resource<PropertyDetailsPdfResponse>>()

    fun downloadPropertyDetailsPdf(property_id:String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                downloadPropertyDetailsPdfResponseLiveData.postValue(Resource.loading(null))
                repository.downloadPropertyDetailsPdf(property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        downloadPropertyDetailsPdfResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        downloadPropertyDetailsPdfResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        downloadPropertyDetailsPdfResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        downloadPropertyDetailsPdfResponseLiveData.postValue(Resource.error("null",response))
                    }
                }

            }catch (ex:Exception){
                Log.e("123", "getPropertyDetail: $ex" )
                downloadPropertyDetailsPdfResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getPropertyDetailsPdfLiveData():LiveData<Resource<PropertyDetailsPdfResponse>>{
        return downloadPropertyDetailsPdfResponseLiveData
    }

    fun getPropertyDetail(property_id:Int){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                propertyDetailResponseLiveData.postValue(Resource.loading(null))
                repository.getPropertyDetails(property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        propertyDetailResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        propertyDetailResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        propertyDetailResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                Log.e("123", "getPropertyDetail: $ex" )
                propertyDetailResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getPropertyDetailLiveData():LiveData<Resource<PropertyDetilsResponse>>{
        return propertyDetailResponseLiveData
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

}