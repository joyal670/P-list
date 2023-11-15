package com.property.propertyuser.ui.main.map_and_nearby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.favorite_list.FavoriteListResponse
import com.property.propertyuser.modal.home_listing_property_location.HomeListingPropertyLocationResponse
import com.property.propertyuser.modal.map_near_places.GoogleResponseModel
import com.property.propertyuser.modal.property_location_details.PropertyLocationDetailsResponse
import com.property.propertyuser.modal.show_all_near_by_property_details.ShowAllNearbyPropertyDetailsMapResponse
import com.property.propertyuser.modal.show_all_property_location.ShowAllPropertyLocationResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class MapAndNearByViewModel:ViewModel() {
    private val listNearByPlacesLiveData= MutableLiveData<Resource<GoogleResponseModel>>()

    fun fetchNearByPlaces(location: String, radius: String,
                          type: String,keyword: String, key: String){
        val repository= ApiRepositoryProvider.providerGoogleMapsApiRepository()
        viewModelScope.launch {
            listNearByPlacesLiveData.postValue(Resource.loading(null))
            try {
                repository.getNearByPlaces(location,radius,type,keyword,key).let {
                    val response=it.body()
                    if(!(response?.results.isNullOrEmpty())){
                        listNearByPlacesLiveData.postValue(Resource.success(response))
                    }
                    else{
                        listNearByPlacesLiveData.postValue(Resource.dataEmpty("Empty result in near by places",
                            response))
                    }
                }

            }catch (ex: Exception){
                listNearByPlacesLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getNearByPlacesListResponse(): LiveData<Resource<GoogleResponseModel>> {
        return listNearByPlacesLiveData
    }

    private val homeListingPropertyLocationResponseLiveData=MutableLiveData<Resource<HomeListingPropertyLocationResponse>>()

    fun fetchHomeListingPropertyLocation(property_id: String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homeListingPropertyLocationResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.listingPropertyLocationFromHome(property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        homeListingPropertyLocationResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        homeListingPropertyLocationResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        homeListingPropertyLocationResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                homeListingPropertyLocationResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getHomeListingPropertyLocationResponse():LiveData<Resource<HomeListingPropertyLocationResponse>>{
        return homeListingPropertyLocationResponseLiveData
    }

    private val showAllNearbyPropertyLocationDetailsResponseLiveData=
        MutableLiveData<Resource<ShowAllNearbyPropertyDetailsMapResponse>>()

    fun fetchShowAllNearbyPropertyLocationDetails(property_id: String,latitude: String,longitude: String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            showAllNearbyPropertyLocationDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.showAllNearbyPropertyLocationMap(property_id,latitude,longitude).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        showAllNearbyPropertyLocationDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        showAllNearbyPropertyLocationDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        showAllNearbyPropertyLocationDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                showAllNearbyPropertyLocationDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getShowAllNearbyPropertyLocationDetailsResponse():LiveData<Resource<ShowAllNearbyPropertyDetailsMapResponse>>{
        return showAllNearbyPropertyLocationDetailsResponseLiveData
    }

    private val propertyLocationDetailsResponseLiveData=
        MutableLiveData<Resource<PropertyLocationDetailsResponse>>()

    fun fetchPropertyLocationDetails(property_id: String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            propertyLocationDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.propertyLocationDetailsMap(property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        propertyLocationDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        propertyLocationDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        propertyLocationDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                propertyLocationDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getPropertyLocationDetailsResponse():LiveData<Resource<PropertyLocationDetailsResponse>>{
        return propertyLocationDetailsResponseLiveData
    }

    private val showAllPropertyLocationDetailsResponseLiveData=
        MutableLiveData<Resource<ShowAllPropertyLocationResponse>>()

    fun fetchShowAllPropertyLocationDetails(latitude: String,longitude: String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            showAllPropertyLocationDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.showAllPropertyLocations(latitude,longitude).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        showAllPropertyLocationDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        showAllPropertyLocationDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        showAllPropertyLocationDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                showAllPropertyLocationDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getShowAllPropertyLocationDetailsResponse():LiveData<Resource<ShowAllPropertyLocationResponse>>{
        return showAllPropertyLocationDetailsResponseLiveData
    }
}