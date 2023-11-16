package com.iroid.jeetmeet.start_up.splash

import android.content.Intent
import com.iroid.jeetmeet.R
import com.iroid.jeetmeet.base.BaseActivity
import com.iroid.jeetmeet.databinding.ActivitySplashBinding
import com.iroid.jeetmeet.start_up.auth.activity.AuthActivity
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


    override fun initData()
    {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                    startActivity(Intent(this@SplashActivity , AuthActivity::class.java))
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