package com.iroid.patrickstore.ui.whisllist

import android.content.Intent
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityWishListBinding
import com.iroid.patrickstore.model.wishlist_listing.Item
import com.iroid.patrickstore.ui.cart.car_list.CartActivity
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.ui.my_account.MyAccountActivity
import com.iroid.patrickstore.ui.whisllist.adapter.WishListAdapter
import com.iroid.patrickstore.utils.*
import kotlinx.android.synthetic.main.activity_cart.view.*
import kotlinx.android.synthetic.main.activity_confirm_order.*

class WishListActivity : BaseActivity<WishListViewModal, ActivityWishListBinding>() {

    private var loaderStatus: Boolean = false

    override val layoutId: Int
        get() = R.layout.activity_wish_list
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menue_cart_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_cart -> {
            startActivity(Intent(this, CartActivity::class.java))
            true
        }
        R.id.action_profile -> {
            startActivity(Intent(this, MyAccountActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun getViewBinding(): ActivityWishListBinding {
        return ActivityWishListBinding.inflate(layoutInflater)
    }

    override fun initViews() {

        setUpObserver()
        setOnClick()

        setTitle(R.string.wishlist)

        binding.rvWishList.addItemDecoration(
            VerticalItemDecoration(
                resources.getDimension(R.dimen.margin_small).toInt()
            )
        )

        viewModel.getWishList()
    }

    private fun functionAddToCart(id: String, qty: String) {
        viewModel.addToCart(id, qty)
    }


    private fun functionRemoveSingleItemFromWishList(it: String) {
        viewModel.removeWishListSingleItem(it)
    }

    private fun setOnClick() {
        binding.btnClear.setOnClickListener {
            viewModel.removeAllFromWishList()
        }

        binding.containerNoData.btnContinueShopping.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        binding.containerNoInternet.btnTryAgain.setOnClickListener {
            if (this.isConnected) {
                viewModel.getWishList()
            }
        }
    }

    private fun setUpObserver() {

        viewModel.wishlistLiveData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    loaderStatus = false
                    dismissProgress()
                    binding.containerNoInternet.root.isVisible = false
                    if (it.data!!.data.items.isNullOrEmpty()) {
                        binding.containerData.isVisible = false
                        binding.containerNoData.isVisible = true
                    } else {
                        binding.containerData.isVisible = true
                        setWishlistData(it.data.data.items)
                    }
                }
                Status.LOADING -> {
                    if (!loaderStatus) {
                        showProgress()
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    binding.containerData.isVisible = false
                    binding.containerNoData.isVisible = true
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
                        binding.containerData.isVisible = false
                        binding.containerNoInternet.root.isVisible = true
                    }
                }
            }
        })

        viewModel.addToCartLiveData.observe(this, {
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
                        binding.containerData.isVisible = false
                        binding.containerNoInternet.root.isVisible = true
                    }
                }
            }
        })

        viewModel.removeAllFromWishListLiveData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    loaderStatus = true
                    viewModel.getWishList()
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
                        binding.containerData.isVisible = false
                        binding.containerNoInternet.root.isVisible = true
                    }
                }
            }
        })

        viewModel.removeSingleItemFromWishListLiveData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    loaderStatus = true
                    viewModel.getWishList()
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
                        binding.containerData.isVisible = false
                        binding.containerNoInternet.root.isVisible = true
                    }
                }
            }
        })
    }

    private fun setWishlistData(product: List<Item>) {
        binding.rvWishList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvWishList.adapter =
            WishListAdapter(
                this,
                product,
                { functionRemoveSingleItemFromWishList(it) },
                { id, qty ->
                    functionAddToCart(
                        id,
                        qty
                    )
                })
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
