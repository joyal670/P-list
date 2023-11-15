package com.proteinium.proteiniumdietapp.ui.main.home.more.aboutus

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

class AboutViewModel() : ViewModel() {
    private val aboutResponseLiveData = MutableLiveData<Resource<AboutResponse>>()
    fun fetchAbout() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            aboutResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getAbout().let {
                    val response = it.body()
                    if (response?.status!!) {
                        aboutResponseLiveData.postValue(Resource.success(response))
                    } else {
                        aboutResponseLiveData.postValue(Resource.dataEmpty(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                aboutResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                aboutResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                aboutResponseLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }


    fun getAboutResponse(): LiveData<Resource<AboutResponse>> {
        return aboutResponseLiveData
    }
}