package com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.pojo.get_addresses.UserAddressesResponse
import com.proteinium.proteiniumdietapp.pojo.promocode.PrmocodeResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class CheckoutViewModel : ViewModel() {

    private val userAddressesResponseMutableLiveData =
        MutableLiveData<Resource<UserAddressesResponse>>()
    private val commonResponseMutableLiveData = MutableLiveData<Resource<CommonResponse>>()
    private val defaultAddressResponseMutableLiveData = MutableLiveData<Resource<CommonResponse>>()
    private val finalSubscriptionLiveData = MutableLiveData<Resource<CommonResponse>>()
    private val promodCodeLiveData = MutableLiveData<Resource<PrmocodeResponse>>()


    fun promoCode(user_id: Int, total: String, code: String, meals_plan_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            userAddressesResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.checkPromoCode(user_id, total, code, meals_plan_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        promodCodeLiveData.postValue(Resource.success(response))
                    } else {


                        promodCodeLiveData.postValue(Resource.error("", response))
                    }

                }

            } catch (ex: Exception) {

            }
        }

    }

    fun getAddresses(user_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            userAddressesResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.getUserAddresses(user_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        userAddressesResponseMutableLiveData.postValue(Resource.success(response))
                    } else {
                        userAddressesResponseMutableLiveData.postValue(
                            Resource.error(
                                response.message,
                                response
                            )
                        )
                    }
                }
            } catch (ex: NetworkErrorException) {
                userAddressesResponseMutableLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: UnknownHostException) {
                userAddressesResponseMutableLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: Exception) {
                Log.e("123", "getAddresses: $ex")
                userAddressesResponseMutableLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun fetchFinalSubscription(user_id: Int, address_id: Int, meal_plan_id: Int,payment_method:String,promoCode:String,payment_reference:String,unique_key:String,renewal:Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            userAddressesResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.addFinalSubscription(
                    user_id,
                    meal_plan_id,
                    1,
                    address_id,
                    payment_method,promoCode,payment_reference,unique_key,renewal
                ).let {
                    val response = it.body()
                    finalSubscriptionLiveData.postValue(Resource.success(response))
                }
            } catch (ex: Exception) {
                userAddressesResponseMutableLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }
        }

    }

    fun getAddressesResponse(): LiveData<Resource<UserAddressesResponse>> {
        return userAddressesResponseMutableLiveData
    }

    fun finalSubscription(): LiveData<Resource<CommonResponse>> {
        return finalSubscriptionLiveData
    }

    fun deleteAddress(addres_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            commonResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.deleteAddress(addres_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        commonResponseMutableLiveData.postValue(Resource.success(response))
                    } else {
                        commonResponseMutableLiveData.postValue(
                            Resource.error(
                                response.message,
                                response
                            )
                        )
                    }
                }
            } catch (ex: NetworkErrorException) {
                commonResponseMutableLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )
            } catch (ex: UnknownHostException) {
                commonResponseMutableLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )
            } catch (ex: Exception) {
                commonResponseMutableLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun deleteAddressResponse(): LiveData<Resource<CommonResponse>> {
        return commonResponseMutableLiveData
    }

    fun setDefaultAddress(user_id: Int, addres_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            defaultAddressResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.setDefaultAddress(user_id, addres_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        defaultAddressResponseMutableLiveData.postValue(Resource.success(response))
                    } else {
                        defaultAddressResponseMutableLiveData.postValue(
                            Resource.error(
                                response.message,
                                response
                            )
                        )
                    }
                }
            } catch (ex: NetworkErrorException) {
                defaultAddressResponseMutableLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: UnknownHostException) {
                defaultAddressResponseMutableLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: Exception) {
                defaultAddressResponseMutableLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun getDefaultAddressResponse(): LiveData<Resource<CommonResponse>> {
        return defaultAddressResponseMutableLiveData
    }
    fun getPromoCode():LiveData<Resource<PrmocodeResponse>>{
        return promodCodeLiveData
    }
}