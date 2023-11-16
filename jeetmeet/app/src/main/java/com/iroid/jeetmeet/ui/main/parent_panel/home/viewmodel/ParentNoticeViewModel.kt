package com.iroid.jeetmeet.ui.main.parent_panel.home.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.notice_view.ParentNoticeViewResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentNoticeViewModel : ViewModel() {
    /* variables */
    private val parentNoticeLiveData = MutableLiveData<Resource<ParentNoticeViewResponse>>()

    val parentNoticeData: LiveData<Resource<ParentNoticeViewResponse>>
        get() = parentNoticeLiveData

    /* Parent notice */
    fun parentNotice(notice_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentNoticeLiveData.postValue(Resource.loading(null))
                repository.parentNoticeView(notice_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentNoticeLiveData.postValue(Resource.success(response))
                    } else {
                        parentNoticeLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentNoticeLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentNoticeLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentNoticeLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}