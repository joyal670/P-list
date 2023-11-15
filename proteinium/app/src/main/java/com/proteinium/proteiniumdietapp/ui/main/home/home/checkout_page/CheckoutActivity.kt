package com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.myfatoorah.sdk.entity.executepayment.MFExecutePaymentRequest
import com.myfatoorah.sdk.entity.paymentstatus.MFGetPaymentStatusResponse
import com.myfatoorah.sdk.utils.MFAPILanguage
import com.myfatoorah.sdk.utils.MFCountry
import com.myfatoorah.sdk.utils.MFEnvironment
import com.myfatoorah.sdk.views.MFResult
import com.myfatoorah.sdk.views.MFSDK
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.pojo.get_addresses.Address
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.chooseLanguage
import com.proteinium.proteiniumdietapp.preference.AppPreferences.user_id
import com.proteinium.proteiniumdietapp.ui.main.home.home.add_deliveryaddress.AddDelivery_Activity
import com.proteinium.proteiniumdietapp.ui.main.home.home.add_deliveryaddress.EditAddedAddressActivity
import com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.adapter.AddressAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.adapter.ExtrasSummaryAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.adapter.modal.ExtrasModal
import com.proteinium.proteiniumdietapp.ui.main.home.home.order_confirm.OrderConfirm_Activity
import com.proteinium.proteiniumdietapp.ui.start_up.auth.AuthActivity
import com.proteinium.proteiniumdietapp.utils.*
import com.proteinium.proteiniumdietapp.utils.Constants.CARD_K_NET
import com.proteinium.proteiniumdietapp.utils.Constants.CARD_MASTER
import com.proteinium.proteiniumdietapp.utils.payment.PaymentConfig
import kotlinx.android.synthetic.main.activity_checkout_.*
import kotlinx.android.synthetic.main.toolbar_sub.*
import kotlin.math.roundToInt


class CheckoutActivity : BaseActivity() {
    private lateinit var checkoutViewModel: CheckoutViewModel
    private var addressesList: ArrayList<Address> = ArrayList()

    lateinit var bottom: BottomSheetDialog
    var myAddress: String = ""
    var delectStatus: Boolean = false
    private var selectedAddressId: Int = -1
    private var mealplanId: Int = -1
    private var unique_key = ""
    private var defaultAddress: String = ""
    private var keyList: ArrayList<String> = ArrayList()
    private var valueList: ArrayList<String> = ArrayList()
    private var oldPrice = 0.00
    private var payment_method: String = ""
    private var mealPlanId_Promo: Int = 0
    private var renewal = 0
    private var promo = ""
    private var isHoliday = true
    private var grandTotal=0.0

    private lateinit var mapSdk: MFSDK
    private var paymentMethod = 2
    var code = ""
    var oldTotal = 0.00

