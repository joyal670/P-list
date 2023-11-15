package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.subscriptionhistory.subscriptionhistorydetails

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.proteinium.proteiniumdietapp.pojo.addon.Carb
import com.proteinium.proteiniumdietapp.pojo.addon.ExtrasAdd
import com.proteinium.proteiniumdietapp.pojo.addon.Protein
import com.proteinium.proteiniumdietapp.pojo.meal_plan.ExtrasModified
import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.ExtraModify
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.user_id
import com.proteinium.proteiniumdietapp.utils.*
import com.proteinium.proteiniumdietapp.utils.payment.PaymentConfig
import kotlinx.android.synthetic.main.activity_checkout_.*
import kotlinx.android.synthetic.main.activity_subscription_add.*
import kotlinx.android.synthetic.main.activity_subscription_add.rvExtras
import kotlinx.android.synthetic.main.activity_subscription_add.tvCrabPrice
import kotlinx.android.synthetic.main.activity_subscription_add.tvExtras
import kotlinx.android.synthetic.main.activity_subscription_add.tvProteinPrice
import kotlinx.android.synthetic.main.activity_subscription_add.tvTotalPrice
import kotlinx.android.synthetic.main.toolbar_sub.*


class SubscriptionAddActivity : BaseActivity() {
    private var carbPrice = 0.00
    private var proteinPrice = 0.00
    private var currentProteinPrice = 0.00
    private var currentCarbPrice = 0.00
    private var proteinCount=-1
    private var carbCount=-1
    private var mealPlanId = 0
    private var currentCrab=0
    private var currentCrabCount=0
    private var currentProteinCount=0
    private var currentProtein=0
    private var overallPrice=0.00
    private lateinit var bottomChange: BottomSheetDialog
    private var protien_reduction_message:String=""
    private var carbs_message:String=""
    private var isRenewal=false
    private lateinit var subscrptionDetailsViewModel: SubscriptionHistoryDetailsViewModel
    private var percenat = 0
    private lateinit var mapSdk: MFSDK
    private var paymentMethod=2
    private var  extrasQty:Int=0
    private var duration=0
    var extrasArrayList = ArrayList<String>()
    var extrasAdd =ArrayList<ExtrasAdd>()
    private var extrasPriceHashList = HashMap<Int, String>()
    private var extrasPriceHashListCopy = HashMap<Int, String>()
    var extrasModified = ArrayList<ExtrasModified>()
    private var extrasPrice = 0.00
    private lateinit var extrasModificationAdapter: ExtrasModificationAdapter


    override val layoutId: Int
        get() = R.layout.activity_subscription_add
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
        tvSubToolbarTitle.text = getString(R.string.modify_add_on)

        if (intent.getIntExtra("meal_plan_subscription_id", -1) != null) {
            isRenewal=intent.getBooleanExtra("is_renewal",false)
            mealPlanId=intent.getIntExtra(
                "meal_plan_subscription_id",
                -1
            )!!
            subscrptionDetailsViewModel.getAddOnData(
                intent.getIntExtra(
                    "meal_plan_subscription_id",
                    -1
                )!!,isRenewal
            )

        }

