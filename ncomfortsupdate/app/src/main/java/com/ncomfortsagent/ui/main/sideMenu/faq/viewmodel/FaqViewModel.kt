package com.ncomfortsagent.ui.main.sideMenu.faq.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncomfortsagent.R
import com.ncomfortsagent.data.ApiRepositoryProvider
import com.ncomfortsagent.model.faq.AgentFaqResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class FaqViewModel(@SuppressLint("StaticFieldLeak") private var activity: Activity) :
    ViewModel() {

    private val faqLiveData = MutableLiveData<Resource<AgentFaqResponse>>()

    fun getAgentFaqResponse(): LiveData<Resource<AgentFaqResponse>> {
        return faqLiveData
    }

    fun faq(lang: String, page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            faqLiveData.postValue(Resource.loading(null))
            try {
                repository.faq(lang, page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        faqLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        faqLiveData.postValue(Resource.error("Bad request", response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        faqLiveData.postValue(Resource.error("Unauthorized", response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        faqLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                faqLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                faqLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                faqLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }

}