package com.property.propertyuser.ui.main.my_property.view_details.rental_view_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.rental_user_property_view_details.RentalUserPropertyViewDetailsResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class RentalViewDetailsViewModel : ViewModel() {
    private val rentalUserPropertyViewDetailsResponseLiveData =
        MutableLiveData<Resource<RentalUserPropertyViewDetailsResponse>>()

    fun fetchRentalUserPropertyViewDetails(user_property_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            rentalUserPropertyViewDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.rentalUserPropertyViewDetails(user_property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        rentalUserPropertyViewDetailsResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        rentalUserPropertyViewDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        rentalUserPropertyViewDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        rentalUserPropertyViewDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                Log.e("TAG", "fetchRentalUserPropertyViewDetails: "+ ex)
                rentalUserPropertyViewDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getRentalUserPropertyViewDetailsResponse(): LiveData<Resource<RentalUserPropertyViewDetailsResponse>> {
        return rentalUserPropertyViewDetailsResponseLiveData
    }
}