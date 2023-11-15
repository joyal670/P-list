package com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.material.appbar.AppBarLayout
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.pojo.meal_plan.*
import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.ExtraModify
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.user_id
import com.proteinium.proteiniumdietapp.ui.main.home.home.checkout_page.CheckoutActivity
import com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.adapter.DaysOfMonthAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.adapter.ExtrasAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.adapter.OffDaysAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.adapter.PlanAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.home.detailed_page.details_page_date_model.DateModal
import com.proteinium.proteiniumdietapp.ui.main.home.myaccount.dislikes.activity.DislikesActivity
import com.proteinium.proteiniumdietapp.utils.*
import kotlinx.android.synthetic.main.activity_main_calender_horizontal.*
import kotlinx.android.synthetic.main.activity_meal_plan.*
import kotlinx.android.synthetic.main.content_scrolling_details.*
import kotlinx.android.synthetic.main.no_internet.*
import ru.rhanza.constraintexpandablelayout.State
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.math.roundToInt


class MealPlanActivity : BaseActivity() {
    private val calendar = Calendar.getInstance(Locale.ENGLISH)
    private var currentMonth = 0
    private val dateList: MutableList<Date> = mutableListOf()
    private val dateListNew: MutableList<Date> = mutableListOf()
    private var scrollPosition = 0
    private val dateModifiedList: ArrayList<DateModal> = ArrayList()
    private lateinit var planViewModel: MealPlanViewModel
    private var carbPrice = 0.00
    private var proteinPrice = 0.00
    private var basePrice = 0.00
    private var nonStopPrice = 0.00
    private var newNonStopPrice = 0.00
    private var startDate: String = ""
    private var subscriptionPlanId: Int = 0
    private var nonStopDeliveryStatus: Int = 0
    private var carbs: String = ""
    private var proteins: String = ""
    private var comments: String = ""
    private var mealPlanId: Int = 0
    private val keyList: ArrayList<String> = ArrayList()
    private val valueList: ArrayList<String> = ArrayList()
    private var duration = 0
    private var suspendInt = 0
    private var saveMealPlanCategory = ""
    private var saveMealPlanDescription = ""
    private var planStartBuffer = 0
    private var planStartBufferNew = 0
    private lateinit var daysOfMonthAdapter: DaysOfMonthAdapter
    private var isPlan = false
    private var isDate = false
    private var info = ""
    private lateinit var listMonths: List<String>
    private lateinit var image: Bitmap
    private var enable_modification = ""
    private var start_date_range = 0
    private var non_stop_check_day = ""
    private var extraSnack = 0
    private var extraSnackKWD = 0
    private lateinit var extrasAdapter: ExtrasAdapter
    private var extrasPriceHashList = HashMap<Int, String>()
    private var extrasPriceHashListCopy = HashMap<Int, String>()
    private var extrasPrice = 0.00
    private var nonStopCheckedDays = ""
    private var date = ""
    private var mealPlanName = ""
    private var isRenewal = false
    private var isLoded = false
    private var isSelectedDate = ""
    private var isClicked = false
    private var isNonStop = false
    private var isHoliday = true
    private var globalSuspension = ArrayList<String>()
    private var extrasList: ArrayList<Extras> = ArrayList()
    private var extrasArrayList = ArrayList<String>()
    private var extrasQtyHashMap=HashMap<Int,String>()
    private var extrasPriceHashMap= HashMap<Int,String>()





