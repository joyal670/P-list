package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.assigned_leave.StudentAssignedLeaveResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentAssignedLeaveViewModel : ViewModel() {
    /* variables */
    private val studentAssignedLeaveLiveData =
        MutableLiveData<Resource<StudentAssignedLeaveResponse>>()

    val studentAssignedLeaveData: LiveData<Resource<StudentAssignedLeaveResponse>>
        get() = studentAssignedLeaveLiveData

    /* Student assigned leave */
    fun studentAssignedLeave(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentAssignedLeaveLiveData.postValue(Resource.loading(null))
                repository.studentAssignedLeave(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentAssignedLeaveLiveData.postValue(Resource.success(response))
                    } else {
                        studentAssignedLeaveLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentAssignedLeaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentAssignedLeaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentAssignedLeaveLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}