package com.proteinium.proteiniumdietapp.ui.main.home.home.order_confirm

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.isPlanActive
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.adapter.ExtrasSummaryAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.adapter.modal.ExtrasModal
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Constants.INTENT_OLD_PRICE
import com.proteinium.proteiniumdietapp.utils.Constants.INTENT_PROMO
import com.proteinium.proteiniumdietapp.utils.EnumFromPage
import com.proteinium.proteiniumdietapp.utils.toPrice
import com.proteinium.proteiniumdietapp.utils.toPriceRound
import kotlinx.android.synthetic.main.activity_order_confirm_.*
import kotlinx.android.synthetic.main.activity_order_confirm_.rvExtras
import kotlinx.android.synthetic.main.activity_order_confirm_.tvCrabPrice
import kotlinx.android.synthetic.main.activity_order_confirm_.tvGrandTotal
import kotlinx.android.synthetic.main.activity_order_confirm_.tvNonStopPrice
import kotlinx.android.synthetic.main.activity_order_confirm_.tvProtein
import kotlinx.android.synthetic.main.activity_order_confirm_.tvProteinPrice


class OrderConfirm_Activity : BaseActivity() {
    private var keyList:ArrayList<String> = ArrayList()
    private var valueList:ArrayList<String> =ArrayList()
    private var promocode=0.0
    override val layoutId: Int
        get() = R.layout.activity_order_confirm_
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        valueList= intent.getStringArrayListExtra(Constants.INTENT_VALUE)!!
        keyList= intent.getStringArrayListExtra(Constants.INTENT_KEY)!!
        tvDuration.text=keyList[0]
        tvDurationValue.text=valueList[0]+" "+getString(R.string.weeeks)
        tvPlan.text=keyList[1]
        tvBasicFee.text=getString(R.string.kwd)+valueList[1].toDouble().toPrice()
        tvCarb.text=keyList[2]
        tvCrabPrice.text=getString(R.string.kwd)+valueList[2].toDouble().toPrice()
        tvProtein.text=keyList[3]
        tvProteinPrice.text=getString(R.string.kwd)+valueList[3].toDouble().toPrice()
        tvNonStop.text=keyList[4]
        tvNonStopPrice.text=getString(R.string.kwd)+valueList[4].toDouble().toPrice()
        if(intent.getDoubleExtra(INTENT_OLD_PRICE,0.00).toString()!=valueList[5].toDouble().toPrice()){
            tvSubTotal.text=getString(R.string.kwd)+intent.getDoubleExtra(INTENT_OLD_PRICE,0.00).toPrice()
        }else{
            tvSubTotal.text=getString(R.string.kwd)+valueList[5].toDouble().toPrice()
        }
        if(intent.getStringExtra(INTENT_PROMO)!!.isNotEmpty()){
            Log.e("#56565656", "initData: ${intent.getStringExtra(INTENT_PROMO)}" )
            promocode=intent.getStringExtra(INTENT_PROMO)!!.toDouble()
            llPromo.visibility=View.VISIBLE
            viewDiscount.visibility=View.VISIBLE
            tvPromo.text=getString(R.string.kwd)+ intent.getStringExtra(INTENT_PROMO)!!.toDouble().toPriceRound()
        }



        tvGrandTotal.text=getString(R.string.kwd)+valueList[5].toDouble().toPriceRound()
        val address=intent.getStringExtra(Constants.INTENT_ADDRESS)
        tvAddress.text=address
        val paymentMethod=intent.getStringExtra(Constants.INTENT_PAYMENT_METHOD)
        if(paymentMethod=="1"){
            ivPayment.setImageResource(R.drawable.sample_netcard)
        }else if(paymentMethod=="2"){
            ivPayment.setImageResource(R.drawable.sample_visa)
        }else{
            cardPayment.visibility=View.GONE
        }
        if(valueList.size>7){
            tvExtras.visibility=View.VISIBLE
            cardExtras.visibility=View.VISIBLE
            val extrasList:ArrayList<ExtrasModal> =ArrayList<ExtrasModal>()
            for(i in 7 until valueList.size){
                extrasList.add(ExtrasModal(keyList[i].toString(),valueList[i].toString()))
            }
            rvExtras.layoutManager= LinearLayoutManager(this)
            rvExtras.adapter= ExtrasSummaryAdapter(extrasList)
        }
        if(isPlanActive){
            homeBtn.visibility=View.VISIBLE
        }else{
            selectMealBtn.visibility=View.VISIBLE
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks()
    {
        selectMealBtn.setOnClickListener {
                val intent= Intent(this, HomeActivity::class.java)
                AppPreferences.isCalendar=true
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putExtra(Constants.TYPE, EnumFromPage.CALENDER.name)
                startActivity(intent)
                finish()



        }
        homeBtn.setOnClickListener {
            val intent= Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra(Constants.TYPE, EnumFromPage.HOME.name)
            startActivity(intent)
            finish()
        }
    }

}