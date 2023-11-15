package com.proteinium.proteiniumdietapp.ui.main.home.home.home_fragment

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.home.HomeResponse
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class HomeViewModel() : ViewModel() {

    private val homeResponseLiveData = MutableLiveData<Resource<HomeResponse>>()

    fun fetchHomeDetails() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            homeResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getHomeDetails("en",AppPreferences.user_id!!).let {
                    val response = it.body()
                    if (response?.status!!) {
                        homeResponseLiveData.postValue(Resource.success(response))
                    } else {
                        homeResponseLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                homeResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                homeResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                Log.e("122", "fetchHomeDetails:$ex ")
                homeResponseLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }

    fun getHomeResponse(): LiveData<Resource<HomeResponse>> {
        return homeResponseLiveData
    }
}