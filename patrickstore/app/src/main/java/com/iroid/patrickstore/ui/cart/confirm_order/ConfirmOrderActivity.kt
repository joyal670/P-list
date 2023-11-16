package com.iroid.patrickstore.ui.cart.confirm_order

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.awesomedialog.*
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityConfirmOrderBinding
import com.iroid.patrickstore.dialogfragment.SuccessDialogFragment
import com.iroid.patrickstore.model.order_summary.CartItem
import com.iroid.patrickstore.model.order_summary.OrderSummaryData
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.preference.ConstantPreference.PAYMENT_COD
import com.iroid.patrickstore.ui.cart.confirm_order.adapter.CartItemsAdapter
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.ui.my_account.my_orders.order_details.OrderDetailActivity
import com.iroid.patrickstore.utils.CommonUtils
import com.iroid.patrickstore.utils.Constants.BUNDLE_CASH_BACK_IS_USED
import com.iroid.patrickstore.utils.Constants.BUNDLE_COUPON_CODE
import com.iroid.patrickstore.utils.Constants.BUNDLE_COUPON_IS_USED
import com.iroid.patrickstore.utils.Constants.BUNDLE_DATA
import com.iroid.patrickstore.utils.Constants.BUNDLE_GIFT_CODE
import com.iroid.patrickstore.utils.Constants.BUNDLE_GIFT_IS_USED
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_QUOTE_ID
import com.iroid.patrickstore.utils.Constants.BUNDLE_PAYMENT_METHOD
import com.iroid.patrickstore.utils.Constants.BUNDLE_REWARD
import com.iroid.patrickstore.utils.Constants.BUNDLE_TYPE
import com.iroid.patrickstore.utils.Constants.BUNDLE_TYPE_SERVICE
import com.iroid.patrickstore.utils.Status
import com.iroid.patrickstore.utils.Toaster
import com.iroid.patrickstore.utils.toPrice
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_confirm_order.*
import kotlinx.android.synthetic.main.fragment_cash_back.*
import org.json.JSONObject
import kotlin.math.roundToInt

