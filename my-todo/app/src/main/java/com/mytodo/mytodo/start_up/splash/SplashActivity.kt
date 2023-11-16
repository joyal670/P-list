package com.mytodo.mytodo.start_up.splash

import android.content.Intent
import com.mytodo.mytodo.R
import com.mytodo.mytodo.base.BaseActivity
import com.mytodo.mytodo.databinding.ActivitySplashBinding
import com.mytodo.mytodo.start_up.welcome_slider.activity.WelcomeSliderActivity
import java.util.*

class SplashActivity : BaseActivity<ActivitySplashBinding>()
{
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = true

    override fun getViewBinging(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun initData() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                startActivity(Intent(this@SplashActivity , WelcomeSliderActivity::class.java))
                finish()
            }
        } , 3000)
    }

    override fun fragmentLaunch() {
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