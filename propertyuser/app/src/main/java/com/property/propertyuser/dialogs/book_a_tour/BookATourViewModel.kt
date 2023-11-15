package com.property.propertyuser.dialogs.book_a_tour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.book_tour_dialog.BookTourDialogResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class BookATourViewModel:ViewModel() {

    private val bookTourDateTimeResponseLiveData= MutableLiveData<Resource<BookTourDialogResponse>>()

    fun bookATourAddDateTime(property_id: String,date: String,time_range: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            bookTourDateTimeResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.bookTourAddDateTime(property_id,date,time_range).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        bookTourDateTimeResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        bookTourDateTimeResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        bookTourDateTimeResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        bookTourDateTimeResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex: Exception){
                bookTourDateTimeResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getBookedTourDataResponse(): LiveData<Resource<BookTourDialogResponse>> {
        return bookTourDateTimeResponseLiveData
    }
}