package com.iroid.patrickstore.ui.productdetail

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_address.AddAddressResponse
import com.iroid.patrickstore.model.add_to_cart.AddToCartResponse
import com.iroid.patrickstore.model.add_to_wishlist.AddToWishListResponse
import com.iroid.patrickstore.model.signup.SignUpResponse
import com.iroid.patrickstore.model.single_product.SingleProductResponse
import com.iroid.patrickstore.utils.*
import kotlinx.coroutines.launch

class ProductDetailViewModal : ViewModel() {
    private val liveDataProduct = MutableLiveData<Resource<SingleProductResponse>>()

    val productLiveData: LiveData<Resource<SingleProductResponse>> get() = liveDataProduct

    private val liveDataAddToWishList = MutableLiveData<Resource<AddToWishListResponse>>()
    val addToWishListLiveData: LiveData<Resource<AddToWishListResponse>> get() = liveDataAddToWishList

    private val liveDataAddToCart = MutableLiveData<Resource<AddToCartResponse>>()
    val addToCartLiveData: LiveData<Resource<AddToCartResponse>> get() = liveDataAddToCart

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

    fun getProduct(productId:String){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataProduct.postValue(Resource.loading(null))
                repository.getSingleProduct(productId).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataProduct.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataProduct.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataProduct.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataProduct.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e("#5252552", ex.toString())
                liveDataProduct.postValue(Resource.noInterNet("", null))
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
                        Constants.REQUEST_OK -> {
                            liveDataAddToWishList.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataAddToWishList.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataAddToWishList.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataAddToWishList.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataAddToWishList.postValue(Resource.noInterNet("", null))
            }
        }
    }



}
