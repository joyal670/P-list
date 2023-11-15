package com.iroid.patrickstore.ui.whisllist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_to_cart.AddToCartResponse
import com.iroid.patrickstore.model.remove_wish_list_all_item.RemoveWishlistAllItemsResponse
import com.iroid.patrickstore.model.remove_wishlist_single_item.RemoveSingleItemFromWishlistResponse
import com.iroid.patrickstore.model.wishlist_listing.WishListResponse
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.launch

class WishListViewModal : ViewModel() {

    private val liveDataWishlist = MutableLiveData<Resource<WishListResponse>>()
    val wishlistLiveData: LiveData<Resource<WishListResponse>> get() = liveDataWishlist

    private val liveDataRemoveSingleItemFromWishList =
        MutableLiveData<Resource<RemoveSingleItemFromWishlistResponse>>()
    val removeSingleItemFromWishListLiveData: LiveData<Resource<RemoveSingleItemFromWishlistResponse>> get() = liveDataRemoveSingleItemFromWishList

    private val liveDataAddToCart = MutableLiveData<Resource<AddToCartResponse>>()
    val addToCartLiveData: LiveData<Resource<AddToCartResponse>> get() = liveDataAddToCart

    private val liveDataRemoveAllFromWishList =
        MutableLiveData<Resource<RemoveWishlistAllItemsResponse>>()
    val removeAllFromWishListLiveData: LiveData<Resource<RemoveWishlistAllItemsResponse>> get() = liveDataRemoveAllFromWishList

    fun getWishList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataWishlist.postValue(Resource.loading(null))
                repository.getWishlist().let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataWishlist.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataWishlist.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataWishlist.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataWishlist.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataWishlist.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun removeWishListSingleItem(id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataRemoveSingleItemFromWishList.postValue(Resource.loading(null))
                repository.removeWishListSingleItem(id).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataRemoveSingleItemFromWishList.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataRemoveSingleItemFromWishList.postValue(
                                Resource.noInterNet(
                                    "",
                                    null
                                )
                            )
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataRemoveSingleItemFromWishList.postValue(
                                Resource.error(
                                    response.msg,
                                    null
                                )
                            )
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataRemoveSingleItemFromWishList.postValue(
                                Resource.noInterNet(
                                    "",
                                    null
                                )
                            )
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataRemoveSingleItemFromWishList.postValue(Resource.noInterNet("", null))
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

    fun removeAllFromWishList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataRemoveAllFromWishList.postValue(Resource.loading(null))
                repository.removeWishListAllItem().let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataRemoveAllFromWishList.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataRemoveAllFromWishList.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataRemoveAllFromWishList.postValue(
                                Resource.error(
                                    response.msg,
                                    null
                                )
                            )
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataRemoveAllFromWishList.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataRemoveAllFromWishList.postValue(Resource.noInterNet("", null))
            }
        }
    }
}