        MFSDK.init(PaymentConfig.PAYMENT_API_LIVE_KEY, MFCountry.KUWAIT, MFEnvironment.LIVE)
        MFSDK.setUpActionBar(getString(R.string.checkout), R.color.white, R.color.color_main, true)
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        subscrptionDetailsViewModel =
            ViewModelProviders.of(this).get(SubscriptionHistoryDetailsViewModel::class.java)
    }

    override fun setupObserver() {
        subscrptionDetailsViewModel.getCancel().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoader()
                    if(::bottomChange.isInitialized){
                        bottomChange.dismiss()
                    }
                    setDataMessage(it.data?.message)
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

        subscrptionDetailsViewModel.getExtrasResponse().observe(this, Observer {

            when (it.status) {
                Status.SUCCESS -> {
                    extrasModified = ArrayList<ExtrasModified>()
                    it.data!!.data.extras.forEach {it2->
                        val extrasModify=ArrayList<ExtraModify>()
                        var qty=0
                        extrasAdd.forEach {
                            if(it2.id==it.id){
                                qty=it.quantity.toInt()
                            }
                        }
                        it2.data.forEach { it3->
                            try{
                                if(it3["quantity"].toString()>qty.toString()){
                                    extrasModify.add(ExtraModify(it2.id.toString(),it3["quantity"]!!,it3["$duration"]!!,it2.name,false))

                                }
                            }catch (ex:Exception){

                            }

                        }
                        extrasModified.add(
                            ExtrasModified
                                (it2.id.toString(), it2.name,extrasModify)
                        )

                    }
                    if(extrasModified.isNotEmpty()){
                        tvExtras.visibility=View.VISIBLE
                    }
                    rvExtras.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL!!,false)
                    extrasModificationAdapter=ExtrasModificationAdapter(extrasModified,{s1,s2,s3,s4->
                        extrasPriceHashList[s1.toInt()] = s3
                        extrasPriceHashListCopy[s1.toInt()] = s2
                        var value = 0.00
                        for (i in extrasPriceHashList.values) {
                            value += i.toDouble()
                        }
                        extrasArrayList = ArrayList<String>()
                        for (i in extrasPriceHashListCopy.values) {
                            extrasArrayList.add(i)
                        }
                        extrasPrice = value.toString().toDouble()
                        setPrice()
                    },{
                        extrasModificationAdapter.notifyDataSetChanged()
                    },{
                        extrasPriceHashList.remove(it.toInt())
                        extrasPriceHashListCopy.remove(it.toInt())
                        var value = 0.00
                        for (i in extrasPriceHashList.values) {
                            value += i.toDouble()
                        }
                        extrasArrayList = ArrayList<String>()
                        for (i in extrasPriceHashListCopy.values) {
                            extrasArrayList.add(i)
                        }
                        extrasPrice = value.toString().toDouble()
                        setPrice()
                    })
                    rvExtras.adapter=extrasModificationAdapter

                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
                Status.NO_INTERNET -> {

                }
                Status.DATA_EMPTY -> {

                }
            }
        })
        subscrptionDetailsViewModel.getAddOnData().observe(this, Observer { addOnResponse ->
            when (addOnResponse.status) {
                Status.SUCCESS -> {
                    dismissLoader()
                    protien_reduction_message=addOnResponse.data!!.data.protien_reduction_message
                    carbs_message=addOnResponse.data!!.data.carbs_message
                    extrasAdd= addOnResponse.data!!.data.extras as ArrayList<ExtrasAdd>
                    duration=addOnResponse.data!!.data.duration
                    subscrptionDetailsViewModel.getExtras(
                        intent.getIntExtra(
                            "meal_plan_subscription_id",
                            -1
                        )!!,isRenewal
                    )
                    setUpCarbs(
                        addOnResponse.data!!.data.carbs,
                        addOnResponse.data!!.data.carbs_alter_status,
                        addOnResponse.data!!.data.current_carbs
                    )
                    setUpProteins(
                        addOnResponse.data!!.data.proteins,
                        addOnResponse.data!!.data.current_proteins,
                        addOnResponse.data!!.data.protien_reduction_status
                    )
                }
                Status.ERROR -> {
                    dismissLoader()
                }
                Status.LOADING -> {
                    showLoader()
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                }
            }
        })

    }

    private fun setDataMessage(message: String?) {
        val dialog = Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent=dialog?.findViewById(R.id.tvContent) as TextView
        tvContent.text=message

        yesBtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        closeBtn.setOnClickListener {
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

    override fun onClicks() {
        contentProceedBtn.setOnClickListener {
            if(currentCrabCount==carbCount){
                carbCount=-1
            }
            if(currentProteinCount==proteinCount){
                proteinCount=-1

            }
            var isModify=false
            overallPrice=0.00
            if(extrasArrayList.isNotEmpty()){
                isModify=true
                var value = 0.00
                for (i in extrasPriceHashList.values) {
                    value += i.toDouble()
                }
                overallPrice +=value
            }
            if(carbCount==-1){
                carbPrice=0.00
            }else{
                if(carbPrice>currentCarbPrice){
                    overallPrice+=carbPrice
                    isModify=true
                }

            }
            if(proteinCount==-1){
                proteinPrice=0.00
            }else{
                if(proteinPrice>currentProteinPrice){
                    overallPrice+=proteinPrice
                    isModify=true
                }
            }
            if(carbCount!=-1||proteinCount!=-1){
                if(isModify){
                    showPaymentPopUp()
                }
                else{
                    subscrptionDetailsViewModel.updateAddOn(user_id!!,mealPlanId,carbCount.toString(),carbPrice.toString(),proteinCount.toString(),
                        proteinPrice.toString(),
                        "",
                        overallPrice.toString(),
                        isRenewal,extrasArrayList)
                }
            }else{
                subscrptionDetailsViewModel.updateAddOn(user_id!!,mealPlanId,carbCount.toString(),carbPrice.toString(),proteinCount.toString(),
                    proteinPrice.toString(),
                    "",
                    overallPrice.toString(),
                    isRenewal,extrasArrayList)



            }



        }

    }

    private fun setUpProteins(
        proteins: List<Protein>,
        currentProteins: Int,
        protienReductionStatus: Boolean
    ) {

        var intCountProtein = 0
        for (i in proteins.indices) {
            if (proteins[i].proteins == currentProteins.toString()) {
                currentProtein=i
                proteinCount=i
                intCountProtein = i
            }
        }
        proteinPrice = proteins[intCountProtein].proteins_price.toString().toDouble()
        proteinCount= proteins[intCountProtein].proteins.toInt()
        currentProteinCount=proteins[intCountProtein].proteins.toInt()
        currentProteinPrice = proteinPrice
        proteinsCountBtn.text = proteins[intCountProtein].proteins.toString() + "g"
        setPrice()
        addProteinsBtn.setOnClickListener {
            intCountProtein++
            if (intCountProtein < proteins.size) {
                proteinsCountBtn.text = proteins[intCountProtein].proteins.toString() + "g"
                proteinCount= proteins[intCountProtein].proteins.toInt()
                proteinPrice = proteins[intCountProtein].proteins_price.toString().toDouble()
                setPrice()
            } else {
                intCountProtein--
                CommonUtils.warningToast(this, getString(R.string.error_maximu_protein))
            }
        }
        removeProteinsBtn.setOnClickListener {
            intCountProtein--
            if (protienReductionStatus) {
                if (intCountProtein != -1) {
                    proteinsCountBtn.text = proteins[intCountProtein].proteins.toString() + "g"
                    proteinPrice = proteins[intCountProtein].proteins_price.toString().toDouble()
                    proteinCount= proteins[intCountProtein].proteins.toInt()
                    setPrice()

                } else {
                    intCountProtein++
                }
            }else{
                if(intCountProtein>currentProtein||intCountProtein==currentProtein){
                    proteinsCountBtn.text = proteins[intCountProtein].proteins.toString() + "g"
                    proteinPrice = proteins[intCountProtein].proteins_price.toString().toDouble()
                    proteinCount= proteins[intCountProtein].proteins.toInt()
                    intCountProtein++
                    setPrice()
                }else{
                    CommonUtils.warningToast(this, protien_reduction_message)
                }

            }

        }
    }

    private fun setPrice() {
        var overAllPrice=0.000
        overAllPrice+=extrasPrice
        if(proteinPrice>currentProteinPrice){
            overAllPrice+=proteinPrice
        }
        if(carbPrice>currentCarbPrice){
            overAllPrice+=carbPrice
        }

        tvTotalPrice.text = getString(R.string.kwd) + overAllPrice.toPriceRound()
        tvCrabPrice.text = getString(R.string.kwd) + carbPrice.toPrice()
        tvProteinPrice.text = getString(R.string.kwd) + proteinPrice.toPrice()
    }

    private fun setUpCarbs(carbs: List<Carb>, carbsAlterStatus: Boolean, currentCarbs: Int) {
        var intCountCarb = 0
        for (i in carbs.indices) {
            if (carbs[i].carbs == currentCarbs.toString()) {
                intCountCarb = i
                currentCrab=i
            }
        }
        carbsCountBtn.text = carbs[intCountCarb].carbs+"g"
        carbPrice = carbs[intCountCarb].carbs_price.toString().toDouble()
        carbCount=carbs[intCountCarb].carbs.toInt()
        currentCrabCount=carbs[intCountCarb].carbs.toInt()
        currentCarbPrice=carbPrice
        setPrice()
        removeCarbsBtn.setOnClickListener {
            if(carbsAlterStatus){
                intCountCarb--
                if (intCountCarb != -1) {
                    carbsCountBtn.text = carbs[intCountCarb].carbs.toString() + "g"
                    carbPrice = carbs[intCountCarb].carbs_price.toString().toDouble()
                    carbCount=carbs[intCountCarb].carbs.toInt()
                    setPrice()
                } else {
                    intCountCarb++


                }
            }else{
                CommonUtils.warningToast(this, carbs_message)
            }
        }

        addCarbsBtn.setOnClickListener {
            if(carbsAlterStatus){
                intCountCarb++
                if (intCountCarb < carbs.size) {
                    carbsCountBtn.text = carbs[intCountCarb].carbs.toString() + "g"
                    this.carbPrice = carbs[intCountCarb].carbs_price.toString().toDouble()
                    carbCount=carbs[intCountCarb].carbs.toInt()
                    setPrice()
                } else {
                    intCountCarb--
                    CommonUtils.warningToast(this, getString(R.string.error_maximu_carb))

                }
            }else{

                CommonUtils.warningToast(this, carbs_message)
            }
        }
    }
    private fun showPaymentPopUp() {
        bottomChange = BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog)
        val bottomSheet: View = this.layoutInflater.inflate(R.layout.activity_paymennt_method, null)

        val radio_visa = bottomSheet.findViewById<RadioButton>(R.id.radio_visa)
        val radio_netcard = bottomSheet.findViewById<RadioButton>(R.id.radio_netcard)
        val paymentProceed=bottomSheet.findViewById<Button>(R.id.paymentProceed)
        val llPaymentCharge=bottomSheet.findViewById<LinearLayout>(R.id.llPaymentCharge)
        val tvPaymentCharge=bottomSheet.findViewById<TextView>(R.id.tvPaymentCharge)
        val tvGrandTotal=bottomSheet.findViewById<TextView>(R.id.tvGrandTotal)

        val charge=0.003*overallPrice
        val totalAmount=overallPrice+charge
        llPaymentCharge.visibility=View.VISIBLE
        tvPaymentCharge.text= getString(R.string.kwd) + charge.toDouble()
            .toPriceRound()
        tvGrandTotal.text= getString(R.string.kwd) + totalAmount.toDouble()
            .toPriceRound()

        paymentProceed.setOnClickListener {
            paymentPage()
        }
        radio_netcard.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
            {
                radio_visa.isChecked = false
                paymentMethod=1
                llPaymentCharge.visibility=View.GONE
                tvGrandTotal.text= getString(R.string.kwd) + overallPrice.toDouble()
                    .toPriceRound()
            }
        }
        radio_visa.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
            {
                radio_netcard.isChecked = false
                paymentMethod=2
                val charge=0.003*overallPrice
                val totalAmount=overallPrice+charge
                llPaymentCharge.visibility=View.VISIBLE
                tvPaymentCharge.text= getString(R.string.kwd) + charge.toDouble()
                    .toPriceRound()
                tvGrandTotal.text= getString(R.string.kwd) + totalAmount.toDouble()
                    .toPriceRound()
            }
        }


        bottomChange.setContentView(bottomSheet)
        bottomChange.show()
    }

    private fun paymentPage() {
        val request = MFExecutePaymentRequest(
            paymentMethod,
            overallPrice
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
                    if(AppPreferences.chooseLanguage ==Constants.ARABIC_LAG){
                        CommonUtils.changeLanguageAwareContext(this,Constants.ARABIC_LAG)
                    }
                    if(AppPreferences.chooseLanguage ==Constants.ENGLISH_LAG){
                        CommonUtils.changeLanguageAwareContext(this,Constants.ENGLISH_LAG)
                    }
                    if(AppPreferences.chooseLanguage ==Constants.ARABIC_LAG){
                        CommonUtils.changeLanguageAwareContext(this,Constants.ARABIC_LAG)
                    }
                    if(AppPreferences.chooseLanguage ==Constants.ENGLISH_LAG){
                        CommonUtils.changeLanguageAwareContext(this,Constants.ENGLISH_LAG)
                    }
                    subscrptionDetailsViewModel.updateAddOn(user_id!!,mealPlanId,carbCount.toString(),carbPrice.toString(),proteinCount.toString(),
                        proteinPrice.toString(),
                        result.response.invoiceTransactions!![0].paymentId,
                        overallPrice.toString(),
                        isRenewal,
                        extrasArrayList
                    )
                }
                is MFResult.Fail -> {
                    if(AppPreferences.chooseLanguage ==Constants.ARABIC_LAG){
                        CommonUtils.changeLanguageAwareContext(this,Constants.ARABIC_LAG)
                    }
                    if(AppPreferences.chooseLanguage ==Constants.ENGLISH_LAG){
                        CommonUtils.changeLanguageAwareContext(this,Constants.ENGLISH_LAG)
                    }
                }

            }
        }
    }

    }