class ConfirmOrderActivity : BaseActivity<ConfirmOrderViewModal, ActivityConfirmOrderBinding>(),
    View.OnClickListener, PaymentResultListener {
    private var paymentId = "#testpaymnet123456"
    private var isAnyDeliverySurge: Boolean = false
    private lateinit var deliverySurgeCharge: String
    private lateinit var deliveryCharge: String
    private lateinit var packingCharge: String
    private lateinit var serviceCharge: String
    private lateinit var taxAmount: String
    private lateinit var itemTotal: String
    private lateinit var tip: String
    private lateinit var paymentMethod: String
    private lateinit var grandTotal: String
    private var offerAmount = 0.00
    private  var offerId=""
    private lateinit var bundle: Bundle
    private lateinit var quoteId: String
    private lateinit var sellerServiceCharge: String
    private var cashbackAmount=0.00
    private var totalOfferAmount=0.00
    private var cashBackExist=false


    override val layoutId: Int
        get() = R.layout.activity_confirm_order
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.confirm_order)
        btnConfirmOrder.setOnClickListener(this)
        offerAmount = 0.00
        offerId = ""

        binding.tvDeliveryChargeDetails.setOnClickListener {
            showInfoDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menue_toolbar, menu)
        return true
    }

    private fun showSuccessDialog() {
        var isLargeLayout = false
        val fragmentManager = supportFragmentManager
        val newFragment = SuccessDialogFragment {
            AppPreferences.isFromOrder=true
            val intent = Intent(this, OrderDetailActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        if (isLargeLayout) {
            newFragment.show(fragmentManager, "dialog")
        } else {
            val transaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            btnConfirmOrder -> {
                if (paymentMethod == PAYMENT_COD) {
                    callOrderApi()
                } else {
                    startPayment()
                }


            }
        }
    }

    private fun callOrderApi() {
        if (bundle[BUNDLE_TYPE] == BUNDLE_TYPE_SERVICE) {
            callConfirmService()
        } else {

            callConfirmOrderApi()
        }
    }

    private fun callConfirmService() {
        viewModel.servicePlaceOrder(quoteId, paymentId, itemTotal, sellerServiceCharge, grandTotal)
    }

    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()
        co.setKeyID("rzp_live_VrwxrQHB1i7wWV");


        try {
            val name = AppPreferences.first_name + " " + AppPreferences.last_name
            val options = JSONObject()
            options.put("theme.color", "#FEC126")
            options.put("name", name)
            options.put("description", "Patrick Store")
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR")
            options.put("amount", grandTotal.toDouble() * 100)

            val prefill = JSONObject()
            prefill.put("email", AppPreferences.email)
            prefill.put("contact", AppPreferences.mobile)

            options.put("prefill", prefill)
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)
            co.open(activity, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun callConfirmOrderApi() {
        viewModel.confirmOrder(
            "COD", "", isAnyDeliverySurge,
            deliverySurgeCharge.toDouble(),
            deliveryCharge.toDouble(),
            packingCharge.toDouble(),
            serviceCharge.toDouble(),
            taxAmount.toDouble(),
            itemTotal.toDouble(),
            0.00,
            grandTotal.toDouble(),
            "",
            offerAmount.toDouble(),
            cashbackAmount,
            totalOfferAmount
        )
    }

    override fun getViewBinding(): ActivityConfirmOrderBinding {
        return ActivityConfirmOrderBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setUpObserver()

        bundle = intent.getBundleExtra(BUNDLE_DATA)!!
        if (bundle[BUNDLE_TYPE] == BUNDLE_TYPE_SERVICE) {
            quoteId = bundle[BUNDLE_KEY_QUOTE_ID].toString()
            paymentMethod = bundle[BUNDLE_PAYMENT_METHOD].toString()
            viewModel.confirmServiceOrder(
                bundle[BUNDLE_KEY_QUOTE_ID].toString(),
                bundle[BUNDLE_COUPON_IS_USED] as Boolean,
                bundle[BUNDLE_COUPON_CODE].toString(),
                bundle[BUNDLE_REWARD] as Boolean,
                bundle[BUNDLE_GIFT_IS_USED] as Boolean,
                bundle[BUNDLE_GIFT_CODE].toString(),
            )
        } else {
            paymentMethod = bundle[BUNDLE_PAYMENT_METHOD].toString()
            cashBackExist= bundle[BUNDLE_CASH_BACK_IS_USED] as Boolean
            viewModel.getOrderSummary(
                bundle[BUNDLE_COUPON_IS_USED] as Boolean,
                bundle[BUNDLE_COUPON_CODE].toString(),
                bundle[BUNDLE_REWARD] as Boolean,
                bundle[BUNDLE_GIFT_IS_USED] as Boolean,
                bundle[BUNDLE_GIFT_CODE].toString(),
                bundle[BUNDLE_CASH_BACK_IS_USED] as Boolean
            )
        }


    }

    private fun setUpObserver() {
        viewModel.listOrderLiveData.observe(this) { orderSummaryResponse ->
            when (orderSummaryResponse.status) {
                Status.SUCCESS -> {
                    constarintMain.visibility=View.VISIBLE
                    itemTotal= orderSummaryResponse.data?.data?.itemTotal.toString()
                    orderAdapter(orderSummaryResponse.data?.data?.cartItems?.cartItems)
                    setPrice(orderSummaryResponse.data?.data!!)
                    val adr= StringBuilder()
                    adr.append("Name:")
                    adr.append(orderSummaryResponse.data?.data.addressData.name)
                    adr.append("\n")
                    adr.append("Address:")
                    adr.append(orderSummaryResponse.data?.data.addressData.address1)
                    adr.append("\n")
                    adr.append("Location:")
                    adr.append(orderSummaryResponse.data?.data.addressData.landMark)
                    adr.append("\n")
                    adr.append("Contact Number:")
                    adr.append(orderSummaryResponse.data?.data.addressData.contactNumber)
                    binding.tvName.text = adr.toString()
                    binding.tvAddress.text = orderSummaryResponse.data?.data.addressData.name
                    binding.tvPlace.text = orderSummaryResponse.data?.data.addressData.name
                }
            }

        }
        viewModel.confirmLiveData.observe(this) { orderConfirmResponse ->
            when (orderConfirmResponse.status) {
                Status.SUCCESS -> {
                    AppPreferences.order_id = orderConfirmResponse.data!!.data.orderId
                    showSuccessDialog()
                }
                Status.LOADING -> {

                }
                Status.NO_INTERNET -> {

                }
                Status.ERROR -> {
                    Toaster.showToast(
                        this,
                        orderConfirmResponse.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        }

        viewModel.servicePlaceOrderLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    AwesomeDialog.build(this)
                        .title("ORDER PLACED")
                        .body(it.data!!.msg)
                        .icon(R.drawable.ic_congrts)
                        .onPositive("Go To Home") {
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                }
                Status.LOADING -> {

                }
                Status.NO_INTERNET -> {

                }
                Status.ERROR -> {
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }
        })

        viewModel.confirmServiceLiveData.observe(this) { confirmServiceLiveData ->
            when (confirmServiceLiveData.status) {
                Status.SUCCESS -> {
                    constarintMain.visibility=View.VISIBLE
                    grandTotal = confirmServiceLiveData.data!!.data.grandTotal.toString()
                    itemTotal = confirmServiceLiveData.data!!.data.itemTotal.toString()
                    sellerServiceCharge =
                        confirmServiceLiveData.data!!.data.sellerServiceCharge.toString()
                }
                Status.LOADING -> {

                }
                Status.NO_INTERNET -> {

                }
                Status.ERROR -> {
                    Toaster.showToast(
                        this,
                        confirmServiceLiveData.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
            }

        }


    }

    private fun setPrice(orderSummaryData: OrderSummaryData) {
        binding.tvAddCharge.text =
            orderSummaryData.deliverySurgeCharge.toString().toDouble().toPrice()




        isAnyDeliverySurge = orderSummaryData.isAnyDeliverySurge
        deliverySurgeCharge = orderSummaryData.deliverySurgeCharge.toString()
        deliveryCharge = orderSummaryData.deliveryCharge.toString()
        packingCharge = orderSummaryData.packingCharge.toString()
        serviceCharge = orderSummaryData.serviceCharge.toString()
        taxAmount = orderSummaryData.taxAmount.toString()
//        offerAmount=orderSummaryData.


        binding.tvDeliveryCharge.text =
            orderSummaryData.serviceCharge.toString()
        binding.tvAmountTax.text=orderSummaryData.taxAmount.toString()
        offerAmount=orderSummaryData.offerAmount
        totalOfferAmount=orderSummaryData.totalOfferAmount
        cashbackAmount=orderSummaryData.cashbackAmount
        offerId=orderSummaryData.offerId
        grandTotal = if(cashBackExist){
            cashbackAmount=orderSummaryData.cashbackAmount.toString().toDouble()
            binding.constraintCashback.visibility=View.VISIBLE
            binding.tvTotal.text= cashbackAmount.toString()

            (orderSummaryData.grandTotal.toString().toDouble()-orderSummaryData.cashbackAmount.toString().toDouble()).toString()
        }else{
            orderSummaryData.grandTotal.toString()
        }
        binding.tvAmountPayable.text = grandTotal.toString()


        binding.tvCalculatedPrice.text =orderSummaryData.itemTotal.toString()


    }

    private fun orderAdapter(cartItems: List<CartItem>?) {
        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = CartItemsAdapter(cartItems!!, this)
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

        tvDeliveryCharge.text = CommonUtils.getCurrencyFormat(deliveryCharge.toString())
        tvDeliverySurCharge.text = CommonUtils.getCurrencyFormat(deliverySurgeCharge.toString())
        tvServiceCharge.text = CommonUtils.getCurrencyFormat(serviceCharge.toString())
        tvPackingCharge.text = CommonUtils.getCurrencyFormat(packingCharge.toString())
        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onPaymentSuccess(p0: String?) {

    }

    override fun onPaymentError(p0: Int, p1: String?) {

    }
}
