package com.iroid.patrickstore.ui.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_to_cart.AddToCartResponse
import com.iroid.patrickstore.model.add_to_wishlist.AddToWishListResponse
import com.iroid.patrickstore.model.cart_listing.CartListingResponse
import com.iroid.patrickstore.model.remove_single_item_from_cart.RemoveSingleItemFromCartResponse
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Constants.REQUEST_BAD_REQUEST
import com.iroid.patrickstore.utils.Constants.REQUEST_CREATED
import com.iroid.patrickstore.utils.Constants.REQUEST_OK
import com.iroid.patrickstore.utils.Constants.REQUEST_UNAUTHORIZED
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

class CartViewModal : ViewModel() {

    private val liveDataCartList = MutableLiveData<Resource<CartListingResponse>>()
    val listCartLiveData: LiveData<Resource<CartListingResponse>> get() = liveDataCartList

    private val liveDataRemoveSingleItem =
        MutableLiveData<Resource<RemoveSingleItemFromCartResponse>>()
    val removeSingleItemLiveData: LiveData<Resource<RemoveSingleItemFromCartResponse>> get() = liveDataRemoveSingleItem

    private val liveDataAddToWishList = MutableLiveData<Resource<AddToWishListResponse>>()
    val addToWishListLiveData: LiveData<Resource<AddToWishListResponse>> get() = liveDataAddToWishList

    private val liveDataAddToCart = MutableLiveData<Resource<AddToCartResponse>>()
    val addToCartLiveData: LiveData<Resource<AddToCartResponse>> get() = liveDataAddToCart

    fun getCartList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataCartList.postValue(Resource.loading(null))
                repository.getCartList(AppPreferences.addressId!!).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        REQUEST_OK -> {
                            liveDataCartList.postValue(Resource.success(response))
                        }
                        REQUEST_CREATED -> {
                            liveDataCartList.postValue(Resource.noInterNet("", null))
                        }
                        REQUEST_BAD_REQUEST -> {
                            liveDataCartList.postValue(Resource.error(response.msg, null))
                        }
                        REQUEST_UNAUTHORIZED -> {
                            liveDataCartList.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                Timber.d(ex)
                Log.e("123456", ex.toString())
                liveDataCartList.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun removeSingleItemFromCart(id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataRemoveSingleItem.postValue(Resource.loading(null))
                repository.removeSingleItemFromCart(id).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        REQUEST_OK -> {
                            liveDataRemoveSingleItem.postValue(Resource.success(response))
                        }
                        REQUEST_CREATED -> {
                            liveDataRemoveSingleItem.postValue(Resource.noInterNet("", null))
                        }
                        REQUEST_BAD_REQUEST -> {
                            liveDataRemoveSingleItem.postValue(Resource.error(response.msg, null))
                        }
                        REQUEST_UNAUTHORIZED -> {
                            liveDataRemoveSingleItem.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataRemoveSingleItem.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun addToWishList(id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataAddToWishList.postValue(Resource.loading(null))
                repository.addToWishList(id).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        REQUEST_OK -> {
                            liveDataAddToWishList.postValue(Resource.success(response))
                        }
                        REQUEST_CREATED -> {
                            liveDataAddToWishList.postValue(Resource.noInterNet("", null))
                        }
                        REQUEST_BAD_REQUEST -> {
                            liveDataAddToWishList.postValue(Resource.error(response.msg, null))
                        }
                        REQUEST_UNAUTHORIZED -> {
                            liveDataAddToWishList.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataAddToWishList.postValue(Resource.noInterNet("", null))
            }
        }
    }


    fun addToCart(product: String, quantity: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataAddToCart.postValue(Resource.loading(null))
                repository.addToCart(product, quantity).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataAddToCart.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataAddToCart.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataAddToCart.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataAddToCart.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataAddToCart.postValue(Resource.noInterNet("", null))
            }
        }
    }
}