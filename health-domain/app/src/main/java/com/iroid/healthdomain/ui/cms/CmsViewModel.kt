package com.iroid.healthdomain.ui.cms

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iroid.healthdomain.data.model_class.cms.CmsResponse
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class CmsViewModel(private val repository: CmsRepository, application: Application) : BaseViewModel(application) {

     val cmsLiveData: LiveData<Resource<CmsResponse>> = MutableLiveData()

    fun getTermsConditions() = viewModelScope.launch {
        cmsLiveData as MutableLiveData
        cmsLiveData.value = Resource.Loading
        cmsLiveData.value = repository.getTerms()
    }

    fun getPrivacyPolicy() = viewModelScope.launch {
        cmsLiveData as MutableLiveData
        cmsLiveData.value = Resource.Loading
        cmsLiveData.value = repository.getPrivacyPolicy()
    }
}