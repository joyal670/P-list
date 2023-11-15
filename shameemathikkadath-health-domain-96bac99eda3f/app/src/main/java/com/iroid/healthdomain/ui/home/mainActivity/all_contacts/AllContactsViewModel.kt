package com.iroid.healthdomain.ui.home.mainActivity.all_contacts

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iroid.healthdomain.data.dummyModel.HashedModel
import com.iroid.healthdomain.data.model_class.add_remove_fav.FavResponse
import com.iroid.healthdomain.data.model_class.create_challenge.CreateChallengeResponse
import com.iroid.healthdomain.data.model_class.invite.UserInviteResponse
import com.iroid.healthdomain.data.model_class.match_contact.GetContactMatchResponse
import com.iroid.healthdomain.data.model_class.user_challenge.UserChallengeResponse
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.ui.base.BaseViewModel
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class AllContactsViewModel(private val repository: HomeRepository, application: Application) :
    BaseViewModel(application) {


    val favResponse: LiveData<Resource<FavResponse>> = MutableLiveData()




    fun sentFavRequest(id: String,status:String) = viewModelScope.launch {
        favResponse as MutableLiveData
        favResponse.value = Resource.Loading
        favResponse.value = repository.addRemoveFav(id, status)
    }


    val matchedContacts: LiveData<Resource<GetContactMatchResponse>> = MutableLiveData()
    val userInvite: LiveData<Resource<UserInviteResponse>> = MutableLiveData()

    fun setHashedList(hashedModel: HashedModel) = viewModelScope.launch {

        Log.i("AllContactsFragment", "setHashedList:$hashedModel ")
        matchedContacts as MutableLiveData
        matchedContacts.value = Resource.Loading
        matchedContacts.value = repository.sendHashedList(hashedModel)
    }

    fun sentUserInvite(number: String) = viewModelScope.launch {
        userInvite as MutableLiveData
        userInvite.value = Resource.Loading
        userInvite.value = repository.sentInvite(number)
    }
}