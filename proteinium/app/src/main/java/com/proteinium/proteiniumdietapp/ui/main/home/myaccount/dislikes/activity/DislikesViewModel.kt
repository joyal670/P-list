package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.dislikes.activity

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.pojo.dislike_tags.TagsResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class DislikesViewModel : ViewModel() {

    private val tagsResponseLiveData = MutableLiveData<Resource<TagsResponse>>()
    private val setTagResponseLiveData = MutableLiveData<Resource<CommonResponse>>()

    fun setTag(user_id: Int, tagsId: ArrayList<Int>) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            tagsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.setDislike(user_id, tagsId).let {
                    val response = it.body()
                    if (response?.status!!) {
                        setTagResponseLiveData.postValue(Resource.success(response))
                    } else {
                        setTagResponseLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }  catch (ex: Exception) {
                Log.e("123", "setTag: $ex" )
                tagsResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun fetchTags(user_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            tagsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getTags(user_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        tagsResponseLiveData.postValue(Resource.success(response))
                    } else {
                        tagsResponseLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            } catch (ex: NetworkErrorException) {
                tagsResponseLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: UnknownHostException) {
                tagsResponseLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: Exception) {
                tagsResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun getTagsResponse(): LiveData<Resource<TagsResponse>> {
        return tagsResponseLiveData
    }
    fun getDisLikeResponse():LiveData<Resource<CommonResponse>>{
        return setTagResponseLiveData
    }
}