    override val layoutId: Int
        get() = R.layout.activity_meal_plan
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun initData() {
        setSupportActionBar(toolbar_detailed)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mealPlanId = intent.getIntExtra(Constants.INTENT_MEAL_ID, 0)
        planViewModel.fetchMealPlanResponse(mealPlanId, user_id!!)
        if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
            toolbar_btn.setBackgroundResource(R.drawable.round_btn)
            btnRight.setBackgroundResource(R.drawable.ic_arrow_right_24dp)
            btnLeft.setBackgroundResource(R.drawable.ic_arrow_left_24dp)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white)
        }
        if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
            toolbar_btn.setBackgroundResource(R.drawable.round_btn_ar)
            btnRight.setBackgroundResource(R.drawable.ic_arrow_left_24dp)
            btnLeft.setBackgroundResource(R.drawable.ic_arrow_right_24dp)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
        }
        listMonths = listOf(
            getString(R.string.January),
            getString(R.string.February),
            getString(R.string.March),
            getString(R.string.April),
            getString(R.string.May),
            getString(R.string.June),
            getString(R.string.July),
            getString(R.string.August),
            getString(R.string.September),
            getString(R.string.October),
            getString(R.string.November),
            getString(R.string.December)
        )


    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        planViewModel = MealPlanViewModel()


    }

    override fun setupObserver() {
        planViewModel.getMealPlanResponse()
            .observe(this, Observer { mealPlanResponse ->
                when (mealPlanResponse.status) {
                    Status.SUCCESS -> {
                        dismissLoader()
                        noInternetLayout.visibility = View.GONE
                        toolbar_btn.visibility = View.VISIBLE
                        tvPlanName.visibility = View.VISIBLE
                        llProceed.visibility = View.VISIBLE
                        mainLayout.visibility = View.VISIBLE
                        appbar_layout.visibility = View.VISIBLE
                        info = mealPlanResponse.data?.data!!.meal_plan.info_text

                        CommonUtils.setImage(
                            this,
                            mealPlanResponse.data.data.meal_plan.image,
                            vpDetailsImage
                        )
                        Glide.with(this).asBitmap()
                            .load(mealPlanResponse.data.data.meal_plan.image)
                            .into(object : CustomTarget<Bitmap?>() {
                                override fun onLoadCleared(placeholder: Drawable?) {}
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                                ) {
                                    try {
                                        image = resource
                                    } catch (ex: Exception) {

                                    }

                                }
                            })
                        mealPlanName = mealPlanResponse.data.data.meal_plan.name
                        tvPlanName.text = mealPlanName
                        setUpMealPlanData(mealPlanResponse.data.data.meal_plan)

                        nonStopCheckedDays = CommonUtils.NonStopDays(
                            mealPlanResponse.data.data.non_stop_check_day,
                            this
                        )
                        if (mealPlanResponse.data.data.meal_plan.plan_end_date != null) {
                            val format1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val format2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val currentDate = format1.format(Calendar.getInstance().time)
                            val currentTime = format1.parse(currentDate)
                            val endDate =
                                format1.parse(mealPlanResponse.data.data.meal_plan.plan_end_date)
                            var different = endDate.time - currentTime.time
                            val secondsInMilli: Long = 1000
                            val minutesInMilli = secondsInMilli * 60
                            val hoursInMilli = minutesInMilli * 60
                            val daysInMilli = hoursInMilli * 24

                            val elapsedDays = different / daysInMilli
                            if (elapsedDays <= mealPlanResponse.data.data.plan_start_buffer) {
                                planStartBuffer = mealPlanResponse.data.data.plan_start_buffer
                                planStartBufferNew = mealPlanResponse.data.data.plan_start_buffer
                            } else {
                                planStartBuffer = elapsedDays.toInt()
                                planStartBufferNew = elapsedDays.toInt()
                            }
                            isRenewal = true
                        } else {
                            planStartBuffer = mealPlanResponse.data.data.plan_start_buffer
                        }

                        start_date_range =
                            mealPlanResponse.data.data.start_date_range + planStartBuffer

                        calendar.time = Date()
                        calendar.add(
                            Calendar.DAY_OF_YEAR,
                            planStartBuffer + 1
                        )

                        currentMonth = calendar[Calendar.MONTH]
                        tv_month.text = listMonths[currentMonth]
                        globalSuspension =
                            mealPlanResponse.data.data.global_suspensions as ArrayList<String>
                        setDates(
                            planStartBuffer
                        )
                        setUpSubscriptionPlans(mealPlanResponse.data.data.meal_plan.durations)
                        setUpProteins(mealPlanResponse.data.data.proteins)
                        setUpCarbs(mealPlanResponse.data.data.carbs)
                        setMealPlanTitle(mealPlanResponse.data.data.meal_plan.name)
                        setUpNonStop(mealPlanResponse.data.data.nonstop_delivery_price)
                        extrasList = mealPlanResponse.data.data.extras as ArrayList<Extras>
                        setupExtraMeals(mealPlanResponse.data.data.extras)
                        setUpOffDays(
                            mealPlanResponse.data.data.meal_plan.off_days as ArrayList<String>,
                            mealPlanResponse.data.data.non_stop_check_day
                        )
                        layoutSelectStartDate.visibility = View.VISIBLE
                        tvCurrentSubscription.visibility = View.GONE


                    }
                    Status.ERROR -> {
                        dismissLoader()
                        if (this.isConnected) {
                            CommonUtils.warningToast(this, getString(R.string.something_wrong))
                        } else {
                            noInternetLayout.visibility = View.VISIBLE
                            toolbar_btn.visibility = View.GONE
                            tvPlanName.visibility = View.GONE
                            llProceed.visibility = View.GONE
                            mainLayout.visibility = View.GONE
                            appbar_layout.visibility = View.GONE
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                    }
                    Status.LOADING -> {
                        showLoader()
                    }


                }

            })

        planViewModel.getMealPlanAddResponse().observe(this, Observer { addSubscriptionResponse ->
            when (addSubscriptionResponse.status) {
                Status.SUCCESS -> {
                    dismissLoader()
                    keyList.clear()
                    valueList.clear()
                    keyList.add(addSubscriptionResponse.data!!.data.plan_summary.duration.name)
                    keyList.add(addSubscriptionResponse.data.data.plan_summary.plan.name)
                    keyList.add(getString(R.string.carbs))
                    keyList.add(getString(R.string.proteins))
                    keyList.add(addSubscriptionResponse.data.data.plan_summary.non_stop.name)
                    keyList.add(addSubscriptionResponse.data.data.plan_summary.total.name)

                    valueList.add(addSubscriptionResponse.data.data.plan_summary.duration.value.toString())
                    valueList.add(addSubscriptionResponse.data.data.plan_summary.plan.value.toString())
                    valueList.add(addSubscriptionResponse.data.data.plan_summary.carbs.value.toString())
                    valueList.add(addSubscriptionResponse.data.data.plan_summary.protein.value.toString())
                    valueList.add(addSubscriptionResponse.data.data.plan_summary.non_stop.value.toString())
                    valueList.add(addSubscriptionResponse.data.data.plan_summary.total.value.toString())



                    if (addSubscriptionResponse.data.data.renewal) {
                        AppPreferences.isPlanActive = true
                        keyList.add("renewel")
                        valueList.add("1")
                    } else {
                        AppPreferences.isPlanActive = false
                        keyList.add("renewel")
                        valueList.add("0")
                    }
                    extrasPriceHashMap.values.forEach {
                        valueList.add(it)
                    }
                    extrasQtyHashMap.values.forEach {
                        keyList.add(it)
                    }

                    val intent = Intent(this, CheckoutActivity::class.java)
                    intent.putExtra(Constants.INTENT_VALUE, valueList)
                    intent.putExtra(Constants.INTENT_KEY, keyList)
                    intent.putExtra(Constants.INTENT_MEAL_ID, mealPlanId)
                    intent.putExtra(
                        Constants.INTENT_MEAL_PLAN_ID,
                        addSubscriptionResponse.data.data.meal_plan_subscription_id
                    )
                    intent.putExtra(
                        Constants.INTENT_UNIQUE_ID,
                        addSubscriptionResponse.data.data.unique_key
                    )
                    startActivity(intent)


                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, addSubscriptionResponse.message.toString())
                }

            }
        })


    }

    fun bubbleSortCarbs(arr: ArrayList<Carb>): ArrayList<Carb> {
        var swap = true
        while (swap) {
            swap = false
            for (i in 0 until arr.size - 1) {
                if (arr[i].carbs.toInt() > arr[i + 1].carbs.toInt()) {
                    val temp = arr[i]
                    arr[i] = arr[i + 1]
                    arr[i + 1] = temp

                    swap = true
                }
            }
        }
        return arr
    }

    fun bubbleSort(arr: ArrayList<Protein>): ArrayList<Protein> {
        var swap = true
        while (swap) {
            swap = false
            for (i in 0 until arr.size - 1) {
                if (arr[i].proteins.toInt() > arr[i + 1].proteins.toInt()) {
                    val temp = arr[i]
                    arr[i] = arr[i + 1]
                    arr[i + 1] = temp

                    swap = true
                }
            }
        }
        return arr
    }

    private fun setUpOffDays(offDays: ArrayList<String>, nonStopCheckDay: String) {
        offDays.add(nonStopCheckDay)
        if (offDays.isNotEmpty()) {
            llOffDays.visibility = View.VISIBLE
            rvOffDays.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvOffDays.adapter = OffDaysAdapter(offDays)
        }


    }

    private fun setupExtraMeals(extras: List<Extras>) {
        val extrasModified = ArrayList<ExtrasModified>()
        tvExtrasPrice.text = "0.000"
        extrasPrice = 0.000

        extrasPriceHashList.clear()
        extrasPriceHashListCopy.clear()
        extrasQtyHashMap.clear()
        extrasPriceHashMap.clear()

        setPrice()
        extras.forEach {
            val extras = ArrayList<ExtraModify>()
            it.data.forEach { it2 ->
                extras.add(ExtraModify(it.id, it2["quantity"]!!, it2["$duration"]!!,
                it.name,
                false))
            }
            extrasModified.add(
                ExtrasModified
                    (it.id, it.name, extras)
            )
        }
        if (extras.isEmpty()) {
            viewExtra.visibility = View.GONE
            llExtraSnack.visibility = View.GONE
            tvExtrasDecription.visibility = View.GONE
        } else {
            viewExtra.visibility = View.VISIBLE
            llExtraSnack.visibility = View.VISIBLE
            rvExtra.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            extrasAdapter = ExtrasAdapter(
                extrasModified,{ s1: String, s2: String, s3: String ,s4:String,s5:String->
                    if(isPlan){
                        extrasPriceHashList[s1.toInt()] = s3
                        extrasPriceHashListCopy[s1.toInt()] = s2
                        extrasQtyHashMap[s1.toInt()]=s4
                        extrasPriceHashMap[s1.toInt()]=s5

                        var value = 0.00
                        for (i in extrasPriceHashList.values) {
                            value += i.toDouble()
                        }
                        extrasArrayList = ArrayList<String>()
                        for (i in extrasPriceHashListCopy.values) {
                            extrasArrayList.add(i)
                        }
                        tvExtrasPrice.text = value.toString().toDouble().toPrice()
                        extrasPrice = value.toString().toDouble()
                        setPrice()
                    }else{
                        CommonUtils.warningToast(this,getString(R.string.select_duration))
                        setupExtraMeals(extrasList)
                    }


                },{ s1->
                    if(isPlan){
                        extrasPriceHashList.remove(s1.toInt())
                        extrasPriceHashListCopy.remove(s1.toInt())
                        extrasQtyHashMap.remove(s1.toInt())
                        extrasPriceHashMap.remove(s1.toInt())

                        var value = 0.00
                        for (i in extrasPriceHashList.values) {
                            value += i.toDouble()
                        }
                        extrasArrayList = ArrayList<String>()
                        for (i in extrasPriceHashListCopy.values) {
                            extrasArrayList.add(i)
                        }
                        tvExtrasPrice.text = value.toString().toDouble().toPrice()
                        extrasPrice = value.toString().toDouble()
                        setPrice()
                    }else{
                        CommonUtils.warningToast(this,getString(R.string.select_duration))
                        setupExtraMeals(extrasList)
                    }


                },duration
            )
            rvExtra.adapter = extrasAdapter
        }

    }


    private fun functionRemove(id: Int, qty: Int, price: String) {
        extrasList.clear()

        var value = ""

    }

    private fun functionAdd() {

    }

    private fun setMealPlanTitle(mealCategoryName: String) {
        appbar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                //  State Expanded
                verticalOffset == 0 -> {
                    if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white)
                    }
                    if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
                    }
                    toolbar_detailed.title = mealCategoryName
                    ivShare.visibility = View.VISIBLE
                    ivInfo.visibility = View.VISIBLE
                    toolbar_btn.visibility = View.VISIBLE
                    tvPlanName.visibility = View.VISIBLE


                }
                //  State Collapsed
                abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                    if (AppPreferences.chooseLanguage == Constants.ENGLISH_LAG) {
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white)
                    }
                    if (AppPreferences.chooseLanguage == Constants.ARABIC_LAG) {
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_forward_white)
                    }
                    toolbar_detailed.title = mealCategoryName
                    ivShare.visibility = View.GONE
                    ivInfo.visibility = View.GONE

                    toolbar_btn.visibility = View.GONE
                    tvPlanName.visibility = View.GONE

                }
                else -> Log.e("onelse", "toolbar")//  Do anything for Ideal State

            }
        })

    }

    private fun setUpNonStop(nonstopDeliveryPrice: Int) {
        llNonStop.setOnClickListener {
            if (isPlan) {
                isClicked = true
                if (detailedPageradioBtn.isChecked) {
                    detailedPageradioBtn.isChecked = false
                    nonStopDeliveryStatus = 0
                    nonStopPrice = 0.000
                    setPrice()
                } else {
                    detailedPageradioBtn.isChecked = true
                    nonStopPrice = newNonStopPrice
                    nonStopDeliveryStatus = 1
                    isLoded = false
                    setPrice()

                }
            } else {
                CommonUtils.warningToast(this, getString(R.string.select_duration))

            }

        }
    }

    private fun setUpCarbs(carbs: List<Carb>) {
        var carbModifiedList: ArrayList<Carb> = ArrayList()
//        carbModifiedList.add(Carb("0","0"))
        carbs.forEach {
            carbModifiedList.add(Carb(it.carbs, it.carbs_price))
        }
        val initialCarbs = carbModifiedList[0].carbs
        carbModifiedList = bubbleSortCarbs(carbModifiedList)
        var intCountCarb = 0
        var i = 0
        carbModifiedList.forEach {
            if (initialCarbs == it.carbs) {
                intCountCarb = i
                carbsCountBtn.text = it.carbs + "g"
                carbPrice = it.carbs_price.toString().toDouble()
                this.carbs = it.carbs
                setPrice()
            }
            i++
        }

        removeCarbsBtn.setOnClickListener {
            if (isPlan) {
                intCountCarb--
                if (intCountCarb != -1) {
                    carbsCountBtn.text = carbModifiedList[intCountCarb].carbs.toString() + "g"
                    this.carbPrice =
                        carbModifiedList[intCountCarb].carbs_price.toString().toDouble()
                    setPrice()
                } else {
                    intCountCarb++

                }
            } else {
                CommonUtils.warningToast(this, getString(R.string.select_duration))
            }

        }

        addCarbsBtn.setOnClickListener {
            if (isPlan) {
                intCountCarb++
                if (intCountCarb < carbModifiedList.size) {
                    carbsCountBtn.text = carbModifiedList[intCountCarb].carbs.toString() + "g"
                    this.carbPrice =
                        carbModifiedList[intCountCarb].carbs_price.toString().toDouble()
                    this.carbs = carbModifiedList[intCountCarb].carbs
                    setPrice()
                } else {
                    intCountCarb--
                    CommonUtils.warningToast(this, getString(R.string.error_maximu_carb))

                }
            } else {
                CommonUtils.warningToast(this, getString(R.string.select_duration))

            }
        }

    }

    private fun setUpProteins(proteins: List<Protein>) {
        var modifiedProteinList: ArrayList<Protein> = ArrayList<Protein>()
        proteins.forEach {
            modifiedProteinList.add(Protein(it.proteins, it.proteins_price))

        }
        val initialProtein = modifiedProteinList[0].proteins
        modifiedProteinList = bubbleSort(modifiedProteinList)

        var intCountProtein = 0
        var i = 0
        modifiedProteinList.forEach {
            if (it.proteins == initialProtein) {
                intCountProtein = i
                proteinsCountBtn.text = it.proteins.toString() + "g"
                proteinPrice = it.proteins_price.toString().toDouble()
                this.proteins = it.proteins
                setPrice()
            }
            i++
        }


        removeProteinsBtn.setOnClickListener {
            if (isPlan) {
                intCountProtein--
                if (intCountProtein != -1) {
                    proteinsCountBtn.text =
                        modifiedProteinList[intCountProtein].proteins.toString() + "g"
                    proteinPrice =
                        modifiedProteinList[intCountProtein].proteins_price.toString().toDouble()
                    this.proteins = modifiedProteinList[intCountProtein].proteins
                    setPrice()

                } else {
                    intCountProtein++
                }
            } else {
                CommonUtils.warningToast(this, getString(R.string.select_duration))
            }

        }

        addProteinsBtn.setOnClickListener {
            if (isPlan) {
                intCountProtein++
                if (intCountProtein < modifiedProteinList.size) {
                    proteinsCountBtn.text =
                        modifiedProteinList[intCountProtein].proteins.toString() + "g"
                    proteinPrice =
                        modifiedProteinList[intCountProtein].proteins_price.toString().toDouble()
                    this.proteins = modifiedProteinList[intCountProtein].proteins
                    setPrice()
                } else {
                    intCountProtein--
                    CommonUtils.warningToast(this, getString(R.string.error_maximu_protein))
                }
            } else {
                CommonUtils.warningToast(this, getString(R.string.select_duration))
            }

        }


    }

    @SuppressLint("SetTextI18n", "StringFormatInvalid")
    private fun setPrice() {
        if (isSelectedDate == "Fri") {
            detailedPageradioBtn.isChecked = true
        } else {
            if (!isClicked) {
                detailedPageradioBtn.isChecked = false
            }
        }
        if (detailedPageradioBtn.isChecked) {
            nonStopPrice = newNonStopPrice
        }
        if (newNonStopPrice == 0.000) {
            isNonStop = false
            llNonStop.visibility = View.GONE
            tvNonStop.visibility = View.GONE
            viewNonStop.visibility = View.GONE
            detailedPageradioBtn.isChecked = false
            nonStopPrice = 0.000
        } else {
            isNonStop = true
            llNonStop.visibility = View.VISIBLE
            tvNonStop.visibility = View.VISIBLE
            tvNonStop.setText(getString(R.string.content_for_non_stop, nonStopCheckedDays))
            viewNonStop.visibility = View.VISIBLE
        }

        if (isPlan) {
            toolbar_btn.text = getString(R.string.kwd) + basePrice.toString().toDouble().toPrice()
            tvNonStopPrice.text =
                getString(R.string.kwd) + newNonStopPrice.toString().toDouble().toPrice()
            val overAllPrice = basePrice + carbPrice + proteinPrice + nonStopPrice + extrasPrice
            tvProteinPrice.text =
                getString(R.string.kwd) + proteinPrice.toString().toDouble().toPrice()
            tvCrabPrice.text = getString(R.string.kwd) + carbPrice.toString().toDouble().toPrice()
            tvTotalPrice.text =
                getString(R.string.kwd) + overAllPrice.toString().toDouble().toPrice()
        }

    }

    private fun setUpSubscriptionPlans(subscriptionPlans: List<SubscriptionPlan>) {
        duration = subscriptionPlans[0].duration.toString().toDouble().roundToInt()
        suspendInt = subscriptionPlans[0].suspend.toString().toDouble().roundToInt()
        basePrice = subscriptionPlans[0].price.toString().toDouble()
        toolbar_btn.text = getString(R.string.kwd) + basePrice.toString().toDouble().toPrice()
        setDates(planStartBuffer)
        setPrice()
//        subscriptionPlanId = subscriptionPlans[0].id
        rvPlanDuration.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPlanDuration.adapter = PlanAdapter(subscriptionPlans) { position ->
            isPlan = true
            duration = subscriptionPlans[position].duration.toInt()
            setupExtraMeals(extrasList)

            suspendInt = subscriptionPlans[position].suspend.toInt()
            basePrice = subscriptionPlans[position].price.toString().toDouble()
            newNonStopPrice = subscriptionPlans[position].non_stop_price.toString().toDouble()
            enable_modification = subscriptionPlans[position].enable_modification
            isSelectedDate = ""
            if (newNonStopPrice == 0.00) {
                isNonStop = false
                if (isSelectedDate == "Fri") {
                    isDate = false
                }
                setDates(planStartBuffer)
            } else {
                isNonStop = true
                setDates(planStartBuffer)
            }
            setPrice()
        }
    }

    private fun setUpMealPlanData(mealPlan: MealPlan) {
        tvMealPlanCategory.text = mealPlan.meal_category_name
        saveMealPlanCategory = mealPlan.meal_category_name
        CommonUtils.setImage(this, mealPlan.image, vpDetailsImage)
        tvDescriptionContent.text = mealPlan.description
        saveMealPlanDescription = mealPlan.description
        if (mealPlan.description.length < 80) {
            expandableDescription.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tvReadmore.visibility = View.GONE
        } else {
            tvReadmore.visibility = View.VISIBLE
            tvReadmore.text = getString(R.string.read_more)
            expandableDescription.layoutParams.height =
                resources.getDimensionPixelSize(R.dimen.margin_65dp)

        }


    }


    override fun onClicks() {
        ivShare.setOnClickListener {
            if (this::image.isInitialized) {
                val shareLink =
                    "$saveMealPlanCategory($mealPlanName) http://proteiniumkw.com"

                val i = Intent(Intent.ACTION_SEND)
                i.type = "*/*"
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(image))
                i.putExtra(Intent.EXTRA_TEXT, shareLink)
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(
                    Intent.createChooser(
                        i,
                        resources.getText(R.string.app_name)
                    )
                )

            } else {
                Toast.makeText(this, getString(R.string.error_image), Toast.LENGTH_SHORT).show()
            }


        }


        ivInfo.setOnClickListener {
            Log.e("123456", "onClicks: " + info)
            var html = "<!DOCTYPE  html>"
            html += "<body style=\"font-size:12px;font-family:file:///assets/font/segoe_ui.ttf; text-align: justify; line-height: 1.6;\">$info</body>"
            html += "</html>"
            val dialog = this.let { it1 -> Dialog(it1) }
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.alert_info_mealplan_detail)

            //val yesBtn = dialog?.findViewById(R.id.RatetheFoodSubmitBtn) as MaterialButton
            val closeBtn = dialog.findViewById(R.id.ivClose) as ImageView
            val description = dialog.findViewById(R.id.wbDescription) as TextView
            val btnOk = dialog.findViewById(R.id.btnOk) as Button
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                description.text =
                    Html.fromHtml(info, Html.FROM_HTML_MODE_COMPACT)
            } else {
                description.text = Html.fromHtml(info)
            }

            closeBtn.setOnClickListener {
                dialog.dismiss()
            }
            btnOk.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        contentDisikeBtn.setOnClickListener {
            startActivity(Intent(this, DislikesActivity::class.java))
        }


        btnRight.setOnClickListener {
            if (AppPreferences.isLogin) {
                if (dateModifiedList.size < start_date_range - planStartBuffer) {
                    if (currentMonth == 11) {
                        currentMonth = 0
                        calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
                    } else {

                        currentMonth++

                    }
                    tv_month.text = listMonths[currentMonth]
                    setDates(planStartBuffer)
                }

            }
        }

        btnLeft.setOnClickListener {
            if (AppPreferences.isLogin) {
                if (dateModifiedList.size < start_date_range - planStartBuffer) {

                    if (currentMonth == 0) {
                        currentMonth = 11
                        calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
                    } else {
                        currentMonth--
                        calendar.time = Date()
                        calendar.add(
                            Calendar.DAY_OF_YEAR,
                            planStartBuffer + 1
                        )
                        currentMonth = calendar[Calendar.MONTH]
                    }
                    tv_month.text = listMonths[currentMonth]
                    setDates(planStartBuffer)
                }
            }


        }
        btnRetry.setOnClickListener {
            planViewModel.fetchMealPlanResponse(mealPlanId, user_id!!)
        }
        contentProceedBtn.setOnClickListener {
            Log.e("check duration supend", "$duration-$suspendInt")
            if (!isPlan) {
                CommonUtils.warningToast(this, getString(R.string.select_duration))

            } else if (!isDate) {
                CommonUtils.warningToast(this, getString(R.string.select_start_date))


            } else {


                planViewModel.addInitialSubscriptionPlan(
                    user_id!!,
                    startDate,
                    mealPlanId,
                    nonStopPrice.toString(),
                    carbs,
                    carbPrice.toString(),
                    proteins,
                    proteinPrice.toString(),
                    "",
                    duration,
                    basePrice.toString(),
                    "",
                    suspendInt.toString(),
                    enable_modification.toInt(),
                    extrasArrayList
                )
            }


        }

        tvReadmore.setOnClickListener {
            Log.e("123456", "onClicks: " + expandableDescription.state)
            if (expandableDescription.state == State.Collapsed) {
                tvReadmore.text = getString(R.string.read_more)
                expandableDescription.layoutParams.height =
                    resources.getDimensionPixelSize(R.dimen.margin_65dp)
                expandableDescription.toggle()
            }
            if (expandableDescription.state == State.Expanded) {
                tvReadmore.text = getString(R.string.read_less)
                expandableDescription.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                expandableDescription.toggle()
            } else if (expandableDescription.state == State.Statical) {
                tvReadmore.text = getString(R.string.read_less)
                expandableDescription.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                expandableDescription.toggle()
            }
        }


