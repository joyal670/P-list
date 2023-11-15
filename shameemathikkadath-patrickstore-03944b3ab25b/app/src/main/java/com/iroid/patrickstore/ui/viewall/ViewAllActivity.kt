package com.iroid.patrickstore.ui.viewall

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityAllProductBinding
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.model.seller.SingleSeller
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.home.adapter.ProductAdapter
import com.iroid.patrickstore.ui.home.adapter.ShopAdapter
import com.iroid.patrickstore.ui.home.adapter.ShopAdapter1
import com.iroid.patrickstore.ui.search.SearchViewModal
import com.iroid.patrickstore.ui.search.adapter.SearchAdapter
import com.iroid.patrickstore.utils.*
import kotlinx.android.synthetic.main.activity_all_product.*


class ViewAllActivity : BaseActivity<ViewAllViewModal,ActivityAllProductBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_all_product
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun initViews() {
        setUpObserver()
        intent.let {
            if(it.getBooleanExtra("type",false)){
                viewModel.getCategorySeller(it.getStringExtra("id")!!,true,"")
            }else{
                viewModel.getCategoryProduct(it.getStringExtra("id")!!,false)
            }

            supportActionBar?.title = it.getStringExtra("category_name")!!
        }
    }
    private fun setUpObserver() {
        viewModel.categorySellerLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    setSellerAdapter(it.data?.data?.items)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {

                    }
                }
            }
        })
        viewModel.categoryProductLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    setProductAdapter(it.data?.data?.items)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {

                    }
                }
            }
        })
        viewModel.addToCartLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    showNavigateToCart()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }
    }
    private fun setSellerAdapter(sellerList: List<SingleSeller>?) {
        if(sellerList!!.isNotEmpty()){
            binding.noData.visibility = View.GONE
            binding.rvViewAll.visibility = View.VISIBLE
            val controller: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)
            binding.rvViewAll.layoutAnimation = controller
            binding.rvViewAll.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvViewAll.adapter = ShopAdapter1(this, sellerList!!)
        }else{
            binding.noData.visibility = View.VISIBLE
            binding.rvViewAll.visibility=View.GONE
        }

    }
    private fun setProductAdapter(productList: List<Product>?) {
        if (productList!!.isNotEmpty()) {
            binding.noData.visibility = View.GONE
            binding.rvViewAll.visibility = View.VISIBLE
            val controller: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)
            binding.rvViewAll.layoutAnimation = controller

            binding.rvViewAll.layoutManager =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            binding.rvViewAll.adapter = ProductAdapter(this, productList) { id, qty ->
                functionAddToCart(
                    id,
                    qty
                )
            }
        } else {
            binding.noData.visibility = View.VISIBLE
            binding.rvViewAll.visibility = View.GONE
        }


    }
    private fun functionAddToCart(id: String, qty: String) {
        viewModel.addToCart(id, qty)
    }

    override fun getViewBinding(): ActivityAllProductBinding {
        return ActivityAllProductBinding.inflate(layoutInflater)
    }


    private fun showNavigateToCart() {
        val snackBar = Snackbar.make(binding.coordMain, "Item added to cart", Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        snackBar.setTextColor(Color.WHITE)
        snackBar.setActionTextColor(Color.WHITE)
        snackBar.setAction("GO TO CART") {
            this.startActivity(Intent(this, CartActivity::class.java))
        }.show()
    }
}
