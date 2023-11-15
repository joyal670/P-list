package com.iroid.patrickstore.ui.cart.order_summary

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityOrderSummaryBinding
import com.iroid.patrickstore.dialogfragment.CouponDialogFragment
import com.iroid.patrickstore.model.coupon.ItemCoupon
import com.iroid.patrickstore.preference.ConstantPreference.PAYMENT_COD
import com.iroid.patrickstore.preference.ConstantPreference.PAYMENT_ONLINE
import com.iroid.patrickstore.ui.address.AddressActivity
import com.iroid.patrickstore.ui.cart.confirm_order.ConfirmOrderActivity
import com.iroid.patrickstore.ui.cart.confirm_order.ConfirmOrderViewModal
import com.iroid.patrickstore.utils.*
import com.iroid.patrickstore.utils.CommonUtils.Companion.getCurrencyFormat
import com.iroid.patrickstore.utils.Constants.BUNDLE_AMOUNT
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_CODE
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_COUPON
import com.iroid.patrickstore.utils.Constants.BUNDLE_KEY_QUOTE_ID
import com.iroid.patrickstore.utils.Constants.BUNDLE_TYPE
import com.iroid.patrickstore.utils.Constants.GRAND_TOTAL
import com.iroid.patrickstore.utils.Constants.ITEM_TOTAL
import com.iroid.patrickstore.utils.Constants.KEY_DELIVERY_CHARGE
import com.iroid.patrickstore.utils.Constants.KEY_DELIVERY_SUR_CHARGE
import com.iroid.patrickstore.utils.Constants.KEY_GRAND_TOTAL
import com.iroid.patrickstore.utils.Constants.KEY_ITEM_TOTAL
import com.iroid.patrickstore.utils.Constants.KEY_PACKING_CHARGE
import com.iroid.patrickstore.utils.Constants.KEY_SERVICE_CHARGE
import com.iroid.patrickstore.utils.Constants.OFFER_CODE
import com.iroid.patrickstore.utils.Constants.REQUEST_KEY_CODE
import com.iroid.patrickstore.utils.Constants.REQUEST_KEY_COUPON
import kotlinx.android.synthetic.main.activity_order_summary.*


