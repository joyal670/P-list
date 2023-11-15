package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.timetable.StudentTimeTableResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentTimeTableViewModel : ViewModel() {
    /* variables */
    private val studentTimeTableLiveData = MutableLiveData<Resource<StudentTimeTableResponse>>()

    val studentTimeTableData: LiveData<Resource<StudentTimeTableResponse>>
        get() = studentTimeTableLiveData

    /* Student time table */
    fun studentTimeTable() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentTimeTableLiveData.postValue(Resource.loading(null))
                repository.studentTimeTable().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentTimeTableLiveData.postValue(Resource.success(response))
                    } else {
                        studentTimeTableLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentTimeTableLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentTimeTableLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentTimeTableLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}