package com.proteinium.proteiniumdietapp.ui.main.home.home.home_fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.base.BaseFragment
import com.proteinium.proteiniumdietapp.listeners.ActivityListener
import com.proteinium.proteiniumdietapp.pojo.home.Banner
import com.proteinium.proteiniumdietapp.pojo.home.MealCategory
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.preference.AppPreferences.isLogin
import com.proteinium.proteiniumdietapp.preference.AppPreferences.isNotification
import com.proteinium.proteiniumdietapp.ui.main.home.HomeActivity
import com.proteinium.proteiniumdietapp.ui.main.home.home.home_fragment.adapter.CategoryItemListAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.home.home_fragment.adapter.HomeImageSliderAdapter
import com.proteinium.proteiniumdietapp.ui.main.home.home.notification.NotificationActivity
import com.proteinium.proteiniumdietapp.ui.start_up.auth.AuthActivity
import com.proteinium.proteiniumdietapp.ui.start_up.language_selection.LangaugeSelectionActivity
import com.proteinium.proteiniumdietapp.utils.CommonUtils
import com.proteinium.proteiniumdietapp.utils.Status
import com.proteinium.proteiniumdietapp.utils.isConnected
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.no_internet.*
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {
    private lateinit var fragmentTransInterface: ActivityListener
    private var homeSliderImages = ArrayList<Banner>()
    private lateinit var homeViewModel: HomeViewModel
    private var currentPage = 0
    private val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed
    private val PERIOD_MS: Long = 3000
    private lateinit var handler: Handler
    private lateinit var timer: Timer
    private lateinit var runnable: Runnable

    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun initData() {
        fragmentTransInterface = activity as HomeActivity
        fragmentTransInterface.setTitle(getString(R.string.app_name), 16f, R.font.segoe_ui, true)
        fragmentTransInterface.setBackButton(false)
        fragmentTransInterface.hideToolbar(true)
        swipeToRefresh.setColorSchemeColors(Color.WHITE)
        swipeToRefresh.setProgressBackgroundColorSchemeColor(Color.rgb(0, 165, 165))
        homeSliderImages.clear()
        swipeToRefresh.setOnRefreshListener {
            homeViewModel.fetchHomeDetails()
            swipeToRefresh.isRefreshing = false
        }
        homeViewModel.fetchHomeDetails()
        setHasOptionsMenu(true)
    }

    private fun setHomeImageSlider() {
        if(this::handler.isInitialized){
            currentPage = 0
            handler.removeCallbacks(runnable)
        }
        if(this::timer.isInitialized){
            currentPage = 0
            timer.cancel()
        }
        vpHomeImageSlider.adapter = context?.let { HomeImageSliderAdapter(it, homeSliderImages) }
        handler = Handler()
        runnable = Runnable {
            if (currentPage ==homeSliderImages.size) {
                currentPage = 0
            }
            vpHomeImageSlider.setCurrentItem(currentPage++, true)
        }

        timer = Timer() // This will create a new Thread

        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(runnable)
            }
        }, DELAY_MS, PERIOD_MS)
        dotsIndicatorFloor.setViewPager2(vpHomeImageSlider)
    }

    private fun setHomeCategoryItems(mealCategoryList: ArrayList<MealCategory>) {
        val layoutManager=LinearLayoutManager(context)
        rvHomeCategoryItems.layoutManager = layoutManager
        rvHomeCategoryItems.adapter = context?.let { CategoryItemListAdapter(it, mealCategoryList) { position->

        }
        }


    }

    override fun onPause() {
        super.onPause()
        if(this::handler.isInitialized){
            handler.removeCallbacks(runnable)
        }
        if(this::timer.isInitialized){
            timer.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!AppPreferences.isNotificationRead) {
            viewNotification.visibility = View.GONE
            tvNotificationCount.visibility =  View.GONE
        }
    }

    override fun setupUI() {

    }


    override fun setupViewModel() {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun setupObserver() {
        homeViewModel.getHomeResponse().observe(this, Observer {
            when (it.status) {

                Status.SUCCESS -> {
                    if(!AppPreferences.isCalendar){
                        dismissLoader()
                    }
                    if(it.data!!.blocked == 1){
                        showExitDialog()
                    }
                    AppPreferences.isCalendar=false
                    noInternetLayout.visibility = View.GONE
                    mainLayout.visibility = View.VISIBLE
                    if (it.data?.status!!) {
                        if (!it.data.data.banners.isNullOrEmpty()) {
                            homeSliderImages = it.data.data.banners as ArrayList<Banner>
                            setHomeImageSlider()
                        }
                        isNotification = it.data.data.notification_count
                        if (isNotification != 0) {
                            AppPreferences.isNotificationRead=true
                            viewNotification.visibility = View.VISIBLE
                            tvNotificationCount.visibility = View.VISIBLE
                            tvNotificationCount.text = isNotification.toString()
                        }
                        if (!it.data.data.meal_categories.isNullOrEmpty()) {
                            setHomeCategoryItems(it.data.data.meal_categories as ArrayList<MealCategory>)
                        }
                    }
                }
                Status.ERROR -> {
                    if(!AppPreferences.isCalendar){
                        dismissLoader()
                    }
                    AppPreferences.isCalendar=false
                    if (requireActivity().isConnected) {
                        CommonUtils.warningToast(requireContext(), getString(R.string.something_wrong))
                    } else {
                        noInternetLayout.visibility = View.VISIBLE
                        mainLayout.visibility = View.GONE
                    }

                }
                Status.LOADING -> {
                    if(!AppPreferences.isCalendar){
                        showLoader()
                    }
                }

                Status.DATA_EMPTY -> {
                    if(!AppPreferences.isCalendar){
                        dismissLoader()
                    }
                    AppPreferences.isCalendar=false
                    CommonUtils.warningToast(requireContext(), getString(R.string.data_empty))
                }
            }
        })
    }


    private fun showExitDialog() {
        val dialog = context?.let { it1 -> Dialog(it1) }
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.alert_remove_an_item)

        val yesBtn = dialog?.findViewById(R.id.RemoveItemOkBtn) as MaterialButton
        val closeBtn = dialog.findViewById(R.id.ivRemoveItemClose) as ImageView
        val tvContent = dialog.findViewById(R.id.tvContent) as TextView
        tvContent.text = requireActivity().getString(R.string.block_message)

        yesBtn.setOnClickListener {
            dialog.dismiss()
            AppPreferences.logoutClearPreference()
            val intent= Intent(requireContext(), LangaugeSelectionActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
            AppPreferences.logoutClearPreference()
            val intent= Intent(requireContext(), LangaugeSelectionActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        dialog.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onClicks() {
        ivNotification.setOnClickListener {
            if (isLogin) {
                startActivity(Intent(requireContext(), NotificationActivity::class.java))
            } else {
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            }

        }
        btnRetry.setOnClickListener {
            homeViewModel.fetchHomeDetails()
        }
    }


}