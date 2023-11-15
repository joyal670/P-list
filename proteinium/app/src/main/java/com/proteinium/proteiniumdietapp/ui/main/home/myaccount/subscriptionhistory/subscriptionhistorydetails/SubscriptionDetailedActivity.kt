package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.subscriptionhistorydetails

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.DeliveryAddress
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.chooseLanguage
import com.proteinium.proteiniumdietapp.preference.AppPreferences.user_id
import com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.MealPlanActivity
import com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.adapter.ExtraAdapter
import com.proteinium.proteiniumdietapp.utils.*
import kotlinx.android.synthetic.main.activity_subscription_detailed.*
import kotlinx.android.synthetic.main.activity_subscription_detailed.cardExtras
import kotlinx.android.synthetic.main.activity_subscription_detailed.tvExtras
import kotlinx.android.synthetic.main.activity_subscription_detailed.tvNonStop
import kotlinx.android.synthetic.main.activity_subscription_detailed.tvProteinPrice
import kotlinx.android.synthetic.main.toolbar_sub.*
import kotlin.math.roundToInt


class SubscriptionDetailedActivity : BaseActivity() {
    private lateinit var subscrptionDetailsViewModel: SubscriptionHistoryDetailsViewModel
    private var mealPlanId = 0
    private var percenat = 0
    private var cancelId=-1
    private var isRenewal=false
    override val layoutId: Int
        get() = R.layout.activity_subscription_detailed
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false
    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbarSub)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_toolbar24)
        }
        if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvSubToolbarTitle.text = getString(R.string.tvSubscriptionPreview)

        Log.e("meal_plan_sub_id", intent.getIntExtra("meal_plan_subscription_id", -1).toString())



    }

    override fun onResume() {
        super.onResume()
        if (intent.getIntExtra("meal_plan_subscription_id", -1) != null) {
            isRenewal=intent.getBooleanExtra("is_renewal",false)
            subscrptionDetailsViewModel.fetchSubscriptionsPreviewDetails(
                intent.getIntExtra(
                    "meal_plan_subscription_id",
                    -1
                )!!,
                isRenewal

            )
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        subscrptionDetailsViewModel =
            ViewModelProviders.of(this).get(SubscriptionHistoryDetailsViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setupObserver() {
        subscrptionDetailsViewModel.getCancel().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.data?.message!!)
                    tvStatus.text = getString(R.string.canceled)
                    tvStatus.setTextColor(getColor(R.color.color_accent_red_dark))
                    if(constraintModify.isVisible){
                        constraintModify.visibility = View.GONE
                    }
                    if(clSubscriptionBtn.isVisible){
                        clSubscriptionBtn.visibility = View.GONE
                    }

                    renewSubscriptionBtn.visibility = View.VISIBLE
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
        subscrptionDetailsViewModel.getSubscriptionsPreviewDetailsResponse()
            .observe(this, Observer {
                when (it.status) {

                    Status.SUCCESS -> {
                        dismissLoader()
                        if (it.data?.status!!) {
                            nestedMain.visibility=View.VISIBLE
                            if (it.data.data != null) {
                                cancelId=it.data.data.id
                                if(it.data.data.non_stop_status==1) {
                                    tvNonStop.text=getString(R.string.yes)
                                }else{
                                    tvNonStop.text=getString(R.string.no)
                                }
                                if(it.data.data.duration==1){
                                    tvDuration.text=it.data.data.duration.toString()+" "+getString(R.string.week)
                                }else{
                                    tvDuration.text=it.data.data.duration.toString()+" "+getString(R.string.weeeks)
                                }



                                when(it.data.data.plan_status){
                                    getString(R.string.not_yet_started)->{
                                        tvStatus.setTextColor(getColor(R.color.orange1))
                                    }
                                    getString(R.string.pending)->{
                                        tvStatus.setTextColor(getColor(R.color.orange1))
                                    }
                                    getString(R.string.renewed)->{
                                        tvStatus.setTextColor(getColor(R.color.orange1))
                                    }
                                    getString(R.string.canceled)->{
                                        tvStatus.setTextColor(getColor(R.color.color_accent_red_dark))
                                    }
                                    getString(R.string.completed)->{
                                        tvStatus.setTextColor(getColor(R.color.green))
                                    }
                                }

                                val categoryAndPlan =
                                    it.data.data.meal_category_name + "-" + it.data.data.meal_plan_name
                                tvSubscriptionDetailCategoryPlan.text = categoryAndPlan
                                if (it.data.data.plan_status == getString(R.string.canceled) || it.data.data.plan_status == getString(
                                        R.string.completed
                                    )
                                ) {
                                    constraintModify.visibility = View.GONE
                                    renewSubscriptionBtn.visibility = View.VISIBLE

                                } else if (it.data.data.plan_status == getString(
                                        R.string.pending
                                    )){
                                    constraintModify.visibility = View.VISIBLE
                                    renewSubscriptionBtn.visibility = View.GONE
                                    clSubscriptionBtn.visibility = View.GONE
                                }
                                else {
                                    if (it.data.data.enable_modifications == 1) {
                                        constraintModify.visibility = View.VISIBLE
                                        clSubscriptionBtn.visibility = View.GONE
                                    } else {
                                        constraintModify.visibility = View.GONE
                                        clSubscriptionBtn.visibility = View.VISIBLE
                                    }

                                    renewSubscriptionBtn.visibility = View.GONE
                                }
                                if (it.data.data.payment_method == Constants.CARD_MASTER) {
                                    ivPayment2.visibility=View.VISIBLE
                                    ivPayment.setImageResource(R.drawable.sample_visa)
                                } else {
                                    ivPayment2.visibility=View.GONE
                                    ivPayment.setImageResource(R.drawable.sample_netcard)
                                }
                                if(it.data!!.data.promo_discount_price.roundToInt()!=0){
                                    constraintDiscount.visibility=View.VISIBLE
                                    tvDisCount.text="${getString(R.string.kwd) +it.data!!.data.promo_discount_price.toDouble().toPriceRound()}"
                                }
                                tvStatus.text = it.data.data.plan_status
                                mealPlanId = it.data.data.meal_plan_id
                                percenat = if (AppPreferences.chooseLanguage == "ar") {
                                    (it.data.data.percentage.toString().toDouble()
                                        .toInt() * 10) * -1
                                } else {
                                    it.data.data.percentage.toString().toDouble().toInt() * 10
                                }

                                startAnimation()
                                tvAmount.text =
                                    "${getString(R.string.kwd) + it.data.data.total.toString().toDouble().toPriceRound()} "
                                tvStartDateValue.text = it.data.data.plan_start_date
                                tvEndDateValue.text = it.data.data.plan_end_date
                                val deliveryAddress =
                                    setDeliveryAddress(it.data.data.delivery_address)
                                if(chooseLanguage==Constants.ARABIC_LAG){
                                    tvDeliveryAddress.gravity=Gravity.END
                                }
                                tvDeliveryAddress.text = deliveryAddress
                                if (AppPreferences.user_phone != null) {
                                    tvPhone.text = AppPreferences.user_phone
                                }

                                tvProteins.text = it.data.data.proteins.toString() + "g"
                                tvCarbs.text = it.data.data.carbs.toString() + "g"
                                tvProteinPrice.text = "${getString(R.string.kwd) + it.data.data.proteins_price.toString().toDouble().toPriceRound()}"
                                tvCarbPrice.text = "${getString(R.string.kwd) + it.data.data.carbs_price.toString().toDouble().toPriceRound()}"

                                if(it.data.data.extras.isNotEmpty()){
                                    tvExtras.visibility=View.VISIBLE
                                    cardExtras.visibility=View.VISIBLE
                                    rvExtras.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                                    rvExtras.adapter = ExtraAdapter( it.data.data.extras)
                                }


                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissLoader()
                        CommonUtils.warningToast(this, it.message.toString())
                    }
                    Status.LOADING -> {
                        showLoader()
                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                        CommonUtils.warningToast(this, getString(R.string.data_empty))
                    }
                }
            })
    }

    private fun startAnimation() {
        if(chooseLanguage==Constants.ARABIC_LAG){
            subscription_Lottie.rotationY= -180f
        }

        val anim =
            ObjectAnimator.ofFloat(subscription_Lottie, "translationX", percenat.toFloat()).apply {
                duration = 2500
                start()

            }
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                subscription_Lottie.pauseAnimation()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }

    private fun setDeliveryAddress(deliveryAddress: DeliveryAddress): String {
        var address: String = ""
        val defaultAdr = StringBuilder()

        defaultAdr.append(getString(R.string.governorate))
        defaultAdr.append(": ")
        defaultAdr.append(deliveryAddress.governorate)
        defaultAdr.append(", ")

        defaultAdr.append(getString(R.string.area))
        defaultAdr.append(": ")
        defaultAdr.append(deliveryAddress.area)
        defaultAdr.append(", ")

        defaultAdr.append(getString(R.string.block))
        defaultAdr.append(": ")
        defaultAdr.append(deliveryAddress.block)
        defaultAdr.append(", ")

        defaultAdr.append(getString(R.string.street))
        defaultAdr.append(": ")
        defaultAdr.append(deliveryAddress.street)
        defaultAdr.append(", ")

        if (!deliveryAddress.avenue.isNullOrEmpty()) {
            defaultAdr.append(getString(R.string.avenue))
            defaultAdr.append(": ")
            defaultAdr.append(deliveryAddress.avenue)
            defaultAdr.append(", ")
        }

        defaultAdr.append(getString(R.string.building))
        defaultAdr.append(": ")
        defaultAdr.append(deliveryAddress.building)
        defaultAdr.append(", ")

        if (!deliveryAddress.floor.isNullOrBlank()) {
            defaultAdr.append(getString(R.string.floor))
            defaultAdr.append(": ")
            defaultAdr.append(deliveryAddress.floor)
            defaultAdr.append(", ")
        }

        if (!deliveryAddress.appartment.isNullOrBlank()) {
            defaultAdr.append(getString(R.string.appartment))
            defaultAdr.append(": ")
            defaultAdr.append(deliveryAddress.appartment)
        }

        return defaultAdr.toString()
    }

    override fun onClicks() {
        renewSubscriptionBtn.setOnClickListener {
            val intent = Intent(this, MealPlanActivity::class.java)
            intent.putExtra(Constants.INTENT_MEAL_ID, mealPlanId)
            startActivity(intent)
        }
        cancelSubscriptionBtn.setOnClickListener {
            cancleSubscription()

        }
        clSubscriptionBtn.setOnClickListener {
            cancleSubscription()
        }
        subscriptionModifyBtn.setOnClickListener {
            modifySubscription()
        }
    }
    private fun modifySubscription() {
        val dialog =  Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_meal_selection)

        val content=dialog?.findViewById(R.id.tvContent) as TextView
        val yesBtn = dialog?.findViewById(R.id.MealSelectionYesBtn) as MaterialButton
        val noBtn = dialog?.findViewById(R.id.MealSelectionNoBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivMealSelectionClose) as ImageView

        content.text=getString(R.string.modify_error)
        yesBtn.text=getString(R.string.ok)
        noBtn.text=getString(R.string.cancel)
        yesBtn.setOnClickListener {
            dialog.dismiss()
            val intent=Intent(this,SubscriptionAddActivity::class.java)
            intent.putExtra("meal_plan_subscription_id",cancelId)
            intent.putExtra("is_renewal", isRenewal)
            startActivity(intent)
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun cancleSubscription() {
        val dialog =  Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_meal_selection)

        val content=dialog?.findViewById(R.id.tvContent) as TextView
        val yesBtn = dialog?.findViewById(R.id.MealSelectionYesBtn) as MaterialButton
        val noBtn = dialog?.findViewById(R.id.MealSelectionNoBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivMealSelectionClose) as ImageView

        content.text=getString(R.string.are_you_sure_cancel_subscription)
        yesBtn.text = getString(R.string.ok)
        noBtn.text = getString(R.string.cancel)

        yesBtn.setOnClickListener {
            dialog.dismiss()
            subscrptionDetailsViewModel.cancelSybscription(user_id!!, cancelId,isRenewal)
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog?.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}
