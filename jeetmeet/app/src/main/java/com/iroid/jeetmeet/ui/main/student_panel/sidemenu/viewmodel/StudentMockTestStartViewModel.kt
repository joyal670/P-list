package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.mock_test_start.StudentMockTestStartResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentMockTestStartViewModel : ViewModel() {
    /* variables */
    private val studentMockTestStartLiveData =
        MutableLiveData<Resource<StudentMockTestStartResponse>>()

    val studentMockTestStartData: LiveData<Resource<StudentMockTestStartResponse>>
        get() = studentMockTestStartLiveData


    /* Student mock test list */
    fun studentMockTestList(test_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentMockTestStartLiveData.postValue(Resource.loading(null))
                repository.studentMockTestStart(test_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentMockTestStartLiveData.postValue(Resource.success(response))
                    } else {
                        studentMockTestStartLiveData.postValue(
                            Resource.error(it.message(), response)
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentMockTestStartLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentMockTestStartLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentMockTestStartLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}