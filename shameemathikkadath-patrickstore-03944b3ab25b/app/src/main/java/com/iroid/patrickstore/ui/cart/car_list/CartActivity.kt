package com.iroid.patrickstore.ui.cart.car_list

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityCartBinding
import com.iroid.patrickstore.model.cart_listing.CartData
import com.iroid.patrickstore.model.cart_listing.CartItem
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.ui.address.AddressActivity
import com.iroid.patrickstore.ui.cart.CartViewModal
import com.iroid.patrickstore.ui.cart.adapter.CartAdapter
import com.iroid.patrickstore.ui.cart.order_summary.OrderSummaryActivity
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.utils.*
import com.iroid.patrickstore.utils.Constants.BUNDLE_AMOUNT
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_GRAND_TOTAL
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_ITEM_TOTAL
import com.iroid.patrickstore.utils.Constants.KEY_DELIVERY_CHARGE
import com.iroid.patrickstore.utils.Constants.KEY_DELIVERY_SUR_CHARGE
import com.iroid.patrickstore.utils.Constants.KEY_GRAND_TOTAL
import com.iroid.patrickstore.utils.Constants.KEY_ITEM_TOTAL
import com.iroid.patrickstore.utils.Constants.KEY_PACKING_CHARGE
import com.iroid.patrickstore.utils.Constants.KEY_SERVICE_CHARGE
import com.iroid.patrickstore.utils.Constants.KEY_TAX
import com.iroid.patrickstore.utils.Constants.KEY_TOTAL_CHARGE
import kotlinx.android.synthetic.main.activity_cart.view.*
import kotlin.math.roundToInt

class CartActivity : BaseActivity<CartViewModal, ActivityCartBinding>() {

    private var deliverySurgeCharge=""
    private var deliveryCharge=""
    private var packingCharge=""
    private var serviceCharge=""
    private var adCharge=""
    private lateinit var meMap:HashMap<String,String>

    override val layoutId: Int
        get() = R.layout.activity_cart
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    private var loaderStatus: Boolean = false

    override fun getViewBinding(): ActivityCartBinding {
        return ActivityCartBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setUpObserver()
        setOnClick()

        binding.layoutToolbar.tvToolbarTitle.text = getString(R.string.my_cart)
        binding.rvCart.addItemDecoration(
            VerticalItemDecoration(
                resources.getDimension(R.dimen.margin_small).toInt()
            )
        )

    }

    override fun onResume() {
        super.onResume()
        if (AppPreferences.addressId.isNullOrEmpty()) {
            MaterialAlertDialogBuilder(this)
                .setMessage(resources.getString(R.string.long_message))
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    startActivity(Intent(this, AddressActivity::class.java))
                }
                .setCancelable(false)
                .show()
        } else {
            viewModel.getCartList()
        }
    }
    private fun showInfoDialog() {
        val dialog = this.let { it1 -> Dialog(it1) }
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_info)


        //val yesBtn = dialog?.findViewById(R.id.RatetheFoodSubmitBtn) as MaterialButton
