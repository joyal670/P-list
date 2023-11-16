package com.iroid.patrickstore.ui.my_account.my_orders.order_details

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityOrderDetailsBinding
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.ui.my_account.my_orders.order_details.adapter.OrdrerDetailAdapter
import com.iroid.patrickstore.utils.*
import kotlinx.android.synthetic.main.fragment_order_details.*
import me.kariot.invoicegenerator.data.*
import me.kariot.invoicegenerator.utils.InvoiceGenerator
import java.time.Instant
import java.util.*


class OrderDetailActivity : BaseActivity<OrderDetailViewModal, ActivityOrderDetailsBinding>() {
    private var deliverySurgeCharge = ""
    private var deliveryCharge = ""
    private var packingCharge = ""
    private var serviceCharge = ""
    private var type = false
    private lateinit var orderId: String
    private lateinit var dialog1: Dialog
    private lateinit var etReason: EditText
    private lateinit var etComments: EditText
    private lateinit var cvReplacement: CheckBox
    private var returnMode = "refund"
    private lateinit var productId: String
    private var deliveryStatus = false
    override val layoutId: Int
        get() = R.layout.activity_order_details
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.order_details)


    }

    override fun getViewBinding(): ActivityOrderDetailsBinding {
        return ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        setUpObserver()
//
        viewModel.getSingleOrder(AppPreferences.order_id!!)

        binding.tvDetails.setOnClickListener {
            showInfoDialog()
        }


    }

    @SuppressLint("NewApi")
    fun setUpObserver() {
        viewModel.returnLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Toaster.showToast(this, it.data!!.msg, Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                    dismissProgress()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                }
            }
        })
        viewModel.myOrderLiveData.observe(this, Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    binding.constraintMain.visibility = View.VISIBLE
                    dismissProgress()
                    val instant = Instant.parse(it.data!!.data.dateOfPurchase)
                    val d: Date = Date.from(instant)
                    binding.tvDate.text = "${DateFormat.format("yyyy-MM-dd hh:mm:ss a", d)}"
                    deliveryCharge = it.data!!.data.deliveryCharge.toString()
                    deliverySurgeCharge = it.data!!.data.deliverySurgeCharge.toString()
                    packingCharge = it.data!!.data.packingCharge.toString()
                    serviceCharge = it.data!!.data.serviceCharge.toString()
                    binding.textView31.text = it.data!!.data.shippingAddressId.name
                    val totalCharge = deliveryCharge.toDouble() + deliverySurgeCharge.toDouble() +
                        serviceCharge.toDouble() +
                        packingCharge.toDouble()

                    binding.tvSubTotal.text = getString(
                        R.string.Rupees_symbol,
                        it.data.data.itemTotal.toString().toDouble().toPrice()
                    )
                    binding.tvServiceCharge.text = getString(
                        R.string.Rupees_symbol,
                        it.data.data.serviceCharge.toString().toDouble().toPrice()
                    )
                    binding.tvTotal.text = getString(
                        R.string.Rupees_symbol,
                        it.data.data.grandTotal.toString().toDouble().toPrice()
                    )
                    binding.tvTax.text = getString(
                        R.string.Rupees_symbol,
                        it.data.data.taxAmount.toString().toDouble().toPrice()
                    )
                    val address = StringBuilder()
                    address.append("${it.data!!.data.shippingAddressId.address1} \n")
                    address.append(it.data!!.data.shippingAddressId.landMark)
                    binding.tvAddress.text = address.toString()
                    binding.tvContactNumber.text = it.data.data.shippingAddressId.contactNumber
                    setOrderStatusAdapter(it.data!!.data.orderStatus)
                    setUpOrderProduct(it.data!!.data.cartItems)
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (isConnected) {
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

    }

    private fun setOrderStatusAdapter(orderStatus: String) {
        when (orderStatus) {
            Constants.ORDER_STATUS_DELIVERY_COMPLETE -> {
                deliveryStatus = true
                ivNode1.setImageResource(R.drawable.delivery_progress_node)
                progressNode1.progress = 100
                ivNode3.setImageResource(R.drawable.delivery_progress_node)
                progressNode3.progress = 100
                ivNode4.setImageResource(R.drawable.delivery_progress_node)

            }
            Constants.ORDER_STATUS_GO_TO_PICK -> {
                deliveryStatus = false
                ivNode1.setImageResource(R.drawable.delivery_progress_node)
                progressNode1.progress = 100
                ivNode3.setImageResource(R.drawable.delivery_progress_node)
                progressNode3.progress = 0
                ivNode4.setImageResource(R.drawable.delivery_progress_ring)
            }
            Constants.ORDER_STATUS_PENDING -> {
                deliveryStatus = false
                ivNode1.setImageResource(R.drawable.delivery_progress_node)
                progressNode1.progress = 0
                ivNode3.setImageResource(R.drawable.delivery_progress_ring)
                progressNode3.progress = 0
                ivNode4.setImageResource(R.drawable.delivery_progress_ring)
            }
        }
    }

    private fun setUpOrderProduct(cartItems: List<com.iroid.patrickstore.model.order_detail.CartItem>) {
        rvOrderDetails.layoutManager = LinearLayoutManager(this)
        rvOrderDetails.adapter = OrdrerDetailAdapter(this, cartItems, {
            productId = it
            showReturnDialog()
        }, deliveryStatus)

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

    private fun showReturnDialog() {
        dialog1 = this.let { it1 -> Dialog(it1) }
        dialog1.setCancelable(true)
        dialog1.setContentView(R.layout.fragment_dialog_return)
        etReason = dialog1.findViewById(R.id.tvSelectDate) as EditText
        etComments = dialog1.findViewById(R.id.tvComments) as EditText
        cvReplacement = dialog1.findViewById(R.id.cvReplacement) as CheckBox
        val btnContinue = dialog1.findViewById(R.id.btnContinue) as Button
        cvReplacement.setOnCheckedChangeListener { buttonView, isChecked ->
            returnMode = if (isChecked) {
                "replacement"

            } else {
                "refund"
            }
        }
        btnContinue.setOnClickListener {
            callRetunApi()
        }

        dialog1.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog1.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog1.window?.attributes = layoutParams
        dialog1.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun callRetunApi() {
        dialog1.dismiss()
        viewModel.returnOrder(
            AppPreferences.order_id!!,
            productId,
            returnMode,
            etReason.text.toString(),
            "",
            etComments.text.toString(),
        )
    }

    override fun onBackPressed() {

        if (AppPreferences.isFromOrder) {
            AppPreferences.isFromOrder = false
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        } else {
            super.onBackPressed()
        }

    }

}
