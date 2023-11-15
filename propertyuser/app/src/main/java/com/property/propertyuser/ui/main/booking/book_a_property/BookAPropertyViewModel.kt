package com.property.propertyuser.ui.main.booking.book_a_property

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.book_property.BookPropertyResponse
import com.property.propertyuser.modal.book_property_details.BookPropertyDetailsResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class BookAPropertyViewModel:ViewModel() {
    private val bookPropertyDetailsResponseLiveData=
        MutableLiveData<Resource<BookPropertyDetailsResponse>>()
    private val bookPropertyResponseLiveData=
        MutableLiveData<Resource<BookPropertyResponse>>()

    fun bookProperty(property_id: String,check_in: String,check_out: String,coupon: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                bookPropertyResponseLiveData.postValue(Resource.loading(null))
                repository.bookProperty(property_id,check_in,check_out,coupon).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        bookPropertyResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        bookPropertyResponseLiveData.postValue(Resource.dataEmpty(response.response,response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        bookPropertyResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex: Exception){
                Log.e("123", "getPropertyDetail: $ex" )
                bookPropertyResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getBookPropertyLiveData(): LiveData<Resource<BookPropertyResponse>> {
        return bookPropertyResponseLiveData
    }
    fun bookPropertyDetail(property_id:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                bookPropertyDetailsResponseLiveData.postValue(Resource.loading(null))
                repository.bookPropertyDetails(property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        bookPropertyDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        bookPropertyDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        bookPropertyDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex: Exception){
                Log.e("123", "getPropertyDetail: $ex" )
                bookPropertyDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getBookPropertyDetailLiveData(): LiveData<Resource<BookPropertyDetailsResponse>> {
        return bookPropertyDetailsResponseLiveData
    }
}