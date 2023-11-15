package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.subscriptionhistorydetails

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.pojo.addon.AddOnResponse
import com.proteinium.proteiniumdietapp.pojo.extras_update.ExtrasUpdateResponse
import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.SubscriptionMealPlanDetailsResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class SubscriptionHistoryDetailsViewModel : ViewModel() {


    private val subscriptionsPreviewDetailsResponseLiveData =
        MutableLiveData<Resource<SubscriptionMealPlanDetailsResponse>>()
    private val subscriptionCancelLiveData = MutableLiveData<Resource<CommonResponse>>()
    private val addOnLiveData = MutableLiveData<Resource<AddOnResponse>>()
    private val extrasLiveData = MutableLiveData<Resource<ExtrasUpdateResponse>>()

    fun getExtras(meal_plan_subscription_id: Int, renewal_status: Boolean) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                repository.getExtras(meal_plan_subscription_id, renewal_status).let {
                    val response = it.body()
                    if(response!!.status){
                        extrasLiveData.postValue(Resource.success(response))
                    }else{
                        extrasLiveData.postValue(Resource.dataEmpty(response.message,null))
                    }

                }
            } catch (ex: Exception) {
                extrasLiveData.postValue(Resource.error("", null))
            }
        }
    }

    fun getAddOnData(meal_plan_subscription_id: Int, renewal_status: Boolean) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            addOnLiveData.postValue(Resource.loading(null))
            try {
                repository.getAddOnProteins(meal_plan_subscription_id, renewal_status).let {
                    val response = it.body()
                    if (response!!.status) {
                        addOnLiveData.postValue(Resource.success(response))
                    } else {
                        addOnLiveData.postValue(Resource.dataEmpty(response.message, null))
                    }

                }
            } catch (ex: Exception) {
                addOnLiveData.postValue(Resource.error("", null))
            }
        }
    }

    fun updateAddOn(
        user_id: Int,
        meal_plan_subscription_id: Int,
        carbs: String,
        carbs_price: String,
        proteins: String,
        proteins_price: String,
        payment_id: String,
        total: String,
        renewal_status: Boolean,
        extras: ArrayList<String>
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            subscriptionCancelLiveData.postValue(Resource.loading(null))
            try {
                repository.updateAddOn(
                    user_id,
                    meal_plan_subscription_id,
                    carbs,
                    carbs_price,
                    proteins,
                    proteins_price,
                    payment_id,
                    total,
                    renewal_status,
                    extras
                ).let {
                    val response = it.body()
                    if (response!!.status) {
                        subscriptionCancelLiveData.postValue(Resource.success(response))
                    } else {
                        subscriptionCancelLiveData.postValue(Resource.dataEmpty("", response))
                    }
                }
            } catch (ex: Exception) {
                subscriptionCancelLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )
            }
        }
    }

    fun cancelSybscription(user_id: Int, meal_plan_subscription_id: Int, isRenewal: Boolean) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            subscriptionCancelLiveData.postValue(Resource.loading(null))
            try {
                repository.cancelSubcription(user_id, meal_plan_subscription_id, isRenewal).let {
                    val response = it.body()
                    if (response!!.status) {
                        subscriptionCancelLiveData.postValue(Resource.success(response))
                    } else {
                        subscriptionCancelLiveData.postValue(Resource.dataEmpty("", response))
                    }
                }
            } catch (ex: Exception) {
                subscriptionCancelLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )
            }
        }
    }


    fun fetchSubscriptionsPreviewDetails(
        meal_plan_subscription_id: Int,
        renewal_status: Boolean
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            subscriptionsPreviewDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getSubscriptionPreviewDetails(meal_plan_subscription_id, renewal_status)
                    .let {
                        val response = it.body()
                        if (response?.status!!) {
                            subscriptionsPreviewDetailsResponseLiveData.postValue(
                                Resource.success(
                                    response
                                )
                            )
                        } else {
                            subscriptionsPreviewDetailsResponseLiveData.postValue(
                                Resource.error(
                                    response.message,
                                    response
                                )
                            )
                        }
                    }
            } catch (ex: NetworkErrorException) {
                subscriptionsPreviewDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: UnknownHostException) {
                subscriptionsPreviewDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: Exception) {
                Log.e("123456", "fetchSubscriptionsPreviewDetails: " + ex)
                subscriptionsPreviewDetailsResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun getSubscriptionsPreviewDetailsResponse(): LiveData<Resource<SubscriptionMealPlanDetailsResponse>> {
        return subscriptionsPreviewDetailsResponseLiveData
    }

    fun getCancel(): LiveData<Resource<CommonResponse>> {
        return subscriptionCancelLiveData
    }

    fun getAddOnData(): LiveData<Resource<AddOnResponse>> {
        return addOnLiveData
    }

    fun getExtrasResponse(): LiveData<Resource<ExtrasUpdateResponse>> {
        return extrasLiveData
    }
}
