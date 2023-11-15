package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.staff_directory.StudentStaffDirectoryResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentStaffDirectoryViewModel : ViewModel() {
    /* variables */
    private val studentStaffDirectoryLiveData =
        MutableLiveData<Resource<StudentStaffDirectoryResponse>>()

    val studentStaffDirectoryData: LiveData<Resource<StudentStaffDirectoryResponse>>
        get() = studentStaffDirectoryLiveData

    /* Student staff directory */
    fun studentStaffDirectory() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentStaffDirectoryLiveData.postValue(Resource.loading(null))
                repository.studentStaffDirectory().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentStaffDirectoryLiveData.postValue(Resource.success(response))
                    } else {
                        studentStaffDirectoryLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentStaffDirectoryLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentStaffDirectoryLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentStaffDirectoryLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }
}