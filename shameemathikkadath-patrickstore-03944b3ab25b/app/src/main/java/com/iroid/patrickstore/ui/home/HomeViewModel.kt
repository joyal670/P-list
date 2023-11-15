package com.iroid.patrickstore.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_to_cart.AddToCartResponse
import com.iroid.patrickstore.model.all_categories.AllCategoriesResponse
import com.iroid.patrickstore.model.home.HomeResponse
import com.iroid.patrickstore.model.location_request.Location
import com.iroid.patrickstore.model.location_request.LocationRequest
import com.iroid.patrickstore.model.location_request.response.LocationResponse
import com.iroid.patrickstore.model.service.ServiceCategoryResponse
import com.iroid.patrickstore.model.view_profile.ViewProfileResponse
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val liveDataHome = MutableLiveData<Resource<HomeResponse>>()
    val homeLiveData: LiveData<Resource<HomeResponse>> get() = liveDataHome

    private val liveDataAddToCart = MutableLiveData<Resource<AddToCartResponse>>()
    val addToCartLiveData: LiveData<Resource<AddToCartResponse>> get() = liveDataAddToCart



    private val liveLocation = MutableLiveData<Resource<LocationResponse>>()
    val locationLiveData: LiveData<Resource<LocationResponse>> get() = liveLocation

    private val liveDataServiceCategory = MutableLiveData<Resource<ServiceCategoryResponse>>()
    val  serviceCategory: LiveData<Resource<ServiceCategoryResponse>> get() = liveDataServiceCategory

    private val liveDataAllCategories= MutableLiveData<Resource<AllCategoriesResponse>>()
    val allCategoriesLiveData: LiveData<Resource<AllCategoriesResponse>> get() = liveDataAllCategories

    private val liveDataViewProfile = MutableLiveData<Resource<ViewProfileResponse>>()
    val viewProfileLiveData: LiveData<Resource<ViewProfileResponse>> get() = liveDataViewProfile

    fun getDashboard() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataHome.postValue(Resource.loading(null))
                coroutineScope {
                    val homeDeferred = async { repository.getDashboard() }
                    val serviceCategoryDeferred= async { repository.mainService() }
                    val allCategoriesDeferred= async { repository.getProductCategories() }
                    val profileApi= async { repository.viewProfile() }

                    val homeFromApi=homeDeferred.await()
                    val serviceFromApi=serviceCategoryDeferred.await()
                    val allCategoriesApi=allCategoriesDeferred.await()
                    val homeProfileApi=profileApi.await()

                    val responseProfileApi=homeProfileApi.body()
                    when (responseProfileApi!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataViewProfile.postValue(Resource.success(responseProfileApi))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataViewProfile.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataViewProfile.postValue(Resource.error(responseProfileApi.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataViewProfile.postValue(Resource.noInterNet("", null))
                        }
                    }

                    val responseHome= homeFromApi.body()
                        when (responseHome!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataHome.postValue(Resource.success(responseHome))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataHome.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataHome.postValue(Resource.error(responseHome.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataHome.postValue(Resource.noInterNet("", null))
                            }
                        }


                    val responseService = serviceFromApi.body()
                    when (responseService!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataServiceCategory.postValue(Resource.success(responseService))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataHome.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataServiceCategory.postValue(Resource.error(responseService.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataHome.postValue(Resource.noInterNet("", null))
                        }
                    }

                    val responseAllCategories = allCategoriesApi.body()
                    when (responseAllCategories!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataAllCategories.postValue(Resource.success(responseAllCategories))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataHome.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataAllCategories.postValue(Resource.error(responseAllCategories.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataHome.postValue(Resource.noInterNet("", null))
                        }
                    }




                }

            } catch (ex: Exception) {
                Log.e("12346", "getDashboard: $ex" )
                liveDataHome.postValue(Resource.noInterNet("", null))
            }
        }
    }
    fun setLocation(lat: String, lang: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        val request=LocationRequest("35647462167862564126542@@", Location(lat,lang))
        viewModelScope.launch {
            try {
                liveLocation.postValue(Resource.loading(null))
                repository.setLocation(request).let {
                    val response = it.body()

                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveLocation.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveLocation.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveLocation.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveLocation.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataAddToCart.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun addToCart(product: String, quantity: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataAddToCart.postValue(Resource.loading(null))
                repository.addToCart(product, quantity).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataAddToCart.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataAddToCart.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataAddToCart.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataAddToCart.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataAddToCart.postValue(Resource.noInterNet("", null))
            }
        }
    }
}
