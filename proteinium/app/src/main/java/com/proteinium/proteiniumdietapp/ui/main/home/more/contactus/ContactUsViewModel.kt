package com.proteinium.proteiniumdietapp.ui.main.home.more.contactus

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ContactUsViewModel:ViewModel() {

    private val contactUsResponseLiveData = MutableLiveData<Resource<CommonResponse>>()

    fun addCcontactUs(user_id:Int, name:String, phone:String, email:String, message:String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            contactUsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.contactUs(user_id, name, phone, email, message).let {
                    val response = it.body()
                    if (response?.status!!) {
                        contactUsResponseLiveData.postValue(Resource.success(response))
                    } else {
                        contactUsResponseLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                contactUsResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                contactUsResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                contactUsResponseLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }

    fun getUserInfoResponse(): LiveData<Resource<CommonResponse>> {
        return contactUsResponseLiveData
    }
}