    override val layoutId: Int
        get() = R.layout.activity_checkout_
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
    }

    override fun onResume() {
        super.onResume()
        if (AppPreferences.isLogin) {
            tvDeliveryAddressTitle.visibility = View.VISIBLE
            addAddressBtn.visibility = View.VISIBLE
            checkoutViewModel.getAddresses(AppPreferences.user_id!!)
        } else {
            tvDeliveryAddressTitle.visibility = View.VISIBLE
            addAddressBtn.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        setSupportActionBar(toolbarSub)
        toolbarSub.setBackgroundResource(R.color.white)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back)
        }
        if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward)
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvSubToolbarTitle.text = getString(R.string.checkout)
        tvSubToolbarTitle.setTextColor(Color.parseColor("#000000"))

        valueList = intent.getStringArrayListExtra(Constants.INTENT_VALUE)!!
        keyList = intent.getStringArrayListExtra(Constants.INTENT_KEY)!!
        mealplanId = intent.getIntExtra(Constants.INTENT_MEAL_PLAN_ID, 0)
        mealPlanId_Promo = intent.getIntExtra(Constants.INTENT_MEAL_ID, 0)
        unique_key = intent.getStringExtra(Constants.INTENT_UNIQUE_ID).toString()
        tvPlanDuration.text =
            valueList[0].toDouble().roundToInt().toString() + " " + getString(R.string.weeeks)
        tvMealsName.text = keyList[1]
        tvBasePrice.text = getString(R.string.kwd) + valueList[1].toDouble().toPrice()
        tvCrab.text = keyList[2]
        tvProtein.text = keyList[3]
        if (valueList[2].toDouble().roundToInt() != 0) {
            tvCrabPrice.text = getString(R.string.kwd) + valueList[2].toDouble().toPrice()
        }
        if (valueList[3].toDouble().roundToInt() != 0) {
            tvProteinPrice.text = getString(R.string.kwd) + valueList[3].toDouble().toPrice()
        }
        if (valueList[4].toDouble().roundToInt() != 0) {
            tvNonStopPrice.text = getString(R.string.kwd) + valueList[4].toDouble().toPrice()
        }
        if (valueList[5].toDouble().roundToInt() != 0) {
            oldPrice = valueList[5].toDouble()
            Log.e("#45659585", "initData:${valueList[5].toDouble()} ")
            grandTotal=valueList[5].toDouble()
            tvTotalPrice.text = getString(R.string.kwd) + valueList[5].toDouble().toPrice()
            tvGrandTotal.text = getString(R.string.kwd) + valueList[5].toDouble().toPriceRound()
        }

        renewal = valueList[6].toInt()
        if (renewal == 1) {
            rlCoupon.visibility = View.GONE
            tvPromo.visibility = View.GONE
        }

        if (valueList.size > 7) {
            tvExtras.visibility = View.VISIBLE
            cardExtras.visibility = View.VISIBLE
            val extrasList: ArrayList<ExtrasModal> = ArrayList<ExtrasModal>()
            for (i in 7 until valueList.size) {
                extrasList.add(ExtrasModal(keyList[i].toString(), valueList[i].toString()))
            }
            rvExtras.layoutManager = LinearLayoutManager(this)
            rvExtras.adapter = ExtrasSummaryAdapter(extrasList)
        }


        bottom = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
        radio_netcard.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radio_visa.isChecked = false
                paymentMethod = 1
                llPaymentCharge.visibility=View.GONE
                tvGrandTotal.text= getString(R.string.kwd) + grandTotal.toDouble()
                    .toPriceRound()
            }
        }
        radio_visa.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radio_netcard.isChecked = false
                paymentMethod = 2
                if(grandTotal.toInt()!=0){
                    val charge=0.003*grandTotal
                    val totalAmount=grandTotal+charge
                    llPaymentCharge.visibility=View.VISIBLE
                    tvPaymentCharge.text= getString(R.string.kwd) + charge.toDouble()
                        .toPriceRound()
                    tvGrandTotal.text= getString(R.string.kwd) + totalAmount.toDouble()
                        .toPriceRound()
                }


            }
        }
        MFSDK.init(PaymentConfig.PAYMENT_API_LIVE_KEY, MFCountry.KUWAIT, MFEnvironment.LIVE)
