package com.proteinium.proteiniumdietapp.ui.main.home.more.termsAndConditions

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

class TermsAndConditionsViewModel() : ViewModel() {
    private val termsAndConditionsResponseLiveData = MutableLiveData<Resource<AboutResponse>>()
    fun fetchAbout() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            termsAndConditionsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getTermsConditions().let {
                    val response = it.body()
                    if (response?.status!!) {
                        termsAndConditionsResponseLiveData.postValue(Resource.success(response))
                    } else {
                        termsAndConditionsResponseLiveData.postValue(Resource.dataEmpty(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                termsAndConditionsResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                termsAndConditionsResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                termsAndConditionsResponseLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }


    fun getAboutResponse(): LiveData<Resource<AboutResponse>> {
        return termsAndConditionsResponseLiveData
    }
}