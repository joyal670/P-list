package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.issued_books.StudentIssuedBookResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentIssuedBookViewModel : ViewModel() {
    /* variables */
    private val studentIssuedBookLiveData = MutableLiveData<Resource<StudentIssuedBookResponse>>()

    val studentIssuedBookData: LiveData<Resource<StudentIssuedBookResponse>>
        get() = studentIssuedBookLiveData

    /* Student issued book */
    fun studentIssuedBook(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentIssuedBookLiveData.postValue(Resource.loading(null))
                repository.studentIssuedBook(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentIssuedBookLiveData.postValue(Resource.success(response))
                    } else {
                        studentIssuedBookLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentIssuedBookLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentIssuedBookLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentIssuedBookLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}