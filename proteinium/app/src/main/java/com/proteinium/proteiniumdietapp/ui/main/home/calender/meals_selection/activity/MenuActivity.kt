package com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseActivity
import com.proteinium.proteiniumdietapp.pojo.menu_day.MealLimit
import com.proteinium.proteiniumdietapp.pojo.menu_day.MealType
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.chooseLanguage
import com.proteinium.proteiniumdietapp.preference.AppPreferences.isLogin
import com.proteinium.proteiniumdietapp.preference.AppPreferences.isPlan
import com.proteinium.proteiniumdietapp.preference.AppPreferences.user_id
import com.proteinium.proteiniumdietapp.ui.main.home.calender.meals_selection.adapter.ViewPagerAdapter
import com.proteinium.proteiniumdietapp.ui.start_up.auth.AuthActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Constants
import com.proteinium.proteiniumdietapp.utils.Constants.INTENT_TYPE
import com.proteinium.proteiniumdietapp.utils.Constants.TYPE_GUEST
import com.proteinium.proteiniumdietapp.utils.Status.*
import kotlinx.android.synthetic.main.activity_meals_selection.*
import kotlinx.android.synthetic.main.no_subscrption.*
import kotlinx.android.synthetic.main.toolbar_sub.*
import kotlin.math.roundToInt

class MenuActivity : BaseActivity() {
    private lateinit var menuViewModel: MenuViewModel
    private var menuDate: String = ""
    private val foodIdList: ArrayList<Int> = ArrayList<Int>()
    private val mealTypeList: ArrayList<String> = ArrayList<String>()
    private val mealQuantityList: ArrayList<Int> = ArrayList<Int>()
    var foodHashMap: HashMap<Int, Int> = HashMap<Int, Int>()
    private var mealPlanSubscriptioId = ""
    private var totalQuantity = 0.0
    private lateinit var adapter: ViewPagerAdapter
    private var mealList: ArrayList<MealType> = ArrayList<MealType>()
    private var isLoded = false
    private var proteinUser = 0
    private var carbUser = 0
    private var proteinNutrition=0.0
    private var carbNutrition=0.0
    private var fatNutrition=0.0
    private var caloriesNutrition=0.0
    private var planCarb=0.0