//        }

    }

    private fun getLocalBitmapUri(resource: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            resource.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri =
                FileProvider.getUriForFile(applicationContext, "$packageName.provider", file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri!!
    }


    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        tv_month.text = listMonths[currentMonth]
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        tv_month.text = listMonths[currentMonth]
        return getDates(mutableListOf())
    }

    @SuppressLint("LongLogTag")
    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        return getDates(mutableListOf())
    }


    private fun getDates(list: MutableList<Date>): List<Date> {
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }


    private fun setDates(planStartBufferArg: Int) {
        dateList.clear()
        dateListNew.clear()
        dateModifiedList.clear()
        dateListNew.addAll(getFutureDatesOfCurrentMonth())
        dateList.addAll(getFutureDatesOfCurrentMonth())


        val today = Date()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, planStartBufferArg + 1)
        calendar.add(Calendar.YEAR, planStartBufferArg + 1)

        currentMonth = calendar[Calendar.MONTH]
//        tv_month.text = listMonths[currentMonth]
        val bufferDate = today.date + 2
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.scrollToPosition(0)
        dateList.forEach {
            if (DateUtils.getDayNumber(it) == DateUtils.getDayNumber(calendar.time) && DateUtils.getMonthNumber(
                    it
                ) == DateUtils.getMonthNumber(today)
            ) {
                dateModifiedList.add(
                    DateModal(
                        it.date,
                        isActive = false,
                        isDisabled = false
                    )
                )

            } else {
                if (DateUtils.getDayNumber(it) < DateUtils.getDayNumber(calendar.time) && DateUtils.getMonthNumber(
                        it
                    ) == DateUtils.getMonthNumber(calendar.time)
                ) {
                    dateListNew.remove(it)

                } else if (DateUtils.getMonthNumber(it) < DateUtils.getMonthNumber(today)) {
                    dateModifiedList.add(
                        DateModal(
                            it.date,
                            isActive = false,
                            isDisabled = false
                        )
                    )
                } else {
                    dateModifiedList.add(
                        DateModal(
                            it.date,
                            isActive = false,
                            isDisabled = false
                        )
                    )
                }

            }


        }
        rv_single_row_calendar.layoutManager = layoutManager
        rv_single_row_calendar.setHasFixedSize(true)
        var i = 0
        dateList.forEach {
            val calendar1 = Calendar.getInstance()
            calendar1.time = Date()
            calendar1.add(
                Calendar.DAY_OF_YEAR,
                start_date_range
            )
            if (it.after(calendar1.time)) {
                if (dateModifiedList.isNotEmpty()) {
                    dateModifiedList.removeAt(0)
                }

            }


        }
        val dateRemoveList = dateModifiedList.filter { it.isDisabled == false }
        daysOfMonthAdapter = DaysOfMonthAdapter(
            this,
            dateRemoveList as ArrayList<DateModal>,
            dateListNew,
            { date, position ->
                isDate = true
                isSelectedDate = DateUtils.getDay3LettersName(date).toString()
                if(isSelectedDate=="Fri"){
                    CommonUtils.warningToast(this,getString(R.string.firday_message))
                }
                setPrice()
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, start_date_range)
                if (date.before(calendar.time)) {
                    startDate =
                        DateUtils.getYear(date) + "-" + DateUtils.getMonthNumber(date) + "-" + DateUtils.getDayNumber(
                            date
                        )

                } else {
                    val limit = start_date_range - planStartBuffer
                    CommonUtils.warningToast(
                        this,
                        getString(R.string.welcome_messages, limit.toString())
                    )
                }

            }, {
                isDate = false
                CommonUtils.warningToast(this, getString(R.string.tue_disable))
            }, start_date_range, currentMonth, isNonStop, isRenewal,
            globalSuspension, {
                CommonUtils.warningToast(
                    this,
                    getString(R.string.holiday_message)
                )
            }
        )


        rv_single_row_calendar.adapter = daysOfMonthAdapter
        rv_single_row_calendar.setHasFixedSize(true)

    }


}
