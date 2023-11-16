package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.subjects.StudentSubjectsReponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentSubjectsViewModel : ViewModel() {
    /* variables */
    private val studentSubjectsLiveData = MutableLiveData<Resource<StudentSubjectsReponse>>()

    val studentSubjectData: LiveData<Resource<StudentSubjectsReponse>>
        get() = studentSubjectsLiveData

    /* Student subjects */
    fun studentSubjects(subject: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentSubjectsLiveData.postValue(Resource.loading(null))
                repository.studentSubjects(subject).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentSubjectsLiveData.postValue(Resource.success(response))
                    } else {
                        studentSubjectsLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentSubjectsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentSubjectsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentSubjectsLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}