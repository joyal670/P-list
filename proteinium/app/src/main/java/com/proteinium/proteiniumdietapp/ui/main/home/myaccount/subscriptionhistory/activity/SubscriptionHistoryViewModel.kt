package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.activity

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.subscriptions_history.SubscriptionsHistoryResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class SubscriptionHistoryViewModel:ViewModel() {

    private val subscriptionsHistoryResponseLiveData = MutableLiveData<Resource<SubscriptionsHistoryResponse>>()

    fun fetchSubscriptionsHistory(user_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            subscriptionsHistoryResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getSubscriptionHistory(user_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        subscriptionsHistoryResponseLiveData.postValue(Resource.success(response))
                    } else {
                        subscriptionsHistoryResponseLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            } catch (ex: NetworkErrorException) {
                subscriptionsHistoryResponseLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: UnknownHostException) {
                subscriptionsHistoryResponseLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: Exception) {
                Log.e("142345", "fetchSubscriptionsHistory: $ex" )
                subscriptionsHistoryResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun getSubscriptionsHistoryResponse(): LiveData<Resource<SubscriptionsHistoryResponse>> {
        return subscriptionsHistoryResponseLiveData
    }
}