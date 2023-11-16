package com.ncomfortsagent.ui.main.sideMenu.feedback.viemodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncomfortsagent.R
import com.ncomfortsagent.data.ApiRepositoryProvider
import com.ncomfortsagent.model.feedback.AgentFeedbackResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class FeedbackViewModel(@SuppressLint("StaticFieldLeak") private var activity: Activity) :
    ViewModel() {

    private val feedbackLiveData = MutableLiveData<Resource<AgentFeedbackResponse>>()


    fun getAgentFeedbackResponse(): LiveData<Resource<AgentFeedbackResponse>> {
        return feedbackLiveData
    }

    fun feedback(feedback: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            feedbackLiveData.postValue(Resource.loading(null))
            try {
                repository.feedback(
                    feedback
                ).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        feedbackLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        feedbackLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        feedbackLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        feedbackLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                feedbackLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                feedbackLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                feedbackLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }

}