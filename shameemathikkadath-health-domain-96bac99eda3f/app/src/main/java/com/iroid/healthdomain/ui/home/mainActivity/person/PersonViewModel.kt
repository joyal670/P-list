package com.iroid.healthdomain.ui.home.mainActivity.person

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class PersonViewModel (private val repository: HomeRepository, application: Application) :
    BaseViewModel(application) {

    val createChallengeApiResponse: LiveData<Resource<CreateChallengeResponse>> = MutableLiveData()

    val favResponse: LiveData<Resource<FavResponse>> = MutableLiveData()

    val userChallengeResponse: LiveData<Resource<UserChallengeResponse>> = MutableLiveData()

    fun sentChallengeRequest(id: String) = viewModelScope.launch {
        try {
            createChallengeApiResponse as MutableLiveData
            createChallengeApiResponse.value = Resource.Loading
            createChallengeApiResponse.value = repository.createChallenge(id)
        }catch (ex:Exception){
            Log.e("123456", "sentChallengeRequest: $ex" )
        }

    }

    fun sentFavRequest(id: String,status:String) = viewModelScope.launch {
        favResponse as MutableLiveData
        favResponse.value = Resource.Loading
        favResponse.value = repository.addRemoveFav(id, status)
    }

    fun getUserChallenge(id: String,page:String) = viewModelScope.launch {
        userChallengeResponse as MutableLiveData
        userChallengeResponse.value = Resource.Loading
        userChallengeResponse.value = repository.getPastChallenges(id,page)
    }
}