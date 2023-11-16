package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.events.StudentEventsReponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentEventsViewModel : ViewModel() {
    /* variables */
    private val studentEventsLiveData = MutableLiveData<Resource<StudentEventsReponse>>()

    val studentEventsData: LiveData<Resource<StudentEventsReponse>>
        get() = studentEventsLiveData

    /* Student events */
    fun studentEvents(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentEventsLiveData.postValue(Resource.loading(null))
                repository.studentEvents(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentEventsLiveData.postValue(Resource.success(response))
                    } else {
                        studentEventsLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentEventsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentEventsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentEventsLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}