//        val closeBtn = dialog.findViewById(R.id.ivClose) as ImageView
        val tvDeliveryCharge = dialog.findViewById(R.id.tvDeliveryCharge) as TextView
        val tvDeliverySurCharge = dialog.findViewById(R.id.tvDeliverySurCharge) as TextView
        val tvServiceCharge = dialog.findViewById(R.id.tvServiceCharge) as TextView
        val tvPackingCharge = dialog.findViewById(R.id.tvPackingCharge) as TextView

        tvDeliveryCharge.text=CommonUtils.getCurrencyFormat(deliveryCharge.toString())
        tvDeliverySurCharge.text=CommonUtils.getCurrencyFormat(deliverySurgeCharge.toString())
        tvServiceCharge.text=CommonUtils.getCurrencyFormat(serviceCharge.toString())
        tvPackingCharge.text=CommonUtils.getCurrencyFormat(packingCharge.toString())
        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setOnClick() {
        binding.layoutCheckout.btnCheckout.setOnClickListener {
            val intent = Intent(this, OrderSummaryActivity::class.java)
            intent.putExtra(BUNDLE_AMOUNT, meMap)
            startActivity(intent)
        }

        binding.changeLocation.setOnClickListener {
            startActivity(Intent(this, AddressActivity::class.java))
        }

        binding.layoutCheckout.btnContinue.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.containerNoInternet.btnTryAgain.setOnClickListener {
            if (this.isConnected) {
                viewModel.getCartList()
            }
        }

        binding.containerNoData.btnContinueShopping.setOnClickListener {
            onBackPressed()
        }
        binding.tvDetails.setOnClickListener {
            showInfoDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpObserver() {

        viewModel.listCartLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    deliveryCharge= it.data!!.data.deliveryCharge.toString()
                    deliverySurgeCharge=it.data!!.data.deliverySurgeCharge.toString()
                    packingCharge=it.data!!.data.packingCharge.toString()
                    serviceCharge=it.data!!.data.serviceCharge.toString()
                    dismissProgress()
                    loaderStatus = false
                    binding.containerNoInternet.root.isVisible = false
                    if (it.data!!.data != null) {
                        if (it.data.data.cartItems.cartItems.isNullOrEmpty()) {
                            binding.containerData.isVisible = false
                            binding.containerNoData.isVisible = true
                        } else {
                            binding.containerData.isVisible = true
                            setCartItemsData(it.data.data.cartItems.cartItems)
                            setPrice(it.data.data)
                        }
                    } else {
                        binding.containerData.isVisible = false
                        binding.containerNoData.isVisible = true
                    }
                    binding.tvDeliveryLocation.text=AppPreferences.address
                    binding.tvPlace.text=AppPreferences.address_place
                    binding.tvPhone.text=AppPreferences.address_mobile
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
        }

        viewModel.removeSingleItemLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    loaderStatus = true
                    viewModel.getCartList()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
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
        }

        viewModel.addToWishListLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    loaderStatus = true
                    viewModel.getCartList()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    loaderStatus = true
                    viewModel.getCartList()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
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
        }

        viewModel.addToCartLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    loaderStatus = true
                    viewModel.getCartList()

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

    private fun setPrice(cartData: CartData) {
        val totalCharge=deliveryCharge.toDouble()+deliverySurgeCharge.toDouble()+
            serviceCharge.toDouble()+
            packingCharge.toDouble()

        meMap = HashMap<String, String>()
        meMap[KEY_DELIVERY_CHARGE] = deliveryCharge
        meMap[KEY_DELIVERY_SUR_CHARGE] = deliverySurgeCharge
        meMap[KEY_SERVICE_CHARGE] = serviceCharge
        meMap[KEY_PACKING_CHARGE] = packingCharge
        meMap[KEY_ITEM_TOTAL] =  cartData.itemTotal.toString()
        meMap[KEY_TAX] =  cartData.taxAmount.toString()
        meMap[KEY_GRAND_TOTAL] =  cartData.grandTotal.toString()
        meMap[KEY_TOTAL_CHARGE] = totalCharge.toString()

        binding.tvSubTotal.text =getString(
            R.string.Rupees_symbol,
            cartData.itemTotal.toString().toDouble().toPrice()
        )
        binding.tvAddCharge.text = getString(
            R.string.Rupees_symbol,
            cartData.deliverySurgeCharge.toString().toDouble().toPrice()
        )
        binding.tvServiceCharge.text =CommonUtils.getCurrencyFormat(serviceCharge.toString())
        binding.tvTax.text = getString(
            R.string.Rupees_symbol,
            cartData.taxAmount.toString().toDouble().toPrice()
        )
        binding.tvTotal.text = getString(
            R.string.Rupees_symbol,
            cartData.grandTotal.toString().toDouble().toPrice()
        )

    }

    private fun setCartItemsData(cartItems: List<CartItem>) {
        binding.rvCart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCart.adapter = CartAdapter(
            this,
            cartItems,
            { functionRemoveSingleItem(it) },
            { functionAddToWishList(it) },
            { id,qty->functionAddToCart(id,qty) })
    }

    private fun functionAddToWishList(it: String) {
        viewModel.addToWishList(it)
    }

    private fun functionRemoveSingleItem(it: String) {
        viewModel.removeSingleItemFromCart(it)
    }
    private fun functionAddToCart(productId:String,quantity:String) {
        viewModel.addToCart(productId, quantity.toString())
    }
}
