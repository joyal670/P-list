package com.property.propertyuser.ui.main.booking.book_a_tour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.booking_tour.BookingTourResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class BookingViewModel:ViewModel() {
    private val bookingTourResponseLiveData= MutableLiveData<Resource<BookingTourResponse>>()
    private val bookingTourConfirmationResponseLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun bookingTourConfirmation(tour_id: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            bookingTourConfirmationResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.bookingTourConfirmation(tour_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        bookingTourConfirmationResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        bookingTourConfirmationResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        bookingTourConfirmationResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        bookingTourConfirmationResponseLiveData.postValue(Resource.error(response.response,response))
                    }
                }

            }catch (ex:Exception){
                bookingTourConfirmationResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getBookingTourConfirmationResponse(): LiveData<Resource<CommonResponse>> {
        return bookingTourConfirmationResponseLiveData
    }
    fun fetchBookingTourDetails(tour_id: String,property_id:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            bookingTourResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.bookingTour(tour_id,property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        bookingTourResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        bookingTourResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        bookingTourResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                bookingTourResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getBookingTourResponse(): LiveData<Resource<BookingTourResponse>> {
        return bookingTourResponseLiveData
    }
}