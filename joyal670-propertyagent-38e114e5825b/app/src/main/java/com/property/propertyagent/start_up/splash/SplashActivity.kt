package com.property.propertyagent.start_up.splash

import android.content.Intent
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.home.HomeActivity
import com.property.propertyagent.base.new.BaseActivity1
import com.property.propertyagent.databinding.ActivitySplashBinding
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
import com.property.propertyagent.start_up.auth.activity.AuthActivity
import com.property.propertyagent.start_up.welcome_slider.WelcomeSliderActivity
import com.property.propertyagent.utils.AppPreferences
import com.property.propertyagent.utils.AppPreferences.isVisited
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.Constants
import render.animations.*
import java.util.*

class SplashActivity : BaseActivity1<ActivitySplashBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = true

    override fun fragmentLaunch() {

    }

    override fun getViewBinging(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun initData() {
        if (AppPreferences.user_lang == Constants.ARABIC_LAG) {
            CommonUtils.changeLanguageAwareContext(this, Constants.ARABIC_LAG)
        } else {
            CommonUtils.changeLanguageAwareContext(this, Constants.ENGLISH_LAG)
        }
    }

    private fun navigateActivity() {
        if (AppPreferences.isLogin) {
            if (AppPreferences.login_type == "1") {
                startActivity(Intent(this, HomeOwnerActivity::class.java))
                finishAffinity()
            } else if (AppPreferences.login_type == "0") {
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()
            }
        } else {
            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            finish()
        }
    }

    override fun setupUI() {
        /* for animation */
        val render1 = Render(this)
        render1.setAnimation(Fade().In(binding.splashImage2))
        render1.setDuration(2000)
        render1.start()

        val render2 = Render(this)
        render2.setAnimation(Zoom().In(binding.splashImage1))
        render2.setAnimation(Zoom().In(binding.splashImage3))
        render2.setDuration(1500)
        render2.start()

        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (isVisited) {
                    navigateActivity()
                } else {
                    startActivity(Intent(this@SplashActivity, WelcomeSliderActivity::class.java))
                    finish()
                }
            }
        }, 3000)
    }

    override fun setupViewModel() {

    }

    override fun setupObserver() {

    }

    override fun onClicks() {

    }
}