//        MFSDK.init(PaymentConfig.PAYMENT_URL,PaymentConfig.PAYMENT_TOKEN)
        MFSDK.setUpActionBar(getString(R.string.checkout), R.color.white, R.color.color_main, true)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        checkoutViewModel = ViewModelProviders.of(this).get(CheckoutViewModel::class.java)
    }

    override fun setupObserver() {
        checkoutViewModel.getAddressesResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()

                    addressesList.clear()
                    if (it.data?.status!!) {
                        if (it.data?.data != null) {
                            addressesList.clear()
                            defaultAddress = ""
                            addressesList = it.data?.data as ArrayList<Address>
                            if (addressesList.isNotEmpty()) {
//                                tv_selectAddress.visibility = View.VISIBLE
                                addAddressBtn.visibility = View.GONE
                            } else {
                                addAddressBtn.visibility = View.VISIBLE
                            }
                            addressesList.forEachIndexed { index, address ->
                                if (addressesList[index].default == 1) {
//                                    tv_selectAddress.visibility = View.VISIBLE
                                    selectedAddressId = addressesList[index].id
                                    val defaultAdr = StringBuilder()

                                    defaultAdr.append(getString(R.string.governorate))
                                    defaultAdr.append(": ")
                                    defaultAdr.append(addressesList[index].governorate)
                                    defaultAdr.append(", ")

                                    defaultAdr.append(getString(R.string.area))
                                    defaultAdr.append(": ")
                                    defaultAdr.append(addressesList[index].area)
                                    defaultAdr.append(", ")

                                    defaultAdr.append(getString(R.string.block))
                                    defaultAdr.append(": ")
                                    defaultAdr.append(addressesList[index].block)
                                    defaultAdr.append(", ")

                                    defaultAdr.append(getString(R.string.street))
                                    defaultAdr.append(": ")
                                    defaultAdr.append(addressesList[index].street)
                                    defaultAdr.append(", ")

                                    if (!addressesList[index].avenue.isNullOrEmpty()) {
                                        defaultAdr.append(getString(R.string.avenue))
                                        defaultAdr.append(": ")
                                        defaultAdr.append(addressesList[index].avenue)
                                        defaultAdr.append(", ")
                                    }

                                    defaultAdr.append(getString(R.string.building))
                                    defaultAdr.append(": ")
                                    defaultAdr.append(addressesList[index].building)
                                    defaultAdr.append(", ")

                                    if (!addressesList[index].floor.isNullOrBlank()) {
                                        defaultAdr.append(getString(R.string.floor))
                                        defaultAdr.append(": ")
                                        defaultAdr.append(addressesList[index].floor)
                                        defaultAdr.append(", ")
                                    }

                                    if (!addressesList[index].appartment.isNullOrBlank()) {
                                        defaultAdr.append(getString(R.string.appartment))
                                        defaultAdr.append(": ")
                                        defaultAdr.append(addressesList[index].appartment)
                                    }
                                    if (!addressesList[index].appartment.isNullOrBlank()) {
                                        defaultAdr.append(", ")
                                        defaultAdr.append(getString(R.string.additional_direction))
                                        defaultAdr.append(": ")
                                        defaultAdr.append(addressesList[index].additonal_directions)
                                    }




                                    defaultAddress = defaultAdr.toString()

                                }

                            }
                            if (defaultAddress.isNotEmpty()) {
                                cardViewSelectAddress.visibility = View.GONE
                                cardViewSelectAddress.visibility = View.VISIBLE
                                tvSelectedAddress.text = defaultAddress
                            } else {
                                cardViewSelectAddress.visibility = View.GONE
                                tvSelectedAddress.text = ""
                            }
                            if (delectStatus) {
                                delectStatus = false
                                selectAddress()
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message.toString())
                }
                Status.LOADING -> {
                    if (!delectStatus) {
                        showLoader()
                    }
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.no_internet))
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
//                    tv_selectAddress.visibility = View.GONE
                }
            }
        })
        checkoutViewModel.deleteAddressResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    addressesList.clear()
                    if (it.data?.status!!) {
                        bottom.dismiss()
                        user_id?.let { user_id -> checkoutViewModel.getAddresses(user_id) }
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message.toString())
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.no_internet))

                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.data_empty))
                }
            }
        })
        checkoutViewModel.getDefaultAddressResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    dismissLoader()
                    for (i in 0 until addressesList.size

                    ) {
                        if (addressesList[i].id == selectedAddressId) {
                            addressesList[i].default = 1
                        } else {
                            addressesList[i].default = 0
                        }
                    }
                    if (it.data?.status!!) {
                        Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                        getAddress()
                    }
                }
                Status.ERROR -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.something_wrong))
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.no_internet))
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.data_empty))
                }
            }
        })
        checkoutViewModel.finalSubscription().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoader()
                    try {
                        if (it.data!!.promocode_discount_price.roundToInt() != 0) {
                            promo = it.data!!.promocode_discount_price.toString()
                        }
                    } catch (ex: Exception) {
                        promo = ""
                    }

                    val intent = Intent(this, OrderConfirm_Activity::class.java)
                    intent.putExtra(Constants.INTENT_VALUE, valueList)
                    intent.putExtra(Constants.INTENT_KEY, keyList)
                    intent.putExtra(Constants.INTENT_ADDRESS, defaultAddress)
                    intent.putExtra(Constants.INTENT_PAYMENT_METHOD, paymentMethod.toString())
                    intent.putExtra(Constants.INTENT_OLD_PRICE, oldPrice)
                    intent.putExtra(Constants.INTENT_PROMO, promo)
                    startActivity(intent)
                }
                Status.DATA_EMPTY -> {
                    CommonUtils.warningToast(this, it.message!!)
                    dismissLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, getString(R.string.no_internet))
                }
            }
        })
        checkoutViewModel.getPromoCode().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoader()
                    rlCoupon.visibility = View.VISIBLE
                    code = etPromoCode.text.trim().toString()
                    val couponBuilder = StringBuilder()
                    couponBuilder.append(getString(R.string.you_have_saved_kwd_152_00_on_the_bill))
                    couponBuilder.append(" ")
                    couponBuilder.append(
                        it.data!!.data.discount_amount.toString().toDouble().toPrice()
                    )
                    couponBuilder.append(" ")
                    couponBuilder.append(getString(R.string.bill))
                    tvDiscount.text = couponBuilder.toString()
                    tvGrandTotal.text =
                        getString(R.string.kwd) + it.data!!.data.new_total.toString().toDouble()
                            .toPriceRound()
                    grandTotal= it.data!!.data.new_total.toString().toDouble()
                    valueList[5] = it.data!!.data.new_total.toString()
                    btnRemove.visibility = View.VISIBLE
                    btnApply.visibility = View.GONE

                }
                Status.ERROR -> {
                    etPromoCode.text = null
                    rlCoupon.visibility = View.GONE
                    tvGrandTotal.text =
                        getString(R.string.kwd) + oldPrice.toString().toDouble().toPrice()
                    CommonUtils.warningToast(this, it.data!!.message)
                    dismissLoader()
                }
            }


        })

    }

    override fun onClicks() {
        addAddressBtn.setOnClickListener {
            if (AppPreferences.isLogin) {
                startActivity(Intent(this, AddDelivery_Activity::class.java))
            } else {
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra(Constants.INTENT_TYPE, Constants.TYPE_GUEST)
                startActivity(intent)
            }

        }

        tv_selectAddress.setOnClickListener {
            selectAddress()
        }
        btnApply.setOnClickListener {
            if (AppPreferences.isLogin) {
                checkoutViewModel.promoCode(
                    AppPreferences.user_id!!,
                    valueList[5].toDouble().toPrice().toString(),
                    etPromoCode.text.toString().trim(),
                    mealPlanId_Promo
                )
            } else {
                CommonUtils.warningToast(this, getString(R.string.need_to))
            }

        }
        btnRemove.setOnClickListener {
            btnRemove.visibility = View.GONE
            btnApply.visibility = View.VISIBLE
            rlCoupon.visibility = View.GONE
            etPromoCode.text = null
            code = ""
            valueList[5] = oldPrice.toString()
            tvTotalPrice.text = getString(R.string.kwd) + valueList[5].toDouble().toPrice()
            tvGrandTotal.text = getString(R.string.kwd) + valueList[5].toDouble().toPriceRound()
        }

        makePaymentBtn.setOnClickListener {
            if (AppPreferences.isLogin) {
                if (defaultAddress.isNotEmpty()) {
                    if (valueList[5].toDouble().roundToInt() != 0) {
                        val request = MFExecutePaymentRequest(
                            paymentMethod,
                            valueList[5].toDouble().toPriceRound().toDouble()
                        )
                        MFSDK.executePayment(
                            this,
                            request,
                            MFAPILanguage.EN,
                            onInvoiceCreated = {
                            }
                        ) { invoiceId: String, result: MFResult<MFGetPaymentStatusResponse> ->
                            when (result) {
                                is MFResult.Success -> {
                                    val response: MFResult<MFGetPaymentStatusResponse> = result
                                    if (chooseLanguage == Constants.ARABIC_LAG) {
                                        CommonUtils.changeLanguageAwareContext(
                                            this,
                                            Constants.ARABIC_LAG
                                        )
                                    }
                                    if (chooseLanguage == Constants.ENGLISH_LAG) {
                                        CommonUtils.changeLanguageAwareContext(
                                            this,
                                            Constants.ENGLISH_LAG
                                        )
                                    }
                                    if (paymentMethod == 2) {
                                        payment_method = CARD_MASTER
                                    } else {
                                        payment_method = CARD_K_NET
                                    }
                                    checkoutViewModel.fetchFinalSubscription(
                                        user_id!!,
                                        selectedAddressId,
                                        mealplanId,
                                        payment_method,
                                        code,
                                        result.response.invoiceTransactions!![0].paymentId,
                                        unique_key,
                                        renewal
                                    )
                                }
                                is MFResult.Fail -> {
                                    if (chooseLanguage == Constants.ARABIC_LAG) {
                                        CommonUtils.changeLanguageAwareContext(
                                            this,
                                            Constants.ARABIC_LAG
                                        )
                                    }
                                    if (chooseLanguage == Constants.ENGLISH_LAG) {
                                        CommonUtils.changeLanguageAwareContext(
                                            this,
                                            Constants.ENGLISH_LAG
                                        )
                                    }
                                }

                            }
                        }
                    } else {
                        paymentMethod = 3
                        checkoutViewModel.fetchFinalSubscription(
                            user_id!!,
                            selectedAddressId,
                            mealplanId,
                            "3",
                            code,
                            "",
                            unique_key,
                            renewal
                        )
                    }


                } else {
                    CommonUtils.warningToast(this, getString(R.string.error_address))
                }
            } else {
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra(Constants.INTENT_TYPE, Constants.TYPE_GUEST)
                startActivity(intent)
            }
        }
        ivEditAddress.setOnClickListener {
            val intent = Intent(this, EditAddedAddressActivity::class.java)
            intent.putExtra("address_id", selectedAddressId)
            startActivity(intent)
        }
    }

    private fun selectAddress() {

        val bottomSheet: View = this.layoutInflater.inflate(R.layout.select_address, null)
        bottom.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        val rvAddress = bottomSheet.findViewById<RecyclerView>(R.id.rvAddressList)
        val close = bottomSheet.findViewById<ImageView>(R.id.ivCloseAddress)
        val select = bottomSheet.findViewById<MaterialButton>(R.id.selectAddedBtn)
        val it: Iterator<Address> = addressesList.iterator()
        it.forEach {
            if (it.default != 1) {
                addressesList.remove(it)
            }
        }

        addressesList.forEach {

        }
        rvAddress.layoutManager = LinearLayoutManager(this)
        rvAddress.adapter = AddressAdapter(addressesList,
            { address_id: Int, address: String -> selectedAddress(address_id, address) },
            { position: Int, address_id: Int -> deleteAddress(position, address_id) },
            { editAddress(it) })
        close.setOnClickListener {
            bottom.dismiss()
        }
        select.setOnClickListener {
            if (selectedAddressId != -1) {

                checkoutViewModel.setDefaultAddress(user_id!!, selectedAddressId)
            }
        }
        bottom.setContentView(bottomSheet)
        bottom.show()

    }

    private fun getAddress() {
        if (myAddress.isNotEmpty()) {
            cardViewSelectAddress.visibility = View.VISIBLE
            tvSelectedAddress.text = myAddress
            bottom.dismiss()
            myAddress = ""
        } else {
            Toast.makeText(this, getString(R.string.error_address), Toast.LENGTH_SHORT).show()
        }


    }

    private fun editAddress(it: Int) {
        bottom.dismiss()
        val intent = Intent(this, EditAddedAddressActivity::class.java)
        intent.putExtra("address_id", it)
        startActivity(intent)
    }

    private fun deleteAddress(position: Int, address_id: Int) {
        delectStatus = true
        checkoutViewModel.deleteAddress(address_id)
        if (myAddress.isNotEmpty()) {
            cardViewSelectAddress.visibility = View.VISIBLE
            tvSelectedAddress.text = myAddress
            bottom.dismiss()
            myAddress = ""
        }
    }

    private fun selectedAddress(address_id: Int, address: String) {
        selectedAddressId = address_id
        myAddress = address
    }
}