    override val layoutId: Int
        get() = R.layout.activity_meals_selection
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
        tvSubToolbarTitle.text = getString(R.string.meals_selecton)
        setDate(intent.getStringExtra(Constants.MEAL_SELECTED_DATE))
        mealPlanSubscriptioId = intent.getStringExtra("passedId").toString()
        if (AppPreferences.isPlan) {
            menuViewModel.fetchMenu(user_id!!, menuDate)
        } else {
            menuViewModel.fetchMenu(0, menuDate)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.info_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.overflowMenu -> startActivity(Intent(this,InfoActivity::class.java))

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDate(date: String?) {
        tvmealsDate.text = date
        menuDate = date.toString()
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        menuViewModel = MenuViewModel()
    }

    override fun onResume() {
        super.onResume()


    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setupObserver() {
        menuViewModel.getMenuResponse().observe(this, Observer {
            when (it.status) {
                SUCCESS -> {

                    dismissLoader()
                    mailLayout.visibility = View.VISIBLE
                    noMenu.visibility = View.GONE
                    meals_tabs!!.setupWithViewPager(meals_viewPager)
                    proteinUser=it.data!!.data.proteins
                    carbUser= it.data.data.carbs
                    try {
                        planCarb = it.data.data.plan_carb.toDouble()
                    }catch(ex:Exception){

                    }
                    if (it.data!!.data.meal_plan_limits.size <= 3) {
                        meals_tabs.tabMode = TabLayout.MODE_FIXED
                    } else {
                        meals_tabs.tabMode = TabLayout.MODE_SCROLLABLE
                    }
                    mealTypeList.clear()
                    foodIdList.clear()
                    val combinationList: ArrayList<MealLimit> = ArrayList<MealLimit>()
                    it.data!!.data.meal_type_id_combinations.forEach { (combination1, combination2) ->
                        combinationList.add(MealLimit(combination1, combination2))
                    }
                    it.data!!.data.meal_plan_limits.forEach {
                        mealTypeList.add(it.meal_type_id)
                    }
                    it.data!!.data.meal_plan_limits.forEach { mealPlanLimt ->
                        totalQuantity += mealPlanLimt.quantity.toInt()
                    }

                    val dislike = ArrayList<Int>()
                    it.data.data.disliked_tags.forEach { tag ->
                        dislike.add(tag.tag_id)
                    }
                    mealList.clear()
                    if (AppPreferences.isLogin) {
                        if (AppPreferences.isPlan) {
                            it.data.data.meal_plan_limits.forEach { meal ->
                                it.data.data.meal_types.forEach { type ->
                                    if (meal.meal_type_id.toInt() == type.id) {
                                        mealList.add(type)
                                    }
                                }
                            }
                        } else {
                            mealList.addAll(it.data.data.meal_types)
                        }

                    } else {
                        mealList.addAll(it.data.data.meal_types)
                    }


                    adapter = ViewPagerAdapter(
                        supportFragmentManager,
                        mealList,
                        { i: Int, i1: Int, i2: Int ->
                            if (i1 == 0) {
                                foodIdList.remove(i)
                                callCarbsProtein()
                            }
                            if (i1 == 1) {
                                foodIdList.add(i)
                                callCarbsProtein()
                            }


                        },
                        mealTypeList,
                        it.data!!.data.meal_plan_limits, { sQty: Int, tQty: Int ->
                            if (AppPreferences.isLogin) {
                                if (isPlan) {
                                    llSelected.visibility = View.VISIBLE
                                    val qty = StringBuilder()
                                    if (chooseLanguage == Constants.ARABIC_LAG) {
                                        qty.append(tQty)
                                        qty.append("/")
                                        qty.append(sQty)
                                    } else {
                                        qty.append(sQty)
                                        qty.append("/")
                                        qty.append(tQty)
                                    }

                                    tvSelected.text = qty.toString()
                                }

                            }

                        },
                        dislike,
                        combinationList,
                        it.data!!.data.extras
                    )
                    callCarbsProtein()
                    meals_viewPager.adapter = adapter

                }
                ERROR -> dismissLoader()
                LOADING -> showLoader()
                NO_INTERNET -> dismissLoader()
                DATA_EMPTY -> {
                    dismissLoader()
                    mailLayout.visibility = View.GONE
                    noMenu.visibility = View.VISIBLE
                    tvNoSubscription.text = it.message.toString()
                }
            }
        })
        menuViewModel.placeOrderResponse().observe(this, Observer {
            when (it.status) {
                SUCCESS -> {
                    dismissLoader()
                    showSuccessMessage(it.data!!.message!!)
                }
                DATA_EMPTY -> {
                    dismissLoader()
                    CommonUtils.warningToast(this, it.message!!)
                }
            }
        })
    }

    private fun callCarbsProtein() {
        proteinNutrition =0.0
        carbNutrition= 0.0
        fatNutrition =0.0
        caloriesNutrition=0.0
        mealList.forEach {
            val proteinFactor=proteinUser-100
            it.foods.forEach {
                if (it.ordered_status) {
                    if(it.nutrition_include){
                        val proteinFactor=((proteinUser.toFloat())/100).toFloat()
                        proteinNutrition += it.proteins*proteinFactor
                        carbNutrition+=it.carbs*proteinFactor
                        fatNutrition+=it.fat*proteinFactor
                        if(carbUser<planCarb){
                            val carbFactor=(planCarb-carbUser)/50
                            proteinNutrition -= carbFactor
                            carbNutrition-=carbFactor*12
                            fatNutrition-=carbFactor*0.5
                        }else{
                            val carbFactor=(carbUser-planCarb)/50
                            proteinNutrition += carbFactor
                            carbNutrition+=carbFactor*12
                            fatNutrition+=carbFactor*0.5
                        }
                    }else{

                        proteinNutrition += it.proteins
                        carbNutrition+=it.carbs
                        fatNutrition+=it.fat
                    }



                }

            }

        }
        Log.e("#12345", "callCarbsProtein: $proteinNutrition", )
        tvGramCarb.text=proteinNutrition.toFloat().toString()
        tvGramProtein.text=carbNutrition.toFloat().toString()
        tvGramFat.text=fatNutrition.toFloat().toString()
        val sumFat=fatNutrition*9
        val sumProt=proteinNutrition*4
        val sumCarb=carbNutrition*4
        val totalCalories=sumFat+sumProt+sumCarb
        tvGramCalories.text=totalCalories.toFloat().toString()


    }

    private fun showSuccessMessage(message: String) {
        val dialog = Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent = dialog?.findViewById(R.id.tvContent) as TextView
        tvContent.text = message

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
        btnSave.setOnClickListener {
            if (isLogin) {
                if (isPlan) {
                    foodIdList.clear()
                    mealList.forEach {
                        it.foods.forEach { it2 ->
                            if (it2.ordered_status) {
                                foodIdList.add(it2.id)
                            }
                        }
                    }

                    foodIdList.forEach {
                        Log.e("8899", "onClicks: ${it.toString()}")
                    }
                    if (foodIdList.size < totalQuantity) {
                        showWarningItemDialog()
                    } else {
                        menuViewModel.placeOrder(
                            user_id!!,
                            menuDate,
                            foodIdList,
                            mealPlanSubscriptioId
                        )
                    }
                } else {
                    CommonUtils.warningToast(this, getString(R.string.subscribe_plan))
                }


            } else {
                val intent = Intent(this, AuthActivity::class.java)
                intent.putExtra(INTENT_TYPE, TYPE_GUEST)
                startActivity(intent)
            }

        }
    }

    private fun showWarningItemDialog() {
        val dialog = Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog?.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent = dialog?.findViewById(R.id.tvContent) as TextView
        tvContent.text = getString(R.string.please_select_food)

        yesBtn.setOnClickListener {
            dialog.dismiss()
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

}
