package com.iroid.patrickstore.ui.search

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ferfalk.simplesearchview.SimpleSearchView
import com.google.android.material.snackbar.Snackbar
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivitySearchBinding
import com.iroid.patrickstore.model.product.Product
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.home.adapter.ProductAdapter
import com.iroid.patrickstore.utils.*
import kotlinx.android.synthetic.main.activity_all_product.*


class SearchActivity : BaseActivity<SearchViewModal,ActivitySearchBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_search
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.search)

        binding.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextCleared(): Boolean {
               return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getSearchProduct(query,"","")
                return false
            }
        })
    }

    override fun initViews() {
        setUpObserver()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menue_search, menu)
        val item: MenuItem = menu.findItem(R.id.action_search)
        binding.searchView.setMenuItem(item)
        binding.searchView.showSearch()
        return true
    }
    private fun setUpObserver() {

        viewModel.searchProductLiveData.observe(this, Observer {
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
                    binding.noData.visibility=View.VISIBLE
                    binding.rvViewAll.visibility= View.GONE
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

    private fun setProductAdapter(productList: List<Product>?) {
        if(productList!!.isNotEmpty()){
            binding.rvViewAll.visibility= View.VISIBLE
            binding.noData.visibility= View.GONE
            val controller: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation)
            binding.rvViewAll.layoutAnimation = controller

            binding.rvViewAll.layoutManager =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            binding.rvViewAll.adapter = ProductAdapter(this, productList!!) { id, qty ->
                functionAddToCart(
                    id,
                    qty
                )
            }
        }else{
            binding.rvViewAll.visibility= View.GONE
            binding.noData.visibility= View.VISIBLE
        }
    }

    override fun getViewBinding(): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(layoutInflater)
    }
    private fun functionAddToCart(id: String, qty: String) {
        viewModel.addToCart(id, qty)
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