class OrderSummaryActivity : BaseActivity<ConfirmOrderViewModal, ActivityOrderSummaryBinding>(),
    View.OnClickListener {
    private var couponList: List<ItemCoupon> = ArrayList<ItemCoupon>()
    private lateinit var itemTotal: String
    private lateinit var grandTotal: String
    private var quoteId = ""
    private var isCouponCodeUsed = false
    private var offerCode = ""
    private var isRewardUsed = false
    private var isGiftCardUsed = false
    private var giftCardCode = ""
    private var isCashBackUsed = false
    private var isCashbackExists = false
    private var paymentMethod = ""
    private lateinit var type: String
    private var offerAmount = 0.00

    override val layoutId: Int
        get() = R.layout.activity_order_summary
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false


    override fun onClick(v: View?) {
        when (v) {
            binding.btnContinue -> {
                if (binding.cbCash.isChecked || binding.cbOnline.isChecked) {
                    if (type==Constants.BUNDLE_TYPE_SERVICE) {
                        goServiceCheckout()
                    } else {
                        goNormalCheckout()
                    }
                } else {
                    Toaster.showToast(
                        this,
                        "Please select payment method",
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }

            }
            binding.tvChangeAddress -> startActivity(Intent(this, AddressActivity::class.java))
            binding.cardView7 -> {
                if (couponList.isNotEmpty()) {
                    showCouponDialog()
                } else {
                    Toaster.showToast(
                        this,
                        "Coupon not available",
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }

            }



        }
    }

    private fun goServiceCheckout() {
        val bundle = Bundle()
        bundle.putString(Constants.BUNDLE_TYPE, Constants.BUNDLE_TYPE_SERVICE)
        bundle.putString(Constants.BUNDLE_KEY_QUOTE_ID, quoteId)
        bundle.putBoolean(Constants.BUNDLE_COUPON_IS_USED, isCouponCodeUsed)
        bundle.putString(Constants.BUNDLE_COUPON_CODE, offerCode)
        bundle.putBoolean(Constants.BUNDLE_GIFT_IS_USED, isGiftCardUsed)
        bundle.putString(Constants.BUNDLE_GIFT_CODE, giftCardCode)
        bundle.putBoolean(Constants.BUNDLE_REWARD, isRewardUsed)
        bundle.putString(Constants.BUNDLE_PAYMENT_METHOD, paymentMethod)
        val intent = Intent(this, ConfirmOrderActivity::class.java)
        intent.putExtra(Constants.BUNDLE_DATA, bundle)
        startActivity(intent)

    }

    private fun goNormalCheckout() {
        val bundle = Bundle()
        bundle.putString(Constants.BUNDLE_TYPE, Constants.BUNDLE_TYPE_NORMAL)
        bundle.putBoolean(Constants.BUNDLE_COUPON_IS_USED, isCouponCodeUsed)
        bundle.putString(Constants.BUNDLE_COUPON_CODE, offerCode)
        bundle.putBoolean(Constants.BUNDLE_GIFT_IS_USED, isGiftCardUsed)
        bundle.putString(Constants.BUNDLE_GIFT_CODE, giftCardCode)
        bundle.putBoolean(Constants.BUNDLE_REWARD, isRewardUsed)
        bundle.putString(Constants.BUNDLE_PAYMENT_METHOD, paymentMethod)
        bundle.putBoolean(Constants.BUNDLE_CASH_BACK_IS_USED, getValueCashback())
        val intent = Intent(this, ConfirmOrderActivity::class.java)
        intent.putExtra(Constants.BUNDLE_DATA, bundle)
        startActivity(intent)

    }

    private fun getValueCashback(): Boolean {
        return binding.radioWallet.isChecked
    }

    private fun showCouponDialog() {
        var isLargeLayout =
            resources.getBoolean(R.bool.large_layout)
        val fragmentManager = supportFragmentManager
        fragmentManager.setFragmentResult(
            REQUEST_KEY_COUPON,
            bundleOf(BUNDLE_KEY_COUPON to couponList)
        )
        val newFragment = CouponDialogFragment()
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

    override fun getViewBinding(): ActivityOrderSummaryBinding {
        return ActivityOrderSummaryBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        binding.layoutToolbar.tvToolbarTitle.text = getString(R.string.order_summary)
        binding.btnContinue.setOnClickListener(this)
        binding.tvChangeAddress.setOnClickListener(this)
        binding.cardView7.setOnClickListener(this)
        getIntentData()
        binding.etCouponApply.setOnClickListener {
            if(binding.radioWallet.isChecked){
                Toaster.showToast(
                    this,
                    "Only one offer can be used at a time",
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            }else{
                callCouponCodeApply(binding.etCode.text.toString())
            }

        }
        binding.etRemove.setOnClickListener {
            binding.etRemove.visibility=View.GONE
            binding.etCouponApply.visibility=View.VISIBLE
            offerAmount=0.0
            grandTotal= (grandTotal.toDouble()+offerAmount).toString()
            isCouponCodeUsed=false
            offerCode=""
            binding.etCode.setText("")
            binding.tvDiscountCoupon.visibility=View.GONE
        }
//        binding.etReward.setOnClickListener {
//            callCouponCodeApply(binding.etCoupon.text.toString())
//        }

        binding.cardWallet.setOnClickListener {
            if(!isCouponCodeUsed){
                if (!isCashbackExists) {
                    Toaster.showToast(
                        this,
                        "Cash back not applicable",
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                    binding.radioWallet.isChecked = !binding.radioWallet.isChecked

                }else{
                    if(!binding.radioWallet.isChecked){
                        binding.radioWallet.isChecked=true
//                        binding.cardCashback.visibility=View.VISIBLE
//                        hideView(binding.cardCashback)
//                    val dialog = Dialog(
//                        this,
//                        android.R.style.Theme_Translucent_NoTitleBar
//                    )
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                    dialog.setContentView(R.layout.dialog_redeem)
//                    val lp = WindowManager.LayoutParams()
//                    lp.copyFrom(dialog.window!!.attributes)
//                    lp.width = WindowManager.LayoutParams.MATCH_PARENT
//                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//                    lp.gravity = Gravity.CENTER
//                    dialog.show()

                    }else{
                        binding.radioWallet.isChecked=false
                        isCashBackUsed=false
                    }
                }
            }
            else{
                Toaster.showToast(
                    this,
                    "Only one offer can be used at a time",
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            }
            }

        setUpObserver()
        viewModel.getCoupon()
        binding.cbCash.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                paymentMethod = PAYMENT_COD
                binding.cbOnline.isChecked = false
                binding.cbCash.isChecked = true
            }
        }
        binding.cbOnline.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                paymentMethod = PAYMENT_ONLINE
                binding.cbCash.isChecked = false
                binding.cbOnline.isChecked = true
            }
        }
    }

    private fun getIntentData() {
        try {
            if (intent.getBundleExtra(BUNDLE_AMOUNT)!![BUNDLE_TYPE] == Constants.BUNDLE_TYPE_SERVICE) {
                type=Constants.BUNDLE_TYPE_SERVICE
                quoteId = intent.getBundleExtra(BUNDLE_AMOUNT)!![BUNDLE_KEY_QUOTE_ID].toString()
                viewModel.serviceSummary(quoteId)
            } else {
                type=Constants.BUNDLE_TYPE_NORMAL
                viewModel.getOfferCheck()
                val intent = intent
                val hashMap = intent.getSerializableExtra(BUNDLE_AMOUNT) as HashMap<String, String>?
                grandTotal = hashMap!![KEY_GRAND_TOTAL].toString()
                itemTotal = hashMap!![KEY_ITEM_TOTAL].toString()
                binding.tvSubTotal.text = getString(
                    R.string.Rupees_symbol,
                    hashMap!![Constants.KEY_ITEM_TOTAL].toString().toDouble().toPrice()
                )

//                binding.tvAddCharge.text =
//                    getCurrencyFormat(hashMap!![KEY_DELIVERY_CHARGE].toString())
                binding.tvServiceCharge.text =
                    getString(
                        R.string.Rupees_symbol,
                        hashMap!![Constants.KEY_SERVICE_CHARGE].toString().toDouble().toPrice()
                    )

                binding.tvTax.text =
                    getString(
                        R.string.Rupees_symbol,
                        hashMap!![Constants.KEY_TAX].toString().toDouble().toPrice()
                    )
                binding.tvTotal.text = getString(
                    R.string.Rupees_symbol,
                    hashMap!![Constants.KEY_GRAND_TOTAL].toString().toDouble().toPrice())
            }
        } catch (ex: Exception) {
            type=Constants.BUNDLE_TYPE_NORMAL
            viewModel.getOfferCheck()
            val intent = intent
            val hashMap = intent.getSerializableExtra(BUNDLE_AMOUNT) as HashMap<String, String>?
//            grandTotal = bundle[BUNDLE_KEY_GRAND_TOTAL].toString()
//            itemTotal = bundle[BUNDLE_KEY_ITEM_TOTAL].toString()

            binding.tvSubTotal.text = getString(
                R.string.Rupees_symbol,
                hashMap!![Constants.KEY_ITEM_TOTAL].toString().toDouble().toPrice()
            )
            grandTotal = hashMap!![KEY_GRAND_TOTAL].toString()
            itemTotal = hashMap!![KEY_ITEM_TOTAL].toString()
            binding.tvSubTotal.text = getString(
                R.string.Rupees_symbol,
                hashMap!![Constants.KEY_ITEM_TOTAL].toString().toDouble().toPrice()
            )
            binding.tvAddCharge.text = getCurrencyFormat(hashMap!![KEY_DELIVERY_CHARGE].toString())
            binding.tvServiceCharge.text =
                getString(
                    R.string.Rupees_symbol,
                    hashMap!![Constants.KEY_SERVICE_CHARGE].toString().toDouble().toPrice()
                    )

            binding.tvTax.text =   getString(
                R.string.Rupees_symbol,
                hashMap!![Constants.KEY_TAX].toString().toDouble().toPrice()
            )
            binding.tvTotal.text =   getString(
                R.string.Rupees_symbol,
                hashMap!![Constants.KEY_GRAND_TOTAL].toString().toDouble().toPrice()
            )


            binding.tvDetails.setOnClickListener {
                showInfoDialog(
                    hashMap!![KEY_DELIVERY_CHARGE].toString(),
                    hashMap!![KEY_DELIVERY_SUR_CHARGE].toString(),
                    hashMap!![KEY_SERVICE_CHARGE].toString(),
                    hashMap!![KEY_PACKING_CHARGE].toString()
                )
            }
        }


    }

    private fun showInfoDialog(
        deliveryCharge: String,
        deliverySurgeCharge: String,
        serviceCharge: String,
        packingCharge: String
    ) {
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

    override fun onResume() {
        super.onResume()
        supportFragmentManager
            .setFragmentResultListener(REQUEST_KEY_CODE, this) { requestKey, bundle ->
                if(binding.radioWallet.isChecked){
                    Toaster.showToast(
                        this,
                        "Only one offer can be used at a time",
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }else{
                    val code = bundle.getString(BUNDLE_KEY_CODE)
                    callCouponCodeApply(code)
                }
                // We use a String here, but any type that can be put in a Bundle is supported



                // Do something with the result
            }
    }

    private fun callCouponCodeApply(code: String?) {
        val data = HashMap<String, Any>()
        data[OFFER_CODE] = code!!
        data[ITEM_TOTAL] = itemTotal.toDouble()
        data[GRAND_TOTAL] = grandTotal.toDouble()
        binding.etCode.setText(code.toString())
        offerCode=code.toString()
        viewModel.applyCoupon(data)

    }


    @SuppressLint("SetTextI18n")
    private fun setUpObserver() {

        viewModel.couponApplyLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    binding.etRemove.visibility=View.VISIBLE
                    binding.etCouponApply.visibility=View.GONE
                    grandTotal= it.data!!.data.grandTotal.toString()
                    offerAmount= it.data!!.data.offerAmount.toString().toDouble()
                    isCouponCodeUsed=true
                    val couponBuilder = StringBuilder()
                    couponBuilder.append(getString(R.string.you_have_saved_kwd_152_00_on_the_bill))
                    couponBuilder.append(" ")
                    couponBuilder.append(
                        offerAmount
                    )
                    couponBuilder.append(" ")
                    couponBuilder.append(getString(R.string.bill))
                    binding.tvDiscountCoupon.visibility=View.VISIBLE
                    binding.tvDiscountCoupon.text=couponBuilder.toString()


                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                }
                Status.ERROR -> {
                    Toaster.showToast(
                        this,
                        it.message!!,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                    dismissProgress()
                }
            }
        })
        viewModel.couponLiveData.observe(this, Observer { couponResponse ->
            when (couponResponse.status) {
                Status.SUCCESS -> {
                    couponList = couponResponse.data!!.data.items


                }
                Status.LOADING -> {

                }
                Status.NO_INTERNET -> {


                }
                Status.ERROR -> {

                }
            }
        })
        viewModel.offerCheckLiveData.observe(this, Observer { offerLiveDataResponse ->
            when (offerLiveDataResponse.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    isCashbackExists = offerLiveDataResponse.data!!.data.isCashbackExists
                    if(offerLiveDataResponse.data!!.data.isRewardExists){

                    }
                    binding.tvWalletCash.text =
                        getCurrencyFormat(offerLiveDataResponse.data!!.data.maximumCashBack.toString())
                    binding.tvPatrickWallet.text =
                        "Total wallet cash ${getCurrencyFormat(offerLiveDataResponse.data!!.data.cashbackAmount.toString())}"

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                }
            }
        })


        viewModel.serviceConfirmOrderLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    isCashbackExists = it.data!!.data.isCashbackExists
                    binding.tvWalletCash.text =
                        getCurrencyFormat(it.data!!.data.maximumCashBack.toString())
                    binding.tvPatrickWallet.text =
                        "Total wallet cash ${getCurrencyFormat(it.data!!.data.cashbackAmount.toString())}"
                    binding.tvSubTotal.text = getCurrencyFormat(it.data!!.data.subTotal.toString())

                    binding.tvServiceCharge.text =
                        getCurrencyFormat(it.data!!.data.sellerServiceCharge.toString())

                    binding.tvTax.visibility = View.GONE
                    binding.textTax.visibility = View.GONE
                    binding.tvAddCharge.visibility = View.GONE
                    binding.textAddCharge.visibility = View.GONE

                    binding.tvTotal.text =
                        getCurrencyFormat(it.data!!.data.subTotalWithServiceCharge.toString())
                    grandTotal = it.data!!.data.subTotalWithServiceCharge.toString()
                    itemTotal = it.data!!.data.subTotal.toString()


                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.NO_INTERNET -> {
                    dismissProgress()

                }
                Status.ERROR -> {
                    dismissProgress()
                }
            }

        })
    }

    private fun hideView(view: View) {
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_down)
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                view.visibility=View.GONE
            }
        })
        view.startAnimation(animation)
    }
}
