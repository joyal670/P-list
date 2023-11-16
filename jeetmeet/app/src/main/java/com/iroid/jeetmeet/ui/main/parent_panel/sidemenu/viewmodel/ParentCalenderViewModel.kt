package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.calender.ParentCalenderResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentCalenderViewModel : ViewModel() {
    /* variables */
    private val parentCalenderLiveData = MutableLiveData<Resource<ParentCalenderResponse>>()

    val parentCalenderData: LiveData<Resource<ParentCalenderResponse>>
        get() = parentCalenderLiveData

    /* Parent calender */
    fun parentCalender(student_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentCalenderLiveData.postValue(Resource.loading(null))
                repository.parentCalender(student_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentCalenderLiveData.postValue(Resource.success(response))
                    } else {
                        parentCalenderLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentCalenderLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentCalenderLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentCalenderLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}