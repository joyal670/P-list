package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.assignments.StudentAssignmentsResponse
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentAssignmentsViewModel : ViewModel() {
    /* variables */
    private val studentAssignmentLiveData = MutableLiveData<Resource<StudentAssignmentsResponse>>()

    val studentAssignmentData: LiveData<Resource<StudentAssignmentsResponse>>
        get() = studentAssignmentLiveData

    /* Student assignments */
    fun studentAssignment(subject: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentAssignmentLiveData.postValue(Resource.loading(null))
                repository.studentAssignments(subject).let {
                    val response = it.body()
                    if (it.isSuccessful) {
                        studentAssignmentLiveData.postValue(Resource.success(response))
                    } else {
                        studentAssignmentLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentAssignmentLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentAssignmentLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentAssignmentLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}