package com.iroid.healthdomain.ui.home.mainActivity

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iroid.healthdomain.data.model_class.contacts_api.ContactResponse
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository, application: Application) : BaseViewModel(application) {

    val contactLiveData: LiveData<Resource<ContactResponse>> = MutableLiveData()

    fun getContacts() = viewModelScope.launch {
        contactLiveData as MutableLiveData
        contactLiveData.value = Resource.Loading
        contactLiveData.value = repository.getAllContacts()
    }
}