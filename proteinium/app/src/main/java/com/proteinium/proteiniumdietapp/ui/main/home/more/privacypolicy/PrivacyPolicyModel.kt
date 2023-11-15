package com.proteinium.proteiniumdietapp.ui.main.home.more.privacypolicy

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.about.AboutResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class PrivacyPolicyModel() : ViewModel() {
    private val privacyResponseLiveData = MutableLiveData<Resource<AboutResponse>>()
    fun fetchAbout() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            privacyResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getPrivacyPolicy().let {
                    val response = it.body()
                    if (response?.status!!) {
                        privacyResponseLiveData.postValue(Resource.success(response))
                    } else {
                        privacyResponseLiveData.postValue(Resource.dataEmpty(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                privacyResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                privacyResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                privacyResponseLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }


    fun getPrivacyResponse(): LiveData<Resource<AboutResponse>> {
        return privacyResponseLiveData
    }
}