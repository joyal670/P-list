package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.attaendance.ParentAttendanceResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentAttendanceViewModel : ViewModel() {
    /* variables */
    private val parentAttendanceLiveData = MutableLiveData<Resource<ParentAttendanceResponse>>()

    val parentAttendanceData: LiveData<Resource<ParentAttendanceResponse>>
        get() = parentAttendanceLiveData

    /* Parent attendance */
    fun parentAttendance(student_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentAttendanceLiveData.postValue(Resource.loading(null))
                repository.parentAttendance(student_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentAttendanceLiveData.postValue(Resource.success(response))
                    } else {
                        parentAttendanceLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentAttendanceLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentAttendanceLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentAttendanceLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}