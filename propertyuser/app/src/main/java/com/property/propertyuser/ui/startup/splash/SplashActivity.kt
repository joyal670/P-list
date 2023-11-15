package com.property.propertyuser.ui.startup.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivitySplashBinding
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.preference.AppPreferences.isLocationLoaded
import com.property.propertyuser.ui.main.home.dashboard.DashboardActivity
import com.property.propertyuser.ui.startup.auth.AuthActivity
import com.property.propertyuser.ui.startup.welcome_slider.WelcomeSliderActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getViewBinging(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override val layoutId: Int
        get() = R.layout.activity_splash
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
    }

    override fun initData() {
        isLocationLoaded = false
        if (AppPreferences.chooseLanguage.isNullOrEmpty()) {
            AppPreferences.chooseLanguage = "english"
        }
        Handler(Looper.getMainLooper()).postDelayed({
            setUpApplication()
        }, 3000)
    }

    private fun setUpApplication() {
        Log.e("TAG", "setUpApplication: dsf" + AppPreferences.isFirstTime)
        if (AppPreferences.isFirstTime) {
            startActivity(Intent(this@SplashActivity, WelcomeSliderActivity::class.java))
            finish()
        } else if (AppPreferences.isLogin) {
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            finish()
        } else if (!AppPreferences.isLogin) {
            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            finish()
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }

}